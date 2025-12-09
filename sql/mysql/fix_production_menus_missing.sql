-- ========================================
-- 修复生产管理模块缺失的菜单
-- 说明: 补充生产管理下缺失的所有子模块
-- ========================================

SET NAMES utf8mb4;
SET @production_parent_id = 5042;

-- 确保生产管理菜单存在且可见
UPDATE system_menu SET 
    visible = b'1', 
    status = 0, 
    deleted = b'0',
    name = '生产管理'
WHERE id = 5042;

-- ========== 生产订单 (5049) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5049, '生产订单', '', 2, 1, @production_parent_id, 'production-order', 'ep:document', 'erp/productionorder/index', 'ProductionOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5049);

-- 生产订单按钮权限
SET @production_order_id = 5049;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '生产订单查询', 'erp:production-order:query', 3, 1, @production_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-order:query' AND parent_id = @production_order_id AND deleted = 0)
UNION ALL
SELECT '生产订单创建', 'erp:production-order:create', 3, 2, @production_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-order:create' AND parent_id = @production_order_id AND deleted = 0)
UNION ALL
SELECT '生产订单更新', 'erp:production-order:update', 3, 3, @production_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-order:update' AND parent_id = @production_order_id AND deleted = 0)
UNION ALL
SELECT '生产订单删除', 'erp:production-order:delete', 3, 4, @production_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-order:delete' AND parent_id = @production_order_id AND deleted = 0)
UNION ALL
SELECT '生产订单导出', 'erp:production-order:export', 3, 5, @production_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-order:export' AND parent_id = @production_order_id AND deleted = 0);

-- ========== 产品BOM (5050) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5050, '产品BOM', '', 2, 2, @production_parent_id, 'product-bom', 'ep:files', 'erp/productbom/index', 'ProductBom', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5050);

SET @product_bom_id = 5050;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '产品BOM查询', 'erp:product-bom:query', 3, 1, @product_bom_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom:query' AND parent_id = @product_bom_id AND deleted = 0)
UNION ALL
SELECT '产品BOM创建', 'erp:product-bom:create', 3, 2, @product_bom_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom:create' AND parent_id = @product_bom_id AND deleted = 0)
UNION ALL
SELECT '产品BOM更新', 'erp:product-bom:update', 3, 3, @product_bom_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom:update' AND parent_id = @product_bom_id AND deleted = 0)
UNION ALL
SELECT '产品BOM删除', 'erp:product-bom:delete', 3, 4, @product_bom_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom:delete' AND parent_id = @product_bom_id AND deleted = 0)
UNION ALL
SELECT '产品BOM导出', 'erp:product-bom:export', 3, 5, @product_bom_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom:export' AND parent_id = @product_bom_id AND deleted = 0);

-- ========== BOM明细 (5051) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5051, 'BOM明细', '', 2, 3, @production_parent_id, 'product-bom-item', 'ep:list', 'erp/productbomitem/index', 'ProductBomItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5051);

SET @bom_item_id = 5051;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 'BOM明细查询', 'erp:product-bom-item:query', 3, 1, @bom_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom-item:query' AND parent_id = @bom_item_id AND deleted = 0)
UNION ALL
SELECT 'BOM明细创建', 'erp:product-bom-item:create', 3, 2, @bom_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom-item:create' AND parent_id = @bom_item_id AND deleted = 0)
UNION ALL
SELECT 'BOM明细更新', 'erp:product-bom-item:update', 3, 3, @bom_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom-item:update' AND parent_id = @bom_item_id AND deleted = 0)
UNION ALL
SELECT 'BOM明细删除', 'erp:product-bom-item:delete', 3, 4, @bom_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom-item:delete' AND parent_id = @bom_item_id AND deleted = 0)
UNION ALL
SELECT 'BOM明细导出', 'erp:product-bom-item:export', 3, 5, @bom_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-bom-item:export' AND parent_id = @bom_item_id AND deleted = 0);

-- ========== 工艺路线 (5052) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5052, '工艺路线', '', 2, 4, @production_parent_id, 'process-route', 'ep:connection', 'erp/processroute/index', 'ProcessRoute', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5052);

