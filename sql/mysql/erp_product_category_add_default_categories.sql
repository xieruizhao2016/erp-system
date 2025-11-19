-- ========================================
-- 产品分类：添加默认分类字段和初始化默认分类
-- 创建时间: 2025-01-XX
-- 说明: 设置4个默认分类（原材料、半成品、成品、普通产品），这些分类不可删除
-- ========================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 添加 is_default 字段（如果不存在）
SET @dbname = DATABASE();
SET @tablename = 'erp_product_category';
SET @columnname = 'is_default';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bit(1) NOT NULL DEFAULT b''0'' COMMENT ''是否默认分类：0-否，1-是（默认分类不可删除）''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 2. 初始化4个默认分类（如果不存在）
-- 注意：这里使用固定的ID，确保每个租户都能正确创建
-- 原材料分类
INSERT IGNORE INTO `erp_product_category` 
(`id`, `tenant_id`, `parent_id`, `name`, `code`, `sort`, `status`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
(1001, 1, 0, '原材料', 'RAW_MATERIAL', 1, 0, b'1', 'system', NOW(), 'system', NOW(), b'0');

-- 半成品分类
INSERT IGNORE INTO `erp_product_category` 
(`id`, `tenant_id`, `parent_id`, `name`, `code`, `sort`, `status`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
(1002, 1, 0, '半成品', 'SEMI_FINISHED', 2, 0, b'1', 'system', NOW(), 'system', NOW(), b'0');

-- 成品分类
INSERT IGNORE INTO `erp_product_category` 
(`id`, `tenant_id`, `parent_id`, `name`, `code`, `sort`, `status`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
(1003, 1, 0, '成品', 'FINISHED', 3, 0, b'1', 'system', NOW(), 'system', NOW(), b'0');

-- 普通产品分类
INSERT IGNORE INTO `erp_product_category` 
(`id`, `tenant_id`, `parent_id`, `name`, `code`, `sort`, `status`, `is_default`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
(1004, 1, 0, '普通产品', 'NORMAL', 4, 0, b'1', 'system', NOW(), 'system', NOW(), b'0');

SET FOREIGN_KEY_CHECKS = 1;

