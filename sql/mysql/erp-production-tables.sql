-- ============================================================
-- 生产管理系统数据库表结构
-- 根据《生产管理系统开发计划-细化版.md》设计
-- 创建时间: 2025-01-XX
-- 说明: 包含Phase 1-4所有数据库表结构
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- Phase 1: 生产订单管理
-- ============================================================

-- ----------------------------
-- Table structure for erp_production_order
-- 生产订单主表（已存在，此处仅作参考）
-- ----------------------------
-- 注意：此表已在 erp_production_order.sql 中创建
-- 如需重新创建，请取消下面的注释
/*
DROP TABLE IF EXISTS `erp_production_order`;
CREATE TABLE `erp_production_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `no` varchar(50) NOT NULL COMMENT '生产订单号',
  `customer_id` bigint NULL COMMENT '客户ID（关联销售订单）',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `product_name` varchar(255) NOT NULL COMMENT '产品名称',
  `product_spec` varchar(500) NULL DEFAULT '' COMMENT '产品规格',
  `unit_id` bigint NULL COMMENT '单位ID',
  `quantity` decimal(20,4) NOT NULL COMMENT '生产数量',
  `completed_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '已完成数量',
  `start_time` datetime NULL COMMENT '计划开始时间',
  `end_time` datetime NULL COMMENT '计划完成时间',
  `actual_start_time` datetime NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL COMMENT '实际完成时间',
  `status` int NOT NULL COMMENT '状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消',
  `priority` int NULL DEFAULT 3 COMMENT '优先级：1-紧急，2-高，3-中，4-低',
  `source_type` int NULL DEFAULT 1 COMMENT '来源类型：1-手动创建，2-销售订单，3-库存补充',
  `source_id` bigint NULL COMMENT '来源单据ID',
  `description` text NULL COMMENT '生产说明',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_no` (`tenant_id`, `no`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE,
  INDEX `idx_customer` (`customer_id`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_create_time` (`create_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产订单 DO';
*/

-- ----------------------------
-- 现有表字段扩展
-- ----------------------------

