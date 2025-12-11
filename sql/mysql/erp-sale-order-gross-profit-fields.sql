SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 扩展销售订单表字段 - 毛利率相关
-- ----------------------------
ALTER TABLE `erp_sale_order` 
ADD COLUMN `gross_profit_rate` decimal(20,2) NULL COMMENT '毛利率（百分比）' AFTER `total_price`,
ADD COLUMN `material_cost` decimal(20,2) NULL COMMENT '原材料成本，单位：元' AFTER `gross_profit_rate`,
ADD COLUMN `labor_cost` decimal(20,2) NULL COMMENT '员工成本，单位：元' AFTER `material_cost`,
ADD COLUMN `total_cost` decimal(20,2) NULL COMMENT '总成本，单位：元（total_cost = material_cost + labor_cost）' AFTER `labor_cost`;

-- ----------------------------
-- 扩展销售订单项表字段 - 毛利率相关
-- ----------------------------
ALTER TABLE `erp_sale_order_items` 
ADD COLUMN `gross_profit_rate` decimal(20,2) NULL COMMENT '行毛利率（百分比）' AFTER `tax_price`,
ADD COLUMN `material_cost` decimal(20,2) NULL COMMENT '行原材料成本，单位：元' AFTER `gross_profit_rate`,
ADD COLUMN `labor_cost` decimal(20,2) NULL COMMENT '行员工成本，单位：元' AFTER `material_cost`;

SET FOREIGN_KEY_CHECKS = 1;

