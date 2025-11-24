SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for erp_sale_return_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_return_items`;
CREATE TABLE `erp_sale_return_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `return_id` bigint NULL COMMENT '销售退货编号',
  `order_item_id` bigint NULL COMMENT '销售订单项编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售退货项 DO
 *';

-- ----------------------------
-- Table structure for erp_customer
-- ----------------------------
DROP TABLE IF EXISTS `erp_customer`;
CREATE TABLE `erp_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '开启状态',
  `name` varchar(100) NULL DEFAULT '' COMMENT '客户名称',
  `contact` varchar(255) NULL DEFAULT '' COMMENT '联系人',
  `mobile` varchar(20) NULL DEFAULT '' COMMENT '手机号码',
  `telephone` varchar(20) NULL DEFAULT '' COMMENT '联系电话',
  `email` varchar(100) NULL DEFAULT '' COMMENT '电子邮箱',
  `fax` varchar(255) NULL DEFAULT '' COMMENT '传真',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `sort` int NULL COMMENT '排序',
  `tax_no` varchar(50) NULL DEFAULT '' COMMENT '纳税人识别号',
  `tax_percent` decimal(20,2) NULL COMMENT '税率',
  `bank_name` varchar(100) NULL DEFAULT '' COMMENT '开户行',
  `bank_account` varchar(255) NULL DEFAULT '' COMMENT '开户账号',
  `bank_address` varchar(500) NULL DEFAULT '' COMMENT '开户地址',
  `shipping_address` varchar(500) NULL DEFAULT '' COMMENT '收货地址',
  `payment_method` varchar(50) NULL DEFAULT '' COMMENT '付款方式',
  `customer_requirements` text NULL COMMENT '客户要求',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 客户 DO
 *';

-- ----------------------------
-- Table structure for erp_sale_order
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_order`;
CREATE TABLE `erp_sale_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sale_user_id` bigint NULL COMMENT '销售员编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `customer_id` bigint NULL COMMENT '客户编号',
  `status` int NULL COMMENT '销售状态',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '销售订单号',
  `order_time` datetime NULL COMMENT '下单时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `deposit_price` decimal(20,2) NULL COMMENT '定金金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `out_count` decimal(20,2) NULL COMMENT '销售出库数量',
  `return_count` decimal(20,2) NULL COMMENT '销售退货数量',
  `total_price` decimal(20,2) NULL COMMENT '最终合计价格，单位：元',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `purchase_order_no` varchar(100) NULL DEFAULT '' COMMENT '采购单号',
  `contract_no` varchar(100) NULL DEFAULT '' COMMENT '合同编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售订单 DO
 *';

-- ----------------------------
-- Table structure for erp_sale_return
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_return`;
CREATE TABLE `erp_sale_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sale_user_id` bigint NULL COMMENT '销售员编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `customer_id` bigint NULL COMMENT '客户编号',
  `order_id` bigint NULL COMMENT '销售订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '销售退货单号',
  `return_time` datetime NULL COMMENT '退货时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `other_price` decimal(20,2) NULL COMMENT '其它金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售退货 DO
 *';

-- ----------------------------
-- Table structure for erp_sale_order_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_order_items`;
CREATE TABLE `erp_sale_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `order_id` bigint NULL COMMENT '销售订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `out_count` decimal(20,2) NULL COMMENT '销售出库数量',
  `return_count` decimal(20,2) NULL COMMENT '销售退货数量',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售订单项 DO
 *';

-- ----------------------------
-- Table structure for erp_sale_out
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_out`;
CREATE TABLE `erp_sale_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sale_user_id` bigint NULL COMMENT '销售员编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `customer_id` bigint NULL COMMENT '客户编号',
  `order_id` bigint NULL COMMENT '销售订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '销售出库单号',
  `out_time` datetime NULL COMMENT '出库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `other_price` decimal(20,2) NULL COMMENT '其它金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售出库 DO
 *';

-- ----------------------------
-- Table structure for erp_sale_out_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_sale_out_items`;
CREATE TABLE `erp_sale_out_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `out_id` bigint NULL COMMENT '销售出库编号',
  `order_item_id` bigint NULL COMMENT '销售订单项编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 销售出库项 DO
 *';

-- ----------------------------
-- Table structure for erp_product_category
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_category`;
CREATE TABLE `erp_product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '开启状态',
  `parent_id` bigint NULL COMMENT '父分类编号',
  `name` varchar(100) NULL DEFAULT '' COMMENT '分类名称',
  `code` varchar(50) NULL DEFAULT '' COMMENT '分类编码',
  `sort` int NULL COMMENT '分类排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品分类 DO
 *';

