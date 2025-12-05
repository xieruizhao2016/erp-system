#!/bin/bash

# 启动宝塔面板服务脚本
# 在VNC控制台或SSH中执行此脚本

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}=== 启动宝塔面板服务 ===${NC}"
echo ""

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then 
    echo -e "${RED}错误: 请使用root用户执行此脚本${NC}"
    exit 1
fi

# 启动宝塔面板
echo -e "${YELLOW}步骤1: 启动宝塔面板服务...${NC}"
/etc/init.d/bt start

# 等待服务启动
sleep 3

# 检查服务状态
echo ""
echo -e "${YELLOW}步骤2: 检查服务状态...${NC}"
/etc/init.d/bt status

# 检查端口监听
echo ""
echo -e "${YELLOW}步骤3: 检查端口监听...${NC}"
if netstat -tlnp | grep -q 23448; then
    echo -e "${GREEN}✅ 端口23448正在监听${NC}"
    netstat -tlnp | grep 23448
else
    echo -e "${RED}⚠️  端口23448未监听${NC}"
fi

# 显示登录信息
echo ""
echo -e "${YELLOW}步骤4: 宝塔面板登录信息${NC}"
echo "=================================="
/etc/init.d/bt default
echo "=================================="

# 检查防火墙
echo ""
echo -e "${YELLOW}步骤5: 检查防火墙...${NC}"
if firewall-cmd --list-ports 2>/dev/null | grep -q 23448; then
    echo -e "${GREEN}✅ 防火墙端口已开放${NC}"
else
    echo -e "${YELLOW}⚠️  防火墙端口未开放，正在开放...${NC}"
    firewall-cmd --permanent --add-port=23448/tcp 2>/dev/null && \
    firewall-cmd --reload 2>/dev/null && \
    echo -e "${GREEN}✅ 防火墙端口已开放${NC}" || \
    echo -e "${YELLOW}⚠️  防火墙可能未启用或命令失败${NC}"
fi

echo ""
echo -e "${GREEN}=== 完成 ===${NC}"
echo ""
echo "访问地址: https://115.190.240.137:23448/a39fc057"
echo "用户名: xla9o1ru"
echo "密码: 540e2adc"
echo ""
echo "如果仍然无法访问，请检查："
echo "1. 安全组规则是否开放端口23448"
echo "2. 服务器是否正常运行"
echo "3. 网络连接是否正常"

