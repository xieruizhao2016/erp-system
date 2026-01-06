-- =====================================================
-- 检查所有ERP系统相关的表
-- 执行日期: 2025-01-03
-- =====================================================

SET @dbname = 'ruoyi-vue-pro';

-- 1. 列出所有ERP表及其注释
SELECT 
    TABLE_NAME AS '表名',
    TABLE_COMMENT AS '表注释',
    TABLE_ROWS AS '估算行数',
    DATA_LENGTH / 1024 / 1024 AS '数据大小(MB)',
    INDEX_LENGTH / 1024 / 1024 AS '索引大小(MB)',
    (DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024 AS '总大小(MB)'
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = @dbname 
  AND TABLE_NAME LIKE 'erp_%'
ORDER BY TABLE_NAME;

-- 2. 统计每个表的字段数量
SELECT 
    TABLE_NAME AS '表名',
    COUNT(*) AS '字段数量'
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname 
  AND TABLE_NAME LIKE 'erp_%'
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

-- 3. 检查是否有表缺少标准字段（creator, updater, create_time, update_time, deleted）
SELECT 
    t.TABLE_NAME AS '表名',
    CASE 
        WHEN c_creator.COLUMN_NAME IS NULL THEN '缺少 creator'
        WHEN c_updater.COLUMN_NAME IS NULL THEN '缺少 updater'
        WHEN c_create_time.COLUMN_NAME IS NULL THEN '缺少 create_time'
        WHEN c_update_time.COLUMN_NAME IS NULL THEN '缺少 update_time'
        WHEN c_deleted.COLUMN_NAME IS NULL THEN '缺少 deleted'
        ELSE '标准字段完整'
    END AS '字段检查'
FROM INFORMATION_SCHEMA.TABLES t
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_creator 
    ON t.TABLE_SCHEMA = c_creator.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_creator.TABLE_NAME 
    AND c_creator.COLUMN_NAME = 'creator'
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_updater 
    ON t.TABLE_SCHEMA = c_updater.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_updater.TABLE_NAME 
    AND c_updater.COLUMN_NAME = 'updater'
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_create_time 
    ON t.TABLE_SCHEMA = c_create_time.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_create_time.TABLE_NAME 
    AND c_create_time.COLUMN_NAME = 'create_time'
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_update_time 
    ON t.TABLE_SCHEMA = c_update_time.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_update_time.TABLE_NAME 
    AND c_update_time.COLUMN_NAME = 'update_time'
LEFT JOIN INFORMATION_SCHEMA.COLUMNS c_deleted 
    ON t.TABLE_SCHEMA = c_deleted.TABLE_SCHEMA 
    AND t.TABLE_NAME = c_deleted.TABLE_NAME 
    AND c_deleted.COLUMN_NAME = 'deleted'
WHERE t.TABLE_SCHEMA = @dbname 
  AND t.TABLE_NAME LIKE 'erp_%'
  AND (
    c_creator.COLUMN_NAME IS NULL 
    OR c_updater.COLUMN_NAME IS NULL 
    OR c_create_time.COLUMN_NAME IS NULL 
    OR c_update_time.COLUMN_NAME IS NULL 
    OR c_deleted.COLUMN_NAME IS NULL
  )
ORDER BY t.TABLE_NAME;

