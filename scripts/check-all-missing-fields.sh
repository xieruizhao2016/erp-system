#!/bin/bash
# 全面检查所有ERP表的缺失字段
# 基于DO类定义和数据库实际字段对比

set -e

SSH_KEY="/Users/xierui/Documents/Project/Other/erp-system/tengxunyun.pem"
SERVER="ubuntu@101.33.244.240"
DB_NAME="ruoyi-vue-pro"

echo "=========================================="
echo "  全面检查ERP表缺失字段"
echo "=========================================="
echo ""

# 获取MySQL密码
echo "正在连接服务器..."
MYSQL_PASSWORD=$(ssh -i "$SSH_KEY" "$SERVER" "grep MYSQL_ROOT_PASSWORD /opt/erp-system/script/docker/.env | cut -d'=' -f2" 2>/dev/null || echo "")

if [ -z "$MYSQL_PASSWORD" ]; then
    echo "❌ 无法获取MySQL密码"
    exit 1
fi

echo "✅ 已连接到服务器"
echo ""

# 定义需要检查的表和字段（基于DO类定义）
declare -A TABLE_FIELDS

# erp_sale_order 表字段（基于 ErpSaleOrderDO.java）
TABLE_FIELDS["erp_sale_order"]="gross_profit_rate material_cost labor_cost total_cost"

# erp_sale_order_items 表字段（基于 ErpSaleOrderItemDO.java）
TABLE_FIELDS["erp_sale_order_items"]="gross_profit_rate material_cost labor_cost"

# erp_product_bom_item 表字段（基于 ProductBomItemDO.java）
TABLE_FIELDS["erp_product_bom_item"]="process_name"

echo "正在检查表字段..."
echo ""

MISSING_FIELDS_FOUND=false
MISSING_FIELDS_SQL=""

for table in "${!TABLE_FIELDS[@]}"; do
    echo "检查表: $table"
    fields="${TABLE_FIELDS[$table]}"
    
    for field in $fields; do
        # 检查字段是否存在
        result=$(ssh -i "$SSH_KEY" "$SERVER" "docker exec 524b4ac44aad_yudao-mysql-prod mysql -uroot -p$MYSQL_PASSWORD $DB_NAME -e \"SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='$DB_NAME' AND TABLE_NAME='$table' AND COLUMN_NAME='$field';\" 2>&1 | grep -v Warning | tail -1" 2>/dev/null || echo "0")
        
        if [ "$result" = "0" ]; then
            echo "  ❌ 缺失字段: $field"
            MISSING_FIELDS_FOUND=true
            
            # 根据字段类型生成SQL
            case "$field" in
                "gross_profit_rate")
                    if [ "$table" = "erp_sale_order" ]; then
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '毛利率（百分比）' AFTER \`total_price\`;\n"
                    else
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '行毛利率（百分比）' AFTER \`tax_price\`;\n"
                    fi
                    ;;
                "material_cost")
                    if [ "$table" = "erp_sale_order" ]; then
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '原材料成本，单位：元' AFTER \`gross_profit_rate\`;\n"
                    else
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '行原材料成本，单位：元' AFTER \`gross_profit_rate\`;\n"
                    fi
                    ;;
                "labor_cost")
                    if [ "$table" = "erp_sale_order" ]; then
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '员工成本，单位：元' AFTER \`material_cost\`;\n"
                    else
                        MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '行员工成本，单位：元' AFTER \`material_cost\`;\n"
                    fi
                    ;;
                "total_cost")
                    MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` decimal(20,2) NULL COMMENT '总成本，单位：元（total_cost = material_cost + labor_cost）' AFTER \`labor_cost\`;\n"
                    ;;
                "process_name")
                    MISSING_FIELDS_SQL="${MISSING_FIELDS_SQL}ALTER TABLE \`$table\` ADD COLUMN \`$field\` varchar(255) NULL COMMENT '工序名称（冗余字段，用于列表显示，避免关联查询）' AFTER \`process_id\`;\n"
                    ;;
            esac
        else
            echo "  ✅ 字段存在: $field"
        fi
    done
    echo ""
done

if [ "$MISSING_FIELDS_FOUND" = true ]; then
    echo "=========================================="
    echo "  发现缺失字段！"
    echo "=========================================="
    echo ""
    echo "修复SQL脚本："
    echo "----------------------------------------"
    echo -e "$MISSING_FIELDS_SQL"
    echo "----------------------------------------"
    echo ""
    echo "是否执行修复？(y/n)"
    read -r answer
    if [ "$answer" = "y" ] || [ "$answer" = "Y" ]; then
        echo "正在执行修复..."
        # 将SQL写入临时文件并执行
        echo -e "$MISSING_FIELDS_SQL" | ssh -i "$SSH_KEY" "$SERVER" "docker exec -i 524b4ac44aad_yudao-mysql-prod mysql -uroot -p$MYSQL_PASSWORD $DB_NAME 2>&1 | grep -v Warning"
        echo ""
        echo "✅ 修复完成！请重启后端服务。"
    fi
    exit 1
else
    echo "=========================================="
    echo "  ✅ 所有字段检查通过"
    echo "=========================================="
    exit 0
fi

