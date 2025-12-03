#!/bin/bash

# Docker镜像导出脚本
# 用于在本地拉取镜像并导出，然后上传到服务器

set -e

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Docker镜像导出脚本 ===${NC}"
echo ""

# 检查Docker是否运行
if ! docker info > /dev/null 2>&1; then
    echo "错误: Docker未运行，请先启动Docker"
    exit 1
fi

# 镜像列表
IMAGES=(
    "mysql:8"
    "redis:6-alpine"
)

echo -e "${YELLOW}步骤1: 拉取Docker镜像${NC}"
echo "需要拉取的镜像:"
for img in "${IMAGES[@]}"; do
    echo "  - $img"
done
echo ""

# 拉取镜像
for img in "${IMAGES[@]}"; do
    echo -e "${GREEN}正在拉取: $img${NC}"
    docker pull "$img" || {
        echo -e "${YELLOW}警告: 拉取 $img 失败，继续...${NC}"
    }
done

echo ""
echo -e "${YELLOW}步骤2: 导出镜像${NC}"

# 导出镜像
OUTPUT_FILE="docker-images-$(date +%Y%m%d-%H%M%S).tar.gz"
echo -e "${GREEN}正在导出镜像到: $OUTPUT_FILE${NC}"

docker save "${IMAGES[@]}" | gzip > "$OUTPUT_FILE"

if [ $? -eq 0 ]; then
    FILE_SIZE=$(du -h "$OUTPUT_FILE" | cut -f1)
    echo -e "${GREEN}✅ 镜像导出成功！${NC}"
    echo "文件: $OUTPUT_FILE"
    echo "大小: $FILE_SIZE"
    echo ""
    echo -e "${YELLOW}步骤3: 上传到服务器${NC}"
    echo "执行以下命令上传镜像："
    echo ""
    echo "scp -i /Users/xierui/Documents/huoshan-ssh.pem $OUTPUT_FILE root@115.190.240.137:/tmp/"
    echo ""
    echo -e "${YELLOW}步骤4: 在服务器上导入${NC}"
    echo "SSH到服务器后执行："
    echo ""
    echo "gunzip -c /tmp/$OUTPUT_FILE | docker load"
    echo ""
    echo -e "${GREEN}完成！${NC}"
else
    echo -e "${YELLOW}错误: 镜像导出失败${NC}"
    exit 1
fi

