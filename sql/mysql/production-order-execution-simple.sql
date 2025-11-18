-- ============================================================
-- 生产订单执行流程Demo脚本（简化版）
-- 基于实际数据库表结构
-- 创建时间: 2025-01-XX
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 设置变量
SET @tenant_id = 1;
SET @creator = 'admin';
SET @production_order_no = 'PO-20250101-001';

-- 获取生产订单ID
SET @production_order_id = (SELECT id FROM erp_production_order WHERE no COLLATE utf8mb4_unicode_ci = @production_order_no COLLATE utf8mb4_unicode_ci AND deleted = 0 LIMIT 1);

-- ============================================================
-- 第一步：创建原材料库存
-- ============================================================

-- 删除旧库存（如果存在）
DELETE FROM `erp_stock` 
WHERE `tenant_id` = @tenant_id 
  AND `warehouse_id` = 3001 
  AND `product_id` IN (2010, 2004, 2005, 2006, 2007, 2011, 2008, 2009)
  AND `deleted` = 0;

-- 创建原材料库存
INSERT INTO `erp_stock` (`tenant_id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(@tenant_id, 2010, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2004, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2005, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2006, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2007, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2011, 3001, 1000.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2008, 3001, 50.00, @creator, NOW(), @creator, NOW(), b'0'),
(@tenant_id, 2009, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0');

-- ============================================================
-- 第二步：更新生产订单状态为"进行中"
-- ============================================================

UPDATE `erp_production_order`
SET 
    `status` = 2,  -- 状态：2-进行中
    `actual_start_time` = '2025-01-15 08:00:00',
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @production_order_id;

-- ============================================================
-- 第三步：更新生产订单状态为"已完成"
-- ============================================================

UPDATE `erp_production_order`
SET 
    `status` = 4,  -- 状态：4-已完成
    `completed_quantity` = 100.0000,
    `actual_end_time` = '2025-01-20 18:00:00',
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @production_order_id;

-- ============================================================
-- 第四步：创建成品库存
-- ============================================================

-- 更新成品库存（如果不存在则创建）
UPDATE `erp_stock` 
SET 
    `count` = `count` + 100.00,
    `updater` = @creator,
    `update_time` = NOW()
WHERE `tenant_id` = @tenant_id AND `product_id` = 2001 AND `warehouse_id` = 3003 AND `deleted` = 0;

INSERT INTO `erp_stock` (`tenant_id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT @tenant_id, 2001, 3003, 100.00, @creator, NOW(), @creator, NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `erp_stock` 
    WHERE `tenant_id` = @tenant_id AND `product_id` = 2001 AND `warehouse_id` = 3003 AND `deleted` = 0
);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证查询
-- ============================================================

-- SELECT no, product_name, quantity, completed_quantity, status, actual_start_time, actual_end_time 
-- FROM erp_production_order 
-- WHERE no = 'PO-20250101-001' AND deleted = 0;