-- ----------------------------
-- Table structure for erp_product_unit
-- ----------------------------
DROP TABLE IF EXISTS `erp_product_unit`;
CREATE TABLE `erp_product_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `name` varchar(100) NULL DEFAULT '' COMMENT '单位名字',
  `status` int NULL COMMENT '单位状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品单位 DO
 *';

-- ----------------------------
-- Table structure for erp_product
-- ----------------------------
DROP TABLE IF EXISTS `erp_product`;
CREATE TABLE `erp_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '产品状态',
  `category_id` bigint NULL COMMENT '产品分类编号',
  `unit_id` bigint NULL COMMENT '单位编号',
  `name` varchar(100) NULL DEFAULT '' COMMENT '产品名称',
  `bar_code` varchar(50) NULL DEFAULT '' COMMENT '产品条码',
  `standard` varchar(255) NULL DEFAULT '' COMMENT '产品规格',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '产品备注',
  `expiry_day` int NULL COMMENT '保质期天数',
  `weight` decimal(20,2) NULL COMMENT '基础重量（kg）',
  `purchase_price` decimal(20,2) NULL COMMENT '采购价格，单位：元',
  `sale_price` decimal(20,2) NULL COMMENT '销售价格，单位：元',
  `min_price` decimal(20,2) NULL COMMENT '最低价格，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_in
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_in`;
CREATE TABLE `erp_purchase_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `supplier_id` bigint NULL COMMENT '供应商编号',
  `order_id` bigint NULL COMMENT '采购订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '采购入库单号',
  `status` int NULL COMMENT '入库状态',
  `order_no` varchar(50) NULL DEFAULT '' COMMENT '采购订单号（冗余字段）',
  `in_time` datetime NULL COMMENT '入库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(20,2) NULL COMMENT '其它金额，单位：元',
  `total_price` decimal(20,2) NULL COMMENT '最终合计价格，单位：元',
  `payment_price` decimal(20,2) NULL COMMENT '已支付金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购入库 DO
 *';

-- ----------------------------
-- Table structure for erp_supplier
-- ----------------------------
DROP TABLE IF EXISTS `erp_supplier`;
CREATE TABLE `erp_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '开启状态',
  `name` varchar(100) NULL DEFAULT '' COMMENT '供应商名称',
  `contact` varchar(255) NULL DEFAULT '' COMMENT '联系人',
  `mobile` varchar(20) NULL DEFAULT '' COMMENT '手机号码',
  `telephone` varchar(20) NULL DEFAULT '' COMMENT '联系电话',
  `email` varchar(100) NULL DEFAULT '' COMMENT '电子邮箱',
  `fax` varchar(255) NULL DEFAULT '' COMMENT '传真',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `sort` int NULL COMMENT '排序',
  `tax_no` varchar(50) NULL DEFAULT '' COMMENT '纳税人识别号',
  `tax_percent` decimal(20,2) NULL COMMENT '税率',
  `bank_name` varchar(100) NULL DEFAULT '' COMMENT '开户行',
  `bank_account` varchar(255) NULL DEFAULT '' COMMENT '开户账号',
  `bank_address` varchar(500) NULL DEFAULT '' COMMENT '开户地址',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 供应商 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_order
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_order`;
CREATE TABLE `erp_purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `supplier_id` bigint NULL COMMENT '供应商编号',
  `status` int NULL COMMENT '采购状态',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '采购订单号',
  `order_time` datetime NULL COMMENT '下单时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `deposit_price` decimal(20,2) NULL COMMENT '定金金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `in_count` decimal(20,2) NULL COMMENT '采购入库数量',
  `return_count` decimal(20,2) NULL COMMENT '采购退货数量',
  `total_price` decimal(20,2) NULL COMMENT '最终合计价格，单位：元',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购订单 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_return_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_return_items`;
CREATE TABLE `erp_purchase_return_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `return_id` bigint NULL COMMENT '采购退货编号',
  `order_item_id` bigint NULL COMMENT '采购订单项编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购退货项 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_in_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_in_items`;
