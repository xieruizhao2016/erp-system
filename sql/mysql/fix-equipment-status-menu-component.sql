-- 修复设备状态菜单的component路径
-- 问题：菜单配置中的component路径为 erp/equipmentstatus/index，但实际文件路径为 erp/equipment/equipmentstatus/index
-- 解决：更新菜单配置中的component路径

UPDATE system_menu
SET component = 'erp/equipment/equipmentstatus/index'
WHERE component = 'erp/equipmentstatus/index' 
   OR (path = 'equipment-status' AND component LIKE '%equipmentstatus%');

-- 验证更新结果
SELECT id, name, path, component, component_name, permission
FROM system_menu
WHERE path = 'equipment-status' OR component LIKE '%equipmentstatus%'
ORDER BY id;

