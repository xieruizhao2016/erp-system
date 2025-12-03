# SSH连接问题解决方案

## 🔍 问题诊断

**问题现象：**
- 私钥文件格式正确（RSA格式）
- 私钥文件权限正确（400）
- 但服务器拒绝私钥认证：`Permission denied (publickey)`

**原因分析：**
服务器上没有配置对应的公钥，或者创建实例时选择的密钥对与当前私钥不匹配。

## ✅ 解决方案

### 方案1: 使用密码登录（如果有root密码）

如果你在创建实例时设置了root密码，可以使用密码登录：

```bash
ssh root@115.190.240.137
# 输入root密码
```

登录成功后，手动添加公钥：

```bash
# 1. 创建.ssh目录（如果不存在）
mkdir -p ~/.ssh
chmod 700 ~/.ssh

# 2. 添加公钥到authorized_keys
echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEQDdP8ejFN3p06jCyTc3BI1TJrBPxtL33v9ezL95CmMtBJ/R3RYB3YmMUm69ZRhajY6kmnSchXBIvJxcVVB7VeiH2GqCAWPGFYzbQQxwiBUfZTEv3cIHFvttTJ9cMh9aEVXKLT1E6y91GiL1UPXxTZ+0RyZiSVdeP9iISzIj/MESBLjFgmQskK570kaGnmNZB9AchzA/xxOtf86XHiVXhfyWLbwtZYQtlZohUt9bku/8u4DlDPjX2z2LOlT09VbjS+q2hbPap6zUwndoksdICkitFCKn10t7wnrVQXaOPWAju7DOw4RBK77prcnMJfe8yq5gzLbdmqCaJwiNss+V7" >> ~/.ssh/authorized_keys

# 3. 设置正确的权限
chmod 600 ~/.ssh/authorized_keys

# 4. 验证
cat ~/.ssh/authorized_keys
```

### 方案2: 通过火山引擎控制台VNC登录

1. 登录火山引擎控制台
2. 找到你的实例（IP: 115.190.240.137）
3. 点击"远程连接"或"VNC登录"
4. 使用root账号和密码登录
5. 执行上述添加公钥的命令

### 方案3: 检查并重新绑定密钥对

1. 登录火山引擎控制台
2. 进入实例详情页
3. 检查"密钥对"配置
4. 如果密钥对不匹配，可以：
   - 在控制台重新绑定正确的密钥对
   - 或者使用控制台提供的密钥对文件

## 🔐 公钥信息

**公钥内容：**
```
ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDEQDdP8ejFN3p06jCyTc3BI1TJrBPxtL33v9ezL95CmMtBJ/R3RYB3YmMUm69ZRhajY6kmnSchXBIvJxcVVB7VeiH2GqCAWPGFYzbQQxwiBUfZTEv3cIHFvttTJ9cMh9aEVXKLT1E6y91GiL1UPXxTZ+0RyZiSVdeP9iISzIj/MESBLjFgmQskK570kaGnmNZB9AchzA/xxOtf86XHiVXhfyWLbwtZYQtlZohUt9bku/8u4DlDPjX2z2LOlT09VbjS+q2hbPap6zUwndoksdICkitFCKn10t7wnrVQXaOPWAju7DOw4RBK77prcnMJfe8yq5gzLbdmqCaJwiNss+V7
```

**私钥文件路径：**
```
/Users/xierui/Documents/huoshan-ssh.pem
```

## ✅ 验证连接

添加公钥后，测试连接：

```bash
ssh erp-server
# 或
ssh -i /Users/xierui/Documents/huoshan-ssh.pem root@115.190.240.137
```

如果连接成功，会直接进入服务器，无需输入密码。

## 📝 下一步

连接成功后，执行系统初始化：

```bash
# 1. 更新系统
apt update && apt upgrade -y

# 2. 安装常用工具
apt install -y curl wget vim git net-tools

# 3. 配置时区
timedatectl set-timezone Asia/Shanghai

# 4. 验证配置
hostname
uname -a
timedatectl
```

---

**提示：** 如果以上方案都无法解决，请联系火山引擎技术支持，或检查实例创建时的密钥对配置。

