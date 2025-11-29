# ERP系统部署文件夹

本文件夹包含ERP系统部署到火山引擎云服务器的所有相关文档、脚本和配置文件。

## 📁 文件夹结构

```
deploy/
├── README.md                    # 本文件
├── docs/                        # 部署文档
│   ├── 火山引擎部署指南.md        # 完整部署指南
│   ├── 火山引擎快速开始.md        # 快速开始指南
│   ├── 火山引擎服务器配置指南.md   # 服务器购买和配置指南
│   └── 部署检查清单.md            # 部署检查清单
├── scripts/                     # 部署脚本
│   ├── deploy-volcano.sh        # 自动化部署脚本
│   └── quick-deploy.sh          # 快速环境准备脚本
└── config/                      # 配置文件
    ├── docker-compose.prod.yml  # 生产环境Docker Compose配置
    └── env.prod.example         # 环境变量配置示例
```

## 🚀 快速开始

### 1. 在服务器上准备环境

```bash
# 上传项目到服务器后
cd /opt/erp-system/deploy/scripts
chmod +x quick-deploy.sh
./quick-deploy.sh
```

### 2. 配置环境变量

```bash
cd /opt/erp-system/deploy/config
cp env.prod.example ../../script/docker/.env
vi ../../script/docker/.env  # 修改密码等配置
```

### 3. 部署服务

```bash
cd /opt/erp-system/deploy/scripts
chmod +x deploy-volcano.sh
./deploy-volcano.sh
```

## 📚 文档说明

### 火山引擎快速开始.md
- 5分钟快速部署指南
- 常用命令
- 故障排查

### 火山引擎部署指南.md
- 完整的部署文档
- 详细的步骤说明
- 常见问题解答
- 安全建议

### 部署检查清单.md
- 部署前检查项
- 部署步骤检查
- 部署后验证

## 🔧 脚本说明

### quick-deploy.sh
快速环境准备脚本，自动安装：
- Docker
- Docker Compose
- 配置防火墙

**使用方法：**
```bash
./quick-deploy.sh
```

### deploy-volcano.sh
自动化部署脚本，执行：
- 检查环境
- 构建后端（如需要）
- 构建Docker镜像
- 启动所有服务

**使用方法：**
```bash
./deploy-volcano.sh
```

**前置条件：**
- Docker和Docker Compose已安装
- 已配置 `.env` 文件
- 项目代码已上传到服务器

## ⚙️ 配置文件说明

### docker-compose.prod.yml
生产环境Docker Compose配置文件，包含：
- MySQL服务
- Redis服务
- 后端服务
- 前端服务

**使用位置：** `script/docker/docker-compose.prod.yml`

### env.prod.example
环境变量配置示例文件，包含：
- 数据库配置
- Redis配置
- Java配置
- 前端配置

**使用方法：**
```bash
cp env.prod.example ../../script/docker/.env
# 然后编辑 .env 文件，修改密码等配置
```

## 📝 部署流程

1. **准备服务器**
   - 运行 `quick-deploy.sh` 安装必要软件

2. **配置环境**
   - 复制 `env.prod.example` 为 `.env`
   - 修改 `.env` 中的密码和配置

3. **部署服务**
   - 运行 `deploy-volcano.sh` 自动部署
   - 或手动使用 `docker-compose` 命令

4. **验证部署**
   - 访问前端：`http://服务器IP`
   - 访问后端：`http://服务器IP:48080`
   - 检查服务状态和日志

## 🔗 相关文件位置

部署脚本会使用项目根目录下的以下文件：
- `script/docker/docker-compose.prod.yml` - Docker Compose配置
- `script/docker/.env` - 环境变量（需要创建）
- `yudao-server/target/yudao-server.jar` - 后端JAR文件（需要构建）
- `original-yudao-ui/Dockerfile` - 前端Dockerfile
- `sql/mysql/ruoyi-vue-pro.sql` - 数据库初始化脚本

## ⚠️ 注意事项

1. **路径说明**
   - 部署脚本假设项目在 `/opt/erp-system`
   - 如果项目在其他位置，需要修改脚本中的路径

2. **权限要求**
   - 所有脚本需要root权限运行
   - 确保脚本有执行权限：`chmod +x *.sh`

3. **配置文件**
   - `.env` 文件包含敏感信息，不要提交到版本控制
   - 生产环境务必修改所有默认密码

4. **备份**
   - 部署前建议备份现有数据
   - 定期备份数据库和配置文件

## 🆘 获取帮助

遇到问题？
1. 查看 `docs/火山引擎部署指南.md` 获取详细说明
2. 查看 `docs/部署检查清单.md` 检查配置
3. 查看服务日志排查问题
4. 提交Issue获取帮助

---

**祝部署顺利！** 🎉

