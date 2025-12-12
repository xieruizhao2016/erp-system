#!/bin/bash
# 调试菜单API，检查后端返回的菜单数据

set -e

echo "=========================================="
echo "菜单API调试工具"
echo "=========================================="
echo ""

# 获取一个测试token（需要先登录获取）
echo "提示：此脚本需要有效的登录token"
echo "请在浏览器中登录系统，然后："
echo "1. 按 F12 打开开发者工具"
echo "2. Application → Local Storage → 找到 ACCESS_TOKEN"
echo "3. 复制token值"
echo ""
read -p "请输入你的ACCESS_TOKEN（或按Enter跳过）: " TOKEN

if [ -z "$TOKEN" ]; then
    echo "跳过API测试，直接检查数据库配置..."
else
    echo ""
    echo "正在调用API..."
    RESPONSE=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:48080/admin-api/system/auth/get-permission-info)
    
    echo "API响应（前500字符）:"
    echo "$RESPONSE" | head -c 500
    echo ""
    echo ""
    
    # 检查是否包含新菜单
    if echo "$RESPONSE" | grep -q "应收账款"; then
        echo "✅ API响应包含'应收账款'菜单"
    else
        echo "❌ API响应不包含'应收账款'菜单"
    fi
    
    if echo "$RESPONSE" | grep -q "内部入库"; then
        echo "✅ API响应包含'内部入库'菜单"
    else
        echo "❌ API响应不包含'内部入库'菜单"
    fi
fi

echo ""
echo "=========================================="
echo "数据库菜单配置检查"
echo "=========================================="
echo ""

# 检查财务菜单
echo "财务管理菜单 (parent_id = 2645):"
mysql -u root -p123456 ruoyi-vue-pro -e "SELECT id, name, path, component, status, visible, sort FROM system_menu WHERE parent_id = 2645 ORDER BY sort;" 2>/dev/null

echo ""
echo "库存管理菜单 (parent_id = 2583):"
mysql -u root -p123456 ruoyi-vue-pro -e "SELECT id, name, path, component, status, visible, sort FROM system_menu WHERE parent_id = 2583 ORDER BY sort;" 2>/dev/null

echo ""
echo "=========================================="
echo "角色权限检查"
echo "=========================================="
echo ""

# 检查所有角色的权限
mysql -u root -p123456 ruoyi-vue-pro -e "SELECT r.id, r.name, COUNT(rm.menu_id) as menu_count FROM system_role r LEFT JOIN system_role_menu rm ON r.id = rm.role_id AND rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id IN (2645, 2583)) WHERE r.status = 0 GROUP BY r.id, r.name ORDER BY r.id;" 2>/dev/null

echo ""
echo "=========================================="
echo "检查完成"
echo "=========================================="

