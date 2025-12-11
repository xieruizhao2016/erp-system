#!/bin/bash
# 新功能代码生成脚本
# 用于生成财务模块和内部出入库模块的代码
#
# 使用方法:
#   1. 确保已执行SQL脚本创建表
#   2. 确保后端服务运行并获取JWT Token
#   3. 运行: ./scripts/generate-new-features-code.sh

set -e

# 配置参数
BASE_URL="${BASE_URL:-http://localhost:48080/admin-api}"
TENANT_ID="${TENANT_ID:-1}"
DATA_SOURCE_ID="${DATA_SOURCE_ID:-1}"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}新功能代码生成脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 检查依赖
if ! command -v jq &> /dev/null; then
    echo -e "${RED}错误: jq 未安装，请先安装 jq${NC}"
    echo "macOS: brew install jq"
    echo "Ubuntu: sudo apt-get install jq"
    exit 1
fi

if ! command -v curl &> /dev/null; then
    echo -e "${RED}错误: curl 未安装${NC}"
    exit 1
fi

# 获取Token
if [ -f ".jwt-token" ]; then
    TOKEN=$(cat .jwt-token)
    echo -e "${GREEN}✓ 已加载JWT Token${NC}"
else
    echo -e "${YELLOW}警告: .jwt-token 文件不存在${NC}"
    echo "正在获取JWT Token..."
    ./scripts/get-jwt-token.sh admin admin123 "${BASE_URL}" "${TENANT_ID}"
    if [ -f ".jwt-token" ]; then
        TOKEN=$(cat .jwt-token)
        echo -e "${GREEN}✓ Token获取成功${NC}"
    else
        echo -e "${RED}✗ Token获取失败${NC}"
        exit 1
    fi
fi
echo ""

# 财务模块表列表
FINANCE_TABLES=(
    "erp_finance_balance_sheet"
    "erp_finance_receivable"
    "erp_finance_payable"
    "erp_finance_prepayment"
    "erp_finance_prereceipt"
    "erp_finance_profit_statement"
)

# 内部出入库表列表（只生成主表，明细表作为子表处理）
STOCK_INTERNAL_TABLES=(
    "erp_stock_internal_in"
    "erp_stock_internal_out"
)

# 所有需要生成的表
ALL_TABLES=("${FINANCE_TABLES[@]}" "${STOCK_INTERNAL_TABLES[@]}")

echo -e "${BLUE}需要生成的表列表:${NC}"
for table in "${ALL_TABLES[@]}"; do
    echo "  - ${table}"
done
echo ""

# 步骤1: 检查数据库表是否存在
echo -e "${GREEN}[1/4] 检查数据库表是否存在...${NC}"
MISSING_TABLES=()
for table in "${ALL_TABLES[@]}"; do
    RESPONSE=$(curl -s -X GET "${BASE_URL}/infra/codegen/db/table/list?dataSourceConfigId=${DATA_SOURCE_ID}&name=${table}" \
      -H "Authorization: Bearer ${TOKEN}" \
      -H "tenant-id: ${TENANT_ID}")
    
    if echo "$RESPONSE" | jq -e ".data[] | select(.name == \"${table}\")" > /dev/null 2>&1; then
        echo -e "  ${GREEN}✓${NC} ${table}"
    else
        echo -e "  ${RED}✗${NC} ${table} (表不存在，请先执行SQL脚本)"
        MISSING_TABLES+=("${table}")
    fi
done
echo ""

