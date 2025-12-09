#!/bin/bash

# 服务器安全加固脚本
# 修复宝塔面板检测到的安全问题
# 使用方法: ./security-hardening.sh

set -e

# 颜色输出
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== 服务器安全加固脚本 ===${NC}"
echo ""

# 检查是否为root用户
if [ "$EUID" -ne 0 ]; then 
    echo -e "${RED}错误: 请使用 sudo 运行此脚本${NC}"
    exit 1
fi

# 备份配置文件
backup_file() {
    local file=$1
    if [ -f "$file" ]; then
        cp "$file" "${file}.bak.$(date +%Y%m%d-%H%M%S)"
        echo -e "${GREEN}✅ 已备份: $file${NC}"
    fi
}

# 应用sysctl配置
apply_sysctl() {
    local key=$1
    local value=$2
    if ! grep -q "^${key}" /etc/sysctl.conf 2>/dev/null; then
        echo "${key} = ${value}" >> /etc/sysctl.conf
        echo -e "${GREEN}✅ 已添加: ${key} = ${value}${NC}"
    else
        sed -i "s|^${key}.*|${key} = ${value}|" /etc/sysctl.conf
        echo -e "${YELLOW}⚠️  已更新: ${key} = ${value}${NC}"
    fi
}

echo -e "${BLUE}步骤1: 备份配置文件${NC}"
backup_file /etc/sysctl.conf
backup_file /etc/security/limits.conf
backup_file /etc/modprobe.d/blacklist.conf
backup_file /etc/ssh/sshd_config
echo ""

echo -e "${BLUE}步骤2: 修复网络相关配置${NC}"

# IPv4 相关
apply_sysctl "net.ipv4.conf.all.send_redirects" "0"
apply_sysctl "net.ipv4.conf.default.send_redirects" "0"
apply_sysctl "net.ipv4.conf.all.accept_redirects" "0"
apply_sysctl "net.ipv4.conf.default.accept_redirects" "0"
apply_sysctl "net.ipv4.conf.all.accept_source_route" "0"
apply_sysctl "net.ipv4.conf.default.accept_source_route" "0"
apply_sysctl "net.ipv4.icmp_echo_ignore_broadcasts" "1"
apply_sysctl "net.ipv4.icmp_ignore_bogus_error_responses" "1"
apply_sysctl "net.ipv4.ip_forward" "0"  # 如果不需要路由功能
apply_sysctl "net.ipv4.conf.all.log_martians" "1"
apply_sysctl "net.ipv4.conf.default.log_martians" "1"

# IPv6 相关（如果使用IPv6，需要谨慎配置）
apply_sysctl "net.ipv6.conf.all.accept_redirects" "0"
apply_sysctl "net.ipv6.conf.default.accept_redirects" "0"
apply_sysctl "net.ipv6.conf.all.accept_source_route" "0"
apply_sysctl "net.ipv6.conf.default.accept_source_route" "0"
apply_sysctl "net.ipv6.conf.all.accept_ra" "0"
apply_sysctl "net.ipv6.conf.default.accept_ra" "0"
apply_sysctl "net.ipv6.conf.all.forwarding" "0"
apply_sysctl "net.ipv6.conf.default.forwarding" "0"

echo ""

echo -e "${BLUE}步骤3: 修复SSH配置${NC}"

# SSH配置
SSH_CONFIG="/etc/ssh/sshd_config"

# ClientAliveInterval: SSH空闲超时时间（秒）
if ! grep -q "^ClientAliveInterval" "$SSH_CONFIG"; then
    echo "ClientAliveInterval 300" >> "$SSH_CONFIG"
    echo -e "${GREEN}✅ 已添加: ClientAliveInterval 300${NC}"
fi

# ClientAliveCountMax: 最大空闲检查次数
if ! grep -q "^ClientAliveCountMax" "$SSH_CONFIG"; then
    echo "ClientAliveCountMax 2" >> "$SSH_CONFIG"
    echo -e "${GREEN}✅ 已添加: ClientAliveCountMax 2${NC}"
fi

# LoginGraceTime: 登录超时时间
if ! grep -q "^LoginGraceTime" "$SSH_CONFIG"; then
    echo "LoginGraceTime 60" >> "$SSH_CONFIG"
    echo -e "${GREEN}✅ 已添加: LoginGraceTime 60${NC}"
fi

# Banner: SSH登录警告
if ! grep -q "^Banner" "$SSH_CONFIG"; then
    echo "Banner /etc/issue.net" >> "$SSH_CONFIG"
    echo -e "${GREEN}✅ 已添加: Banner /etc/issue.net${NC}"
    # 创建警告文件
    if [ ! -f /etc/issue.net ]; then
        cat > /etc/issue.net << 'EOF'
***************************************************************************
                            WARNING NOTICE
