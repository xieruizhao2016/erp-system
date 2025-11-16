# 生产管理系统数据库表结构说明

## 📋 文件说明

**文件名**: `erp-production-tables.sql`  
**创建时间**: 2025-01-XX  
**说明**: 根据《生产管理系统开发计划-细化版.md》设计，包含Phase 1-4所有数据库表结构

## 🗂️ 表结构概览

### Phase 1: 生产订单管理
- **现有表字段扩展**（6个字段）
  - `erp_product`: `production_type`, `is_bom`
  - `erp_warehouse`: `warehouse_type`
  - `erp_sale_order`: `production_status`
  - `erp_purchase_order`: `production_order_id`
  - `erp_stock_in`: `production_order_id`
  - `erp_stock_out`: `production_order_id`

### Phase 2: 生产计划管理（8张表）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `erp_product_bom` | BOM主表 | bom_no, product_id, version, status |
| `erp_product_bom_item` | BOM明细表 | bom_id, parent_product_id, child_product_id, quantity |
| `erp_process_route` | 工艺路线主表 | route_no, product_id, standard_cycle_time |
| `erp_process_route_item` | 工艺路线明细表 | route_id, process_id, sequence, standard_time |
| `erp_production_schedule` | 生产排程主表 | schedule_no, schedule_type, start_date, end_date |
| `erp_production_schedule_item` | 排程明细表 | schedule_id, production_order_id, planned_start_time |
| `erp_mrp_params` | MRP参数表 | param_code, param_value, param_type |
| `erp_mrp_result` | MRP运算结果表 | run_no, product_id, net_requirement, order_type |

### Phase 3: 生产执行与质量管理（8张表）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `erp_work_order` | 工单主表 | work_order_no, production_order_id, status |
| `erp_work_order_progress` | 工单进度表 | work_order_id, process_id, completed_quantity |
| `erp_quality_standard` | 质检标准表 | standard_no, product_id, inspection_type |
| `erp_quality_item` | 质检项目表 | standard_id, item_name, item_type, standard_value |
| `erp_quality_inspection` | 质检记录表 | inspection_no, product_id, inspection_result |
| `erp_quality_inspection_item` | 质检明细表 | inspection_id, item_id, test_result, actual_value |
| `erp_equipment` | 设备台账表 | equipment_no, equipment_name, status, capacity |
| `erp_equipment_status` | 设备状态记录表 | equipment_id, status, status_start_time, duration |

### Phase 4: 成本核算与数据分析（7张表）

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| `erp_cost_standard` | 标准成本表 | product_id, material_cost, labor_cost, overhead_cost |
| `erp_cost_actual` | 实际成本表 | cost_no, work_order_id, material_cost, total_cost |
| `erp_cost_variance` | 成本差异分析表 | cost_actual_id, total_variance, variance_type |
| `erp_work_hours` | 工时统计表 | work_order_id, operator_id, duration, labor_cost |
| `erp_production_kpi` | 生产KPI表 | kpi_no, kpi_type, actual_value, target_value |
| `erp_production_report` | 生产报表表 | report_no, report_type, completion_rate, oee |
| `erp_production_dashboard_config` | 看板配置表 | config_name, config_type, layout_config |

## 🚀 使用方法

### 方法一：使用MySQL命令行

```bash
# 1. 登录MySQL
mysql -uroot -p123456

# 2. 选择数据库
USE ruoyi-vue-pro;

# 3. 执行SQL脚本
source sql/mysql/erp-production-tables.sql;
```

### 方法二：使用数据库管理工具

使用 Navicat、DataGrip、MySQL Workbench 等工具：

1. 连接到MySQL数据库
2. 选择数据库 `ruoyi-vue-pro`
3. 打开并执行 `sql/mysql/erp-production-tables.sql` 文件

### 方法三：使用初始化脚本

```bash
# 在项目根目录执行
mysql -uroot -p123456 ruoyi-vue-pro < sql/mysql/erp-production-tables.sql
```

## ⚠️ 注意事项

### 1. 执行顺序
- **必须先执行** `ruoyi-vue-pro.sql`（基础表结构）
- **然后执行** `erp-tables.sql`（现有ERP业务表）
- **最后执行** `erp-production-tables.sql`（生产管理表）

