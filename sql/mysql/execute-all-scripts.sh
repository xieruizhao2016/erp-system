#!/bin/bash

# ============================================================
# 生产管理系统关联字段补充脚本 - 批量执行
# 创建时间: 2024年
# 说明: 按顺序执行所有SQL脚本，添加缺失的关联字段
# ============================================================

# 颜色定义
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# 数据库连接配置（从环境变量或配置文件读取，默认值）
DB_HOST=${DB_HOST:-127.0.0.1}
DB_PORT=${DB_PORT:-3306}
DB_NAME=${DB_NAME:-ruoyi-vue-pro}
DB_USER=${DB_USER:-root}
DB_PASS=${DB_PASS:-123456}

# 脚本目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}生产管理系统关联字段补充脚本${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo "数据库配置："
echo "  主机: $DB_HOST"
echo "  端口: $DB_PORT"
echo "  数据库: $DB_NAME"
echo "  用户: $DB_USER"
echo ""
echo -e "${YELLOW}提示: 如需修改配置，请设置环境变量：${NC}"
echo "  export DB_HOST=127.0.0.1"
echo "  export DB_PORT=3306"
echo "  export DB_NAME=ruoyi-vue-pro"
echo "  export DB_USER=root"
echo "  export DB_PASS=your_password"
echo ""
read -p "是否继续执行？(y/n): " -n 1 -r
echo ""
if [[ ! $REPLY =~ ^[Yy]$ ]]; then
    echo "已取消执行"
    exit 1
fi

# 检查MySQL是否可用
echo -e "${YELLOW}检查MySQL连接...${NC}"
if ! mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" -e "USE $DB_NAME;" 2>/dev/null; then
    echo -e "${RED}错误: 无法连接到数据库${NC}"
    echo "请检查数据库配置或手动执行SQL脚本"
    exit 1
fi
echo -e "${GREEN}✓ 数据库连接成功${NC}"
echo ""

# 执行脚本列表（按顺序）
SCRIPTS=(
    "erp-production-order-enhancement.sql"
    "erp-production-association-fields.sql"
)

# 执行每个脚本
for script in "${SCRIPTS[@]}"; do
    script_path="$SCRIPT_DIR/$script"
    if [ ! -f "$script_path" ]; then
        echo -e "${RED}错误: 脚本文件不存在: $script_path${NC}"
        continue
    fi
    
    echo -e "${YELLOW}执行脚本: $script${NC}"
    if mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$script_path" 2>&1; then
        echo -e "${GREEN}✓ $script 执行成功${NC}"
    else
        echo -e "${RED}✗ $script 执行失败${NC}"
        echo "请检查错误信息"
    fi
    echo ""
done

# 执行验证脚本
echo -e "${YELLOW}执行验证脚本...${NC}"
verify_script="$SCRIPT_DIR/verify-production-associations.sql"
if [ -f "$verify_script" ]; then
    mysql -h"$DB_HOST" -P"$DB_PORT" -u"$DB_USER" -p"$DB_PASS" "$DB_NAME" < "$verify_script"
    echo -e "${GREEN}✓ 验证完成${NC}"
else
    echo -e "${YELLOW}警告: 验证脚本不存在${NC}"
fi

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}所有脚本执行完成！${NC}"
echo -e "${GREEN}========================================${NC}"

