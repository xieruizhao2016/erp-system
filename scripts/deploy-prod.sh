#!/bin/bash
# 生产环境部署脚本
# 用途: 本地打包并部署到腾讯云服务器
# 使用方法: ./scripts/deploy-prod.sh

set -e  # 遇到错误立即退出

PROJECT_ROOT="/Users/xierui/Documents/Project/Other/erp-system"
SSH_KEY="$PROJECT_ROOT/tengxunyun.pem"
SERVER="ubuntu@101.33.244.240"
SERVER_IP="101.33.244.240"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "  生产环境部署脚本"
echo "=========================================="
echo ""

# 步骤1: 检查配置
echo -e "${YELLOW}【步骤1】检查配置...${NC}"
cd "$PROJECT_ROOT/original-yudao-ui"

# 检查前端环境变量
if ! grep -q "VITE_BASE_URL=''" .env.prod 2>/dev/null; then
    echo -e "${RED}❌ 错误: .env.prod 中 VITE_BASE_URL 不是空字符串${NC}"
    echo "当前配置:"
    grep "VITE_BASE_URL" .env.prod || echo "未找到 VITE_BASE_URL"
    exit 1
fi

if ! grep -q "VITE_API_URL=/prod-api" .env.prod 2>/dev/null; then
    echo -e "${RED}❌ 错误: .env.prod 中 VITE_API_URL 不是 /prod-api${NC}"
    echo "当前配置:"
    grep "VITE_API_URL" .env.prod || echo "未找到 VITE_API_URL"
    exit 1
fi

# 检查Nginx配置
if ! grep -q "yudao-server-prod:8080" ../original-yudao-ui/nginx.conf 2>/dev/null; then
    echo -e "${RED}❌ 错误: nginx.conf 中 proxy_pass 配置不正确${NC}"
    echo "应该使用: proxy_pass http://yudao-server-prod:8080;"
    exit 1
fi

echo -e "${GREEN}✅ 配置检查通过${NC}"
echo ""

# 步骤2: 构建前端
echo -e "${YELLOW}【步骤2】构建前端...${NC}"
if ! pnpm build:prod; then
    echo -e "${RED}❌ 前端构建失败${NC}"
    exit 1
fi

if [ ! -d "dist-prod" ]; then
    echo -e "${RED}❌ 前端构建产物不存在${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 前端构建完成${NC}"
echo "  构建产物: $(du -sh dist-prod | cut -f1)"
echo ""

# 步骤3: 构建后端
echo -e "${YELLOW}【步骤3】构建后端...${NC}"
cd "$PROJECT_ROOT"

if ! mvn clean package -DskipTests -pl yudao-server -am > /tmp/maven-build.log 2>&1; then
    echo -e "${RED}❌ 后端构建失败，查看日志:${NC}"
    tail -30 /tmp/maven-build.log
    exit 1
fi

if [ ! -f "yudao-server/target/yudao-server.jar" ]; then
    echo -e "${RED}❌ 后端构建产物不存在${NC}"
    exit 1
fi

JAR_SIZE=$(ls -lh yudao-server/target/yudao-server.jar | awk '{print $5}')
echo -e "${GREEN}✅ 后端构建完成${NC}"
echo "  JAR大小: $JAR_SIZE"
echo ""

# 步骤4: 上传文件
echo -e "${YELLOW}【步骤4】上传文件到云服务器...${NC}"

# 上传前端
echo "  上传前端文件..."
if ! scp -i "$SSH_KEY" -o StrictHostKeyChecking=no -r \
    original-yudao-ui/dist-prod/* \
    "$SERVER:/tmp/dist-prod/" > /dev/null 2>&1; then
    echo -e "${RED}❌ 前端文件上传失败${NC}"
    exit 1
fi

# 上传后端
echo "  上传后端jar包..."
if ! scp -i "$SSH_KEY" -o StrictHostKeyChecking=no \
    yudao-server/target/yudao-server.jar \
    "$SERVER:/opt/erp-system/yudao-server/target/yudao-server.jar" > /dev/null 2>&1; then
    echo -e "${RED}❌ 后端jar包上传失败${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 文件上传完成${NC}"
echo ""

# 步骤5: 部署
echo -e "${YELLOW}【步骤5】部署到云服务器...${NC}"
ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SERVER" << 'EOF'
set -e

# 更新前端
echo "  更新前端容器..."
if ! docker cp /tmp/dist-prod/. yudao-admin-prod:/usr/share/nginx/html/ > /dev/null 2>&1; then
    echo "❌ 前端文件更新失败"
    exit 1
fi
echo "  ✅ 前端更新完成"

# 更新后端
echo "  更新后端容器..."
if ! docker cp /opt/erp-system/yudao-server/target/yudao-server.jar \
    yudao-server-prod:/yudao-server/app.jar > /dev/null 2>&1; then
    echo "❌ 后端jar包更新失败"
    exit 1
fi

echo "  重启后端服务..."
docker restart yudao-server-prod > /dev/null 2>&1

echo "  等待服务启动（60秒）..."
sleep 60

# 验证服务
echo ""
echo "【验证】服务状态:"
BACKEND_STATUS=$(docker exec yudao-server-prod curl -s -o /dev/null -w '%{http_code}' \
    http://localhost:8080/actuator/health 2>/dev/null || echo "000")
FRONTEND_STATUS=$(docker exec yudao-admin-prod curl -s -o /dev/null -w '%{http_code}' \
    http://localhost/health 2>/dev/null || echo "000")

if [ "$BACKEND_STATUS" = "200" ]; then
    echo "  ✅ 后端服务: 正常 ($BACKEND_STATUS)"
else
    echo "  ❌ 后端服务: 异常 ($BACKEND_STATUS)"
    echo "  查看日志: docker logs --tail=50 yudao-server-prod"
fi

if [ "$FRONTEND_STATUS" = "200" ]; then
    echo "  ✅ 前端服务: 正常 ($FRONTEND_STATUS)"
else
    echo "  ❌ 前端服务: 异常 ($FRONTEND_STATUS)"
fi

# 检查容器状态
echo ""
echo "【容器状态】:"
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" | grep -E "NAME|yudao"
EOF

DEPLOY_RESULT=$?

echo ""
if [ $DEPLOY_RESULT -eq 0 ]; then
    echo "=========================================="
    echo -e "${GREEN}  部署完成！${NC}"
    echo "=========================================="
    echo ""
    echo "访问地址: http://$SERVER_IP"
    echo ""
    echo "验证步骤:"
    echo "1. 访问 http://$SERVER_IP"
    echo "2. 测试登录功能（验证码已禁用）"
    echo "3. 检查产品管理等模块功能"
    echo ""
else
    echo -e "${RED}部署过程中出现错误，请检查上述输出${NC}"
    exit 1
fi

