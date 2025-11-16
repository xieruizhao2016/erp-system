-- ============================================================
-- 生产管理系统表结构验证脚本
-- 用途：验证所有生产管理相关表是否正确创建
-- 使用方法：mysql -uroot -p123456 ruoyi-vue-pro < verify-production-tables.sql
-- ============================================================

SET NAMES utf8mb4;

-- 选择数据库
USE ruoyi-vue-pro;

-- ============================================================
-- Phase 1: 验证现有表字段扩展
-- ============================================================

SELECT '=== Phase 1: 现有表字段扩展 ===' AS '验证项';

-- 验证产品表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 2 THEN '✓ erp_product 字段扩展正常'
        ELSE CONCAT('✗ erp_product 字段扩展异常，期望2个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_product'
    AND COLUMN_NAME IN ('production_type', 'is_bom');

-- 验证仓库表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ erp_warehouse 字段扩展正常'
        ELSE CONCAT('✗ erp_warehouse 字段扩展异常，期望1个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_warehouse'
    AND COLUMN_NAME = 'warehouse_type';

-- 验证销售订单表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ erp_sale_order 字段扩展正常'
        ELSE CONCAT('✗ erp_sale_order 字段扩展异常，期望1个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_sale_order'
    AND COLUMN_NAME = 'production_status';

-- 验证采购订单表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ erp_purchase_order 字段扩展正常'
        ELSE CONCAT('✗ erp_purchase_order 字段扩展异常，期望1个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_purchase_order'
    AND COLUMN_NAME = 'production_order_id';

-- 验证库存入库表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ erp_stock_in 字段扩展正常'
        ELSE CONCAT('✗ erp_stock_in 字段扩展异常，期望1个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_stock_in'
    AND COLUMN_NAME = 'production_order_id';

-- 验证库存出库表字段扩展
SELECT 
    CASE 
        WHEN COUNT(*) = 1 THEN '✓ erp_stock_out 字段扩展正常'
        ELSE CONCAT('✗ erp_stock_out 字段扩展异常，期望1个字段，实际', COUNT(*), '个')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'erp_stock_out'
    AND COLUMN_NAME = 'production_order_id';

-- ============================================================
-- Phase 2: 验证生产计划管理表
-- ============================================================

SELECT '' AS '';
SELECT '=== Phase 2: 生产计划管理表 ===' AS '验证项';

-- 验证Phase 2表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) = 8 THEN '✓ Phase 2 所有表创建成功'
        ELSE CONCAT('✗ Phase 2 表创建异常，期望8张表，实际', COUNT(*), '张')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_product_bom',
        'erp_product_bom_item',
        'erp_process_route',
        'erp_process_route_item',
        'erp_production_schedule',
        'erp_production_schedule_item',
        'erp_mrp_params',
        'erp_mrp_result'
    );

-- 列出Phase 2所有表
SELECT TABLE_NAME AS 'Phase 2 表名'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_product_bom',
        'erp_product_bom_item',
        'erp_process_route',
        'erp_process_route_item',
        'erp_production_schedule',
        'erp_production_schedule_item',
        'erp_mrp_params',
        'erp_mrp_result'
    )
ORDER BY TABLE_NAME;

-- ============================================================
-- Phase 3: 验证生产执行与质量管理表
-- ============================================================

SELECT '' AS '';
SELECT '=== Phase 3: 生产执行与质量管理表 ===' AS '验证项';

-- 验证Phase 3表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) = 8 THEN '✓ Phase 3 所有表创建成功'
        ELSE CONCAT('✗ Phase 3 表创建异常，期望8张表，实际', COUNT(*), '张')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_work_order',
        'erp_work_order_progress',
        'erp_quality_standard',
        'erp_quality_item',
        'erp_quality_inspection',
        'erp_quality_inspection_item',
        'erp_equipment',
        'erp_equipment_status'
    );

-- 列出Phase 3所有表
SELECT TABLE_NAME AS 'Phase 3 表名'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_work_order',
        'erp_work_order_progress',
        'erp_quality_standard',
        'erp_quality_item',
        'erp_quality_inspection',
        'erp_quality_inspection_item',
        'erp_equipment',
        'erp_equipment_status'
    )
ORDER BY TABLE_NAME;

-- ============================================================
-- Phase 4: 验证成本核算与数据分析表
-- ============================================================

SELECT '' AS '';
SELECT '=== Phase 4: 成本核算与数据分析表 ===' AS '验证项';

-- 验证Phase 4表是否存在
SELECT 
    CASE 
        WHEN COUNT(*) = 7 THEN '✓ Phase 4 所有表创建成功'
        ELSE CONCAT('✗ Phase 4 表创建异常，期望7张表，实际', COUNT(*), '张')
    END AS '验证结果'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_cost_standard',
        'erp_cost_actual',
        'erp_cost_variance',
        'erp_work_hours',
        'erp_production_kpi',
        'erp_production_report',
        'erp_production_dashboard_config'
    );

-- 列出Phase 4所有表
SELECT TABLE_NAME AS 'Phase 4 表名'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_cost_standard',
        'erp_cost_actual',
        'erp_cost_variance',
        'erp_work_hours',
        'erp_production_kpi',
        'erp_production_report',
        'erp_production_dashboard_config'
    )
ORDER BY TABLE_NAME;

-- ============================================================
-- 总体统计
-- ============================================================

SELECT '' AS '';
SELECT '=== 总体统计 ===' AS '验证项';

-- 统计所有生产管理相关表
SELECT 
    COUNT(*) AS '生产管理表总数',
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES 
     WHERE TABLE_SCHEMA = DATABASE() 
     AND TABLE_NAME LIKE 'erp_%') AS 'ERP表总数'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_product_bom',
        'erp_product_bom_item',
        'erp_process_route',
        'erp_process_route_item',
        'erp_production_schedule',
        'erp_production_schedule_item',
        'erp_mrp_params',
        'erp_mrp_result',
        'erp_work_order',
        'erp_work_order_progress',
        'erp_quality_standard',
        'erp_quality_item',
        'erp_quality_inspection',
        'erp_quality_inspection_item',
        'erp_equipment',
        'erp_equipment_status',
        'erp_cost_standard',
        'erp_cost_actual',
        'erp_cost_variance',
        'erp_work_hours',
        'erp_production_kpi',
        'erp_production_report',
        'erp_production_dashboard_config'
    );

-- 验证关键索引
SELECT '' AS '';
SELECT '=== 关键索引验证 ===' AS '验证项';

SELECT 
    TABLE_NAME AS '表名',
    INDEX_NAME AS '索引名',
    GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX) AS '索引字段'
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME IN (
        'erp_product_bom',
        'erp_work_order',
        'erp_cost_actual',
        'erp_production_kpi'
    )
    AND INDEX_NAME != 'PRIMARY'
GROUP BY TABLE_NAME, INDEX_NAME
ORDER BY TABLE_NAME, INDEX_NAME
LIMIT 20;

-- ============================================================
-- 验证完成
-- ============================================================

SELECT '' AS '';
SELECT '=== 验证完成 ===' AS '验证项';
SELECT '请检查上述验证结果，确保所有表和字段都已正确创建。' AS '提示';

