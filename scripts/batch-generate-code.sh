#!/bin/bash
# 批量生成生产管理表代码脚本
# 使用方法: ./scripts/batch-generate-code.sh [TOKEN] [BASE_URL] [DATA_SOURCE_ID]

set -e

# 配置参数
TOKEN="${1}"
BASE_URL="${2:-http://localhost:48080/admin-api}"
DATA_SOURCE_ID="${3:-1}"

# 如果未提供Token，尝试从文件读取
if [ -z "$TOKEN" ]; then
    if [ -f ".jwt-token" ]; then
        TOKEN=$(cat .jwt-token)
        echo -e "${YELLOW}从 .jwt-token 文件读取Token${NC}"
    else
        echo -e "${RED}错误: 未提供Token且 .jwt-token 文件不存在${NC}"
        echo "请先运行: ./scripts/get-jwt-token.sh"
        echo "或提供Token作为第一个参数: ./scripts/batch-generate-code.sh YOUR_TOKEN"
        exit 1
    fi
fi

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 生产管理相关表列表（Phase 1-4所有表）
TABLES=(
  # Phase 1: 生产订单管理
  "erp_production_order"
  
  # Phase 2: 生产计划管理
  "erp_product_bom"
  "erp_product_bom_item"
  "erp_process_route"
  "erp_process_route_item"
  "erp_production_schedule"
  "erp_production_schedule_item"
  "erp_mrp_params"
  "erp_mrp_result"
  
  # Phase 3: 生产执行与质量管理
  "erp_work_order"
  "erp_work_order_progress"
  "erp_quality_standard"
  "erp_quality_item"
  "erp_quality_inspection"
  "erp_quality_inspection_item"
  "erp_equipment"
  "erp_equipment_status"
  
  # Phase 4: 成本核算与数据分析
  "erp_cost_standard"
  "erp_cost_actual"
  "erp_cost_variance"
  "erp_work_hours"
  "erp_production_kpi"
  "erp_production_report"
  "erp_production_dashboard_config"
)

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}批量生成生产管理表代码${NC}"
echo -e "${GREEN}========================================${NC}"
echo "Base URL: ${BASE_URL}"
echo "Data Source ID: ${DATA_SOURCE_ID}"
echo "表数量: ${#TABLES[@]}"
echo ""

# 检查jq是否安装
if ! command -v jq &> /dev/null; then
    echo -e "${RED}错误: jq 未安装，请先安装 jq${NC}"
    echo "macOS: brew install jq"
    echo "Ubuntu: sudo apt-get install jq"
    exit 1
fi

# 检查curl是否安装
if ! command -v curl &> /dev/null; then
    echo -e "${RED}错误: curl 未安装${NC}"
    exit 1
fi

# 创建输出目录
OUTPUT_DIR="codegen-output-$(date +%Y%m%d-%H%M%S)"
mkdir -p "${OUTPUT_DIR}"
echo -e "${YELLOW}输出目录: ${OUTPUT_DIR}${NC}"
echo ""

# 步骤1: 获取数据库表列表
echo -e "${GREEN}[1/5] 获取数据库表列表...${NC}"
RESPONSE=$(curl -s -X GET "${BASE_URL}/infra/codegen/db/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}")

if echo "$RESPONSE" | jq -e '.code != 0' > /dev/null 2>&1; then
    echo -e "${RED}错误: 获取表列表失败${NC}"
    echo "$RESPONSE" | jq '.'
    exit 1
fi

AVAILABLE_TABLES=$(echo "$RESPONSE" | jq -r '.data[].name')
echo "可用表数量: $(echo "$AVAILABLE_TABLES" | wc -l | tr -d ' ')"
echo ""

# 步骤2: 创建代码生成器表定义
echo -e "${GREEN}[2/5] 创建代码生成器表定义...${NC}"

# 检查哪些表已经存在
EXISTING_TABLES=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}" | jq -r '.data[].tableName' 2>/dev/null || echo "")

# 过滤出需要创建的表
TABLES_TO_CREATE=()
for table in "${TABLES[@]}"; do
    if ! echo "$EXISTING_TABLES" | grep -q "^${table}$"; then
        TABLES_TO_CREATE+=("$table")
    else
        echo -e "${YELLOW}  跳过已存在的表: ${table}${NC}"
    fi
done

