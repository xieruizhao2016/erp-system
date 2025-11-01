-- ERP 模块完整表结构
-- 基于 ruoyi-vue-pro v2025.10 版本
-- 生成时间：2025-11-01

-- ========== 产品模块 ==========

-- 产品分类表
CREATE TABLE IF NOT EXISTS `erp_product_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类编号',
  `parent_id` bigint NOT NULL COMMENT '父分类编号',
  `name` varchar(255) NOT NULL COMMENT '分类名称',
  `code` varchar(50) NOT NULL COMMENT '分类编码',
  `sort` int NOT NULL COMMENT '分类排序',
  `status` tinyint NOT NULL COMMENT '开启状态',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 产品分类';

-- 产品单位表
CREATE TABLE IF NOT EXISTS `erp_product_unit` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '单位编号',
  `name` varchar(255) NOT NULL COMMENT '单位名称',
  `status` tinyint NOT NULL COMMENT '单位状态',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 产品单位表';

-- 产品表
CREATE TABLE IF NOT EXISTS `erp_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品编号',
  `name` varchar(255) NOT NULL COMMENT '产品名称',
  `bar_code` varchar(50) DEFAULT NULL COMMENT '产品条码',
  `category_id` bigint NOT NULL COMMENT '产品分类编号',
  `unit_id` bigint NOT NULL COMMENT '单位编号',
  `status` tinyint NOT NULL COMMENT '产品状态',
  `standard` varchar(255) DEFAULT NULL COMMENT '产品规格',
  `remark` varchar(1024) DEFAULT NULL COMMENT '产品备注',
  `expiry_day` int DEFAULT NULL COMMENT '保质期天数',
  `weight` decimal(24,6) DEFAULT NULL COMMENT '基础重量（kg）',
  `purchase_price` decimal(24,2) DEFAULT NULL COMMENT '采购价格，单位：元',
  `sale_price` decimal(24,2) DEFAULT NULL COMMENT '销售价格，单位：元',
  `min_price` decimal(24,2) DEFAULT NULL COMMENT '最低价格，单位：元',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 产品表';

-- ========== 仓库模块 ==========

-- 仓库表
CREATE TABLE IF NOT EXISTS `erp_warehouse` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '仓库编号',
  `name` varchar(255) NOT NULL COMMENT '仓库名称',
  `address` varchar(255) DEFAULT NULL COMMENT '仓库地址',
  `sort` int NOT NULL COMMENT '仓库排序',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `principal` varchar(50) DEFAULT NULL COMMENT '负责人',
  `warehouse_price` decimal(24,2) DEFAULT NULL COMMENT '仓储费，单位：元',
  `truckage` decimal(24,2) DEFAULT NULL COMMENT '搬运费，单位：元',
  `status` tinyint NOT NULL COMMENT '开启状态',
  `default_status` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否默认',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 仓库表';

-- ========== 库存模块 ==========

