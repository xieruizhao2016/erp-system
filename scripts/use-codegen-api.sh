#!/bin/bash
# 代码生成器 API 使用脚本
# 使用方法: ./scripts/use-codegen-api.sh [ACTION] [OPTIONS]
#
# Actions:
#   list-db-tables      - 获取数据库表列表
#   list-tables         - 获取已导入的表定义列表
#   create-table        - 创建代码生成器表定义
#   get-detail          - 获取表定义详情
#   preview             - 预览生成代码
#   download            - 下载生成代码
#   update              - 更新表定义

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
else
    echo -e "${RED}错误: .jwt-token 文件不存在${NC}"
    echo "请先运行: ./scripts/get-jwt-token.sh"
    exit 1
fi

# 显示帮助信息
show_help() {
    echo -e "${GREEN}代码生成器 API 使用脚本${NC}"
    echo ""
    echo "使用方法:"
    echo "  ./scripts/use-codegen-api.sh [ACTION] [OPTIONS]"
    echo ""
    echo "Actions:"
    echo "  list-db-tables [name] [comment]  - 获取数据库表列表（可选：表名、注释过滤）"
    echo "  list-tables                      - 获取已导入的表定义列表"
    echo "  create-table TABLE_NAME...       - 创建代码生成器表定义（可指定多个表名）"
    echo "  get-detail TABLE_ID              - 获取表定义详情"
    echo "  preview TABLE_ID                 - 预览生成代码"
    echo "  download TABLE_ID [OUTPUT_FILE]  - 下载生成代码（默认保存为 codegen-TABLE_ID.zip）"
    echo "  update TABLE_ID JSON_FILE        - 更新表定义（需要提供JSON文件）"
    echo ""
    echo "环境变量:"
    echo "  BASE_URL         - API基础URL（默认: http://localhost:48080/admin-api）"
    echo "  TENANT_ID        - 租户ID（默认: 1）"
    echo "  DATA_SOURCE_ID   - 数据源ID（默认: 1）"
    echo ""
    echo "示例:"
    echo "  # 列出所有数据库表"
    echo "  ./scripts/use-codegen-api.sh list-db-tables"
    echo ""
    echo "  # 列出包含 'erp' 的表"
    echo "  ./scripts/use-codegen-api.sh list-db-tables erp"
    echo ""
    echo "  # 创建表定义"
    echo "  ./scripts/use-codegen-api.sh create-table erp_production_order"
    echo ""
    echo "  # 获取表详情"
    echo "  ./scripts/use-codegen-api.sh get-detail 123"
    echo ""
    echo "  # 预览代码"
    echo "  ./scripts/use-codegen-api.sh preview 123"
    echo ""
    echo "  # 下载代码"
    echo "  ./scripts/use-codegen-api.sh download 123"
}

# 执行API调用
ACTION="${1:-help}"

