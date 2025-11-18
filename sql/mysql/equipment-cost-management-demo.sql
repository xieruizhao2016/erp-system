-- ============================================================
-- 设备管理和成本管理Demo脚本
-- 创建时间: 2025-01-XX
-- 说明: 基于已完成的生产订单，创建设备管理和成本管理相关数据
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 设置变量
SET @tenant_id = 1;
SET @creator = 'admin';
SET @production_order_no = 'PO-20250101-001';

-- 获取相关ID
SET @production_order_id = (SELECT id FROM erp_production_order WHERE no COLLATE utf8mb4_unicode_ci = @production_order_no COLLATE utf8mb4_unicode_ci AND deleted = 0 LIMIT 1);

-- ============================================================
-- 第一部分：设备管理
-- ============================================================

-- 1.1 创建设备台账（补充更多设备）
-- 基于已有的设备（4001-4005），创建更多设备数据

-- 设备6：SMT贴片机（如果不存在则创建）
INSERT IGNORE INTO `erp_equipment` (
    `id`, `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `model`,
    `manufacturer`, `serial_number`, `purchase_date`, `purchase_price`, `service_life`,
    `work_center_id`, `location`, `capacity`, `efficiency_rate`, `status`,
    `responsible_person`, `specification`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    4006, @tenant_id,
    'EQ-4006',  -- 设备编号
    'SMT贴片机',  -- 设备名称
    '生产设备',  -- 设备类型
    'SMT-3000',  -- 设备型号
    '富士康',  -- 制造商
    'SMT-2023-001',  -- 序列号
    '2023-06-01',  -- 购置日期
    500000.00,  -- 购置价格：50万元
    10,  -- 设计寿命：10年
    5001,  -- 工作中心ID：SMT车间
    'SMT车间-01号位',  -- 设备位置
    16.00,  -- 产能：16小时/天
    95.00,  -- 效率系数：95%
    1,  -- 状态：1-正常
    '张师傅',  -- 责任人
    '精度：±0.05mm，速度：30000点/小时，支持0402-50×50mm元件',  -- 技术规格
    'SMT贴片设备，用于PCB板贴片',
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 获取设备ID
SET @equipment_6_id = 4006;

-- 设备7：回流焊炉（如果不存在则创建）
INSERT IGNORE INTO `erp_equipment` (
    `id`,
    `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `model`,
    `manufacturer`, `serial_number`, `purchase_date`, `purchase_price`, `service_life`,
    `work_center_id`, `location`, `capacity`, `efficiency_rate`, `status`,
    `responsible_person`, `specification`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    4007, @tenant_id,
    'EQ-4007',
    '回流焊炉',
    '生产设备',
    'REFLOW-2000',
    '劲拓股份',
    'RT-2023-002',
    '2023-06-15',
    300000.00,  -- 购置价格：30万元
    8,  -- 设计寿命：8年
    5001,  -- 工作中心ID：SMT车间
    'SMT车间-02号位',
    16.00,  -- 产能：16小时/天
    98.00,  -- 效率系数：98%
    1,  -- 状态：1-正常
    '李师傅',
    '温度范围：室温-300℃，温控精度：±2℃，传送速度：0.1-1.5m/min',  -- 技术规格
    '回流焊设备，用于PCB板焊接',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @equipment_7_id = 4007;

-- 设备8：注塑机（如果不存在则创建）
INSERT IGNORE INTO `erp_equipment` (
    `id`,
    `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `model`,
    `manufacturer`, `serial_number`, `purchase_date`, `purchase_price`, `service_life`,
    `work_center_id`, `location`, `capacity`, `efficiency_rate`, `status`,
    `responsible_person`, `specification`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    4008, @tenant_id,
    'EQ-4008',
    '注塑机',
    '生产设备',
    'INJ-500',
    '海天塑机',
    'HT-2023-003',
    '2023-07-01',
    200000.00,  -- 购置价格：20万元
    10,  -- 设计寿命：10年
    5002,  -- 工作中心ID：注塑车间
    '注塑车间-01号位',
    12.00,  -- 产能：12小时/天
    92.00,  -- 效率系数：92%
    1,  -- 状态：1-正常
    '王师傅',
    '锁模力：500T，射胶量：500g，射胶速度：200mm/s',  -- 技术规格
    '注塑设备，用于表带注塑成型',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @equipment_8_id = 4008;

-- 设备9：老化测试箱（如果不存在则创建）
INSERT IGNORE INTO `erp_equipment` (
    `id`,
    `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `model`,
    `manufacturer`, `serial_number`, `purchase_date`, `purchase_price`, `service_life`,
    `work_center_id`, `location`, `capacity`, `efficiency_rate`, `status`,
    `responsible_person`, `specification`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    4009, @tenant_id,
    'EQ-4009',
    '老化测试箱',
    '测试设备',
    'AGING-1000',
    '环试仪器',
    'HS-2023-004',
    '2023-08-01',
    150000.00,  -- 购置价格：15万元
    8,  -- 设计寿命：8年
    5003,  -- 工作中心ID：测试中心
    '测试中心-02号位',
    24.00,  -- 产能：24小时/天（可连续运行）
    100.00,  -- 效率系数：100%
    1,  -- 状态：1-正常
    '赵师傅',
    '温度范围：-40℃~+150℃，湿度范围：20%~98%RH，容量：1000L',  -- 技术规格
    '老化测试设备，用于产品可靠性测试',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @equipment_9_id = 4009;

-- 设备10：包装机（如果不存在则创建）
INSERT IGNORE INTO `erp_equipment` (
    `id`,
    `tenant_id`, `equipment_no`, `equipment_name`, `equipment_type`, `model`,
    `manufacturer`, `serial_number`, `purchase_date`, `purchase_price`, `service_life`,
    `work_center_id`, `location`, `capacity`, `efficiency_rate`, `status`,
    `responsible_person`, `specification`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    4010, @tenant_id,
    'EQ-4010',
    '自动包装机',
    '包装设备',
    'PACK-300',
    '包装机械',
    'PK-2023-005',
    '2023-09-01',
    80000.00,  -- 购置价格：8万元
    8,  -- 设计寿命：8年
    5003,  -- 工作中心ID：包装中心
    '包装中心-01号位',
    8.00,  -- 产能：8小时/天
    90.00,  -- 效率系数：90%
    1,  -- 状态：1-正常
    '钱师傅',
    '包装速度：300件/小时，包装尺寸：50×50×50mm-300×300×300mm',  -- 技术规格
    '自动包装设备，用于产品包装',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @equipment_10_id = 4010;

-- 1.2 创建设备状态记录
-- 为关键设备创建状态记录，展示设备运行历史

-- 获取工单ID（用于关联设备状态）
SET @work_order_1_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250115-001' AND deleted = 0 LIMIT 1);
SET @work_order_2_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250116-001' AND deleted = 0 LIMIT 1);
SET @work_order_3_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250117-001' AND deleted = 0 LIMIT 1);

-- 设备4（组装线）的状态记录
INSERT INTO `erp_equipment_status` (
    `tenant_id`, `equipment_id`, `status`, `status_start_time`, `status_end_time`,
    `duration`, `work_order_id`, `operator_id`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES 
-- 运行状态（生产期间）
(@tenant_id, 4004, 1, '2025-01-15 08:00:00', '2025-01-15 18:00:00', 600, @work_order_1_id, NULL, '组装工序生产', @creator, NOW(), NOW(), b'0'),
-- 待机状态（生产间隙）
(@tenant_id, 4004, 2, '2025-01-15 18:00:00', '2025-01-16 08:00:00', 840, NULL, NULL, '生产间隙待机', @creator, NOW(), NOW(), b'0');

-- 设备5（测试设备）的状态记录
INSERT INTO `erp_equipment_status` (
    `tenant_id`, `equipment_id`, `status`, `status_start_time`, `status_end_time`,
    `duration`, `work_order_id`, `operator_id`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES 
-- 运行状态（测试期间）
(@tenant_id, 4005, 1, '2025-01-16 08:00:00', '2025-01-16 18:00:00', 600, @work_order_2_id, NULL, '最终测试工序', @creator, NOW(), NOW(), b'0'),
-- 待机状态
(@tenant_id, 4005, 2, '2025-01-16 18:00:00', '2025-01-17 08:00:00', 840, NULL, NULL, '测试间隙待机', @creator, NOW(), NOW(), b'0');

-- 设备6（SMT贴片机）的状态记录
INSERT INTO `erp_equipment_status` (
    `tenant_id`, `equipment_id`, `status`, `status_start_time`, `status_end_time`,
    `duration`, `work_order_id`, `operator_id`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES 
-- 运行状态
(@tenant_id, @equipment_6_id, 1, '2025-01-10 08:00:00', '2025-01-10 18:00:00', 600, NULL, NULL, 'PCB板贴片生产', @creator, NOW(), NOW(), b'0'),
-- 维修状态（模拟一次维修）
(@tenant_id, @equipment_6_id, 4, '2025-01-11 08:00:00', '2025-01-11 14:00:00', 360, NULL, NULL, '定期维护保养', @creator, NOW(), NOW(), b'0'),
-- 运行状态（维修后）
(@tenant_id, @equipment_6_id, 1, '2025-01-11 14:00:00', '2025-01-11 18:00:00', 240, NULL, NULL, '维修后恢复生产', @creator, NOW(), NOW(), b'0');

-- ============================================================
-- 第二部分：成本管理
-- ============================================================

-- 2.1 创建标准成本
-- 为智能手环及其半成品创建标准成本

-- 2.1.1 智能手环（成品）标准成本
INSERT INTO `erp_cost_standard` (
    `tenant_id`, `product_id`, `version`, `effective_date`, `expire_date`,
    `material_cost`, `labor_cost`, `overhead_cost`, `total_cost`,
    `cost_currency`, `calculation_date`, `bom_version`, `route_version`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2001, '1.0', '2025-01-01 00:00:00', NULL,
    180.00,  -- 材料成本：180元（基于BOM计算）
    30.00,  -- 人工成本：30元（3道工序，每道10元）
    20.00,  -- 制造费用：20元（设备折旧、水电等）
    230.00,  -- 总成本：230元
    'CNY',  -- 成本币种：人民币
    '2025-01-01 00:00:00',  -- 计算日期
    '1.0',  -- BOM版本
    '1.0',  -- 工艺版本
    2,  -- 状态：2-生效
    '智能手环标准成本，基于BOM和工艺路线计算',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_standard_2001_id = LAST_INSERT_ID();

-- 2.1.2 手环主板（半成品）标准成本
INSERT INTO `erp_cost_standard` (
    `tenant_id`, `product_id`, `version`, `effective_date`, `expire_date`,
    `material_cost`, `labor_cost`, `overhead_cost`, `total_cost`,
    `cost_currency`, `calculation_date`, `bom_version`, `route_version`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2002, '1.0', '2025-01-01 00:00:00', NULL,
    90.00,  -- 材料成本：90元（PCB板、芯片、显示屏、电池、传感器、焊锡）
    8.00,  -- 人工成本：8元（SMT贴片、焊接工序）
    5.00,  -- 制造费用：5元
    103.00,  -- 总成本：103元
    'CNY',
    '2025-01-01 00:00:00',
    '1.0',
    '1.0',
    2,  -- 状态：2-生效
    '手环主板标准成本',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_standard_2002_id = LAST_INSERT_ID();

-- 2.1.3 手环表带（半成品）标准成本
INSERT INTO `erp_cost_standard` (
    `tenant_id`, `product_id`, `version`, `effective_date`, `expire_date`,
    `material_cost`, `labor_cost`, `overhead_cost`, `total_cost`,
    `cost_currency`, `calculation_date`, `bom_version`, `route_version`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, 2003, '1.0', '2025-01-01 00:00:00', NULL,
    6.50,  -- 材料成本：6.5元（硅胶材料、表扣）
    2.00,  -- 人工成本：2元（注塑工序）
    1.50,  -- 制造费用：1.5元
    10.00,  -- 总成本：10元
    'CNY',
    '2025-01-01 00:00:00',
    '1.0',
    '1.0',
    2,  -- 状态：2-生效
    '手环表带标准成本',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_standard_2003_id = LAST_INSERT_ID();

-- 2.2 创建实际成本
-- 基于已完成的生产订单，创建实际成本记录

-- 获取工单ID
SET @work_order_1_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250115-001' AND deleted = 0 LIMIT 1);
SET @work_order_2_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250116-001' AND deleted = 0 LIMIT 1);
SET @work_order_3_id = (SELECT id FROM erp_work_order WHERE work_order_no = 'WO-20250117-001' AND deleted = 0 LIMIT 1);

-- 2.2.1 智能手环（成品）实际成本
INSERT INTO `erp_cost_actual` (
    `tenant_id`, `cost_no`, `work_order_id`, `production_order_id`, `product_id`,
    `production_quantity`, `material_cost`, `material_cost_adjust`, `labor_cost`, `labor_cost_adjust`,
    `overhead_cost`, `overhead_cost_adjust`, `total_cost`, `unit_cost`,
    `cost_currency`, `cost_period`, `calculation_date`, `last_adjust_date`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'COST-20250120-001',  -- 成本单号
    NULL,  -- 工单ID（成品成本汇总所有工单）
    @production_order_id, 2001,
    100.0000,  -- 生产数量：100个
    18200.00,  -- 材料成本：18200元（实际领料成本，略高于标准）
    0.00,  -- 材料成本调整：0
    3100.00,  -- 人工成本：3100元（实际工时成本）
    0.00,  -- 人工成本调整：0
    2100.00,  -- 制造费用：2100元（设备折旧、水电等）
    0.00,  -- 制造费用调整：0
    23400.00,  -- 总成本：23400元
    234.00,  -- 单位成本：234元/个
    'CNY',
    '2025-01',  -- 成本期间：2025年1月
    '2025-01-20 18:00:00',  -- 计算日期
    NULL,  -- 最后调整日期
    3,  -- 状态：3-已确认
    '智能手环实际成本，基于生产订单PO-20250101-001计算',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_actual_2001_id = LAST_INSERT_ID();

-- 2.2.2 手环主板（半成品）实际成本
INSERT INTO `erp_cost_actual` (
    `tenant_id`, `cost_no`, `work_order_id`, `production_order_id`, `product_id`,
    `production_quantity`, `material_cost`, `material_cost_adjust`, `labor_cost`, `labor_cost_adjust`,
    `overhead_cost`, `overhead_cost_adjust`, `total_cost`, `unit_cost`,
    `cost_currency`, `cost_period`, `calculation_date`, `last_adjust_date`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'COST-20250116-001',
    NULL,  -- 工单ID（半成品可能有多个工单）
    @production_order_id, 2002,
    101.0000,  -- 生产数量：101个（考虑损耗）
    9191.00,  -- 材料成本：9191元（101个×91元，略高于标准）
    0.00,
    808.00,  -- 人工成本：808元（101个×8元）
    0.00,
    505.00,  -- 制造费用：505元（101个×5元）
    0.00,
    10504.00,  -- 总成本：10504元
    104.00,  -- 单位成本：104元/个
    'CNY',
    '2025-01',
    '2025-01-16 14:00:00',
    NULL,
    3,  -- 状态：3-已确认
    '手环主板实际成本',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_actual_2002_id = LAST_INSERT_ID();

-- 2.2.3 手环表带（半成品）实际成本
INSERT INTO `erp_cost_actual` (
    `tenant_id`, `cost_no`, `work_order_id`, `production_order_id`, `product_id`,
    `production_quantity`, `material_cost`, `material_cost_adjust`, `labor_cost`, `labor_cost_adjust`,
    `overhead_cost`, `overhead_cost_adjust`, `total_cost`, `unit_cost`,
    `cost_currency`, `cost_period`, `calculation_date`, `last_adjust_date`,
    `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'COST-20250116-002',
    NULL,
    @production_order_id, 2003,
    100.0000,  -- 生产数量：100个
    650.00,  -- 材料成本：650元（100个×6.5元）
    0.00,
    200.00,  -- 人工成本：200元（100个×2元）
    0.00,
    150.00,  -- 制造费用：150元（100个×1.5元）
    0.00,
    1000.00,  -- 总成本：1000元
    10.00,  -- 单位成本：10元/个
    'CNY',
    '2025-01',
    '2025-01-16 16:00:00',
    NULL,
    3,  -- 状态：3-已确认
    '手环表带实际成本',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @cost_actual_2003_id = LAST_INSERT_ID();

-- 2.3 创建成本差异分析
-- 对比标准成本和实际成本，分析成本差异

-- 2.3.1 智能手环（成品）成本差异分析
INSERT INTO `erp_cost_variance` (
    `tenant_id`, `cost_actual_id`, `product_id`, `production_quantity`,
    `standard_total_cost`, `actual_total_cost`, `total_variance`, `total_variance_rate`,
    `material_variance`, `material_variance_rate`, `labor_variance`, `labor_variance_rate`,
    `overhead_variance`, `overhead_variance_rate`, `variance_type`,
    `analysis_date`, `variance_reason`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @cost_actual_2001_id, 2001, 100.0000,
    23000.00,  -- 标准总成本：100个×230元=23000元
    23400.00,  -- 实际总成本：23400元
    400.00,  -- 总差异：+400元（不利差异）
    1.74,  -- 总差异率：+1.74%
    200.00,  -- 材料成本差异：+200元（18200-18000）
    1.11,  -- 材料差异率：+1.11%
    100.00,  -- 人工成本差异：+100元（3100-3000）
    3.33,  -- 人工差异率：+3.33%
    100.00,  -- 制造费用差异：+100元（2100-2000）
    5.00,  -- 制造费用差异率：+5.00%
    2,  -- 差异类型：2-不利差异
    '2025-01-20 18:00:00',  -- 分析日期
    '材料价格上涨、人工工时增加、制造费用略增',  -- 差异原因
    '智能手环成本差异分析，实际成本略高于标准成本',
    @creator, NOW(), NOW(), b'0'
);

-- 2.3.2 手环主板（半成品）成本差异分析
INSERT INTO `erp_cost_variance` (
    `tenant_id`, `cost_actual_id`, `product_id`, `production_quantity`,
    `standard_total_cost`, `actual_total_cost`, `total_variance`, `total_variance_rate`,
    `material_variance`, `material_variance_rate`, `labor_variance`, `labor_variance_rate`,
    `overhead_variance`, `overhead_variance_rate`, `variance_type`,
    `analysis_date`, `variance_reason`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @cost_actual_2002_id, 2002, 101.0000,
    10403.00,  -- 标准总成本：101个×103元=10403元
    10504.00,  -- 实际总成本：10504元
    101.00,  -- 总差异：+101元（不利差异）
    0.97,  -- 总差异率：+0.97%
    91.00,  -- 材料成本差异：+91元（9191-9100）
    1.00,  -- 材料差异率：+1.00%
    0.00,  -- 人工成本差异：0元（808-808）
    0.00,  -- 人工差异率：0%
    10.00,  -- 制造费用差异：+10元（505-495）
    2.02,  -- 制造费用差异率：+2.02%
    2,  -- 差异类型：2-不利差异
    '2025-01-16 14:00:00',
    '材料成本略增，制造费用略增',
    '手环主板成本差异分析',
    @creator, NOW(), NOW(), b'0'
);

-- 2.3.3 手环表带（半成品）成本差异分析
INSERT INTO `erp_cost_variance` (
    `tenant_id`, `cost_actual_id`, `product_id`, `production_quantity`,
    `standard_total_cost`, `actual_total_cost`, `total_variance`, `total_variance_rate`,
    `material_variance`, `material_variance_rate`, `labor_variance`, `labor_variance_rate`,
    `overhead_variance`, `overhead_variance_rate`, `variance_type`,
    `analysis_date`, `variance_reason`, `remark`,
    `creator`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @cost_actual_2003_id, 2003, 100.0000,
    1000.00,  -- 标准总成本：100个×10元=1000元
    1000.00,  -- 实际总成本：1000元
    0.00,  -- 总差异：0元（无差异）
    0.00,  -- 总差异率：0%
    0.00,  -- 材料成本差异：0元
    0.00,  -- 材料差异率：0%
    0.00,  -- 人工成本差异：0元
    0.00,  -- 人工差异率：0%
    0.00,  -- 制造费用差异：0元
    0.00,  -- 制造费用差异率：0%
    1,  -- 差异类型：1-有利差异（无差异视为有利）
    '2025-01-16 16:00:00',
    '实际成本与标准成本完全一致',
    '手环表带成本差异分析，成本控制良好',
    @creator, NOW(), NOW(), b'0'
);

SET FOREIGN_KEY_CHECKS = 1;

