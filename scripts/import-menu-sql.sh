#!/bin/bash
# 导入菜单SQL脚本
# 使用方法: ./scripts/import-menu-sql.sh [DATABASE_NAME] [USERNAME] [PASSWORD]

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 配置参数
DATABASE_NAME="${1:-ruoyi-vue-pro}"
DB_USER="${2:-root}"
DB_PASSWORD="${3}"
SQL_FILE="sql/mysql/erp_production_menus.sql"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}导入生产管理模块菜单SQL${NC}"
echo -e "${GREEN}========================================${NC}"
echo "数据库: ${DATABASE_NAME}"
echo "用户: ${DB_USER}"
echo "SQL文件: ${SQL_FILE}"
echo ""

# 检查SQL文件是否存在
if [ ! -f "$SQL_FILE" ]; then
    echo -e "${RED}错误: SQL文件不存在: ${SQL_FILE}${NC}"
    exit 1
fi

# 检查mysql命令是否可用
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}错误: mysql 命令未找到${NC}"
    echo "请安装 MySQL 客户端或使用其他方式导入SQL"
    exit 1
fi

# 提示用户确认
echo -e "${YELLOW}即将导入菜单SQL到数据库: ${DATABASE_NAME}${NC}"
echo -e "${YELLOW}请确保:${NC}"
echo "  1. 数据库已创建"
echo "  2. 用户有足够的权限"
echo "  3. 菜单ID不会与现有菜单冲突"
echo ""
read -p "是否继续? (y/N): " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}已取消${NC}"
    exit 0
fi

# 导入SQL
echo -e "${GREEN}开始导入...${NC}"

if [ -z "$DB_PASSWORD" ]; then
    # 如果没有提供密码，使用交互式输入
    mysql -u "$DB_USER" -p "$DATABASE_NAME" < "$SQL_FILE"
else
    # 使用提供的密码
    mysql -u "$DB_USER" -p"$DB_PASSWORD" "$DATABASE_NAME" < "$SQL_FILE"
fi

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}导入成功！${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo ""
    echo -e "${YELLOW}下一步操作:${NC}"
    echo "1. 登录系统管理界面"
    echo "2. 进入：系统管理 → 菜单管理"
    echo "3. 刷新页面，查看新添加的菜单"
    echo "4. 为角色分配新菜单的权限"
    echo "5. 测试各个功能模块"
else
    echo ""
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}导入失败！${NC}"
    echo -e "${RED}========================================${NC}"
    echo "请检查："
    echo "1. 数据库连接信息是否正确"
    echo "2. 用户是否有足够的权限"
    echo "3. 菜单ID是否与现有菜单冲突"
    exit 1
fi

