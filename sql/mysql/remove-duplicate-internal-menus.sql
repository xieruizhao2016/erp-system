-- 删除重复的内部入库和出库菜单
-- 问题：菜单中有重复的"内部入库"和"内部出库"菜单项
-- 解决：删除重复的菜单项，保留较早创建的（ID较小的）

-- 删除重复的内部入库菜单（保留6674，删除6712）
-- 先删除6712的权限关联
DELETE FROM system_role_menu WHERE menu_id = 6712;

-- 删除6712的子菜单权限关联
DELETE FROM system_role_menu WHERE menu_id IN (
    SELECT id FROM system_menu WHERE parent_id = 6712
);

-- 删除6712的子菜单
DELETE FROM system_menu WHERE parent_id = 6712;

-- 删除6712菜单本身
DELETE FROM system_menu WHERE id = 6712;

-- 删除重复的内部出库菜单（保留6681，删除6701）
-- 先删除6701的权限关联
DELETE FROM system_role_menu WHERE menu_id = 6701;

-- 删除6701的子菜单权限关联
DELETE FROM system_role_menu WHERE menu_id IN (
    SELECT id FROM system_menu WHERE parent_id = 6701
);

-- 删除6701的子菜单
DELETE FROM system_menu WHERE parent_id = 6701;

-- 删除6701菜单本身
DELETE FROM system_menu WHERE id = 6701;

-- 删除重复的内部出库菜单（保留6681，删除6719）
-- 先删除6719的权限关联
DELETE FROM system_role_menu WHERE menu_id = 6719;

-- 删除6719的子菜单权限关联
DELETE FROM system_role_menu WHERE menu_id IN (
    SELECT id FROM system_menu WHERE parent_id = 6719
);

-- 删除6719的子菜单
DELETE FROM system_menu WHERE parent_id = 6719;

-- 删除6719菜单本身
DELETE FROM system_menu WHERE id = 6719;

-- 验证删除结果
SELECT id, name, path, component, permission, parent_id, type, sort
FROM system_menu
WHERE deleted = 0
  AND ((permission LIKE '%internal-in%' AND type = 2)
    OR (permission LIKE '%internal-out%' AND type = 2))
ORDER BY parent_id, sort, id;

