# Docker镜像拉取问题解决方案

## 🔍 问题描述

在执行 `docker compose up -d` 时，无法拉取Docker镜像，出现连接超时错误：

```
Error: failed to resolve reference "docker.io/library/mysql:8": 
failed to do request: Head "https://dockerproxy.com/v2/library/mysql/manifests/8?ns=docker.io": 
dial tcp 185.60.219.36:443: connect: connection timed out
```

## ✅ 已尝试的解决方案

1. ✅ 配置多个镜像加速器（均失败）
2. ✅ 移除镜像源配置，直接使用Docker Hub（连接超时）
3. ✅ 检查安全组规则（出向规则已正确配置）
4. ✅ 检查防火墙（已开放端口）

## 💡 解决方案

### 方案1: 联系火山引擎技术支持（推荐）

这是网络路由层面的问题，需要云服务商协助解决。

**工单内容：**
- 无法访问Docker Hub (registry-1.docker.io)
- 安全组规则已正确配置
- 可以访问国内网站，但无法访问国际网站
- 请求检查网络路由策略

### 方案2: 使用VPN代理（如果可用）

如果你有VPN服务，可以配置Docker使用代理：

```bash
# 1. 配置Docker使用代理
mkdir -p /etc/systemd/system/docker.service.d
cat > /etc/systemd/system/docker.service.d/http-proxy.conf << 'EOF'
[Service]
Environment="HTTP_PROXY=socks5://127.0.0.1:1080"
Environment="HTTPS_PROXY=socks5://127.0.0.1:1080"
Environment="NO_PROXY=localhost,127.0.0.1,192.168.0.0/16,10.0.0.0/8"
EOF

# 2. 重启Docker
systemctl daemon-reload
systemctl restart docker
```

### 方案3: 手动导入镜像（临时方案）

如果本地或其他环境可以访问Docker Hub：

**步骤1: 在可访问Docker Hub的环境拉取镜像**

```bash
# 在本地或其他服务器执行
docker pull mysql:8
docker pull redis:6-alpine

# 导出镜像
docker save mysql:8 redis:6-alpine | gzip > docker-images.tar.gz
```

**步骤2: 上传到服务器**

```bash
# 从本地上传
scp -i /Users/xierui/Documents/huoshan-ssh.pem docker-images.tar.gz root@115.190.240.137:/tmp/
```

**步骤3: 在服务器上导入**

```bash
# SSH到服务器
ssh erp-server

# 导入镜像
gunzip -c /tmp/docker-images.tar.gz | docker load

# 验证
docker images
```

**步骤4: 继续部署**

```bash
cd /opt/erp-system/script/docker
docker compose -f docker-compose.prod.yml --env-file .env up -d
```

### 方案4: 使用火山引擎容器镜像服务（如果提供）

如果火山引擎提供容器镜像服务：

1. 在火山引擎控制台创建镜像仓库
2. 将镜像推送到火山引擎镜像仓库
3. 修改 `docker-compose.prod.yml` 中的镜像地址
4. 从火山引擎镜像仓库拉取

### 方案5: 等待网络恢复

有时是临时网络问题，可以稍后重试：

```bash
cd /opt/erp-system/script/docker
docker compose -f docker-compose.prod.yml --env-file .env pull
```

## 📋 当前状态总结

### 已完成 ✅
- ✅ SSH密钥连接配置
- ✅ 系统初始化完成
- ✅ Docker和Docker Compose安装
- ✅ 项目代码上传
- ✅ 环境变量配置
- ✅ 安全组规则配置（入向和出向）
- ✅ 宝塔面板安装
- ✅ Docker管理器安装

### 待解决 ⏳
- ⚠️ 无法从Docker Hub拉取镜像（网络连接超时）
- ⚠️ 需要解决网络访问问题或使用替代方案

## 🎯 推荐行动方案

### 立即行动

1. **联系火山引擎技术支持**
   - 说明无法访问Docker Hub等国际网站
   - 请求检查网络路由策略
   - 参考：`deploy/docs/技术支持工单-简洁版.md`

2. **同时准备手动导入方案**
   - 如果本地可以访问Docker Hub，准备镜像文件
   - 作为临时解决方案

### 长期方案

- 配置VPN服务（如果需要持续访问国际网站）
- 使用火山引擎容器镜像服务（如果提供）

## 📝 需要的镜像清单

部署ERP系统需要以下镜像：

1. **mysql:8** - MySQL数据库
2. **redis:6-alpine** - Redis缓存
3. **yudao-server:latest** - 后端服务（需要构建）
4. **yudao-admin:latest** - 前端服务（需要构建）

## 🔧 如果镜像拉取成功后的部署步骤

```bash
# 1. 进入项目目录
cd /opt/erp-system/script/docker

# 2. 拉取基础镜像（如果网络问题解决）
docker compose -f docker-compose.prod.yml --env-file .env pull

# 3. 构建应用镜像
docker compose -f docker-compose.prod.yml --env-file .env build

# 4. 启动所有服务
docker compose -f docker-compose.prod.yml --env-file .env up -d

# 5. 查看服务状态
docker compose -f docker-compose.prod.yml ps

# 6. 查看日志
docker compose -f docker-compose.prod.yml logs -f
```

## 🆘 故障排查

### 检查网络连接

```bash
# 测试DNS解析
nslookup registry-1.docker.io

# 测试HTTPS连接
curl -I https://registry-1.docker.io

# 测试Docker拉取
docker pull hello-world
```

### 检查Docker配置

```bash
# 查看Docker配置
cat /etc/docker/daemon.json

# 查看Docker信息
docker info
```

### 检查防火墙

```bash
# 查看防火墙状态
systemctl status firewalld

# 查看防火墙规则
firewall-cmd --list-all
```

---

**当前建议：** 优先联系火山引擎技术支持解决网络问题，这是最根本的解决方案。

