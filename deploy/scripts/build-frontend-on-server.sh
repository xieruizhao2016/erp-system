#!/bin/bash

# 在云服务器上构建前端镜像的脚本
# 可以在宝塔面板终端执行此脚本

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}=== 在云服务器上构建前端镜像 ===${NC}"
echo ""

# 检查Docker
if ! command -v docker &> /dev/null; then
    echo -e "${RED}错误: Docker未安装${NC}"
    exit 1
fi

# 进入项目目录
cd /opt/erp-system/script/docker || {
    echo -e "${RED}错误: 项目目录不存在${NC}"
    exit 1
}

echo -e "${YELLOW}步骤1: 检查Dockerfile配置${NC}"
if grep -q "registry.npmmirror.com" /opt/erp-system/original-yudao-ui/Dockerfile; then
    echo -e "${GREEN}✅ npm镜像源已配置${NC}"
else
    echo -e "${YELLOW}⚠️  npm镜像源未配置，构建可能较慢${NC}"
fi

echo ""
echo -e "${YELLOW}步骤2: 检查基础镜像${NC}"
docker images | grep -E "node.*18|nginx.*alpine" || {
    echo -e "${RED}错误: 缺少基础镜像（node:18-alpine 或 nginx:alpine）${NC}"
    exit 1
}

echo ""
echo -e "${YELLOW}步骤3: 开始构建前端镜像${NC}"
echo "这可能需要5-15分钟，请耐心等待..."
echo ""

# 构建前端镜像
docker compose -f docker-compose.prod.yml --env-file .env build admin 2>&1 | tee /tmp/frontend-build-$(date +%Y%m%d-%H%M%S).log

BUILD_STATUS=$?

if [ $BUILD_STATUS -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ 前端镜像构建成功！${NC}"
    echo ""
    echo -e "${YELLOW}步骤4: 检查镜像${NC}"
    docker images | grep yudao-admin
    echo ""
    echo -e "${GREEN}步骤5: 启动前端服务${NC}"
    docker compose -f docker-compose.prod.yml --env-file .env up -d admin
    echo ""
    echo -e "${GREEN}✅ 前端服务已启动！${NC}"
    echo ""
    echo -e "${YELLOW}检查服务状态:${NC}"
    docker compose -f docker-compose.prod.yml ps
    echo ""
    echo -e "${GREEN}部署完成！访问地址: http://115.190.240.137${NC}"
else
    echo ""
    echo -e "${RED}❌ 前端镜像构建失败${NC}"
    echo "查看详细日志: tail -50 /tmp/frontend-build-*.log"
    exit 1
fi

