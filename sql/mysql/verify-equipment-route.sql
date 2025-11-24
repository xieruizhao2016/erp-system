-- ========================================
-- 验证设备管理路由配置
-- ========================================

-- 1. 检查设备管理菜单配置
SELECT 
    id, 
    name, 
    path, 
    component, 
    component_name, 
    parent_id,
    (SELECT name FROM system_menu WHERE id = m.parent_id) AS parent_name,
    (SELECT path FROM system_menu WHERE id = m.parent_id) AS parent_path
FROM system_menu m
WHERE name = '设备管理' 
  AND deleted = 0;

-- 2. 检查父菜单（生产管理）配置
SELECT 
    id, 
    name, 
    path, 
    component, 
    parent_id
FROM system_menu 
WHERE id = 5042 
  AND deleted = 0;

-- 3. 预期的路由路径
-- 父菜单 path: production
-- 子菜单 path: equipment
-- 完整路由路径应该是: /erp/production/equipment
-- 组件路径: erp/equipment/index
-- 组件文件位置: views/erp/equipment/index.vue

-- 4. 如果URL显示 /erp/equipment/equipment，说明可能有另一个父菜单的path是equipment
-- 检查是否有其他菜单的path是equipment
SELECT 
    id, 
    name, 
    path, 
    component, 
    parent_id,
    type
FROM system_menu 
WHERE path = 'equipment' 
  AND deleted = 0
ORDER BY id;

