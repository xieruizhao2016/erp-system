#!/bin/bash
# 调试菜单查询，模拟后端查询逻辑

echo "=========================================="
echo "菜单查询调试"
echo "=========================================="
echo ""

echo "[1] 检查菜单基础配置..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    id,
    name,
    type,
    status,
    visible,
    deleted,
    parent_id,
    sort
FROM system_menu 
WHERE id IN (6376, 6382, 6412, 6419)
ORDER BY id;
EOF

echo ""
echo "[2] 检查角色权限..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    m.id,
    m.name,
    COUNT(rm.role_id) as role_count,
    GROUP_CONCAT(rm.role_id) as role_ids
FROM system_menu m
LEFT JOIN system_role_menu rm ON m.id = rm.menu_id
WHERE m.id IN (6376, 6382, 6412, 6419)
GROUP BY m.id, m.name;
EOF

echo ""
echo "[3] 模拟后端查询：获取所有菜单（超级管理员）..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    COUNT(*) as total,
    SUM(CASE WHEN id IN (6376, 6382, 6412, 6419) THEN 1 ELSE 0 END) as new_menus_count
FROM system_menu 
WHERE deleted = 0;
EOF

echo ""
echo "[4] 检查菜单树构建：父菜单是否存在..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    m.id,
    m.name,
    m.parent_id,
    p.id as parent_exists,
    p.name as parent_name,
    p.status as parent_status,
    p.visible as parent_visible
FROM system_menu m
LEFT JOIN system_menu p ON m.parent_id = p.id
WHERE m.id IN (6376, 6382, 6412, 6419);
EOF

echo ""
echo "[5] 检查菜单排序..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    parent_id,
    COUNT(*) as child_count,
    MIN(sort) as min_sort,
    MAX(sort) as max_sort,
    GROUP_CONCAT(CONCAT(name, '(', sort, ')') ORDER BY sort SEPARATOR ', ') as children
FROM system_menu 
WHERE parent_id IN (2645, 2583)
GROUP BY parent_id;
EOF

echo ""
echo "=========================================="
echo "如果所有检查都正常，问题可能在："
echo "1. 后端代码的菜单查询逻辑"
echo "2. 菜单树构建时的过滤条件"
echo "3. 需要重新编译后端代码"
echo "=========================================="