CREATE TABLE `erp_purchase_in_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `in_id` bigint NULL COMMENT '采购入库编号',
  `order_item_id` bigint NULL COMMENT '采购订单项编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购入库项 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_return
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_return`;
CREATE TABLE `erp_purchase_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `supplier_id` bigint NULL COMMENT '供应商编号',
  `order_id` bigint NULL COMMENT '采购订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '采购退货单号',
  `status` int NULL COMMENT '退货状态',
  `order_no` varchar(50) NULL DEFAULT '' COMMENT '采购订单号（冗余字段）',
  `return_time` datetime NULL COMMENT '退货时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_product_price` decimal(20,2) NULL COMMENT '合计产品价格，单位：元',
  `total_tax_price` decimal(20,2) NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(20,2) NULL COMMENT '优惠率，百分比',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(20,2) NULL COMMENT '其它金额，单位：元',
  `total_price` decimal(20,2) NULL COMMENT '最终合计价格，单位：元',
  `refund_price` decimal(20,2) NULL COMMENT '已退款金额，单位：元',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件地址',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购退货 DO
 *';

-- ----------------------------
-- Table structure for erp_purchase_order_items
-- ----------------------------
DROP TABLE IF EXISTS `erp_purchase_order_items`;
CREATE TABLE `erp_purchase_order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `order_id` bigint NULL COMMENT '采购订单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单位单价，单位：元',
  `count` decimal(20,2) NULL COMMENT '数量',
  `tax_percent` decimal(20,2) NULL COMMENT '税率，百分比',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `in_count` decimal(20,2) NULL COMMENT '采购入库数量',
  `return_count` decimal(20,2) NULL COMMENT '采购退货数量',
  `total_price` decimal(20,2) NULL COMMENT '总价，单位：元',
  `tax_price` decimal(20,2) NULL COMMENT '税额，单位：元',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 采购订单项 DO
 *';

-- ----------------------------
-- Table structure for erp_account
-- ----------------------------
DROP TABLE IF EXISTS `erp_account`;
CREATE TABLE `erp_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '开启状态',
  `name` varchar(100) NULL DEFAULT '' COMMENT '账户名称',
  `no` varchar(50) NULL DEFAULT '' COMMENT '账户编码',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `sort` int NULL COMMENT '排序',
  `default_status` bit(1) NULL COMMENT '是否默认',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 结算账户 DO
 *';

-- ----------------------------
-- Table structure for erp_finance_payment_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_payment_item`;
CREATE TABLE `erp_finance_payment_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `biz_type` int NULL COMMENT '业务类型',
  `biz_id` bigint NULL COMMENT '业务单据编号',
  `biz_no` varchar(50) NULL DEFAULT '' COMMENT '业务单号',
  `payment_id` bigint NULL COMMENT '付款单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `total_price` decimal(20,2) NULL COMMENT '应付金额，单位：分',
  `paid_price` decimal(20,2) NULL COMMENT '已付金额，单位：分',
  `payment_price` decimal(20,2) NULL COMMENT '本次付款，单位：分',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 付款项 DO
 *';

-- ----------------------------
-- Table structure for erp_finance_receipt
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_receipt`;
CREATE TABLE `erp_finance_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `customer_id` bigint NULL COMMENT '客户编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '收款单号',
  `status` int NULL COMMENT '收款状态',
  `receipt_time` datetime NULL COMMENT '收款时间',
  `finance_user_id` bigint NULL COMMENT '财务人员编号',
  `total_price` decimal(20,2) NULL COMMENT '合计价格，单位：元',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `receipt_price` decimal(20,2) NULL COMMENT '实付金额',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 收款单 DO
 *';

-- ----------------------------
-- Table structure for erp_finance_payment
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_payment`;
CREATE TABLE `erp_finance_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `supplier_id` bigint NULL COMMENT '供应商编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '付款单号',
  `status` int NULL COMMENT '付款状态',
  `payment_time` datetime NULL COMMENT '付款时间',
  `finance_user_id` bigint NULL COMMENT '财务人员编号',
  `total_price` decimal(20,2) NULL COMMENT '合计价格，单位：元',
  `discount_price` decimal(20,2) NULL COMMENT '优惠金额，单位：元',
  `payment_price` decimal(20,2) NULL COMMENT '实付金额',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 付款单 DO
 *';

