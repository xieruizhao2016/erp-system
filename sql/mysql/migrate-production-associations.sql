-- ============================================================
-- 生产订单关联关系数据迁移脚本
-- 用于建立历史数据的关联关系
-- 创建时间: 2024年
-- 说明: 此脚本用于迁移已有数据，建立生产订单与其他单据的关联关系
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 注意事项
-- ============================================================
-- 1. 执行此脚本前，请先备份数据库
-- 2. 此脚本基于业务逻辑推断关联关系，可能需要根据实际情况调整
-- 3. 建议在测试环境先验证脚本的正确性
-- 4. 执行后需要人工审核关联关系的准确性

-- ============================================================
-- 1. 迁移销售订单与生产订单的关联关系
-- ============================================================
-- 根据生产订单的 source_type=2 和 source_id 反向更新销售订单的 production_order_id
-- 注意：如果一个销售订单对应多个生产订单，此脚本只关联第一个，需要手动处理多对多关系

UPDATE erp_sale_order so
INNER JOIN erp_production_order po 
    ON po.source_type = 2 
    AND po.source_id = so.id
    AND po.deleted = 0
    AND so.deleted = 0
SET so.production_order_id = po.id
WHERE so.production_order_id IS NULL
    AND EXISTS (
        SELECT 1 FROM erp_production_order 
        WHERE source_type = 2 
        AND source_id = so.id 
        AND deleted = 0
    );

-- 显示迁移结果
SELECT 
    '销售订单关联迁移' AS '迁移项',
    COUNT(*) AS '已关联数量'
FROM erp_sale_order
WHERE production_order_id IS NOT NULL;

-- ============================================================
-- 2. 迁移收款单与生产订单的关联关系
-- ============================================================
-- 通过收款单明细关联的销售订单，再关联到生产订单
-- 注意：此迁移基于收款单明细的 biz_type=21（销售出库）关联销售订单

UPDATE erp_finance_receipt fr
INNER JOIN erp_finance_receipt_item fri 
    ON fri.receipt_id = fr.id
    AND fri.biz_type = 21  -- 销售出库
    AND fri.deleted = 0
INNER JOIN erp_sale_out so 
    ON so.id = fri.biz_id
    AND so.deleted = 0
INNER JOIN erp_sale_order sale_order 
    ON sale_order.id = so.sale_order_id
    AND sale_order.deleted = 0
INNER JOIN erp_production_order po 
    ON po.source_type = 2 
    AND po.source_id = sale_order.id
    AND po.deleted = 0
SET fr.production_order_id = po.id
WHERE fr.production_order_id IS NULL
    AND fr.deleted = 0;

-- 显示迁移结果
SELECT 
    '收款单关联迁移' AS '迁移项',
    COUNT(*) AS '已关联数量'
FROM erp_finance_receipt
WHERE production_order_id IS NOT NULL;

-- ============================================================
-- 3. 迁移生产订单的BOM关联
-- ============================================================
-- 根据产品ID，查找该产品生效状态的BOM，关联到生产订单
-- 如果有多个生效的BOM，选择最新的一个

UPDATE erp_production_order po
INNER JOIN (
    SELECT 
        product_id,
        MAX(id) AS latest_bom_id
    FROM erp_product_bom
    WHERE status = 2  -- 生效状态
        AND deleted = 0
        AND (expire_date IS NULL OR expire_date > NOW())
    GROUP BY product_id
) bom ON bom.product_id = po.product_id
SET po.bom_id = bom.latest_bom_id
WHERE po.bom_id IS NULL
    AND po.deleted = 0;

-- 显示迁移结果
SELECT 
    '生产订单BOM关联迁移' AS '迁移项',
    COUNT(*) AS '已关联数量'
FROM erp_production_order
WHERE bom_id IS NOT NULL;

-- ============================================================
-- 4. 迁移生产订单的工艺路线关联
-- ============================================================
-- 根据产品ID，查找该产品生效状态的工艺路线，关联到生产订单
-- 如果有多个生效的工艺路线，选择最新的一个

