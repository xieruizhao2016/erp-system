# 云服务器VPN配置指南

## 📋 概述

如果云服务器无法直接访问Docker Hub等国际网站，可以通过安装VPN来解决。本指南提供几种常见的VPN解决方案。

## ⚠️ 重要提示

1. **合规性**: 请确保使用VPN符合当地法律法规和云服务商的使用条款
2. **安全性**: VPN服务可能涉及敏感网络流量，请妥善保管密钥和配置
3. **性能**: VPN会增加网络延迟，可能影响下载速度
4. **成本**: 某些VPN服务需要付费

## 🔧 方案选择

### 方案1: V2Ray（推荐）

**优点:**
- 功能强大，支持多种协议
- 配置灵活
- 性能较好

**缺点:**
- 配置相对复杂
- 需要VPS或VPN服务商

### 方案2: Shadowsocks

**优点:**
- 配置简单
- 轻量级
- 广泛支持

**缺点:**
- 可能被某些网络检测

### 方案3: Clash

**优点:**
- 支持规则分流
- 配置灵活
- 社区活跃

**缺点:**
- 配置较复杂

## 🚀 快速部署 - V2Ray

### 步骤1: 安装V2Ray

```bash
# 使用官方一键安装脚本
bash <(curl -L https://raw.githubusercontent.com/v2fly/fhs-install-v2ray/master/install-release.sh)

# 或者使用第三方脚本（更简单）
bash <(curl -L https://raw.githubusercontent.com/233boy/v2ray/master/install.sh)
```

### 步骤2: 配置V2Ray

如果你有V2Ray服务端配置，编辑配置文件：

```bash
vim /usr/local/etc/v2ray/config.json
```

### 步骤3: 启动服务

```bash
systemctl start v2ray
systemctl enable v2ray
systemctl status v2ray
```

## 🚀 快速部署 - Shadowsocks

### 步骤1: 安装Shadowsocks

```bash
# CentOS/RHEL
yum install -y python3-pip
pip3 install shadowsocks

# 或者使用shadowsocks-libev（推荐）
yum install -y epel-release
yum install -y shadowsocks-libev
```

### 步骤2: 配置Shadowsocks客户端

创建配置文件：

```bash
cat > /etc/shadowsocks-libev/config.json << 'EOF'
{
    "server": "your-vpn-server-ip",
    "server_port": 8388,
    "local_address": "127.0.0.1",
    "local_port": 1080,
    "password": "your-password",
    "timeout": 300,
    "method": "aes-256-gfb"
}
EOF
```

### 步骤3: 启动服务

```bash
systemctl start shadowsocks-libev
systemctl enable shadowsocks-libev
```

## 🔧 配置Docker使用VPN代理

### 方法1: 配置Docker全局代理

```bash
# 创建Docker服务配置目录
mkdir -p /etc/systemd/system/docker.service.d

# 创建代理配置文件
cat > /etc/systemd/system/docker.service.d/http-proxy.conf << 'EOF'
[Service]
Environment="HTTP_PROXY=socks5://127.0.0.1:1080"
Environment="HTTPS_PROXY=socks5://127.0.0.1:1080"
Environment="NO_PROXY=localhost,127.0.0.1,192.168.0.0/16,10.0.0.0/8"
EOF

# 重载配置并重启Docker
systemctl daemon-reload
systemctl restart docker

# 验证配置
systemctl show --property=Environment docker
```

### 方法2: 配置Docker daemon.json

```bash
cat > /etc/docker/daemon.json << 'EOF'
{
  "proxies": {
    "http-proxy": "socks5://127.0.0.1:1080",
    "https-proxy": "socks5://127.0.0.1:1080",
    "no-proxy": "localhost,127.0.0.1,192.168.0.0/16,10.0.0.0/8"
  }
}
EOF

systemctl restart docker
```

### 方法3: 使用proxychains（临时方案）