-- 产品库存表
CREATE TABLE IF NOT EXISTS `erp_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `count` decimal(24,6) NOT NULL DEFAULT '0.000000' COMMENT '库存数量',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_warehouse` (`product_id`, `warehouse_id`, `deleted`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 产品库存表';

-- 库存流水记录表
CREATE TABLE IF NOT EXISTS `erp_stock_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `count` decimal(24,6) NOT NULL COMMENT '出入库数量',
  `total_count` decimal(24,6) NOT NULL COMMENT '总库存数量',
  `biz_type` tinyint NOT NULL COMMENT '业务类型',
  `biz_id` bigint NOT NULL COMMENT '业务编号',
  `biz_item_id` bigint DEFAULT NULL COMMENT '业务项编号',
  `biz_no` varchar(255) NOT NULL COMMENT '业务单号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 产品库存流水表';

-- 其它入库单表
CREATE TABLE IF NOT EXISTS `erp_stock_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '入库编号',
  `no` varchar(255) DEFAULT NULL COMMENT '入库单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号',
  `in_time` datetime DEFAULT NULL COMMENT '入库时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '合计金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 其它入库单表';

-- 其它入库单子表
CREATE TABLE IF NOT EXISTS `erp_stock_in_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '入库项编号',
  `in_id` bigint NOT NULL COMMENT '入库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) DEFAULT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '总价，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 其它入库单子表';

-- 其它出库单表
CREATE TABLE IF NOT EXISTS `erp_stock_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库编号',
  `no` varchar(255) DEFAULT NULL COMMENT '出库单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `out_time` datetime DEFAULT NULL COMMENT '出库时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '合计金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 其它出库单表';

-- 其它出库单子表
CREATE TABLE IF NOT EXISTS `erp_stock_out_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出库项编号',
  `out_id` bigint NOT NULL COMMENT '出库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) DEFAULT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '总价，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 其它出库单子表';

-- 调拨单表
CREATE TABLE IF NOT EXISTS `erp_stock_move` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '调拨编号',
  `no` varchar(255) DEFAULT NULL COMMENT '调拨单号',
  `move_time` datetime DEFAULT NULL COMMENT '调拨时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '合计金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 库存调拨单表';

-- 调拨单子表
CREATE TABLE IF NOT EXISTS `erp_stock_move_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '调拨项编号',
  `move_id` bigint NOT NULL COMMENT '调拨编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `from_warehouse_id` bigint NOT NULL COMMENT '调出仓库编号',
  `to_warehouse_id` bigint NOT NULL COMMENT '调入仓库编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) DEFAULT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '总价，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 库存调拨单子表';

-- 盘点单表
CREATE TABLE IF NOT EXISTS `erp_stock_check` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '盘点编号',
  `no` varchar(255) DEFAULT NULL COMMENT '盘点单号',
  `check_time` datetime DEFAULT NULL COMMENT '盘点时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '合计金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 库存盘点单表';

-- 盘点单子表
CREATE TABLE IF NOT EXISTS `erp_stock_check_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '盘点项编号',
  `check_id` bigint NOT NULL COMMENT '盘点编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `book_count` decimal(24,6) DEFAULT NULL COMMENT '账面数量',
  `real_count` decimal(24,6) DEFAULT NULL COMMENT '实际数量',
  `count` decimal(24,6) NOT NULL COMMENT '盈亏数量',
  `price` decimal(24,2) DEFAULT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '总价，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 库存盘点单子表';

-- ========== 供应商/客户模块 ==========

-- 供应商表
CREATE TABLE IF NOT EXISTS `erp_supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商编号',
  `name` varchar(255) NOT NULL COMMENT '供应商名称',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `telephone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL COMMENT '开启状态',
  `sort` int NOT NULL COMMENT '排序',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '纳税人识别号',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `bank_name` varchar(255) DEFAULT NULL COMMENT '开户行',
  `bank_account` varchar(100) DEFAULT NULL COMMENT '开户账号',
  `bank_address` varchar(255) DEFAULT NULL COMMENT '开户地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 供应商表';

-- 客户表
CREATE TABLE IF NOT EXISTS `erp_customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户编号',
  `name` varchar(255) NOT NULL COMMENT '客户名称',
  `contact` varchar(100) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(50) DEFAULT NULL COMMENT '手机号码',
  `telephone` varchar(50) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) DEFAULT NULL COMMENT '电子邮箱',
  `fax` varchar(50) DEFAULT NULL COMMENT '传真',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `status` tinyint NOT NULL COMMENT '开启状态',
  `sort` int NOT NULL COMMENT '排序',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '纳税人识别号',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `bank_name` varchar(255) DEFAULT NULL COMMENT '开户行',
  `bank_account` varchar(100) DEFAULT NULL COMMENT '开户账号',
  `bank_address` varchar(255) DEFAULT NULL COMMENT '开户地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 客户表';

-- ========== 采购模块 ==========

-- 采购订单表
CREATE TABLE IF NOT EXISTS `erp_purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单编号',
  `no` varchar(255) DEFAULT NULL COMMENT '采购订单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `deposit_price` decimal(24,2) DEFAULT NULL COMMENT '定金金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购订单表';

-- 采购订单子表
CREATE TABLE IF NOT EXISTS `erp_purchase_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单项编号',
  `order_id` bigint NOT NULL COMMENT '采购订单编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购订单子表';

