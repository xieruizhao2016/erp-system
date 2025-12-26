# BOM明细表添加工序名称冗余字段迁移说明

## 脚本文件
`erp_product_bom_item_add_process_name.sql`

## 功能说明
为 `erp_product_bom_item` 表添加 `process_name` 冗余字段，用于优化列表查询性能。

### 优化效果
- **性能提升**：列表查询不再需要关联 `erp_process` 表
- **代码简化**：前端不再需要维护工序列表映射关系
- **数据一致性**：保存时自动填充，保证数据准确

## 执行方法

### 方法一：使用MySQL命令行（推荐）

```bash
# 1. 登录MySQL
mysql -uroot -p123456

# 2. 选择数据库
USE ruoyi-vue-pro;

# 3. 执行迁移脚本
source sql/mysql/erp_product_bom_item_add_process_name.sql;
```

### 方法二：直接执行SQL文件

```bash
mysql -uroot -p123456 ruoyi-vue-pro < sql/mysql/erp_product_bom_item_add_process_name.sql
```

### 方法三：使用数据库管理工具

使用 Navicat、DataGrip、MySQL Workbench 等工具：
1. 连接到数据库 `ruoyi-vue-pro`
2. 打开文件 `sql/mysql/erp_product_bom_item_add_process_name.sql`
3. 执行脚本

## 脚本内容

### 1. 添加字段
- 字段名：`process_name`
- 类型：`varchar(255)`
- 位置：在 `process_id` 字段之后
- 说明：工序名称（冗余字段，用于列表显示，避免关联查询）

### 2. 更新已有数据
- 自动更新已有BOM明细的工序名称
- 只更新 `process_id` 不为空且 `process_name` 为空的数据

## 安全性

✅ **可以安全地重复执行**
- 脚本会检查字段是否已存在
- 如果字段已存在，会跳过添加操作
- 不会影响已有数据

## 验证执行结果

执行脚本后，可以运行以下SQL验证：

```sql
-- 1. 检查字段是否添加成功
DESC erp_product_bom_item;

-- 应该看到 process_name 字段在 process_id 之后

-- 2. 检查已有数据是否更新
SELECT 
    id,
    process_id,
    process_name,
    child_product_name
FROM erp_product_bom_item
WHERE process_id IS NOT NULL
LIMIT 10;

-- 应该看到 process_name 字段有值
```

## 后续操作

执行完迁移脚本后，需要：

1. **重新编译后端**
   ```bash
   mvn clean package -DskipTests
   ```

2. **重启后端服务**
   ```bash
   ./scripts/dev/start-backend.sh -d
   ```

3. **验证功能**
   - 新增BOM明细时，选择工序后会自动填充工序名称
   - 编辑BOM明细时，修改工序后会自动更新工序名称
   - BOM明细列表直接显示工序名称，无需关联查询

## 注意事项

1. **备份数据**：执行前建议备份数据库
2. **执行顺序**：可以在任何时候执行，不影响系统运行
3. **数据一致性**：系统会自动维护 `process_name` 字段，无需手动维护

## 回滚方案

如果需要回滚（删除字段），可以执行：

```sql
ALTER TABLE `erp_product_bom_item` DROP COLUMN `process_name`;
```

**注意**：回滚后需要重新编译后端代码，移除相关字段的使用。

