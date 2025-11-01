# X-ERP 前端部署说明

## 前端代码获取

本项目的前端代码存放在独立的 Git 仓库中，需要单独克隆。

### 方式一：直接克隆（推荐）

```bash
# 在项目根目录下执行
git clone https://gitee.com/yudaocode/yudao-ui-admin-vue3.git original-yudao-ui
cd original-yudao-ui
```

### 方式二：使用已有的前端代码

如果您已经有 `yudao-ui-admin-vue3` 的代码，可以直接放在项目根目录，并重命名为 `original-yudao-ui`。

## 环境配置

### 1. 创建环境变量文件

在 `original-yudao-ui` 目录下创建 `.env.local` 文件：

```bash
# 项目本地运行端口号
VITE_PORT=80

# open 运行 npm run dev 时自动打开浏览器
VITE_OPEN=true

# 后端接口地址
VITE_BASE_URL=http://localhost:48080

# 租户开关
VITE_APP_TENANT_ENABLE=true

# 验证码的开关
VITE_APP_CAPTCHA_ENABLE=true

# 文档地址的开关
VITE_APP_DOCALERT_ENABLE=true

# 百度统计
VITE_APP_BAIDU_CODE=a1ff8825baa73c3a78eb96aa40325abc

# 默认账户密码
VITE_APP_DEFAULT_LOGIN_TENANT=X-ERP
VITE_APP_DEFAULT_LOGIN_USERNAME=admin
VITE_APP_DEFAULT_LOGIN_PASSWORD=admin123

# API 加解密
VITE_APP_API_ENCRYPT_ENABLE=true
VITE_APP_API_ENCRYPT_HEADER=X-Api-Encrypt
VITE_APP_API_ENCRYPT_ALGORITHM=AES
VITE_APP_API_ENCRYPT_REQUEST_KEY=52549111389893486934626385991395
VITE_APP_API_ENCRYPT_RESPONSE_KEY=96103715984234343991809655248883
```

### 2. 更新品牌信息

修改 `original-yudao-ui/.env` 文件中的以下配置：

```bash
# 标题
VITE_APP_TITLE=X-ERP管理系统

# 默认账户
VITE_APP_DEFAULT_LOGIN_TENANT=X-ERP
```

## 安装依赖

```bash
cd original-yudao-ui

# 使用 pnpm 安装（推荐）
pnpm install

# 或使用国内镜像源
pnpm install --registry=https://registry.npmmirror.com
```

## 启动前端服务

```bash
# 开发模式
pnpm run dev

# 构建生产环境
pnpm run build:prod
```

前端服务将运行在 http://localhost:80

## 目录说明

```
erp-system/
├── original-yudao-ui/          # 前端完整项目（独立 Git 仓库，不提交到主仓库）
│   ├── src/
│   ├── public/
│   ├── .env
│   ├── .env.local
│   └── package.json
├── yudao-ui/                   # 后端项目自带的多个前端版本
│   ├── yudao-ui-admin-vue3/   # Vue3 版本（仅部分文件）
│   ├── yudao-ui-admin-vue2/   # Vue2 版本
│   └── yudao-ui-admin-vben/   # Vben 版本
├── yudao-server/              # 后端主项目
└── README-FRONTEND.md         # 本文档
```

## 常见问题

### Q: 为什么前端代码不在主仓库中？

A: `original-yudao-ui` 是一个独立的 Git 仓库，包含大量的 node_modules 和构建产物。为了保持主仓库的简洁性和加快克隆速度，我们将其排除在外。

### Q: 两个前端目录有什么区别？

- `original-yudao-ui`: 完整的前端项目，可以独立运行，**推荐使用**
- `yudao-ui/yudao-ui-admin-vue3`: 后端项目自带的前端代码（不完整），仅供参考

### Q: 如何切换后端地址？

修改 `original-yudao-ui/.env.local` 文件中的 `VITE_BASE_URL` 配置。

## 技术栈

- Vue 3.4+
- Vite 5.1.4
- Element Plus
- TypeScript
- Pinia

## 相关链接

- 前端源码仓库: https://gitee.com/yudaocode/yudao-ui-admin-vue3
- 后端 API 文档: http://localhost:48080/doc.html
- Swagger UI: http://localhost:48080/swagger-ui
