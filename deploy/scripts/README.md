# 部署脚本说明

本目录包含ERP系统的各种部署和维护脚本。

## 📋 脚本列表

### 本地构建相关（推荐用于更新部署）

| 脚本 | 说明 | 使用场景 |
|------|------|----------|
| `build-images-local.sh` | 在本地构建Docker镜像 | 代码更新后，在本地构建镜像 |
| `export-app-images.sh` | 导出应用镜像 | 构建完成后，导出镜像文件 |
| `upload-app-images.sh` | 上传并导入镜像到服务器 | 将本地构建的镜像上传到服务器 |

**使用流程：**
```bash
./build-images-local.sh      # 1. 本地构建
./export-app-images.sh       # 2. 导出镜像
./upload-app-images.sh        # 3. 上传到服务器
```

### 服务器部署相关

| 脚本 | 说明 | 使用场景 |
|------|------|----------|
| `quick-deploy.sh` | 快速环境准备 | 首次部署，安装Docker等基础环境 |
| `deploy-volcano.sh` | 完整部署脚本 | 在服务器上执行完整部署流程 |
| `build-frontend-on-server.sh` | 在服务器上构建前端 | 需要在服务器上构建前端时 |

### 镜像管理相关

| 脚本 | 说明 | 使用场景 |
|------|------|----------|
| `export-docker-images.sh` | 导出基础镜像 | 导出mysql、redis等基础镜像 |
| `export-build-images.sh` | 导出构建镜像 | 导出node、nginx等构建用镜像 |
| `import-docker-images.sh` | 导入镜像 | 在服务器上导入镜像文件 |

### 其他工具脚本

| 脚本 | 说明 |
|------|------|
| `setup-ssh-and-init.sh` | SSH连接和系统初始化 |
| `start-baota-panel.sh` | 启动宝塔面板服务 |
| `install-claude-cli-centos.sh` | 安装Claude CLI工具 |

## 🚀 快速开始

### 首次部署

1. **在服务器上准备环境：**
   ```bash
   cd /opt/erp-system
   ./deploy/scripts/quick-deploy.sh
   ```

2. **配置环境变量：**
   ```bash
   cp deploy/config/env.prod.example script/docker/.env
   vi script/docker/.env  # 修改密码等配置
   ```

3. **选择部署方式：**

   **方式A：本地构建后上传（推荐）**
   ```bash
   # 在本地执行
   ./deploy/scripts/build-images-local.sh
   ./deploy/scripts/export-app-images.sh
   ./deploy/scripts/upload-app-images.sh
   
   # 在服务器上启动
   cd /opt/erp-system/script/docker
   docker-compose -f docker-compose.prod.yml --env-file .env up -d
   ```

   **方式B：在服务器上构建**
   ```bash
   # 在服务器上执行
   cd /opt/erp-system
   ./deploy/scripts/deploy-volcano.sh
   ```

### 更新部署

当代码有更新时，推荐使用本地构建方式：

```bash
# 1. 拉取最新代码
git pull

# 2. 本地构建新镜像
./deploy/scripts/build-images-local.sh

# 3. 导出镜像
./deploy/scripts/export-app-images.sh

# 4. 上传并导入到服务器
./deploy/scripts/upload-app-images.sh

# 5. SSH到服务器重启服务
ssh -i ~/Documents/huoshan-ssh.pem root@115.190.240.137
cd /opt/erp-system/script/docker
docker-compose -f docker-compose.prod.yml --env-file .env up -d --force-recreate
```

## 📝 脚本使用说明

### build-images-local.sh

在本地构建应用Docker镜像。

**前置要求：**
- Docker Desktop/Engine 已安装并运行
- Docker Compose 已安装
- Maven 已安装（用于构建后端JAR）

**使用方法：**
```bash
./deploy/scripts/build-images-local.sh
```

**功能：**
- 检查并构建后端JAR文件（如果不存在）
- 构建 `yudao-server:latest` 镜像
- 构建 `yudao-admin:latest` 镜像

### export-app-images.sh

导出本地构建的应用镜像。

**使用方法：**
```bash
./deploy/scripts/export-app-images.sh
```

**输出：**
- 镜像文件保存在 `deploy/exports/app-images-YYYYMMDD-HHMMSS.tar.gz`

### upload-app-images.sh

上传应用镜像到服务器并自动导入。

**使用方法：**
```bash
# 自动使用最新导出的镜像
./deploy/scripts/upload-app-images.sh

# 指定镜像文件
./deploy/scripts/upload-app-images.sh deploy/exports/app-images-20240101-120000.tar.gz

# 指定服务器地址
./deploy/scripts/upload-app-images.sh deploy/exports/app-images-20240101-120000.tar.gz root@your-server-ip
```

**配置：**
- 默认SSH密钥：`~/Documents/huoshan-ssh.pem`
- 默认服务器：`root@115.190.240.137`
- 可在脚本中修改这些默认值

## ⚠️ 注意事项

1. **权限：** 确保脚本有执行权限：
   ```bash
   chmod +x deploy/scripts/*.sh
   ```

2. **环境变量：** 本地构建时，`.env` 文件是可选的，但建议创建以自定义构建参数

3. **网络：** 本地构建需要能访问Docker Hub或镜像源

4. **磁盘空间：** 确保有足够的磁盘空间存储镜像文件（通常需要500MB-1GB）

5. **SSH配置：** 确保SSH密钥路径正确，且有访问服务器的权限

## 📚 相关文档

- [本地构建部署指南](../docs/本地构建部署指南.md)
- [快速部署指南](../快速部署.md)
- [部署检查清单](../docs/部署检查清单.md)

---

**提示：** 本地构建方式特别适合频繁更新的开发环境，可以大大节省部署时间！

