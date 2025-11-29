-- ========================================
-- 生产管理菜单重组：添加分类层级
-- 说明：在生产管理下添加分类菜单，将相关模块组织起来
-- 生成时间: 2025-01-27
-- ========================================

-- 生产管理父菜单ID
SET @production_parent_id = 5042;

-- ========================================
-- 第一步：创建分类菜单（作为生产管理的子菜单）
-- ========================================

-- 1. 基础数据分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6100, '基础数据', '', 1, 1, @production_parent_id, 'production-basic', 'ep:folder', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='基础数据', `sort`=1;

-- 2. 生产计划分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6101, '生产计划', '', 1, 2, @production_parent_id, 'production-plan', 'ep:calendar', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='生产计划', `sort`=2;

-- 3. 生产执行分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6102, '生产执行', '', 1, 3, @production_parent_id, 'production-execution', 'ep:operation', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='生产执行', `sort`=3;

-- 4. 质量管理分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6103, '质量管理', '', 1, 4, @production_parent_id, 'production-quality', 'ep:star', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='质量管理', `sort`=4;

-- 5. 成本管理分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6104, '成本管理', '', 1, 5, @production_parent_id, 'production-cost', 'ep:money', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='成本管理', `sort`=5;

-- 6. 统计分析分类
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6105, '统计分析', '', 1, 6, @production_parent_id, 'production-analysis', 'ep:data-line', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='统计分析', `sort`=6;

-- ========================================
-- 第二步：将现有模块移动到对应的分类下
-- ========================================

