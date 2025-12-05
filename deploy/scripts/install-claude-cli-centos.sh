#!/bin/bash

# Claude CLI 安装脚本 - CentOS Stream 9 专用
# 使用方法: chmod +x install-claude-cli-centos.sh && ./install-claude-cli-centos.sh

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${GREEN}=== Claude CLI 安装脚本 (CentOS Stream 9) ===${NC}"
echo ""

# 检查操作系统
echo -e "${YELLOW}步骤1: 检查操作系统...${NC}"
if [ -f /etc/os-release ]; then
    . /etc/os-release
    echo "操作系统: $PRETTY_NAME"
    if [[ "$ID" != "centos" ]] && [[ "$ID" != "rhel" ]]; then
        echo -e "${YELLOW}⚠️  警告: 此脚本专为 CentOS/RHEL 设计，当前系统可能不兼容${NC}"
        read -p "是否继续? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
else
    echo -e "${RED}错误: 无法检测操作系统${NC}"
    exit 1
fi

# 检查是否为root用户（可选，因为可以使用nvm避免权限问题）
echo ""
echo -e "${YELLOW}步骤2: 检查用户权限...${NC}"
if [ "$EUID" -eq 0 ]; then
    echo -e "${YELLOW}⚠️  检测到root用户，建议使用普通用户安装以避免权限问题${NC}"
    read -p "是否继续? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# 检查是否已安装 Node.js
echo ""
echo -e "${YELLOW}步骤3: 检查 Node.js 安装...${NC}"
if command -v node &> /dev/null; then
    NODE_VERSION=$(node --version | sed 's/v//')
    echo "当前 Node.js 版本: v$NODE_VERSION"
    
    # 检查版本是否 >= 18
    MAJOR_VERSION=$(echo $NODE_VERSION | cut -d. -f1)
    if [ "$MAJOR_VERSION" -ge 18 ]; then
        echo -e "${GREEN}✅ Node.js 版本符合要求 (>= 18)${NC}"
        SKIP_NODE_INSTALL=true
    else
        echo -e "${RED}❌ Node.js 版本过低，需要 >= 18，当前: $NODE_VERSION${NC}"
        SKIP_NODE_INSTALL=false
    fi
else
    echo -e "${YELLOW}未检测到 Node.js${NC}"
    SKIP_NODE_INSTALL=false
fi

# 安装 Node.js（如果需要）
if [ "$SKIP_NODE_INSTALL" = false ]; then
    echo ""
    echo -e "${YELLOW}步骤4: 安装 Node.js 18...${NC}"
    
    # 检查是否已安装 nvm
    if [ -s "$HOME/.nvm/nvm.sh" ]; then
        echo "检测到 nvm，使用 nvm 安装 Node.js..."
        source "$HOME/.nvm/nvm.sh"
        nvm install 18
        nvm use 18
        nvm alias default 18
        echo -e "${GREEN}✅ 使用 nvm 安装 Node.js 18 成功${NC}"
    else
        echo "使用 NodeSource 仓库安装 Node.js 18..."
        
        # 安装必要的工具
        if ! command -v curl &> /dev/null; then
            echo "安装 curl..."
            sudo dnf install -y curl
        fi
        
        # 添加 NodeSource 仓库
        echo "添加 NodeSource 仓库..."
        curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
        
        # 安装 Node.js
        echo "安装 Node.js..."
        sudo dnf install -y nodejs
        
        # 验证安装
        if command -v node &> /dev/null; then
            NODE_VERSION=$(node --version)
            echo -e "${GREEN}✅ Node.js 安装成功: $NODE_VERSION${NC}"
        else
            echo -e "${RED}❌ Node.js 安装失败${NC}"
            exit 1
        fi
    fi
else
    echo ""
    echo -e "${GREEN}✅ 跳过 Node.js 安装（已安装且版本符合要求）${NC}"
fi

# 验证 Node.js 和 npm
echo ""
echo -e "${YELLOW}步骤5: 验证 Node.js 和 npm...${NC}"
NODE_VERSION=$(node --version)
NPM_VERSION=$(npm --version)
echo "Node.js: $NODE_VERSION"
echo "npm: $NPM_VERSION"

