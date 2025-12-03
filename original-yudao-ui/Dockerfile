# 构建阶段
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 安装pnpm
RUN npm install -g pnpm

# 配置npm镜像源（解决网络问题）
RUN npm config set registry https://registry.npmmirror.com
RUN pnpm config set registry https://registry.npmmirror.com

# 复制package文件
COPY package.json pnpm-lock.yaml ./

# 安装依赖
RUN pnpm install --frozen-lockfile

# 复制源代码
COPY . .

# 构建参数
ARG NODE_ENV=production
ARG PUBLIC_PATH=/
ARG VUE_APP_TITLE=ERP管理系统
ARG VUE_APP_BASE_API=/prod-api
ARG VUE_APP_APP_NAME=/
ARG VUE_APP_TENANT_ENABLE=true
ARG VUE_APP_CAPTCHA_ENABLE=true
ARG VUE_APP_DOC_ENABLE=true
ARG VUE_APP_BAIDU_CODE=

# 设置环境变量
ENV NODE_ENV=$NODE_ENV
ENV PUBLIC_PATH=$PUBLIC_PATH
ENV VUE_APP_TITLE=$VUE_APP_TITLE
ENV VUE_APP_BASE_API=$VUE_APP_BASE_API
ENV VUE_APP_APP_NAME=$VUE_APP_APP_NAME
ENV VUE_APP_TENANT_ENABLE=$VUE_APP_TENANT_ENABLE
ENV VUE_APP_CAPTCHA_ENABLE=$VUE_APP_CAPTCHA_ENABLE
ENV VUE_APP_DOC_ENABLE=$VUE_APP_DOC_ENABLE
ENV VUE_APP_BAIDU_CODE=$VUE_APP_BAIDU_CODE

# 构建生产版本
RUN pnpm build:prod

# 运行阶段
FROM nginx:alpine

# 复制构建产物到nginx目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]

