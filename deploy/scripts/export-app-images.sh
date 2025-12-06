#!/bin/bash

# 导出应用Docker镜像脚本
# 导出本地构建的yudao-server和yudao-admin镜像
# 使用方法: ./export-app-images.sh

set -e

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== 导出应用Docker镜像 ===${NC}"
echo ""

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo -e "${RED}错误: Docker未运行，请先启动Docker${NC}"
    exit 1
fi

# 应用镜像列表
APP_IMAGES=(
    "yudao-server:latest"
    "yudao-admin:latest"
)

echo -e "${BLUE}步骤1: 检查镜像是否存在${NC}"

# 检查镜像是否存在
MISSING_IMAGES=()
for img in "${APP_IMAGES[@]}"; do
    if docker images --format "{{.Repository}}:{{.Tag}}" | grep -q "^${img}$"; then
        echo -e "${GREEN}✅ 找到镜像: $img${NC}"
    else
        echo -e "${YELLOW}⚠️  镜像不存在: $img${NC}"
        MISSING_IMAGES+=("$img")
    fi
done

if [ ${#MISSING_IMAGES[@]} -gt 0 ]; then
    echo ""
    echo -e "${YELLOW}以下镜像不存在，请先构建：${NC}"
    for img in "${MISSING_IMAGES[@]}"; do
        echo "  - $img"
    done
    echo ""
    echo "执行以下命令构建镜像："
    echo "  ./deploy/scripts/build-images-local.sh"
    exit 1
fi

echo ""
echo -e "${BLUE}步骤2: 导出镜像${NC}"

# 创建导出目录
EXPORT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)/deploy/exports"
mkdir -p "$EXPORT_DIR"

# 生成导出文件名
TIMESTAMP=$(date +%Y%m%d-%H%M%S)
OUTPUT_FILE="$EXPORT_DIR/app-images-${TIMESTAMP}.tar.gz"

echo -e "${GREEN}正在导出镜像到: $OUTPUT_FILE${NC}"
echo "镜像列表:"
for img in "${APP_IMAGES[@]}"; do
    echo "  - $img"
done
echo ""

# 导出镜像
docker save "${APP_IMAGES[@]}" | gzip > "$OUTPUT_FILE"

if [ $? -eq 0 ]; then
    FILE_SIZE=$(du -h "$OUTPUT_FILE" | cut -f1)
    echo ""
    echo -e "${GREEN}✅ 镜像导出成功！${NC}"
    echo "文件: $OUTPUT_FILE"
    echo "大小: $FILE_SIZE"
    echo ""
    echo -e "${BLUE}下一步操作：${NC}"
    echo "1. 上传镜像到服务器:"
    echo "   ./deploy/scripts/upload-app-images.sh $OUTPUT_FILE"
    echo ""
    echo "或手动上传："
    echo "   scp $OUTPUT_FILE root@你的服务器IP:/tmp/"
    echo ""
else
    echo -e "${RED}❌ 镜像导出失败${NC}"
    exit 1
fi

