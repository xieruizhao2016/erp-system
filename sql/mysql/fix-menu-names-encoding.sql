-- ========================================
-- 修复菜单名称编码问题
-- ========================================

-- 修复质量管理模块名称
UPDATE `system_menu` 
SET `name` = '质量管理' 
WHERE `id` = 5100 AND `deleted` = 0;

-- 修复设备管理模块名称
UPDATE `system_menu` 
SET `name` = '设备管理' 
WHERE `id` = 5200 AND `deleted` = 0;

-- 修复成本管理模块名称
UPDATE `system_menu` 
SET `name` = '成本管理' 
WHERE `id` = 5300 AND `deleted` = 0;

-- 修复质量管理下的子菜单名称
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

-- 修复设备管理下的子菜单名称
UPDATE `system_menu` 
SET `name` = '设备管理' 
WHERE `id` = 5064 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '设备状态' 
WHERE `id` = 5065 AND `deleted` = 0;

-- 修复成本管理下的子菜单名称
UPDATE `system_menu` 
SET `name` = '标准成本' 
WHERE `id` = 5066 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '实际成本' 
WHERE `id` = 5067 AND `deleted` = 0;

UPDATE `system_menu` 
SET `name` = '成本差异' 
WHERE `id` = 5068 AND `deleted` = 0;

-- 验证修复结果
SELECT id, name, parent_id, component 
FROM system_menu 
WHERE id IN (5100, 5200, 5300, 5060, 5061, 5062, 5063, 5064, 5065, 5066, 5067, 5068) 
  AND deleted = 0 
ORDER BY parent_id, sort;

