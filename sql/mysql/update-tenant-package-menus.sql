-- 更新租户套餐的menu_ids，添加新功能菜单
-- 问题：新添加的功能（内部入库、出库、财务管理新功能）需要添加到租户套餐中
-- 解决：更新ERP套餐的menu_ids，并更新所有使用该套餐的租户角色权限

-- 获取内部入库和出库的菜单ID及其子菜单
SET @internal_in_menu = 6674;
SET @internal_out_menu = 6681;
SET @internal_in_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @internal_in_menu);
SET @internal_out_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @internal_out_menu);

-- 获取财务管理新功能的菜单ID（type=2的主菜单）
SET @finance_receivable = 6602;
SET @finance_payable = 6608;
SET @finance_prepayment = 6614;
SET @finance_prereceipt = 6620;
SET @finance_balance_sheet = 6626;
SET @finance_profit_statement = 6632;

-- 获取这些菜单的所有子菜单
SET @finance_receivable_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_receivable);
SET @finance_payable_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_payable);
SET @finance_prepayment_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_prepayment);
SET @finance_prereceipt_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_prereceipt);
SET @finance_balance_sheet_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_balance_sheet);
SET @finance_profit_statement_submenus = (SELECT GROUP_CONCAT(id) FROM system_menu WHERE parent_id = @finance_profit_statement);

-- 获取当前ERP套餐的menu_ids
SET @current_menu_ids = (SELECT menu_ids FROM system_tenant_package WHERE id = 113);

-- 构建需要添加的菜单ID列表（如果还没有包含）
SET @menus_to_add = CONCAT_WS(',',
    IF(FIND_IN_SET(@internal_in_menu, @current_menu_ids) = 0, @internal_in_menu, NULL),
    IF(@internal_in_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@internal_in_submenus, ',', 1), @current_menu_ids) = 0, @internal_in_submenus, NULL),
    IF(FIND_IN_SET(@internal_out_menu, @current_menu_ids) = 0, @internal_out_menu, NULL),
    IF(@internal_out_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@internal_out_submenus, ',', 1), @current_menu_ids) = 0, @internal_out_submenus, NULL),
    IF(FIND_IN_SET(@finance_receivable, @current_menu_ids) = 0, @finance_receivable, NULL),
    IF(@finance_receivable_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_receivable_submenus, ',', 1), @current_menu_ids) = 0, @finance_receivable_submenus, NULL),
    IF(FIND_IN_SET(@finance_payable, @current_menu_ids) = 0, @finance_payable, NULL),
    IF(@finance_payable_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_payable_submenus, ',', 1), @current_menu_ids) = 0, @finance_payable_submenus, NULL),
    IF(FIND_IN_SET(@finance_prepayment, @current_menu_ids) = 0, @finance_prepayment, NULL),
    IF(@finance_prepayment_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_prepayment_submenus, ',', 1), @current_menu_ids) = 0, @finance_prepayment_submenus, NULL),
    IF(FIND_IN_SET(@finance_prereceipt, @current_menu_ids) = 0, @finance_prereceipt, NULL),
    IF(@finance_prereceipt_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_prereceipt_submenus, ',', 1), @current_menu_ids) = 0, @finance_prereceipt_submenus, NULL),
    IF(FIND_IN_SET(@finance_balance_sheet, @current_menu_ids) = 0, @finance_balance_sheet, NULL),
    IF(@finance_balance_sheet_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_balance_sheet_submenus, ',', 1), @current_menu_ids) = 0, @finance_balance_sheet_submenus, NULL),
    IF(FIND_IN_SET(@finance_profit_statement, @current_menu_ids) = 0, @finance_profit_statement, NULL),
    IF(@finance_profit_statement_submenus IS NOT NULL AND FIND_IN_SET(SUBSTRING_INDEX(@finance_profit_statement_submenus, ',', 1), @current_menu_ids) = 0, @finance_profit_statement_submenus, NULL)
);

-- 更新ERP套餐的menu_ids（如果@menus_to_add不为空）
UPDATE system_tenant_package
SET menu_ids = CONCAT(menu_ids, IF(@menus_to_add IS NOT NULL AND @menus_to_add != '', CONCAT(',', @menus_to_add), ''))
WHERE id = 113 AND (@menus_to_add IS NOT NULL AND @menus_to_add != '');

-- 验证更新结果
SELECT id, name, 
       CASE WHEN FIND_IN_SET(@internal_in_menu, menu_ids) > 0 THEN '包含内部入库' ELSE '不包含内部入库' END as has_internal_in,
       CASE WHEN FIND_IN_SET(@internal_out_menu, menu_ids) > 0 THEN '包含内部出库' ELSE '不包含内部出库' END as has_internal_out,
       CASE WHEN FIND_IN_SET(@finance_receivable, menu_ids) > 0 THEN '包含应收账款' ELSE '不包含应收账款' END as has_receivable
FROM system_tenant_package
WHERE id = 113;

