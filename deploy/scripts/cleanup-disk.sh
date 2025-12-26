#!/bin/bash
# 磁盘清理脚本
# 用于清理腾讯云服务器的磁盘空间

set -e

echo "=========================================="
echo "开始清理磁盘空间..."
echo "=========================================="

# 1. 清理APT包缓存
echo ""
echo "1. 清理APT包缓存..."
sudo apt-get clean
sudo apt-get autoclean
echo "✅ APT包缓存已清理"

# 2. 清理系统日志（保留7天）
echo ""
echo "2. 清理系统日志（保留7天）..."
sudo journalctl --vacuum-time=7d
echo "✅ 系统日志已清理"

# 3. 清理旧的日志文件
echo ""
echo "3. 清理旧的日志文件..."
sudo find /var/log -type f -name '*.log.*' -mtime +7 -delete 2>/dev/null || true
sudo find /var/log -type f -name '*.gz' -mtime +30 -delete 2>/dev/null || true
echo "✅ 旧日志文件已清理"

# 4. 清理Docker未使用的镜像（可选，谨慎使用）
echo ""
echo "4. 检查Docker未使用的资源..."
docker system df
read -p "是否清理Docker未使用的镜像？(y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    docker system prune -a -f
    echo "✅ Docker未使用的镜像已清理"
else
    echo "⏭️  跳过Docker镜像清理"
fi

# 5. 显示清理后的磁盘使用情况
echo ""
echo "=========================================="
echo "清理完成！当前磁盘使用情况："
echo "=========================================="
df -h /

echo ""
echo "=========================================="
echo "各目录占用情况："
echo "=========================================="
du -h --max-depth=1 / 2>/dev/null | sort -hr | head -10

echo ""
echo "✅ 磁盘清理完成！"

