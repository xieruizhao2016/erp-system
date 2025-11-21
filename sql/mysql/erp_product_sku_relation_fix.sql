-- ========================================
-- 修复产品与SKU关系：从一对一改为一对多
-- 创建时间: 2025-11-21
-- ========================================

SET @dbname = DATABASE();

-- ========================================
-- 1. 在SKU表添加product_id字段（关联产品）
-- ========================================
SET @tablename = 'erp_product_sku';
SET @columnname = 'product_id';
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = @columnname) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''产品编号（关联产品）'' AFTER `id`, ADD INDEX `idx_product_id` (`product_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ========================================
-- 2. 迁移现有数据：将产品表的sku_id迁移到SKU表的product_id
-- ========================================
UPDATE erp_product_sku sku
INNER JOIN erp_product p ON p.sku_id = sku.id
SET sku.product_id = p.id
WHERE p.sku_id IS NOT NULL;

-- ========================================
-- 3. 移除产品表的sku_id字段（改为通过SKU表的product_id关联）
-- ========================================
SET @tablename = 'erp_product';
SET @columnname = 'sku_id';
SET @indexname = 'idx_sku';

-- 先删除索引（如果存在）
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND INDEX_NAME = @indexname) > 0,
    CONCAT('ALTER TABLE `', @tablename, '` DROP INDEX `', @indexname, '`'),
    'SELECT 1'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 再删除字段（如果存在）
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = @columnname) > 0,
    CONCAT('ALTER TABLE `', @tablename, '` DROP COLUMN `', @columnname, '`'),
    'SELECT 1'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

