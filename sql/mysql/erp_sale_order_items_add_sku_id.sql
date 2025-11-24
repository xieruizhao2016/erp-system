-- ========================================
-- 为销售订单项表添加SKU字段
-- 创建时间: 2025-01-XX
-- 说明: 支持产品多SKU管理
-- ========================================

SET @dbname = DATABASE();

-- ========================================
-- 1. 在销售订单项表添加sku_id字段
-- ========================================
SET @tablename = 'erp_sale_order_items';
SET @columnname = 'sku_id';
SET @preparedStatement = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
     WHERE TABLE_SCHEMA = @dbname AND TABLE_NAME = @tablename AND COLUMN_NAME = @columnname) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''SKU编号（关联产品SKU）'' AFTER `product_id`, ADD INDEX `idx_sku_id` (`sku_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

