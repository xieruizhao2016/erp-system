SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_finance_balance_sheet
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_balance_sheet`;
CREATE TABLE `erp_finance_balance_sheet` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `period_date` date NOT NULL COMMENT '期间日期（年月）',
  `asset_total` decimal(20,2) NULL COMMENT '资产总计，单位：元',
  `liability_total` decimal(20,2) NULL COMMENT '负债总计，单位：元',
  `equity_total` decimal(20,2) NULL COMMENT '所有者权益总计，单位：元',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_period_date` (`period_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 资产负债表';

-- ----------------------------
-- Table structure for erp_finance_receivable
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_receivable`;
CREATE TABLE `erp_finance_receivable` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '单据号',
  `customer_id` bigint NULL COMMENT '客户编号（关联 erp_customer.id）',
  `order_id` bigint NULL COMMENT '销售订单编号（关联 erp_sale_order.id）',
  `amount` decimal(20,2) NULL COMMENT '应收金额，单位：元',
  `received_amount` decimal(20,2) NULL DEFAULT 0.00 COMMENT '已收金额，单位：元',
  `balance` decimal(20,2) NULL COMMENT '余额，单位：元（balance = amount - received_amount）',
  `due_date` date NULL COMMENT '到期日',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 应收账款';

-- ----------------------------
-- Table structure for erp_finance_payable
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_payable`;
CREATE TABLE `erp_finance_payable` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '单据号',
  `supplier_id` bigint NULL COMMENT '供应商编号（关联 erp_supplier.id）',
  `order_id` bigint NULL COMMENT '采购订单编号（关联 erp_purchase_order.id）',
  `amount` decimal(20,2) NULL COMMENT '应付金额，单位：元',
  `paid_amount` decimal(20,2) NULL DEFAULT 0.00 COMMENT '已付金额，单位：元',
  `balance` decimal(20,2) NULL COMMENT '余额，单位：元（balance = amount - paid_amount）',
  `due_date` date NULL COMMENT '到期日',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 应付账款';

-- ----------------------------
-- Table structure for erp_finance_prepayment
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_prepayment`;
CREATE TABLE `erp_finance_prepayment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '单据号',
  `supplier_id` bigint NULL COMMENT '供应商编号（关联 erp_supplier.id）',
  `order_id` bigint NULL COMMENT '采购订单编号（关联 erp_purchase_order.id，可选）',
  `amount` decimal(20,2) NULL COMMENT '预付款金额，单位：元',
  `used_amount` decimal(20,2) NULL DEFAULT 0.00 COMMENT '已使用金额，单位：元',
  `balance` decimal(20,2) NULL COMMENT '余额，单位：元（balance = amount - used_amount）',
  `prepay_date` date NULL COMMENT '预付日期',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_supplier_id` (`supplier_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 预付款';

-- ----------------------------
-- Table structure for erp_finance_prereceipt
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_prereceipt`;
CREATE TABLE `erp_finance_prereceipt` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '单据号',
  `customer_id` bigint NULL COMMENT '客户编号（关联 erp_customer.id）',
  `order_id` bigint NULL COMMENT '销售订单编号（关联 erp_sale_order.id，可选）',
  `amount` decimal(20,2) NULL COMMENT '预收款金额，单位：元',
  `used_amount` decimal(20,2) NULL DEFAULT 0.00 COMMENT '已使用金额，单位：元',
  `balance` decimal(20,2) NULL COMMENT '余额，单位：元（balance = amount - used_amount）',
  `prereceipt_date` date NULL COMMENT '预收日期',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_order_id` (`order_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 预收款';

-- ----------------------------
-- Table structure for erp_finance_profit_statement
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_profit_statement`;
CREATE TABLE `erp_finance_profit_statement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `period_date` date NOT NULL COMMENT '期间日期（年月）',
  `revenue` decimal(20,2) NULL COMMENT '营业收入，单位：元（从销售订单汇总）',
  `cost` decimal(20,2) NULL COMMENT '营业成本，单位：元（从采购订单/生产订单汇总）',
  `gross_profit` decimal(20,2) NULL COMMENT '毛利润，单位：元（gross_profit = revenue - cost）',
  `operating_expense` decimal(20,2) NULL DEFAULT 0.00 COMMENT '营业费用，单位：元',
  `net_profit` decimal(20,2) NULL COMMENT '净利润，单位：元（net_profit = gross_profit - operating_expense）',
  `status` int NULL COMMENT '状态（10-未审核，20-已审核）',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_period_date` (`period_date`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 利润表';

SET FOREIGN_KEY_CHECKS = 1;

