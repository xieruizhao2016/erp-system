-- =====================================================
-- 修复所有ERP表缺失字段的SQL脚本
-- 执行日期: 2025-12-31
-- 说明: 本脚本可以安全地重复执行，已存在的字段会被跳过
-- =====================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =====================================================
-- 1. erp_sale_order 表 - 毛利率统计字段
-- =====================================================

-- 检查并添加 gross_profit_rate 字段
SET @dbname = DATABASE();
SET @tablename = 'erp_sale_order';
SET @columnname = 'gross_profit_rate';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''毛利率（百分比）'' AFTER `total_price`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 material_cost 字段
SET @columnname = 'material_cost';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''原材料成本，单位：元'' AFTER `gross_profit_rate`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 labor_cost 字段
SET @columnname = 'labor_cost';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''员工成本，单位：元'' AFTER `material_cost`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 total_cost 字段
SET @columnname = 'total_cost';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''总成本，单位：元（total_cost = material_cost + labor_cost）'' AFTER `labor_cost`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 2. erp_sale_order_items 表 - 毛利率统计字段
-- =====================================================

SET @tablename = 'erp_sale_order_items';

-- 检查并添加 gross_profit_rate 字段
SET @columnname = 'gross_profit_rate';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''行毛利率（百分比）'' AFTER `tax_price`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 material_cost 字段
SET @columnname = 'material_cost';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''行原材料成本，单位：元'' AFTER `gross_profit_rate`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 检查并添加 labor_cost 字段
SET @columnname = 'labor_cost';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` decimal(20,2) NULL COMMENT ''行员工成本，单位：元'' AFTER `material_cost`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 3. erp_product_bom_item 表 - 工序名称字段
-- =====================================================

SET @tablename = 'erp_product_bom_item';

-- 检查并添加 process_name 字段
SET @columnname = 'process_name';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1',
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(255) NULL COMMENT ''工序名称（冗余字段，用于列表显示，避免关联查询）'' AFTER `process_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 更新已有BOM明细的工序名称
UPDATE `erp_product_bom_item` bom_item
INNER JOIN `erp_process` p ON bom_item.process_id = p.id
SET bom_item.process_name = p.process_name
WHERE bom_item.process_id IS NOT NULL
  AND bom_item.process_name IS NULL;

SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- 脚本执行完成
-- =====================================================
-- 说明：
-- 1. 已检查并添加所有缺失字段
-- 2. 已更新已有数据的冗余字段
-- 3. 后续新增/修改数据时，系统会自动填充这些字段
-- =====================================================

