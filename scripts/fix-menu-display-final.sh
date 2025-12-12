#!/bin/bash
# 最终修复菜单显示问题

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}最终修复菜单显示问题${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 1. 确保所有菜单配置正确
echo -e "${BLUE}[1/5] 确保菜单配置正确...${NC}"
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
-- 确保所有新菜单都是启用且可见的
UPDATE system_menu 
SET status = 0, visible = 1, deleted = 0
WHERE id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419);

-- 确保父菜单也是启用且可见的
UPDATE system_menu 
SET status = 0, visible = 1, deleted = 0
WHERE id IN (2645, 2583);
EOF
echo -e "${GREEN}✓ 菜单配置已更新${NC}"
echo ""

# 2. 确保角色权限
echo -e "${BLUE}[2/5] 确保角色权限...${NC}"
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
-- 为所有启用角色分配新菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, m.id
FROM system_role r
CROSS JOIN system_menu m
WHERE r.status = 0
  AND m.id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu rm
    WHERE rm.role_id = r.id AND rm.menu_id = m.id
  );
EOF
echo -e "${GREEN}✓ 角色权限已更新${NC}"
echo ""

# 3. 清除Redis缓存
echo -e "${BLUE}[3/5] 清除Redis缓存...${NC}"
redis-cli -p 6379 FLUSHALL > /dev/null 2>&1
echo -e "${GREEN}✓ Redis缓存已清除${NC}"
echo ""

# 4. 重启后端服务
echo -e "${BLUE}[4/5] 重启后端服务...${NC}"
pkill -f yudao-server 2>/dev/null || true
sleep 3
cd /Users/xierui/Documents/Project/Other/erp-system
./scripts/dev/start-backend.sh -d > /dev/null 2>&1
echo -e "${GREEN}✓ 后端服务已重启${NC}"
echo ""

# 5. 验证配置
echo -e "${BLUE}[5/5] 验证配置...${NC}"
sleep 5
MENU_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419) AND status = 0 AND visible = 1 AND deleted = 0;" 2>/dev/null)
if [ "$MENU_COUNT" -eq 8 ]; then
    echo -e "${GREEN}✓ 所有8个新菜单配置正确${NC}"
else
    echo -e "${YELLOW}⚠ 只有 $MENU_COUNT 个菜单配置正确${NC}"
fi

ROLE_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(DISTINCT role_id) FROM system_role_menu WHERE menu_id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419);" 2>/dev/null)
echo -e "${GREEN}✓ $ROLE_COUNT 个角色已分配新菜单权限${NC}"
echo ""

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}修复完成${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}重要提示：${NC}"
echo ""
echo "1. ${BLUE}等待后端服务完全启动（约20秒）${NC}"
echo "2. ${BLUE}清除浏览器缓存${NC}"
echo "   - F12 → Application → Local Storage → Clear"
echo "   - 或使用无痕模式（Cmd+Shift+N）"
echo "3. ${BLUE}重新登录系统${NC}"
echo "4. ${BLUE}检查API响应${NC}"
echo "   - Network → /admin-api/system/auth/get-permission-info"
echo "   - 搜索 '应收账款' 或 '内部入库'"
echo ""
echo "如果API响应中仍然没有新菜单，可能需要："
echo "- 重新编译后端代码：mvn clean package -DskipTests"
echo "- 检查后端日志：tail -f nohup.out"
echo ""

