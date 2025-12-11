#!/bin/bash
# 部署生成的代码到项目
# 将代码从 codegen-new-features 目录复制到项目对应位置

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}部署生成的代码${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 项目根目录
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
CODEGEN_DIR="${PROJECT_ROOT}/codegen-new-features"

# 检查代码生成目录是否存在
if [ ! -d "${CODEGEN_DIR}" ]; then
    echo -e "${RED}错误: 代码生成目录不存在: ${CODEGEN_DIR}${NC}"
    exit 1
fi

# 财务模块表ID和包名映射
declare -A FINANCE_TABLES=(
    ["220"]="balance-sheet"
    ["221"]="receivable"
    ["222"]="payable"
    ["223"]="prepayment"
    ["224"]="prereceipt"
    ["225"]="profit-statement"
)

# 库存模块表ID和包名映射
declare -A STOCK_TABLES=(
    ["226"]="internal-in"
    ["227"]="internal-out"
)

# 复制函数
copy_code() {
    local table_id=$1
    local package_name=$2
    local module_type=$3  # finance 或 stock
    
    local source_dir="${CODEGEN_DIR}/codegen-${table_id}"
    local target_base="${PROJECT_ROOT}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp"
    
    if [ ! -d "${source_dir}" ]; then
        echo -e "${YELLOW}跳过: codegen-${table_id} 不存在${NC}"
        return
    fi
    
    echo -e "${BLUE}处理表ID: ${table_id} (${package_name})${NC}"
    
    # 1. 复制 Controller
    if [ -d "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/${package_name}" ]; then
        local target_controller_dir="${target_base}/controller/admin/${module_type}"
        mkdir -p "${target_controller_dir}"
        
        # 重命名包目录（从 finance-balance-sheet 到 finance/balancesheet）
        local new_package_name=$(echo "${package_name}" | sed 's/-//g')
        if [ "${module_type}" = "finance" ]; then
            # 财务模块：balance-sheet -> balancesheet, receivable -> receivable
            new_package_name=$(echo "${package_name}" | sed 's/-//g')
        else
            # 库存模块：internal-in -> internalin, internal-out -> internalout
            new_package_name=$(echo "${package_name}" | sed 's/-//g')
        fi
        
        echo "  复制 Controller: ${package_name} -> ${module_type}/${new_package_name}"
        cp -r "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/${package_name}" \
              "${target_controller_dir}/${new_package_name}" 2>/dev/null || true
    fi
    
    # 2. 复制 Service
    if [ -d "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/${package_name}" ]; then
        local target_service_dir="${target_base}/service/${module_type}"
        mkdir -p "${target_service_dir}"
        
        local new_package_name=$(echo "${package_name}" | sed 's/-//g')
        echo "  复制 Service: ${package_name} -> ${module_type}/${new_package_name}"
        cp -r "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/${package_name}" \
              "${target_service_dir}/${new_package_name}" 2>/dev/null || true
    fi
    
    # 3. 复制 DO
    if [ -d "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/${package_name}" ]; then
        local target_do_dir="${target_base}/dal/dataobject/${module_type}"
        mkdir -p "${target_do_dir}"
        
        local new_package_name=$(echo "${package_name}" | sed 's/-//g')
        echo "  复制 DO: ${package_name} -> ${module_type}/${new_package_name}"
        cp -r "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/${package_name}" \
              "${target_do_dir}/${new_package_name}" 2>/dev/null || true
    fi
    
    # 4. 复制 Mapper
    if [ -d "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/${package_name}" ]; then
        local target_mapper_dir="${target_base}/dal/mysql/${module_type}"
        mkdir -p "${target_mapper_dir}"
        
        local new_package_name=$(echo "${package_name}" | sed 's/-//g')
        echo "  复制 Mapper: ${package_name} -> ${module_type}/${new_package_name}"
        cp -r "${source_dir}/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/${package_name}" \
              "${target_mapper_dir}/${new_package_name}" 2>/dev/null || true
    fi
    
    # 5. 复制 Mapper XML
    if [ -d "${source_dir}/yudao-module-erp/src/main/resources/mapper" ]; then
        local target_xml_dir="${PROJECT_ROOT}/yudao-module-erp/src/main/resources/mapper"
        mkdir -p "${target_xml_dir}"
        
        echo "  复制 Mapper XML"
        cp -r "${source_dir}/yudao-module-erp/src/main/resources/mapper/"* "${target_xml_dir}/" 2>/dev/null || true
    fi
    
    echo -e "  ${GREEN}✓ 完成${NC}"
    echo ""
}

# 处理财务模块
echo -e "${BLUE}[1/2] 处理财务模块...${NC}"
for table_id in "${!FINANCE_TABLES[@]}"; do
    copy_code "${table_id}" "${FINANCE_TABLES[$table_id]}" "finance"
done

# 处理库存模块
echo -e "${BLUE}[2/2] 处理库存模块...${NC}"
for table_id in "${!STOCK_TABLES[@]}"; do
    copy_code "${table_id}" "${STOCK_TABLES[$table_id]}" "stock"
done

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}代码复制完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}注意:${NC}"
echo "1. 需要手动修复包名（将 finance-balance-sheet 改为 finance.balancesheet）"
echo "2. 需要合并 ErrorCode（不要直接覆盖 ErrorCodeConstants.java）"
echo "3. 需要检查并修复编译错误"
echo ""
echo "下一步:"
echo "  1. 修复包名和导入路径"
echo "  2. 合并 ErrorCode"
echo "  3. 编译测试"

