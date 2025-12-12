#!/bin/bash
# 检查财务管理菜单配置脚本

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

PROJECT_ROOT="/Users/xierui/Documents/Project/Other/erp-system"
cd "$PROJECT_ROOT"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}财务管理菜单配置检查${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 1. 检查菜单数量
echo -e "${BLUE}[1/4] 检查菜单配置...${NC}"
MENU_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2645 AND status = 0;" 2>/dev/null)
echo "  财务管理(2645)下的菜单数量: $MENU_COUNT"

EXPECTED_MENUS=("付款单" "收款单" "应收账款" "应付账款" "预付款" "预收款" "资产负债表" "利润表" "结算账户")
echo "  期望的菜单: ${#EXPECTED_MENUS[@]} 个"

for menu in "${EXPECTED_MENUS[@]}"; do
    EXISTS=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2645 AND name = '$menu' AND status = 0;" 2>/dev/null)
    if [ "$EXISTS" -gt 0 ]; then
        echo -e "    ${GREEN}✓${NC} $menu"
    else
        echo -e "    ${RED}✗${NC} $menu (缺失)"
    fi
done

if [ "$MENU_COUNT" -ge 9 ]; then
    echo -e "  ${GREEN}✓ 菜单配置完整${NC}"
else
    echo -e "  ${YELLOW}⚠ 菜单数量不足，期望至少9个${NC}"
fi
echo ""

# 2. 检查前端文件
echo -e "${BLUE}[2/4] 检查前端文件...${NC}"
FRONTEND_FILES=(
    "finance/receivable/index.vue"
    "finance/payable/index.vue"
    "finance/prepayment/index.vue"
    "finance/prereceipt/index.vue"
    "finance/balancesheet/index.vue"
    "finance/profitstatement/index.vue"
    "finance/payment/index.vue"
    "finance/receipt/index.vue"
    "finance/account/index.vue"
)

MISSING_FILES=0
for file in "${FRONTEND_FILES[@]}"; do
    if [ -f "original-yudao-ui/src/views/erp/$file" ]; then
        echo -e "    ${GREEN}✓${NC} $file"
    else
        echo -e "    ${RED}✗${NC} $file (缺失)"
        MISSING_FILES=$((MISSING_FILES + 1))
    fi
done

if [ "$MISSING_FILES" -eq 0 ]; then
    echo -e "  ${GREEN}✓ 前端文件完整${NC}"
else
    echo -e "  ${RED}✗ 有 $MISSING_FILES 个文件缺失${NC}"
fi
echo ""

# 3. 检查角色权限
echo -e "${BLUE}[3/4] 检查角色权限...${NC}"
ROLE_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(DISTINCT role_id) FROM system_role_menu WHERE menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645);" 2>/dev/null)
echo "  已分配财务菜单的角色数量: $ROLE_COUNT"

if [ "$ROLE_COUNT" -gt 0 ]; then
    echo -e "  ${GREEN}✓ 角色权限已配置${NC}"
    echo "  角色权限详情:"
    mysql -u root -p123456 ruoyi-vue-pro -e "SELECT r.id, r.name, COUNT(rm.menu_id) as menu_count FROM system_role r JOIN system_role_menu rm ON r.id = rm.role_id WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645) GROUP BY r.id, r.name;" 2>/dev/null | tail -n +2
else
    echo -e "  ${YELLOW}⚠ 角色权限未配置，需要在角色管理中分配菜单权限${NC}"
fi
echo ""

# 4. 检查菜单状态
echo -e "${BLUE}[4/4] 检查菜单状态...${NC}"
DISABLED_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2645 AND status != 0;" 2>/dev/null)
HIDDEN_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2645 AND visible = 0;" 2>/dev/null)

if [ "$DISABLED_COUNT" -eq 0 ]; then
    echo -e "  ${GREEN}✓ 所有菜单已启用${NC}"
else
    echo -e "  ${YELLOW}⚠ 有 $DISABLED_COUNT 个菜单被禁用${NC}"
fi

if [ "$HIDDEN_COUNT" -eq 0 ]; then
    echo -e "  ${GREEN}✓ 所有菜单可见${NC}"
else
    echo -e "  ${YELLOW}⚠ 有 $HIDDEN_COUNT 个菜单不可见${NC}"
fi
echo ""

# 总结
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}检查完成${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}如果菜单仍不显示，请执行以下操作：${NC}"
echo ""
echo "1. ${BLUE}清除浏览器缓存${NC}"
echo "   - 按 F12 打开开发者工具"
echo "   - Application → Local Storage → 清除 ROLE_ROUTERS 和 USER"
echo "   - 刷新页面并重新登录"
echo ""
echo "2. ${BLUE}分配角色权限${NC}"
echo "   - 系统管理 → 角色管理"
echo "   - 编辑你的角色"
echo "   - 勾选所有财务相关菜单权限"
echo "   - 保存后重新登录"
echo ""
echo "3. ${BLUE}验证菜单${NC}"
echo "   - 登录后查看左侧菜单"
echo "   - 应该能看到：财务管理 → 9个子菜单"
echo ""

