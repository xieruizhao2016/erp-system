-- ========================================
-- ERP 产品类型和仓库类型字典配置
-- 创建时间: 2025-01-XX
-- 说明: 产品类型和仓库类型的系统默认字典，不可删除/修改
-- ========================================

-- 产品类型字典类型
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2030, '产品类型', 'erp_product_type', 0, '产品类型：原材料、半成品、成品（系统默认，不可删除）', '1', NOW(), '1', NOW(), b'0');

-- 产品类型字典数据（系统默认，不可删除）
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3107, 1, '原材料', '1', 'erp_product_type', 0, 'info', '', '系统默认类型，不可删除', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3108, 2, '半成品', '2', 'erp_product_type', 0, 'warning', '', '系统默认类型，不可删除', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3109, 3, '成品', '3', 'erp_product_type', 0, 'success', '', '系统默认类型，不可删除', '1', NOW(), '1', NOW(), b'0');

-- 仓库类型字典类型
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2031, '仓库类型', 'erp_warehouse_type', 0, '仓库类型：普通仓库、原材料仓、半成品仓、成品仓（系统默认，不可修改）', '1', NOW(), '1', NOW(), b'0');

-- 仓库类型字典数据（系统默认，不可修改）
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3110, 1, '普通仓库', '1', 'erp_warehouse_type', 0, 'default', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3111, 2, '原材料仓', '2', 'erp_warehouse_type', 0, 'info', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3112, 3, '半成品仓', '3', 'erp_warehouse_type', 0, 'warning', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3113, 4, '成品仓', '4', 'erp_warehouse_type', 0, 'success', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