SET @process_route_id = 5052;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '工艺路线查询', 'erp:process-route:query', 3, 1, @process_route_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route:query' AND parent_id = @process_route_id AND deleted = 0)
UNION ALL
SELECT '工艺路线创建', 'erp:process-route:create', 3, 2, @process_route_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route:create' AND parent_id = @process_route_id AND deleted = 0)
UNION ALL
SELECT '工艺路线更新', 'erp:process-route:update', 3, 3, @process_route_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route:update' AND parent_id = @process_route_id AND deleted = 0)
UNION ALL
SELECT '工艺路线删除', 'erp:process-route:delete', 3, 4, @process_route_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route:delete' AND parent_id = @process_route_id AND deleted = 0)
UNION ALL
SELECT '工艺路线导出', 'erp:process-route:export', 3, 5, @process_route_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route:export' AND parent_id = @process_route_id AND deleted = 0);

-- ========== 工艺路线明细 (5053) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5053, '工艺路线明细', '', 2, 5, @production_parent_id, 'process-route-item', 'ep:list', 'erp/processrouteitem/index', 'ProcessRouteItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5053);

SET @process_route_item_id = 5053;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '工艺路线明细查询', 'erp:process-route-item:query', 3, 1, @process_route_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route-item:query' AND parent_id = @process_route_item_id AND deleted = 0)
UNION ALL
SELECT '工艺路线明细创建', 'erp:process-route-item:create', 3, 2, @process_route_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route-item:create' AND parent_id = @process_route_item_id AND deleted = 0)
UNION ALL
SELECT '工艺路线明细更新', 'erp:process-route-item:update', 3, 3, @process_route_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route-item:update' AND parent_id = @process_route_item_id AND deleted = 0)
UNION ALL
SELECT '工艺路线明细删除', 'erp:process-route-item:delete', 3, 4, @process_route_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route-item:delete' AND parent_id = @process_route_item_id AND deleted = 0)
UNION ALL
SELECT '工艺路线明细导出', 'erp:process-route-item:export', 3, 5, @process_route_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:process-route-item:export' AND parent_id = @process_route_item_id AND deleted = 0);

-- ========== 生产排程 (5054) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5054, '生产排程', '', 2, 6, @production_parent_id, 'production-schedule', 'ep:calendar', 'erp/productionschedule/index', 'ProductionSchedule', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5054);

SET @production_schedule_id = 5054;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '生产排程查询', 'erp:production-schedule:query', 3, 1, @production_schedule_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule:query' AND parent_id = @production_schedule_id AND deleted = 0)
UNION ALL
SELECT '生产排程创建', 'erp:production-schedule:create', 3, 2, @production_schedule_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule:create' AND parent_id = @production_schedule_id AND deleted = 0)
UNION ALL
SELECT '生产排程更新', 'erp:production-schedule:update', 3, 3, @production_schedule_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule:update' AND parent_id = @production_schedule_id AND deleted = 0)
UNION ALL
SELECT '生产排程删除', 'erp:production-schedule:delete', 3, 4, @production_schedule_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule:delete' AND parent_id = @production_schedule_id AND deleted = 0)
UNION ALL
SELECT '生产排程导出', 'erp:production-schedule:export', 3, 5, @production_schedule_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule:export' AND parent_id = @production_schedule_id AND deleted = 0);

-- ========== 排程明细 (5055) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5055, '排程明细', '', 2, 7, @production_parent_id, 'production-schedule-item', 'ep:list', 'erp/productionscheduleitem/index', 'ProductionScheduleItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5055);