-- 产品表扩展：production_type
SET @dbname = DATABASE();
SET @tablename = 'erp_product';
SET @columnname = 'production_type';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL DEFAULT 1 COMMENT ''产品类型：1-原材料，2-半成品，3-成品''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 产品表扩展：is_bom
SET @columnname = 'is_bom';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bit(1) NULL DEFAULT b''0'' COMMENT ''是否有BOM''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 仓库表扩展：warehouse_type
SET @tablename = 'erp_warehouse';
SET @columnname = 'warehouse_type';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL DEFAULT 1 COMMENT ''仓库类型：1-原材料仓，2-半成品仓，3-成品仓，4-废品仓''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 销售订单表扩展：production_status
SET @tablename = 'erp_sale_order';
SET @columnname = 'production_status';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` int NULL DEFAULT 0 COMMENT ''生产状态：0-无需生产，1-生产中，2-生产完成''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 采购订单表扩展：production_order_id
SET @tablename = 'erp_purchase_order';
SET @columnname = 'production_order_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联生产订单ID''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 库存入库表扩展：production_order_id
SET @tablename = 'erp_stock_in';
SET @columnname = 'production_order_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联生产订单ID''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- 库存出库表扩展：production_order_id
SET @tablename = 'erp_stock_out';
SET @columnname = 'production_order_id';
SET @preparedStatement = (SELECT IF(
    (
        SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
        WHERE
            (TABLE_SCHEMA = @dbname)
            AND (TABLE_NAME = @tablename)
            AND (COLUMN_NAME = @columnname)
    ) > 0,
    'SELECT 1',
    CONCAT('ALTER TABLE `', @tablename, '` ADD COLUMN `', @columnname, '` bigint NULL COMMENT ''关联生产订单ID''')
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- ============================================================
-- Phase 2: 生产计划管理
-- ============================================================

-- ----------------------------
-- Table structure for erp_product_bom
-- BOM主表
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_bom`;
CREATE TABLE `erp_product_bom` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `bom_no` varchar(50) NOT NULL COMMENT 'BOM编号',
  `bom_name` varchar(255) NOT NULL COMMENT 'BOM名称',
  `version` varchar(20) NULL DEFAULT '1.0' COMMENT '版本号',
  `effective_date` datetime NULL COMMENT '生效日期',
  `expire_date` datetime NULL COMMENT '失效日期',
  `bom_type` int NULL DEFAULT 1 COMMENT 'BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM',
  `standard_cost` decimal(20,2) NULL COMMENT '标准成本',
  `total_material_cost` decimal(20,2) NULL COMMENT '总材料成本',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-生效，3-失效',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_product` (`tenant_id`, `product_id`) USING BTREE,
  INDEX `idx_bom_no` (`tenant_id`, `bom_no`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP BOM主表';

-- ----------------------------
-- Table structure for erp_product_bom_item
-- BOM明细表
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_bom_item`;
CREATE TABLE `erp_product_bom_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `bom_id` bigint NOT NULL COMMENT 'BOM ID',
  `parent_product_id` bigint NOT NULL COMMENT '父产品ID',
  `child_product_id` bigint NOT NULL COMMENT '子产品ID',
  `child_product_name` varchar(255) NOT NULL COMMENT '子产品名称',
  `child_product_spec` varchar(500) NULL DEFAULT '' COMMENT '子产品规格',
  `unit_id` bigint NULL COMMENT '单位ID',
  `quantity` decimal(20,4) NOT NULL COMMENT '用量',
  `loss_rate` decimal(8,4) NULL DEFAULT 0 COMMENT '损耗率',
  `effective_quantity` decimal(20,4) GENERATED ALWAYS AS (`quantity` * (1 + `loss_rate`)) STORED COMMENT '有效用量',
  `is_key_material` bit(1) NULL DEFAULT b'0' COMMENT '是否关键物料',
  `is_alternative` bit(1) NULL DEFAULT b'0' COMMENT '是否替代料',
  `alternative_group` varchar(50) NULL DEFAULT '' COMMENT '替代料组',
  `position` int NULL DEFAULT 1 COMMENT '位号',
  `process_id` bigint NULL COMMENT '工序ID',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_bom` (`tenant_id`, `bom_id`) USING BTREE,
  INDEX `idx_parent` (`parent_product_id`) USING BTREE,
  INDEX `idx_child` (`child_product_id`) USING BTREE,
  INDEX `idx_process` (`process_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP BOM明细表';

-- ----------------------------
-- Table structure for erp_process_route
-- 工艺路线主表
-- ----------------------------
DROP TABLE IF EXISTS `erp_process_route`;
CREATE TABLE `erp_process_route` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `route_no` varchar(50) NOT NULL COMMENT '工艺路线编号',
  `route_name` varchar(255) NOT NULL COMMENT '工艺路线名称',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `version` varchar(20) NULL DEFAULT '1.0' COMMENT '版本号',
  `standard_cycle_time` int NULL COMMENT '标准周期时间（分钟）',
  `standard_labor_cost` decimal(20,2) NULL COMMENT '标准人工成本',
  `standard_overhead_cost` decimal(20,2) NULL COMMENT '标准制造费用',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-生效，3-失效',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_route` (`tenant_id`, `route_no`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工艺路线主表';

-- ----------------------------
-- Table structure for erp_process_route_item
-- 工艺路线明细表
-- ----------------------------
DROP TABLE IF EXISTS `erp_process_route_item`;
CREATE TABLE `erp_process_route_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `route_id` bigint NOT NULL COMMENT '工艺路线ID',
  `process_id` bigint NOT NULL COMMENT '工序ID',
  `sequence` int NOT NULL COMMENT '序号',
  `operation_name` varchar(255) NOT NULL COMMENT '工序名称',
  `standard_time` int NOT NULL COMMENT '标准工时（分钟）',
  `setup_time` int NULL DEFAULT 0 COMMENT '准备时间（分钟）',
  `worker_count` int NULL DEFAULT 1 COMMENT '人员数量',
  `equipment_id` bigint NULL COMMENT '设备ID',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `labor_rate` decimal(10,2) NULL COMMENT '人工费率',
  `overhead_rate` decimal(10,2) NULL COMMENT '制造费率',
  `is_bottleneck` bit(1) NULL DEFAULT b'0' COMMENT '是否瓶颈工序',
  `quality_check_required` bit(1) NULL DEFAULT b'0' COMMENT '是否需要质检',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_route` (`tenant_id`, `route_id`) USING BTREE,
  INDEX `idx_process` (`process_id`) USING BTREE,
  INDEX `idx_equipment` (`equipment_id`) USING BTREE,
  INDEX `idx_sequence` (`sequence`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工艺路线明细表';

-- ----------------------------
-- Table structure for erp_production_schedule
-- 生产排程主表
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_schedule`;
CREATE TABLE `erp_production_schedule` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `schedule_no` varchar(50) NOT NULL COMMENT '排程单号',
  `schedule_name` varchar(255) NOT NULL COMMENT '排程名称',
  `schedule_type` int NULL DEFAULT 1 COMMENT '排程类型：1-主排程，2-详细排程',
  `planning_horizon_days` int NULL DEFAULT 30 COMMENT '计划天数',
  `start_date` date NOT NULL COMMENT '计划开始日期',
  `end_date` date NOT NULL COMMENT '计划结束日期',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已发布，3-执行中，4-已完成',
  `total_orders` int NULL DEFAULT 0 COMMENT '总订单数',
  `total_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '总数量',
  `total_work_hours` int NULL DEFAULT 0 COMMENT '总工时',
  `created_by` varchar(64) NULL DEFAULT '' COMMENT '创建人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` varchar(64) NULL DEFAULT '' COMMENT '更新人',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_schedule` (`tenant_id`, `schedule_no`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE,
  INDEX `idx_date_range` (`start_date`, `end_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产排程主表';

-- ----------------------------
-- Table structure for erp_production_schedule_item
-- 排程明细表
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_schedule_item`;
CREATE TABLE `erp_production_schedule_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `schedule_id` bigint NOT NULL COMMENT '排程ID',
  `production_order_id` bigint NOT NULL COMMENT '生产订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `quantity` decimal(20,4) NOT NULL COMMENT '排程数量',
  `planned_start_time` datetime NOT NULL COMMENT '计划开始时间',
  `planned_end_time` datetime NOT NULL COMMENT '计划结束时间',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `equipment_id` bigint NULL COMMENT '设备ID',
  `process_sequence` int NULL COMMENT '工序序号',
  `priority` int NULL DEFAULT 3 COMMENT '优先级',
  `setup_time` int NULL DEFAULT 0 COMMENT '准备时间',
  `run_time` int NOT NULL COMMENT '运行时间',
  `wait_time` int NULL DEFAULT 0 COMMENT '等待时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟',
  `actual_start_time` datetime NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL COMMENT '实际结束时间',
  `completion_rate` decimal(5,2) NULL DEFAULT 0 COMMENT '完成率',
  `delay_reason` varchar(500) NULL DEFAULT '' COMMENT '延迟原因',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_schedule` (`tenant_id`, `schedule_id`) USING BTREE,
  INDEX `idx_order` (`production_order_id`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_equipment` (`equipment_id`) USING BTREE,
  INDEX `idx_time_range` (`planned_start_time`, `planned_end_time`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 排程明细表';

-- ----------------------------
-- Table structure for erp_mrp_params
-- MRP参数表
-- ----------------------------
DROP TABLE IF EXISTS `erp_mrp_params`;
CREATE TABLE `erp_mrp_params` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `param_name` varchar(100) NOT NULL COMMENT '参数名称',
  `param_code` varchar(50) NOT NULL COMMENT '参数编码',
  `param_value` text NOT NULL COMMENT '参数值',
  `param_type` int NULL DEFAULT 1 COMMENT '参数类型：1-字符串，2-数字，3-日期，4-布尔',
  `description` varchar(500) NULL DEFAULT '' COMMENT '参数描述',
  `is_system` bit(1) NULL DEFAULT b'0' COMMENT '是否系统参数',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_code` (`tenant_id`, `param_code`) USING BTREE,
  INDEX `idx_system` (`is_system`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP MRP参数表';

-- ----------------------------
-- Table structure for erp_mrp_result
-- MRP运算结果表
-- ----------------------------
DROP TABLE IF EXISTS `erp_mrp_result`;
CREATE TABLE `erp_mrp_result` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `run_no` varchar(50) NOT NULL COMMENT '运算批次号',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `warehouse_id` bigint NULL COMMENT '仓库ID',
  `period_start_date` date NOT NULL COMMENT '周期开始日期',
  `period_end_date` date NOT NULL COMMENT '周期结束日期',
  `gross_requirement` decimal(20,4) NULL DEFAULT 0 COMMENT '毛需求',
  `scheduled_receipts` decimal(20,4) NULL DEFAULT 0 COMMENT '计划接收量',
  `on_hand_inventory` decimal(20,4) NULL DEFAULT 0 COMMENT '现有库存',
  `net_requirement` decimal(20,4) NULL DEFAULT 0 COMMENT '净需求',
  `planned_order_receipts` decimal(20,4) NULL DEFAULT 0 COMMENT '计划订单接收量',
  `planned_order_releases` decimal(20,4) NULL DEFAULT 0 COMMENT '计划订单发放量',
  `order_type` int NULL DEFAULT 1 COMMENT '订单类型：1-生产订单，2-采购订单',
  `lot_sizing_rule` int NULL DEFAULT 1 COMMENT '批量规则：1-固定批量，2-按需，3-最小-最大',
  `lead_time` int NULL DEFAULT 0 COMMENT '提前期（天）',
  `safety_stock` decimal(20,4) NULL DEFAULT 0 COMMENT '安全库存',
  `order_status` int NULL DEFAULT 1 COMMENT '订单状态：1-建议，2-确认，3-下达',
  `due_date` date NULL COMMENT '需求日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_run` (`tenant_id`, `run_no`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_warehouse` (`warehouse_id`) USING BTREE,
  INDEX `idx_period` (`period_start_date`, `period_end_date`) USING BTREE,
  INDEX `idx_due_date` (`due_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP MRP运算结果表';

-- ============================================================
-- Phase 3: 生产执行与质量管理
-- ============================================================

-- ----------------------------
-- Table structure for erp_work_order
-- 工单主表
-- ----------------------------
DROP TABLE IF EXISTS `erp_work_order`;
CREATE TABLE `erp_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `work_order_no` varchar(50) NOT NULL COMMENT '工单号',
  `production_order_id` bigint NOT NULL COMMENT '生产订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `product_name` varchar(255) NOT NULL COMMENT '产品名称',
  `quantity` decimal(20,4) NOT NULL COMMENT '工单数量',
  `completed_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '完成数量',
  `qualified_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '合格数量',
  `work_center_id` bigint NOT NULL COMMENT '工作中心ID',
  `supervisor_id` bigint NULL COMMENT '主管ID',
  `planned_start_time` datetime NOT NULL COMMENT '计划开始时间',
  `planned_end_time` datetime NOT NULL COMMENT '计划结束时间',
  `actual_start_time` datetime NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL COMMENT '实际结束时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消',
  `priority` int NULL DEFAULT 3 COMMENT '优先级',
  `instruction` text NULL COMMENT '作业指导书',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_work_order` (`tenant_id`, `work_order_no`) USING BTREE,
  INDEX `idx_production_order` (`production_order_id`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE,
  INDEX `idx_time_range` (`planned_start_time`, `planned_end_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工单主表';

-- ----------------------------
-- Table structure for erp_work_order_progress
-- 工单进度表
-- ----------------------------
DROP TABLE IF EXISTS `erp_work_order_progress`;
CREATE TABLE `erp_work_order_progress` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `process_id` bigint NOT NULL COMMENT '工序ID',
  `process_name` varchar(255) NOT NULL COMMENT '工序名称',
  `sequence` int NOT NULL COMMENT '工序序号',
  `planned_start_time` datetime NULL COMMENT '计划开始时间',
  `planned_end_time` datetime NULL COMMENT '计划结束时间',
  `actual_start_time` datetime NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL COMMENT '实际结束时间',
  `planned_quantity` decimal(20,4) NULL COMMENT '计划数量',
  `completed_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '完成数量',
  `qualified_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '合格数量',
  `rejected_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '不合格数量',
  `scrap_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '报废数量',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-待开始，2-进行中，3-已完成，4-异常',
  `operator_id` bigint NULL COMMENT '操作员ID',
  `equipment_id` bigint NULL COMMENT '设备ID',
  `work_time` int NULL DEFAULT 0 COMMENT '实际工时（分钟）',
  `downtime` int NULL DEFAULT 0 COMMENT '停机时间（分钟）',
  `quality_status` int NULL DEFAULT 1 COMMENT '质检状态：1-待检，2-合格，3-不合格',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_work_order` (`tenant_id`, `work_order_id`) USING BTREE,
  INDEX `idx_process` (`process_id`) USING BTREE,
  INDEX `idx_operator` (`operator_id`) USING BTREE,
  INDEX `idx_equipment` (`equipment_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工单进度表';

-- ----------------------------
-- Table structure for erp_quality_standard
-- 质检标准表
-- ----------------------------
DROP TABLE IF EXISTS `erp_quality_standard`;
CREATE TABLE `erp_quality_standard` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `standard_no` varchar(50) NOT NULL COMMENT '标准编号',
  `standard_name` varchar(255) NOT NULL COMMENT '标准名称',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `process_id` bigint NULL COMMENT '工序ID',
  `inspection_type` int NULL DEFAULT 1 COMMENT '检验类型：1-进料检验，2-过程检验，3-成品检验',
  `standard_version` varchar(20) NULL DEFAULT '1.0' COMMENT '标准版本',
  `aql_level` varchar(10) NULL DEFAULT '' COMMENT 'AQL水平',
  `sampling_method` varchar(100) NULL DEFAULT '' COMMENT '抽样方法',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-生效，3-失效',
  `description` text NULL COMMENT '标准描述',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_standard` (`tenant_id`, `standard_no`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_process` (`process_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 质检标准表';

-- ----------------------------
-- Table structure for erp_quality_item
-- 质检项目表
-- ----------------------------
DROP TABLE IF EXISTS `erp_quality_item`;
CREATE TABLE `erp_quality_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `standard_id` bigint NOT NULL COMMENT '标准ID',
  `item_no` varchar(50) NOT NULL COMMENT '项目编号',
  `item_name` varchar(255) NOT NULL COMMENT '项目名称',
  `item_type` int NULL DEFAULT 1 COMMENT '项目类型：1-定性，2-定量',
  `test_method` varchar(500) NULL DEFAULT '' COMMENT '检验方法',
  `standard_value` varchar(500) NULL DEFAULT '' COMMENT '标准值',
  `tolerance` varchar(100) NULL DEFAULT '' COMMENT '公差范围',
  `unit` varchar(20) NULL DEFAULT '' COMMENT '单位',
  `is_key_item` bit(1) NULL DEFAULT b'0' COMMENT '是否关键项',
  `sequence` int NULL DEFAULT 1 COMMENT '检验序号',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_standard` (`tenant_id`, `standard_id`) USING BTREE,
  INDEX `idx_key_item` (`is_key_item`) USING BTREE,
  INDEX `idx_sequence` (`sequence`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 质检项目表';

-- ----------------------------
-- Table structure for erp_quality_inspection
-- 质检记录表
-- ----------------------------
DROP TABLE IF EXISTS `erp_quality_inspection`;
CREATE TABLE `erp_quality_inspection` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `inspection_no` varchar(50) NOT NULL COMMENT '检验单号',
  `batch_no` varchar(50) NULL DEFAULT '' COMMENT '批次号',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `process_id` bigint NULL COMMENT '工序ID',
  `work_order_id` bigint NULL COMMENT '工单ID',
  `inspection_type` int NULL DEFAULT 1 COMMENT '检验类型：1-进料检验，2-过程检验，3-成品检验',
  `inspection_level` int NULL DEFAULT 1 COMMENT '检验级别：1-全检，2-抽检',
  `lot_size` int NULL COMMENT '批量大小',
  `sample_size` int NULL COMMENT '样本数量',
  `qualified_quantity` int NULL DEFAULT 0 COMMENT '合格数量',
  `rejected_quantity` int NULL DEFAULT 0 COMMENT '不合格数量',
  `scrap_quantity` int NULL DEFAULT 0 COMMENT '报废数量',
  `inspection_result` int NULL DEFAULT 1 COMMENT '检验结果：1-合格，2-不合格，3-待复检',
  `inspector_id` bigint NULL COMMENT '检验员ID',
  `inspection_time` datetime NOT NULL COMMENT '检验时间',
  `environment` varchar(200) NULL DEFAULT '' COMMENT '检验环境',
  `equipment` varchar(200) NULL DEFAULT '' COMMENT '检验设备',
  `remark` text NULL COMMENT '检验备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_inspection` (`tenant_id`, `inspection_no`) USING BTREE,
  INDEX `idx_batch_no` (`batch_no`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_work_order` (`work_order_id`) USING BTREE,
  INDEX `idx_inspection_time` (`inspection_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 质检记录表';

-- ----------------------------
-- Table structure for erp_quality_inspection_item
-- 质检明细表
-- ----------------------------
DROP TABLE IF EXISTS `erp_quality_inspection_item`;
CREATE TABLE `erp_quality_inspection_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `inspection_id` bigint NOT NULL COMMENT '检验ID',
  `item_id` bigint NOT NULL COMMENT '检验项目ID',
  `sample_no` varchar(50) NULL DEFAULT '' COMMENT '样本编号',
  `measured_value` varchar(500) NULL DEFAULT '' COMMENT '测量值',
  `actual_value` decimal(20,6) NULL COMMENT '实际数值',
  `test_result` int NULL DEFAULT 1 COMMENT '检验结果：1-合格，2-不合格，3-超差',
  `defect_type` varchar(100) NULL DEFAULT '' COMMENT '缺陷类型',
  `defect_description` varchar(500) NULL DEFAULT '' COMMENT '缺陷描述',
  `image_urls` text NULL COMMENT '缺陷图片URLs',
  `inspector_id` bigint NULL COMMENT '检验员ID',
  `inspection_time` datetime NOT NULL COMMENT '检验时间',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_inspection` (`tenant_id`, `inspection_id`) USING BTREE,
  INDEX `idx_item` (`item_id`) USING BTREE,
  INDEX `idx_inspector` (`inspector_id`) USING BTREE,
  INDEX `idx_result` (`test_result`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 质检明细表';

-- ----------------------------
-- Table structure for erp_equipment
-- 设备台账表
-- ----------------------------
DROP TABLE IF EXISTS `erp_equipment`;
CREATE TABLE `erp_equipment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `equipment_no` varchar(50) NOT NULL COMMENT '设备编号',
  `equipment_name` varchar(255) NOT NULL COMMENT '设备名称',
  `equipment_type` varchar(100) NULL DEFAULT '' COMMENT '设备类型',
  `model` varchar(100) NULL DEFAULT '' COMMENT '设备型号',
  `manufacturer` varchar(255) NULL DEFAULT '' COMMENT '制造商',
  `serial_number` varchar(100) NULL DEFAULT '' COMMENT '序列号',
  `purchase_date` date NULL COMMENT '购置日期',
  `purchase_price` decimal(20,2) NULL COMMENT '购置价格',
  `service_life` int NULL COMMENT '设计寿命（年）',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `location` varchar(200) NULL DEFAULT '' COMMENT '设备位置',
  `capacity` decimal(20,4) NULL COMMENT '产能（小时/天）',
  `efficiency_rate` decimal(5,2) NULL DEFAULT 100.00 COMMENT '效率系数',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-正常，2-维修中，3-故障，4-报废',
  `responsible_person` varchar(64) NULL DEFAULT '' COMMENT '责任人',
  `specification` text NULL COMMENT '技术规格',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_equipment` (`tenant_id`, `equipment_no`) USING BTREE,
  INDEX `idx_type` (`equipment_type`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 设备台账表';

-- ----------------------------
-- Table structure for erp_equipment_status
-- 设备状态记录表
-- ----------------------------
DROP TABLE IF EXISTS `erp_equipment_status`;
CREATE TABLE `erp_equipment_status` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `equipment_id` bigint NOT NULL COMMENT '设备ID',
  `status` int NOT NULL COMMENT '状态：1-运行，2-待机，3-故障，4-维修，5-停机',
  `status_start_time` datetime NOT NULL COMMENT '状态开始时间',
  `status_end_time` datetime NULL COMMENT '状态结束时间',
  `duration` int NULL COMMENT '持续时间（分钟）',
  `work_order_id` bigint NULL COMMENT '关联工单ID',
  `operator_id` bigint NULL COMMENT '操作员ID',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_equipment` (`tenant_id`, `equipment_id`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE,
  INDEX `idx_time_range` (`status_start_time`, `status_end_time`) USING BTREE,
  INDEX `idx_work_order` (`work_order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 设备状态记录表';

-- ============================================================
-- Phase 4: 成本核算与数据分析
-- ============================================================

-- ----------------------------
-- Table structure for erp_cost_standard
-- 标准成本表
-- ----------------------------
DROP TABLE IF EXISTS `erp_cost_standard`;
CREATE TABLE `erp_cost_standard` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `version` varchar(20) NULL DEFAULT '1.0' COMMENT '版本号',
  `effective_date` datetime NOT NULL COMMENT '生效日期',
  `expire_date` datetime NULL COMMENT '失效日期',
  `material_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '材料成本',
  `labor_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '人工成本',
  `overhead_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '制造费用',
  `total_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '总成本',
  `cost_currency` varchar(10) NULL DEFAULT 'CNY' COMMENT '成本币种',
  `calculation_date` datetime NOT NULL COMMENT '计算日期',
  `bom_version` varchar(20) NULL DEFAULT '' COMMENT '关联BOM版本',
  `route_version` varchar(20) NULL DEFAULT '' COMMENT '关联工艺版本',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-生效，3-失效',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_product` (`tenant_id`, `product_id`) USING BTREE,
  INDEX `idx_effective_date` (`effective_date`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 标准成本表';

-- ----------------------------
-- Table structure for erp_cost_actual
-- 实际成本表
-- ----------------------------
DROP TABLE IF EXISTS `erp_cost_actual`;
CREATE TABLE `erp_cost_actual` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `cost_no` varchar(50) NOT NULL COMMENT '成本单号',
  `work_order_id` bigint NULL COMMENT '工单ID',
  `production_order_id` bigint NULL COMMENT '生产订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `production_quantity` decimal(20,4) NOT NULL COMMENT '生产数量',
  `material_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '材料成本',
  `material_cost_adjust` decimal(20,2) NULL DEFAULT 0 COMMENT '材料成本调整',
  `labor_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '人工成本',
  `labor_cost_adjust` decimal(20,2) NULL DEFAULT 0 COMMENT '人工成本调整',
  `overhead_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '制造费用',
  `overhead_cost_adjust` decimal(20,2) NULL DEFAULT 0 COMMENT '制造费用调整',
  `total_cost` decimal(20,2) NOT NULL DEFAULT 0 COMMENT '总成本',
  `unit_cost` decimal(20,4) NOT NULL DEFAULT 0 COMMENT '单位成本',
  `cost_currency` varchar(10) NULL DEFAULT 'CNY' COMMENT '成本币种',
  `cost_period` varchar(20) NOT NULL COMMENT '成本期间（YYYY-MM）',
  `calculation_date` datetime NOT NULL COMMENT '计算日期',
  `last_adjust_date` datetime NULL COMMENT '最后调整日期',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已计算，3-已确认',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_cost` (`tenant_id`, `cost_no`) USING BTREE,
  INDEX `idx_work_order` (`work_order_id`) USING BTREE,
  INDEX `idx_production_order` (`production_order_id`) USING BTREE,
  INDEX `idx_cost_period` (`cost_period`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 实际成本表';

-- ----------------------------
-- Table structure for erp_cost_variance
-- 成本差异分析表
-- ----------------------------
DROP TABLE IF EXISTS `erp_cost_variance`;
CREATE TABLE `erp_cost_variance` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `cost_actual_id` bigint NOT NULL COMMENT '实际成本ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `production_quantity` decimal(20,4) NOT NULL COMMENT '生产数量',
  `standard_total_cost` decimal(20,2) NOT NULL COMMENT '标准总成本',
  `actual_total_cost` decimal(20,2) NOT NULL COMMENT '实际总成本',
  `total_variance` decimal(20,2) NOT NULL COMMENT '总差异',
  `total_variance_rate` decimal(8,4) NOT NULL COMMENT '总差异率',
  `material_variance` decimal(20,2) NULL DEFAULT 0 COMMENT '材料成本差异',
  `material_variance_rate` decimal(8,4) NULL DEFAULT 0 COMMENT '材料差异率',
  `labor_variance` decimal(20,2) NULL DEFAULT 0 COMMENT '人工成本差异',
  `labor_variance_rate` decimal(8,4) NULL DEFAULT 0 COMMENT '人工差异率',
  `overhead_variance` decimal(20,2) NULL DEFAULT 0 COMMENT '制造费用差异',
  `overhead_variance_rate` decimal(8,4) NULL DEFAULT 0 COMMENT '制造费用差异率',
  `variance_type` int NULL DEFAULT 1 COMMENT '差异类型：1-有利，2-不利',
  `analysis_date` datetime NOT NULL COMMENT '分析日期',
  `variance_reason` varchar(500) NULL DEFAULT '' COMMENT '差异原因',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_actual` (`tenant_id`, `cost_actual_id`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_analysis_date` (`analysis_date`) USING BTREE,
  INDEX `idx_variance_type` (`variance_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 成本差异分析表';

-- ----------------------------
-- Table structure for erp_work_hours
-- 工时统计表
-- ----------------------------
DROP TABLE IF EXISTS `erp_work_hours`;
CREATE TABLE `erp_work_hours` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `work_order_id` bigint NOT NULL COMMENT '工单ID',
  `process_id` bigint NOT NULL COMMENT '工序ID',
  `operator_id` bigint NULL COMMENT '操作员ID',
  `work_date` date NOT NULL COMMENT '工作日期',
  `shift_id` bigint NULL COMMENT '班次ID',
  `start_time` time NULL COMMENT '开始时间',
  `end_time` time NULL COMMENT '结束时间',
  `duration` int NOT NULL COMMENT '工作时长（分钟）',
  `standard_duration` int NULL DEFAULT 0 COMMENT '标准工时（分钟）',
  `overtime_duration` int NULL DEFAULT 0 COMMENT '加班时长（分钟）',
  `machine_hours` decimal(10,2) NULL DEFAULT 0 COMMENT '机时',
  `operator_rate` decimal(10,2) NULL COMMENT '操作员工时费率',
  `machine_rate` decimal(10,2) NULL COMMENT '设备时费率',
  `labor_cost` decimal(20,2) NULL DEFAULT 0 COMMENT '人工成本',
  `machine_cost` decimal(20,2) NULL DEFAULT 0 COMMENT '设备成本',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-有效，2-无效',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_work_order` (`tenant_id`, `work_order_id`) USING BTREE,
  INDEX `idx_process` (`process_id`) USING BTREE,
  INDEX `idx_operator` (`operator_id`) USING BTREE,
  INDEX `idx_work_date` (`work_date`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工时统计表';

-- ----------------------------
-- Table structure for erp_production_kpi
-- 生产KPI表
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_kpi`;
CREATE TABLE `erp_production_kpi` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `kpi_no` varchar(50) NOT NULL COMMENT 'KPI编号',
  `kpi_name` varchar(255) NOT NULL COMMENT 'KPI名称',
  `kpi_type` int NOT NULL COMMENT 'KPI类型：1-OEE，2-合格率，3-达成率，4-交期率',
  `category` int NULL DEFAULT 1 COMMENT '分类：1-效率，2-质量，3-交付，4-成本',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `product_id` bigint NULL COMMENT '产品ID',
  `measurement_period` int NOT NULL COMMENT '统计周期：1-小时，2-天，3-周，4-月',
  `target_value` decimal(20,4) NULL COMMENT '目标值',
  `actual_value` decimal(20,4) NOT NULL COMMENT '实际值',
  `variance` decimal(20,4) NULL COMMENT '差异值',
  `variance_rate` decimal(8,4) NULL COMMENT '差异率',
  `calculation_date` datetime NOT NULL COMMENT '计算日期',
  `period_start_time` datetime NOT NULL COMMENT '周期开始时间',
  `period_end_time` datetime NOT NULL COMMENT '周期结束时间',
  `data_source` varchar(100) NULL DEFAULT '' COMMENT '数据来源',
  `remarks` text NULL COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_kpi` (`tenant_id`, `kpi_no`) USING BTREE,
  INDEX `idx_type` (`kpi_type`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_product` (`product_id`) USING BTREE,
  INDEX `idx_calculation_date` (`calculation_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产KPI表';

-- ----------------------------
-- Table structure for erp_production_report
-- 生产报表表
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_report`;
CREATE TABLE `erp_production_report` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `report_no` varchar(50) NOT NULL COMMENT '报表编号',
  `report_name` varchar(255) NOT NULL COMMENT '报表名称',
  `report_type` int NOT NULL COMMENT '报表类型：1-日报，2-周报，3-月报，4-年报',
  `report_period` varchar(50) NOT NULL COMMENT '报表期间',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `production_orders` int NULL DEFAULT 0 COMMENT '生产订单数',
  `total_plan_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '总计划数量',
  `total_completed_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '总完成数量',
  `total_qualified_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '总合格数量',
  `completion_rate` decimal(5,2) NULL DEFAULT 0 COMMENT '完成率',
  `quality_rate` decimal(5,2) NULL DEFAULT 0 COMMENT '合格率',
  `total_work_hours` decimal(20,2) NULL DEFAULT 0 COMMENT '总工时',
  `total_equipment_hours` decimal(20,2) NULL DEFAULT 0 COMMENT '总机时',
  `oee` decimal(5,2) NULL DEFAULT 0 COMMENT 'OEE',
  `on_time_delivery_rate` decimal(5,2) NULL DEFAULT 0 COMMENT '准时交付率',
  `production_value` decimal(20,2) NULL DEFAULT 0 COMMENT '产值',
  `average_cost` decimal(20,4) NULL DEFAULT 0 COMMENT '平均成本',
  `report_date` date NOT NULL COMMENT '报表日期',
  `generate_time` datetime NOT NULL COMMENT '生成时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-草稿，2-已发布',
  `report_data` text NULL COMMENT '详细数据（JSON）',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_report` (`tenant_id`, `report_no`) USING BTREE,
  INDEX `idx_type` (`report_type`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_report_date` (`report_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产报表表';

-- ----------------------------
-- Table structure for erp_production_dashboard_config
-- 看板配置表
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_dashboard_config`;
CREATE TABLE `erp_production_dashboard_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `config_name` varchar(255) NOT NULL COMMENT '配置名称',
  `config_type` int NULL DEFAULT 1 COMMENT '配置类型：1-大屏，2-PC端，3-移动端',
  `screen_resolution` varchar(50) NULL DEFAULT '' COMMENT '屏幕分辨率',
  `layout_config` text NOT NULL COMMENT '布局配置（JSON）',
  `component_config` text NOT NULL COMMENT '组件配置（JSON）',
  `data_refresh_interval` int NULL DEFAULT 5 COMMENT '数据刷新间隔（秒）',
  `is_default` bit(1) NULL DEFAULT b'0' COMMENT '是否默认配置',
  `is_active` bit(1) NULL DEFAULT b'1' COMMENT '是否启用',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_config` (`tenant_id`, `config_name`) USING BTREE,
  INDEX `idx_type` (`config_type`) USING BTREE,
  INDEX `idx_active` (`is_active`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 看板配置表';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 表结构创建完成
-- 总计：38张新表（不含生产订单主表，已在erp_production_order.sql中创建）
-- Phase 1: 现有表字段扩展（6个字段）
-- Phase 2: 8张表（BOM、工艺路线、排程、MRP）
-- Phase 3: 8张表（工单、质量、设备）
-- Phase 4: 7张表（成本、KPI、报表、看板）
-- ============================================================

