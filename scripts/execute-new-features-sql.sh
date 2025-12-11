#!/bin/bash
# 执行新功能SQL脚本
# 用于创建财务模块、内部出入库和销售订单字段扩展的表

set -e

# 配置参数
DB_NAME="${1:-ruoyi-vue-pro}"
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-123456}"
MYSQL_CMD="mysql -u${DB_USER} -p${DB_PASSWORD}"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}执行新功能SQL脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo "数据库: ${DB_NAME}"
echo "用户: ${DB_USER}"
echo ""

# 检查MySQL是否运行
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}错误: MySQL命令未找到，请确保MySQL客户端已安装${NC}"
    exit 1
fi

# 检查数据库连接
echo -e "${BLUE}[1/4] 检查数据库连接...${NC}"
if $MYSQL_CMD -e "USE \`${DB_NAME}\`;" 2>/dev/null; then
    echo -e "${GREEN}✓ 数据库连接成功${NC}"
else
    echo -e "${RED}✗ 数据库连接失败${NC}"
    echo "请检查："
    echo "  1. MySQL服务是否运行"
    echo "  2. 数据库名称是否正确: ${DB_NAME}"
    echo "  3. 用户名和密码是否正确"
    exit 1
fi
echo ""

# SQL脚本列表
SQL_SCRIPTS=(
    "sql/mysql/erp-finance-tables.sql:财务模块表"
    "sql/mysql/erp-stock-internal-tables.sql:内部出入库表"
    "sql/mysql/erp-sale-order-gross-profit-fields.sql:销售订单字段扩展"
)

# 执行SQL脚本的函数
execute_sql() {
    local sql_file=$1
    local description=$2

    echo -e "${BLUE}[执行] ${description}${NC}"
    echo "  文件: ${sql_file}"

    if [ ! -f "${sql_file}" ]; then
        echo -e "  ${RED}✗ 文件不存在${NC}"
        return 1
    fi

    if $MYSQL_CMD "${DB_NAME}" < "${sql_file}" 2>/dev/null; then
        echo -e "  ${GREEN}✓ 执行成功${NC}"
        return 0
    else
        echo -e "  ${RED}✗ 执行失败${NC}"
        return 1
    fi
}

# 执行所有SQL脚本
echo -e "${BLUE}[2/4] 执行SQL脚本...${NC}"
echo ""

FAILED_SCRIPTS=()
for script_info in "${SQL_SCRIPTS[@]}"; do
    IFS=':' read -r sql_file description <<< "$script_info"
    if ! execute_sql "${sql_file}" "${description}"; then
        FAILED_SCRIPTS+=("${sql_file}")
    fi
    echo ""
done

# 检查执行结果
if [ ${#FAILED_SCRIPTS[@]} -gt 0 ]; then
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}部分脚本执行失败${NC}"
    echo -e "${RED}========================================${NC}"
    for script in "${FAILED_SCRIPTS[@]}"; do
        echo "  - ${script}"
    done
    exit 1
fi

# 验证表是否创建成功
echo -e "${BLUE}[3/4] 验证表是否创建成功...${NC}"

# 财务模块表
FINANCE_TABLES=(
    "erp_finance_balance_sheet"
    "erp_finance_receivable"
    "erp_finance_payable"
    "erp_finance_prepayment"
    "erp_finance_prereceipt"
    "erp_finance_profit_statement"
)

# 内部出入库表
STOCK_INTERNAL_TABLES=(
    "erp_stock_internal_in"
    "erp_stock_internal_in_item"
    "erp_stock_internal_out"
    "erp_stock_internal_out_item"
)

ALL_TABLES=("${FINANCE_TABLES[@]}" "${STOCK_INTERNAL_TABLES[@]}")

MISSING_TABLES=()
for table in "${ALL_TABLES[@]}"; do
    if $MYSQL_CMD "${DB_NAME}" -e "SHOW TABLES LIKE '${table}';" 2>/dev/null | grep -q "${table}"; then
        echo -e "  ${GREEN}✓${NC} ${table}"
    else
        echo -e "  ${RED}✗${NC} ${table} (未创建)"
        MISSING_TABLES+=("${table}")
    fi
done
echo ""

# 验证销售订单表字段
echo -e "${BLUE}[4/4] 验证销售订单表字段扩展...${NC}"
SALE_ORDER_FIELDS=(
    "gross_profit_rate"
    "material_cost"
    "labor_cost"
    "total_cost"
)

MISSING_FIELDS=()
for field in "${SALE_ORDER_FIELDS[@]}"; do
    if $MYSQL_CMD "${DB_NAME}" -e "SHOW COLUMNS FROM erp_sale_order LIKE '${field}';" 2>/dev/null | grep -q "${field}"; then
        echo -e "  ${GREEN}✓${NC} erp_sale_order.${field}"
    else
        echo -e "  ${RED}✗${NC} erp_sale_order.${field} (未创建)"
        MISSING_FIELDS+=("${field}")
    fi
done
echo ""

# 总结
echo -e "${GREEN}========================================${NC}"
if [ ${#MISSING_TABLES[@]} -eq 0 ] && [ ${#MISSING_FIELDS[@]} -eq 0 ]; then
    echo -e "${GREEN}✓ 所有表和字段创建成功！${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo "下一步:"
    echo "  1. 运行代码生成脚本: ./scripts/generate-new-features-code.sh"
    echo "  2. 配置代码生成选项（模块名、业务名、类名等）"
    echo "  3. 下载生成的代码并部署"
else
    echo -e "${YELLOW}⚠ 部分表或字段未创建成功${NC}"
    echo -e "${GREEN}========================================${NC}"
    if [ ${#MISSING_TABLES[@]} -gt 0 ]; then
        echo "未创建的表:"
        for table in "${MISSING_TABLES[@]}"; do
            echo "  - ${table}"
        done
    fi
    if [ ${#MISSING_FIELDS[@]} -gt 0 ]; then
        echo "未创建的字段:"
        for field in "${MISSING_FIELDS[@]}"; do
            echo "  - erp_sale_order.${field}"
        done
    fi
    exit 1
fi