```bash
# 安装proxychains
yum install -y proxychains-ng

# 配置proxychains
cat >> /etc/proxychains.conf << 'EOF'
socks5 127.0.0.1 1080
EOF

# 使用proxychains运行docker命令
proxychains docker pull mysql:8
```

## 🔍 验证VPN连接

### 测试VPN是否工作

```bash
# 测试连接（不使用VPN）
curl -I https://www.google.com

# 测试连接（使用VPN代理）
curl -x socks5://127.0.0.1:1080 -I https://www.google.com

# 或者使用proxychains
proxychains curl -I https://www.google.com
```

### 测试Docker拉取

```bash
# 配置代理后测试
docker pull hello-world
docker pull mysql:8
```

## 📝 完整部署示例

### 使用Shadowsocks + Docker代理

```bash
# 1. 安装Shadowsocks
yum install -y epel-release
yum install -y shadowsocks-libev

# 2. 配置Shadowsocks（需要替换为你的VPN服务器信息）
cat > /etc/shadowsocks-libev/config.json << 'EOF'
{
    "server": "your-vpn-server-ip",
    "server_port": 8388,
    "local_address": "127.0.0.1",
    "local_port": 1080,
    "password": "your-password",
    "timeout": 300,
    "method": "aes-256-gfb"
}
EOF

# 3. 启动Shadowsocks
systemctl start shadowsocks-libev
systemctl enable shadowsocks-libev

# 4. 配置Docker使用代理
mkdir -p /etc/systemd/system/docker.service.d
cat > /etc/systemd/system/docker.service.d/http-proxy.conf << 'EOF'
[Service]
Environment="HTTP_PROXY=socks5://127.0.0.1:1080"
Environment="HTTPS_PROXY=socks5://127.0.0.1:1080"
Environment="NO_PROXY=localhost,127.0.0.1,192.168.0.0/16,10.0.0.0/8"
EOF

# 5. 重启Docker
systemctl daemon-reload
systemctl restart docker

# 6. 测试
docker pull hello-world
```

## 🛡️ 安全建议

1. **使用强密码**: VPN密码应该足够复杂
2. **限制访问**: 只允许必要的端口开放
3. **定期更新**: 保持VPN软件最新版本
4. **监控日志**: 定期检查VPN连接日志
5. **备份配置**: 妥善保管VPN配置文件

## 🔗 VPN服务商选择

如果你需要VPN服务，可以考虑：

1. **自建VPS**: 在海外VPS上搭建VPN服务器
2. **商业VPN服务**: 选择可靠的VPN服务商
3. **机场服务**: 使用Shadowsocks/V2Ray机场服务

## ⚠️ 注意事项

1. **合规性**: 确保使用VPN符合法律法规
2. **服务条款**: 检查云服务商是否允许使用VPN
3. **性能影响**: VPN会增加延迟，可能影响下载速度
4. **成本**: 考虑VPN服务的成本

## 🆘 故障排查

### VPN无法连接

```bash
# 检查VPN服务状态
systemctl status shadowsocks-libev

# 检查端口是否监听
netstat -tlnp | grep 1080

# 查看日志
journalctl -u shadowsocks-libev -f
```

### Docker无法使用代理

```bash
# 检查Docker代理配置
systemctl show --property=Environment docker

# 检查Docker日志
journalctl -u docker -f

# 测试代理连接
curl -x socks5://127.0.0.1:1080 -I https://registry-1.docker.io
```

## 📚 相关资源

- V2Ray官方文档: https://www.v2fly.org/
- Shadowsocks文档: https://github.com/shadowsocks/shadowsocks-libev
- Docker代理配置: https://docs.docker.com/config/daemon/systemd/#httphttps-proxy

---

**提示**: 如果你没有VPN服务，可以考虑：
1. 使用火山引擎的容器镜像服务（如果提供）
2. 联系技术支持解决网络访问问题
3. 使用其他可用的镜像源

