#!/bin/bash

# ERP系统 - 火山引擎云服务器部署脚本
# 使用方法: ./deploy-volcano.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
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

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        log_error "$1 未安装，请先安装 $1"
        exit 1
    fi
}

# 检查是否为root用户
check_root() {
    if [ "$EUID" -ne 0 ]; then 
        log_error "请使用root用户运行此脚本"
        exit 1
    fi
}

# 主函数
main() {
    log_info "开始部署ERP系统到火山引擎云服务器..."
    
    # 检查root权限
    check_root
    
    # 检查必要的命令
    log_info "检查必要的命令..."
    check_command docker
    check_command docker-compose
    
    # 获取项目目录
    SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
    PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
    DOCKER_DIR="$PROJECT_DIR/script/docker"
    
    log_info "项目目录: $PROJECT_DIR"
    log_info "Docker配置目录: $DOCKER_DIR"
    
    # 检查.env文件
    if [ ! -f "$DOCKER_DIR/.env" ]; then
        log_warn ".env 文件不存在，从示例文件创建..."
        if [ -f "$DOCKER_DIR/.env.prod.example" ]; then
            cp "$DOCKER_DIR/.env.prod.example" "$DOCKER_DIR/.env"
            log_warn "请编辑 $DOCKER_DIR/.env 文件，修改配置后重新运行此脚本"
            exit 1
        else
            log_error ".env 文件不存在且没有示例文件"
            exit 1
        fi
    fi
    
    # 检查密码是否已修改
    if grep -q "请修改为强密码" "$DOCKER_DIR/.env"; then
        log_error "请先修改 .env 文件中的默认密码！"
        exit 1
    fi
    
    # 检查并构建后端JAR
    if [ ! -f "$PROJECT_DIR/yudao-server/target/yudao-server.jar" ]; then
        log_warn "后端JAR文件不存在，开始构建..."
        if command -v mvn &> /dev/null; then
            cd "$PROJECT_DIR"
            log_info "使用Maven构建后端..."
            mvn clean package -DskipTests
        else
            log_error "Maven未安装，无法构建后端JAR文件"
            log_error "请先安装Maven或手动构建后端项目"
            exit 1
        fi
    else
        log_info "后端JAR文件已存在，跳过构建"
    fi
    
    # 进入docker目录
    cd "$DOCKER_DIR"
    
    # 停止现有服务
    log_info "停止现有服务..."
    docker-compose -f docker-compose.prod.yml down || true
    
    # 构建镜像
    log_info "构建Docker镜像..."
    docker-compose -f docker-compose.prod.yml build --no-cache
    
    # 启动服务
    log_info "启动服务..."
    docker-compose -f docker-compose.prod.yml up -d
    
    # 等待服务启动
    log_info "等待服务启动..."
    sleep 10
    
    # 检查服务状态
    log_info "检查服务状态..."
    docker-compose -f docker-compose.prod.yml ps
    
    # 显示日志
    log_info "显示服务日志（按Ctrl+C退出）..."
    docker-compose -f docker-compose.prod.yml logs -f
}

# 运行主函数
main "$@"

