-- 修复内部出库权限问题
-- 为管理员角色(role_id=1)添加内部出库相关权限
-- 执行时间: 2025-12-26

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 获取内部出库主菜单ID（可能有多个，取最新的）
SET @internal_out_id = (
    SELECT id FROM system_menu 
    WHERE parent_id = 2583 
      AND name = '内部出库' 
      AND permission = 'erp:stock:internal-out:query'
    ORDER BY id DESC
    LIMIT 1
);

-- 为管理员角色(1)添加内部出库主菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT 1, @internal_out_id
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = 1 AND menu_id = @internal_out_id
);

-- 为管理员角色(1)添加所有内部出库权限按钮
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT 1, sm.id
FROM system_menu sm
WHERE sm.parent_id = @internal_out_id
  AND sm.permission LIKE 'erp:stock:internal-out:%'
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = 1 AND menu_id = sm.id
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 验证权限是否添加成功
SELECT 
    m.id,
    m.name,
    m.permission,
    CASE WHEN rm.role_id IS NOT NULL THEN '有权限' ELSE '无权限' END as has_permission
FROM system_menu m
LEFT JOIN system_role_menu rm ON m.id = rm.menu_id AND rm.role_id = 1
WHERE m.permission LIKE '%internal-out%'
ORDER BY m.id;

