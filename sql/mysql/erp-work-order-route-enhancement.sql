-- ============================================================
-- 工单表增强脚本
-- 添加工艺路线关联字段
-- 创建时间: 2025-01-02
-- 说明: 为了支持版本管理和追溯，工单应该关联具体的工艺路线
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

SET @dbname = DATABASE();
SET @tablename = 'erp_work_order';

-- ============================================================
-- 添加工艺路线关联字段
-- ============================================================
SET @columnname = 'route_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1 AS "字段已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联工艺路线ID（记录使用的工艺路线版本）'' AFTER `product_name`')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 添加工艺路线索引
SET @indexname = 'idx_route';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (INDEX_NAME = @indexname)
    ) > 0,
    'SELECT 1 AS "索引已存在，跳过"',
    CONCAT('ALTER TABLE `', @tablename, '` ADD INDEX `', @indexname, '` (`route_id`) USING BTREE')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证脚本执行结果
-- ============================================================
SELECT 
    'erp_work_order' AS table_name,
    'route_id' AS column_name,
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ route_id 字段已存在'
        ELSE '✗ route_id 字段缺失'
    END AS status
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME = @columnname;

-- 显示表结构
DESC erp_work_order;

