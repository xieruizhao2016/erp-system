#!/bin/bash
# ERP系统部署脚本

set -e

echo "=========================================="
echo "ERP系统部署脚本"
echo "=========================================="

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "❌ 错误: Docker未安装，请先安装Docker"
    exit 1
fi

# 检查Docker Compose是否安装
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    echo "❌ 错误: Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 检查环境变量文件
if [ ! -f ".env" ]; then
    echo "⚠️  警告: .env 文件不存在"
    echo "正在从 .env.example 创建 .env 文件..."
    cp .env.example .env
    echo "✅ 已创建 .env 文件，请编辑并设置密码等配置"
    echo "⚠️  请编辑 .env 文件后重新运行此脚本"
    exit 1
fi

# 导入Docker镜像
echo ""
echo "📦 步骤1: 导入Docker镜像..."
if [ -f "yudao-admin.tar" ]; then
    echo "导入前端镜像..."
    docker load -i yudao-admin.tar
    echo "✅ 前端镜像导入完成"
else
    echo "⚠️  警告: yudao-admin.tar 不存在，跳过前端镜像导入"
fi

if [ -f "yudao-server.tar" ]; then
    echo "导入后端镜像..."
    docker load -i yudao-server.tar
    echo "✅ 后端镜像导入完成"
else
    echo "⚠️  警告: yudao-server.tar 不存在，跳过后端镜像导入"
fi

# 检查Docker Compose命令
if docker compose version &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker compose"
else
    DOCKER_COMPOSE_CMD="docker-compose"
fi

# 启动服务
echo ""
echo "🚀 步骤2: 启动服务..."
$DOCKER_COMPOSE_CMD -f docker-compose.prod.yml --env-file .env up -d

echo ""
echo "⏳ 等待服务启动..."
sleep 10

# 检查服务状态
echo ""
echo "📊 步骤3: 检查服务状态..."
docker ps --filter "name=yudao" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "=========================================="
echo "✅ 部署完成！"
echo "=========================================="
echo ""
echo "📝 后续操作："
echo "1. 查看日志: docker logs -f yudao-server-prod"
echo "2. 查看前端: docker logs -f yudao-admin-prod"
echo "3. 访问前端: http://服务器IP"
echo "4. 访问后端API: http://服务器IP:48080"
echo ""
