#!/bin/bash
# ========================================
# 生产管理菜单重组：备份并执行脚本
# ========================================

set -e  # 遇到错误立即退出

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 数据库配置（可通过环境变量覆盖）
DB_HOST=${DB_HOST:-127.0.0.1}
DB_PORT=${DB_PORT:-3306}
DB_NAME=${DB_NAME:-ruoyi-vue-pro}
DB_USER=${DB_USER:-root}
DB_PASSWORD=${DB_PASSWORD:-123456}

# 脚本路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"
BACKUP_DIR="$PROJECT_ROOT/sql/mysql/backups"
BACKUP_FILE="$BACKUP_DIR/system_menu_backup_$(date +%Y%m%d_%H%M%S).sql"
REORGANIZE_SCRIPT="$SCRIPT_DIR/reorganize-production-menus-with-categories.sql"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}生产管理菜单重组：备份并执行${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 显示配置信息
echo -e "${BLUE}数据库配置：${NC}"
echo -e "  主机: ${DB_HOST}"
echo -e "  端口: ${DB_PORT}"
echo -e "  数据库: ${DB_NAME}"
echo -e "  用户: ${DB_USER}"
echo -e "  备份目录: ${BACKUP_DIR}"
echo ""

# 检查MySQL命令是否可用
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}错误: 未找到 mysql 命令，请确保已安装 MySQL 客户端${NC}"
    exit 1
fi

# 测试数据库连接
echo -e "${YELLOW}正在测试数据库连接...${NC}"
if ! mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" -e "USE ${DB_NAME};" 2>/dev/null; then
    echo -e "${RED}错误: 无法连接到数据库，请检查配置${NC}"
    echo -e "${YELLOW}提示: 可以通过环境变量设置数据库连接信息：${NC}"
    echo -e "  export DB_HOST=127.0.0.1"
    echo -e "  export DB_PORT=3306"
    echo -e "  export DB_NAME=ruoyi-vue-pro"
    echo -e "  export DB_USER=root"
    echo -e "  export DB_PASSWORD=your_password"
    exit 1
fi
echo -e "${GREEN}✓ 数据库连接成功${NC}"
echo ""

# 第一步：备份 system_menu 表
echo -e "${YELLOW}第一步：备份 system_menu 表...${NC}"
if mysqldump -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" \
    "${DB_NAME}" system_menu > "$BACKUP_FILE" 2>/dev/null; then
    echo -e "${GREEN}✓ 备份完成: ${BACKUP_FILE}${NC}"
    echo -e "  备份文件大小: $(du -h "$BACKUP_FILE" | cut -f1)"
else
    echo -e "${RED}错误: 备份失败${NC}"
    exit 1
fi
echo ""

# 第二步：执行重组脚本
echo -e "${YELLOW}第二步：执行菜单重组脚本...${NC}"
if [ ! -f "$REORGANIZE_SCRIPT" ]; then
    echo -e "${RED}错误: 未找到重组脚本: ${REORGANIZE_SCRIPT}${NC}"
    exit 1
fi

if mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" \
    "${DB_NAME}" < "$REORGANIZE_SCRIPT" 2>/dev/null; then
    echo -e "${GREEN}✓ 菜单重组脚本执行成功${NC}"
else
    echo -e "${RED}错误: 脚本执行失败${NC}"
    echo -e "${YELLOW}提示: 可以使用备份文件恢复:${NC}"
    echo -e "  mysql -h${DB_HOST} -P${DB_PORT} -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} < ${BACKUP_FILE}"
    exit 1
fi
echo ""

# 第三步：验证结果
echo -e "${YELLOW}第三步：验证菜单结构...${NC}"
VERIFY_QUERY="
SELECT 
    CONCAT('分类菜单: ', name, ' (ID: ', id, ')') AS result
FROM system_menu 
WHERE id IN (6100, 6101, 6102, 6103, 6104, 6105) 
AND deleted = 0
ORDER BY sort;
"

if mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" \
    "${DB_NAME}" -e "$VERIFY_QUERY" 2>/dev/null | grep -q "分类菜单"; then
    echo -e "${GREEN}✓ 分类菜单创建成功${NC}"
    mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" \
        "${DB_NAME}" -e "$VERIFY_QUERY" 2>/dev/null
else
    echo -e "${YELLOW}⚠ 警告: 未找到分类菜单，可能已存在或需要检查${NC}"
fi
echo ""

# 显示模块统计
echo -e "${YELLOW}模块分布统计：${NC}"
STATS_QUERY="
SELECT 
    c.name AS category,
    COUNT(m.id) AS module_count
FROM system_menu c
LEFT JOIN system_menu m ON m.parent_id = c.id AND m.type = 2 AND m.deleted = 0
WHERE c.id IN (6100, 6101, 6102, 6103, 6104, 6105)
AND c.deleted = 0
GROUP BY c.id, c.name
ORDER BY c.sort;
"

mysql -h"${DB_HOST}" -P"${DB_PORT}" -u"${DB_USER}" -p"${DB_PASSWORD}" \
    "${DB_NAME}" -e "$STATS_QUERY" 2>/dev/null || echo -e "${YELLOW}  无法获取统计信息${NC}"
echo ""

# 完成提示
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}✓ 菜单重组完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${BLUE}重要提示：${NC}"
echo -e "  1. 备份文件已保存: ${BACKUP_FILE}"
echo -e "  2. 请登录系统检查菜单结构是否正确"
echo -e "  3. 如有问题，可以使用备份文件恢复"
echo -e "  4. 建议清除系统缓存并重新登录"
echo ""

