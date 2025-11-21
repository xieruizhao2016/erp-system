-- ----------------------------
-- ERP客户信息新增字段迁移脚本
-- 新增字段：收货地址、付款方式、客户要求
-- ----------------------------

-- 检查并添加收货地址字段
ALTER TABLE `erp_customer` ADD COLUMN IF NOT EXISTS `shipping_address` varchar(500) NULL DEFAULT '' COMMENT '收货地址' AFTER `bank_address`;

-- 检查并添加付款方式字段
ALTER TABLE `erp_customer` ADD COLUMN IF NOT EXISTS `payment_method` varchar(50) NULL DEFAULT '' COMMENT '付款方式' AFTER `shipping_address`;

-- 检查并添加客户要求字段
ALTER TABLE `erp_customer` ADD COLUMN IF NOT EXISTS `customer_requirements` text NULL COMMENT '客户要求' AFTER `payment_method`;

