-- 修复内部入库菜单权限问题
-- 问题：权限检查需要type=2的菜单权限，不仅仅是按钮权限（type=3）
-- 解决：为所有角色添加type=2的菜单权限

-- 获取type=2的菜单ID（主菜单，不是按钮）
SET @menu_id_1 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-in:query' AND type = 2 ORDER BY id LIMIT 1);
SET @menu_id_2 = (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-in:query' AND type = 2 ORDER BY id DESC LIMIT 1);

-- 为所有角色添加这两个菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @menu_id_1
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @menu_id_1
)
AND r.deleted = 0;

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @menu_id_2
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @menu_id_2
)
AND r.deleted = 0;

-- 验证结果：检查admin用户的所有角色是否有type=2的菜单权限
SELECT 
    r.id as role_id,
    r.name as role_name,
    COUNT(DISTINCT rm.menu_id) as menu_permissions
FROM system_users u
JOIN system_user_role ur ON u.id = ur.user_id
JOIN system_role r ON ur.role_id = r.id
LEFT JOIN system_role_menu rm ON r.id = rm.role_id
    AND rm.menu_id IN (SELECT id FROM system_menu WHERE permission = 'erp:stock:internal-in:query' AND type = 2)
WHERE u.username = 'admin'
GROUP BY r.id, r.name;

