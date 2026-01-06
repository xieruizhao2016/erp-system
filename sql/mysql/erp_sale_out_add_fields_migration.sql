-- =====================================================
-- 销售出库表字段补丁脚本
-- 修复销售出库表缺失的字段
-- 执行日期: 2025-01-03
-- 说明: 本脚本可以安全地重复执行，已存在的字段会被跳过
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @dbname = DATABASE();

-- =====================================================
-- 修复 erp_sale_out 表（销售出库）
-- =====================================================

SET @tablename = 'erp_sale_out';

-- 添加 status 字段（出库状态）
SET @columnname = 'status';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL COMMENT ''出库状态'' AFTER `no`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 order_no 字段（销售订单号，冗余字段）
SET @columnname = 'order_no';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(50) NULL DEFAULT '''' COMMENT ''销售订单号（冗余字段）'' AFTER `order_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 total_price 字段（最终合计价格）
SET @columnname = 'total_price';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''最终合计价格，单位：元'' AFTER `other_price`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 receipt_price 字段（已收款金额）
SET @columnname = 'receipt_price';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL DEFAULT 0.00 COMMENT ''已收款金额，单位：元'' AFTER `total_price`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加 discount_price 字段（优惠金额）
SET @columnname = 'discount_price';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1 AS "字段已存在，跳过"',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''优惠金额，单位：元'' AFTER `total_price`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 验证脚本执行结果
-- =====================================================
SELECT
    'erp_sale_out' AS table_name,
    COLUMN_NAME AS column_name,
    CASE
        WHEN COUNT(*) > 0 THEN '✓ 字段已存在'
        ELSE '✗ 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname 
  AND TABLE_NAME = @tablename 
  AND COLUMN_NAME IN ('status', 'order_no', 'total_price', 'receipt_price', 'discount_price')
GROUP BY COLUMN_NAME
ORDER BY COLUMN_NAME;

