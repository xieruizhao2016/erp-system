-- ========================================
-- 工单进度表结构修复
-- 创建时间: 2025-12-09
-- 说明: 为 erp_work_order_progress 表添加缺失的 BaseDO 字段（deleted, creator, updater）
-- ========================================

SET NAMES utf8mb4;

-- 检查并添加 deleted 字段
SET @dbname = DATABASE();
SET @tablename = 'erp_work_order_progress';
SET @columnname = 'deleted';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bit(1) NOT NULL DEFAULT b''0'' COMMENT ''是否删除'' AFTER `update_time`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 creator 字段
SET @columnname = 'creator';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''创建者'' AFTER `deleted`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 updater 字段
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 验证表结构
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME IN ('deleted', 'creator', 'updater')
ORDER BY ORDINAL_POSITION;

