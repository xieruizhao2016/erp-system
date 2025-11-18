-- ============================================================
-- 生产管理系统高级功能Demo脚本
-- 包含：生产排程、MRP、工单、KPI、报表等功能
-- 创建时间: 2025-01-XX
-- 说明: 基于已完成的生产订单，创建相关的高级功能数据
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
-- 第一部分：生产排程
-- ============================================================

-- 1.1 创建生产排程主表
INSERT INTO `erp_production_schedule` (
    `tenant_id`, `schedule_no`, `schedule_name`, `schedule_type`,
    `planning_horizon_days`, `start_date`, `end_date`, `status`,
    `total_orders`, `total_quantity`, `total_work_hours`,
    `created_by`, `create_time`, `updated_by`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'SCH-202501-001',  -- 排程单号
    '2025年1月生产排程',  -- 排程名称
    1,  -- 排程类型：1-主排程
    30,  -- 计划天数：30天
    '2025-01-01',  -- 计划开始日期
    '2025-01-31',  -- 计划结束日期
    3,  -- 状态：3-执行中
    1,  -- 总订单数：1个
    100.0000,  -- 总数量：100个
    1800,  -- 总工时：1800分钟（30小时）
    @creator,
    NOW(),
    @creator,
    NOW(),
    b'0'
);

SET @schedule_id = LAST_INSERT_ID();

-- 1.2 创建排程明细（基于工艺路线）
-- 根据智能手环的工艺路线，创建3道工序的排程明细

-- 获取工艺路线ID
SET @route_id = (SELECT id FROM erp_process_route WHERE product_id = 2001 AND deleted = 0 LIMIT 1);