case "$ACTION" in
    list-db-tables)
        echo -e "${GREEN}获取数据库表列表...${NC}"
        NAME_PARAM="${2:+&name=${2}}"
        COMMENT_PARAM="${3:+&comment=${3}}"
        curl -s -X GET "${BASE_URL}/infra/codegen/db/table/list?dataSourceConfigId=${DATA_SOURCE_ID}${NAME_PARAM}${COMMENT_PARAM}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" | jq '.'
        ;;
    
    list-tables)
        echo -e "${GREEN}获取已导入的表定义列表...${NC}"
        curl -s -X GET "${BASE_URL}/infra/codegen/table/list?dataSourceConfigId=${DATA_SOURCE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" | jq '.'
        ;;
    
    create-table)
        if [ -z "$2" ]; then
            echo -e "${RED}错误: 请指定表名${NC}"
            exit 1
        fi
        shift
        TABLE_NAMES=("$@")
        echo -e "${GREEN}创建代码生成器表定义...${NC}"
        echo "表名: ${TABLE_NAMES[*]}"
        
        # 构建表名JSON数组
        TABLE_NAMES_JSON=$(printf '%s\n' "${TABLE_NAMES[@]}" | jq -R . | jq -s .)
        
        RESPONSE=$(curl -s -X POST "${BASE_URL}/infra/codegen/create-list" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" \
          -H "Content-Type: application/json" \
          -d "{\"dataSourceConfigId\": ${DATA_SOURCE_ID}, \"tableNames\": ${TABLE_NAMES_JSON}}")
        
        if echo "$RESPONSE" | jq -e '.code == 0' > /dev/null 2>&1; then
            echo -e "${GREEN}✓ 创建成功！${NC}"
            echo "$RESPONSE" | jq '.'
            TABLE_IDS=$(echo "$RESPONSE" | jq -r '.data[]')
            echo ""
            echo -e "${YELLOW}创建的表ID:${NC}"
            for ID in $TABLE_IDS; do
                echo "  - $ID"
            done
        else
            echo -e "${RED}✗ 创建失败${NC}"
            echo "$RESPONSE" | jq '.'
            exit 1
        fi
        ;;
    
    get-detail)
        if [ -z "$2" ]; then
            echo -e "${RED}错误: 请指定表ID${NC}"
            exit 1
        fi
        TABLE_ID="$2"
        echo -e "${GREEN}获取表定义详情 (ID: ${TABLE_ID})...${NC}"
        curl -s -X GET "${BASE_URL}/infra/codegen/detail?tableId=${TABLE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" | jq '.'
        ;;
    
    preview)
        if [ -z "$2" ]; then
            echo -e "${RED}错误: 请指定表ID${NC}"
            exit 1
        fi
        TABLE_ID="$2"
        echo -e "${GREEN}预览生成代码 (ID: ${TABLE_ID})...${NC}"
        OUTPUT_FILE="${3:-preview-${TABLE_ID}.json}"
        curl -s -X GET "${BASE_URL}/infra/codegen/preview?tableId=${TABLE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" | jq '.' > "${OUTPUT_FILE}"
        echo -e "${GREEN}✓ 预览结果已保存到: ${OUTPUT_FILE}${NC}"
        ;;
    
    download)
        if [ -z "$2" ]; then
            echo -e "${RED}错误: 请指定表ID${NC}"
            exit 1
        fi
        TABLE_ID="$2"
        OUTPUT_FILE="${3:-codegen-${TABLE_ID}.zip}"
        echo -e "${GREEN}下载生成代码 (ID: ${TABLE_ID})...${NC}"
        
        HTTP_CODE=$(curl -s -w "%{http_code}" -X GET "${BASE_URL}/infra/codegen/download?tableId=${TABLE_ID}" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" \
          -o "${OUTPUT_FILE}")
        
        if [ "$HTTP_CODE" = "200" ]; then
            echo -e "${GREEN}✓ 下载成功！${NC}"
            echo "文件保存到: ${OUTPUT_FILE}"
            if command -v unzip &> /dev/null; then
                FILE_SIZE=$(ls -lh "${OUTPUT_FILE}" | awk '{print $5}')
                echo "文件大小: ${FILE_SIZE}"
            fi
        else
            echo -e "${RED}✗ 下载失败 (HTTP ${HTTP_CODE})${NC}"
            rm -f "${OUTPUT_FILE}"
            exit 1
        fi
        ;;
    
    update)
        if [ -z "$2" ] || [ -z "$3" ]; then
            echo -e "${RED}错误: 请指定表ID和JSON文件${NC}"
            exit 1
        fi
        TABLE_ID="$2"
        JSON_FILE="$3"
        if [ ! -f "$JSON_FILE" ]; then
            echo -e "${RED}错误: JSON文件不存在: ${JSON_FILE}${NC}"
            exit 1
        fi
        echo -e "${GREEN}更新表定义 (ID: ${TABLE_ID})...${NC}"
        RESPONSE=$(curl -s -X PUT "${BASE_URL}/infra/codegen/update" \
          -H "Authorization: Bearer ${TOKEN}" \
          -H "tenant-id: ${TENANT_ID}" \
          -H "Content-Type: application/json" \
          -d "@${JSON_FILE}")
        
        if echo "$RESPONSE" | jq -e '.code == 0' > /dev/null 2>&1; then
            echo -e "${GREEN}✓ 更新成功！${NC}"
            echo "$RESPONSE" | jq '.'
        else
            echo -e "${RED}✗ 更新失败${NC}"
            echo "$RESPONSE" | jq '.'
            exit 1
        fi
        ;;
    
    help|--help|-h)
        show_help
        ;;
    
    *)
        echo -e "${RED}错误: 未知的操作 '${ACTION}'${NC}"
        echo ""
        show_help
        exit 1
        ;;
esac

