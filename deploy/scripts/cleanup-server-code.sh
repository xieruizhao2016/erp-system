#!/bin/bash
# 清理云服务器上的源代码脚本
# 用途: 删除服务器上不需要的源代码，只保留必要的部署文件
# 使用方法: ./deploy/scripts/cleanup-server-code.sh

set -e

PROJECT_ROOT="/Users/xierui/Documents/Project/Other/erp-system"
SSH_KEY="$PROJECT_ROOT/tengxunyun.pem"
SERVER="ubuntu@101.33.244.240"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "=========================================="
echo "  清理云服务器源代码"
echo "=========================================="
echo ""
echo -e "${YELLOW}⚠️  警告：此操作将删除服务器上的源代码${NC}"
echo "将保留以下必要文件："
echo "  ✅ /opt/erp-system/script/docker/  (Docker配置)"
echo "  ✅ /opt/erp-system/yudao-server/target/  (JAR包存放目录)"
echo "  ✅ /opt/erp-system/sql/  (数据库脚本)"
echo "  ✅ /opt/erp-system/deploy/  (部署脚本)"
echo ""
read -p "确认继续？(yes/no): " CONFIRM

if [ "$CONFIRM" != "yes" ]; then
    echo "操作已取消"
    exit 0
fi

echo ""
echo -e "${YELLOW}【步骤1】检查当前磁盘使用情况...${NC}"
ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SERVER" << 'EOF'
echo "清理前磁盘使用："
df -h / | tail -1
echo ""
echo "项目目录大小："
du -sh /opt/erp-system 2>/dev/null || echo "目录不存在"
EOF

echo ""
echo -e "${YELLOW}【步骤2】备份重要文件...${NC}"
ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SERVER" << 'EOF'
set -e
cd /opt/erp-system

# 创建备份目录
BACKUP_DIR="/opt/erp-system-backup-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$BACKUP_DIR"

# 备份Docker配置
if [ -d "script/docker" ]; then
    echo "  备份 Docker 配置..."
    cp -r script/docker "$BACKUP_DIR/" 2>/dev/null || true
fi

# 备份SQL脚本
if [ -d "sql" ]; then
    echo "  备份 SQL 脚本..."
    cp -r sql "$BACKUP_DIR/" 2>/dev/null || true
fi

# 备份部署脚本
if [ -d "deploy" ]; then
    echo "  备份部署脚本..."
    cp -r deploy "$BACKUP_DIR/" 2>/dev/null || true
fi

echo "  ✅ 备份完成: $BACKUP_DIR"
EOF

echo ""
echo -e "${YELLOW}【步骤3】删除源代码目录...${NC}"
ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SERVER" << 'EOF'
set -e
cd /opt/erp-system

# 删除源代码模块（保留yudao-server/target目录）
echo "  删除后端模块..."
rm -rf yudao-dependencies
rm -rf yudao-framework
rm -rf yudao-module-*
rm -rf yudao-server/src
rm -rf yudao-server/target/classes
rm -rf yudao-server/target/generated-sources
rm -rf yudao-server/target/maven-archiver
rm -rf yudao-server/target/maven-status
# 保留 yudao-server/target 目录本身，用于存放上传的jar包

# 删除前端源代码
echo "  删除前端源代码..."
rm -rf original-yudao-ui/src
rm -rf original-yudao-ui/build
rm -rf original-yudao-ui/dist-prod
rm -rf original-yudao-ui/types
rm -rf original-yudao-ui/public
rm -rf original-yudao-ui/.image
rm -rf original-yudao-ui/node_modules 2>/dev/null || true

# 删除代码生成输出
echo "  删除代码生成输出..."
rm -rf codegen-*

# 删除其他不需要的文件
echo "  删除其他文件..."
rm -rf .gitee
rm -rf .github
rm -rf .image
rm -rf docs
rm -rf scripts
rm -f .DS_Store
rm -f .flattened-pom.xml
rm -f .gitignore
rm -f .jwt-token
rm -f LICENSE
rm -f *.xml
rm -f *.sql

# 确保必要的目录存在
mkdir -p yudao-server/target
mkdir -p script/docker
mkdir -p sql/mysql

echo "  ✅ 源代码删除完成"
EOF

echo ""
echo -e "${YELLOW}【步骤4】验证清理结果...${NC}"
ssh -i "$SSH_KEY" -o StrictHostKeyChecking=no "$SERVER" << 'EOF'
echo "清理后磁盘使用："
df -h / | tail -1
echo ""
echo "项目目录大小："
du -sh /opt/erp-system 2>/dev/null || echo "目录不存在"
echo ""
echo "保留的目录结构："
cd /opt/erp-system && find . -maxdepth 2 -type d | sort
EOF

echo ""
echo "=========================================="
echo -e "${GREEN}  清理完成！${NC}"
echo "=========================================="
echo ""
echo "✅ 已删除所有源代码"
echo "✅ 已保留必要的部署文件"
echo ""
echo "📝 后续部署方式："
echo "   使用本地脚本: ./scripts/deploy-prod.sh"
echo "   该脚本会在本地构建，然后上传JAR包到服务器"
echo ""

