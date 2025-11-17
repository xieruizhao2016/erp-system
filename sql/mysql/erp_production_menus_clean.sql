-- ========================================
-- ERP 生产管理模块菜单配置
-- 生成时间: 2025-11-16 16:56:23
-- ========================================

-- 1. 创建生产管理目录（如果不存在）
-- 注意：如果已存在，请手动调整ID
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5042, '生产管理', '', 1, 50, 2563, 'production', 'ep:operation', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='生产管理';


-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5050, '产品BOM', '', 2, 2, 5042, 'product-bom', 'ep:files', 'erp/productbom/index', 'ProductBom', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5050;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('产品BOM查询', 'erp:product-bom:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('产品BOM创建', 'erp:product-bom:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('产品BOM更新', 'erp:product-bom:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('产品BOM删除', 'erp:product-bom:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('产品BOM导出', 'erp:product-bom:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== BOM明细 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5051, 'BOM明细', '', 2, 3, 5042, 'product-bom-item', 'ep:list', 'erp/productbomitem/index', 'ProductBomItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5051;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('BOM明细查询', 'erp:product-bom-item:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('BOM明细创建', 'erp:product-bom-item:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('BOM明细更新', 'erp:product-bom-item:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('BOM明细删除', 'erp:product-bom-item:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('BOM明细导出', 'erp:product-bom-item:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 工艺路线 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5052, '工艺路线', '', 2, 4, 5042, 'process-route', 'ep:connection', 'erp/processroute/index', 'ProcessRoute', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5052;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工艺路线查询', 'erp:process-route:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线创建', 'erp:process-route:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线更新', 'erp:process-route:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线删除', 'erp:process-route:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线导出', 'erp:process-route:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 工艺路线明细 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5053, '工艺路线明细', '', 2, 5, 5042, 'process-route-item', 'ep:list', 'erp/processrouteitem/index', 'ProcessRouteItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5053;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工艺路线明细查询', 'erp:process-route-item:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线明细创建', 'erp:process-route-item:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线明细更新', 'erp:process-route-item:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线明细删除', 'erp:process-route-item:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工艺路线明细导出', 'erp:process-route-item:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 生产排程 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5054, '生产排程', '', 2, 6, 5042, 'production-schedule', 'ep:calendar', 'erp/productionschedule/index', 'ProductionSchedule', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5054;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('生产排程查询', 'erp:production-schedule:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产排程创建', 'erp:production-schedule:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产排程更新', 'erp:production-schedule:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产排程删除', 'erp:production-schedule:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产排程导出', 'erp:production-schedule:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 排程明细 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5055, '排程明细', '', 2, 7, 5042, 'production-schedule-item', 'ep:list', 'erp/productionscheduleitem/index', 'ProductionScheduleItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5055;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('排程明细查询', 'erp:production-schedule-item:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细创建', 'erp:production-schedule-item:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细更新', 'erp:production-schedule-item:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细删除', 'erp:production-schedule-item:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细导出', 'erp:production-schedule-item:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== MRP参数 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5056, 'MRP参数', '', 2, 8, 5042, 'mrp-params', 'ep:setting', 'erp/mrpparams/index', 'MrpParams', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5056;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('MRP参数查询', 'erp:mrp-params:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP参数创建', 'erp:mrp-params:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP参数更新', 'erp:mrp-params:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP参数删除', 'erp:mrp-params:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP参数导出', 'erp:mrp-params:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== MRP运算结果 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5057, 'MRP运算结果', '', 2, 9, 5042, 'mrp-result', 'ep:data-analysis', 'erp/mrpresult/index', 'MrpResult', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5057;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('MRP运算结果查询', 'erp:mrp-result:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP运算结果创建', 'erp:mrp-result:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP运算结果更新', 'erp:mrp-result:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP运算结果删除', 'erp:mrp-result:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('MRP运算结果导出', 'erp:mrp-result:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 工单 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5058, '工单', '', 2, 10, 5042, 'work-order', 'ep:document', 'erp/workorder/index', 'WorkOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5058;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工单查询', 'erp:work-order:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单创建', 'erp:work-order:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单更新', 'erp:work-order:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单删除', 'erp:work-order:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单导出', 'erp:work-order:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 工单进度 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5059, '工单进度', '', 2, 11, 5042, 'work-order-progress', 'ep:loading', 'erp/workorderprogress/index', 'WorkOrderProgress', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5059;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工单进度查询', 'erp:work-order-progress:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单进度创建', 'erp:work-order-progress:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单进度更新', 'erp:work-order-progress:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单进度删除', 'erp:work-order-progress:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工单进度导出', 'erp:work-order-progress:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 质检标准 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5060, '质检标准', '', 2, 12, 5042, 'quality-standard', 'ep:star', 'erp/qualitystandard/index', 'QualityStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5060;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('质检标准查询', 'erp:quality-standard:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检标准创建', 'erp:quality-standard:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检标准更新', 'erp:quality-standard:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检标准删除', 'erp:quality-standard:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检标准导出', 'erp:quality-standard:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 质检项目 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5061, '质检项目', '', 2, 13, 5042, 'quality-item', 'ep:list', 'erp/qualityitem/index', 'QualityItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5061;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('质检项目查询', 'erp:quality-item:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检项目创建', 'erp:quality-item:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检项目更新', 'erp:quality-item:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检项目删除', 'erp:quality-item:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检项目导出', 'erp:quality-item:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 质检记录 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5062, '质检记录', '', 2, 14, 5042, 'quality-inspection', 'ep:edit', 'erp/qualityinspection/index', 'QualityInspection', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5062;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('质检记录查询', 'erp:quality-inspection:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检记录创建', 'erp:quality-inspection:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检记录更新', 'erp:quality-inspection:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检记录删除', 'erp:quality-inspection:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检记录导出', 'erp:quality-inspection:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 质检明细 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5063, '质检明细', '', 2, 15, 5042, 'quality-inspection-item', 'ep:list', 'erp/qualityinspectionitem/index', 'QualityInspectionItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5063;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('质检明细查询', 'erp:quality-inspection-item:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检明细创建', 'erp:quality-inspection-item:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检明细更新', 'erp:quality-inspection-item:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检明细删除', 'erp:quality-inspection-item:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('质检明细导出', 'erp:quality-inspection-item:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 设备管理 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5064, '设备管理', '', 2, 16, 5042, 'equipment', 'ep:tools', 'erp/equipment/index', 'Equipment', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5064;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('设备管理查询', 'erp:equipment:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备管理创建', 'erp:equipment:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备管理更新', 'erp:equipment:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备管理删除', 'erp:equipment:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备管理导出', 'erp:equipment:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 设备状态 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5065, '设备状态', '', 2, 17, 5042, 'equipment-status', 'ep:monitor', 'erp/equipmentstatus/index', 'EquipmentStatus', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5065;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('设备状态查询', 'erp:equipment-status:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备状态创建', 'erp:equipment-status:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备状态更新', 'erp:equipment-status:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备状态删除', 'erp:equipment-status:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('设备状态导出', 'erp:equipment-status:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 标准成本 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5066, '标准成本', '', 2, 18, 5042, 'cost-standard', 'ep:money', 'erp/coststandard/index', 'CostStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5066;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('标准成本查询', 'erp:cost-standard:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('标准成本创建', 'erp:cost-standard:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('标准成本更新', 'erp:cost-standard:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('标准成本删除', 'erp:cost-standard:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('标准成本导出', 'erp:cost-standard:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 实际成本 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5067, '实际成本', '', 2, 19, 5042, 'cost-actual', 'ep:money', 'erp/costactual/index', 'CostActual', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5067;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('实际成本查询', 'erp:cost-actual:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('实际成本创建', 'erp:cost-actual:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('实际成本更新', 'erp:cost-actual:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('实际成本删除', 'erp:cost-actual:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('实际成本导出', 'erp:cost-actual:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 成本差异 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5068, '成本差异', '', 2, 20, 5042, 'cost-variance', 'ep:data-analysis', 'erp/costvariance/index', 'CostVariance', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5068;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('成本差异查询', 'erp:cost-variance:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('成本差异创建', 'erp:cost-variance:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('成本差异更新', 'erp:cost-variance:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('成本差异删除', 'erp:cost-variance:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('成本差异导出', 'erp:cost-variance:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 工时统计 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5069, '工时统计', '', 2, 21, 5042, 'work-hours', 'ep:time', 'erp/workhours/index', 'WorkHours', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5069;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工时统计查询', 'erp:work-hours:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工时统计创建', 'erp:work-hours:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工时统计更新', 'erp:work-hours:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工时统计删除', 'erp:work-hours:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工时统计导出', 'erp:work-hours:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 生产KPI ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5070, '生产KPI', '', 2, 22, 5042, 'production-kpi', 'ep:data-line', 'erp/productionkpi/index', 'ProductionKpi', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5070;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('生产KPI查询', 'erp:production-kpi:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产KPI创建', 'erp:production-kpi:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产KPI更新', 'erp:production-kpi:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产KPI删除', 'erp:production-kpi:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产KPI导出', 'erp:production-kpi:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 生产报表 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5071, '生产报表', '', 2, 23, 5042, 'production-report', 'ep:document', 'erp/productionreport/index', 'ProductionReport', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5071;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('生产报表查询', 'erp:production-report:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产报表创建', 'erp:production-report:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产报表更新', 'erp:production-report:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产报表删除', 'erp:production-report:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('生产报表导出', 'erp:production-report:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');


-- ========== 看板配置 ==========
-- 菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5072, '看板配置', '', 2, 24, 5042, 'production-dashboard-config', 'ep:setting', 'erp/productiondashboardconfig/index', 'ProductionDashboardConfig', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = 5072;
INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('看板配置查询', 'erp:production-dashboard-config:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('看板配置创建', 'erp:production-dashboard-config:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('看板配置更新', 'erp:production-dashboard-config:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('看板配置删除', 'erp:production-dashboard-config:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('看板配置导出', 'erp:production-dashboard-config:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

