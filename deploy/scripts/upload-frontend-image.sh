#!/bin/bash

# 上传前端镜像到服务器并导入脚本
# 使用方法: ./upload-frontend-image.sh [镜像文件路径] [服务器地址]

set -e

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 默认配置
# 优先使用项目目录下的私钥，其次使用用户目录下的私钥
if [ -f "$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)/huoshan-ssh.pem" ]; then
    DEFAULT_SSH_KEY="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)/huoshan-ssh.pem"
elif [ -f "$HOME/Documents/huoshan-ssh.pem" ]; then
    DEFAULT_SSH_KEY="$HOME/Documents/huoshan-ssh.pem"
else
    DEFAULT_SSH_KEY=""
fi
DEFAULT_SERVER="root@115.190.240.137"
DEFAULT_REMOTE_PATH="/tmp"

echo -e "${GREEN}=== 上传前端镜像到服务器 ===${NC}"
echo ""

# 获取镜像文件路径
if [ -z "$1" ]; then
    # 如果没有提供路径，查找最新的导出文件
    EXPORT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)/deploy/exports"
    if [ -d "$EXPORT_DIR" ]; then
        LATEST_FILE=$(ls -t "$EXPORT_DIR"/frontend-image-*.tar.gz 2>/dev/null | head -1)
        if [ -n "$LATEST_FILE" ]; then
            IMAGE_FILE="$LATEST_FILE"
            echo -e "${BLUE}自动选择最新镜像文件: $IMAGE_FILE${NC}"
        else
            echo -e "${RED}错误: 未找到镜像文件${NC}"
            echo "请先执行: ./deploy/scripts/export-frontend-image.sh"
            exit 1
        fi
    else
        echo -e "${RED}错误: 请提供镜像文件路径${NC}"
        echo "使用方法: $0 [镜像文件路径] [服务器地址]"
        exit 1
    fi
else
    IMAGE_FILE="$1"
fi

# 检查文件是否存在
if [ ! -f "$IMAGE_FILE" ]; then
    echo -e "${RED}错误: 镜像文件不存在: $IMAGE_FILE${NC}"
    exit 1
fi

# 获取服务器地址
SERVER="${2:-$DEFAULT_SERVER}"

# 检查SSH密钥
SSH_KEY=""
if [ -n "$DEFAULT_SSH_KEY" ] && [ -f "$DEFAULT_SSH_KEY" ]; then
    SSH_KEY="-i $DEFAULT_SSH_KEY"
    # 确保私钥权限正确
    chmod 400 "$DEFAULT_SSH_KEY" 2>/dev/null || true
elif [ -f "$HOME/.ssh/id_rsa" ]; then
    SSH_KEY="-i $HOME/.ssh/id_rsa"
fi

# 显示文件信息
FILE_SIZE=$(du -h "$IMAGE_FILE" | cut -f1)
FILE_NAME=$(basename "$IMAGE_FILE")
echo -e "${BLUE}镜像文件信息:${NC}"
echo "  文件: $IMAGE_FILE"
echo "  大小: $FILE_SIZE"
echo "  服务器: $SERVER"
echo ""

# 确认上传
read -p "是否继续上传? (y/n) " -n 1 -r
echo
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "已取消"
    exit 1
fi

echo ""
echo -e "${BLUE}步骤1: 上传镜像文件到服务器${NC}"
echo -e "${GREEN}正在上传，请稍候...${NC}"

# 上传文件
if [ -n "$SSH_KEY" ]; then
    scp $SSH_KEY "$IMAGE_FILE" "$SERVER:$DEFAULT_REMOTE_PATH/"
else
    scp "$IMAGE_FILE" "$SERVER:$DEFAULT_REMOTE_PATH/"
fi

if [ $? -ne 0 ]; then
    echo -e "${RED}❌ 上传失败${NC}"
    exit 1
fi

echo -e "${GREEN}✅ 上传成功${NC}"
echo ""

echo -e "${BLUE}步骤2: 在服务器上导入镜像${NC}"
echo -e "${YELLOW}正在SSH到服务器并导入镜像...${NC}"

# 构建SSH命令
REMOTE_FILE="$DEFAULT_REMOTE_PATH/$FILE_NAME"
SSH_CMD="gunzip -c $REMOTE_FILE | docker load && rm -f $REMOTE_FILE && echo '✅ 镜像导入成功' && docker images | grep yudao-admin"

if [ -n "$SSH_KEY" ]; then
    ssh $SSH_KEY "$SERVER" "$SSH_CMD"
else
    ssh "$SERVER" "$SSH_CMD"
fi

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ 镜像导入成功！${NC}"
    echo ""
    echo -e "${BLUE}下一步操作：${NC}"
    echo "1. SSH到服务器: ssh $SSH_KEY $SERVER"
    echo "2. 进入项目目录: cd /opt/erp-system/script/docker"
    echo "3. 重启前端服务: docker-compose -f docker-compose.prod.yml --env-file .env up -d --force-recreate admin"
    echo ""
else
    echo -e "${RED}❌ 镜像导入失败${NC}"
    echo ""
    echo "可以手动执行以下命令："
    echo "  ssh $SSH_KEY $SERVER"
    echo "  gunzip -c $REMOTE_FILE | docker load"
    exit 1
fi

