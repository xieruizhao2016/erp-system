#!/bin/bash

# ERP系统数据库初始化脚本
# 使用方法: ./init-database.sh [数据库名]
# 默认数据库名: ruoyi-vue-pro

DB_NAME=${1:-ruoyi-vue-pro}
MYSQL_CMD="mysql -uroot -p123456"

echo "=================================="
echo "  ERP系统数据库初始化脚本"
echo "=================================="
echo "数据库名: $DB_NAME"
echo ""

# 检查MySQL是否运行
if ! command -v mysql &> /dev/null; then
    echo "❌ 错误: MySQL命令未找到，请确保MySQL客户端已安装"
    exit 1
fi

# 检查数据库是否存在，如果不存在则创建
echo "🔍 检查数据库..."
$MYSQL_CMD -e "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null

if [ $? -ne 0 ]; then
    echo "❌ 无法连接到MySQL，请检查服务是否启动或密码是否正确"
    echo "默认密码: 123456"
    echo "如果密码不对，请修改脚本中的MYSQL_CMD变量"
    exit 1
fi

echo "✅ 数据库检查完成"
echo ""

# SQL脚本路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SQL_DIR="$SCRIPT_DIR/sql/mysql"

# 检查SQL文件是否存在
if [ ! -f "$SQL_DIR/ruoyi-vue-pro.sql" ]; then
    echo "❌ 错误: 找不到SQL文件 $SQL_DIR/ruoyi-vue-pro.sql"
    exit 1
fi

# 执行SQL脚本的函数
execute_sql() {
    local sql_file=$1
    local description=$2

    echo "📄 正在执行: $description"
    echo "   文件: $sql_file"

    if $MYSQL_CMD $DB_NAME < "$sql_file" 2>/dev/null; then
        echo "   ✅ 执行成功"
    else
        echo "   ❌ 执行失败"
        return 1
    fi
    echo ""
}

# 执行顺序很重要！
echo "🚀 开始执行SQL脚本..."
echo ""

# 1. 基础表结构（必须先执行）
execute_sql "$SQL_DIR/ruoyi-vue-pro.sql" "基础表结构（用户、角色、权限等）"

# 2. ERP业务表
execute_sql "$SQL_DIR/erp-tables.sql" "ERP业务表（销售、采购、库存等）"

# 3. 定时任务表
execute_sql "$SQL_DIR/quartz.sql" "定时任务表"

# 4. 示例数据（可选）
if [ -f "$SQL_DIR/sample-data.sql" ]; then
    read -p "❓ 是否导入示例数据？(y/N): " import_sample
    if [[ $import_sample =~ ^[Yy]$ ]]; then
        execute_sql "$SQL_DIR/sample-data.sql" "示例数据"
    fi
fi

echo "=================================="
echo "  🎉 数据库初始化完成！"
echo "=================================="
echo ""
echo "接下来你可以："
echo "1. 启动后端服务: ./start-backend.sh"
echo "2. 启动前端服务: cd original-yudao-ui && pnpm dev"
echo ""
echo "访问地址："
echo "- 前端: http://localhost:5173"
echo "- 后端API: http://localhost:48080"
echo "- Swagger文档: http://localhost:48080/swagger-ui"
echo ""
