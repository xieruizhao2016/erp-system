-- ============================================================
-- 生产管理系统关联字段补充脚本
-- 根据《数据库表关联字段检查报告.md》生成
-- 创建时间: 2024年
-- 说明: 添加缺失的生产订单关联字段
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @dbname = DATABASE();

-- ============================================================
-- 1. 销售订单表扩展：production_order_id
-- ============================================================
SET @tablename = 'erp_sale_order';
SET @columnname = 'production_order_id';

-- 检查字段是否存在，不存在则添加
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1 AS "字段已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联生产订单ID'' AFTER `production_status`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查索引是否存在，不存在则添加
SET @indexname = 'idx_production_order';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1 AS "索引已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`', @columnname, '`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ============================================================
-- 2. 收款单表扩展：production_order_id
-- ============================================================
SET @tablename = 'erp_finance_receipt';
SET @columnname = 'production_order_id';

-- 检查字段是否存在，不存在则添加
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1 AS "字段已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联生产订单ID'' AFTER `customer_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查索引是否存在，不存在则添加
SET @indexname = 'idx_production_order';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1 AS "索引已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`', @columnname, '`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证脚本执行结果
-- ============================================================
SELECT 
    'erp_sale_order' AS table_name,
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ production_order_id 字段已存在'
        ELSE '✗ production_order_id 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_sale_order'
    AND COLUMN_NAME = 'production_order_id'

UNION ALL

SELECT 
    'erp_finance_receipt' AS table_name,
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ production_order_id 字段已存在'
        ELSE '✗ production_order_id 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_finance_receipt'
    AND COLUMN_NAME = 'production_order_id';

-- ============================================================
-- 显示所有生产订单关联字段
-- ============================================================
SELECT 
    TABLE_NAME AS '表名',
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_COMMENT AS '字段说明'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND COLUMN_NAME = 'production_order_id'
ORDER BY TABLE_NAME;

