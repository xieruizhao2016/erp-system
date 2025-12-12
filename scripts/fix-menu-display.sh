#!/bin/bash
# 修复菜单显示问题的脚本
# 用于排查和修复新菜单不显示的问题

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
echo -e "${GREEN}菜单显示问题修复工具${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 1. 检查菜单配置
echo -e "${BLUE}[1/6] 检查菜单配置...${NC}"

# 财务菜单
FINANCE_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2645 AND status = 0 AND visible = 1;" 2>/dev/null)
echo "  财务管理菜单数量: $FINANCE_COUNT (期望: 9)"

# 库存菜单
STOCK_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 2583 AND status = 0 AND visible = 1;" 2>/dev/null)
echo "  库存管理菜单数量: $STOCK_COUNT (期望: 9)"

if [ "$FINANCE_COUNT" -ge 9 ] && [ "$STOCK_COUNT" -ge 9 ]; then
    echo -e "  ${GREEN}✓ 菜单配置正常${NC}"
else
    echo -e "  ${RED}✗ 菜单配置异常${NC}"
    echo "  财务菜单详情:"
    mysql -u root -p123456 ruoyi-vue-pro -e "SELECT id, name, status, visible FROM system_menu WHERE parent_id = 2645 ORDER BY sort;" 2>/dev/null
    echo "  库存菜单详情:"
    mysql -u root -p123456 ruoyi-vue-pro -e "SELECT id, name, status, visible FROM system_menu WHERE parent_id = 2583 ORDER BY sort;" 2>/dev/null
fi
echo ""

# 2. 检查角色权限
echo -e "${BLUE}[2/6] 检查角色权限...${NC}"
ROLE_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(DISTINCT role_id) FROM system_role_menu WHERE menu_id IN (SELECT id FROM system_menu WHERE parent_id IN (2645, 2583));" 2>/dev/null)
echo "  已分配菜单的角色数量: $ROLE_COUNT"

# 检查超级管理员角色（通常是ID最小的启用角色）
ADMIN_ROLE=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT id FROM system_role WHERE status = 0 ORDER BY id LIMIT 1;" 2>/dev/null)
if [ -n "$ADMIN_ROLE" ]; then
    ADMIN_MENU_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_role_menu WHERE role_id = $ADMIN_ROLE AND menu_id IN (SELECT id FROM system_menu WHERE parent_id IN (2645, 2583));" 2>/dev/null)
    echo "  角色 $ADMIN_ROLE 的菜单权限数量: $ADMIN_MENU_COUNT"
    
    if [ "$ADMIN_MENU_COUNT" -lt 15 ]; then
        echo -e "  ${YELLOW}⚠ 角色权限可能不完整，需要重新分配${NC}"
    else
        echo -e "  ${GREEN}✓ 角色权限正常${NC}"
    fi
fi
echo ""

# 3. 检查后端服务
echo -e "${BLUE}[3/6] 检查后端服务...${NC}"
if lsof -i :48080 >/dev/null 2>&1; then
    echo -e "  ${GREEN}✓ 后端服务运行中${NC}"
    
    # 检查API是否可访问
    API_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/system/auth/get-permission-info 2>/dev/null)
    if [ "$API_STATUS" = "200" ] || [ "$API_STATUS" = "401" ]; then
        echo -e "  ${GREEN}✓ 后端API可访问 (HTTP $API_STATUS)${NC}"
    else
        echo -e "  ${YELLOW}⚠ 后端API响应异常 (HTTP $API_STATUS)${NC}"
    fi
else
    echo -e "  ${RED}✗ 后端服务未运行${NC}"
    echo "  请运行: ./scripts/dev/start-backend.sh"
fi
echo ""

# 4. 检查前端服务
echo -e "${BLUE}[4/6] 检查前端服务...${NC}"
if lsof -i :5173 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
    PORT=$(lsof -i :5173 >/dev/null 2>&1 && echo "5173" || echo "3000")
    echo -e "  ${GREEN}✓ 前端服务运行中 (端口 $PORT)${NC}"
else
    echo -e "  ${YELLOW}⚠ 前端服务未运行${NC}"
fi
echo ""

# 5. 提供修复建议
echo -e "${BLUE}[5/6] 修复建议...${NC}"

# 检查是否需要重新分配权限
if [ "$ADMIN_MENU_COUNT" -lt 15 ] && [ -n "$ADMIN_ROLE" ]; then
    echo -e "  ${YELLOW}⚠ 检测到权限不完整，正在修复...${NC}"
    
    # 为超级管理员角色分配所有财务和库存菜单权限
    mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
-- 分配财务菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT $ADMIN_ROLE, id FROM system_menu 
WHERE parent_id = 2645 
AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = $ADMIN_ROLE AND menu_id = system_menu.id
);

-- 分配库存菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT $ADMIN_ROLE, id FROM system_menu 
WHERE parent_id = 2583 
AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = $ADMIN_ROLE AND menu_id = system_menu.id
);
EOF
    
    if [ $? -eq 0 ]; then
        echo -e "  ${GREEN}✓ 已为角色 $ADMIN_ROLE 分配所有菜单权限${NC}"
    else
        echo -e "  ${RED}✗ 权限分配失败${NC}"
    fi
fi
echo ""

# 6. 最终检查
echo -e "${BLUE}[6/6] 最终检查...${NC}"
FINAL_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_role_menu WHERE role_id = $ADMIN_ROLE AND menu_id IN (SELECT id FROM system_menu WHERE parent_id IN (2645, 2583));" 2>/dev/null)
echo "  角色 $ADMIN_ROLE 的最终菜单权限数量: $FINAL_COUNT"

if [ "$FINAL_COUNT" -ge 15 ]; then
    echo -e "  ${GREEN}✓ 权限配置完整${NC}"
else
    echo -e "  ${YELLOW}⚠ 权限可能仍不完整${NC}"
fi
echo ""

# 总结
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}修复完成${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}重要提示：${NC}"
echo ""
echo "1. ${BLUE}必须清除浏览器缓存${NC}"
echo "   - 按 F12 → Application → Local Storage"
echo "   - 删除 ROLE_ROUTERS 和 USER"
echo "   - 或者使用无痕模式打开浏览器"
echo ""
echo "2. ${BLUE}重新登录系统${NC}"
echo "   - 退出当前登录"
echo "   - 清除缓存后重新登录"
echo ""
echo "3. ${BLUE}如果仍不显示，重启后端服务${NC}"
echo "   - 停止后端: pkill -f yudao-server"
echo "   - 启动后端: ./scripts/dev/start-backend.sh -d"
echo ""
echo "4. ${BLUE}检查浏览器控制台${NC}"
echo "   - 按 F12 查看是否有错误信息"
echo "   - 查看 Network 标签中的 API 响应"
echo ""

