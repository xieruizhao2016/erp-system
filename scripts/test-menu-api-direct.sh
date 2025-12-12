#!/bin/bash
# 直接测试菜单API，模拟登录用户

echo "=========================================="
echo "菜单API直接测试"
echo "=========================================="
echo ""

# 获取admin用户的ID
ADMIN_ID=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT id FROM system_users WHERE username = 'admin' AND deleted = 0 LIMIT 1;" 2>/dev/null)

if [ -z "$ADMIN_ID" ]; then
    echo "❌ 找不到admin用户"
    exit 1
fi

echo "找到admin用户，ID: $ADMIN_ID"
echo ""

# 获取admin用户的角色
ROLE_IDS=$(mysql -u root -p123456 ruoyi-vue-pro -N -e "SELECT role_id FROM system_user_role WHERE user_id = $ADMIN_ID;" 2>/dev/null | tr '\n' ',' | sed 's/,$//')

if [ -z "$ROLE_IDS" ]; then
    echo "❌ admin用户没有分配角色"
    exit 1
fi

echo "admin用户的角色ID: $ROLE_IDS"
echo ""

# 检查这些角色是否有新菜单的权限
echo "检查角色菜单权限..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    r.id as role_id,
    r.name as role_name,
    COUNT(rm.menu_id) as menu_count,
    GROUP_CONCAT(m.name ORDER BY m.sort SEPARATOR ', ') as menu_names
FROM system_role r
LEFT JOIN system_role_menu rm ON r.id = rm.role_id
LEFT JOIN system_menu m ON rm.menu_id = m.id AND m.parent_id IN (2645, 2583)
WHERE r.id IN ($ROLE_IDS)
GROUP BY r.id, r.name;
EOF

echo ""
echo "检查新菜单是否在角色权限中..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    m.id,
    m.name,
    m.parent_id,
    CASE 
        WHEN rm.role_id IS NOT NULL THEN '有权限'
        ELSE '无权限'
    END as has_permission
FROM system_menu m
LEFT JOIN system_role_menu rm ON m.id = rm.menu_id AND rm.role_id IN ($ROLE_IDS)
WHERE m.id IN (6376, 6382, 6412, 6419)
ORDER BY m.id;
EOF

echo ""
echo "=========================================="
echo "提示："
echo "1. 如果显示'无权限'，需要为角色分配菜单权限"
echo "2. 如果显示'有权限'但仍不显示，可能是前端缓存问题"
echo "3. 请清除浏览器缓存并使用无痕模式测试"
echo "=========================================="