-- 采购入库单表
CREATE TABLE IF NOT EXISTS `erp_purchase_in` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购入库编号',
  `no` varchar(255) DEFAULT NULL COMMENT '采购入库单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号',
  `in_time` datetime DEFAULT NULL COMMENT '入库时间',
  `order_id` bigint DEFAULT NULL COMMENT '采购订单编号',
  `order_no` varchar(255) DEFAULT NULL COMMENT '采购订单号',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(24,2) DEFAULT NULL COMMENT '其它费用，单位：元',
  `payment_price` decimal(24,2) DEFAULT NULL COMMENT '本次付款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购入库单表';

-- 采购入库单子表
CREATE TABLE IF NOT EXISTS `erp_purchase_in_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购入库项编号',
  `in_id` bigint NOT NULL COMMENT '采购入库编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `order_item_id` bigint DEFAULT NULL COMMENT '采购订单项编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购入库单子表';

-- 采购退货单表
CREATE TABLE IF NOT EXISTS `erp_purchase_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购退货编号',
  `no` varchar(255) DEFAULT NULL COMMENT '采购退货单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号',
  `return_time` datetime DEFAULT NULL COMMENT '退货时间',
  `in_id` bigint DEFAULT NULL COMMENT '采购入库编号',
  `in_no` varchar(255) DEFAULT NULL COMMENT '采购入库单号',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(24,2) DEFAULT NULL COMMENT '其它费用，单位：元',
  `refund_price` decimal(24,2) DEFAULT NULL COMMENT '本次退款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购退货单表';

-- 采购退货单子表
CREATE TABLE IF NOT EXISTS `erp_purchase_return_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购退货项编号',
  `return_id` bigint NOT NULL COMMENT '采购退货编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `in_item_id` bigint DEFAULT NULL COMMENT '采购入库项编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 采购退货单子表';

-- ========== 销售模块 ==========

-- 销售订单表
CREATE TABLE IF NOT EXISTS `erp_sale_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售订单编号',
  `no` varchar(255) DEFAULT NULL COMMENT '销售订单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `deposit_price` decimal(24,2) DEFAULT NULL COMMENT '定金金额，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售订单表';

-- 销售订单子表
CREATE TABLE IF NOT EXISTS `erp_sale_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售订单项编号',
  `order_id` bigint NOT NULL COMMENT '销售订单编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售订单子表';

-- 销售出库单表
CREATE TABLE IF NOT EXISTS `erp_sale_out` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售出库编号',
  `no` varchar(255) DEFAULT NULL COMMENT '销售出库单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `out_time` datetime DEFAULT NULL COMMENT '出库时间',
  `order_id` bigint DEFAULT NULL COMMENT '销售订单编号',
  `order_no` varchar(255) DEFAULT NULL COMMENT '销售订单号',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(24,2) DEFAULT NULL COMMENT '其它费用，单位：元',
  `receipt_price` decimal(24,2) DEFAULT NULL COMMENT '本次收款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售出库单表';

-- 销售出库单子表
CREATE TABLE IF NOT EXISTS `erp_sale_out_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售出库项编号',
  `out_id` bigint NOT NULL COMMENT '销售出库编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `order_item_id` bigint DEFAULT NULL COMMENT '销售订单项编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售出库单子表';

-- 销售退货单表
CREATE TABLE IF NOT EXISTS `erp_sale_return` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售退货编号',
  `no` varchar(255) DEFAULT NULL COMMENT '销售退货单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `return_time` datetime DEFAULT NULL COMMENT '退货时间',
  `out_id` bigint DEFAULT NULL COMMENT '销售出库编号',
  `out_no` varchar(255) DEFAULT NULL COMMENT '销售出库单号',
  `total_count` decimal(24,6) DEFAULT NULL COMMENT '合计数量',
  `total_price` decimal(24,2) DEFAULT NULL COMMENT '最终合计价格，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '合计税额，单位：元',
  `discount_percent` decimal(24,6) DEFAULT NULL COMMENT '优惠率',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `other_price` decimal(24,2) DEFAULT NULL COMMENT '其它费用，单位：元',
  `refund_price` decimal(24,2) DEFAULT NULL COMMENT '本次退款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售退货单表';

