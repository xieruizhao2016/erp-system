-- ========================================
-- 销售订单明细表添加 sku_id 字段
-- 创建时间: 2025-12-09
-- 说明: 为 erp_sale_order_items 表添加 sku_id 字段，用于关联产品SKU
-- ========================================

SET NAMES utf8mb4;

SET @dbname = DATABASE();
SET @tablename = 'erp_sale_order_items';
SET @columnname = 'sku_id';

-- 检查并添加 sku_id 字段
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''SKU编号（关联产品SKU）'' AFTER `product_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 验证字段是否添加成功
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME = @columnname;

