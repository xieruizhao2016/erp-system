-- =====================================================
-- BOM明细表添加工序名称冗余字段迁移脚本
-- 执行日期: 2025-12-22
-- 说明: 本脚本可以安全地重复执行，已存在的字段会被跳过
-- =====================================================

-- 设置SQL模式，忽略重复字段错误
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='';

-- =====================================================
-- 1. 添加 process_name 字段到 erp_product_bom_item 表
-- =====================================================

SET @dbname = DATABASE();
SET @tablename = 'erp_product_bom_item';
SET @columnname = 'process_name';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (TABLE_SCHEMA = @dbname)
      AND (TABLE_NAME = @tablename)
      AND (COLUMN_NAME = @columnname)
  ) > 0,
  'SELECT 1', -- 字段已存在，不执行任何操作
  CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` varchar(255) NULL COMMENT ''工序名称（冗余字段，用于列表显示，避免关联查询）'' AFTER `process_id`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- =====================================================
-- 2. 更新已有数据的工序名称（如果process_id不为空）
-- =====================================================

-- 更新已有BOM明细的工序名称
UPDATE `erp_product_bom_item` bom_item
INNER JOIN `erp_process` p ON bom_item.process_id = p.id
SET bom_item.process_name = p.process_name
WHERE bom_item.process_id IS NOT NULL
  AND bom_item.process_name IS NULL;

-- 恢复SQL模式
SET SQL_MODE=@OLD_SQL_MODE;

-- =====================================================
-- 脚本执行完成
-- =====================================================
-- 说明：
-- 1. 已添加 process_name 字段到 erp_product_bom_item 表
-- 2. 已更新已有数据的工序名称
-- 3. 后续新增/修改BOM明细时，系统会自动填充工序名称
-- =====================================================