SET @schedule_item_id = 5055;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '排程明细查询', 'erp:production-schedule-item:query', 3, 1, @schedule_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule-item:query' AND parent_id = @schedule_item_id AND deleted = 0)
UNION ALL
SELECT '排程明细创建', 'erp:production-schedule-item:create', 3, 2, @schedule_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule-item:create' AND parent_id = @schedule_item_id AND deleted = 0)
UNION ALL
SELECT '排程明细更新', 'erp:production-schedule-item:update', 3, 3, @schedule_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule-item:update' AND parent_id = @schedule_item_id AND deleted = 0)
UNION ALL
SELECT '排程明细删除', 'erp:production-schedule-item:delete', 3, 4, @schedule_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule-item:delete' AND parent_id = @schedule_item_id AND deleted = 0)
UNION ALL
SELECT '排程明细导出', 'erp:production-schedule-item:export', 3, 5, @schedule_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-schedule-item:export' AND parent_id = @schedule_item_id AND deleted = 0);

-- ========== 工单 (5056) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5056, '工单', '', 2, 8, @production_parent_id, 'work-order', 'ep:document', 'erp/workorder/index', 'WorkOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5056);

SET @work_order_id = 5056;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '工单查询', 'erp:work-order:query', 3, 1, @work_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order:query' AND parent_id = @work_order_id AND deleted = 0)
UNION ALL
SELECT '工单创建', 'erp:work-order:create', 3, 2, @work_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order:create' AND parent_id = @work_order_id AND deleted = 0)
UNION ALL
SELECT '工单更新', 'erp:work-order:update', 3, 3, @work_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order:update' AND parent_id = @work_order_id AND deleted = 0)
UNION ALL
SELECT '工单删除', 'erp:work-order:delete', 3, 4, @work_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order:delete' AND parent_id = @work_order_id AND deleted = 0)
UNION ALL
SELECT '工单导出', 'erp:work-order:export', 3, 5, @work_order_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order:export' AND parent_id = @work_order_id AND deleted = 0);

-- ========== 工单进度 (5057) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5057, '工单进度', '', 2, 9, @production_parent_id, 'work-order-progress', 'ep:time', 'erp/workorderprogress/index', 'WorkOrderProgress', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5057);

SET @work_order_progress_id = 5057;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '工单进度查询', 'erp:work-order-progress:query', 3, 1, @work_order_progress_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order-progress:query' AND parent_id = @work_order_progress_id AND deleted = 0)
UNION ALL
SELECT '工单进度创建', 'erp:work-order-progress:create', 3, 2, @work_order_progress_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order-progress:create' AND parent_id = @work_order_progress_id AND deleted = 0)
UNION ALL
SELECT '工单进度更新', 'erp:work-order-progress:update', 3, 3, @work_order_progress_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order-progress:update' AND parent_id = @work_order_progress_id AND deleted = 0)
UNION ALL
SELECT '工单进度删除', 'erp:work-order-progress:delete', 3, 4, @work_order_progress_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order-progress:delete' AND parent_id = @work_order_progress_id AND deleted = 0)
UNION ALL
SELECT '工单进度导出', 'erp:work-order-progress:export', 3, 5, @work_order_progress_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-order-progress:export' AND parent_id = @work_order_progress_id AND deleted = 0);

-- ========== 质检标准 (5058) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5058, '质检标准', '', 2, 10, @production_parent_id, 'quality-standard', 'ep:star', 'erp/qualitystandard/index', 'QualityStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5058);

SET @quality_standard_id = 5058;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '质检标准查询', 'erp:quality-standard:query', 3, 1, @quality_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-standard:query' AND parent_id = @quality_standard_id AND deleted = 0)
UNION ALL
SELECT '质检标准创建', 'erp:quality-standard:create', 3, 2, @quality_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-standard:create' AND parent_id = @quality_standard_id AND deleted = 0)
UNION ALL
SELECT '质检标准更新', 'erp:quality-standard:update', 3, 3, @quality_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-standard:update' AND parent_id = @quality_standard_id AND deleted = 0)
UNION ALL
SELECT '质检标准删除', 'erp:quality-standard:delete', 3, 4, @quality_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-standard:delete' AND parent_id = @quality_standard_id AND deleted = 0)
UNION ALL
SELECT '质检标准导出', 'erp:quality-standard:export', 3, 5, @quality_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-standard:export' AND parent_id = @quality_standard_id AND deleted = 0);

