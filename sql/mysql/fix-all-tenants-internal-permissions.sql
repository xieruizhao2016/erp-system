-- 为所有租户的所有角色统一添加内部入库和出库权限
-- 问题：部分租户的角色缺少内部入库和出库的菜单权限（type=2）
-- 解决：为所有角色统一添加type=2的菜单权限，确保所有租户都能正常访问

-- 获取内部入库和出库的菜单ID（type=2）
SET @internal_in_menu_1 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-in:query' AND type = 2 ORDER BY id LIMIT 1);
SET @internal_in_menu_2 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-in:query' AND type = 2 ORDER BY id DESC LIMIT 1);
SET @internal_out_menu_1 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-out:query' AND type = 2 ORDER BY id LIMIT 1);
SET @internal_out_menu_2 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-out:query' AND type = 2 ORDER BY id DESC LIMIT 1);

-- 为所有角色添加内部入库菜单权限（如果还没有）
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @internal_in_menu_1
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @internal_in_menu_1
)
AND r.deleted = 0;

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @internal_in_menu_2
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @internal_in_menu_2
)
AND r.deleted = 0;

-- 为所有角色添加内部出库菜单权限（如果还没有）
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @internal_out_menu_1
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @internal_out_menu_1
)
AND r.deleted = 0;

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @internal_out_menu_2
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @internal_out_menu_2
)
AND r.deleted = 0;

-- 验证结果：检查所有租户的角色权限
SELECT 
    u.tenant_id,
    r.id as role_id,
    r.name as role_name,
    COUNT(DISTINCT CASE WHEN m.permission LIKE '%internal-in%' AND m.type = 2 THEN rm.menu_id END) as internal_in_menus,
    COUNT(DISTINCT CASE WHEN m.permission LIKE '%internal-out%' AND m.type = 2 THEN rm.menu_id END) as internal_out_menus
FROM system_users u
JOIN system_user_role ur ON u.id = ur.user_id
JOIN system_role r ON ur.role_id = r.id
LEFT JOIN system_role_menu rm ON r.id = rm.role_id
LEFT JOIN system_menu m ON rm.menu_id = m.id
WHERE u.deleted = 0 AND r.deleted = 0
GROUP BY u.tenant_id, r.id, r.name
ORDER BY u.tenant_id, r.id;

