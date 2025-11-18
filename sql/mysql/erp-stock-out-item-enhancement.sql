-- ============================================================
-- ERP 出库单明细表字段补充脚本
-- 创建时间: 2025-01-XX
-- 说明: 为erp_stock_out_item表添加缺失的字段
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 为erp_stock_out_item表添加缺失字段
-- ============================================================

SET @dbname = DATABASE();

-- 1. 添加product_id字段
SET @tablename = 'erp_stock_out_item';
SET @columnname = 'product_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''产品编号'' AFTER `tenant_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 2. 添加product_unit_id字段
SET @columnname = 'product_unit_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''产品单位编号'' AFTER `product_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 3. 添加out_id字段
SET @columnname = 'out_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''其它出库编号'' AFTER `product_unit_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 4. 添加warehouse_id字段
SET @columnname = 'warehouse_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''仓库编号'' AFTER `out_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ============================================================
-- 验证字段是否添加成功
-- ============================================================

SELECT 
    '=== erp_stock_out_item表字段检查 ===' AS '检查项';

SELECT 
    COLUMN_NAME AS '字段名',
    DATA_TYPE AS '数据类型',
    IS_NULLABLE AS '允许为空',
    COLUMN_DEFAULT AS '默认值',
    COLUMN_COMMENT AS '注释'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
ORDER BY ORDINAL_POSITION;

-- 检查必需字段是否存在
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ product_id 字段已存在'
        ELSE '✗ product_id 字段缺失'
    END AS 'product_id字段检查'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
  AND COLUMN_NAME = 'product_id';

SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ product_unit_id 字段已存在'
        ELSE '✗ product_unit_id 字段缺失'
    END AS 'product_unit_id字段检查'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
  AND COLUMN_NAME = 'product_unit_id';

SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ out_id 字段已存在'
        ELSE '✗ out_id 字段缺失'
    END AS 'out_id字段检查'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
  AND COLUMN_NAME = 'out_id';

SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ warehouse_id 字段已存在'
        ELSE '✗ warehouse_id 字段缺失'
    END AS 'warehouse_id字段检查'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
  AND COLUMN_NAME = 'warehouse_id';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 脚本执行完成
-- ============================================================