-- ========== 质检项目 (5059) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5059, '质检项目', '', 2, 11, @production_parent_id, 'quality-item', 'ep:list', 'erp/qualityitem/index', 'QualityItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5059);

SET @quality_item_id = 5059;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '质检项目查询', 'erp:quality-item:query', 3, 1, @quality_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-item:query' AND parent_id = @quality_item_id AND deleted = 0)
UNION ALL
SELECT '质检项目创建', 'erp:quality-item:create', 3, 2, @quality_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-item:create' AND parent_id = @quality_item_id AND deleted = 0)
UNION ALL
SELECT '质检项目更新', 'erp:quality-item:update', 3, 3, @quality_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-item:update' AND parent_id = @quality_item_id AND deleted = 0)
UNION ALL
SELECT '质检项目删除', 'erp:quality-item:delete', 3, 4, @quality_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-item:delete' AND parent_id = @quality_item_id AND deleted = 0)
UNION ALL
SELECT '质检项目导出', 'erp:quality-item:export', 3, 5, @quality_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-item:export' AND parent_id = @quality_item_id AND deleted = 0);

-- ========== 质检记录 (5060) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5060, '质检记录', '', 2, 12, @production_parent_id, 'quality-inspection', 'ep:document', 'erp/qualityinspection/index', 'QualityInspection', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5060);

SET @quality_inspection_id = 5060;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '质检记录查询', 'erp:quality-inspection:query', 3, 1, @quality_inspection_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection:query' AND parent_id = @quality_inspection_id AND deleted = 0)
UNION ALL
SELECT '质检记录创建', 'erp:quality-inspection:create', 3, 2, @quality_inspection_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection:create' AND parent_id = @quality_inspection_id AND deleted = 0)
UNION ALL
SELECT '质检记录更新', 'erp:quality-inspection:update', 3, 3, @quality_inspection_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection:update' AND parent_id = @quality_inspection_id AND deleted = 0)
UNION ALL
SELECT '质检记录删除', 'erp:quality-inspection:delete', 3, 4, @quality_inspection_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection:delete' AND parent_id = @quality_inspection_id AND deleted = 0)
UNION ALL
SELECT '质检记录导出', 'erp:quality-inspection:export', 3, 5, @quality_inspection_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection:export' AND parent_id = @quality_inspection_id AND deleted = 0);

-- ========== 质检明细 (5061) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5061, '质检明细', '', 2, 13, @production_parent_id, 'quality-inspection-item', 'ep:list', 'erp/qualityinspectionitem/index', 'QualityInspectionItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5061);

SET @quality_inspection_item_id = 5061;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '质检明细查询', 'erp:quality-inspection-item:query', 3, 1, @quality_inspection_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection-item:query' AND parent_id = @quality_inspection_item_id AND deleted = 0)
UNION ALL
SELECT '质检明细创建', 'erp:quality-inspection-item:create', 3, 2, @quality_inspection_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection-item:create' AND parent_id = @quality_inspection_item_id AND deleted = 0)
UNION ALL
SELECT '质检明细更新', 'erp:quality-inspection-item:update', 3, 3, @quality_inspection_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection-item:update' AND parent_id = @quality_inspection_item_id AND deleted = 0)
UNION ALL
SELECT '质检明细删除', 'erp:quality-inspection-item:delete', 3, 4, @quality_inspection_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection-item:delete' AND parent_id = @quality_inspection_item_id AND deleted = 0)
UNION ALL
SELECT '质检明细导出', 'erp:quality-inspection-item:export', 3, 5, @quality_inspection_item_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:quality-inspection-item:export' AND parent_id = @quality_inspection_item_id AND deleted = 0);

-- ========== 标准成本 (5062) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5062, '标准成本', '', 2, 14, @production_parent_id, 'cost-standard', 'ep:money', 'erp/coststandard/index', 'CostStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5062);

