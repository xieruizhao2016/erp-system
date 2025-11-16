-- ========================================
-- ERP 生产管理模块字典配置
-- 生成时间: 2025-11-16 18:11:19
-- ========================================

-- 字典类型
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2008, '生产订单状态', 'erp_production_order_status', 0, '生产订单的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2009, '生产订单优先级', 'erp_production_order_priority', 0, '生产订单的优先级', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2010, '生产订单来源类型', 'erp_production_order_source_type', 0, '生产订单的来源类型', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2011, '工单状态', 'erp_work_order_status', 0, '工单的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2012, '工单进度状态', 'erp_work_order_progress_status', 0, '工单进度的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2013, '产品BOM状态', 'erp_product_bom_status', 0, '产品BOM的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2014, 'BOM类型', 'erp_bom_type', 0, 'BOM的类型', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2015, '工艺路线状态', 'erp_process_route_status', 0, '工艺路线的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2016, '生产排程状态', 'erp_production_schedule_status', 0, '生产排程的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2017, '排程明细状态', 'erp_production_schedule_item_status', 0, '排程明细的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2018, 'MRP订单类型', 'erp_mrp_order_type', 0, 'MRP运算结果的订单类型', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2019, 'MRP批量规则', 'erp_mrp_lot_sizing_rule', 0, 'MRP的批量规则', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2020, 'MRP订单状态', 'erp_mrp_order_status', 0, 'MRP订单的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2021, 'MRP参数类型', 'erp_mrp_param_type', 0, 'MRP参数的数据类型', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2022, '标准成本状态', 'erp_cost_standard_status', 0, '标准成本的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2023, '实际成本状态', 'erp_cost_actual_status', 0, '实际成本的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2024, '成本差异类型', 'erp_cost_variance_type', 0, '成本差异的类型', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2025, '设备状态', 'erp_equipment_status', 0, '设备的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2026, '设备状态记录', 'erp_equipment_status_record', 0, '设备状态记录的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2027, '质检状态', 'erp_quality_status', 0, '质检的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2028, '工时统计状态', 'erp_work_hours_status', 0, '工时统计的状态', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2029, '生产报表状态', 'erp_production_report_status', 0, '生产报表的状态', '1', NOW(), '1', NOW(), b'0');

-- 字典数据
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

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3036, 1, '紧急', '1', 'erp_production_order_priority', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3037, 2, '高', '2', 'erp_production_order_priority', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3038, 3, '中', '3', 'erp_production_order_priority', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3039, 4, '低', '4', 'erp_production_order_priority', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3040, 1, '手动创建', '1', 'erp_production_order_source_type', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3041, 2, '销售订单', '2', 'erp_production_order_source_type', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3042, 3, '库存补充', '3', 'erp_production_order_source_type', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3043, 1, '已创建', '1', 'erp_work_order_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3044, 2, '已下达', '2', 'erp_work_order_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3045, 3, '进行中', '3', 'erp_work_order_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3046, 4, '已暂停', '4', 'erp_work_order_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3047, 5, '已完成', '5', 'erp_work_order_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3048, 6, '已取消', '6', 'erp_work_order_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3049, 1, '待开始', '1', 'erp_work_order_progress_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3050, 2, '进行中', '2', 'erp_work_order_progress_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3051, 3, '已完成', '3', 'erp_work_order_progress_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3052, 4, '异常', '4', 'erp_work_order_progress_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3053, 1, '草稿', '1', 'erp_product_bom_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3054, 2, '生效', '2', 'erp_product_bom_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3055, 3, '失效', '3', 'erp_product_bom_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3056, 1, '生产BOM', '1', 'erp_bom_type', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3057, 2, '设计BOM', '2', 'erp_bom_type', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3058, 3, '工艺BOM', '3', 'erp_bom_type', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3059, 1, '草稿', '1', 'erp_process_route_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3060, 2, '生效', '2', 'erp_process_route_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3061, 3, '失效', '3', 'erp_process_route_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3062, 1, '草稿', '1', 'erp_production_schedule_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3063, 2, '已发布', '2', 'erp_production_schedule_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3064, 3, '执行中', '3', 'erp_production_schedule_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3065, 4, '已完成', '4', 'erp_production_schedule_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3066, 1, '已计划', '1', 'erp_production_schedule_item_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3067, 2, '已下达', '2', 'erp_production_schedule_item_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3068, 3, '进行中', '3', 'erp_production_schedule_item_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3069, 4, '已完成', '4', 'erp_production_schedule_item_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3070, 5, '已延迟', '5', 'erp_production_schedule_item_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3071, 1, '生产订单', '1', 'erp_mrp_order_type', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3072, 2, '采购订单', '2', 'erp_mrp_order_type', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3073, 1, '固定批量', '1', 'erp_mrp_lot_sizing_rule', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3074, 2, '按需', '2', 'erp_mrp_lot_sizing_rule', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3075, 3, '最小-最大', '3', 'erp_mrp_lot_sizing_rule', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3076, 1, '建议', '1', 'erp_mrp_order_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3077, 2, '确认', '2', 'erp_mrp_order_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3078, 3, '下达', '3', 'erp_mrp_order_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3079, 1, '字符串', '1', 'erp_mrp_param_type', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3080, 2, '数字', '2', 'erp_mrp_param_type', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3081, 3, '日期', '3', 'erp_mrp_param_type', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3082, 4, '布尔', '4', 'erp_mrp_param_type', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3083, 1, '草稿', '1', 'erp_cost_standard_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3084, 2, '生效', '2', 'erp_cost_standard_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3085, 3, '失效', '3', 'erp_cost_standard_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3086, 1, '草稿', '1', 'erp_cost_actual_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3087, 2, '已计算', '2', 'erp_cost_actual_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3088, 3, '已确认', '3', 'erp_cost_actual_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3089, 1, '有利', '1', 'erp_cost_variance_type', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3090, 2, '不利', '2', 'erp_cost_variance_type', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3091, 1, '正常', '1', 'erp_equipment_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3092, 2, '维修中', '2', 'erp_equipment_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3093, 3, '故障', '3', 'erp_equipment_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3094, 4, '报废', '4', 'erp_equipment_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3095, 1, '运行', '1', 'erp_equipment_status_record', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3096, 2, '待机', '2', 'erp_equipment_status_record', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3097, 3, '故障', '3', 'erp_equipment_status_record', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3098, 4, '维修', '4', 'erp_equipment_status_record', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3099, 5, '停机', '5', 'erp_equipment_status_record', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3100, 1, '待检', '1', 'erp_quality_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3101, 2, '合格', '2', 'erp_quality_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3102, 3, '不合格', '3', 'erp_quality_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3103, 1, '有效', '1', 'erp_work_hours_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3104, 2, '无效', '2', 'erp_work_hours_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3105, 1, '草稿', '1', 'erp_production_report_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3106, 2, '已发布', '2', 'erp_production_report_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');

