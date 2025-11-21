-- ----------------------------
-- ERP销售订单新增字段迁移脚本
-- 新增字段：采购单号、合同编号
-- ----------------------------

-- 检查并添加采购单号字段
ALTER TABLE `erp_sale_order` ADD COLUMN IF NOT EXISTS `purchase_order_no` varchar(100) NULL DEFAULT '' COMMENT '采购单号' AFTER `discount_price`;

-- 检查并添加合同编号字段
ALTER TABLE `erp_sale_order` ADD COLUMN IF NOT EXISTS `contract_no` varchar(100) NULL DEFAULT '' COMMENT '合同编号' AFTER `purchase_order_no`;

