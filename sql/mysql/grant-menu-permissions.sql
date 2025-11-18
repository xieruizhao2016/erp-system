-- ========================================
-- 为所有角色授予新菜单的访问权限
-- ========================================

-- 获取所有角色ID（排除已删除的角色）
-- 为每个角色添加新菜单的权限

-- 为所有角色添加质量管理模块权限
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    5100 as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
WHERE r.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = 5100 AND rm.deleted = 0
  );

-- 为所有角色添加设备管理模块权限
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    5200 as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
WHERE r.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = 5200 AND rm.deleted = 0
  );

-- 为所有角色添加成本管理模块权限
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    5300 as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
WHERE r.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = 5300 AND rm.deleted = 0
  );

-- 为所有角色添加子菜单权限（质量管理）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    m.id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
CROSS JOIN `system_menu` m
WHERE r.deleted = 0
  AND m.parent_id = 5100
  AND m.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = m.id AND rm.deleted = 0
  );

-- 为所有角色添加子菜单权限（设备管理）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    m.id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
CROSS JOIN `system_menu` m
WHERE r.deleted = 0
  AND m.parent_id = 5200
  AND m.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = m.id AND rm.deleted = 0
  );

-- 为所有角色添加子菜单权限（成本管理）
INSERT INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 
    r.id as role_id,
    m.id as menu_id,
    '1' as creator,
    NOW() as create_time,
    '1' as updater,
    NOW() as update_time,
    0 as deleted
FROM `system_role` r
CROSS JOIN `system_menu` m
WHERE r.deleted = 0
  AND m.parent_id = 5300
  AND m.deleted = 0
  AND NOT EXISTS (
    SELECT 1 FROM `system_role_menu` rm 
    WHERE rm.role_id = r.id AND rm.menu_id = m.id AND rm.deleted = 0
  );

-- 验证权限分配结果
SELECT 
    rm.role_id,
    r.name as role_name,
    rm.menu_id,
    m.name as menu_name,
    m.parent_id
FROM system_role_menu rm
JOIN system_role r ON rm.role_id = r.id
JOIN system_menu m ON rm.menu_id = m.id
WHERE rm.menu_id IN (5100, 5200, 5300)
  AND rm.deleted = 0
  AND r.deleted = 0
  AND m.deleted = 0
ORDER BY rm.role_id, m.parent_id, m.sort;

