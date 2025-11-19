-- 生产订单项表
-- 创建时间: 2025-01-XX

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_production_order_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_production_order_items`;
CREATE TABLE `erp_production_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `production_order_id` bigint NOT NULL COMMENT '生产订单编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `product_name` varchar(255) NOT NULL COMMENT '产品名称',
  `product_spec` varchar(500) NULL DEFAULT '' COMMENT '产品规格',
  `unit_id` bigint NULL COMMENT '单位编号',
  `quantity` decimal(20,4) NOT NULL COMMENT '生产数量',
  `completed_quantity` decimal(20,4) NULL DEFAULT 0 COMMENT '已完成数量',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_production_order_id` (`production_order_id`) USING BTREE,
  INDEX `idx_product_id` (`product_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产订单项 DO';

SET FOREIGN_KEY_CHECKS = 1;