### 2. 字段扩展
- 脚本使用动态SQL检查列是否存在，避免重复添加
- 如果字段已存在，脚本会跳过，不会报错

### 3. 表依赖关系
- 生产订单表 `erp_production_order` 已在 `erp_production_order.sql` 中创建
- 本脚本中的生产订单表定义已注释，如需重新创建请取消注释

### 4. 索引说明
- 所有表都包含必要的索引，优化查询性能
- 主要索引包括：租户ID、状态、时间范围、关联ID等

## 📊 表结构统计

| 阶段 | 表数量 | 说明 |
|------|--------|------|
| Phase 1 | 6个字段扩展 | 现有表字段扩展 |
| Phase 2 | 8张表 | BOM、工艺路线、排程、MRP |
| Phase 3 | 8张表 | 工单、质量、设备 |
| Phase 4 | 7张表 | 成本、KPI、报表、看板 |
| **总计** | **23张新表 + 6个字段扩展** | **完整生产管理系统** |

## 🔍 验证表结构

执行SQL脚本后，可以使用以下SQL验证表是否创建成功：

```sql
-- 查看所有生产管理相关表
SHOW TABLES LIKE 'erp_%';

-- 查看Phase 2表
SHOW TABLES LIKE 'erp_product_bom%';
SHOW TABLES LIKE 'erp_process_route%';
SHOW TABLES LIKE 'erp_production_schedule%';
SHOW TABLES LIKE 'erp_mrp%';

-- 查看Phase 3表
SHOW TABLES LIKE 'erp_work_order%';
SHOW TABLES LIKE 'erp_quality%';
SHOW TABLES LIKE 'erp_equipment%';

-- 查看Phase 4表
SHOW TABLES LIKE 'erp_cost%';
SHOW TABLES LIKE 'erp_production_kpi%';
SHOW TABLES LIKE 'erp_production_report%';
SHOW TABLES LIKE 'erp_production_dashboard%';

-- 查看表结构
DESC erp_product_bom;
DESC erp_work_order;
DESC erp_cost_actual;
```

## 📝 字段扩展验证

```sql
-- 验证产品表字段扩展
DESC erp_product;
-- 应该看到 production_type 和 is_bom 字段

-- 验证仓库表字段扩展
DESC erp_warehouse;
-- 应该看到 warehouse_type 字段

-- 验证销售订单表字段扩展
DESC erp_sale_order;
-- 应该看到 production_status 字段

-- 验证采购订单表字段扩展
DESC erp_purchase_order;
-- 应该看到 production_order_id 字段

-- 验证库存表字段扩展
DESC erp_stock_in;
DESC erp_stock_out;
-- 应该看到 production_order_id 字段
```

## 🔧 常见问题

### Q1: 执行时提示表已存在
**A**: 这是正常的，脚本使用 `DROP TABLE IF EXISTS`，会先删除再创建。如果不想删除现有数据，请先备份。

### Q2: 执行ALTER TABLE时提示列已存在
**A**: 脚本已使用动态SQL检查列是否存在，如果列已存在会跳过。如果仍然报错，可能是列名或类型不匹配。

### Q3: 外键约束错误
**A**: 确保已执行 `ruoyi-vue-pro.sql` 和 `erp-tables.sql`，因为生产管理表可能依赖这些基础表。

### Q4: 字符集问题
**A**: 脚本已设置 `SET NAMES utf8mb4`，确保使用UTF-8字符集。

## 📚 相关文档

- [生产管理系统开发计划-细化版.md](../../生产管理系统开发计划-细化版.md)
- [代码生成器使用指南.md](../../代码生成器使用指南.md)
- [数据库初始化说明.md](../../数据库初始化说明.md)

## 🎯 下一步操作

1. ✅ 执行SQL脚本创建表结构
2. ⬜ 使用代码生成器生成各模块代码
3. ⬜ 根据业务需求调整表结构
4. ⬜ 编写单元测试和集成测试
5. ⬜ 完善业务逻辑和前端界面

---

**最后更新**: 2025-01-XX  
**维护人**: 开发团队

