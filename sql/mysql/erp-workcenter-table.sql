-- ============================================================
-- ERP 工作中心表结构
-- 创建时间: 2025-12-12
-- 说明: 工作中心主表，用于管理生产工作中心信息
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Table structure for erp_work_center
-- 工作中心主表
DROP TABLE IF EXISTS `erp_work_center`;
CREATE TABLE `erp_work_center` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `work_center_no` varchar(50) NOT NULL COMMENT '工作中心编号',
  `work_center_name` varchar(255) NOT NULL COMMENT '工作中心名称',
  `description` varchar(500) NULL DEFAULT '' COMMENT '描述',
  `location` varchar(200) NULL DEFAULT '' COMMENT '位置',
  `responsible_person` varchar(64) NULL DEFAULT '' COMMENT '负责人',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-启用，2-停用',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_work_center_no` (`tenant_id`, `work_center_no`) USING BTREE,
  INDEX `idx_work_center_name` (`work_center_name`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 工作中心主表';

SET FOREIGN_KEY_CHECKS = 1;

