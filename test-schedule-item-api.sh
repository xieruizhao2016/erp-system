#!/bin/bash

# 排程明细API诊断脚本

echo "=========================================="
echo "排程明细API诊断测试"
echo "=========================================="
echo ""

# 测试后端服务基本连接
echo "1. 测试后端服务是否运行..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/system/auth/get-permission-info)
if [ "$response" = "401" ]; then
    echo "✓ 后端服务正常运行（端口48080）"
else
    echo "✗ 后端服务异常，状态码: $response"
    exit 1
fi
echo ""

# 测试排程明细API（无需登录，看看是否404）
echo "2. 测试排程明细API是否存在..."
response=$(curl -s -w "\n%{http_code}" http://localhost:48080/admin-api/erp/production-schedule-item/page?pageNo=1&pageSize=10)
http_code=$(echo "$response" | tail -n 1)
body=$(echo "$response" | sed '$d')

echo "HTTP状态码: $http_code"
echo "响应内容: $body"
echo ""

if [ "$http_code" = "404" ]; then
    echo "✗ 问题：排程明细API不存在（404）"
    echo ""
    echo "可能原因："
    echo "  1. 后端Controller未编译或未部署"
    echo "  2. 后端代码有编译错误"
    echo "  3. 数据库表未创建导致Mapper初始化失败"
    echo ""
    echo "建议解决方案："
    echo "  1. 检查后端编译是否成功"
    echo "  2. 查看后端启动日志是否有错误"
    echo "  3. 执行数据库表创建SQL"
elif [ "$http_code" = "403" ]; then
    echo "✓ API存在，但需要权限（这是正常的）"
    echo ""
    echo "解决方案："
    echo "  1. 执行菜单和权限配置SQL"
    echo "  2. 在系统管理中给角色分配权限"
elif [ "$http_code" = "401" ]; then
    echo "✓ API存在，需要登录（这是正常的）"
    echo ""
    echo "后端API配置正常，问题可能在前端或权限配置"
elif [ "$http_code" = "500" ]; then
    echo "✗ 服务器内部错误（500）"
    echo ""
    echo "可能原因："
    echo "  1. 数据库表不存在"
    echo "  2. SQL查询错误"
    echo "  3. 代码逻辑异常"
    echo ""
    echo "需要查看后端日志获取详细错误"
fi
echo ""

# 测试依赖的API
echo "3. 测试依赖的API..."
echo ""

echo "3.1 测试生产排程API..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/erp/production-schedule/page?pageNo=1&pageSize=10)
if [ "$response" = "401" ] || [ "$response" = "403" ]; then
    echo "✓ 生产排程API存在"
elif [ "$response" = "404" ]; then
    echo "✗ 生产排程API不存在（404）"
else
    echo "⚠ 生产排程API状态: $response"
fi

echo "3.2 测试生产订单API..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/erp/production-order/page?pageNo=1&pageSize=10)
if [ "$response" = "401" ] || [ "$response" = "403" ]; then
    echo "✓ 生产订单API存在"
elif [ "$response" = "404" ]; then
    echo "✗ 生产订单API不存在（404）"
else
    echo "⚠ 生产订单API状态: $response"
fi

echo "3.3 测试产品API..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/erp/product/page?pageNo=1&pageSize=10)
if [ "$response" = "401" ] || [ "$response" = "403" ]; then
    echo "✓ 产品API存在"
elif [ "$response" = "404" ]; then
    echo "✗ 产品API不存在（404）"
else
    echo "⚠ 产品API状态: $response"
fi

echo "3.4 测试设备API..."
response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:48080/admin-api/erp/equipment/page?pageNo=1&pageSize=10)
if [ "$response" = "401" ] || [ "$response" = "403" ]; then
    echo "✓ 设备API存在"
elif [ "$response" = "404" ]; then
    echo "✗ 设备API不存在（404）"
else
    echo "⚠ 设备API状态: $response"
fi

echo ""
echo "=========================================="
echo "诊断完成"
echo "=========================================="