-- 工序1：组装
INSERT INTO `erp_production_schedule_item` (
    `tenant_id`, `schedule_id`, `production_order_id`, `product_id`,
    `quantity`, `planned_start_time`, `planned_end_time`,
    `work_center_id`, `equipment_id`, `process_sequence`, `priority`,
    `setup_time`, `run_time`, `wait_time`, `status`,
    `actual_start_time`, `actual_end_time`, `completion_rate`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @schedule_id, @production_order_id, 2001,
    100.0000,  -- 排程数量
    '2025-01-15 08:00:00',  -- 计划开始时间
    '2025-01-15 18:00:00',  -- 计划结束时间（30分钟×100个+准备时间）
    5003,  -- 工作中心ID：组装中心
    4004,  -- 设备ID：组装线
    1,  -- 工序序号
    2,  -- 优先级：高
    300,  -- 准备时间：5分钟×100个=500分钟，简化为300分钟
    3000,  -- 运行时间：30分钟×100个=3000分钟
    0,  -- 等待时间
    4,  -- 状态：4-已完成
    '2025-01-15 08:00:00',  -- 实际开始时间
    '2025-01-15 18:00:00',  -- 实际结束时间
    100.00,  -- 完成率：100%
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 工序2：最终测试
INSERT INTO `erp_production_schedule_item` (
    `tenant_id`, `schedule_id`, `production_order_id`, `product_id`,
    `quantity`, `planned_start_time`, `planned_end_time`,
    `work_center_id`, `equipment_id`, `process_sequence`, `priority`,
    `setup_time`, `run_time`, `wait_time`, `status`,
    `actual_start_time`, `actual_end_time`, `completion_rate`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @schedule_id, @production_order_id, 2001,
    100.0000,
    '2025-01-16 08:00:00',  -- 计划开始时间
    '2025-01-16 18:00:00',  -- 计划结束时间
    5003,  -- 工作中心ID：测试中心
    4005,  -- 设备ID：测试设备
    2,  -- 工序序号
    2,  -- 优先级：高
    500,  -- 准备时间：5分钟×100个=500分钟
    2000,  -- 运行时间：20分钟×100个=2000分钟
    0,
    4,  -- 状态：4-已完成
    '2025-01-16 08:00:00',
    '2025-01-16 18:00:00',
    100.00,
    @creator, NOW(), @creator, NOW(), b'0'
);

-- 工序3：包装
INSERT INTO `erp_production_schedule_item` (
    `tenant_id`, `schedule_id`, `production_order_id`, `product_id`,
    `quantity`, `planned_start_time`, `planned_end_time`,
    `work_center_id`, `equipment_id`, `process_sequence`, `priority`,
    `setup_time`, `run_time`, `wait_time`, `status`,
    `actual_start_time`, `actual_end_time`, `completion_rate`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @schedule_id, @production_order_id, 2001,
    100.0000,
    '2025-01-17 08:00:00',
    '2025-01-17 12:00:00',  -- 计划结束时间（5分钟×100个）
    5003,  -- 工作中心ID：包装中心
    NULL,  -- 设备ID：无（手工包装）
    3,  -- 工序序号
    3,  -- 优先级：中
    0,  -- 准备时间：0
    500,  -- 运行时间：5分钟×100个=500分钟
    0,
    4,  -- 状态：4-已完成
    '2025-01-17 08:00:00',
    '2025-01-17 12:00:00',
    100.00,
    @creator, NOW(), @creator, NOW(), b'0'
);

-- ============================================================
-- 第二部分：MRP物料需求计划
-- ============================================================

-- 2.1 创建MRP参数配置
INSERT INTO `erp_mrp_params` (
    `tenant_id`, `param_name`, `param_code`, `param_value`,
    `param_type`, `description`, `is_system`, `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES 
-- 安全库存比例
(@tenant_id, '安全库存比例', 'SAFETY_STOCK_RATE', '0.1', 2, '安全库存占需求量的比例', b'1', @creator, NOW(), @creator, NOW(), b'0'),
-- 提前期（天）
(@tenant_id, '默认提前期', 'DEFAULT_LEAD_TIME', '7', 2, '默认采购/生产提前期（天）', b'1', @creator, NOW(), @creator, NOW(), b'0'),
-- 批量规则
(@tenant_id, '批量规则', 'LOT_SIZING_RULE', '2', 2, '批量规则：1-固定批量，2-按需，3-最小-最大', b'1', @creator, NOW(), @creator, NOW(), b'0'),
-- 计划展望期（天）
(@tenant_id, '计划展望期', 'PLANNING_HORIZON', '90', 2, 'MRP计划展望期（天）', b'1', @creator, NOW(), @creator, NOW(), b'0');

-- 2.2 创建MRP运算结果
-- 运算批次号
SET @mrp_run_no = 'MRP-20250115-001';

-- 2.2.1 智能手环（成品）的MRP结果
INSERT INTO `erp_mrp_result` (
    `tenant_id`, `run_no`, `product_id`, `warehouse_id`,
    `period_start_date`, `period_end_date`,
    `gross_requirement`, `scheduled_receipts`, `on_hand_inventory`,
    `net_requirement`, `planned_order_receipts`, `planned_order_releases`,
    `order_type`, `lot_sizing_rule`, `lead_time`, `safety_stock`,
    `order_status`, `due_date`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @mrp_run_no, 2001, 3003,  -- 智能手环，成品仓
    '2025-01-15', '2025-01-20',
    100.0000,  -- 毛需求：100个
    0.0000,  -- 计划接收量：0
    0.0000,  -- 现有库存：0（生产前）
    100.0000,  -- 净需求：100个
    100.0000,  -- 计划订单接收量：100个
    100.0000,  -- 计划订单发放量：100个
    1,  -- 订单类型：1-生产订单
    2,  -- 批量规则：2-按需
    5,  -- 提前期：5天
    10.0000,  -- 安全库存：10个
    3,  -- 订单状态：3-下达（已创建生产订单）
    '2025-01-20',  -- 需求日期
    NOW(), NOW(), b'0'
);

-- 2.2.2 手环主板（半成品）的MRP结果
INSERT INTO `erp_mrp_result` (
    `tenant_id`, `run_no`, `product_id`, `warehouse_id`,
    `period_start_date`, `period_end_date`,
    `gross_requirement`, `scheduled_receipts`, `on_hand_inventory`,
    `net_requirement`, `planned_order_receipts`, `planned_order_releases`,
    `order_type`, `lot_sizing_rule`, `lead_time`, `safety_stock`,
    `order_status`, `due_date`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @mrp_run_no, 2002, 3002,  -- 手环主板，半成品仓
    '2025-01-15', '2025-01-16',
    101.0000,  -- 毛需求：101个（100×1.01）
    0.0000,  -- 计划接收量：0
    0.0000,  -- 现有库存：0
    101.0000,  -- 净需求：101个
    101.0000,  -- 计划订单接收量：101个
    101.0000,  -- 计划订单发放量：101个
    1,  -- 订单类型：1-生产订单（半成品生产）
    2,  -- 批量规则：2-按需
    2,  -- 提前期：2天
    10.0000,  -- 安全库存：10个
    3,  -- 订单状态：3-下达
    '2025-01-16',  -- 需求日期
    NOW(), NOW(), b'0'
);

-- 2.2.3 手环表带（半成品）的MRP结果
INSERT INTO `erp_mrp_result` (
    `tenant_id`, `run_no`, `product_id`, `warehouse_id`,
    `period_start_date`, `period_end_date`,
    `gross_requirement`, `scheduled_receipts`, `on_hand_inventory`,
    `net_requirement`, `planned_order_receipts`, `planned_order_releases`,
    `order_type`, `lot_sizing_rule`, `lead_time`, `safety_stock`,
    `order_status`, `due_date`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @mrp_run_no, 2003, 3002,  -- 手环表带，半成品仓
    '2025-01-15', '2025-01-16',
    100.0000,  -- 毛需求：100个
    0.0000,  -- 计划接收量：0
    0.0000,  -- 现有库存：0
    100.0000,  -- 净需求：100个
    100.0000,  -- 计划订单接收量：100个
    100.0000,  -- 计划订单发放量：100个
    1,  -- 订单类型：1-生产订单
    2,  -- 批量规则：2-按需
    1,  -- 提前期：1天
    5.0000,  -- 安全库存：5个
    3,  -- 订单状态：3-下达
    '2025-01-16',  -- 需求日期
    NOW(), NOW(), b'0'
);

-- 2.2.4 原材料MRP结果（以PCB板为例）
INSERT INTO `erp_mrp_result` (
    `tenant_id`, `run_no`, `product_id`, `warehouse_id`,
    `period_start_date`, `period_end_date`,
    `gross_requirement`, `scheduled_receipts`, `on_hand_inventory`,
    `net_requirement`, `planned_order_receipts`, `planned_order_releases`,
    `order_type`, `lot_sizing_rule`, `lead_time`, `safety_stock`,
    `order_status`, `due_date`, `create_time`, `update_time`, `deleted`
)
VALUES (
    @tenant_id, @mrp_run_no, 2010, 3001,  -- PCB板，原材料仓
    '2025-01-10', '2025-01-15',
    104.0000,  -- 毛需求：104个（101×1.02）
    0.0000,  -- 计划接收量：0
    150.0000,  -- 现有库存：150个
    0.0000,  -- 净需求：0（库存充足）
    0.0000,  -- 计划订单接收量：0
    0.0000,  -- 计划订单发放量：0
    2,  -- 订单类型：2-采购订单
    2,  -- 批量规则：2-按需
    7,  -- 提前期：7天
    15.0000,  -- 安全库存：15个
    1,  -- 订单状态：1-建议（库存充足，无需采购）
    '2025-01-15',  -- 需求日期
    NOW(), NOW(), b'0'
);

-- ============================================================
-- 第三部分：工单管理
-- ============================================================

-- 3.1 创建工单（基于生产订单和工艺路线）
-- 根据智能手环的工艺路线，为每道工序创建工单

-- 工单1：组装工序
INSERT INTO `erp_work_order` (
    `tenant_id`, `work_order_no`, `production_order_id`, `product_id`, `product_name`,
    `quantity`, `completed_quantity`, `qualified_quantity`,
    `work_center_id`, `supervisor_id`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `status`, `priority`, `instruction`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'WO-20250115-001',  -- 工单号
    @production_order_id, 2001, '智能手环',
    100.0000,  -- 工单数量
    100.0000,  -- 完成数量
    98.0000,  -- 合格数量（假设有2个不合格）
    5003,  -- 工作中心ID：组装中心
    NULL,  -- 主管ID
    '2025-01-15 08:00:00',  -- 计划开始时间
    '2025-01-15 18:00:00',  -- 计划结束时间
    '2025-01-15 08:00:00',  -- 实际开始时间
    '2025-01-15 18:00:00',  -- 实际结束时间
    5,  -- 状态：5-已完成
    2,  -- 优先级：高
    '1. 检查手环主板和表带是否完好\n2. 将主板安装到表带中\n3. 检查连接是否牢固',  -- 作业指导书
    '组装工序工单',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @work_order_1_id = LAST_INSERT_ID();

-- 工单2：最终测试工序
INSERT INTO `erp_work_order` (
    `tenant_id`, `work_order_no`, `production_order_id`, `product_id`, `product_name`,
    `quantity`, `completed_quantity`, `qualified_quantity`,
    `work_center_id`, `supervisor_id`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `status`, `priority`, `instruction`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'WO-20250116-001',
    @production_order_id, 2001, '智能手环',
    100.0000,
    100.0000,
    98.0000,  -- 合格数量（2个不合格）
    5003,  -- 工作中心ID：测试中心
    NULL,
    '2025-01-16 08:00:00',
    '2025-01-16 18:00:00',
    '2025-01-16 08:00:00',
    '2025-01-16 18:00:00',
    5,  -- 状态：5-已完成
    2,  -- 优先级：高
    '1. 测试心率监测功能\n2. 测试运动追踪功能\n3. 测试显示屏\n4. 测试电池续航',  -- 作业指导书
    '最终测试工序工单',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @work_order_2_id = LAST_INSERT_ID();

-- 工单3：包装工序
INSERT INTO `erp_work_order` (
    `tenant_id`, `work_order_no`, `production_order_id`, `product_id`, `product_name`,
    `quantity`, `completed_quantity`, `qualified_quantity`,
    `work_center_id`, `supervisor_id`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `status`, `priority`, `instruction`, `remark`,
    `creator`, `create_time`, `updater`, `update_time`, `deleted`
)
VALUES (
    @tenant_id,
    'WO-20250117-001',
    @production_order_id, 2001, '智能手环',
    98.0000,  -- 工单数量（只包装合格品）
    98.0000,  -- 完成数量
    98.0000,  -- 合格数量
    5003,  -- 工作中心ID：包装中心
    NULL,
    '2025-01-17 08:00:00',
    '2025-01-17 12:00:00',
    '2025-01-17 08:00:00',
    '2025-01-17 12:00:00',
    5,  -- 状态：5-已完成
    3,  -- 优先级：中
    '1. 检查产品外观\n2. 放入包装盒\n3. 贴标签\n4. 装箱',  -- 作业指导书
    '包装工序工单',
    @creator, NOW(), @creator, NOW(), b'0'
);

SET @work_order_3_id = LAST_INSERT_ID();

-- 3.2 创建工单进度记录
-- 工单1的进度记录
INSERT INTO `erp_work_order_progress` (
    `tenant_id`, `work_order_id`, `process_id`, `process_name`, `sequence`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `planned_quantity`, `completed_quantity`, `qualified_quantity`, `rejected_quantity`, `scrap_quantity`,
    `status`, `operator_id`, `equipment_id`, `work_time`, `downtime`,
    `quality_status`, `remark`, `create_time`, `update_time`
)
VALUES (
    @tenant_id, @work_order_1_id, 10006, '组装', 1,
    '2025-01-15 08:00:00', '2025-01-15 18:00:00',
    '2025-01-15 08:00:00', '2025-01-15 18:00:00',
    100.0000, 100.0000, 98.0000, 2.0000, 0.0000,
    3,  -- 状态：3-已完成
    NULL, 4004, 600, 0,  -- 实际工时：600分钟（10小时），停机时间：0
    2,  -- 质检状态：2-合格
    '组装完成，2个不合格品需返工',
    NOW(), NOW()
);

-- 工单2的进度记录
INSERT INTO `erp_work_order_progress` (
    `tenant_id`, `work_order_id`, `process_id`, `process_name`, `sequence`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `planned_quantity`, `completed_quantity`, `qualified_quantity`, `rejected_quantity`, `scrap_quantity`,
    `status`, `operator_id`, `equipment_id`, `work_time`, `downtime`,
    `quality_status`, `remark`, `create_time`, `update_time`
)
VALUES (
    @tenant_id, @work_order_2_id, 10007, '最终测试', 2,
    '2025-01-16 08:00:00', '2025-01-16 18:00:00',
    '2025-01-16 08:00:00', '2025-01-16 18:00:00',
    100.0000, 100.0000, 98.0000, 2.0000, 0.0000,
    3,  -- 状态：3-已完成
    NULL, 4005, 600, 0,  -- 实际工时：600分钟
    2,  -- 质检状态：2-合格
    '测试完成，2个不合格品',
    NOW(), NOW()
);

-- 工单3的进度记录
INSERT INTO `erp_work_order_progress` (
    `tenant_id`, `work_order_id`, `process_id`, `process_name`, `sequence`,
    `planned_start_time`, `planned_end_time`,
    `actual_start_time`, `actual_end_time`,
    `planned_quantity`, `completed_quantity`, `qualified_quantity`, `rejected_quantity`, `scrap_quantity`,
    `status`, `operator_id`, `equipment_id`, `work_time`, `downtime`,
    `quality_status`, `remark`, `create_time`, `update_time`
)
VALUES (
    @tenant_id, @work_order_3_id, 10008, '包装', 3,
    '2025-01-17 08:00:00', '2025-01-17 12:00:00',
    '2025-01-17 08:00:00', '2025-01-17 12:00:00',
    98.0000, 98.0000, 98.0000, 0.0000, 0.0000,
    3,  -- 状态：3-已完成
    NULL, NULL, 240, 0,  -- 实际工时：240分钟（4小时）
    2,  -- 质检状态：2-合格
    '包装完成',
    NOW(), NOW()
);

-- ============================================================
-- 第四部分：生产KPI
-- ============================================================

-- 4.1 OEE（设备综合效率）KPI
INSERT INTO `erp_production_kpi` (
    `tenant_id`, `kpi_no`, `kpi_name`, `kpi_type`, `category`,
    `work_center_id`, `product_id`, `measurement_period`,
    `target_value`, `actual_value`, `variance`, `variance_rate`,
    `calculation_date`, `period_start_time`, `period_end_time`,
    `data_source`, `remarks`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'KPI-202501-OEE-001',
    '组装中心OEE', 1, 1,  -- KPI类型：1-OEE，分类：1-效率
    5003, 2001, 4,  -- 工作中心：5003，产品：2001，统计周期：4-月
    85.00,  -- 目标值：85%
    82.50,  -- 实际值：82.5%
    -2.50,  -- 差异值：-2.5%
    -2.94,  -- 差异率：-2.94%
    '2025-01-20 18:00:00',  -- 计算日期
    '2025-01-01 00:00:00',  -- 周期开始时间
    '2025-01-31 23:59:59',  -- 周期结束时间
    '生产执行数据',  -- 数据来源
    'OEE = 可用率 × 性能率 × 质量率',
    @creator, NOW(), b'0'
);

-- 4.2 合格率KPI
INSERT INTO `erp_production_kpi` (
    `tenant_id`, `kpi_no`, `kpi_name`, `kpi_type`, `category`,
    `work_center_id`, `product_id`, `measurement_period`,
    `target_value`, `actual_value`, `variance`, `variance_rate`,
    `calculation_date`, `period_start_time`, `period_end_time`,
    `data_source`, `remarks`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'KPI-202501-QR-001',
    '智能手环合格率', 2, 2,  -- KPI类型：2-合格率，分类：2-质量
    5003, 2001, 4,  -- 统计周期：4-月
    99.00,  -- 目标值：99%
    98.00,  -- 实际值：98%（100个中98个合格）
    -1.00,  -- 差异值：-1%
    -1.01,  -- 差异率：-1.01%
    '2025-01-20 18:00:00',
    '2025-01-01 00:00:00',
    '2025-01-31 23:59:59',
    '质检数据',
    '合格率 = 合格数量 / 完成数量 × 100%',
    @creator, NOW(), b'0'
);

-- 4.3 达成率KPI
INSERT INTO `erp_production_kpi` (
    `tenant_id`, `kpi_no`, `kpi_name`, `kpi_type`, `category`,
    `work_center_id`, `product_id`, `measurement_period`,
    `target_value`, `actual_value`, `variance`, `variance_rate`,
    `calculation_date`, `period_start_time`, `period_end_time`,
    `data_source`, `remarks`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'KPI-202501-AR-001',
    '生产计划达成率', 3, 3,  -- KPI类型：3-达成率，分类：3-交付
    NULL, 2001, 4,  -- 不限定工作中心，产品：2001
    100.00,  -- 目标值：100%
    100.00,  -- 实际值：100%（100个全部完成）
    0.00,  -- 差异值：0%
    0.00,  -- 差异率：0%
    '2025-01-20 18:00:00',
    '2025-01-01 00:00:00',
    '2025-01-31 23:59:59',
    '生产订单数据',
    '达成率 = 完成数量 / 计划数量 × 100%',
    @creator, NOW(), b'0'
);

-- 4.4 交期率KPI
INSERT INTO `erp_production_kpi` (
    `tenant_id`, `kpi_no`, `kpi_name`, `kpi_type`, `category`,
    `work_center_id`, `product_id`, `measurement_period`,
    `target_value`, `actual_value`, `variance`, `variance_rate`,
    `calculation_date`, `period_start_time`, `period_end_time`,
    `data_source`, `remarks`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'KPI-202501-DR-001',
    '准时交付率', 4, 3,  -- KPI类型：4-交期率，分类：3-交付
    NULL, 2001, 4,
    95.00,  -- 目标值：95%
    100.00,  -- 实际值：100%（按时完成）
    5.00,  -- 差异值：+5%
    5.26,  -- 差异率：+5.26%
    '2025-01-20 18:00:00',
    '2025-01-01 00:00:00',
    '2025-01-31 23:59:59',
    '生产订单数据',
    '交期率 = 按时完成订单数 / 总订单数 × 100%',
    @creator, NOW(), b'0'
);

-- ============================================================
-- 第五部分：生产报表
-- ============================================================

-- 5.1 创建日报
INSERT INTO `erp_production_report` (
    `tenant_id`, `report_no`, `report_name`, `report_type`, `report_period`,
    `work_center_id`, `production_orders`, `total_plan_quantity`, `total_completed_quantity`, `total_qualified_quantity`,
    `completion_rate`, `quality_rate`, `total_work_hours`, `total_equipment_hours`,
    `oee`, `on_time_delivery_rate`, `production_value`, `average_cost`,
    `report_date`, `generate_time`, `status`, `report_data`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'RPT-20250120-DAILY',
    '2025年1月20日生产日报', 1, '2025-01-20',  -- 报表类型：1-日报
    5003,  -- 工作中心ID
    1,  -- 生产订单数
    100.0000,  -- 总计划数量
    100.0000,  -- 总完成数量
    98.0000,  -- 总合格数量
    100.00,  -- 完成率：100%
    98.00,  -- 合格率：98%
    1440.00,  -- 总工时：1440分钟（24小时）
    1200.00,  -- 总机时：1200分钟（20小时）
    82.50,  -- OEE：82.5%
    100.00,  -- 准时交付率：100%
    29302.00,  -- 产值：98个×299元=29,302元
    257.11,  -- 平均成本：257.11元/个
    '2025-01-20',  -- 报表日期
    '2025-01-20 18:00:00',  -- 生成时间
    2,  -- 状态：2-已发布
    '{"daily_summary": "生产订单PO-20250101-001完成，100个智能手环生产完成，98个合格"}',  -- 详细数据（JSON）
    @creator, NOW(), b'0'
);

-- 5.2 创建周报
INSERT INTO `erp_production_report` (
    `tenant_id`, `report_no`, `report_name`, `report_type`, `report_period`,
    `work_center_id`, `production_orders`, `total_plan_quantity`, `total_completed_quantity`, `total_qualified_quantity`,
    `completion_rate`, `quality_rate`, `total_work_hours`, `total_equipment_hours`,
    `oee`, `on_time_delivery_rate`, `production_value`, `average_cost`,
    `report_date`, `generate_time`, `status`, `report_data`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'RPT-202501-WEEK03',
    '2025年1月第3周生产周报', 2, '2025-01-13至2025-01-19',  -- 报表类型：2-周报
    5003,
    1,
    100.0000,
    100.0000,
    98.0000,
    100.00,
    98.00,
    1440.00,
    1200.00,
    82.50,
    100.00,
    29302.00,
    257.11,
    '2025-01-19',  -- 报表日期（周末）
    '2025-01-19 18:00:00',
    2,  -- 状态：2-已发布
    '{"week_summary": "本周完成1个生产订单，100个智能手环，98个合格"}',
    @creator, NOW(), b'0'
);

-- 5.3 创建月报
INSERT INTO `erp_production_report` (
    `tenant_id`, `report_no`, `report_name`, `report_type`, `report_period`,
    `work_center_id`, `production_orders`, `total_plan_quantity`, `total_completed_quantity`, `total_qualified_quantity`,
    `completion_rate`, `quality_rate`, `total_work_hours`, `total_equipment_hours`,
    `oee`, `on_time_delivery_rate`, `production_value`, `average_cost`,
    `report_date`, `generate_time`, `status`, `report_data`, `creator`, `create_time`, `deleted`
)
VALUES (
    @tenant_id,
    'RPT-202501-MONTHLY',
    '2025年1月生产月报', 3, '2025年1月',  -- 报表类型：3-月报
    5003,
    1,
    100.0000,
    100.0000,
    98.0000,
    100.00,
    98.00,
    1440.00,
    1200.00,
    82.50,
    100.00,
    29302.00,
    257.11,
    '2025-01-31',  -- 报表日期（月末）
    '2025-01-31 18:00:00',
    2,  -- 状态：2-已发布
    '{"month_summary": "1月完成1个生产订单，100个智能手环，98个合格，按时交付率100%"}',
    @creator, NOW(), b'0'
);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证查询
-- ============================================================

-- 查询生产排程
-- SELECT schedule_no, schedule_name, total_orders, total_quantity, total_work_hours, status 
-- FROM erp_production_schedule 
-- WHERE schedule_no = 'SCH-202501-001' AND deleted = 0;

-- 查询排程明细
-- SELECT item.sequence, item.quantity, item.planned_start_time, item.planned_end_time, 
--        item.actual_start_time, item.actual_end_time, item.completion_rate, item.status
-- FROM erp_production_schedule_item item
-- WHERE item.schedule_id = @schedule_id AND item.deleted = 0
-- ORDER BY item.sequence;

-- 查询MRP运算结果
-- SELECT p.name as product_name, mr.gross_requirement, mr.on_hand_inventory, 
--        mr.net_requirement, mr.planned_order_receipts, mr.order_status, mr.due_date
-- FROM erp_mrp_result mr
-- LEFT JOIN erp_product p ON mr.product_id = p.id
-- WHERE mr.run_no = 'MRP-20250115-001' AND mr.deleted = 0
-- ORDER BY mr.product_id;

-- 查询工单
-- SELECT work_order_no, product_name, quantity, completed_quantity, qualified_quantity, status
-- FROM erp_work_order
-- WHERE production_order_id = @production_order_id AND deleted = 0
-- ORDER BY create_time;

-- 查询工单进度
-- SELECT wo.work_order_no, prog.process_name, prog.sequence, 
--        prog.completed_quantity, prog.qualified_quantity, prog.status
-- FROM erp_work_order_progress prog
-- LEFT JOIN erp_work_order wo ON prog.work_order_id = wo.id
-- WHERE wo.production_order_id = @production_order_id
-- ORDER BY wo.id, prog.sequence;

-- 查询生产KPI
-- SELECT kpi_name, kpi_type, target_value, actual_value, variance, variance_rate
-- FROM erp_production_kpi
-- WHERE calculation_date >= '2025-01-01' AND deleted = 0
-- ORDER BY kpi_type;

-- 查询生产报表
-- SELECT report_name, report_type, report_period, production_orders, 
--        completion_rate, quality_rate, oee, on_time_delivery_rate
-- FROM erp_production_report
-- WHERE report_date >= '2025-01-01' AND deleted = 0
-- ORDER BY report_type, report_date;

