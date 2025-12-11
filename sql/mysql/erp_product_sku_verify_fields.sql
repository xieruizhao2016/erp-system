-- ========================================
-- 验证 erp_product_sku 表字段完整性
-- ========================================

SELECT 
    '数据库字段检查' AS check_type,
    COLUMN_NAME,
    DATA_TYPE,
    IS_NULLABLE,
    COLUMN_DEFAULT,
    COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'erp_product_sku'
ORDER BY ORDINAL_POSITION;

-- 检查必需字段是否存在
SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '缺失'
        ELSE '存在'
    END AS status,
    'specification' AS field_name
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'erp_product_sku'
  AND COLUMN_NAME = 'specification'

UNION ALL

SELECT 
    CASE 
        WHEN COUNT(*) = 0 THEN '缺失'
        ELSE '存在'
    END AS status,
    'attributes' AS field_name
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = DATABASE() 
  AND TABLE_NAME = 'erp_product_sku'
  AND COLUMN_NAME = 'attributes';

