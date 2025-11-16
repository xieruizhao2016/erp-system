#!/bin/bash
# 修复菜单编码问题
# 使用方法: ./scripts/fix-menu-encoding.sh

set -e

echo "=== 修复菜单编码问题 ==="
echo ""

# 使用UTF-8字符集连接数据库
docker exec yudao-mysql mysql -u root -p123456 --default-character-set=utf8mb4 ruoyi-vue-pro << 'EOF'
SET NAMES utf8mb4;

-- 修复生产管理菜单名称
UPDATE system_menu SET name = '生产管理' WHERE id = 5042;

-- 验证修复结果
SELECT id, name, path, parent_id 
FROM system_menu 
WHERE id = 5042 OR (parent_id = 2563 AND path = 'production')
ORDER BY id;
EOF

echo ""
echo "✅ 菜单编码已修复！"
echo "请刷新前端页面查看效果"

