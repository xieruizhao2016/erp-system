-- 为所有启用角色分配财务和库存管理菜单权限
-- 生成时间: 2025-12-12
-- 说明: 确保所有启用角色都有完整的财务和库存管理菜单权限

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 为所有启用角色分配财务菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT r.id, m.id
FROM system_role r
CROSS JOIN system_menu m
WHERE r.status = 0
  AND m.parent_id = 2645
  AND m.status = 0
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu rm
    WHERE rm.role_id = r.id AND rm.menu_id = m.id
  );

-- 为所有启用角色分配库存管理菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT r.id, m.id
FROM system_role r
CROSS JOIN system_menu m
WHERE r.status = 0
  AND m.parent_id = 2583
  AND m.status = 0
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu rm
    WHERE rm.role_id = r.id AND rm.menu_id = m.id
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 此脚本会为所有启用状态的角色，自动添加财务和库存管理菜单权限
-- 2. 包括主菜单和权限按钮
-- 3. 使用 NOT EXISTS 避免重复插入
-- 4. 执行后可通过以下SQL验证：
--    SELECT r.id, r.name, COUNT(rm.menu_id) as menu_count 
--    FROM system_role r 
--    LEFT JOIN system_role_menu rm ON r.id = rm.role_id 
--      AND rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id IN (2645, 2583))
--    WHERE r.status = 0 
--    GROUP BY r.id, r.name 
--    ORDER BY r.id;

