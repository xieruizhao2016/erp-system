-- ========================================
-- 修复设备管理菜单404问题
-- ========================================

-- 问题分析：
-- 1. 存在重复的path="equipment"导致路由冲突
-- 2. 组件路径不正确：erp/equipment/equipment/index -> 应该是 erp/equipment/index
-- 3. 菜单层级关系不正确

-- 修复1: 删除冲突的一级菜单(id=5200)
DELETE FROM system_menu 
WHERE id = 5200 
  AND name = '设备管理' 
  AND parent_id = 2563 
  AND deleted = 0;

-- 修复2: 修复设备管理菜单的组件路径
UPDATE system_menu 
SET component = 'erp/equipment/index',
    path = 'equipment',
    updater = 'admin',
    update_time = NOW()
WHERE id = 5064 
  AND component LIKE '%equipment%' 
  AND deleted = 0;

-- 修复3: 确保父菜单正确（生产管理）
-- 先找到生产管理父菜单
SET @parentId = (SELECT id FROM system_menu WHERE name = '生产管理' AND type = 1 AND deleted = 0 LIMIT 1);

-- 更新设备管理的父菜单ID
UPDATE system_menu 
SET parent_id = @parentId,
    updater = 'admin',
    update_time = NOW()
WHERE id = 5064 
  AND (@parentId IS NOT NULL);

-- 修复4: 修改菜单名称为"设备列表"
UPDATE system_menu 
SET name = '设备列表',
    updater = 'admin',
    update_time = NOW()
WHERE id = 5064 
  AND deleted = 0;

-- 修复5: 同时更新按钮权限名称
UPDATE system_menu 
SET name = CASE
    WHEN name = '设备管理查询' THEN '设备列表查询'
    WHEN name = '设备管理创建' THEN '设备列表创建'
    WHEN name = '设备管理更新' THEN '设备列表更新'
    WHEN name = '设备管理删除' THEN '设备列表删除'
    WHEN name = '设备管理导出' THEN '设备列表导出'
    ELSE name
  END,
  updater = 'admin',
  update_time = NOW()
WHERE parent_id = 5064 
  AND deleted = 0;

-- 验证修复结果
SELECT '=== 修复后的设备列表菜单 ===' AS info;
SELECT 
    sm.id,
    sm.name,
    sm.path,
    sm.component,
    sm.parent_id,
    sm.type,
    sm.sort,
    parent.name AS parent_name
FROM system_menu sm
LEFT JOIN system_menu parent ON sm.parent_id = parent.id
WHERE sm.name LIKE '%设备列表%'
  AND sm.deleted = 0
ORDER BY sm.id;

SELECT '=== 检查路由路径 ===' AS info;
-- 计算完整路由路径
SELECT 
    sm.id,
    sm.name,
    CONCAT('/', parent.path, '/', sm.path) AS full_path,
    sm.component
FROM system_menu sm
LEFT JOIN system_menu parent ON sm.parent_id = parent.id
WHERE sm.name = '设备列表'
  AND sm.deleted = 0;

-- ========================================
-- 修复完成！
-- 下一步：重启后端服务并重新登录前端
-- ========================================