SET @cost_standard_id = 5062;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '标准成本查询', 'erp:cost-standard:query', 3, 1, @cost_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-standard:query' AND parent_id = @cost_standard_id AND deleted = 0)
UNION ALL
SELECT '标准成本创建', 'erp:cost-standard:create', 3, 2, @cost_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-standard:create' AND parent_id = @cost_standard_id AND deleted = 0)
UNION ALL
SELECT '标准成本更新', 'erp:cost-standard:update', 3, 3, @cost_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-standard:update' AND parent_id = @cost_standard_id AND deleted = 0)
UNION ALL
SELECT '标准成本删除', 'erp:cost-standard:delete', 3, 4, @cost_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-standard:delete' AND parent_id = @cost_standard_id AND deleted = 0)
UNION ALL
SELECT '标准成本导出', 'erp:cost-standard:export', 3, 5, @cost_standard_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-standard:export' AND parent_id = @cost_standard_id AND deleted = 0);

-- ========== 实际成本 (5063) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5063, '实际成本', '', 2, 15, @production_parent_id, 'cost-actual', 'ep:money', 'erp/costactual/index', 'CostActual', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5063);

SET @cost_actual_id = 5063;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '实际成本查询', 'erp:cost-actual:query', 3, 1, @cost_actual_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-actual:query' AND parent_id = @cost_actual_id AND deleted = 0)
UNION ALL
SELECT '实际成本创建', 'erp:cost-actual:create', 3, 2, @cost_actual_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-actual:create' AND parent_id = @cost_actual_id AND deleted = 0)
UNION ALL
SELECT '实际成本更新', 'erp:cost-actual:update', 3, 3, @cost_actual_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-actual:update' AND parent_id = @cost_actual_id AND deleted = 0)
UNION ALL
SELECT '实际成本删除', 'erp:cost-actual:delete', 3, 4, @cost_actual_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-actual:delete' AND parent_id = @cost_actual_id AND deleted = 0)
UNION ALL
SELECT '实际成本导出', 'erp:cost-actual:export', 3, 5, @cost_actual_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-actual:export' AND parent_id = @cost_actual_id AND deleted = 0);

-- ========== 成本差异 (5064) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5064, '成本差异', '', 2, 16, @production_parent_id, 'cost-variance', 'ep:money', 'erp/costvariance/index', 'CostVariance', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5064);

SET @cost_variance_id = 5064;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '成本差异查询', 'erp:cost-variance:query', 3, 1, @cost_variance_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-variance:query' AND parent_id = @cost_variance_id AND deleted = 0)
UNION ALL
SELECT '成本差异创建', 'erp:cost-variance:create', 3, 2, @cost_variance_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-variance:create' AND parent_id = @cost_variance_id AND deleted = 0)
UNION ALL
SELECT '成本差异更新', 'erp:cost-variance:update', 3, 3, @cost_variance_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-variance:update' AND parent_id = @cost_variance_id AND deleted = 0)
UNION ALL
SELECT '成本差异删除', 'erp:cost-variance:delete', 3, 4, @cost_variance_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-variance:delete' AND parent_id = @cost_variance_id AND deleted = 0)
UNION ALL
SELECT '成本差异导出', 'erp:cost-variance:export', 3, 5, @cost_variance_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:cost-variance:export' AND parent_id = @cost_variance_id AND deleted = 0);

-- ========== 工时统计 (5065) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5065, '工时统计', '', 2, 17, @production_parent_id, 'work-hours', 'ep:time', 'erp/workhours/index', 'WorkHours', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5065);

SET @work_hours_id = 5065;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '工时统计查询', 'erp:work-hours:query', 3, 1, @work_hours_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-hours:query' AND parent_id = @work_hours_id AND deleted = 0)
UNION ALL
SELECT '工时统计创建', 'erp:work-hours:create', 3, 2, @work_hours_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-hours:create' AND parent_id = @work_hours_id AND deleted = 0)
UNION ALL
SELECT '工时统计更新', 'erp:work-hours:update', 3, 3, @work_hours_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-hours:update' AND parent_id = @work_hours_id AND deleted = 0)
UNION ALL
SELECT '工时统计删除', 'erp:work-hours:delete', 3, 4, @work_hours_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-hours:delete' AND parent_id = @work_hours_id AND deleted = 0)
UNION ALL
SELECT '工时统计导出', 'erp:work-hours:export', 3, 5, @work_hours_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:work-hours:export' AND parent_id = @work_hours_id AND deleted = 0);