-- 基础数据分类下的模块
-- 产品BOM -> 基础数据 (6100)
SET @product_bom_id = (SELECT id FROM system_menu WHERE name = '产品BOM' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 1 WHERE `id` = @product_bom_id AND `name` = '产品BOM';

-- BOM明细 -> 基础数据 (6100)
SET @bom_item_id = (SELECT id FROM system_menu WHERE name = 'BOM明细' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 2 WHERE `id` = @bom_item_id AND `name` = 'BOM明细';

-- 工艺路线 -> 基础数据 (6100)
SET @process_route_id = (SELECT id FROM system_menu WHERE name = '工艺路线' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 3 WHERE `id` = @process_route_id AND `name` = '工艺路线';

-- 工艺路线明细 -> 基础数据 (6100)
SET @process_route_item_id = (SELECT id FROM system_menu WHERE name = '工艺路线明细' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 4 WHERE `id` = @process_route_item_id AND `name` = '工艺路线明细';

-- 设备管理 -> 基础数据 (6100)
-- 先查找设备管理的菜单ID
SET @equipment_id = (SELECT id FROM system_menu WHERE name = '设备管理' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 5 WHERE `id` = @equipment_id AND `name` = '设备管理';

-- 设备状态 -> 基础数据 (6100)
SET @equipment_status_id = (SELECT id FROM system_menu WHERE name = '设备状态' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6100, `sort` = 6 WHERE `id` = @equipment_status_id AND `name` = '设备状态';

-- 生产计划分类下的模块
-- 生产订单 -> 生产计划 (6101)
SET @production_order_id = (SELECT id FROM system_menu WHERE name = '生产订单' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6101, `sort` = 1 WHERE `id` = @production_order_id AND `name` = '生产订单';

-- MRP参数 -> 生产计划 (6101)
SET @mrp_params_id = (SELECT id FROM system_menu WHERE name = 'MRP参数' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6101, `sort` = 2 WHERE `id` = @mrp_params_id AND `name` = 'MRP参数';

-- MRP运算结果 -> 生产计划 (6101)
SET @mrp_result_id = (SELECT id FROM system_menu WHERE name = 'MRP运算结果' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6101, `sort` = 3 WHERE `id` = @mrp_result_id AND `name` = 'MRP运算结果';

-- 生产排程 -> 生产计划 (6101)
SET @production_schedule_id = (SELECT id FROM system_menu WHERE name = '生产排程' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6101, `sort` = 4 WHERE `id` = @production_schedule_id AND `name` = '生产排程';

-- 排程明细 -> 生产计划 (6101)
SET @production_schedule_item_id = (SELECT id FROM system_menu WHERE name = '排程明细' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6101, `sort` = 5 WHERE `id` = @production_schedule_item_id AND `name` = '排程明细';

-- 生产执行分类下的模块
-- 工单 -> 生产执行 (6102)
SET @work_order_id = (SELECT id FROM system_menu WHERE name = '工单' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6102, `sort` = 1 WHERE `id` = @work_order_id AND `name` = '工单';

-- 工单进度 -> 生产执行 (6102)
SET @work_order_progress_id = (SELECT id FROM system_menu WHERE name = '工单进度' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6102, `sort` = 2 WHERE `id` = @work_order_progress_id AND `name` = '工单进度';

-- 质量管理分类下的模块
-- 质检标准 -> 质量管理 (6103)
SET @quality_standard_id = (SELECT id FROM system_menu WHERE name = '质检标准' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6103, `sort` = 1 WHERE `id` = @quality_standard_id AND `name` = '质检标准';

-- 质检项目 -> 质量管理 (6103)
SET @quality_item_id = (SELECT id FROM system_menu WHERE name = '质检项目' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6103, `sort` = 2 WHERE `id` = @quality_item_id AND `name` = '质检项目';

-- 质检记录 -> 质量管理 (6103)
SET @quality_inspection_id = (SELECT id FROM system_menu WHERE name = '质检记录' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6103, `sort` = 3 WHERE `id` = @quality_inspection_id AND `name` = '质检记录';

-- 质检明细 -> 质量管理 (6103)
SET @quality_inspection_item_id = (SELECT id FROM system_menu WHERE name = '质检明细' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6103, `sort` = 4 WHERE `id` = @quality_inspection_item_id AND `name` = '质检明细';

-- 成本管理分类下的模块
-- 标准成本 -> 成本管理 (6104)
SET @cost_standard_id = (SELECT id FROM system_menu WHERE name = '标准成本' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6104, `sort` = 1 WHERE `id` = @cost_standard_id AND `name` = '标准成本';

-- 实际成本 -> 成本管理 (6104)
SET @cost_actual_id = (SELECT id FROM system_menu WHERE name = '实际成本' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6104, `sort` = 2 WHERE `id` = @cost_actual_id AND `name` = '实际成本';

-- 成本差异 -> 成本管理 (6104)
SET @cost_variance_id = (SELECT id FROM system_menu WHERE name = '成本差异' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6104, `sort` = 3 WHERE `id` = @cost_variance_id AND `name` = '成本差异';

-- 统计分析分类下的模块
-- 工时统计 -> 统计分析 (6105)
SET @work_hours_id = (SELECT id FROM system_menu WHERE name = '工时统计' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6105, `sort` = 1 WHERE `id` = @work_hours_id AND `name` = '工时统计';

-- 生产KPI -> 统计分析 (6105)
SET @production_kpi_id = (SELECT id FROM system_menu WHERE name = '生产KPI' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6105, `sort` = 2 WHERE `id` = @production_kpi_id AND `name` = '生产KPI';

-- 生产报表 -> 统计分析 (6105)
SET @production_report_id = (SELECT id FROM system_menu WHERE name = '生产报表' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6105, `sort` = 3 WHERE `id` = @production_report_id AND `name` = '生产报表';

-- 看板配置 -> 统计分析 (6105)
SET @dashboard_config_id = (SELECT id FROM system_menu WHERE name = '看板配置' AND parent_id = @production_parent_id AND deleted = 0 LIMIT 1);
UPDATE `system_menu` SET `parent_id` = 6105, `sort` = 4 WHERE `id` = @dashboard_config_id AND `name` = '看板配置';

-- ========================================
-- 第三步：验证菜单结构
-- ========================================
SELECT 
    '=== 菜单重组完成 ===' AS info;

SELECT 
    CONCAT('分类菜单已创建: ', name) AS result
FROM system_menu 
WHERE id IN (6100, 6101, 6102, 6103, 6104, 6105) 
AND deleted = 0;

SELECT 
    CONCAT('模块已移动到分类: ', name, ' -> 父菜单ID: ', parent_id) AS result
FROM system_menu 
WHERE parent_id IN (6100, 6101, 6102, 6103, 6104, 6105) 
AND type = 2 
AND deleted = 0
ORDER BY parent_id, sort;