UPDATE erp_production_order po
INNER JOIN (
    SELECT 
        product_id,
        MAX(id) AS latest_route_id
    FROM erp_process_route
    WHERE status = 2  -- 生效状态
        AND deleted = 0
    GROUP BY product_id
) route ON route.product_id = po.product_id
SET po.route_id = route.latest_route_id
WHERE po.route_id IS NULL
    AND po.deleted = 0;

-- 显示迁移结果
SELECT 
    '生产订单工艺路线关联迁移' AS '迁移项',
    COUNT(*) AS '已关联数量'
FROM erp_production_order
WHERE route_id IS NOT NULL;

-- ============================================================
-- 5. 迁移结果汇总
-- ============================================================
SELECT 
    '=== 数据迁移结果汇总 ===' AS '迁移报告';

SELECT 
    '销售订单关联生产订单' AS '迁移项',
    COUNT(*) AS '已关联数量',
    (SELECT COUNT(*) FROM erp_sale_order WHERE deleted = 0) AS '总销售订单数',
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM erp_sale_order WHERE deleted = 0), 2) AS '关联率(%)'
FROM erp_sale_order
WHERE production_order_id IS NOT NULL
    AND deleted = 0

UNION ALL

SELECT 
    '收款单关联生产订单' AS '迁移项',
    COUNT(*) AS '已关联数量',
    (SELECT COUNT(*) FROM erp_finance_receipt WHERE deleted = 0) AS '总收款单数',
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM erp_finance_receipt WHERE deleted = 0), 2) AS '关联率(%)'
FROM erp_finance_receipt
WHERE production_order_id IS NOT NULL
    AND deleted = 0

UNION ALL

SELECT 
    '生产订单关联BOM' AS '迁移项',
    COUNT(*) AS '已关联数量',
    (SELECT COUNT(*) FROM erp_production_order WHERE deleted = 0) AS '总生产订单数',
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM erp_production_order WHERE deleted = 0), 2) AS '关联率(%)'
FROM erp_production_order
WHERE bom_id IS NOT NULL
    AND deleted = 0

UNION ALL

SELECT 
    '生产订单关联工艺路线' AS '迁移项',
    COUNT(*) AS '已关联数量',
    (SELECT COUNT(*) FROM erp_production_order WHERE deleted = 0) AS '总生产订单数',
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM erp_production_order WHERE deleted = 0), 2) AS '关联率(%)'
FROM erp_production_order
WHERE route_id IS NOT NULL
    AND deleted = 0;

-- ============================================================
-- 6. 检查未关联的数据
-- ============================================================
SELECT 
    '=== 未关联的数据检查 ===' AS '检查项';

SELECT 
    '未关联生产订单的销售订单' AS '检查项',
    COUNT(*) AS '数量'
FROM erp_sale_order
WHERE production_order_id IS NULL
    AND deleted = 0
    AND EXISTS (
        SELECT 1 FROM erp_production_order 
        WHERE source_type = 2 
        AND source_id = erp_sale_order.id 
        AND deleted = 0
    )

UNION ALL

SELECT 
    '未关联BOM的生产订单' AS '检查项',
    COUNT(*) AS '数量'
FROM erp_production_order
WHERE bom_id IS NULL
    AND deleted = 0
    AND EXISTS (
        SELECT 1 FROM erp_product_bom 
        WHERE product_id = erp_production_order.product_id 
        AND status = 2 
        AND deleted = 0
    )

UNION ALL

SELECT 
    '未关联工艺路线的生产订单' AS '检查项',
    COUNT(*) AS '数量'
FROM erp_production_order
WHERE route_id IS NULL
    AND deleted = 0
    AND EXISTS (
        SELECT 1 FROM erp_process_route 
        WHERE product_id = erp_production_order.product_id 
        AND status = 2 
        AND deleted = 0
    );

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 迁移完成提示
-- ============================================================
SELECT 
    '数据迁移完成！' AS '提示',
    '请检查上述迁移结果，确认关联关系的正确性。' AS '说明',
    '如有异常，请根据实际情况调整迁移逻辑。' AS '建议';

