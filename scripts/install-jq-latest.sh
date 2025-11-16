#!/bin/bash
# 安装最新版本jq脚本
# 使用方法: ./scripts/install-jq-latest.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}安装最新版本jq${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查当前版本
CURRENT_VERSION=$(jq --version 2>/dev/null || echo "未安装")
echo "当前jq版本: ${CURRENT_VERSION}"
echo ""

# 方法1: 尝试使用Homebrew安装（推荐）
echo -e "${YELLOW}[方法1] 尝试使用Homebrew安装...${NC}"

# 检查Homebrew是否可用
if command -v brew &> /dev/null; then
    echo "检测到Homebrew，尝试安装jq..."
    
    # 等待Homebrew进程完成（最多等待2分钟）
    echo "检查是否有其他Homebrew进程在运行..."
    BREW_PROCESSES=$(ps aux | grep -i "brew" | grep -v grep | wc -l | tr -d ' ')
    
    if [ "$BREW_PROCESSES" -gt 0 ]; then
        echo -e "${YELLOW}检测到Homebrew进程正在运行，等待完成...${NC}"
        for i in {1..24}; do
            if ! ps aux | grep -i "brew.*vendor-install" | grep -v grep > /dev/null; then
                echo "Homebrew进程已完成"
                break
            fi
            echo "等待中... ($i/24)"
            sleep 5
        done
    fi
    
    # 尝试安装
    if brew install jq 2>&1; then
        INSTALLED_VERSION=$(brew list jq > /dev/null 2>&1 && /usr/local/bin/jq --version 2>/dev/null || /opt/homebrew/bin/jq --version 2>/dev/null || echo "")
        if [ -n "$INSTALLED_VERSION" ]; then
            echo -e "${GREEN}✓ 通过Homebrew安装成功！${NC}"
            echo "安装版本: ${INSTALLED_VERSION}"
            echo ""
            echo "jq路径:"
            which -a jq | grep -v "/usr/bin/jq" | head -1 || which jq
            exit 0
        fi
    else
        echo -e "${YELLOW}Homebrew安装失败，尝试其他方法...${NC}"
    fi
else
    echo -e "${YELLOW}Homebrew未安装，跳过此方法${NC}"
fi

echo ""

# 方法2: 手动下载并安装
echo -e "${YELLOW}[方法2] 手动下载最新版本jq...${NC}"

# 检测系统架构
ARCH=$(uname -m)
OS=$(uname -s)

if [ "$OS" = "Darwin" ]; then
    if [ "$ARCH" = "arm64" ]; then
        JQ_BINARY="jq-macos-arm64"
        INSTALL_PATH="/usr/local/bin/jq"
    else
        JQ_BINARY="jq-macos-amd64"
        INSTALL_PATH="/usr/local/bin/jq"
    fi
else
    echo -e "${RED}不支持的操作系统: ${OS}${NC}"
    exit 1
fi

echo "系统架构: ${ARCH}"
echo "目标二进制: ${JQ_BINARY}"
echo ""

# 获取最新版本号
echo "获取最新版本信息..."
LATEST_VERSION=$(curl -s https://api.github.com/repos/stedolan/jq/releases/latest | jq -r '.tag_name' 2>/dev/null || echo "")

if [ -z "$LATEST_VERSION" ]; then
    # 如果jq不可用，使用grep
    LATEST_VERSION=$(curl -s https://api.github.com/repos/stedolan/jq/releases/latest | grep -o '"tag_name": "[^"]*' | cut -d'"' -f4 | head -1)
fi

if [ -z "$LATEST_VERSION" ]; then
    echo -e "${RED}无法获取最新版本号${NC}"
    exit 1
fi

echo "最新版本: ${LATEST_VERSION}"
echo ""

# 下载jq
DOWNLOAD_URL="https://github.com/stedolan/jq/releases/download/${LATEST_VERSION}/${JQ_BINARY}"
TEMP_FILE="/tmp/jq-${LATEST_VERSION}"

echo "下载地址: ${DOWNLOAD_URL}"
echo "保存到: ${TEMP_FILE}"

if curl -L -o "${TEMP_FILE}" "${DOWNLOAD_URL}" 2>&1; then
    echo -e "${GREEN}✓ 下载成功${NC}"
else
    echo -e "${RED}✗ 下载失败${NC}"
    exit 1
fi

# 设置执行权限
chmod +x "${TEMP_FILE}"

# 验证下载的文件
if "${TEMP_FILE}" --version > /dev/null 2>&1; then
    DOWNLOADED_VERSION=$("${TEMP_FILE}" --version)
    echo "下载的版本: ${DOWNLOADED_VERSION}"
else
    echo -e "${RED}✗ 下载的文件无效${NC}"
    rm -f "${TEMP_FILE}"
    exit 1
fi

# 备份现有版本（如果存在且不是系统版本）
if [ -f "${INSTALL_PATH}" ] && [ ! -f "/usr/bin/jq" ] || [ "${INSTALL_PATH}" != "/usr/bin/jq" ]; then
    BACKUP_PATH="${INSTALL_PATH}.backup.$(date +%Y%m%d-%H%M%S)"
    echo "备份现有版本到: ${BACKUP_PATH}"
    sudo mv "${INSTALL_PATH}" "${BACKUP_PATH}" 2>/dev/null || mv "${INSTALL_PATH}" "${BACKUP_PATH}"
fi

# 安装新版本
echo "安装到: ${INSTALL_PATH}"
if sudo cp "${TEMP_FILE}" "${INSTALL_PATH}" 2>/dev/null; then
    sudo chmod +x "${INSTALL_PATH}"
elif cp "${TEMP_FILE}" "${INSTALL_PATH}" 2>/dev/null; then
    chmod +x "${INSTALL_PATH}"
else
    echo -e "${YELLOW}需要管理员权限，请手动执行:${NC}"
    echo "sudo cp ${TEMP_FILE} ${INSTALL_PATH}"
    echo "sudo chmod +x ${INSTALL_PATH}"
    echo ""
    echo "或者安装到用户目录:"
    USER_BIN="${HOME}/.local/bin/jq"
    mkdir -p "${HOME}/.local/bin"
    cp "${TEMP_FILE}" "${USER_BIN}"
    chmod +x "${USER_BIN}"
    INSTALL_PATH="${USER_BIN}"
    echo "已安装到: ${INSTALL_PATH}"
    echo "请将 ${HOME}/.local/bin 添加到PATH环境变量"
fi

# 清理临时文件
rm -f "${TEMP_FILE}"

# 验证安装
if "${INSTALL_PATH}" --version > /dev/null 2>&1; then
    INSTALLED_VERSION=$("${INSTALL_PATH}" --version)
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}安装成功！${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo "安装版本: ${INSTALLED_VERSION}"
    echo "安装路径: ${INSTALL_PATH}"
    echo ""
    echo "验证安装:"
    "${INSTALL_PATH}" --version
    echo ""
    echo "测试JSON解析:"
    echo '{"test":"success"}' | "${INSTALL_PATH}" '.test'
else
    echo -e "${RED}✗ 安装验证失败${NC}"
    exit 1
fi

