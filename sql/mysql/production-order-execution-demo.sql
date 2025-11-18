-- ============================================================
-- 生产订单执行流程Demo脚本
-- 基于已创建的demo数据，继续执行生产流程
-- 创建时间: 2025-01-XX
-- 说明: 从物料准备到生产完成的完整流程
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 第一步：物料准备阶段
-- ============================================================

-- 设置变量
SET @tenant_id = 1;
SET @creator = 'admin';
SET @production_order_no = 'PO-20250101-001';

-- 获取生产订单ID
SET @production_order_id = (SELECT id FROM erp_production_order WHERE no COLLATE utf8mb4_unicode_ci = @production_order_no COLLATE utf8mb4_unicode_ci AND deleted = 0 LIMIT 1);

-- ============================================================
-- 第二步：创建原材料库存（采购入库）
-- ============================================================

-- 注意：这里假设原材料已经通过采购入库，创建初始库存
-- 根据BOM计算，生产100个智能手环需要：
-- PCB板：104个，主控芯片：103个，显示屏：103个，电池：101个
-- 传感器：103个，焊锡：556克，硅胶材料：16米，表扣：100个

-- 为了确保有足够库存，我们创建比需求更多的库存
-- 原材料仓库ID：3001

-- 2.1 创建原材料库存（如果不存在则创建，存在则更新）
-- 注意：由于erp_stock表可能没有唯一索引，使用先删除再插入的方式
DELETE FROM `erp_stock` 
WHERE `tenant_id` = @tenant_id 
  AND `warehouse_id` = 3001 
  AND `product_id` IN (2010, 2004, 2005, 2006, 2007, 2011, 2008, 2009)
  AND `deleted` = 0;

