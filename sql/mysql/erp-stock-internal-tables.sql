SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_stock_internal_in
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_internal_in`;
CREATE TABLE `erp_stock_internal_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '入库单号',
  `employee_id` bigint NULL COMMENT '员工编号（关联 system_users.id）',
  `dept_id` bigint NULL COMMENT '部门编号（关联 system_dept.id）',
  `internal_type` int NULL COMMENT '内部类型（1-部门调拨，2-员工领用，3-其他）',
  `in_time` datetime NULL COMMENT '入库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_employee_id` (`employee_id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 内部入库单';

-- ----------------------------
-- Table structure for erp_stock_internal_in_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_internal_in_item`;
CREATE TABLE `erp_stock_internal_in_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `in_id` bigint NULL COMMENT '内部入库编号（关联 erp_stock_internal_in.id）',
  `warehouse_id` bigint NULL COMMENT '仓库编号（关联 erp_warehouse.id）',
  `product_id` bigint NULL COMMENT '产品编号（关联 erp_product.id）',
  `product_unit_id` bigint NULL COMMENT '产品单位编号（冗余 erp_product.unit_id）',
  `product_price` decimal(20,2) NULL COMMENT '产品单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_in_id` (`in_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 内部入库单项';

-- ----------------------------
-- Table structure for erp_stock_internal_out
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_internal_out`;
CREATE TABLE `erp_stock_internal_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '出库单号',
  `employee_id` bigint NULL COMMENT '员工编号（关联 system_users.id）',
  `dept_id` bigint NULL COMMENT '部门编号（关联 system_dept.id）',
  `internal_type` int NULL COMMENT '内部类型（1-部门调拨，2-员工领用，3-其他）',
  `out_time` datetime NULL COMMENT '出库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_employee_id` (`employee_id`) USING BTREE,
  KEY `idx_dept_id` (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 内部出库单';

-- ----------------------------
-- Table structure for erp_stock_internal_out_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_internal_out_item`;
CREATE TABLE `erp_stock_internal_out_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `out_id` bigint NULL COMMENT '内部出库编号（关联 erp_stock_internal_out.id）',
  `warehouse_id` bigint NULL COMMENT '仓库编号（关联 erp_warehouse.id）',
  `product_id` bigint NULL COMMENT '产品编号（关联 erp_product.id）',
  `product_unit_id` bigint NULL COMMENT '产品单位编号（冗余 erp_product.unit_id）',
  `product_price` decimal(20,2) NULL COMMENT '产品单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_out_id` (`out_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 内部出库单项';

SET FOREIGN_KEY_CHECKS = 1;

