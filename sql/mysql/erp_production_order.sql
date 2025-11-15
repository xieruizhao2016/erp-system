-- 生产订单主表 - 用于测试代码生成功能
-- 创建时间: 2025-01-XX

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_production_order
-- ----------------------------
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

SET FOREIGN_KEY_CHECKS = 1;

