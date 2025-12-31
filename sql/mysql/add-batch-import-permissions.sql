-- 创建批量导入权限菜单
-- 问题：采购订单、销售订单、产品的批量导入按钮不显示，因为缺少权限菜单
-- 解决：创建批量导入权限菜单，并为所有角色添加这些权限

-- 获取采购订单、销售订单、产品的主菜单ID
SET @purchase_order_menu_id = 2666; -- 采购订单菜单ID
SET @sale_order_menu_id = (SELECT parent_id FROM system_menu WHERE permission = 'erp:sale-out:query' AND type = 3 LIMIT 1); -- 销售订单菜单ID（通过查找子菜单的parent_id）
SET @product_menu_id = 2565; -- 产品菜单ID

-- 获取最大的菜单ID
SET @max_menu_id = (SELECT MAX(id) FROM system_menu);

-- 创建采购订单批量导入权限菜单
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, creator, create_time, updater, update_time, deleted)
SELECT @max_menu_id + 1, '批量导入', 'erp:purchase-order:import', 3, 10, @purchase_order_menu_id, '', '', '', 0, '1', NOW(), '1', NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:purchase-order:import');

-- 创建销售订单批量导入权限菜单
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, creator, create_time, updater, update_time, deleted)
SELECT @max_menu_id + 2, '批量导入', 'erp:sale-out:import', 3, 10, @sale_order_menu_id, '', '', '', 0, '1', NOW(), '1', NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:sale-out:import');

-- 创建产品批量导入权限菜单
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, status, creator, create_time, updater, update_time, deleted)
SELECT @max_menu_id + 3, '批量导入', 'erp:product:import', 3, 10, @product_menu_id, '', '', '', 0, '1', NOW(), '1', NOW(), 0
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product:import');

-- 获取批量导入权限菜单ID
SET @purchase_import_menu_id = (SELECT id FROM system_menu WHERE permission = 'erp:purchase-order:import' LIMIT 1);
SET @sale_import_menu_id = (SELECT id FROM system_menu WHERE permission = 'erp:sale-out:import' LIMIT 1);
SET @product_import_menu_id = (SELECT id FROM system_menu WHERE permission = 'erp:product:import' LIMIT 1);

-- 为所有角色添加这些权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @purchase_import_menu_id
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @purchase_import_menu_id
)
AND r.deleted = 0;

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @sale_import_menu_id
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @sale_import_menu_id
)
AND r.deleted = 0;

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT r.id, @product_import_menu_id
FROM system_role r
WHERE NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = r.id AND menu_id = @product_import_menu_id
)
AND r.deleted = 0;

-- 更新ERP套餐的menu_ids，添加批量导入权限
UPDATE system_tenant_package
SET menu_ids = CONCAT(
    menu_ids,
    IF(FIND_IN_SET(@purchase_import_menu_id, menu_ids) = 0, CONCAT(',', @purchase_import_menu_id), ''),
    IF(FIND_IN_SET(@sale_import_menu_id, menu_ids) = 0, CONCAT(',', @sale_import_menu_id), ''),
    IF(FIND_IN_SET(@product_import_menu_id, menu_ids) = 0, CONCAT(',', @product_import_menu_id), '')
)
WHERE id = 113;

-- 验证结果
SELECT id, name, permission, parent_id, type, sort
FROM system_menu
WHERE permission IN ('erp:purchase-order:import', 'erp:sale-out:import', 'erp:product:import')
ORDER BY id;

