#!/bin/bash
# 检查所有ERP表的缺失字段
# 使用方法: ./scripts/check-missing-fields.sh

set -e

SSH_KEY="/Users/xierui/Documents/Project/Other/erp-system/tengxunyun.pem"
SERVER="ubuntu@101.33.244.240"
DB_NAME="ruoyi-vue-pro"

echo "=========================================="
echo "  检查ERP表缺失字段"
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

# 定义需要检查的表和字段
declare -A TABLE_FIELDS

# erp_sale_order 表字段
TABLE_FIELDS["erp_sale_order"]="gross_profit_rate material_cost labor_cost total_cost"

# erp_sale_order_items 表字段
TABLE_FIELDS["erp_sale_order_items"]="gross_profit_rate material_cost labor_cost"

# erp_product_bom_item 表字段
TABLE_FIELDS["erp_product_bom_item"]="process_name"

echo "正在检查表字段..."
echo ""

MISSING_FIELDS_FOUND=false

for table in "${!TABLE_FIELDS[@]}"; do
    echo "检查表: $table"
    fields="${TABLE_FIELDS[$table]}"
    
    for field in $fields; do
        # 检查字段是否存在
        result=$(ssh -i "$SSH_KEY" "$SERVER" "docker exec 524b4ac44aad_yudao-mysql-prod mysql -uroot -p$MYSQL_PASSWORD $DB_NAME -e \"SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='$DB_NAME' AND TABLE_NAME='$table' AND COLUMN_NAME='$field';\" 2>&1 | grep -v Warning | tail -1" 2>/dev/null || echo "0")
        
        if [ "$result" = "0" ]; then
            echo "  ❌ 缺失字段: $field"
            MISSING_FIELDS_FOUND=true
        else
            echo "  ✅ 字段存在: $field"
        fi
    done
    echo ""
done

if [ "$MISSING_FIELDS_FOUND" = true ]; then
    echo "=========================================="
    echo "  发现缺失字段，需要修复"
    echo "=========================================="
    exit 1
else
    echo "=========================================="
    echo "  ✅ 所有字段检查通过"
    echo "=========================================="
    exit 0
fi

