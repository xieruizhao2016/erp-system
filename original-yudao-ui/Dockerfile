# 构建阶段
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 安装pnpm（单独一层，利用缓存）
RUN npm install -g pnpm && \
    npm config set registry https://registry.npmmirror.com && \
    pnpm config set registry https://registry.npmmirror.com

# 复制package文件（依赖变化时才重新安装）
COPY package.json pnpm-lock.yaml ./

# 安装依赖（使用缓存挂载加速，仅在 package.json 或 pnpm-lock.yaml 变化时重新安装）
# 使用 BuildKit 缓存挂载可以显著加速依赖安装
RUN --mount=type=cache,id=pnpm-store,target=/root/.local/share/pnpm/store \
    pnpm install --frozen-lockfile

# 复制源代码（源代码变化时才重新构建）
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

# 设置环境变量（Vite需要VITE_前缀）
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

# 构建生产版本（使用缓存挂载加速 Vite 构建缓存）
RUN --mount=type=cache,id=vite-cache,target=/app/node_modules/.vite \
    pnpm build:prod

# 运行阶段
FROM nginx:alpine

# 复制构建产物到nginx目录（prod 模式输出到 dist-prod）
COPY --from=builder /app/dist-prod /usr/share/nginx/html

# 复制nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]

