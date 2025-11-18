-- ========================================
-- 验证设备管理菜单配置
-- ========================================

-- 查询设备管理相关菜单配置
SELECT 
    id, 
    name, 
    path, 
    component, 
    component_name, 
    parent_id,
    CASE 
        WHEN parent_id = 0 THEN '一级菜单'
        WHEN parent_id = 2563 THEN 'ERP系统下的一级菜单'
        ELSE '子菜单'
    END AS menu_type
FROM system_menu 
WHERE (id IN (5200, 5064, 5065) OR name LIKE '%设备管理%' OR path LIKE '%equipment%')
  AND deleted = 0
ORDER BY parent_id, id;

-- 检查路由路径配置
-- 根据路由生成逻辑：
-- - 父菜单 path: equipment (ID 5200)
-- - 子菜单 path: equipment (ID 5064)
-- - 完整路由路径应该是: /erp/equipment/equipment
-- 
-- 如果访问 /erp/equipment/equipment-manage 出现404，可能是：
-- 1. 前端缓存了旧的菜单数据
-- 2. 需要清除浏览器缓存并重新登录
-- 3. 或者检查前端路由配置

