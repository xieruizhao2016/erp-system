# 在云服务器上安装 Claude CLI 指南

## ✅ 可以安装

是的，您可以在云服务器的终端上安装并运行 Claude CLI。

## 📋 系统要求

在安装之前，请确保您的云服务器满足以下要求：

- **操作系统**：macOS 10.15+、Ubuntu 20.04+/Debian 10+、CentOS Stream 9/RHEL 9+，或通过 WSL 在 Windows 10+ 上运行
- **硬件**：至少 4GB RAM
- **软件**：Node.js 18+（必需）
- **网络**：需要互联网连接进行身份验证和 AI 处理

**当前服务器信息：**
- **操作系统**: CentOS Stream 9 ✅
- **公网IP**: 115.190.240.137
- **推荐方法**: 使用自动安装脚本（见下方）

## 🔧 安装步骤

### 🚀 方法1: 使用自动安装脚本（推荐 - CentOS Stream 9）

如果您使用的是 **CentOS Stream 9**（如火山引擎云服务器），可以使用我们提供的自动安装脚本：

```bash
# 1. 上传脚本到服务器（如果脚本在本地）
# 在本地执行：
scp -i ~/Documents/huoshan-ssh.pem deploy/scripts/install-claude-cli-centos.sh root@115.190.240.137:/tmp/

# 2. SSH 连接到服务器
ssh -i ~/Documents/huoshan-ssh.pem root@115.190.240.137

# 3. 在服务器上执行脚本
chmod +x /tmp/install-claude-cli-centos.sh
/tmp/install-claude-cli-centos.sh
```

**脚本功能：**
- ✅ 自动检测操作系统版本
- ✅ 自动安装 Node.js 18+（如果未安装）
- ✅ 自动配置 npm 避免权限问题
- ✅ 自动安装 Claude CLI
- ✅ 验证安装并显示使用说明

### 📝 方法2: 手动安装

#### 步骤1: 检查 Node.js 版本

首先检查服务器是否已安装 Node.js，以及版本是否符合要求：

```bash
node --version
```

如果未安装或版本低于 18，需要先安装 Node.js 18+。

#### 安装 Node.js（如果未安装）

**Ubuntu/Debian 系统：**

```bash
# 使用 NodeSource 仓库安装 Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# 验证安装
node --version
npm --version
```

**CentOS Stream 9 / RHEL 系统：**

```bash
# 使用 NodeSource 仓库安装 Node.js 18
curl -fsSL https://rpm.nodesource.com/setup_18.x | sudo bash -
sudo dnf install -y nodejs  # CentOS Stream 9 使用 dnf，不是 yum

# 验证安装
node --version
npm --version
```

**注意：** CentOS Stream 9 使用 `dnf` 而不是 `yum` 作为包管理器。

#### 步骤2: 安装 Claude CLI

在终端中运行以下命令：

```bash
npm install -g @anthropic-ai/claude-code
```

**⚠️ 重要提示：**
- 不要使用 `sudo` 进行全局安装，以避免权限问题和安全风险
- 如果遇到权限问题，可以配置 npm 使用其他目录，或使用 nvm 管理 Node.js

#### 如果遇到权限问题

**方法1：配置 npm 全局安装目录（推荐）**

```bash
# 创建全局安装目录
mkdir ~/.npm-global

# 配置 npm 使用新目录
npm config set prefix '~/.npm-global'

# 添加到 PATH（添加到 ~/.bashrc 或 ~/.zshrc）
echo 'export PATH=~/.npm-global/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# 重新安装
npm install -g @anthropic-ai/claude-code
```

**方法2：使用 nvm（推荐用于开发环境）**

```bash
# 安装 nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash

# 重新加载 shell
source ~/.bashrc

# 安装 Node.js 18
nvm install 18
nvm use 18

# 安装 Claude CLI
npm install -g @anthropic-ai/claude-code
```

### 步骤3: 验证安装

安装完成后，运行以下命令检查安装是否成功：

```bash
claude --version
```

如果显示版本号，说明安装成功。

