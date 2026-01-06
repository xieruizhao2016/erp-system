-- ========================================
-- 设备状态表结构修复
-- 创建时间: 2025-12-31
-- 说明: 为 erp_equipment_status 表添加缺失的 updater 字段（BaseDO 必需字段）
-- ========================================

SET NAMES utf8mb4;

-- 检查并添加 updater 字段
SET @dbname = DATABASE();
SET @tablename = 'erp_equipment_status';
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
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `update_time`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 验证表结构
SELECT
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME IN ('creator', 'updater', 'create_time', 'update_time', 'deleted')
ORDER BY ORDINAL_POSITION;

