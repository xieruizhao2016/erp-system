#!/bin/bash
# 检查菜单配置脚本
# 用于诊断新功能前端不显示的问题

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
echo -e "${GREEN}菜单配置检查工具${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 1. 检查数据库菜单配置
echo -e "${BLUE}[1/5] 检查数据库菜单配置...${NC}"
MENU_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 5042 AND status = 0;" 2>/dev/null)
echo "  生产管理(5042)下的菜单数量: $MENU_COUNT"

DIRECTORY_MENUS=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id = 5042 AND component = '' AND status = 0;" 2>/dev/null)
echo "  目录菜单数量: $DIRECTORY_MENUS"

SUB_MENUS=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(*) FROM system_menu WHERE parent_id IN (6100,6101,6102,6103,6104,6105) AND status = 0;" 2>/dev/null)
echo "  子菜单数量: $SUB_MENUS"

if [ "$DIRECTORY_MENUS" -ge 6 ] && [ "$SUB_MENUS" -ge 20 ]; then
    echo -e "  ${GREEN}✓ 数据库菜单配置正常${NC}"
else
    echo -e "  ${RED}✗ 数据库菜单配置可能有问题${NC}"
fi
echo ""

# 2. 检查角色权限
echo -e "${BLUE}[2/5] 检查角色权限配置...${NC}"
ROLE_MENU_COUNT=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT COUNT(DISTINCT role_id) FROM system_role_menu WHERE menu_id IN (5042,6100,6101,6102,6103,6104,6105);" 2>/dev/null)
echo "  已分配菜单的角色数量: $ROLE_MENU_COUNT"

if [ "$ROLE_MENU_COUNT" -gt 0 ]; then
    echo -e "  ${GREEN}✓ 角色权限配置正常${NC}"
else
    echo -e "  ${RED}✗ 角色权限未配置，需要为角色分配菜单权限${NC}"
fi
echo ""

# 3. 检查前端文件
echo -e "${BLUE}[3/5] 检查前端文件...${NC}"
FRONTEND_DIR="original-yudao-ui/src/views/erp"

# 检查关键文件是否存在
KEY_FILES=(
    "productionorder/index.vue"
    "productbom/index.vue"
    "processroute/index.vue"
    "workorder/index.vue"
    "quality/qualitystandard/index.vue"
    "cost/coststandard/index.vue"
)

MISSING_FILES=0
for file in "${KEY_FILES[@]}"; do
    if [ -f "$FRONTEND_DIR/$file" ]; then
        echo -e "  ${GREEN}✓${NC} $file"
    else
        echo -e "  ${RED}✗${NC} $file (缺失)"
        MISSING_FILES=$((MISSING_FILES + 1))
    fi
done

if [ "$MISSING_FILES" -eq 0 ]; then
    echo -e "  ${GREEN}✓ 前端文件完整${NC}"
else
    echo -e "  ${RED}✗ 有 $MISSING_FILES 个文件缺失${NC}"
fi
echo ""

# 4. 检查后端服务
echo -e "${BLUE}[4/5] 检查后端服务...${NC}"
if lsof -i :48080 >/dev/null 2>&1; then
    echo -e "  ${GREEN}✓ 后端服务运行中 (端口 48080)${NC}"
    
    # 测试API
    API_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/system/auth/get-permission-info 2>/dev/null)
    if [ "$API_RESPONSE" = "200" ] || [ "$API_RESPONSE" = "401" ]; then
        echo -e "  ${GREEN}✓ 后端API可访问${NC}"
    else
        echo -e "  ${YELLOW}⚠ 后端API响应异常 (HTTP $API_RESPONSE)${NC}"
    fi
else
    echo -e "  ${RED}✗ 后端服务未运行${NC}"
fi
echo ""

# 5. 检查前端服务
echo -e "${BLUE}[5/5] 检查前端服务...${NC}"
if lsof -i :5173 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
    PORT=$(lsof -i :5173 >/dev/null 2>&1 && echo "5173" || echo "3000")
    echo -e "  ${GREEN}✓ 前端服务运行中 (端口 $PORT)${NC}"
else
    echo -e "  ${YELLOW}⚠ 前端服务未运行${NC}"
fi
echo ""

# 总结和建议
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}检查完成${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""
echo -e "${YELLOW}如果新功能前端不显示，请尝试以下解决方案：${NC}"
echo ""
echo "1. ${BLUE}清除前端缓存并重新登录${NC}"
echo "   - 打开浏览器开发者工具 (F12)"
echo "   - 进入 Application/存储 标签"
echo "   - 清除 Local Storage 和 Session Storage"
echo "   - 或者直接清除浏览器缓存"
echo "   - 重新登录系统"
echo ""
echo "2. ${BLUE}检查角色权限${NC}"
echo "   - 登录系统管理后台"
echo "   - 进入：系统管理 → 角色管理"
echo "   - 编辑你的角色，确保勾选了新的菜单权限"
echo "   - 保存后重新登录"
echo ""
echo "3. ${BLUE}检查菜单状态${NC}"
echo "   - 进入：系统管理 → 菜单管理"
echo "   - 确认新菜单的状态为'启用'"
echo "   - 确认菜单的'可见'选项已勾选"
echo ""
echo "4. ${BLUE}重启服务${NC}"
echo "   - 重启后端服务：./scripts/dev/start-backend.sh"
echo "   - 重启前端服务：cd original-yudao-ui && pnpm dev"
echo ""