-- 销售退货单子表
CREATE TABLE IF NOT EXISTS `erp_sale_return_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售退货项编号',
  `return_id` bigint NOT NULL COMMENT '销售退货编号',
  `warehouse_id` bigint NOT NULL COMMENT '仓库编号',
  `product_id` bigint NOT NULL COMMENT '产品编号',
  `count` decimal(24,6) NOT NULL COMMENT '数量',
  `price` decimal(24,2) NOT NULL COMMENT '产品单价，单位：元',
  `total_price` decimal(24,2) NOT NULL COMMENT '总价，单位：元',
  `tax_percent` decimal(24,6) DEFAULT NULL COMMENT '税率',
  `tax_price` decimal(24,2) DEFAULT NULL COMMENT '税额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `out_item_id` bigint DEFAULT NULL COMMENT '销售出库项编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 销售退货单子表';

-- ========== 财务模块 ==========

-- 结算账户表
CREATE TABLE IF NOT EXISTS `erp_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '结算账户编号',
  `name` varchar(255) NOT NULL COMMENT '账户名称',
  `no` varchar(50) NOT NULL COMMENT '账户编号',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `sort` int NOT NULL COMMENT '排序',
  `default_status` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否默认',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 结算账户';

-- 付款单表
CREATE TABLE IF NOT EXISTS `erp_finance_payment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '付款编号',
  `no` varchar(255) DEFAULT NULL COMMENT '付款单号',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商编号',
  `payment_time` datetime DEFAULT NULL COMMENT '付款时间',
  `finance_user_id` bigint DEFAULT NULL COMMENT '财务人员编号',
  `total_price` decimal(24,2) NOT NULL COMMENT '合计金额，单位：元',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `payment_price` decimal(24,2) NOT NULL COMMENT '实际付款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 付款单表';

-- 付款单子表
CREATE TABLE IF NOT EXISTS `erp_finance_payment_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '付款项编号',
  `payment_id` bigint NOT NULL COMMENT '付款编号',
  `biz_type` tinyint NOT NULL COMMENT '业务类型',
  `biz_id` bigint NOT NULL COMMENT '业务编号',
  `biz_no` varchar(255) NOT NULL COMMENT '业务单号',
  `account_id` bigint NOT NULL COMMENT '结算账户编号',
  `total_price` decimal(24,2) NOT NULL COMMENT '应付金额，单位：元',
  `payment_price` decimal(24,2) NOT NULL COMMENT '实付金额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 付款单子表';

-- 收款单表
CREATE TABLE IF NOT EXISTS `erp_finance_receipt` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收款编号',
  `no` varchar(255) DEFAULT NULL COMMENT '收款单号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `receipt_time` datetime DEFAULT NULL COMMENT '收款时间',
  `finance_user_id` bigint DEFAULT NULL COMMENT '财务人员编号',
  `total_price` decimal(24,2) NOT NULL COMMENT '合计金额，单位：元',
  `discount_price` decimal(24,2) DEFAULT NULL COMMENT '优惠金额，单位：元',
  `receipt_price` decimal(24,2) NOT NULL COMMENT '实际收款，单位：元',
  `status` tinyint DEFAULT NULL COMMENT '状态',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `file_url` varchar(512) DEFAULT NULL COMMENT '附件地址',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 收款单表';

-- 收款单子表
CREATE TABLE IF NOT EXISTS `erp_finance_receipt_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收款项编号',
  `receipt_id` bigint NOT NULL COMMENT '收款编号',
  `biz_type` tinyint NOT NULL COMMENT '业务类型',
  `biz_id` bigint NOT NULL COMMENT '业务编号',
  `biz_no` varchar(255) NOT NULL COMMENT '业务单号',
  `account_id` bigint NOT NULL COMMENT '结算账户编号',
  `total_price` decimal(24,2) NOT NULL COMMENT '应收金额，单位：元',
  `receipt_price` decimal(24,2) NOT NULL COMMENT '实收金额，单位：元',
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='ERP 收款单子表';

-- ========== 完成 ==========
