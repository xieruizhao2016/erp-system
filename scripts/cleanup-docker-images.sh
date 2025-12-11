#!/bin/bash

# Docker 镜像清理脚本
# 安全地清理未使用的 Docker 镜像、容器、卷和网络
# 使用方法: ./scripts/cleanup-docker-images.sh [--dry-run]

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查是否为 dry-run 模式
DRY_RUN=false
if [[ "$1" == "--dry-run" ]]; then
    DRY_RUN=true
    echo -e "${YELLOW}⚠️  运行在 DRY-RUN 模式（不会实际删除）${NC}"
    echo ""
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Docker 资源清理工具${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# 显示当前磁盘使用情况
echo -e "${YELLOW}当前 Docker 磁盘使用情况：${NC}"
docker system df
echo ""

# 1. 清理悬空镜像（dangling images）
echo -e "${YELLOW}1. 检查悬空镜像...${NC}"
DANGLING_IMAGES=$(docker images -f "dangling=true" -q)
if [ -z "$DANGLING_IMAGES" ]; then
    echo -e "${GREEN}  ✓ 没有悬空镜像${NC}"
else
    echo -e "${YELLOW}  发现悬空镜像：${NC}"
    docker images -f "dangling=true" --format "    - {{.ID}} {{.Repository}}:{{.Tag}}"
    if [ "$DRY_RUN" = false ]; then
        docker rmi $DANGLING_IMAGES 2>/dev/null || true
        echo -e "${GREEN}  ✓ 已删除悬空镜像${NC}"
    else
        echo -e "${BLUE}  [DRY-RUN] 将删除这些悬空镜像${NC}"
    fi
fi
echo ""

# 2. 清理未使用的镜像（未被任何容器使用）
echo -e "${YELLOW}2. 检查未使用的镜像...${NC}"
echo "  分析哪些镜像未被使用..."
UNUSED_IMAGES=()

# 获取所有镜像
ALL_IMAGES=$(docker images --format "{{.Repository}}:{{.Tag}}")

for img in $ALL_IMAGES; do
    # 跳过 <none> 标签
    if [[ "$img" == *"<none>"* ]]; then
        continue
    fi
    
    # 检查是否有容器使用此镜像
    USED=$(docker ps -a --filter ancestor="$img" -q)
    if [ -z "$USED" ]; then
        # 检查是否在 docker-compose 配置中使用
        # 这里我们只标记，不自动删除，让用户确认
        UNUSED_IMAGES+=("$img")
    fi
done

if [ ${#UNUSED_IMAGES[@]} -eq 0 ]; then
    echo -e "${GREEN}  ✓ 所有镜像都在使用中或需要保留${NC}"
else
    echo -e "${YELLOW}  发现可能未使用的镜像：${NC}"
    for img in "${UNUSED_IMAGES[@]}"; do
        SIZE=$(docker images "$img" --format "{{.Size}}")
        echo "    - $img ($SIZE)"
    done
    echo -e "${YELLOW}  ⚠️  这些镜像当前未被容器使用，但可能是：${NC}"
    echo "    - 应用镜像（yudao-server, yudao-admin）- 建议保留"
    echo "    - 旧版本镜像 - 可以删除"
    echo ""
    echo -e "${YELLOW}  建议手动检查后删除：${NC}"
    echo "    docker rmi <镜像名>"
fi
echo ""

# 3. 清理未使用的容器
echo -e "${YELLOW}3. 检查未使用的容器...${NC}"
STOPPED_CONTAINERS=$(docker ps -a -f "status=exited" -q)
if [ -z "$STOPPED_CONTAINERS" ]; then
    echo -e "${GREEN}  ✓ 没有已停止的容器${NC}"
else
    COUNT=$(echo "$STOPPED_CONTAINERS" | wc -l)
    echo -e "${YELLOW}  发现 $COUNT 个已停止的容器${NC}"
    if [ "$DRY_RUN" = false ]; then
        docker rm $STOPPED_CONTAINERS 2>/dev/null || true
        echo -e "${GREEN}  ✓ 已删除已停止的容器${NC}"
    else
        echo -e "${BLUE}  [DRY-RUN] 将删除这些容器${NC}"
    fi
fi
echo ""

# 4. 清理未使用的卷
echo -e "${YELLOW}4. 检查未使用的卷...${NC}"
UNUSED_VOLUMES=$(docker volume ls -q -f dangling=true)
if [ -z "$UNUSED_VOLUMES" ]; then
    echo -e "${GREEN}  ✓ 没有未使用的卷${NC}"
else
    echo -e "${YELLOW}  发现未使用的卷：${NC}"
    docker volume ls -f dangling=true --format "    - {{.Name}}"
    if [ "$DRY_RUN" = false ]; then
        docker volume rm $UNUSED_VOLUMES 2>/dev/null || true
        echo -e "${GREEN}  ✓ 已删除未使用的卷${NC}"
    else
        echo -e "${BLUE}  [DRY-RUN] 将删除这些卷${NC}"
    fi
fi
echo ""

# 5. 清理未使用的网络
echo -e "${YELLOW}5. 检查未使用的网络...${NC}"
UNUSED_NETWORKS=$(docker network ls -q -f dangling=true)
if [ -z "$UNUSED_NETWORKS" ]; then
    echo -e "${GREEN}  ✓ 没有未使用的网络${NC}"
else
    echo -e "${YELLOW}  发现未使用的网络：${NC}"
    docker network ls -f dangling=true --format "    - {{.Name}}"
    if [ "$DRY_RUN" = false ]; then
        docker network rm $UNUSED_NETWORKS 2>/dev/null || true
        echo -e "${GREEN}  ✓ 已删除未使用的网络${NC}"
    else
        echo -e "${BLUE}  [DRY-RUN] 将删除这些网络${NC}"
    fi
fi
echo ""

# 6. 清理构建缓存
echo -e "${YELLOW}6. 检查构建缓存...${NC}"
BUILD_CACHE_SIZE=$(docker system df | grep "Build Cache" | awk '{print $4}')
if [ "$BUILD_CACHE_SIZE" = "0B" ]; then
    echo -e "${GREEN}  ✓ 没有构建缓存${NC}"
else
    echo -e "${YELLOW}  发现构建缓存：$BUILD_CACHE_SIZE${NC}"
    if [ "$DRY_RUN" = false ]; then
        docker builder prune -f
        echo -e "${GREEN}  ✓ 已清理构建缓存${NC}"
    else
        echo -e "${BLUE}  [DRY-RUN] 将清理构建缓存${NC}"
    fi
fi
echo ""

# 显示清理后的磁盘使用情况
if [ "$DRY_RUN" = false ]; then
    echo -e "${YELLOW}清理后的磁盘使用情况：${NC}"
    docker system df
    echo ""
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}✅ 清理完成！${NC}"
echo -e "${BLUE}========================================${NC}"

# 显示建议
echo ""
echo -e "${YELLOW}💡 建议：${NC}"
echo "1. 应用镜像（yudao-server, yudao-admin）建议保留"
echo "2. 如果版本不匹配（如 mysql:8.4.7 vs mysql:8），可以删除旧版本"
echo "3. 使用 'docker system prune -a' 可以清理所有未使用的资源（谨慎使用）"
echo "4. 定期运行此脚本保持系统清洁"

