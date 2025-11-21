#!/bin/bash

# 重新编译和重启后端服务脚本
# 用于在修改后端代码后重新部署

cd "$(dirname "$0")"

echo "========================================="
echo "重新编译并重启后端服务"
echo "========================================="
echo ""

# 1. 停止当前运行的后端服务
echo "📍 步骤1: 停止当前运行的后端服务..."
if lsof -i :48080 >/dev/null 2>&1; then
    echo "发现后端服务正在运行，准备停止..."
    
    # 查找并杀死Java进程
    PID=$(lsof -ti :48080)
    if [ -n "$PID" ]; then
        echo "正在停止进程 PID: $PID"
        kill $PID
        
        # 等待进程结束（最多10秒）
        for i in {1..10}; do
            if ! kill -0 $PID 2>/dev/null; then
                echo "✅ 后端服务已停止"
                break
            fi
            echo "等待进程结束... ($i/10)"
            sleep 1
        done
        
        # 如果进程还在，强制杀死
        if kill -0 $PID 2>/dev/null; then
            echo "进程未响应，强制停止..."
            kill -9 $PID
        fi
    fi
else
    echo "✅ 后端服务未运行"
fi

echo ""

# 2. 清理旧的编译文件
echo "📍 步骤2: 清理旧的编译文件..."
if [ -d "yudao-server/target" ]; then
    echo "删除 yudao-server/target 目录..."
    rm -rf yudao-server/target
fi

# 清理各模块的target目录
echo "清理各模块的target目录..."
find . -type d -name "target" -not -path "./node_modules/*" -not -path "./original-yudao-ui/*" | while read dir; do
    echo "  删除: $dir"
    rm -rf "$dir"
done

echo "✅ 清理完成"
echo ""

# 3. 重新编译打包
echo "📍 步骤3: 重新编译打包（这可能需要几分钟）..."
echo ""

# 检查Maven是否可用
if ! command -v mvn &> /dev/null; then
    echo "❌ 错误: 找不到Maven命令"
    echo "请确保Maven已安装并添加到PATH中"
    exit 1
fi

# 显示Maven版本
echo "Maven版本:"
mvn -version | head -3
echo ""

# 开始编译
echo "开始编译打包..."
echo "-------------------------------------------"

# 使用mvn clean package编译，跳过测试
mvn clean package -DskipTests -T 1C

# 检查编译是否成功
if [ $? -ne 0 ]; then
    echo ""
    echo "❌ 编译失败！"
    echo "请检查错误信息并修复后重试"
    exit 1
fi

echo "-------------------------------------------"
echo "✅ 编译打包成功"
echo ""

# 4. 验证jar包是否生成
echo "📍 步骤4: 验证jar包..."
JAR_FILE="yudao-server/target/yudao-server.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "❌ 错误: jar包未生成 ($JAR_FILE)"
    exit 1
fi

# 显示jar包信息
JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
JAR_DATE=$(ls -lh "$JAR_FILE" | awk '{print $6, $7, $8}')
echo "✅ jar包已生成:"
echo "   文件: $JAR_FILE"
echo "   大小: $JAR_SIZE"
echo "   时间: $JAR_DATE"
echo ""

# 5. 启动后端服务
echo "📍 步骤5: 启动后端服务..."
echo ""

# 使用后台模式启动
bash start-backend.sh -d

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================="
    echo "✅ 重新部署完成！"
    echo "========================================="
    echo ""
    echo "📝 后续操作:"
    echo "   1. 等待几秒钟让服务完全启动"
    echo "   2. 访问: http://localhost:48080"
    echo "   3. 查看日志: tail -f nohup.out"
    echo "   4. 刷新前端页面（Cmd+Shift+R）"
    echo ""
    echo "🎉 新增的功能模块已生效:"
    echo "   ✓ SKU管理"
    echo "   ✓ 产品包装"
    echo "   ✓ 产品OEM"
    echo ""
else
    echo ""
    echo "❌ 启动失败，请检查错误信息"
    exit 1
fi

