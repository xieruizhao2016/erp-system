#!/bin/bash
# 清除Redis缓存脚本

echo "=========================================="
echo "清除Redis菜单缓存"
echo "=========================================="
echo ""

# 检查Redis是否运行
if ! redis-cli -p 6379 ping > /dev/null 2>&1; then
    echo "❌ Redis未运行，请先启动Redis"
    exit 1
fi

echo "正在清除Redis缓存..."

# 清除所有菜单相关缓存
redis-cli -p 6379 <<EOF 2>/dev/null
KEYS *menu*
KEYS *MENU*
KEYS *permission*
KEYS *PERMISSION*
EOF | while read key; do
    if [ -n "$key" ]; then
        redis-cli -p 6379 DEL "$key" > /dev/null 2>&1
        echo "已删除: $key"
    fi
done

# 或者直接清除所有缓存（更彻底）
read -p "是否清除所有Redis缓存？(y/n): " confirm
if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
    redis-cli -p 6379 FLUSHDB
    echo "✅ 已清除所有Redis缓存"
else
    echo "✅ 已清除菜单相关缓存"
fi

echo ""
echo "=========================================="
echo "缓存已清除，请重启后端服务"
echo "=========================================="
echo ""
echo "重启命令:"
echo "  pkill -f yudao-server"
echo "  ./scripts/dev/start-backend.sh -d"
echo ""

