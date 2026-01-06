-- =====================================================
-- 修复ERP表缺失的标准字段（creator, updater等）
-- 执行日期: 2025-01-03
-- 说明: 本脚本可以安全地重复执行，已存在的字段会被跳过
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @dbname = DATABASE();

-- =====================================================
-- 1. 修复 erp_cost_variance 表 - 添加 updater 字段
-- =====================================================
SET @tablename = 'erp_cost_variance';
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 2. 修复 erp_production_kpi 表 - 添加 updater 字段
-- =====================================================
SET @tablename = 'erp_production_kpi';
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 3. 修复 erp_production_report 表 - 添加 updater 字段
-- =====================================================
SET @tablename = 'erp_production_report';
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 4. 修复 erp_production_schedule 表 - 添加 creator 字段
-- =====================================================
SET @tablename = 'erp_production_schedule';
SET @columnname = 'creator';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''创建者'' AFTER `update_time`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 同时添加 updater 字段（如果不存在）
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 5. 修复 erp_quality_inspection_item 表 - 添加 creator 字段
-- =====================================================
SET @tablename = 'erp_quality_inspection_item';
SET @columnname = 'creator';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''创建者'' AFTER `update_time`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 同时添加 updater 字段（如果不存在）
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 6. 修复 erp_quality_item 表 - 添加 creator 字段
-- =====================================================
SET @tablename = 'erp_quality_item';
SET @columnname = 'creator';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''创建者'' AFTER `update_time`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 同时添加 updater 字段（如果不存在）
SET @columnname = 'updater';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(64) NULL DEFAULT '''' COMMENT ''更新者'' AFTER `creator`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 验证脚本执行结果
-- =====================================================
SELECT 
    t.TABLE_NAME AS '表名',
    CASE 
        WHEN c_creator.COLUMN_NAME IS NULL THEN '缺少 creator'
        WHEN c_updater.COLUMN_NAME IS NULL THEN '缺少 updater'
        ELSE '标准字段完整'
    END AS '字段检查状态'
FROM INFORMATION_SCHEMA.TABLES t
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_creator 
    ON t.TABLE_SCHEMA = c_creator.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_creator.TABLE_NAME 
    AND c_creator.COLUMN_NAME = 'creator'
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_updater 
    ON t.TABLE_SCHEMA = c_updater.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_updater.TABLE_NAME 
    AND c_updater.COLUMN_NAME = 'updater'
WHERE t.TABLE_SCHEMA = @dbname 
  AND t.TABLE_NAME LIKE 'erp_%'
  AND (
    c_creator.COLUMN_NAME IS NULL 
    OR c_updater.COLUMN_NAME IS NULL
  )
ORDER BY t.TABLE_NAME;

