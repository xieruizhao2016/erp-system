#!/bin/bash

# 快速部署脚本 - 适用于火山引擎云服务器
# 此脚本会自动安装必要的软件并部署项目

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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

# 检测操作系统
detect_os() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        OS=$ID
        VER=$VERSION_ID
    else
        log_error "无法检测操作系统"
        exit 1
    fi
    log_info "检测到操作系统: $OS $VER"
}

# 安装Docker
install_docker() {
    if command -v docker &> /dev/null; then
        log_info "Docker 已安装: $(docker --version)"
        return
    fi
    
    log_step "安装 Docker..."
    curl -fsSL https://get.docker.com | bash
    systemctl start docker
    systemctl enable docker
    log_info "Docker 安装完成"
}

# 安装Docker Compose
install_docker_compose() {
    if command -v docker-compose &> /dev/null; then
        log_info "Docker Compose 已安装: $(docker-compose --version)"
        return
    fi
    
    log_step "安装 Docker Compose..."
    COMPOSE_VERSION=$(curl -s https://api.github.com/repos/docker/compose/releases/latest | grep 'tag_name' | cut -d\" -f4)
    curl -L "https://github.com/docker/compose/releases/download/${COMPOSE_VERSION}/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    chmod +x /usr/local/bin/docker-compose
    log_info "Docker Compose 安装完成"
}

# 配置防火墙
configure_firewall() {
    log_step "配置防火墙..."
    
    if command -v firewall-cmd &> /dev/null; then
        firewall-cmd --permanent --add-port=80/tcp
        firewall-cmd --permanent --add-port=48080/tcp
        firewall-cmd --reload
        log_info "防火墙配置完成 (firewalld)"
    elif command -v ufw &> /dev/null; then
        ufw allow 80/tcp
        ufw allow 48080/tcp
        log_info "防火墙配置完成 (ufw)"
    else
        log_warn "未检测到防火墙管理工具，请手动配置防火墙规则"
    fi
}

# 主函数
main() {
    log_info "=========================================="
    log_info "ERP系统 - 火山引擎云服务器快速部署脚本"
    log_info "=========================================="
    
    # 检查root权限
    if [ "$EUID" -ne 0 ]; then 
        log_error "请使用root用户运行此脚本"
        exit 1
    fi
    
    # 检测操作系统
    detect_os
    
    # 安装必要软件
    install_docker
    install_docker_compose
    
    # 配置防火墙
    configure_firewall
    
    log_info "=========================================="
    log_info "环境准备完成！"
    log_info "=========================================="
    log_info "下一步："
    log_info "1. 将项目文件上传到服务器"
    log_info "2. 进入项目目录: cd /opt/erp-system"
    log_info "3. 配置环境变量: cp script/docker/env.prod.example script/docker/.env"
    log_info "4. 编辑 .env 文件，修改密码等配置"
    log_info "5. 运行部署脚本: ./scripts/deploy-volcano.sh"
    log_info "=========================================="
}

main "$@"

