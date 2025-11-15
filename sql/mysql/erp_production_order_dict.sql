-- 生产订单相关字典配置

-- 1. 生产订单状态字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) 
VALUES (2008, '生产订单状态', 'erp_production_order_status', 0, '生产订单状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

-- 2. 生产订单状态字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3031, 1, '待开始', '1', 'erp_production_order_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3032, 2, '进行中', '2', 'erp_production_order_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3033, 3, '已暂停', '3', 'erp_production_order_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3034, 4, '已完成', '4', 'erp_production_order_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3035, 5, '已取消', '5', 'erp_production_order_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

-- 3. 优先级字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) 
VALUES (2009, '生产订单优先级', 'erp_production_order_priority', 0, '生产订单优先级：1-紧急，2-高，3-中，4-低', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

-- 4. 优先级字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3036, 1, '紧急', '1', 'erp_production_order_priority', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3037, 2, '高', '2', 'erp_production_order_priority', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3038, 3, '中', '3', 'erp_production_order_priority', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3039, 4, '低', '4', 'erp_production_order_priority', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

-- 5. 来源类型字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) 
VALUES (2010, '生产订单来源类型', 'erp_production_order_source_type', 0, '生产订单来源类型：1-手动创建，2-销售订单，3-库存补充', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

-- 6. 来源类型字典数据
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3040, 1, '手动创建', '1', 'erp_production_order_source_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3041, 2, '销售订单', '2', 'erp_production_order_source_type', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3042, 3, '库存补充', '3', 'erp_production_order_source_type', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

