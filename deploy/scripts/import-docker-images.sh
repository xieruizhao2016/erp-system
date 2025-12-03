#!/bin/bash

# Docker镜像导入脚本（在服务器上执行）
# 用于导入从本地导出的Docker镜像

set -e

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== Docker镜像导入脚本 ===${NC}"
echo ""

# 检查镜像文件
IMAGE_FILE="/tmp/docker-images-*.tar.gz"
if ls $IMAGE_FILE 1> /dev/null 2>&1; then
    IMAGE_FILE=$(ls -t /tmp/docker-images-*.tar.gz | head -1)
    echo -e "${GREEN}找到镜像文件: $IMAGE_FILE${NC}"
else
    echo -e "${RED}错误: 未找到镜像文件${NC}"
    echo "请先上传镜像文件到 /tmp/ 目录"
    echo "上传命令: scp -i your-key.pem docker-images-*.tar.gz root@115.190.240.137:/tmp/"
    exit 1
fi

# 检查文件大小
FILE_SIZE=$(du -h "$IMAGE_FILE" | cut -f1)
echo "文件大小: $FILE_SIZE"
echo ""

# 确认
read -p "是否继续导入镜像? (y/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "已取消"
    exit 1
fi

# 导入镜像
echo -e "${YELLOW}正在导入镜像，请稍候...${NC}"
gunzip -c "$IMAGE_FILE" | docker load

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ 镜像导入成功！${NC}"
    echo ""
    echo -e "${YELLOW}已导入的镜像:${NC}"
    docker images | grep -E "mysql|redis" || docker images
    echo ""
    echo -e "${GREEN}现在可以继续部署ERP系统了！${NC}"
    echo ""
    echo "执行以下命令部署："
    echo "cd /opt/erp-system/script/docker"
    echo "docker compose -f docker-compose.prod.yml --env-file .env up -d --build"
else
    echo -e "${RED}错误: 镜像导入失败${NC}"
    exit 1
fi