-- ========== 生产KPI (5070) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5070, '生产KPI', '', 2, 18, @production_parent_id, 'production-kpi', 'ep:data-line', 'erp/productionkpi/index', 'ProductionKpi', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5070);

SET @production_kpi_id = 5070;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '生产KPI查询', 'erp:production-kpi:query', 3, 1, @production_kpi_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-kpi:query' AND parent_id = @production_kpi_id AND deleted = 0)
UNION ALL
SELECT '生产KPI创建', 'erp:production-kpi:create', 3, 2, @production_kpi_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-kpi:create' AND parent_id = @production_kpi_id AND deleted = 0)
UNION ALL
SELECT '生产KPI更新', 'erp:production-kpi:update', 3, 3, @production_kpi_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-kpi:update' AND parent_id = @production_kpi_id AND deleted = 0)
UNION ALL
SELECT '生产KPI删除', 'erp:production-kpi:delete', 3, 4, @production_kpi_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-kpi:delete' AND parent_id = @production_kpi_id AND deleted = 0)
UNION ALL
SELECT '生产KPI导出', 'erp:production-kpi:export', 3, 5, @production_kpi_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-kpi:export' AND parent_id = @production_kpi_id AND deleted = 0);

-- ========== 生产报表 (5071) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5071, '生产报表', '', 2, 19, @production_parent_id, 'production-report', 'ep:document', 'erp/productionreport/index', 'ProductionReport', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5071);

SET @production_report_id = 5071;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '生产报表查询', 'erp:production-report:query', 3, 1, @production_report_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-report:query' AND parent_id = @production_report_id AND deleted = 0)
UNION ALL
SELECT '生产报表创建', 'erp:production-report:create', 3, 2, @production_report_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-report:create' AND parent_id = @production_report_id AND deleted = 0)
UNION ALL
SELECT '生产报表更新', 'erp:production-report:update', 3, 3, @production_report_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-report:update' AND parent_id = @production_report_id AND deleted = 0)
UNION ALL
SELECT '生产报表删除', 'erp:production-report:delete', 3, 4, @production_report_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-report:delete' AND parent_id = @production_report_id AND deleted = 0)
UNION ALL
SELECT '生产报表导出', 'erp:production-report:export', 3, 5, @production_report_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-report:export' AND parent_id = @production_report_id AND deleted = 0);

-- ========== 看板配置 (5072) ==========
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5072, '看板配置', '', 2, 20, @production_parent_id, 'production-dashboard-config', 'ep:setting', 'erp/productiondashboardconfig/index', 'ProductionDashboardConfig', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5072);

SET @dashboard_config_id = 5072;
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT '看板配置查询', 'erp:production-dashboard-config:query', 3, 1, @dashboard_config_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-dashboard-config:query' AND parent_id = @dashboard_config_id AND deleted = 0)
UNION ALL
SELECT '看板配置创建', 'erp:production-dashboard-config:create', 3, 2, @dashboard_config_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-dashboard-config:create' AND parent_id = @dashboard_config_id AND deleted = 0)
UNION ALL
SELECT '看板配置更新', 'erp:production-dashboard-config:update', 3, 3, @dashboard_config_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-dashboard-config:update' AND parent_id = @dashboard_config_id AND deleted = 0)
UNION ALL
SELECT '看板配置删除', 'erp:production-dashboard-config:delete', 3, 4, @dashboard_config_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-dashboard-config:delete' AND parent_id = @dashboard_config_id AND deleted = 0)
UNION ALL
SELECT '看板配置导出', 'erp:production-dashboard-config:export', 3, 5, @dashboard_config_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:production-dashboard-config:export' AND parent_id = @dashboard_config_id AND deleted = 0);

-- ========================================
-- 验证插入结果
-- ========================================
SELECT 
    id,
    name,
    path,
    component,
    sort,
    parent_id
FROM system_menu 
WHERE parent_id = 5042 
AND type = 2
AND deleted = 0
ORDER BY sort;

