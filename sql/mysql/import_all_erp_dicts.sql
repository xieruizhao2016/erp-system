-- ========================================
-- 导入所有ERP相关字典数据
-- 创建时间: 2025-12-09
-- 说明: 导入仓库类型、产品类型、客户付款方式、生产管理相关字典
-- ========================================

SET NAMES utf8mb4;

-- 先导入产品类型和仓库类型字典
SOURCE /tmp/erp_product_warehouse_type_dict.sql;

-- 导入客户付款方式字典
SOURCE /tmp/erp_customer_payment_method_dict.sql;

-- 导入生产管理字典
SOURCE /tmp/erp_production_dict.sql;

