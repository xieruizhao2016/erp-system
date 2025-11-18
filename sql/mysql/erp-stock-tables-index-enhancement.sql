-- ============================================================
-- ERP 库存相关表索引补充脚本
-- 创建时间: 2025-01-XX
-- 说明: 为库存相关表添加必要的索引，提高查询性能
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 为erp_stock_out_item表添加索引
-- ============================================================

-- 检查并添加out_id索引
SET @dbname = DATABASE();
SET @tablename = 'erp_stock_out_item';
SET @indexname = 'idx_out_id';

SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = @dbname
          AND TABLE_NAME = @tablename
          AND INDEX_NAME = @indexname
    ) > 0,
    'SELECT 1',
    CONCAT('CREATE INDEX `', @indexname, '` ON `', @tablename, '` (`out_id`)')
));
PREPARE createIndexIfNotExists FROM @preparedStatement;
EXECUTE createIndexIfNotExists;
DEALLOCATE PREPARE createIndexIfNotExists;

-- 检查并添加product_id索引
SET @indexname = 'idx_product_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = @dbname
          AND TABLE_NAME = @tablename
          AND INDEX_NAME = @indexname
    ) > 0,
    'SELECT 1',
    CONCAT('CREATE INDEX `', @indexname, '` ON `', @tablename, '` (`product_id`)')
));
PREPARE createIndexIfNotExists FROM @preparedStatement;
EXECUTE createIndexIfNotExists;
DEALLOCATE PREPARE createIndexIfNotExists;

-- 检查并添加warehouse_id索引
SET @indexname = 'idx_warehouse_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = @dbname
          AND TABLE_NAME = @tablename
          AND INDEX_NAME = @indexname
    ) > 0,
    'SELECT 1',
    CONCAT('CREATE INDEX `', @indexname, '` ON `', @tablename, '` (`warehouse_id`)')
));
PREPARE createIndexIfNotExists FROM @preparedStatement;
EXECUTE createIndexIfNotExists;
DEALLOCATE PREPARE createIndexIfNotExists;

-- 检查并添加tenant_id和out_id的联合索引
SET @indexname = 'idx_tenant_out';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = @dbname
          AND TABLE_NAME = @tablename
          AND INDEX_NAME = @indexname
    ) > 0,
    'SELECT 1',
    CONCAT('CREATE INDEX `', @indexname, '` ON `', @tablename, '` (`tenant_id`, `out_id`)')
));
PREPARE createIndexIfNotExists FROM @preparedStatement;
EXECUTE createIndexIfNotExists;
DEALLOCATE PREPARE createIndexIfNotExists;

-- ============================================================
-- 为erp_stock表添加索引（如果不存在）
-- ============================================================

SET @tablename = 'erp_stock';

-- 检查并添加product_id和warehouse_id的联合唯一索引
SET @indexname = 'idx_product_warehouse';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = @dbname
          AND TABLE_NAME = @tablename
          AND INDEX_NAME = @indexname
    ) > 0,
    'SELECT 1',
    CONCAT('CREATE UNIQUE INDEX `', @indexname, '` ON `', @tablename, '` (`tenant_id`, `product_id`, `warehouse_id`)')
));
PREPARE createIndexIfNotExists FROM @preparedStatement;
EXECUTE createIndexIfNotExists;
DEALLOCATE PREPARE createIndexIfNotExists;

-- ============================================================
-- 验证索引创建情况
-- ============================================================

SELECT 
    '=== erp_stock_out_item表索引检查 ===' AS '检查项';

SELECT 
    INDEX_NAME AS '索引名',
    COLUMN_NAME AS '字段名',
    NON_UNIQUE AS '非唯一',
    SEQ_IN_INDEX AS '顺序'
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock_out_item'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

SELECT 
    '=== erp_stock表索引检查 ===' AS '检查项';

SELECT 
    INDEX_NAME AS '索引名',
    COLUMN_NAME AS '字段名',
    NON_UNIQUE AS '非唯一',
    SEQ_IN_INDEX AS '顺序'
FROM INFORMATION_SCHEMA.STATISTICS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = 'erp_stock'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 脚本执行完成
-- ============================================================

