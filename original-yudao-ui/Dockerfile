# 构建阶段
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 安装pnpm
RUN npm install -g pnpm

# 配置npm镜像源（解决网络问题）
RUN npm config set registry https://registry.npmmirror.com && \
    pnpm config set registry https://registry.npmmirror.com

# ========== 优化：分离依赖安装和代码复制，利用Docker层缓存 ==========
# 先复制依赖文件（这些文件变化频率低，缓存命中率高）
COPY package.json pnpm-lock.yaml ./

# 安装依赖（这一层会被缓存，除非 package.json 或 pnpm-lock.yaml 变化）
# 只修改代码时，这一层会使用缓存，节省大量时间
RUN pnpm install --frozen-lockfile

# 复制源代码（只有代码变化时才会重新执行后续步骤）
COPY . .

# 构建参数
ARG NODE_ENV=production
ARG PUBLIC_PATH=/
ARG VITE_APP_TITLE=ERP管理系统
ARG VITE_APP_BASE_API=/prod-api
ARG VITE_APP_APP_NAME=/
ARG VITE_APP_TENANT_ENABLE=true
ARG VITE_APP_CAPTCHA_ENABLE=false
ARG VITE_APP_DOC_ENABLE=true
ARG VITE_APP_BAIDU_CODE=
ARG VITE_BASE_URL=
ARG VITE_API_URL=/prod-api

# 设置环境变量（Vite使用VITE_前缀）
ENV NODE_ENV=$NODE_ENV
ENV PUBLIC_PATH=$PUBLIC_PATH
ENV VITE_APP_TITLE=$VITE_APP_TITLE
ENV VITE_APP_BASE_API=$VITE_APP_BASE_API
ENV VITE_APP_APP_NAME=$VITE_APP_APP_NAME
ENV VITE_APP_TENANT_ENABLE=$VITE_APP_TENANT_ENABLE
ENV VITE_APP_CAPTCHA_ENABLE=$VITE_APP_CAPTCHA_ENABLE
ENV VITE_APP_DOC_ENABLE=$VITE_APP_DOC_ENABLE
ENV VITE_APP_BAIDU_CODE=$VITE_APP_BAIDU_CODE
ENV VITE_BASE_URL=$VITE_BASE_URL
ENV VITE_API_URL=$VITE_API_URL

# 构建生产版本
RUN pnpm build:prod

# 运行阶段
FROM nginx:alpine

# 复制构建产物到nginx目录（根据 .env.prod 配置，输出目录是 dist-prod）
COPY --from=builder /app/dist-prod /usr/share/nginx/html

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]

