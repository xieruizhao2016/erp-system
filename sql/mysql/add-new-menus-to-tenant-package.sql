-- 将新创建的财务和库存菜单添加到租户套餐
-- 生成时间: 2025-12-12
-- 说明: 多租户系统中，菜单需要通过租户套餐来分配，新菜单需要添加到套餐中才能显示

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 获取所有租户套餐ID
-- 通常有：普通套餐(111)、ERP套餐(113)等

-- 1. 将新财务菜单添加到所有套餐
INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6376  -- 应收账款
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6376
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6382  -- 应付账款
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6382
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6388  -- 预付款
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6388
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6394  -- 预收款
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6394
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6400  -- 资产负债表
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6400
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6406  -- 利润表
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6406
  );

-- 2. 将新库存菜单添加到所有套餐
INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6412  -- 内部入库
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6412
  );

INSERT INTO system_tenant_package_menu (package_id, menu_id)
SELECT DISTINCT tpm.package_id, 6419  -- 内部出库
FROM system_tenant_package_menu tpm
WHERE tpm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2583)
  AND NOT EXISTS (
    SELECT 1 FROM system_tenant_package_menu 
    WHERE package_id = tpm.package_id AND menu_id = 6419
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 此脚本会将新菜单添加到所有已有财务/库存菜单的租户套餐中
-- 2. 使用 NOT EXISTS 避免重复插入
-- 3. 执行后需要清除Redis缓存并重启后端服务
-- 4. 验证：SELECT * FROM system_tenant_package_menu WHERE menu_id IN (6376, 6382, 6412, 6419);