## 🔐 首次使用认证

首次运行 `claude` 命令时，系统会提示您完成一次性 OAuth 认证流程：

```bash
claude
```

**认证步骤：**

1. 运行命令后，会显示一个认证 URL
2. 在本地浏览器中打开该 URL
3. 登录您的 Anthropic 账户
4. 授权 Claude CLI 访问
5. 返回服务器终端，认证完成

**注意：**
- 如果服务器没有图形界面，您需要在本地电脑的浏览器中完成认证
- 认证信息会保存在服务器上，后续使用无需再次认证

## 📝 使用示例

安装并认证完成后，您可以在服务器终端使用 Claude CLI：

```bash
# 查看帮助
claude --help

# 与 Claude 对话
claude "帮我检查这个文件的代码"

# 在特定目录下使用
cd /opt/erp-system
claude "分析这个项目的结构"
```

## ⚠️ 常见问题

### 1. 安装失败：权限被拒绝

**错误信息：**
```
EACCES: permission denied
```

**解决方法：**
- 使用上述的 npm 配置方法，避免使用 sudo
- 或使用 nvm 管理 Node.js 环境

### 2. 找不到 claude 命令

**原因：** PATH 环境变量未包含 npm 全局安装目录

**解决方法：**
```bash
# 查找 npm 全局安装目录
npm config get prefix

# 将结果添加到 PATH（例如：/usr/local）
export PATH=/usr/local/bin:$PATH

# 永久添加到 ~/.bashrc 或 ~/.zshrc
echo 'export PATH=/usr/local/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 3. Node.js 版本过低

**错误信息：**
```
requires Node.js version >= 18
```

**解决方法：**
- 升级 Node.js 到 18 或更高版本
- 使用 nvm 管理多个 Node.js 版本

### 4. 网络连接问题

**问题：** 无法下载包或进行认证

**解决方法：**
- 检查服务器网络连接
- 配置 npm 镜像源（如果需要）：
  ```bash
  npm config set registry https://registry.npmmirror.com
  ```
- 检查防火墙设置，确保可以访问外部网络

### 5. 认证失败

**问题：** 无法在浏览器中打开认证链接

**解决方法：**
- 确保服务器可以访问互联网
- 手动复制认证 URL 到本地浏览器
- 检查 Anthropic 服务是否可访问

### 6. CentOS Stream 9 特定问题

**问题1：使用 yum 命令失败**

**错误信息：**
```
bash: yum: command not found
```

**解决方法：**
CentOS Stream 9 使用 `dnf` 而不是 `yum`：
```bash
# 使用 dnf 代替 yum
sudo dnf install -y nodejs
```

**问题2：网络连接问题（无法访问 npm registry）**

如果服务器无法访问 npm registry，可以配置国内镜像源：
```bash
# 配置 npm 使用国内镜像
npm config set registry https://registry.npmmirror.com

# 验证配置
npm config get registry

# 然后重新安装
npm install -g @anthropic-ai/claude-code
```

**问题3：防火墙阻止访问**

如果启用了 firewalld，确保允许出站连接：
```bash
# 检查防火墙状态
sudo firewall-cmd --state

# 如果防火墙开启，确保出站规则允许（通常默认允许）
# 检查出站规则
sudo firewall-cmd --list-all
```

## 🔒 安全建议

1. **不要使用 sudo 安装**：避免权限问题和安全风险
2. **定期更新**：保持 Claude CLI 为最新版本
   ```bash
   npm update -g @anthropic-ai/claude-code
   ```
3. **保护认证信息**：认证信息存储在用户目录，确保目录权限正确
4. **网络访问控制**：如果可能，限制服务器只能访问必要的网络资源

## 📚 参考资源

- [Claude CLI 官方文档](https://docs.anthropic.com/claude/docs/claude-cli)
- [Node.js 官方安装指南](https://nodejs.org/)
- [npm 官方文档](https://docs.npmjs.com/)

---

**提示：** 如果遇到其他问题，建议参考官方文档获取详细的故障排除指南。

