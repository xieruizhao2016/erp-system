-- 修复设备状态菜单组件路径
-- 将组件路径从 erp/equipmentstatus/index 更新为 erp/equipment/equipmentstatus/index

UPDATE `system_menu` 
SET `component` = 'erp/equipment/equipmentstatus/index'
WHERE `name` = '设备状态' 
  AND `component` = 'erp/equipmentstatus/index' 
  AND `deleted` = 0;

-- 验证更新结果
SELECT 
    id,
    name,
    component,
    path,
    parent_id
FROM `system_menu`
WHERE `name` = '设备状态' 
  AND `deleted` = 0;