INSERT INTO `erp_stock` (`tenant_id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
-- PCB板：150个（需求104个）
(@tenant_id, 2010, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 主控芯片：150个（需求103个）
(@tenant_id, 2004, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 显示屏：150个（需求103个）
(@tenant_id, 2005, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 电池：150个（需求101个）
(@tenant_id, 2006, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 传感器：150个（需求103个）
(@tenant_id, 2007, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 焊锡：1000克（需求556克）
(@tenant_id, 2011, 3001, 1000.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 硅胶材料：50米（需求16米）
(@tenant_id, 2008, 3001, 50.00, @creator, NOW(), @creator, NOW(), b'0'),
-- 表扣：150个（需求100个）
(@tenant_id, 2009, 3001, 150.00, @creator, NOW(), @creator, NOW(), b'0');

-- ============================================================
-- 第三步：原材料出库（生产领料）
-- ============================================================

-- 3.1 创建原材料出库单
-- 业务类型：20-其它出库（生产领料）
INSERT INTO `erp_stock_out` (
    `tenant_id`, `no`, `out_time`, `total_count`, `total_price`, 
    `status`, `production_order_id`, `remark`, 
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 
    'OUT-20250115-001',  -- 出库单号
    '2025-01-15 08:00:00',  -- 出库时间
    0.00,  -- 合计数量（后面计算）
    0.00,  -- 合计金额（后面计算）
    0,  -- 状态：0-待审核（假设）
    @production_order_id,  -- 关联生产订单
    '生产订单PO-20250101-001原材料领料出库', 
    @creator, 
    NOW(), 
    @creator, 
    NOW(), 
    b'0'
);

SET @stock_out_id = LAST_INSERT_ID();

-- 3.2 创建出库单明细
-- 根据BOM计算的实际需求数量
INSERT INTO `erp_stock_out_item` (
    `tenant_id`, `product_id`, `product_unit_id`, `out_id`, `warehouse_id`,
    `product_price`, `count`, `total_price`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES 
-- PCB板：104个
(@tenant_id, 2010, 103, @stock_out_id, 3001, 12.00, 104.00, 1248.00, 'PCB板-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 主控芯片：103个
(@tenant_id, 2004, 103, @stock_out_id, 3001, 25.00, 103.00, 2575.00, '主控芯片-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 显示屏：103个
(@tenant_id, 2005, 103, @stock_out_id, 3001, 35.00, 103.00, 3605.00, '显示屏-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 电池：101个
(@tenant_id, 2006, 103, @stock_out_id, 3001, 15.00, 101.00, 1515.00, '电池-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 传感器：103个
(@tenant_id, 2007, 103, @stock_out_id, 3001, 20.00, 103.00, 2060.00, '传感器-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 焊锡：556克
(@tenant_id, 2011, 105, @stock_out_id, 3001, 0.05, 556.00, 27.80, '焊锡-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 硅胶材料：16米
(@tenant_id, 2008, 104, @stock_out_id, 3001, 5.00, 16.00, 80.00, '硅胶材料-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 表扣：100个
(@tenant_id, 2009, 103, @stock_out_id, 3001, 3.00, 100.00, 300.00, '表扣-生产领料', @creator, NOW(), @creator, NOW(), b'0');

-- 3.3 更新出库单合计
UPDATE `erp_stock_out` 
SET 
    `total_count` = (SELECT SUM(`count`) FROM `erp_stock_out_item` WHERE `out_id` = @stock_out_id AND `deleted` = 0),
    `total_price` = (SELECT SUM(`total_price`) FROM `erp_stock_out_item` WHERE `out_id` = @stock_out_id AND `deleted` = 0),
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @stock_out_id;

-- 3.4 更新库存数量（减少）
UPDATE `erp_stock` s
INNER JOIN `erp_stock_out_item` item ON s.product_id = item.product_id AND s.warehouse_id = item.warehouse_id
SET 
    s.count = s.count - item.count,
    s.updater = @creator,
    s.update_time = NOW()
WHERE item.out_id = @stock_out_id 
  AND item.deleted = 0 
  AND s.deleted = 0;

-- 3.5 创建库存记录（出库记录）
-- 业务类型：20-其它出库
INSERT INTO `erp_stock_record` (
    `tenant_id`, `product_id`, `warehouse_id`, `count`, `total_count`,
    `biz_type`, `biz_id`, `biz_item_id`, `biz_no`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
SELECT 
    @tenant_id,
    item.product_id,
    item.warehouse_id,
    -item.count,  -- 负数表示出库
    s.count,  -- 出库后的总库存
    20,  -- 业务类型：20-其它出库
    @stock_out_id,
    item.id,
    'OUT-20250115-001',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
FROM `erp_stock_out_item` item
INNER JOIN `erp_stock` s ON item.product_id = s.product_id AND item.warehouse_id = s.warehouse_id
WHERE item.out_id = @stock_out_id AND item.deleted = 0 AND s.deleted = 0;

-- ============================================================
-- 第四步：更新生产订单状态为"进行中"
-- ============================================================

-- 状态：2-进行中
-- 记录实际开始时间
UPDATE `erp_production_order`
SET 
    `status` = 2,  -- 状态：2-进行中
    `actual_start_time` = '2025-01-15 08:00:00',  -- 实际开始时间
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @production_order_id;

-- ============================================================
-- 第五步：半成品生产完成，半成品入库
-- ============================================================

-- 半成品仓库ID：3002
-- 生产数量：手环主板101个，手环表带100个

-- 5.1 手环主板入库
INSERT INTO `erp_stock_in` (
    `tenant_id`, `no`, `in_time`, `total_count`, `total_price`,
    `status`, `production_order_id`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'IN-20250116-001',  -- 入库单号
    '2025-01-16 14:00:00',  -- 入库时间（假设手环主板生产完成）
    101.00,  -- 合计数量
    0.00,  -- 合计金额（后面计算）
    0,  -- 状态：0-待审核
    @production_order_id,  -- 关联生产订单
    '生产订单PO-20250101-001手环主板半成品入库',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
);

SET @stock_in_board_id = LAST_INSERT_ID();

-- 5.1.1 手环主板入库明细
INSERT INTO `erp_stock_in_item` (
    `tenant_id`, `product_id`, `product_unit_id`, `in_id`, `warehouse_id`,
    `product_price`, `count`, `total_price`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2002, 101, @stock_in_board_id, 3002,
    150.00, 101.00, 15150.00, '手环主板-生产入库',
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 5.1.2 更新入库单合计
UPDATE `erp_stock_in`
SET 
    `total_price` = (SELECT SUM(`total_price`) FROM `erp_stock_in_item` WHERE `in_id` = @stock_in_board_id AND `deleted` = 0),
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @stock_in_board_id;

-- 5.1.3 更新手环主板库存（增加）
-- 如果库存不存在则创建，存在则更新
UPDATE `erp_stock` 
SET 
    `count` = `count` + 101.00,
    `updater` = @creator,
    `update_time` = NOW()
WHERE `tenant_id` = @tenant_id AND `product_id` = 2002 AND `warehouse_id` = 3002 AND `deleted` = 0;

-- 如果更新影响行数为0，说明库存不存在，需要创建
INSERT INTO `erp_stock` (`tenant_id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT @tenant_id, 2002, 3002, 101.00, @creator, NOW(), @creator, NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `erp_stock` 
    WHERE `tenant_id` = @tenant_id AND `product_id` = 2002 AND `warehouse_id` = 3002 AND `deleted` = 0
);

