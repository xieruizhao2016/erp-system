-- ========================================
-- 修复设备管理菜单路径问题
-- 问题：菜单path为equipment-manage，但组件路径为erp/equipment/index，导致404
-- 原因：路由系统使用path构建URL，但使用component查找组件文件
--       如果path和component不匹配，可能导致路由无法正确解析
-- 解决：将菜单path改回equipment，与组件路径匹配
-- ========================================

-- 方法1：将path改回equipment（推荐）
-- 注意：如果一级菜单的path也是equipment，可能会有路径冲突
-- 但根据路由系统的实现，子路由的path会追加到父路由path后面
-- 所以完整路径会是 /erp/equipment/equipment，这可能不是我们想要的

-- 方法2：保持path为equipment-manage，但确保component路径正确
-- 由于component已经是正确的 erp/equipment/index，所以只需要确保path和component能够正确匹配

-- 实际上，根据路由生成代码，系统会使用component来查找组件文件
-- 所以只要component正确，路由应该能够工作
-- 但为了保持一致性，建议将path改为与component匹配的路径

-- 修复设备管理菜单的path
-- 将path从equipment-manage改为equipment（与组件路径匹配）
UPDATE `system_menu` 
SET `path` = 'equipment' 
WHERE `name` = '设备管理' 
  AND `path` = 'equipment-manage' 
  AND `component` = 'erp/equipment/index'
  AND `deleted` = 0;

-- 如果上面的更新没有影响任何行，说明菜单配置可能不同
-- 可以使用以下查询来查找正确的菜单配置
-- SELECT id, name, path, component, component_name, parent_id
-- FROM system_menu 
-- WHERE name = '设备管理' AND deleted = 0;

-- 验证修复结果
SELECT id, name, path, component, component_name, parent_id
FROM system_menu 
WHERE name = '设备管理' AND deleted = 0;

