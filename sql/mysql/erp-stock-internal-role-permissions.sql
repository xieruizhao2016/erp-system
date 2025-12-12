-- 为所有角色分配内部出入库菜单权限
-- 生成时间: 2025-12-12
-- 说明: 为已分配库存管理菜单的角色，添加内部入库和内部出库菜单权限

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 获取内部出入库菜单ID
SET @internal_in_id = (SELECT id FROM system_menu WHERE parent_id = 2583 AND name = '内部入库' LIMIT 1);
SET @internal_out_id = (SELECT id FROM system_menu WHERE parent_id = 2583 AND name = '内部出库' LIMIT 1);

-- 为所有已分配库存管理菜单的角色，分配内部入库菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @internal_in_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @internal_in_id
  );

-- 为所有已分配库存管理菜单的角色，分配内部出库菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @internal_out_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @internal_out_id
  );

-- 同时为新菜单的权限按钮分配权限
-- 获取所有权限按钮的ID
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, sm.id
FROM system_role_menu rm
JOIN system_menu sm ON sm.parent_id IN (@internal_in_id, @internal_out_id)
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = sm.id
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 此脚本会为所有已分配库存管理菜单的角色，自动添加内部入库和内部出库菜单权限
-- 2. 包括主菜单和权限按钮
-- 3. 使用 NOT EXISTS 避免重复插入
-- 4. 执行后可通过以下SQL验证：
--    SELECT role_id, COUNT(*) as menu_count 
--    FROM system_role_menu 
--    WHERE menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583) 
--    GROUP BY role_id;

