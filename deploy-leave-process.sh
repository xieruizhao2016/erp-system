#!/bin/bash

# 部署请假流程模型脚本
# 需要先登录获取token

BASE_URL="http://localhost:48080"
USERNAME="admin"
PASSWORD="admin123"

echo "正在部署请假审批流程模型..."

# 1. 登录获取token
echo "1. 登录系统..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/admin-api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"$USERNAME\",
    \"password\": \"$PASSWORD\"
  }")

# 提取token
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.accessToken')

if [ "$TOKEN" == "null" ]; then
    echo "登录失败，请检查用户名和密码"
    echo "响应: $LOGIN_RESPONSE"
    exit 1
fi

echo "登录成功，获取到token"

# 2. 创建流程模型
echo "2. 创建流程模型..."
MODEL_RESPONSE=$(curl -s -X POST "$BASE_URL/bpm/model/create" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"请假审批流程\",
    \"category\": \"LEAVE\",
    \"description\": \"员工请假审批流程\",
    \"sort\": 1
  }")

echo "创建模型响应: $MODEL_RESPONSE"

# 3. 上传BPMN文件（如果需要）
# 暂时跳过，因为创建模型时应该已经创建了基础模型

echo "流程模型创建完成！"
echo "请在前端访问: $BASE_URL/bpm/model 查看并配置流程图"