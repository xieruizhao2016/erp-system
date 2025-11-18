-- ========================================
-- 根据菜单ID更新组件路径以匹配新的目录结构
-- ========================================

-- 质量管理模块 (parent_id = 5100)
-- 质检标准 (5060)
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualitystandard/index' 
WHERE `id` = 5060 AND `deleted` = 0;

-- 质检项目 (5061)
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityitem/index' 
WHERE `id` = 5061 AND `deleted` = 0;

-- 质检记录 (5062)
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspection/index' 
WHERE `id` = 5062 AND `deleted` = 0;

-- 质检明细 (5063)
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspectionitem/index' 
WHERE `id` = 5063 AND `deleted` = 0;

-- 设备管理模块 (parent_id = 5200)
-- 设备管理 (5064)
UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipment/index' 
WHERE `id` = 5064 AND `deleted` = 0;

-- 设备状态 (5065)
UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipmentstatus/index' 
WHERE `id` = 5065 AND `deleted` = 0;

-- 成本管理模块 (parent_id = 5300)
-- 标准成本 (5066)
UPDATE `system_menu` 
SET `component` = 'erp/cost/coststandard/index' 
WHERE `id` = 5066 AND `deleted` = 0;

-- 实际成本 (5067)
UPDATE `system_menu` 
SET `component` = 'erp/cost/costactual/index' 
WHERE `id` = 5067 AND `deleted` = 0;

-- 成本差异 (5068)
UPDATE `system_menu` 
SET `component` = 'erp/cost/costvariance/index' 
WHERE `id` = 5068 AND `deleted` = 0;

-- 验证更新结果
SELECT id, name, path, component, parent_id 
FROM system_menu 
WHERE id IN (5060, 5061, 5062, 5063, 5064, 5065, 5066, 5067, 5068) 
  AND deleted = 0 
ORDER BY parent_id, sort;