if [ ${#TABLES_TO_CREATE[@]} -eq 0 ]; then
    echo -e "${YELLOW}所有表已存在，跳过创建步骤${NC}"
    # 获取已存在的表ID
    EXISTING_TABLE_IDS=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
      -H "Authorization: Bearer ${TOKEN}" | jq -r '.data[] | select(.tableName | IN('"$(printf '%s\n' "${TABLES[@]}" | jq -R . | jq -s . | jq -c .)"')) | .id')
else
    # 构建表名JSON数组
    TABLE_NAMES_JSON=$(printf '%s\n' "${TABLES_TO_CREATE[@]}" | jq -R . | jq -s .)
    
    # 创建表定义
    RESPONSE=$(curl -s -X POST "${BASE_URL}/infra/codegen/create-list" \
      -H "Authorization: Bearer ${TOKEN}" \
      -H "Content-Type: application/json" \
      -d "{\"dataSourceConfigId\": ${DATA_SOURCE_ID}, \"tableNames\": ${TABLE_NAMES_JSON}}")
    
    if echo "$RESPONSE" | jq -e '.code != 0' > /dev/null 2>&1; then
        echo -e "${RED}错误: 创建表定义失败${NC}"
        echo "$RESPONSE" | jq '.'
        exit 1
    fi
    
    NEW_TABLE_IDS=$(echo "$RESPONSE" | jq -r '.data[]')
    echo "创建了 ${#TABLES_TO_CREATE[@]} 个新表定义"
    echo "新表ID: ${NEW_TABLE_IDS}"
fi

# 获取所有表ID（包括新创建和已存在的）
ALL_TABLE_IDS=$(curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
  -H "Authorization: Bearer ${TOKEN}" | jq -r '.data[] | select(.tableName | IN('"$(printf '%s\n' "${TABLES[@]}" | jq -R . | jq -s . | jq -c .)"')) | .id')

echo ""
echo -e "${GREEN}[3/5] 获取表定义详情...${NC}"

# 步骤3: 获取并保存表定义详情
TABLE_DETAILS_FILE="${OUTPUT_DIR}/table-details.json"
echo "{" > "${TABLE_DETAILS_FILE}"
FIRST=true
for TABLE_ID in $ALL_TABLE_IDS; do
    if [ "$FIRST" = true ]; then
        FIRST=false
    else
        echo "," >> "${TABLE_DETAILS_FILE}"
    fi
    
    echo -n "  \"${TABLE_ID}\": " >> "${TABLE_DETAILS_FILE}"
    curl -s -X GET "${BASE_URL}/infra/codegen/detail?tableId=${TABLE_ID}" \
      -H "Authorization: Bearer ${TOKEN}" | jq '.data' >> "${TABLE_DETAILS_FILE}"
done
echo "}" >> "${TABLE_DETAILS_FILE}"

echo "表定义详情已保存到: ${TABLE_DETAILS_FILE}"
echo ""

# 步骤4: 预览代码（可选）
echo -e "${GREEN}[4/5] 预览代码（可选，跳过）...${NC}"
echo "如需预览，请手动调用: curl -X GET \"${BASE_URL}/infra/codegen/preview?tableId=TABLE_ID\" -H \"Authorization: Bearer ${TOKEN}\""
echo ""

# 步骤5: 下载代码
echo -e "${GREEN}[5/5] 下载生成代码...${NC}"
DOWNLOAD_COUNT=0
for TABLE_ID in $ALL_TABLE_IDS; do
    TABLE_NAME=$(curl -s -X GET "${BASE_URL}/infra/codegen/detail?tableId=${TABLE_ID}" \
      -H "Authorization: Bearer ${TOKEN}" | jq -r '.data.table.tableName')
    
    echo -n "  下载表 ${TABLE_NAME} (ID: ${TABLE_ID})... "
    
    OUTPUT_FILE="${OUTPUT_DIR}/codegen-${TABLE_NAME}.zip"
    HTTP_CODE=$(curl -s -w "%{http_code}" -X GET "${BASE_URL}/infra/codegen/download?tableId=${TABLE_ID}" \
      -H "Authorization: Bearer ${TOKEN}" \
      -o "${OUTPUT_FILE}")
    
    if [ "$HTTP_CODE" = "200" ]; then
        echo -e "${GREEN}✓${NC}"
        DOWNLOAD_COUNT=$((DOWNLOAD_COUNT + 1))
    else
        echo -e "${RED}✗ (HTTP ${HTTP_CODE})${NC}"
        rm -f "${OUTPUT_FILE}"
    fi
done

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}批量生成完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo "成功下载: ${DOWNLOAD_COUNT} 个代码包"
echo "输出目录: ${OUTPUT_DIR}"
echo ""
echo "下一步操作:"
echo "1. 解压代码包: cd ${OUTPUT_DIR} && unzip codegen-*.zip"
echo "2. 按照《代码生成器使用指南.md》中的说明放置代码"
echo "3. 检查并修复导入路径"
echo "4. 编译测试"

