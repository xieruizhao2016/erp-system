-- ========================================
-- 更新菜单组件路径以匹配新的目录结构
-- 前端模块重组后，需要更新数据库中的 component 路径
-- ========================================

-- 质量管理模块
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualitystandard/index' 
WHERE `name` = '质检标准' AND `component` LIKE '%quality-standard%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityitem/index' 
WHERE `name` = '质检项目' AND `component` LIKE '%quality-item%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspection/index' 
WHERE `name` = '质检记录' AND `component` LIKE '%quality-inspection%' AND `component` NOT LIKE '%item%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspectionitem/index' 
WHERE `name` = '质检明细' AND `component` LIKE '%quality-inspection-item%' AND `deleted` = 0;

-- 设备管理模块
UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipment/index' 
WHERE `name` = '设备管理' AND `parent_id` = 5200 AND `component` LIKE '%equipment%' AND `component` NOT LIKE '%status%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipmentstatus/index' 
WHERE `name` = '设备状态' AND `component` LIKE '%equipment-status%' AND `deleted` = 0;

-- 成本管理模块
UPDATE `system_menu` 
SET `component` = 'erp/cost/coststandard/index' 
WHERE `name` = '标准成本' AND `component` LIKE '%cost-standard%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/cost/costactual/index' 
WHERE `name` = '实际成本' AND `component` LIKE '%cost-actual%' AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/cost/costvariance/index' 
WHERE `name` = '成本差异' AND `component` LIKE '%cost-variance%' AND `deleted` = 0;

-- 验证更新结果
SELECT id, name, path, component, parent_id 
FROM system_menu 
WHERE name IN ('质检标准', '质检项目', '质检记录', '质检明细', '设备管理', '设备状态', '标准成本', '实际成本', '成本差异') 
  AND deleted = 0 
ORDER BY parent_id, sort;

