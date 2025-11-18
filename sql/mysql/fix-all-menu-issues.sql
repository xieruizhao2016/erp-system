-- ========================================
-- 完整修复菜单问题（编码 + 结构）
-- ========================================

-- 1. 修复一级菜单的 parent_id
UPDATE `system_menu` 
SET `parent_id` = 2563 
WHERE `id` IN (5100, 5200, 5300) AND `deleted` = 0;

-- 2. 修复菜单名称（使用UTF-8编码）
UPDATE `system_menu` 
SET `name` = '质量管理' 
WHERE `id` = 5100 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '设备管理' 
WHERE `id` = 5200 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '成本管理' 
WHERE `id` = 5300 AND `deleted` = 0;

-- 3. 修复子菜单名称
UPDATE `system_menu` 
SET `name` = '质检标准' 
WHERE `id` = 5060 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '质检项目' 
WHERE `id` = 5061 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '质检记录' 
WHERE `id` = 5062 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '质检明细' 
WHERE `id` = 5063 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '设备管理' 
WHERE `id` = 5064 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '设备状态' 
WHERE `id` = 5065 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '标准成本' 
WHERE `id` = 5066 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '实际成本' 
WHERE `id` = 5067 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '成本差异' 
WHERE `id` = 5068 AND `deleted` = 0;

-- 4. 确保 component 路径正确
UPDATE `system_menu` 
SET `component` = 'erp/quality/qualitystandard/index' 
WHERE `id` = 5060 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityitem/index' 
WHERE `id` = 5061 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspection/index' 
WHERE `id` = 5062 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/quality/qualityinspectionitem/index' 
WHERE `id` = 5063 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipment/index' 
WHERE `id` = 5064 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipmentstatus/index' 
WHERE `id` = 5065 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/cost/coststandard/index' 
WHERE `id` = 5066 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/cost/costactual/index' 
WHERE `id` = 5067 AND `deleted` = 0;

UPDATE `system_menu` 
SET `component` = 'erp/cost/costvariance/index' 
WHERE `id` = 5068 AND `deleted` = 0;

-- 5. 验证最终结果
SELECT 
    CASE 
        WHEN parent_id = 2563 THEN '一级菜单'
        ELSE '子菜单'
    END as menu_type,
    id, 
    name, 
    parent_id, 
    component 
FROM system_menu 
WHERE id IN (5100, 5200, 5300, 5060, 5061, 5062, 5063, 5064, 5065, 5066, 5067, 5068) 
  AND deleted = 0 
ORDER BY 
    CASE WHEN parent_id = 2563 THEN 0 ELSE 1 END,
    parent_id, 
    sort;

