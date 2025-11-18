-- ========================================
-- 为产品分类表添加产品类型字段
-- 创建时间: 2025-01-XX
-- 说明: 产品类型应该作为产品分类的属性
-- ========================================

-- 产品分类表扩展：production_type
SET @dbname = DATABASE();
SET @tablename = 'erp_product_category';
SET @columnname = 'production_type';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL DEFAULT 1 COMMENT ''产品类型：1-原材料，2-半成品，3-成品''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

