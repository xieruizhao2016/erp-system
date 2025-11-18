-- ============================================================
-- 生产管理系统完整案例Demo
-- 案例：生产"智能手环"成品
-- 创建时间: 2025-01-XX
-- 说明: 包含从基础数据到生产完成的完整流程数据
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- 第一步：基础数据准备
-- ============================================================

-- ----------------------------
-- 1.1 产品数据准备
-- ----------------------------

-- 假设租户ID为1，实际使用时请根据实际情况修改
SET @tenant_id = 1;
SET @creator = 'admin';

-- 1.1.1 创建产品分类（如果不存在）
INSERT IGNORE INTO `erp_product_category` (`id`, `tenant_id`, `name`, `parent_id`, `sort`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(1001, @tenant_id, '电子产品', 0, 1, 0, @creator, NOW(), @creator, NOW(), b'0'),
(1002, @tenant_id, '原材料', 0, 2, 0, @creator, NOW(), @creator, NOW(), b'0'),
(1003, @tenant_id, '半成品', 0, 3, 0, @creator, NOW(), @creator, NOW(), b'0');

-- 1.1.2 创建产品单位（如果不存在）
INSERT IGNORE INTO `erp_product_unit` (`id`, `tenant_id`, `name`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(101, @tenant_id, '个', 0, @creator, NOW(), @creator, NOW(), b'0'),
(102, @tenant_id, '套', 0, @creator, NOW(), @creator, NOW(), b'0'),
(103, @tenant_id, '片', 0, @creator, NOW(), @creator, NOW(), b'0'),
(104, @tenant_id, '米', 0, @creator, NOW(), @creator, NOW(), b'0'),
(105, @tenant_id, '克', 0, @creator, NOW(), @creator, NOW(), b'0');

-- 1.1.3 创建产品（成品：智能手环）
INSERT INTO `erp_product` (`id`, `tenant_id`, `status`, `category_id`, `unit_id`, `name`, `bar_code`, `standard`, `remark`, `purchase_price`, `sale_price`, `min_price`, `production_type`, `is_bom`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
-- 成品：智能手环
(2001, @tenant_id, 0, 1001, 101, '智能手环', 'PROD-2001', '标准版', '智能手环成品，支持心率监测、运动追踪等功能', 0.00, 299.00, 250.00, 3, b'1', @creator, NOW(), @creator, NOW(), b'0'),

-- 半成品
(2002, @tenant_id, 0, 1003, 101, '手环主板', 'PROD-2002', 'V1.0', '智能手环主板半成品', 0.00, 150.00, 120.00, 2, b'1', @creator, NOW(), @creator, NOW(), b'0'),
(2003, @tenant_id, 0, 1003, 101, '手环表带', 'PROD-2003', '标准', '硅胶表带半成品', 0.00, 20.00, 15.00, 2, b'0', @creator, NOW(), @creator, NOW(), b'0'),

-- 原材料
(2004, @tenant_id, 0, 1002, 103, '主控芯片', 'PROD-2004', 'MCU-001', '主控芯片原材料', 25.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2005, @tenant_id, 0, 1002, 103, '显示屏', 'PROD-2005', 'LCD-001', '1.4寸OLED显示屏', 35.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2006, @tenant_id, 0, 1002, 103, '电池', 'PROD-2006', 'BAT-001', '200mAh锂电池', 15.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2007, @tenant_id, 0, 1002, 103, '传感器', 'PROD-2007', 'SEN-001', '心率传感器', 20.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2008, @tenant_id, 0, 1002, 104, '硅胶材料', 'PROD-2008', 'SIL-001', '医用级硅胶材料（米）', 5.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2009, @tenant_id, 0, 1002, 103, '表扣', 'PROD-2009', 'BUC-001', '不锈钢表扣', 3.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2010, @tenant_id, 0, 1002, 103, 'PCB板', 'PROD-2010', 'PCB-001', '4层PCB电路板', 12.00, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0'),
(2011, @tenant_id, 0, 1002, 105, '焊锡', 'PROD-2011', 'SOLD-001', '无铅焊锡（克）', 0.05, 0.00, 0.00, 1, b'0', @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 1.2 仓库数据准备
-- ----------------------------

-- 创建仓库（如果不存在）
INSERT IGNORE INTO `erp_warehouse` (`id`, `tenant_id`, `name`, `address`, `status`, `warehouse_type`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(3001, @tenant_id, '原材料仓', 'A区1号仓库', 0, 1, @creator, NOW(), @creator, NOW(), b'0'),
(3002, @tenant_id, '半成品仓', 'B区2号仓库', 0, 2, @creator, NOW(), @creator, NOW(), b'0'),
(3003, @tenant_id, '成品仓', 'C区3号仓库', 0, 3, @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 1.3 设备和工作中心数据准备
-- ----------------------------

-- 创建设备（如果表存在）
-- 注意：如果erp_equipment表不存在，请先创建
INSERT IGNORE INTO `erp_equipment` (`id`, `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `work_center_id`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(4001, @tenant_id, 'EQ-001', 'SMT贴片机', 'SMT设备', 5001, 1, @creator, NOW(), @creator, NOW(), b'0'),
(4002, @tenant_id, 'EQ-002', '回流焊炉', '焊接设备', 5001, 1, @creator, NOW(), @creator, NOW(), b'0'),
(4003, @tenant_id, 'EQ-003', '注塑机', '注塑设备', 5002, 1, @creator, NOW(), @creator, NOW(), b'0'),
(4004, @tenant_id, 'EQ-004', '组装线', '组装设备', 5003, 1, @creator, NOW(), @creator, NOW(), b'0'),
(4005, @tenant_id, 'EQ-005', '测试设备', '测试设备', 5003, 1, @creator, NOW(), @creator, NOW(), b'0');

-- 创建工作中心（如果表存在）
-- 注意：如果erp_work_center表不存在，请先创建或使用其他表
-- 这里假设工作中心ID为5001-5003

-- ============================================================
-- 第二步：BOM配置
-- ============================================================

-- ----------------------------
-- 2.1 创建手环主板BOM（半成品）
-- ----------------------------

-- 2.1.1 手环主板BOM主表
INSERT INTO `erp_product_bom` (`id`, `tenant_id`, `product_id`, `bom_no`, `bom_name`, `version`, `effective_date`, `bom_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(6001, @tenant_id, 2002, 'BOM-2002-001', '手环主板BOM', '1.0', NOW(), 1, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 2.1.2 手环主板BOM明细
INSERT INTO `erp_product_bom_item` (`id`, `tenant_id`, `bom_id`, `parent_product_id`, `child_product_id`, `child_product_name`, `child_product_spec`, `unit_id`, `quantity`, `loss_rate`, `is_key_material`, `is_alternative`, `position`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(7001, @tenant_id, 6001, 2002, 2010, 'PCB板', 'PCB-001', 103, 1.0000, 0.0200, b'1', b'0', 1, '主控板', @creator, NOW(), @creator, NOW(), b'0'),
(7002, @tenant_id, 6001, 2002, 2004, '主控芯片', 'MCU-001', 103, 1.0000, 0.0100, b'1', b'0', 2, '核心芯片', @creator, NOW(), @creator, NOW(), b'0'),
(7003, @tenant_id, 6001, 2002, 2005, '显示屏', 'LCD-001', 103, 1.0000, 0.0100, b'1', b'0', 3, '显示模块', @creator, NOW(), @creator, NOW(), b'0'),
(7004, @tenant_id, 6001, 2002, 2006, '电池', 'BAT-001', 103, 1.0000, 0.0000, b'1', b'0', 4, '电源', @creator, NOW(), @creator, NOW(), b'0'),
(7005, @tenant_id, 6001, 2002, 2007, '传感器', 'SEN-001', 103, 1.0000, 0.0100, b'1', b'0', 5, '心率传感器', @creator, NOW(), @creator, NOW(), b'0'),
(7006, @tenant_id, 6001, 2002, 2011, '焊锡', 'SOLD-001', 105, 5.0000, 0.1000, b'0', b'0', 6, '焊接材料', @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 2.2 创建手环表带BOM（半成品）
-- ----------------------------

-- 2.2.1 手环表带BOM主表
INSERT INTO `erp_product_bom` (`id`, `tenant_id`, `product_id`, `bom_no`, `bom_name`, `version`, `effective_date`, `bom_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(6002, @tenant_id, 2003, 'BOM-2003-001', '手环表带BOM', '1.0', NOW(), 1, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 2.2.2 手环表带BOM明细
INSERT INTO `erp_product_bom_item` (`id`, `tenant_id`, `bom_id`, `parent_product_id`, `child_product_id`, `child_product_name`, `child_product_spec`, `unit_id`, `quantity`, `loss_rate`, `is_key_material`, `is_alternative`, `position`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(7007, @tenant_id, 6002, 2003, 2008, '硅胶材料', 'SIL-001', 104, 0.1500, 0.0500, b'0', b'0', 1, '表带材料', @creator, NOW(), @creator, NOW(), b'0'),
(7008, @tenant_id, 6002, 2003, 2009, '表扣', 'BUC-001', 103, 1.0000, 0.0000, b'0', b'0', 2, '固定扣', @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 2.3 创建智能手环成品BOM
-- ----------------------------

-- 2.3.1 智能手环BOM主表
INSERT INTO `erp_product_bom` (`id`, `tenant_id`, `product_id`, `bom_no`, `bom_name`, `version`, `effective_date`, `bom_type`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(6003, @tenant_id, 2001, 'BOM-2001-001', '智能手环BOM', '1.0', NOW(), 1, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 2.3.2 智能手环BOM明细
INSERT INTO `erp_product_bom_item` (`id`, `tenant_id`, `bom_id`, `parent_product_id`, `child_product_id`, `child_product_name`, `child_product_spec`, `unit_id`, `quantity`, `loss_rate`, `is_key_material`, `is_alternative`, `position`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(7009, @tenant_id, 6003, 2001, 2002, '手环主板', 'V1.0', 101, 1.0000, 0.0100, b'1', b'0', 1, '核心组件', @creator, NOW(), @creator, NOW(), b'0'),
(7010, @tenant_id, 6003, 2001, 2003, '手环表带', '标准', 101, 1.0000, 0.0000, b'0', b'0', 2, '外观组件', @creator, NOW(), @creator, NOW(), b'0');

-- ============================================================
-- 第三步：工艺路线配置
-- ============================================================

-- ----------------------------
-- 3.1 创建手环主板工艺路线（半成品）
-- ----------------------------

-- 3.1.1 手环主板工艺路线主表
INSERT INTO `erp_process_route` (`id`, `tenant_id`, `route_no`, `route_name`, `product_id`, `version`, `standard_cycle_time`, `standard_labor_cost`, `standard_overhead_cost`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(8001, @tenant_id, 'ROUTE-2002-001', '手环主板工艺路线', 2002, '1.0', 120, 50.00, 30.00, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 3.1.2 手环主板工艺路线明细
INSERT INTO `erp_process_route_item` (`id`, `tenant_id`, `route_id`, `process_id`, `sequence`, `operation_name`, `standard_time`, `setup_time`, `worker_count`, `equipment_id`, `work_center_id`, `labor_rate`, `overhead_rate`, `is_bottleneck`, `quality_check_required`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
-- 工序1：SMT贴片
(9001, @tenant_id, 8001, 10001, 1, 'SMT贴片', 30, 10, 2, 4001, 5001, 25.00, 15.00, b'0', b'1', '贴装主控芯片和传感器', @creator, NOW(), @creator, NOW(), b'0'),
-- 工序2：回流焊
(9002, @tenant_id, 8001, 10002, 2, '回流焊', 20, 5, 1, 4002, 5001, 15.00, 10.00, b'0', b'0', '焊接电子元件', @creator, NOW(), @creator, NOW(), b'0'),
-- 工序3：测试
(9003, @tenant_id, 8001, 10003, 3, '功能测试', 15, 5, 1, 4005, 5003, 10.00, 5.00, b'0', b'1', '测试主板功能', @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 3.2 创建手环表带工艺路线（半成品）
-- ----------------------------

-- 3.2.1 手环表带工艺路线主表
INSERT INTO `erp_process_route` (`id`, `tenant_id`, `route_no`, `route_name`, `product_id`, `version`, `standard_cycle_time`, `standard_labor_cost`, `standard_overhead_cost`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(8002, @tenant_id, 'ROUTE-2003-001', '手环表带工艺路线', 2003, '1.0', 30, 10.00, 5.00, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 3.2.2 手环表带工艺路线明细
INSERT INTO `erp_process_route_item` (`id`, `tenant_id`, `route_id`, `process_id`, `sequence`, `operation_name`, `standard_time`, `setup_time`, `worker_count`, `equipment_id`, `work_center_id`, `labor_rate`, `overhead_rate`, `is_bottleneck`, `quality_check_required`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
-- 工序1：注塑成型
(9004, @tenant_id, 8002, 10004, 1, '注塑成型', 20, 5, 1, 4003, 5002, 8.00, 4.00, b'0', b'0', '注塑表带', @creator, NOW(), @creator, NOW(), b'0'),
-- 工序2：安装表扣
(9005, @tenant_id, 8002, 10005, 2, '安装表扣', 5, 0, 1, NULL, 5002, 2.00, 1.00, b'0', b'0', '手工安装', @creator, NOW(), @creator, NOW(), b'0');

-- ----------------------------
-- 3.3 创建智能手环成品工艺路线
-- ----------------------------

-- 3.3.1 智能手环工艺路线主表
INSERT INTO `erp_process_route` (`id`, `tenant_id`, `route_no`, `route_name`, `product_id`, `version`, `standard_cycle_time`, `standard_labor_cost`, `standard_overhead_cost`, `status`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
(8003, @tenant_id, 'ROUTE-2001-001', '智能手环工艺路线', 2001, '1.0', 60, 30.00, 20.00, 2, @creator, NOW(), @creator, NOW(), b'0');

-- 3.3.2 智能手环工艺路线明细
INSERT INTO `erp_process_route_item` (`id`, `tenant_id`, `route_id`, `process_id`, `sequence`, `operation_name`, `standard_time`, `setup_time`, `worker_count`, `equipment_id`, `work_center_id`, `labor_rate`, `overhead_rate`, `is_bottleneck`, `quality_check_required`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
-- 工序1：组装主板和表带
(9006, @tenant_id, 8003, 10006, 1, '组装', 30, 5, 2, 4004, 5003, 20.00, 12.00, b'0', b'0', '组装主板和表带', @creator, NOW(), @creator, NOW(), b'0'),
-- 工序2：最终测试
(9007, @tenant_id, 8003, 10007, 2, '最终测试', 20, 5, 1, 4005, 5003, 10.00, 8.00, b'0', b'1', '成品功能测试', @creator, NOW(), @creator, NOW(), b'0'),
-- 工序3：包装
(9008, @tenant_id, 8003, 10008, 3, '包装', 5, 0, 1, NULL, 5003, 0.00, 0.00, b'0', b'0', '产品包装', @creator, NOW(), @creator, NOW(), b'0');

-- ============================================================
-- 第四步：库存初始化（原材料入库）
-- ============================================================

-- 注意：这里假设有库存表，实际使用时请根据系统实际情况调整
-- 以下为示例数据，需要根据实际的库存表结构进行调整

-- 假设库存表为 erp_product_stock 或类似表
-- 这里仅作示例，实际使用时需要根据系统实际情况调整

-- ============================================================
-- 第五步：创建生产订单
-- ============================================================

-- ----------------------------
-- 5.1 创建生产订单（生产100个智能手环）
-- ----------------------------

INSERT INTO `erp_production_order` (
    `tenant_id`, `no`, `customer_id`, `product_id`, `product_name`, `product_spec`, 
    `unit_id`, `quantity`, `completed_quantity`, `start_time`, `end_time`, 
    `status`, `priority`, `source_type`, `description`, `remark`, 
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 
    'PO-20250101-001',  -- 生产订单号
    NULL,  -- 客户ID（手动创建，无客户）
    2001,  -- 产品ID：智能手环
    '智能手环',  -- 产品名称
    '标准版',  -- 产品规格
    101,  -- 单位ID：个
    100.0000,  -- 生产数量：100个
    0.0000,  -- 已完成数量：0
    '2025-01-15 08:00:00',  -- 计划开始时间
    '2025-01-20 18:00:00',  -- 计划完成时间
    1,  -- 状态：1-待开始
    2,  -- 优先级：2-高
    1,  -- 来源类型：1-手动创建
    '生产100个智能手环，用于春节促销活动',  -- 生产说明
    '注意质量把控，确保按时交付',  -- 备注
    @creator, 
    NOW(), 
    @creator, 
    NOW(), 
    b'0'
);

-- ============================================================
-- 第六步：生产执行过程（状态流转示例）
-- ============================================================

-- ----------------------------
-- 6.1 物料检查通过，状态变更为"待排产"
-- ----------------------------
-- UPDATE `erp_production_order` 
-- SET `status` = 2, `updater` = @creator, `update_time` = NOW()
-- WHERE `no` = 'PO-20250101-001';

-- ----------------------------
-- 6.2 开始生产，状态变更为"进行中"
-- ----------------------------
-- UPDATE `erp_production_order` 
-- SET `status` = 2, `actual_start_time` = NOW(), `updater` = @creator, `update_time` = NOW()
-- WHERE `no` = 'PO-20250101-001';

-- ----------------------------
-- 6.3 生产完成，状态变更为"已完成"
-- ----------------------------
-- UPDATE `erp_production_order` 
-- SET `status` = 4, `completed_quantity` = 100.0000, `actual_end_time` = NOW(), `updater` = @creator, `update_time` = NOW()
-- WHERE `no` = 'PO-20250101-001';

-- ============================================================
-- 数据查询示例
-- ============================================================

-- 查询生产订单
-- SELECT * FROM `erp_production_order` WHERE `no` = 'PO-20250101-001' AND `deleted` = b'0';

-- 查询BOM明细
-- SELECT 
--     bom.bom_no, bom.bom_name, bom.product_id, p.name as product_name,
--     item.child_product_id, cp.name as child_product_name, 
--     item.quantity, item.loss_rate, item.effective_quantity,
--     item.is_key_material, item.position
-- FROM `erp_product_bom` bom
-- LEFT JOIN `erp_product` p ON bom.product_id = p.id
-- LEFT JOIN `erp_product_bom_item` item ON bom.id = item.bom_id
-- LEFT JOIN `erp_product` cp ON item.child_product_id = cp.id
-- WHERE bom.product_id = 2001 AND bom.deleted = b'0' AND item.deleted = b'0'
-- ORDER BY item.position;

-- 查询工艺路线明细
-- SELECT 
--     route.route_no, route.route_name, route.product_id, p.name as product_name,
--     item.sequence, item.operation_name, item.standard_time, item.setup_time,
--     item.worker_count, item.is_bottleneck, item.quality_check_required
-- FROM `erp_process_route` route
-- LEFT JOIN `erp_product` p ON route.product_id = p.id
-- LEFT JOIN `erp_process_route_item` item ON route.id = item.route_id
-- WHERE route.product_id = 2001 AND route.deleted = b'0' AND item.deleted = b'0'
-- ORDER BY item.sequence;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- Demo数据说明
-- ============================================================
-- 1. 本Demo创建了一个完整的生产流程案例：生产100个智能手环
-- 2. 包含3层BOM结构：成品 -> 半成品 -> 原材料
-- 3. 包含完整的工艺路线配置
-- 4. 生产订单状态为"待开始"，可根据实际业务流程更新状态
-- 5. 所有数据使用租户ID=1，创建者=admin，实际使用时请根据实际情况修改
-- 6. 部分表（如设备表、工作中心表、库存表）需要根据系统实际情况调整
-- ============================================================

