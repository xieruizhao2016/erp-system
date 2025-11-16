#!/bin/bash

# 后端启动脚本
# 如果Java未在PATH中，请设置JAVA_HOME环境变量
# 例如：export JAVA_HOME=/path/to/java/home

cd "$(dirname "$0")"

# 自动查找Java的函数
find_java() {
    # 1. 检查JAVA_HOME环境变量
    if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
        echo "$JAVA_HOME/bin/java"
        return 0
    fi
    
    # 2. 检查PATH中的java命令（避免触发macOS安装提示）
    # 注意：macOS的/usr/bin/java是包装器，直接运行会触发安装提示
    # 所以跳过这个检查，直接查找实际的Java安装位置
    
    # 3. 使用/usr/libexec/java_home查找（macOS特有）
    if [ -x /usr/libexec/java_home ]; then
        JAVA_HOME_PATH=$(/usr/libexec/java_home 2>/dev/null)
        if [ -n "$JAVA_HOME_PATH" ] && [ -x "$JAVA_HOME_PATH/bin/java" ]; then
            echo "$JAVA_HOME_PATH/bin/java"
            return 0
        fi
    fi
    
    # 4. 查找常见安装位置（使用通配符匹配）
    # macOS标准安装位置
    if [ -d "/Library/Java/JavaVirtualMachines" ]; then
        for java_path in /Library/Java/JavaVirtualMachines/*/Contents/Home/bin/java; do
            if [ -x "$java_path" ]; then
                echo "$java_path"
                return 0
            fi
        done
    fi
    
    # 用户目录安装位置
    if [ -d "$HOME/Library/Java/JavaVirtualMachines" ]; then
        for java_path in "$HOME/Library/Java/JavaVirtualMachines"/*/Contents/Home/bin/java; do
            if [ -x "$java_path" ]; then
                echo "$java_path"
                return 0
            fi
        done
    fi
    
    # Homebrew安装位置（Intel Mac）
    for brew_java in "/usr/local/opt/openjdk@8/bin/java" "/usr/local/opt/openjdk/bin/java" "/usr/local/opt/openjdk@11/bin/java" "/usr/local/opt/openjdk@17/bin/java"; do
        if [ -x "$brew_java" ]; then
            echo "$brew_java"
            return 0
        fi
    done
    
    # Homebrew安装位置（Apple Silicon Mac）
    for brew_java in "/opt/homebrew/opt/openjdk@8/bin/java" "/opt/homebrew/opt/openjdk/bin/java" "/opt/homebrew/opt/openjdk@11/bin/java" "/opt/homebrew/opt/openjdk@17/bin/java"; do
        if [ -x "$brew_java" ]; then
            echo "$brew_java"
            return 0
        fi
    done
    
    return 1
}

# 查找Java
JAVA_CMD=$(find_java)

if [ -z "$JAVA_CMD" ]; then
    echo "错误: 找不到Java运行时环境"
    echo ""
    echo "请执行以下操作之一："
    echo "1. 设置JAVA_HOME环境变量（推荐）："
    echo "   export JAVA_HOME=/path/to/java/home"
    echo "   例如: export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_xxx/Contents/Home"
    echo ""
    echo "2. 安装Java 8（如果未安装）："
    echo "   # 使用Homebrew安装"
    echo "   brew install openjdk@8"
    echo "   # 或者从Oracle/AdoptOpenJDK官网下载安装"
    echo ""
    echo "3. 将Java添加到PATH中"
    exit 1
fi

# 检查jar包是否存在
JAR_FILE="yudao-server/target/yudao-server.jar"
if [ ! -f "$JAR_FILE" ]; then
    echo "错误: 找不到jar包 $JAR_FILE"
    echo "请先运行: mvn clean package -DskipTests"
    exit 1
fi

# 检查是否已经在运行
if lsof -i :48080 >/dev/null 2>&1; then
    echo "警告: 端口48080已被占用，后端服务可能已在运行"
    echo "正在检查进程..."
    ps aux | grep -E "java.*yudao-server" | grep -v grep
    exit 0
fi

# 加载 .env 文件（如果存在）
ENV_FILE="yudao-server/.env"
if [ -f "$ENV_FILE" ]; then
    echo "加载环境变量文件: $ENV_FILE"
    # 读取 .env 文件并导出环境变量（忽略注释和空行）
    set -a
    source "$ENV_FILE"
    set +a
fi

echo "正在启动后端服务..."
echo "使用Java: $JAVA_CMD"
$JAVA_CMD -version

# 检查是否以后台模式运行（添加-d参数）
if [ "$1" = "-d" ] || [ "$1" = "--daemon" ]; then
    echo "以后台模式启动..."
    nohup $JAVA_CMD -Xms512m -Xmx512m \
        -jar "$JAR_FILE" \
        --spring.profiles.active=local \
        --spring.redis.host=127.0.0.1 \
        --spring.redis.port=6379 > nohup.out 2>&1 &
    echo "后端服务已在后台启动，PID: $!"
    echo "日志文件: nohup.out"
    echo "使用 'tail -f nohup.out' 查看日志"
    echo "使用 './stop-backend.sh' 停止服务"
else
    echo "前台模式启动（Ctrl+C停止服务）"
    echo "提示: 使用 './start-backend.sh -d' 在后台启动"
    # 前台启动
    $JAVA_CMD -Xms512m -Xmx512m \
        -jar "$JAR_FILE" \
        --spring.profiles.active=local \
        --spring.redis.host=127.0.0.1 \
        --spring.redis.port=6379
fi

