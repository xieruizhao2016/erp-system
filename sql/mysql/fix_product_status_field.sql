-- 修复产品表和产品分类表缺少 status 字段的问题
-- 执行时间: 2025-01-XX
-- 说明: 为 erp_product 和 erp_product_category 表添加 status 字段

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 修复 erp_product 表：添加 status 字段
-- ----------------------------
-- 检查字段是否存在，如果不存在则添加
SET @dbname = DATABASE();
SET @tablename = 'erp_product';
SET @columnname = 'status';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL COMMENT ''产品状态'' AFTER `tenant_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 为现有数据设置默认值（启用状态 = 0）
UPDATE `erp_product` SET `status` = 0 WHERE `status` IS NULL;

-- ----------------------------
-- 修复 erp_product_category 表：添加 status 字段
-- ----------------------------
SET @tablename = 'erp_product_category';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL COMMENT ''开启状态'' AFTER `tenant_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 为现有数据设置默认值（启用状态 = 0）
UPDATE `erp_product_category` SET `status` = 0 WHERE `status` IS NULL;

SET FOREIGN_KEY_CHECKS = 1;

