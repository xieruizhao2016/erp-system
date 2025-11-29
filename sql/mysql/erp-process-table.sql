-- ============================================================
-- ERP 工序管理表结构
-- 创建时间: 2025-01-XX
-- 说明: 工序主表，用于管理基础工序信息
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_process
-- 工序主表
-- ----------------------------
DROP TABLE IF EXISTS `erp_process`;
CREATE TABLE `erp_process` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `process_no` varchar(50) NOT NULL COMMENT '工序编号',
  `process_name` varchar(255) NOT NULL COMMENT '工序名称',
  `process_type` int NULL DEFAULT 1 COMMENT '工序类型：1-加工，2-装配，3-检验，4-包装，5-其他',
  `category` varchar(100) NULL COMMENT '工序分类',
  `standard_time` int NULL COMMENT '标准工时（分钟）',
  `setup_time` int NULL DEFAULT 0 COMMENT '准备时间（分钟）',
  `worker_count` int NULL DEFAULT 1 COMMENT '标准人员数量',
  `equipment_type` varchar(100) NULL COMMENT '设备类型',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `labor_rate` decimal(10,2) NULL COMMENT '人工费率（元/小时）',
  `overhead_rate` decimal(10,2) NULL COMMENT '制造费率（元/小时）',
  `quality_check_required` bit(1) NULL DEFAULT b'0' COMMENT '是否需要质检',
  `is_bottleneck` bit(1) NULL DEFAULT b'0' COMMENT '是否瓶颈工序',
  `description` text NULL COMMENT '工序描述',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-启用，2-停用',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_process_no` (`tenant_id`, `process_no`) USING BTREE,
  INDEX `idx_process_name` (`process_name`) USING BTREE,
  INDEX `idx_process_type` (`process_type`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工序主表';

SET FOREIGN_KEY_CHECKS = 1;





