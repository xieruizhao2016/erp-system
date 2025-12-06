#!/bin/bash

# 本地构建前端Docker镜像脚本
# 只构建 yudao-admin 前端镜像
# 使用方法: ./build-frontend-local.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

log_step() {
    echo -e "${BLUE}[STEP]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "$1 未安装，请先安装 $1"
        exit 1
    fi
}

# 检查Docker是否运行
check_docker() {
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker未运行，请先启动Docker"
        exit 1
    fi
}

# 主函数
main() {
    log_info "开始本地构建前端Docker镜像..."
    echo ""
    
    # 检查必要的命令
    log_step "检查必要的命令..."
    check_command docker
    check_command docker-compose
    check_docker
    
    # 获取项目目录
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    PROJECT_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"
    DOCKER_DIR="$PROJECT_DIR/script/docker"
    
    log_info "项目目录: $PROJECT_DIR"
    log_info "Docker配置目录: $DOCKER_DIR"
    echo ""
    
    # 检查.env文件（可选，用于构建参数）
    ENV_FILE="$DOCKER_DIR/.env"
    if [ ! -f "$ENV_FILE" ]; then
        log_warn ".env 文件不存在，将使用默认构建参数"
        log_warn "如需自定义构建参数，请先创建 $ENV_FILE 文件"
    fi
    
    # 进入docker目录
    cd "$DOCKER_DIR"
    
    # 构建前端镜像
    log_step "开始构建前端Docker镜像..."
    log_info "这可能需要几分钟时间，请耐心等待..."
    echo ""
    
    if [ -f "$ENV_FILE" ]; then
        log_info "使用 .env 文件中的构建参数"
        docker-compose -f docker-compose.prod.yml --env-file .env build admin
    else
        log_info "使用默认构建参数"
        docker-compose -f docker-compose.prod.yml build admin
    fi
    
    if [ $? -eq 0 ]; then
        echo ""
        log_info "✅ 前端Docker镜像构建成功！"
        echo ""
        log_step "已构建的镜像："
        docker images | grep yudao-admin || docker images
        echo ""
        log_info "下一步操作："
        echo "1. 导出镜像: ./deploy/scripts/export-frontend-image.sh"
        echo "2. 上传镜像到服务器: ./deploy/scripts/upload-frontend-image.sh"
        echo ""
    else
        log_error "❌ 前端Docker镜像构建失败"
        exit 1
    fi
}

# 运行主函数
main "$@"

