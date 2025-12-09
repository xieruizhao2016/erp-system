-- ========================================
-- 补充生产管理缺失的子模块
-- ========================================

SET NAMES utf8mb4;

-- 生产计划分类(6101)下缺失的模块
-- MRP参数 (5056)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5056, 'MRP参数', '', 2, 2, 6101, 'mrp-params', 'ep:setting', 'erp/mrpparams/index', 'MrpParams', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5056 AND name = 'MRP参数');

-- MRP运算结果 (5057)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5057, 'MRP运算结果', '', 2, 3, 6101, 'mrp-result', 'ep:data-analysis', 'erp/mrpresult/index', 'MrpResult', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5057 AND name = 'MRP运算结果');

-- 生产排程 (5054)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5054, '生产排程', '', 2, 4, 6101, 'production-schedule', 'ep:calendar', 'erp/productionschedule/index', 'ProductionSchedule', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5054 AND name = '生产排程');

-- 排程明细 (5055)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5055, '排程明细', '', 2, 5, 6101, 'production-schedule-item', 'ep:list', 'erp/productionscheduleitem/index', 'ProductionScheduleItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5055 AND name = '排程明细');

-- 生产执行分类(6102)下缺失的模块
-- 工单 (5058)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5058, '工单', '', 2, 1, 6102, 'work-order', 'ep:document', 'erp/workorder/index', 'WorkOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5058 AND name = '工单');

-- 工单进度 (5059)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5059, '工单进度', '', 2, 2, 6102, 'work-order-progress', 'ep:loading', 'erp/workorderprogress/index', 'WorkOrderProgress', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5059 AND name = '工单进度');

-- 质量管理分类(6103)下缺失的模块
-- 质检标准 (5060) - 注意：5060可能已被占用，需要检查
UPDATE system_menu SET name = '质检标准', type = 2, parent_id = 6103, path = 'quality-standard', icon = 'ep:star', component = 'erp/qualitystandard/index', component_name = 'QualityStandard', sort = 1, deleted = b'0', visible = b'1', status = 0
WHERE id = 5060 AND name != '质检标准';

INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5060, '质检标准', '', 2, 1, 6103, 'quality-standard', 'ep:star', 'erp/qualitystandard/index', 'QualityStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5060 AND name = '质检标准');

-- 质检项目 (5061) - 注意：5061可能已被占用
UPDATE system_menu SET name = '质检项目', type = 2, parent_id = 6103, path = 'quality-item', icon = 'ep:list', component = 'erp/qualityitem/index', component_name = 'QualityItem', sort = 2, deleted = b'0', visible = b'1', status = 0
WHERE id = 5061 AND name != '质检项目';

INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5061, '质检项目', '', 2, 2, 6103, 'quality-item', 'ep:list', 'erp/qualityitem/index', 'QualityItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5061 AND name = '质检项目');

-- 成本管理分类(6104)下缺失的模块
-- 标准成本 (5066)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5066, '标准成本', '', 2, 1, 6104, 'cost-standard', 'ep:money', 'erp/coststandard/index', 'CostStandard', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5066 AND name = '标准成本');

-- 统计分析分类(6105)下缺失的模块
-- 工时统计 (5069)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5069, '工时统计', '', 2, 1, 6105, 'work-hours', 'ep:time', 'erp/workhours/index', 'WorkHours', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5069 AND name = '工时统计');

-- 生产KPI (5070)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5070, '生产KPI', '', 2, 2, 6105, 'production-kpi', 'ep:data-line', 'erp/productionkpi/index', 'ProductionKpi', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5070 AND name = '生产KPI');

-- 生产报表 (5071)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5071, '生产报表', '', 2, 3, 6105, 'production-report', 'ep:document', 'erp/productionreport/index', 'ProductionReport', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5071 AND name = '生产报表');

-- 看板配置 (5072)
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5072, '看板配置', '', 2, 4, 6105, 'production-dashboard-config', 'ep:setting', 'erp/productiondashboardconfig/index', 'ProductionDashboardConfig', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5072 AND name = '看板配置');

-- 基础数据分类(6100)下缺失的模块
-- 设备管理 (5064) - 注意：5064可能已被占用
UPDATE system_menu SET name = '设备管理', type = 2, parent_id = 6100, path = 'equipment', icon = 'ep:tools', component = 'erp/equipment/index', component_name = 'Equipment', sort = 5, deleted = b'0', visible = b'1', status = 0
WHERE id = 5064 AND name != '设备管理';

INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5064, '设备管理', '', 2, 5, 6100, 'equipment', 'ep:tools', 'erp/equipment/index', 'Equipment', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5064 AND name = '设备管理');

-- 设备状态 (5065) - 注意：5065可能已被占用
UPDATE system_menu SET name = '设备状态', type = 2, parent_id = 6100, path = 'equipment-status', icon = 'ep:monitor', component = 'erp/equipmentstatus/index', component_name = 'EquipmentStatus', sort = 6, deleted = b'0', visible = b'1', status = 0
WHERE id = 5065 AND name != '设备状态';

INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
SELECT 5065, '设备状态', '', 2, 6, 6100, 'equipment-status', 'ep:monitor', 'erp/equipmentstatus/index', 'EquipmentStatus', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 5065 AND name = '设备状态');

-- 验证结果
SELECT 
    c.name as category,
    COUNT(m.id) as module_count,
    GROUP_CONCAT(m.name ORDER BY m.sort SEPARATOR ', ') as modules
FROM system_menu c
LEFT JOIN system_menu m ON m.parent_id = c.id AND m.type = 2 AND m.deleted = 0
WHERE c.id IN (6100, 6101, 6102, 6103, 6104, 6105) AND c.deleted = 0
GROUP BY c.id, c.name
ORDER BY c.sort;

