-- ----------------------------
-- 客户付款方式字典配置
-- ----------------------------
-- 新增字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2100, '客户付款方式', 'erp_customer_payment_method', 0, 'ERP客户付款方式类型', '1', NOW(), '1', NOW(), b'0', NULL);

-- 新增字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4000, 1, '现结', '现结', 'erp_customer_payment_method', 0, 'success', '', '现金结算', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4001, 2, '月结', '月结', 'erp_customer_payment_method', 0, 'primary', '', '月度结算', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4002, 3, '年结', '年结', 'erp_customer_payment_method', 0, 'warning', '', '年度结算', '1', NOW(), '1', NOW(), b'0');

