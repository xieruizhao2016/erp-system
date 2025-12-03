#!/usr/bin/expect -f

# SSH连接和系统初始化脚本
# 使用方法: ./setup-ssh-and-init.sh

set timeout 60
set server_ip "115.190.240.137"
set server_user "root"
set server_password "Xie12580..."
set public_key "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEQDdP8ejFN3p06jCyTc3BI1TJrBPxtL33v9ezL95CmMtBJ/R3RYB3YmMUm69ZRhajY6kmnSchXBIvJxcVVB7VeiH2GqCAWPGFYzbQQxwiBUfZTEv3cIHFvttTJ9cMh9aEVXKLT1E6y91GiL1UPXxTZ+0RyZiSVdeP9iISzIj/MESBLjFgmQskK570kaGnmNZB9AchzA/xxOtf86XHiVXhfyWLbwtZYQtlZohUt9bku/8u4DlDPjX2z2LOlT09VbjS+q2hbPap6zUwndoksdICkitFCKn10t7wnrVQXaOPWAju7DOw4RBK77prcnMJfe8yq5gzLbdmqCaJwiNss+V7"

# 连接服务器
spawn ssh -o StrictHostKeyChecking=accept-new ${server_user}@${server_ip}

expect {
    "password:" {
        send "${server_password}\r"
        exp_continue
    }
    "Password:" {
        send "${server_password}\r"
        exp_continue
    }
    "yes/no" {
        send "yes\r"
        exp_continue
    }
    -re "\\\[root@.*\\\]# " {
        # CentOS root提示符
    }
    -re "root@.*:.*# " {
        # Ubuntu root提示符
    }
    "# " {
        # 通用root提示符
    }
    "$ " {
        # 非root用户提示符
    }
    timeout {
        puts "连接超时"
        exit 1
    }
}

# 等待命令提示符（更灵活的匹配）
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {
        # 即使超时也继续，可能已经登录成功
        sleep 1
    }
}

# 发送一个简单的命令来确认连接
send "\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 1. 创建.ssh目录并设置权限
send "mkdir -p ~/.ssh && chmod 700 ~/.ssh\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 2. 检查公钥是否已存在
send "grep -q '${public_key}' ~/.ssh/authorized_keys 2>/dev/null && echo '公钥已存在' || echo '${public_key}' >> ~/.ssh/authorized_keys\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 3. 设置authorized_keys权限
send "chmod 600 ~/.ssh/authorized_keys\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 4. 检测系统类型
send "echo '=== 检测系统类型 ==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

send "if \[ -f /etc/redhat-release \]; then OS_TYPE='centos'; elif \[ -f /etc/debian_version \]; then OS_TYPE='debian'; else OS_TYPE='unknown'; fi && echo \\\$OS_TYPE > /tmp/os_type.txt && cat /etc/os-release | head -1\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 5. 更新系统（根据系统类型选择命令）
send "echo '=== 开始更新系统 ==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

set timeout 600
send "if \[ -f /etc/redhat-release \]; then yum update -y; else apt update && apt upgrade -y; fi\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}
set timeout 60

# 6. 安装常用工具
send "echo '=== 安装常用工具 ==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

set timeout 300
send "if \[ -f /etc/redhat-release \]; then yum install -y curl wget vim git net-tools htop; else apt install -y curl wget vim git net-tools htop; fi\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}
set timeout 60

# 7. 配置时区
send "echo '=== 配置时区 ==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

send "timedatectl set-timezone Asia/Shanghai 2>/dev/null || ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 8. 验证配置
send "echo '=== 验证配置 ==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

send "hostname && uname -a && timedatectl 2>/dev/null || date && free -h && df -h\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 9. 显示完成信息
send "echo '=== 配置完成！==='\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

send "echo '现在可以使用密钥登录: ssh erp-server'\r"
expect {
    -re "\\\[root@.*\\\]# " {}
    -re "root@.*:.*# " {}
    "# " {}
    "$ " {}
    timeout {}
}

# 退出
send "exit\r"
expect eof

puts "\n✅ SSH公钥已添加，系统初始化完成！"
puts "现在可以使用以下命令连接（无需密码）："
puts "  ssh erp-server"