This system is for the use of authorized users only. Individuals using
this computer system without authority, or in excess of their authority,
are subject to having all of their activities on this system monitored
and recorded by system personnel.
***************************************************************************
EOF
        echo -e "${GREEN}✅ 已创建: /etc/issue.net${NC}"
    fi
fi

# 确保不允许空密码
sed -i 's/^#*PermitEmptyPasswords.*/PermitEmptyPasswords no/' "$SSH_CONFIG"
echo -e "${GREEN}✅ 已设置: PermitEmptyPasswords no${NC}"

# 确保不允许空密码sudo
if [ -f /etc/sudoers ]; then
    if ! grep -q "^Defaults.*requiretty" /etc/sudoers; then
        echo "Defaults requiretty" >> /etc/sudoers
    fi
    if ! grep -q "^Defaults.*!authenticate" /etc/sudoers; then
        echo "Defaults !authenticate" >> /etc/sudoers
    fi
    echo -e "${GREEN}✅ 已配置: sudo 安全设置${NC}"
fi

echo ""

echo -e "${BLUE}步骤4: 禁用不必要的内核模块${NC}"

# 禁用内核模块
MODULES_TO_DISABLE=(
    "dccp"
    "sctp"
    "rds"
    "tipc"
    "udf"
    "jffs2"
)

for module in "${MODULES_TO_DISABLE[@]}"; do
    if ! grep -q "blacklist ${module}" /etc/modprobe.d/blacklist.conf 2>/dev/null; then
        echo "install ${module} /bin/true" >> /etc/modprobe.d/blacklist.conf
        echo "blacklist ${module}" >> /etc/modprobe.d/blacklist.conf
        echo -e "${GREEN}✅ 已禁用: ${module} 模块${NC}"
    fi
done

echo ""

echo -e "${BLUE}步骤5: 限制核心转储${NC}"

# 限制核心转储
apply_sysctl "fs.suid_dumpable" "0"

# 在limits.conf中添加
if ! grep -q "^\\*.*soft.*core.*0" /etc/security/limits.conf; then
    echo "* soft core 0" >> /etc/security/limits.conf
    echo "* hard core 0" >> /etc/security/limits.conf
    echo -e "${GREEN}✅ 已限制: 核心转储${NC}"
fi

echo ""

echo -e "${BLUE}步骤6: 检查空密码用户${NC}"

# 检查空密码用户
EMPTY_PASSWORD_USERS=$(awk -F: '($2 == "" || $2 == "!") {print $1}' /etc/shadow 2>/dev/null | grep -v "^root$" || true)

if [ -n "$EMPTY_PASSWORD_USERS" ]; then
    echo -e "${YELLOW}⚠️  发现空密码用户:${NC}"
    echo "$EMPTY_PASSWORD_USERS"
    echo -e "${YELLOW}请手动为这些用户设置密码: sudo passwd <用户名>${NC}"
else
    echo -e "${GREEN}✅ 未发现空密码用户${NC}"
fi

echo ""

echo -e "${BLUE}步骤7: 配置/tmp挂载选项${NC}"

# 检查/tmp挂载选项
TMP_MOUNT=$(mount | grep " /tmp " || true)
if [ -n "$TMP_MOUNT" ]; then
    if echo "$TMP_MOUNT" | grep -q "nosuid"; then
        echo -e "${GREEN}✅ /tmp 已配置 nosuid${NC}"
    else
        echo -e "${YELLOW}⚠️  /tmp 未配置 nosuid，需要手动配置${NC}"
        echo "编辑 /etc/fstab，为 /tmp 添加 nosuid,noexec,nodev 选项"
    fi
else
    echo -e "${YELLOW}⚠️  /tmp 未单独挂载，使用系统默认配置${NC}"
fi

echo ""

echo -e "${BLUE}步骤8: 应用配置${NC}"

# 应用sysctl配置
sysctl -p > /dev/null 2>&1
echo -e "${GREEN}✅ 已应用 sysctl 配置${NC}"

# 重启SSH服务（不中断当前连接）
if systemctl is-active --quiet sshd; then
    systemctl reload sshd
    echo -e "${GREEN}✅ 已重新加载 SSH 配置${NC}"
fi

echo ""
echo -e "${GREEN}=== 安全加固完成 ===${NC}"
echo ""
echo -e "${BLUE}下一步操作：${NC}"
echo "1. 重新运行宝塔面板安全扫描，验证修复效果"
echo "2. 如果发现空密码用户，请手动设置密码"
echo "3. 部分配置需要重启服务器才能完全生效（可选）"
echo ""
echo -e "${YELLOW}注意：${NC}"
echo "- IPv4转发已禁用，如果服务器需要路由功能，请手动启用"
echo "- IPv6相关配置已禁用，如果使用IPv6，请谨慎调整"
echo "- 已禁用多个内核模块，如果业务需要，请手动启用"
echo ""