-- ----------------------------
-- Table structure for erp_finance_receipt_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_finance_receipt_item`;
CREATE TABLE `erp_finance_receipt_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `biz_type` int NULL COMMENT '业务类型',
  `biz_id` bigint NULL COMMENT '业务单据编号',
  `biz_no` varchar(50) NULL DEFAULT '' COMMENT '业务单号',
  `receipt_id` bigint NULL COMMENT '收款单编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `total_price` decimal(20,2) NULL COMMENT '应收金额，单位：分',
  `receipted_price` decimal(20,2) NULL COMMENT '已收金额，单位：分',
  `receipt_price` decimal(20,2) NULL COMMENT '本次收款，单位：分',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 收款项 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_record
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_record`;
CREATE TABLE `erp_stock_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `count` decimal(20,2) NULL COMMENT '出入库数量',
  `total_count` decimal(20,2) NULL COMMENT '总库存量',
  `biz_type` int NULL COMMENT '业务类型',
  `biz_id` bigint NULL COMMENT '业务编号',
  `biz_item_id` bigint NULL COMMENT '业务项编号',
  `biz_no` varchar(50) NULL DEFAULT '' COMMENT '业务单号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品库存明细 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_move
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_move`;
CREATE TABLE `erp_stock_move` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `to_warehouse_id` bigint NULL COMMENT '调入仓库编号',
  `from_warehouse_id` bigint NULL COMMENT '调出仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '调拨单号',
  `move_time` datetime NULL COMMENT '调拨时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 库存调拨单 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_check
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_check`;
CREATE TABLE `erp_stock_check` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '盘点单号',
  `check_time` datetime NULL COMMENT '盘点时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 库存盘点单 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_in_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_in_item`;
CREATE TABLE `erp_stock_in_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `in_id` bigint NULL COMMENT '其它入库编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单价',
  `count` decimal(20,2) NULL COMMENT '产品数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 其它入库单项 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_move_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_move_item`;
CREATE TABLE `erp_stock_move_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `move_id` bigint NULL COMMENT '库存调拨编号',
  `from_warehouse_id` bigint NULL COMMENT '调出仓库编号',
  `to_warehouse_id` bigint NULL COMMENT '调入仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单价',
  `count` decimal(20,2) NULL COMMENT '产品数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 库存调拨单项 DO
 *';

-- ----------------------------
-- Table structure for erp_warehouse
-- ----------------------------
DROP TABLE IF EXISTS `erp_warehouse`;
CREATE TABLE `erp_warehouse` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `status` int NULL COMMENT '开启状态',
  `name` varchar(100) NULL DEFAULT '' COMMENT '仓库名称',
  `address` varchar(500) NULL DEFAULT '' COMMENT '仓库地址',
  `sort` bigint NULL COMMENT '排序',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `principal` varchar(255) NULL DEFAULT '' COMMENT '负责人',
  `warehouse_price` decimal(20,2) NULL COMMENT '仓储费，单位：元',
  `truckage_price` decimal(20,2) NULL COMMENT '搬运费，单位：元',
  `default_status` bit(1) NULL COMMENT '是否默认',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 仓库 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_check_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_check_item`;
CREATE TABLE `erp_stock_check_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `check_id` bigint NULL COMMENT '库存盘点编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单价',
  `stock_count` decimal(20,2) NULL COMMENT '账面数量（当前库存）',
  `actual_count` decimal(20,2) NULL COMMENT '实际数量（实际库存）',
  `count` decimal(20,2) NULL COMMENT '盈亏数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 库存盘点单项 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_out_item
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_out_item`;
CREATE TABLE `erp_stock_out_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_unit_id` bigint NULL COMMENT '产品单位编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `out_id` bigint NULL COMMENT '其它出库编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_price` decimal(20,2) NULL COMMENT '产品单价',
  `count` decimal(20,2) NULL COMMENT '产品数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 其它出库单项 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_out
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_out`;
CREATE TABLE `erp_stock_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `customer_id` bigint NULL COMMENT '客户编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '出库单号',
  `out_time` datetime NULL COMMENT '出库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 其它出库单 DO
 *';

-- ----------------------------
-- Table structure for erp_stock_in
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock_in`;
CREATE TABLE `erp_stock_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `account_id` bigint NULL COMMENT '结算账户编号',
  `supplier_id` bigint NULL COMMENT '供应商编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `no` varchar(50) NULL DEFAULT '' COMMENT '入库单号',
  `in_time` datetime NULL COMMENT '入库时间',
  `total_count` decimal(20,2) NULL COMMENT '合计数量',
  `total_price` decimal(20,2) NULL COMMENT '合计金额，单位：元',
  `status` int NULL COMMENT '状态',
  `remark` varchar(500) NULL DEFAULT '' COMMENT '备注',
  `file_url` varchar(500) NULL DEFAULT '' COMMENT '附件 URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 其它入库单 DO
 *';

-- ----------------------------
-- Table structure for erp_stock
-- ----------------------------
DROP TABLE IF EXISTS `erp_stock`;
CREATE TABLE `erp_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID',
  `product_id` bigint NULL COMMENT '产品编号',
  `warehouse_id` bigint NULL COMMENT '仓库编号',
  `count` decimal(20,2) NULL COMMENT '库存数量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 产品库存 DO
 *';

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产订单 DO
 *';

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