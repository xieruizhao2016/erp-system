#!/bin/bash
# 批量下载所有代码生成器表的代码
# 使用方法: ./scripts/download-all-codegen.sh [DATA_SOURCE_ID]

set -e

DATA_SOURCE_ID="${1:-0}"
BASE_URL="${BASE_URL:-http://localhost:48080/admin-api}"
TENANT_ID="${TENANT_ID:-1}"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

# 获取Token
if [ -f ".jwt-token" ]; then
    TOKEN=$(cat .jwt-token)
else
    echo -e "${RED}错误: .jwt-token 文件不存在${NC}"
    echo "请先运行: ./scripts/get-jwt-token.sh"
    exit 1
fi

# 检查依赖
if ! command -v jq &> /dev/null; then
    echo -e "${RED}错误: jq 未安装${NC}"
    exit 1
fi

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}批量下载代码生成器代码${NC}"
echo -e "${GREEN}========================================${NC}"
echo "Data Source ID: ${DATA_SOURCE_ID}"
echo ""

# 创建输出目录
OUTPUT_DIR="codegen-downloads-$(date +%Y%m%d-%H%M%S)"
mkdir -p "${OUTPUT_DIR}"
echo -e "${YELLOW}输出目录: ${OUTPUT_DIR}${NC}"
echo ""

# 获取所有表ID
echo -e "${GREEN}[1/2] 获取表列表...${NC}"
RESPONSE=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "tenant-id: ${TENANT_ID}")

TABLE_IDS=$(echo "$RESPONSE" | jq -r '.data[] | select(.tableName | startswith("erp_")) | .id')
TABLE_COUNT=$(echo "$TABLE_IDS" | grep -v '^$' | wc -l | tr -d ' ')

if [ "$TABLE_COUNT" -eq 0 ]; then
    echo -e "${YELLOW}没有找到ERP相关的表${NC}"
    exit 0
fi

echo "找到 ${TABLE_COUNT} 个ERP表"
echo ""

# 下载代码
echo -e "${GREEN}[2/2] 下载代码...${NC}"
SUCCESS_COUNT=0
FAIL_COUNT=0

for TABLE_ID in $TABLE_IDS; do
    # 获取表名
    TABLE_NAME=$(echo "$RESPONSE" | jq -r ".data[] | select(.id == ${TABLE_ID}) | .tableName")
    
    if [ -z "$TABLE_NAME" ] || [ "$TABLE_NAME" = "null" ]; then
        # 如果从列表获取不到，尝试从详情获取
        DETAIL_RESPONSE=$(curl -s -X GET "${BASE_URL}/infra/codegen/detail?tableId=${TABLE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}")
        TABLE_NAME=$(echo "$DETAIL_RESPONSE" | jq -r '.data.table.tableName // "unknown"')
    fi
    
    OUTPUT_FILE="${OUTPUT_DIR}/codegen-${TABLE_NAME}.zip"
    echo -n "  下载表 ${TABLE_NAME} (ID: ${TABLE_ID})... "
    
    HTTP_CODE=$(curl -s -w "%{http_code}" -X GET "${BASE_URL}/infra/codegen/download?tableId=${TABLE_ID}" \
      -H "Authorization: Bearer ${TOKEN}" \
      -H "tenant-id: ${TENANT_ID}" \
      -o "${OUTPUT_FILE}")
    
    if [ "$HTTP_CODE" = "200" ] && [ -f "${OUTPUT_FILE}" ] && [ -s "${OUTPUT_FILE}" ]; then
        FILE_SIZE=$(ls -lh "${OUTPUT_FILE}" | awk '{print $5}')
        echo -e "${GREEN}✓ (${FILE_SIZE})${NC}"
        SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
    else
        echo -e "${RED}✗ (HTTP ${HTTP_CODE})${NC}"
        rm -f "${OUTPUT_FILE}"
        FAIL_COUNT=$((FAIL_COUNT + 1))
    fi
done

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}下载完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo "成功: ${SUCCESS_COUNT} 个"
echo "失败: ${FAIL_COUNT} 个"
echo "输出目录: ${OUTPUT_DIR}"
echo ""
echo "下一步操作:"
echo "1. 解压代码包: cd ${OUTPUT_DIR} && for f in *.zip; do unzip -q \"\$f\" -d \"\${f%.zip}\"; done"
echo "2. 检查生成的代码文件"
echo "3. 按照《代码生成器使用指南.md》中的说明放置代码"

