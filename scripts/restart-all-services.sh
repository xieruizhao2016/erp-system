#!/bin/bash
# 重启所有服务脚本
# 使用方法: ./scripts/restart-all-services.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

PROJECT_ROOT="/Users/RUIZHAO/Documents/Project/erp-system"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}重启所有服务${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

cd "$PROJECT_ROOT"

# 步骤1: 重启数据库服务
echo -e "${BLUE}[1/4] 重启数据库服务...${NC}"
cd script/docker
docker-compose restart mysql redis
echo -e "${GREEN}✓ 数据库服务已重启${NC}"
echo ""

# 等待数据库启动
echo "等待数据库启动..."
sleep 5

# 验证数据库连接
echo "验证数据库连接..."
if docker exec yudao-mysql mysqladmin ping -h localhost --silent 2>/dev/null; then
    echo -e "${GREEN}✓ MySQL 连接正常${NC}"
else
    echo -e "${YELLOW}⚠ MySQL 可能还在启动中${NC}"
fi

if docker exec yudao-redis redis-cli ping 2>/dev/null | grep -q PONG; then
    echo -e "${GREEN}✓ Redis 连接正常${NC}"
else
    echo -e "${YELLOW}⚠ Redis 可能还在启动中${NC}"
fi
echo ""

# 步骤2: 停止后端服务
cd "$PROJECT_ROOT"
echo -e "${BLUE}[2/4] 停止后端服务...${NC}"
if lsof -i :48080 >/dev/null 2>&1; then
    pkill -f "yudao-server" || true
    sleep 2
    echo -e "${GREEN}✓ 后端服务已停止${NC}"
else
    echo -e "${YELLOW}后端服务未运行${NC}"
fi
echo ""

# 步骤3: 启动后端服务
echo -e "${BLUE}[3/4] 启动后端服务...${NC}"
if [ -f "yudao-server/target/yudao-server.jar" ]; then
    ./start-backend.sh -d
    echo -e "${GREEN}✓ 后端服务已在后台启动${NC}"
    echo "等待后端服务启动..."
    sleep 10
    
    # 验证后端服务
    if lsof -i :48080 >/dev/null 2>&1; then
        echo -e "${GREEN}✓ 后端服务运行正常 (端口 48080)${NC}"
    else
        echo -e "${YELLOW}⚠ 后端服务可能还在启动中，请检查日志: tail -f nohup.out${NC}"
    fi
else
    echo -e "${RED}✗ 找不到后端jar包，请先编译: mvn clean package -DskipTests${NC}"
fi
echo ""

# 步骤4: 启动前端服务
echo -e "${BLUE}[4/4] 启动前端服务...${NC}"
cd original-yudao-ui

# 检查是否已安装依赖
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}⚠ node_modules 不存在，需要先安装依赖${NC}"
    echo "请手动运行: cd original-yudao-ui && npm install"
    echo ""
fi

# 检查前端服务是否已在运行
if lsof -i :5173 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
    echo -e "${YELLOW}前端服务可能已在运行${NC}"
    echo "如需重启，请手动停止后运行: npm run dev"
else
    echo -e "${GREEN}前端服务未运行${NC}"
    echo "请手动启动: cd original-yudao-ui && npm run dev"
fi
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}服务重启完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "服务状态:"
echo "  - MySQL:   localhost:3306"
echo "  - Redis:   localhost:6379"
echo "  - 后端API: http://localhost:48080"
echo "  - 前端UI:  http://localhost:5173 (需要手动启动)"
echo ""
echo "查看后端日志: tail -f nohup.out"
echo "查看Docker日志: docker-compose -f script/docker/docker-compose.yml logs -f"

