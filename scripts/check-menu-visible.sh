#!/bin/bash
# 检查菜单可见性配置

echo "=========================================="
echo "菜单可见性检查"
echo "=========================================="
echo ""

echo "财务菜单配置："
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    id,
    name,
    type,
    status,
    visible,
    CASE 
        WHEN status = 0 THEN '启用'
        ELSE '禁用'
    END as status_text,
    CASE 
        WHEN visible = 1 THEN '可见'
        ELSE '不可见'
    END as visible_text
FROM system_menu 
WHERE parent_id = 2645 
ORDER BY sort;
EOF

echo ""
echo "库存菜单配置："
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    id,
    name,
    type,
    status,
    visible,
    CASE 
        WHEN status = 0 THEN '启用'
        ELSE '禁用'
    END as status_text,
    CASE 
        WHEN visible = 1 THEN '可见'
        ELSE '不可见'
    END as visible_text
FROM system_menu 
WHERE parent_id = 2583 
ORDER BY sort;
EOF

echo ""
echo "检查新菜单的详细配置："
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    m.id,
    m.name,
    m.type,
    m.status,
    m.visible,
    m.parent_id,
    p.name as parent_name,
    p.status as parent_status,
    p.visible as parent_visible
FROM system_menu m
LEFT JOIN system_menu p ON m.parent_id = p.id
WHERE m.id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419)
ORDER BY m.id;
EOF

echo ""
echo "=========================================="
echo "如果visible=0或status!=0，菜单不会显示"
echo "如果parent_visible=0或parent_status!=0，子菜单也不会显示"
echo "=========================================="

