-- ============================================================
-- 生产管理系统关联字段验证脚本
-- 用于验证所有生产订单关联字段是否正确添加
-- 创建时间: 2024年
-- ============================================================

SET @dbname = DATABASE();

-- ============================================================
-- 1. 检查所有包含 production_order_id 字段的表
-- ============================================================
SELECT 
    '=== 所有包含 production_order_id 字段的表 ===' AS '检查项';

SELECT 
    TABLE_NAME AS '表名',
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_DEFAULT AS '默认值',
    COLUMN_COMMENT AS '字段说明',
    CASE 
        WHEN IS_NULLABLE = 'YES' THEN '✓ 可为空（可选关联）'
        ELSE '✓ 必填（必须关联）'
    END AS '关联类型'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'production_order_id'
ORDER BY TABLE_NAME;

-- ============================================================
-- 2. 检查索引是否已创建
-- ============================================================
SELECT 
    '=== production_order_id 字段的索引检查 ===' AS '检查项';

SELECT 
    TABLE_NAME AS '表名',
    INDEX_NAME AS '索引名',
    COLUMN_NAME AS '字段名',
    NON_UNIQUE AS '非唯一索引',
    CASE 
        WHEN INDEX_NAME = 'PRIMARY' THEN '主键'
        WHEN NON_UNIQUE = 0 THEN '唯一索引'
        ELSE '普通索引'
    END AS '索引类型'
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'production_order_id'
ORDER BY TABLE_NAME, INDEX_NAME;

-- ============================================================
-- 3. 检查必需的关联字段
-- ============================================================
SELECT 
    '=== 必需的关联字段检查 ===' AS '检查项';

-- 3.1 采购订单表
SELECT 
    'erp_purchase_order' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_purchase_order'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.2 库存入库表
SELECT 
    'erp_stock_in' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_stock_in'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.3 库存出库表
SELECT 
    'erp_stock_out' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_stock_out'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.4 销售订单表
SELECT 
    'erp_sale_order' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_sale_order'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.5 收款单表
SELECT 
    'erp_finance_receipt' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_finance_receipt'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.6 工单表
SELECT 
    'erp_work_order' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_work_order'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.7 排程明细表
SELECT 
    'erp_production_schedule_item' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_production_schedule_item'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

-- 3.8 实际成本表
SELECT 
    'erp_cost_actual' AS '表名',
    'production_order_id' AS '字段名',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_cost_actual'
    AND COLUMN_NAME = 'production_order_id';

-- ============================================================
-- 4. 检查生产订单表的关联字段
-- ============================================================
SELECT 
    '=== 生产订单表的关联字段 ===' AS '检查项';

SELECT 
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_COMMENT AS '字段说明'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_production_order'
    AND COLUMN_NAME IN ('customer_id', 'product_id', 'source_id', 'source_type')
ORDER BY COLUMN_NAME;

-- ============================================================
-- 5. 检查销售订单表的生产相关字段
-- ============================================================
SELECT 
    '=== 销售订单表的生产相关字段 ===' AS '检查项';

SELECT 
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_COMMENT AS '字段说明'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_sale_order'
    AND COLUMN_NAME IN ('production_status', 'production_order_id')
ORDER BY COLUMN_NAME;

-- ============================================================
-- 6. 统计关联关系完整性
-- ============================================================
SELECT 
    '=== 关联关系完整性统计 ===' AS '检查项';

SELECT 
    COUNT(DISTINCT TABLE_NAME) AS '包含production_order_id字段的表数量',
    COUNT(DISTINCT CASE WHEN IS_NULLABLE = 'NO' THEN TABLE_NAME END) AS '必填关联字段的表数量',
    COUNT(DISTINCT CASE WHEN IS_NULLABLE = 'YES' THEN TABLE_NAME END) AS '可选关联字段的表数量'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'production_order_id';

-- ============================================================
-- 7. 检查间接关联关系（通过工单）
-- ============================================================
SELECT 
    '=== 间接关联关系检查 ===' AS '检查项';

-- 质检记录表通过工单关联生产订单
SELECT 
    'erp_quality_inspection' AS '表名',
    'work_order_id' AS '关联字段',
    '通过 erp_work_order.production_order_id 间接关联生产订单' AS '关联说明',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_quality_inspection'
    AND COLUMN_NAME = 'work_order_id'

UNION ALL

-- 工单进度表通过工单关联生产订单
SELECT 
    'erp_work_order_progress' AS '表名',
    'work_order_id' AS '关联字段',
    '通过 erp_work_order.production_order_id 间接关联生产订单' AS '关联说明',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_work_order_progress'
    AND COLUMN_NAME = 'work_order_id'

UNION ALL

-- 工时统计表通过工单关联生产订单
SELECT 
    'erp_work_hours' AS '表名',
    'work_order_id' AS '关联字段',
    '通过 erp_work_order.production_order_id 间接关联生产订单' AS '关联说明',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_work_hours'
    AND COLUMN_NAME = 'work_order_id'

UNION ALL

-- 实际成本表通过工单关联生产订单
SELECT 
    'erp_cost_actual' AS '表名',
    'work_order_id' AS '关联字段',
    '通过 erp_work_order.production_order_id 间接关联生产订单' AS '关联说明',
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS '状态'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_cost_actual'
    AND COLUMN_NAME = 'work_order_id';

-- ============================================================
-- 8. 生成关联关系汇总报告
-- ============================================================
SELECT 
    '=== 关联关系汇总报告 ===' AS '检查项';

SELECT 
    '直接关联' AS '关联类型',
    TABLE_NAME AS '表名',
    COLUMN_NAME AS '关联字段',
    'erp_production_order' AS '关联目标表'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

SELECT 
    '间接关联（通过工单）' AS '关联类型',
    TABLE_NAME AS '表名',
    'work_order_id' AS '关联字段',
    'erp_production_order (通过 erp_work_order)' AS '关联目标表'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'work_order_id'
    AND TABLE_NAME IN ('erp_quality_inspection', 'erp_work_order_progress', 'erp_work_hours', 'erp_cost_actual')

ORDER BY '关联类型', 2;