# 配置 npm（避免权限问题）
echo ""
echo -e "${YELLOW}步骤6: 配置 npm...${NC}"
if [ "$EUID" -ne 0 ]; then
    # 非root用户，配置npm全局目录
    NPM_PREFIX="$HOME/.npm-global"
    mkdir -p "$NPM_PREFIX"
    npm config set prefix "$NPM_PREFIX"
    
    # 添加到 PATH（如果还没有）
    if ! echo "$PATH" | grep -q "$NPM_PREFIX/bin"; then
        echo "export PATH=$NPM_PREFIX/bin:\$PATH" >> "$HOME/.bashrc"
        export PATH="$NPM_PREFIX/bin:$PATH"
        echo -e "${GREEN}✅ npm 全局目录已配置: $NPM_PREFIX${NC}"
        echo -e "${BLUE}提示: 请运行 'source ~/.bashrc' 或重新登录以使PATH生效${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  root用户，使用系统全局安装（不推荐）${NC}"
fi

# 检查是否已安装 Claude CLI
echo ""
echo -e "${YELLOW}步骤7: 检查 Claude CLI 安装...${NC}"
if command -v claude &> /dev/null; then
    CLAUDE_VERSION=$(claude --version 2>/dev/null || echo "已安装")
    echo -e "${GREEN}✅ Claude CLI 已安装: $CLAUDE_VERSION${NC}"
    read -p "是否重新安装? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo -e "${GREEN}跳过安装${NC}"
        exit 0
    fi
    echo "卸载旧版本..."
    npm uninstall -g @anthropic-ai/claude-code 2>/dev/null || true
fi

# 安装 Claude CLI
echo ""
echo -e "${YELLOW}步骤8: 安装 Claude CLI...${NC}"
echo "正在从 npm 安装 @anthropic-ai/claude-code..."

if npm install -g @anthropic-ai/claude-code; then
    echo -e "${GREEN}✅ Claude CLI 安装成功${NC}"
else
    echo -e "${RED}❌ Claude CLI 安装失败${NC}"
    echo ""
    echo "可能的解决方案："
    echo "1. 检查网络连接"
    echo "2. 配置 npm 镜像源: npm config set registry https://registry.npmmirror.com"
    echo "3. 使用 sudo（不推荐）: sudo npm install -g @anthropic-ai/claude-code"
    exit 1
fi

# 验证安装
echo ""
echo -e "${YELLOW}步骤9: 验证安装...${NC}"
if command -v claude &> /dev/null; then
    CLAUDE_VERSION=$(claude --version 2>/dev/null || echo "未知版本")
    echo -e "${GREEN}✅ Claude CLI 安装成功！${NC}"
    echo "版本: $CLAUDE_VERSION"
    echo ""
    echo -e "${BLUE}安装路径: $(which claude)${NC}"
else
    echo -e "${RED}❌ Claude CLI 未在 PATH 中找到${NC}"
    echo ""
    echo "可能的解决方案："
    echo "1. 运行: source ~/.bashrc"
    echo "2. 检查 npm 全局目录: npm config get prefix"
    echo "3. 手动添加到 PATH"
    exit 1
fi

# 显示使用说明
echo ""
echo -e "${GREEN}=== 安装完成 ===${NC}"
echo ""
echo -e "${BLUE}下一步操作:${NC}"
echo "1. 首次使用需要认证，运行: claude"
echo "2. 按照提示在浏览器中完成 OAuth 认证"
echo "3. 认证完成后即可使用 Claude CLI"
echo ""
echo -e "${BLUE}使用示例:${NC}"
echo "  claude --help              # 查看帮助"
echo "  claude \"你的问题\"          # 与 Claude 对话"
echo ""
echo -e "${YELLOW}注意:${NC}"
echo "- 如果 'claude' 命令未找到，请运行: source ~/.bashrc"
echo "- 或者重新登录终端"
echo ""

