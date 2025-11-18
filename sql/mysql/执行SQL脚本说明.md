# 执行SQL脚本说明

## 📋 概述

本文档说明如何执行SQL脚本来添加生产管理系统缺失的关联字段。

## 🚀 执行方式

### 方式1：使用批量执行脚本（推荐）

```bash
# 进入脚本目录
cd /Users/xierui/Documents/Project/erp-system/sql/mysql

# 设置数据库连接信息（可选，默认使用配置文件中的值）
export DB_HOST=127.0.0.1
export DB_PORT=3306
export DB_NAME=ruoyi-vue-pro
export DB_USER=root
export DB_PASS=your_password

# 执行批量脚本
./execute-all-scripts.sh
```

### 方式2：手动执行单个脚本

#### 步骤1：执行生产订单表增强脚本

```bash
mysql -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro < erp-production-order-enhancement.sql
```

**功能**：
- 添加 `bom_id` 字段到生产订单表
- 添加 `route_id` 字段到生产订单表
- 创建相应的索引

#### 步骤2：执行补充关联字段脚本

```bash
mysql -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro < erp-production-association-fields.sql
```

**功能**：
- 添加 `production_order_id` 字段到销售订单表
- 添加 `production_order_id` 字段到收款单表
- 创建相应的索引

#### 步骤3：验证执行结果

```bash
mysql -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro < verify-production-associations.sql
```

### 方式3：在MySQL客户端中执行

1. 连接到MySQL数据库：
```bash
mysql -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro
```

2. 在MySQL客户端中执行：
```sql
-- 执行生产订单表增强脚本
source /Users/xierui/Documents/Project/erp-system/sql/mysql/erp-production-order-enhancement.sql;

-- 执行补充关联字段脚本
source /Users/xierui/Documents/Project/erp-system/sql/mysql/erp-production-association-fields.sql;

-- 验证执行结果
source /Users/xierui/Documents/Project/erp-system/sql/mysql/verify-production-associations.sql;
```

## 📝 执行顺序

**重要**：必须按以下顺序执行：

1. ✅ **生产订单表增强** → `erp-production-order-enhancement.sql`
   - 添加 `bom_id` 和 `route_id` 字段

2. ✅ **补充关联字段** → `erp-production-association-fields.sql`
   - 添加销售订单和收款单的 `production_order_id` 字段

3. ✅ **验证执行结果** → `verify-production-associations.sql`
   - 验证所有字段是否正确添加

4. ⚠️ **数据迁移**（如有历史数据）→ `migrate-production-associations.sql`
   - 建立历史数据的关联关系
   - **注意**：执行前必须备份数据库

## ⚠️ 注意事项

1. **备份数据库**：执行任何SQL脚本前，请先备份数据库
   ```bash
   mysqldump -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro > backup_$(date +%Y%m%d_%H%M%S).sql
   ```

2. **数据库连接信息**：
   - 默认配置：`127.0.0.1:3306`，数据库：`ruoyi-vue-pro`，用户：`root`，密码：`123456`
   - 如果使用不同的配置，请修改连接参数

3. **脚本安全性**：
   - 所有脚本都包含字段存在性检查，可以安全地重复执行
   - 如果字段已存在，脚本会自动跳过

4. **执行环境**：
   - 建议在测试环境先执行并验证
   - 确认无误后再在生产环境执行

## 🔍 验证方法

执行完成后，可以通过以下方式验证：

### 1. 使用验证脚本

```bash
mysql -h127.0.0.1 -P3306 -uroot -p123456 ruoyi-vue-pro < verify-production-associations.sql
```

### 2. 手动验证

```sql
-- 检查生产订单表的BOM和工艺路线字段
SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'ruoyi-vue-pro' 
  AND TABLE_NAME = 'erp_production_order' 
  AND COLUMN_NAME IN ('bom_id', 'route_id');

-- 检查销售订单表的生产订单关联字段
SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'ruoyi-vue-pro' 
  AND TABLE_NAME = 'erp_sale_order' 
  AND COLUMN_NAME = 'production_order_id';

-- 检查收款单表的生产订单关联字段
SELECT COLUMN_NAME, DATA_TYPE, COLUMN_COMMENT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'ruoyi-vue-pro' 
  AND TABLE_NAME = 'erp_finance_receipt' 
  AND COLUMN_NAME = 'production_order_id';
```

## 📊 预期结果

执行成功后，应该看到：

1. ✅ 生产订单表新增字段：
   - `bom_id` - 关联BOM ID
   - `route_id` - 关联工艺路线ID

2. ✅ 销售订单表新增字段：
   - `production_order_id` - 关联生产订单ID

3. ✅ 收款单表新增字段：
   - `production_order_id` - 关联生产订单ID

4. ✅ 所有字段都有相应的索引

## 🆘 常见问题

### Q1: 执行脚本时提示"表不存在"
**A:** 请确认数据库名称正确，并且表已经创建。

### Q2: 执行脚本时提示"权限不足"
**A:** 请确认数据库用户有ALTER TABLE权限。

### Q3: 如何查看执行日志？
**A:** 脚本执行时会输出执行结果，也可以查看MySQL的错误日志。

### Q4: 可以重复执行脚本吗？
**A:** 可以，脚本包含字段存在性检查，已存在的字段会被跳过。

---

*最后更新时间：2024年*