if [ ${#MISSING_TABLES[@]} -gt 0 ]; then
    echo -e "${RED}错误: 以下表不存在，请先执行SQL脚本:${NC}"
    for table in "${MISSING_TABLES[@]}"; do
        echo "  - ${table}"
    done
    echo ""
    echo "执行SQL脚本命令:"
    echo "  mysql -uroot -p123456 ruoyi-vue-pro < sql/mysql/erp-finance-tables.sql"
    echo "  mysql -uroot -p123456 ruoyi-vue-pro < sql/mysql/erp-stock-internal-tables.sql"
    exit 1
fi

# 步骤2: 检查哪些表已经创建了代码生成器定义
echo -e "${GREEN}[2/4] 检查已创建的表定义...${NC}"
EXISTING_TABLES=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "tenant-id: ${TENANT_ID}" | jq -r '.data[].tableName' 2>/dev/null || echo "")

TABLES_TO_CREATE=()
for table in "${ALL_TABLES[@]}"; do
    if echo "$EXISTING_TABLES" | grep -q "^${table}$"; then
        echo -e "  ${YELLOW}⊙${NC} ${table} (已存在，跳过)"
    else
        echo -e "  ${BLUE}○${NC} ${table} (需要创建)"
        TABLES_TO_CREATE+=("${table}")
    fi
done
echo ""

# 步骤3: 创建代码生成器表定义
if [ ${#TABLES_TO_CREATE[@]} -gt 0 ]; then
    echo -e "${GREEN}[3/4] 创建代码生成器表定义...${NC}"
    
    # 构建表名JSON数组
    TABLE_NAMES_JSON=$(printf '%s\n' "${TABLES_TO_CREATE[@]}" | jq -R . | jq -s .)
    
    RESPONSE=$(curl -s -X POST "${BASE_URL}/infra/codegen/create-list" \
      -H "Authorization: Bearer ${TOKEN}" \
      -H "tenant-id: ${TENANT_ID}" \
      -H "Content-Type: application/json" \
      -d "{\"dataSourceConfigId\": ${DATA_SOURCE_ID}, \"tableNames\": ${TABLE_NAMES_JSON}}")
    
    if echo "$RESPONSE" | jq -e '.code == 0' > /dev/null 2>&1; then
        echo -e "${GREEN}✓ 创建成功！${NC}"
        TABLE_IDS=$(echo "$RESPONSE" | jq -r '.data[]')
        echo "创建的表ID:"
        for ID in $TABLE_IDS; do
            echo "  - ${ID}"
        done
    else
        echo -e "${RED}✗ 创建失败${NC}"
        echo "$RESPONSE" | jq '.'
        exit 1
    fi
    echo ""
else
    echo -e "${GREEN}[3/4] 所有表定义已存在，跳过创建${NC}"
    echo ""
fi

# 步骤4: 获取所有表ID并下载代码
echo -e "${GREEN}[4/4] 下载生成的代码...${NC}"

# 创建输出目录
OUTPUT_DIR="codegen-new-features-$(date +%Y%m%d-%H%M%S)"
mkdir -p "${OUTPUT_DIR}"
echo "输出目录: ${OUTPUT_DIR}"
echo ""

# 获取所有表定义
ALL_TABLE_DEFS=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "tenant-id: ${TENANT_ID}")

# 下载每个表的代码
for table in "${ALL_TABLES[@]}"; do
    TABLE_ID=$(echo "$ALL_TABLE_DEFS" | jq -r ".data[] | select(.tableName == \"${table}\") | .id")
    
    if [ -n "$TABLE_ID" ] && [ "$TABLE_ID" != "null" ]; then
        echo -e "${BLUE}下载 ${table} (ID: ${TABLE_ID})...${NC}"
        OUTPUT_FILE="${OUTPUT_DIR}/codegen-${table}-${TABLE_ID}.zip"
        
        HTTP_CODE=$(curl -s -w "%{http_code}" -X GET "${BASE_URL}/infra/codegen/download?tableId=${TABLE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" \
          -o "${OUTPUT_FILE}")
        
        if [ "$HTTP_CODE" = "200" ]; then
            FILE_SIZE=$(ls -lh "${OUTPUT_FILE}" | awk '{print $5}')
            echo -e "  ${GREEN}✓${NC} 下载成功 (${FILE_SIZE})"
        else
            echo -e "  ${RED}✗${NC} 下载失败 (HTTP ${HTTP_CODE})"
            rm -f "${OUTPUT_FILE}"
        fi
    else
        echo -e "  ${RED}✗${NC} 未找到表定义: ${table}"
    fi
done

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}代码生成完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "生成的代码保存在: ${OUTPUT_DIR}"
echo ""
echo "下一步:"
echo "  1. 解压代码包"
echo "  2. 复制代码到项目目录"
echo "  3. 配置字段选项（模块名、业务名、类名等）"
echo "  4. 重新下载配置后的代码"

