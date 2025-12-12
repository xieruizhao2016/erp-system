#!/bin/bash
# 强制刷新菜单 - 清除所有可能的缓存

echo "=========================================="
echo "强制刷新菜单配置"
echo "=========================================="
echo ""

# 1. 确保所有菜单都是可见的
echo "[1/3] 确保菜单可见性..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
UPDATE system_menu 
SET visible = 1, status = 0 
WHERE id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419);
EOF

echo "✅ 菜单可见性已更新"

# 2. 确保父菜单也是可见的
echo ""
echo "[2/3] 确保父菜单可见性..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
UPDATE system_menu 
SET visible = 1, status = 0 
WHERE id IN (2645, 2583);
EOF

echo "✅ 父菜单可见性已更新"

# 3. 验证配置
echo ""
echo "[3/3] 验证配置..."
mysql -u root -p123456 ruoyi-vue-pro <<EOF 2>/dev/null
SELECT 
    CASE 
        WHEN COUNT(*) = 8 THEN '✅ 所有新菜单配置正确'
        ELSE CONCAT('⚠️ 有 ', 8 - COUNT(*), ' 个菜单配置异常')
    END as status
FROM system_menu 
WHERE id IN (6376, 6382, 6388, 6394, 6400, 6406, 6412, 6419)
  AND status = 0 
  AND visible = 1;
EOF

echo ""
echo "=========================================="
echo "配置已刷新"
echo "=========================================="
echo ""
echo "下一步操作："
echo "1. 重启后端服务: ./scripts/dev/start-backend.sh -d"
echo "2. 清除浏览器缓存（F12 → Application → Local Storage → Clear）"
echo "3. 使用无痕模式测试"
echo "4. 检查Network标签中的API响应"
echo ""

