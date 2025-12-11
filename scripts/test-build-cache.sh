#!/bin/bash

# 测试 Docker 构建缓存效果
# 使用方法: ./scripts/test-build-cache.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Docker 构建缓存效果测试${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# 获取项目目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
DOCKER_DIR="$PROJECT_DIR/script/docker"

cd "$DOCKER_DIR"

echo -e "${YELLOW}步骤1: 首次构建（无缓存）${NC}"
echo "这将安装所有依赖并构建前端..."
echo ""
echo "开始时间: $(date '+%Y-%m-%d %H:%M:%S')"
START_TIME=$(date +%s)

docker-compose -f docker-compose.prod.yml build admin 2>&1 | grep -E "(Step|CACHED|Using cache|RUN|COPY)" | tail -20

END_TIME=$(date +%s)
DURATION=$((END_TIME - START_TIME))
echo ""
echo -e "${GREEN}✅ 首次构建完成${NC}"
echo -e "耗时: ${DURATION} 秒"
echo ""

# 等待一下
sleep 2

echo -e "${YELLOW}步骤2: 模拟修改代码（只修改一个文件）${NC}"
# 创建一个测试文件来模拟代码修改
TEST_FILE="$PROJECT_DIR/original-yudao-ui/src/test-cache.txt"
echo "Build cache test - $(date)" > "$TEST_FILE"
echo "已创建测试文件: $TEST_FILE"
echo ""

echo "开始时间: $(date '+%Y-%m-%d %H:%M:%S')"
START_TIME2=$(date +%s)

echo -e "${BLUE}开始第二次构建（应该使用缓存）...${NC}"
docker-compose -f docker-compose.prod.yml build admin 2>&1 | grep -E "(Step|CACHED|Using cache|RUN|COPY)" | tail -20

END_TIME2=$(date +%s)
DURATION2=$((END_TIME2 - START_TIME2))
echo ""
echo -e "${GREEN}✅ 第二次构建完成${NC}"
echo -e "耗时: ${DURATION2} 秒"
echo ""

# 清理测试文件
rm -f "$TEST_FILE"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}测试结果对比${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "首次构建（无缓存）: ${DURATION} 秒"
echo -e "第二次构建（使用缓存）: ${DURATION2} 秒"
echo ""

if [ $DURATION2 -lt $DURATION ]; then
    SAVED=$((DURATION - DURATION2))
    PERCENT=$((SAVED * 100 / DURATION))
    echo -e "${GREEN}✅ 缓存生效！节省时间: ${SAVED} 秒 (${PERCENT}%)${NC}"
    
    if [ $PERCENT -gt 50 ]; then
        echo -e "${GREEN}🎉 优化效果显著！${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  缓存可能未生效，请检查构建日志${NC}"
fi

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}缓存层分析${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "查看构建历史（最后10层）:"
docker history yudao-admin:latest --format "table {{.CreatedBy}}\t{{.Size}}" | head -12
echo ""
echo -e "${YELLOW}提示:${NC}"
echo "- 如果看到 'CACHED' 标记，说明该层使用了缓存"
echo "- 依赖安装层（pnpm install）应该被缓存"
echo "- 只有代码复制和构建层会重新执行"

