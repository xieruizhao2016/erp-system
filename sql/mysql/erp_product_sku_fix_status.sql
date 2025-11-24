-- ========================================
-- 修复SKU状态值，使其与CommonStatusEnum一致
-- 创建时间: 2025-11-23
-- 说明: CommonStatusEnum定义：0=开启，1=关闭
--       需要将数据库中的状态值进行转换：1->0（启用改为开启），0->1（禁用改为关闭）
-- ========================================

SET @dbname = DATABASE();

-- ========================================
-- 1. 更新SKU状态值：将1改为0（启用->开启），将0改为1（禁用->关闭）
-- ========================================
UPDATE `erp_product_sku` 
SET `status` = CASE 
    WHEN `status` = 1 THEN 0  -- 原启用(1) -> 新开启(0)
    WHEN `status` = 0 THEN 1  -- 原禁用(0) -> 新关闭(1)
    ELSE `status`              -- 其他值保持不变
END
WHERE `status` IN (0, 1);

-- ========================================
-- 2. 更新表注释，使其与CommonStatusEnum一致
-- ========================================
ALTER TABLE `erp_product_sku` 
MODIFY COLUMN `status` int NOT NULL DEFAULT 0 COMMENT '状态：0-开启，1-关闭（关联CommonStatusEnum）';

-- ========================================
-- 3. 验证更新结果
-- ========================================
SELECT 
    `status`,
    COUNT(*) as count,
    CASE 
        WHEN `status` = 0 THEN '开启'
        WHEN `status` = 1 THEN '关闭'
        ELSE '其他'
    END as status_name
FROM `erp_product_sku`
GROUP BY `status`;

