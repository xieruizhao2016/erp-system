-- ========================================
-- 为 erp_product_sku 表添加缺失的字段
-- specification: 规格参数（JSON格式）
-- attributes: 属性信息（JSON格式）
-- ========================================

SET @dbname = DATABASE();
SET @tablename = 'erp_product_sku';

-- 添加 specification 字段
SET @columnname = 'specification';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(2000) NULL DEFAULT NULL COMMENT ''规格参数（JSON格式）'' AFTER `image_url`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 attributes 字段
SET @columnname = 'attributes';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(2000) NULL DEFAULT NULL COMMENT ''属性信息（JSON格式，如：颜色、尺寸等）'' AFTER `specification`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SELECT '字段添加完成！' AS result;

