#!/bin/bash
# 批量复制生成的代码到项目目录
# 使用方法: ./scripts/copy-generated-code.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 项目根目录
PROJECT_ROOT="/Users/RUIZHAO/Documents/Project/erp-system"
CODEGEN_DIR="${PROJECT_ROOT}/codegen-downloads-20251116-162611"

# 目标目录
BACKEND_TARGET="${PROJECT_ROOT}/yudao-module-erp"
FRONTEND_TARGET="${PROJECT_ROOT}/original-yudao-ui"
MAPPER_TARGET="${PROJECT_ROOT}/yudao-module-erp/src/main/resources/mapper"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}批量复制生成的代码${NC}"
echo -e "${GREEN}========================================${NC}"
echo "代码生成目录: ${CODEGEN_DIR}"
echo "后端目标目录: ${BACKEND_TARGET}"
echo "前端目标目录: ${FRONTEND_TARGET}"
echo ""

# 检查目录是否存在
if [ ! -d "$CODEGEN_DIR" ]; then
    echo -e "${RED}错误: 代码生成目录不存在: ${CODEGEN_DIR}${NC}"
    exit 1
fi

if [ ! -d "$BACKEND_TARGET" ]; then
    echo -e "${RED}错误: 后端目标目录不存在: ${BACKEND_TARGET}${NC}"
    exit 1
fi

if [ ! -d "$FRONTEND_TARGET" ]; then
    echo -e "${RED}错误: 前端目标目录不存在: ${FRONTEND_TARGET}${NC}"
    exit 1
fi

# 统计变量
COPIED_BACKEND=0
COPIED_FRONTEND=0
SKIPPED=0
ERRORS=0

# 获取所有代码生成目录
CODEGEN_DIRS=$(find "$CODEGEN_DIR" -maxdepth 1 -type d -name "codegen-erp_*" | sort)

echo -e "${GREEN}[1/3] 复制后端代码...${NC}"

for CODEGEN_MODULE_DIR in $CODEGEN_DIRS; do
    MODULE_NAME=$(basename "$CODEGEN_MODULE_DIR" | sed 's/codegen-erp_//')
    echo -e "${BLUE}处理模块: ${MODULE_NAME}${NC}"
    
    # 后端 Java 代码路径
    BACKEND_SRC="${CODEGEN_MODULE_DIR}/yudao-module-erp/src/main/java"
    
    if [ -d "$BACKEND_SRC" ]; then
        # 复制 Controller
        if [ -d "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/controller" ]; then
            echo "  复制 Controller..."
            cp -r "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/controller"/* \
              "${BACKEND_TARGET}/src/main/java/cn/iocoder/yudao/module/erp/controller/" 2>/dev/null || true
            COPIED_BACKEND=$((COPIED_BACKEND + 1))
        fi
        
        # 复制 Service
        if [ -d "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/service" ]; then
            echo "  复制 Service..."
            cp -r "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/service"/* \
              "${BACKEND_TARGET}/src/main/java/cn/iocoder/yudao/module/erp/service/" 2>/dev/null || true
            COPIED_BACKEND=$((COPIED_BACKEND + 1))
        fi
        
        # 复制 DO
        if [ -d "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/dal/dataobject" ]; then
            echo "  复制 DO..."
            cp -r "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/dal/dataobject"/* \
              "${BACKEND_TARGET}/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/" 2>/dev/null || true
            COPIED_BACKEND=$((COPIED_BACKEND + 1))
        fi
        
        # 复制 Mapper
        if [ -d "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/dal/mysql" ]; then
            echo "  复制 Mapper..."
            cp -r "${BACKEND_SRC}/cn/iocoder/yudao/module/erp/dal/mysql"/* \
              "${BACKEND_TARGET}/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/" 2>/dev/null || true
            COPIED_BACKEND=$((COPIED_BACKEND + 1))
        fi
    fi
    
    # 复制 Mapper XML
    MAPPER_XML_SRC="${CODEGEN_MODULE_DIR}/yudao-module-erp/src/main/resources/mapper"
    if [ -d "$MAPPER_XML_SRC" ]; then
        echo "  复制 Mapper XML..."
        cp -r "${MAPPER_XML_SRC}"/* "${MAPPER_TARGET}/" 2>/dev/null || true
        COPIED_BACKEND=$((COPIED_BACKEND + 1))
    fi
done

echo ""
echo -e "${GREEN}[2/3] 复制前端代码...${NC}"

for CODEGEN_MODULE_DIR in $CODEGEN_DIRS; do
    MODULE_NAME=$(basename "$CODEGEN_MODULE_DIR" | sed 's/codegen-erp_//')
    
    # 前端代码路径
    FRONTEND_SRC="${CODEGEN_MODULE_DIR}/yudao-ui-admin-vue3/src"
    
    if [ -d "$FRONTEND_SRC" ]; then
        # 复制 API
        if [ -d "${FRONTEND_SRC}/api" ]; then
            echo "  复制 API: ${MODULE_NAME}..."
            cp -r "${FRONTEND_SRC}/api"/* "${FRONTEND_TARGET}/src/api/" 2>/dev/null || true
            COPIED_FRONTEND=$((COPIED_FRONTEND + 1))
        fi
        
        # 复制 Views
        if [ -d "${FRONTEND_SRC}/views" ]; then
            echo "  复制 Views: ${MODULE_NAME}..."
            cp -r "${FRONTEND_SRC}/views"/* "${FRONTEND_TARGET}/src/views/" 2>/dev/null || true
            COPIED_FRONTEND=$((COPIED_FRONTEND + 1))
        fi
    fi
done

echo ""
echo -e "${GREEN}[3/3] 处理 ErrorCode...${NC}"
echo -e "${YELLOW}注意: ErrorCode 需要手动合并，请检查以下文件:${NC}"

ERROR_CODE_FILES=$(find "$CODEGEN_DIR" -name "*ErrorCodeConstants*" -type f 2>/dev/null | head -5)
if [ -n "$ERROR_CODE_FILES" ]; then
    echo "找到 ErrorCode 文件:"
    echo "$ERROR_CODE_FILES" | while read -r file; do
        echo "  - $file"
    done
    echo ""
    echo -e "${YELLOW}请手动将这些文件中的错误码合并到:${NC}"
    echo "  ${BACKEND_TARGET}/src/main/java/cn/iocoder/yudao/module/erp/enums/ErrorCodeConstants.java"
else
    echo -e "${GREEN}未找到需要合并的 ErrorCode 文件${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}复制完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo "后端文件复制操作: ${COPIED_BACKEND} 次"
echo "前端文件复制操作: ${COPIED_FRONTEND} 次"
echo ""
echo -e "${YELLOW}下一步操作:${NC}"
echo "1. 检查复制的文件是否正确"
echo "2. 手动合并 ErrorCodeConstants.java"
echo "3. 检查并修复导入路径"
echo "4. 编译测试: cd yudao-module-erp && mvn clean compile"