-- 5.1.4 创建库存记录（入库记录）
-- 业务类型：10-其它入库
INSERT INTO `erp_stock_record` (
    `tenant_id`, `product_id`, `warehouse_id`, `count`, `total_count`,
    `biz_type`, `biz_id`, `biz_item_id`, `biz_no`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
SELECT 
    @tenant_id,
    item.product_id,
    item.warehouse_id,
    item.count,  -- 正数表示入库
    s.count,  -- 入库后的总库存
    10,  -- 业务类型：10-其它入库
    @stock_in_board_id,
    item.id,
    'IN-20250116-001',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
FROM `erp_stock_in_item` item
INNER JOIN `erp_stock` s ON item.product_id = s.product_id AND item.warehouse_id = s.warehouse_id
WHERE item.in_id = @stock_in_board_id AND item.deleted = 0 AND s.deleted = 0;

-- 5.2 手环表带入库
INSERT INTO `erp_stock_in` (
    `tenant_id`, `no`, `in_time`, `total_count`, `total_price`,
    `status`, `production_order_id`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'IN-20250116-002',  -- 入库单号
    '2025-01-16 16:00:00',  -- 入库时间（假设手环表带生产完成）
    100.00,  -- 合计数量
    0.00,  -- 合计金额（后面计算）
    0,  -- 状态：0-待审核
    @production_order_id,  -- 关联生产订单
    '生产订单PO-20250101-001手环表带半成品入库',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
);

SET @stock_in_band_id = LAST_INSERT_ID();

-- 5.2.1 手环表带入库明细
INSERT INTO `erp_stock_in_item` (
    `tenant_id`, `product_id`, `product_unit_id`, `in_id`, `warehouse_id`,
    `product_price`, `count`, `total_price`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2003, 101, @stock_in_band_id, 3002,
    20.00, 100.00, 2000.00, '手环表带-生产入库',
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 5.2.2 更新入库单合计
UPDATE `erp_stock_in`
SET 
    `total_price` = (SELECT SUM(`total_price`) FROM `erp_stock_in_item` WHERE `in_id` = @stock_in_band_id AND `deleted` = 0),
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @stock_in_band_id;

-- 5.2.3 更新手环表带库存（增加）
UPDATE `erp_stock` 
SET 
    `count` = `count` + 100.00,
    `updater` = @creator,
    `update_time` = NOW()
WHERE `tenant_id` = @tenant_id AND `product_id` = 2003 AND `warehouse_id` = 3002 AND `deleted` = 0;

INSERT INTO `erp_stock` (`tenant_id`, `product_id`, `warehouse_id`, `count`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT @tenant_id, 2003, 3002, 100.00, @creator, NOW(), @creator, NOW(), b'0'
WHERE NOT EXISTS (
    SELECT 1 FROM `erp_stock` 
    WHERE `tenant_id` = @tenant_id AND `product_id` = 2003 AND `warehouse_id` = 3002 AND `deleted` = 0
);

-- 5.2.4 创建库存记录（入库记录）
INSERT INTO `erp_stock_record` (
    `tenant_id`, `product_id`, `warehouse_id`, `count`, `total_count`,
    `biz_type`, `biz_id`, `biz_item_id`, `biz_no`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
SELECT 
    @tenant_id,
    item.product_id,
    item.warehouse_id,
    item.count,  -- 正数表示入库
    s.count,  -- 入库后的总库存
    10,  -- 业务类型：10-其它入库
    @stock_in_band_id,
    item.id,
    'IN-20250116-002',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
FROM `erp_stock_in_item` item
INNER JOIN `erp_stock` s ON item.product_id = s.product_id AND item.warehouse_id = s.warehouse_id
WHERE item.in_id = @stock_in_band_id AND item.deleted = 0 AND s.deleted = 0;

-- ============================================================
-- 第六步：成品生产完成，成品入库
-- ============================================================

-- 注意：在成品生产前，需要从半成品仓库领用半成品
-- 6.0 半成品出库（从半成品仓库领用）
-- 6.0.1 创建半成品出库单
INSERT INTO `erp_stock_out` (
    `tenant_id`, `no`, `out_time`, `total_count`, `total_price`, 
    `status`, `production_order_id`, `remark`, 
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 
    'OUT-20250118-001',  -- 出库单号
    '2025-01-18 08:00:00',  -- 出库时间
    0.00,  -- 合计数量（后面计算）
    0.00,  -- 合计金额（后面计算）
    0,  -- 状态：0-待审核
    @production_order_id,  -- 关联生产订单
    '生产订单PO-20250101-001半成品领料出库（用于成品组装）', 
    @creator, 
    NOW(), 
    @creator, 
    NOW(), 
    b'0'
);

SET @stock_out_semi_id = LAST_INSERT_ID();

-- 6.0.2 创建半成品出库单明细
INSERT INTO `erp_stock_out_item` (
    `tenant_id`, `product_id`, `product_unit_id`, `out_id`, `warehouse_id`,
    `product_price`, `count`, `total_price`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES 
-- 手环主板：101个（从半成品仓库出库）
(@tenant_id, 2002, 101, @stock_out_semi_id, 3002, 150.00, 101.00, 15150.00, '手环主板-生产领料', @creator, NOW(), @creator, NOW(), b'0'),
-- 手环表带：100个（从半成品仓库出库）
(@tenant_id, 2003, 101, @stock_out_semi_id, 3002, 20.00, 100.00, 2000.00, '手环表带-生产领料', @creator, NOW(), @creator, NOW(), b'0');

-- 6.0.3 更新出库单合计
UPDATE `erp_stock_out` 
SET 
    `total_count` = (SELECT SUM(`count`) FROM `erp_stock_out_item` WHERE `out_id` = @stock_out_semi_id AND `deleted` = 0),
    `total_price` = (SELECT SUM(`total_price`) FROM `erp_stock_out_item` WHERE `out_id` = @stock_out_semi_id AND `deleted` = 0),
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @stock_out_semi_id;

-- 6.0.4 更新半成品库存数量（减少）
UPDATE `erp_stock` s
INNER JOIN `erp_stock_out_item` item ON s.product_id = item.product_id AND s.warehouse_id = item.warehouse_id
SET 
    s.count = s.count - item.count,
    s.updater = @creator,
    s.update_time = NOW()
WHERE item.out_id = @stock_out_semi_id 
  AND item.deleted = 0 
  AND s.deleted = 0;

-- 6.0.5 创建库存记录（半成品出库记录）
INSERT INTO `erp_stock_record` (
    `tenant_id`, `product_id`, `warehouse_id`, `count`, `total_count`,
    `biz_type`, `biz_id`, `biz_item_id`, `biz_no`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
SELECT 
    @tenant_id,
    item.product_id,
    item.warehouse_id,
    -item.count,  -- 负数表示出库
    s.count,  -- 出库后的总库存
    20,  -- 业务类型：20-其它出库
    @stock_out_semi_id,
    item.id,
    'OUT-20250118-001',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
FROM `erp_stock_out_item` item
INNER JOIN `erp_stock` s ON item.product_id = s.product_id AND item.warehouse_id = s.warehouse_id
WHERE item.out_id = @stock_out_semi_id AND item.deleted = 0 AND s.deleted = 0;

-- 成品仓库ID：3003
-- 生产数量：100个智能手环

-- 6.1 创建成品入库单
INSERT INTO `erp_stock_in` (
    `tenant_id`, `no`, `in_time`, `total_count`, `total_price`,
    `status`, `production_order_id`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'IN-20250120-001',  -- 入库单号
    '2025-01-20 18:00:00',  -- 入库时间（假设成品生产完成）
    100.00,  -- 合计数量
    0.00,  -- 合计金额（后面计算）
    0,  -- 状态：0-待审核
    @production_order_id,  -- 关联生产订单
    '生产订单PO-20250101-001智能手环成品入库',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
);

SET @stock_in_finished_id = LAST_INSERT_ID();

-- 6.2 创建成品入库明细
INSERT INTO `erp_stock_in_item` (
    `tenant_id`, `product_id`, `product_unit_id`, `in_id`, `warehouse_id`,
    `product_price`, `count`, `total_price`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2001, 101, @stock_in_finished_id, 3003,
    299.00, 100.00, 29900.00, '智能手环-生产入库',
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 6.3 更新入库单合计
UPDATE `erp_stock_in`
SET 
    `total_price` = (SELECT SUM(`total_price`) FROM `erp_stock_in_item` WHERE `in_id` = @stock_in_finished_id AND `deleted` = 0),
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @stock_in_finished_id;

-- 6.4 更新成品库存（增加）
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

-- 6.5 创建库存记录（入库记录）
-- 业务类型：10-其它入库
INSERT INTO `erp_stock_record` (
    `tenant_id`, `product_id`, `warehouse_id`, `count`, `total_count`,
    `biz_type`, `biz_id`, `biz_item_id`, `biz_no`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
SELECT 
    @tenant_id,
    item.product_id,
    item.warehouse_id,
    item.count,  -- 正数表示入库
    s.count,  -- 入库后的总库存
    10,  -- 业务类型：10-其它入库
    @stock_in_finished_id,
    item.id,
    'IN-20250120-001',
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
FROM `erp_stock_in_item` item
INNER JOIN `erp_stock` s ON item.product_id = s.product_id AND item.warehouse_id = s.warehouse_id
WHERE item.in_id = @stock_in_finished_id AND item.deleted = 0 AND s.deleted = 0;

-- ============================================================
-- 第七步：更新生产订单状态为"已完成"
-- ============================================================

-- 状态：4-已完成
-- 更新已完成数量和实际完成时间
UPDATE `erp_production_order`
SET 
    `status` = 4,  -- 状态：4-已完成
    `completed_quantity` = 100.0000,  -- 已完成数量
    `actual_end_time` = '2025-01-20 18:00:00',  -- 实际完成时间
    `updater` = @creator,
    `update_time` = NOW()
WHERE `id` = @production_order_id;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 执行结果验证查询
-- ============================================================

-- 查询生产订单状态
-- SELECT no, product_name, quantity, completed_quantity, status, actual_start_time, actual_end_time 
-- FROM erp_production_order 
-- WHERE no = 'PO-20250101-001' AND deleted = 0;

-- 查询原材料库存
-- SELECT p.name as product_name, s.count, s.warehouse_id, w.name as warehouse_name
-- FROM erp_stock s
-- LEFT JOIN erp_product p ON s.product_id = p.id
-- LEFT JOIN erp_warehouse w ON s.warehouse_id = w.id
-- WHERE s.warehouse_id = 3001 AND s.deleted = 0
-- ORDER BY p.id;

-- 查询半成品库存
-- SELECT p.name as product_name, s.count, s.warehouse_id, w.name as warehouse_name
-- FROM erp_stock s
-- LEFT JOIN erp_product p ON s.product_id = p.id
-- LEFT JOIN erp_warehouse w ON s.warehouse_id = w.id
-- WHERE s.warehouse_id = 3002 AND s.deleted = 0
-- ORDER BY p.id;

-- 查询成品库存
-- SELECT p.name as product_name, s.count, s.warehouse_id, w.name as warehouse_name
-- FROM erp_stock s
-- LEFT JOIN erp_product p ON s.product_id = p.id
-- LEFT JOIN erp_warehouse w ON s.warehouse_id = w.id
-- WHERE s.warehouse_id = 3003 AND s.deleted = 0
-- ORDER BY p.id;

-- 查询出库单列表
-- SELECT no, out_time, total_count, total_price, status, production_order_id
-- FROM erp_stock_out
-- WHERE production_order_id = @production_order_id AND deleted = 0
-- ORDER BY out_time;

-- 查询入库单列表
-- SELECT no, in_time, total_count, total_price, status, production_order_id
-- FROM erp_stock_in
-- WHERE production_order_id = @production_order_id AND deleted = 0
-- ORDER BY in_time;

-- 查询库存记录（出入库明细）
-- SELECT 
--     p.name as product_name,
--     w.name as warehouse_name,
--     sr.count,
--     sr.total_count,
--     CASE sr.biz_type
--         WHEN 10 THEN '其它入库'
--         WHEN 20 THEN '其它出库'
--         ELSE '未知'
--     END as biz_type_name,
--     sr.biz_no,
--     sr.create_time
-- FROM erp_stock_record sr
-- LEFT JOIN erp_product p ON sr.product_id = p.id
-- LEFT JOIN erp_warehouse w ON sr.warehouse_id = w.id
-- WHERE sr.biz_no IN ('OUT-20250115-001', 'OUT-20250118-001', 'IN-20250116-001', 'IN-20250116-002', 'IN-20250120-001')
--   AND sr.deleted = 0
-- ORDER BY sr.create_time;

