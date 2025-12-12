#!/bin/bash
# 测试后端API返回的菜单数据

echo "=========================================="
echo "后端API菜单数据测试"
echo "=========================================="
echo ""

echo "提示：此脚本需要有效的登录token"
echo "请在浏览器中："
echo "1. 登录系统"
echo "2. 按 F12 → Application → Local Storage"
echo "3. 找到 ACCESS_TOKEN 并复制"
echo ""
read -p "请输入ACCESS_TOKEN（或按Enter跳过）: " TOKEN

if [ -z "$TOKEN" ]; then
    echo ""
    echo "跳过API测试，检查数据库配置..."
    echo ""
    echo "数据库中的菜单配置："
    mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    CONCAT('财务-', m.name) as menu_path,
    m.id,
    m.name,
    m.path,
    m.component,
    m.status,
    m.visible,
    COUNT(rm.role_id) as role_count
FROM system_menu m
LEFT JOIN system_role_menu rm ON m.id = rm.menu_id
WHERE m.parent_id = 2645 AND m.id IN (6376, 6382, 6388, 6394, 6400, 6406)
GROUP BY m.id, m.name, m.path, m.component, m.status, m.visible
ORDER BY m.sort;
EOF

    echo ""
    echo "库存菜单："
    mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    CONCAT('库存-', m.name) as menu_path,
    m.id,
    m.name,
    m.path,
    m.component,
    m.status,
    m.visible,
    COUNT(rm.role_id) as role_count
FROM system_menu m
LEFT JOIN system_role_menu rm ON m.id = rm.menu_id
WHERE m.parent_id = 2583 AND m.id IN (6412, 6419)
GROUP BY m.id, m.name, m.path, m.component, m.status, m.visible
ORDER BY m.sort;
EOF
else
    echo ""
    echo "正在调用API..."
    RESPONSE=$(curl -s -H "Authorization: Bearer $TOKEN" http://localhost:48080/admin-api/system/auth/get-permission-info)
    
    echo "检查API响应中是否包含新菜单..."
    echo ""
    
    # 检查财务菜单
    for menu in "应收账款" "应付账款" "预付款" "预收款" "资产负债表" "利润表"; do
        if echo "$RESPONSE" | grep -q "$menu"; then
            echo "✅ API响应包含: $menu"
        else
            echo "❌ API响应不包含: $menu"
        fi
    done
    
    echo ""
    # 检查库存菜单
    for menu in "内部入库" "内部出库"; do
        if echo "$RESPONSE" | grep -q "$menu"; then
            echo "✅ API响应包含: $menu"
        else
            echo "❌ API响应不包含: $menu"
        fi
    done
    
    echo ""
    echo "API响应（前1000字符）:"
    echo "$RESPONSE" | head -c 1000
    echo "..."
fi

echo ""
echo "=========================================="
echo "如果API响应中不包含新菜单，说明后端有问题"
echo "如果API响应中包含新菜单，说明前端缓存或路由生成有问题"
echo "=========================================="

