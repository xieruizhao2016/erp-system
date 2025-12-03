#!/bin/bash
# 导出构建应用所需的基础镜像

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}=== 导出构建镜像脚本 ===${NC}"

IMAGES=(
    "node:16-alpine"
    "nginx:alpine"
    "eclipse-temurin:8-jre"
)

echo -e "${YELLOW}需要拉取的镜像:${NC}"
for img in "${IMAGES[@]}"; do
    echo "  - $img"
done

# 自动继续，不需要交互

for img in "${IMAGES[@]}"; do
    echo -e "${GREEN}正在拉取: $img${NC}"
    docker pull "$img" || echo "警告: 拉取 $img 失败"
done

OUTPUT_FILE="docker-build-images-$(date +%Y%m%d-%H%M%S).tar.gz"
echo -e "${GREEN}正在导出镜像...${NC}"
docker save "${IMAGES[@]}" | gzip > "$OUTPUT_FILE"

FILE_SIZE=$(du -h "$OUTPUT_FILE" | cut -f1)
echo -e "${GREEN}✅ 导出成功: $OUTPUT_FILE ($FILE_SIZE)${NC}"
echo ""
echo "上传命令:"
echo "scp -i /Users/xierui/Documents/huoshan-ssh.pem $OUTPUT_FILE root@115.190.240.137:/tmp/"
