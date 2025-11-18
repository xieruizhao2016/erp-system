-- ============================================================
-- 生产订单表增强脚本
-- 添加BOM和工艺路线的直接关联字段
-- 创建时间: 2024年
-- 说明: 为了支持版本管理和追溯，生产订单应该直接关联具体的BOM和工艺路线
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @dbname = DATABASE();
SET @tablename = 'erp_production_order';

-- ============================================================
-- 1. 添加BOM关联字段
-- ============================================================
SET @columnname = 'bom_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1 AS "字段已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联BOM ID（记录使用的BOM版本）'' AFTER `product_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加BOM索引
SET @indexname = 'idx_bom';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1 AS "索引已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`bom_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ============================================================
-- 2. 添加工艺路线关联字段
-- ============================================================
SET @columnname = 'route_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1 AS "字段已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联工艺路线ID（记录使用的工艺路线版本）'' AFTER `bom_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加工艺路线索引
SET @indexname = 'idx_route';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1 AS "索引已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`route_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证脚本执行结果
-- ============================================================
SELECT 
    'erp_production_order' AS table_name,
    'bom_id' AS column_name,
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ bom_id 字段已存在'
        ELSE '✗ bom_id 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_production_order'
    AND COLUMN_NAME = 'bom_id'

UNION ALL

SELECT 
    'erp_production_order' AS table_name,
    'route_id' AS column_name,
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ route_id 字段已存在'
        ELSE '✗ route_id 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_production_order'
    AND COLUMN_NAME = 'route_id';

-- ============================================================
-- 显示生产订单表的所有关联字段
-- ============================================================
SELECT 
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_COMMENT AS '字段说明'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
    AND TABLE_NAME = 'erp_production_order'
    AND COLUMN_NAME IN ('customer_id', 'product_id', 'bom_id', 'route_id', 'source_id', 'source_type')
ORDER BY 
    CASE COLUMN_NAME
        WHEN 'customer_id' THEN 1
        WHEN 'product_id' THEN 2
        WHEN 'bom_id' THEN 3
        WHEN 'route_id' THEN 4
        WHEN 'source_id' THEN 5
        WHEN 'source_type' THEN 6
    END;

