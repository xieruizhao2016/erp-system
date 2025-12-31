# 部署包说明

## 📦 部署包结构

每个部署包目录（格式：YYYYMMDD）包含以下文件：

```
YYYYMMDD/
├── yudao-admin.tar          # 前端Docker镜像（约31MB）
├── yudao-server.tar         # 后端Docker镜像（约289MB）
├── docker-compose.prod.yml  # Docker Compose配置文件
├── .env.example             # 环境变量配置示例
├── deploy.sh                # 一键部署脚本
└── 部署说明.md              # 详细部署说明文档
```

## 🚀 快速部署

### 方法1：使用部署脚本（推荐）

```bash
# 1. 上传部署包到服务器
scp -r deploy/packages/YYYYMMDD user@server:/path/to/deploy/

# 2. SSH登录服务器
ssh user@server

# 3. 进入部署目录
cd /path/to/deploy/YYYYMMDD

# 4. 准备环境变量文件
cp .env.example .env
vi .env  # 编辑并设置密码等配置

# 5. 执行部署脚本
chmod +x deploy.sh
./deploy.sh
```

### 方法2：手动部署

```bash
# 1. 导入镜像
docker load -i yudao-admin.tar
docker load -i yudao-server.tar

# 2. 准备环境变量
cp .env.example .env
vi .env  # 编辑配置

# 3. 启动服务
docker-compose -f docker-compose.prod.yml --env-file .env up -d
```

## ⚠️ 重要提示

1. **环境变量配置**
   - 必须编辑 `.env` 文件，设置数据库密码、Redis密码等
   - 不要使用默认密码

2. **端口要求**
   - 确保服务器防火墙开放端口：80（前端）、48080（后端）

3. **数据库初始化**
   - 首次部署需要执行SQL初始化脚本
   - SQL文件位置：`sql/mysql/ruoyi-vue-pro.sql`

4. **磁盘空间**
   - 确保服务器有足够空间（至少2GB）

## 📝 版本历史

- **20251226**: 初始版本
  - 前端镜像：yudao-admin:latest
  - 后端镜像：yudao-server:latest
  - 修复Dockerfile端口配置问题

