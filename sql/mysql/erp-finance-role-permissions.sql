-- 为所有角色分配新的财务菜单权限
-- 生成时间: 2025-12-12
-- 说明: 为已分配财务菜单的角色，添加新创建的6个财务菜单权限

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 获取新创建的财务菜单ID
SET @receivable_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '应收账款' LIMIT 1);
SET @payable_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '应付账款' LIMIT 1);
SET @prepayment_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '预付款' LIMIT 1);
SET @prereceipt_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '预收款' LIMIT 1);
SET @balance_sheet_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '资产负债表' LIMIT 1);
SET @profit_statement_id = (SELECT id FROM system_menu WHERE parent_id = 2645 AND name = '利润表' LIMIT 1);

-- 获取所有已分配财务菜单的角色ID
-- 为这些角色分配新的财务菜单权限
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @receivable_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @receivable_id
  );

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @payable_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @payable_id
  );

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @prepayment_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @prepayment_id
  );

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @prereceipt_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @prereceipt_id
  );

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @balance_sheet_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @balance_sheet_id
  );

INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, @profit_statement_id
FROM system_role_menu rm
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = @profit_statement_id
  );

-- 同时为新菜单的权限按钮分配权限
-- 获取所有权限按钮的ID
INSERT INTO system_role_menu (role_id, menu_id)
SELECT DISTINCT rm.role_id, sm.id
FROM system_role_menu rm
JOIN system_menu sm ON sm.parent_id IN (@receivable_id, @payable_id, @prepayment_id, @prereceipt_id, @balance_sheet_id, @profit_statement_id)
WHERE rm.menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645)
  AND NOT EXISTS (
    SELECT 1 FROM system_role_menu 
    WHERE role_id = rm.role_id AND menu_id = sm.id
  );

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 此脚本会为所有已分配财务菜单的角色，自动添加新创建的6个财务菜单权限
-- 2. 包括主菜单和权限按钮
-- 3. 使用 NOT EXISTS 避免重复插入
-- 4. 执行后可通过以下SQL验证：
--    SELECT role_id, COUNT(*) as menu_count 
--    FROM system_role_menu 
--    WHERE menu_id IN (SELECT id FROM system_menu WHERE parent_id = 2645) 
--    GROUP BY role_id;

