#!/bin/bash
# 启动所有服务脚本
# 使用方法: ./scripts/start-all-services.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

PROJECT_ROOT="/Users/xierui/Documents/Project/Other/erp-system"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}启动所有服务${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

cd "$PROJECT_ROOT"

# 步骤1: 启动Docker（如果需要）
echo -e "${BLUE}[1/5] 检查Docker服务...${NC}"
if ! docker info >/dev/null 2>&1; then
    echo -e "${YELLOW}Docker未运行，正在启动Docker...${NC}"
    open -a Docker
    echo "等待Docker启动..."
    sleep 10
    
    # 等待Docker完全启动
    for i in {1..30}; do
        if docker info >/dev/null 2>&1; then
            echo -e "${GREEN}✓ Docker已启动${NC}"
            break
        fi
        echo "等待Docker启动... ($i/30)"
        sleep 2
    done
    
    if ! docker info >/dev/null 2>&1; then
        echo -e "${RED}✗ Docker启动失败，请手动启动Docker Desktop${NC}"
        exit 1
    fi
else
    echo -e "${GREEN}✓ Docker正在运行${NC}"
fi
echo ""

# 步骤2: 启动数据库服务
echo -e "${BLUE}[2/5] 启动数据库服务（MySQL和Redis）...${NC}"
cd script/docker

# 启动MySQL和Redis
docker-compose up -d mysql redis

# 等待数据库启动
echo "等待数据库服务启动..."
sleep 5

# 验证数据库连接
echo "验证数据库连接..."
if docker exec yudao-mysql mysqladmin ping -h localhost --silent 2>/dev/null; then
    echo -e "${GREEN}✓ MySQL 连接正常${NC}"
else
    echo -e "${YELLOW}⚠ MySQL 可能还在启动中，继续执行...${NC}"
fi

if docker exec yudao-redis redis-cli ping 2>/dev/null | grep -q PONG; then
    echo -e "${GREEN}✓ Redis 连接正常${NC}"
else
    echo -e "${YELLOW}⚠ Redis 可能还在启动中，继续执行...${NC}"
fi
echo ""

# 步骤3: 检查后端jar包
cd "$PROJECT_ROOT"
echo -e "${BLUE}[3/5] 检查后端服务...${NC}"
if [ ! -f "yudao-server/target/yudao-server.jar" ]; then
    echo -e "${YELLOW}⚠ 后端jar包不存在，需要先编译${NC}"
    echo "正在编译后端代码..."
    mvn clean package -DskipTests
    if [ ! -f "yudao-server/target/yudao-server.jar" ]; then
        echo -e "${RED}✗ 编译失败，请检查错误信息${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ 后端代码编译完成${NC}"
else
    echo -e "${GREEN}✓ 后端jar包已存在${NC}"
fi
echo ""

# 步骤4: 启动后端服务
echo -e "${BLUE}[4/5] 启动后端服务...${NC}"
if lsof -i :48080 >/dev/null 2>&1; then
    echo -e "${YELLOW}⚠ 端口48080已被占用，后端服务可能已在运行${NC}"
    ps aux | grep -E "java.*yudao-server" | grep -v grep || true
else
    if [ -f "scripts/dev/start-backend.sh" ]; then
        chmod +x scripts/dev/start-backend.sh
        ./scripts/dev/start-backend.sh -d
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
        echo -e "${RED}✗ 找不到启动脚本: scripts/dev/start-backend.sh${NC}"
    fi
fi
echo ""

# 步骤5: 启动前端服务
echo -e "${BLUE}[5/5] 启动前端服务...${NC}"
cd original-yudao-ui

# 检查是否已安装依赖
if [ ! -d "node_modules" ]; then
    echo -e "${YELLOW}⚠ node_modules 不存在，需要先安装依赖${NC}"
    echo "正在安装依赖..."
    if command -v pnpm >/dev/null 2>&1; then
        pnpm install
    elif command -v npm >/dev/null 2>&1; then
        npm install
    else
        echo -e "${RED}✗ 未找到npm或pnpm，请先安装Node.js${NC}"
        exit 1
    fi
    echo -e "${GREEN}✓ 依赖安装完成${NC}"
fi

# 检查前端服务是否已在运行
if lsof -i :5173 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
    echo -e "${YELLOW}⚠ 前端服务可能已在运行${NC}"
    echo "如需重启，请手动停止后运行: npm run dev 或 pnpm dev"
else
    echo -e "${GREEN}正在启动前端服务...${NC}"
    if command -v pnpm >/dev/null 2>&1; then
        pnpm dev > ../frontend.log 2>&1 &
        FRONTEND_PID=$!
        echo "前端服务已在后台启动，PID: $FRONTEND_PID"
        echo "日志文件: frontend.log"
    elif command -v npm >/dev/null 2>&1; then
        npm run dev > ../frontend.log 2>&1 &
        FRONTEND_PID=$!
        echo "前端服务已在后台启动，PID: $FRONTEND_PID"
        echo "日志文件: frontend.log"
    else
        echo -e "${RED}✗ 未找到npm或pnpm，请先安装Node.js${NC}"
        exit 1
    fi
    
    sleep 5
    if lsof -i :5173 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
        echo -e "${GREEN}✓ 前端服务运行正常${NC}"
    else
        echo -e "${YELLOW}⚠ 前端服务可能还在启动中，请检查日志: tail -f frontend.log${NC}"
    fi
fi
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}所有服务启动完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "服务状态:"
echo "  - MySQL:   localhost:3306"
echo "  - Redis:   localhost:6379"
echo "  - 后端API: http://localhost:48080"
echo "  - 前端UI:  http://localhost:5173 或 http://localhost:3000"
echo ""
echo "查看日志:"
echo "  - 后端日志: tail -f nohup.out"
echo "  - 前端日志: tail -f frontend.log"
echo "  - Docker日志: docker-compose -f script/docker/docker-compose.yml logs -f"
echo ""
echo "停止服务:"
echo "  - 停止后端: pkill -f yudao-server"
echo "  - 停止前端: pkill -f 'vite|node.*dev'"
echo "  - 停止数据库: cd script/docker && docker-compose down"

