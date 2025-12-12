-- ERP财务管理模块菜单配置
-- 生成时间: 2025-12-12
-- 说明: 为财务管理模块添加缺失的菜单项

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 财务管理父菜单ID: 2645
-- 当前已有菜单: 结算账户(2646), 付款单(2687), 收款单(2694)

-- 使用自动递增ID，避免ID冲突
-- 1. 应收账款菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('应收账款', 'erp:finance:receivable:query', 2, 3, 2645, 'receivable', 'ep:money', 'erp/finance/receivable/index', 'ErpFinanceReceivable', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @receivable_id = LAST_INSERT_ID();

-- 应收账款权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('应收账款查询', 'erp:finance:receivable:query', 3, 1, @receivable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应收账款创建', 'erp:finance:receivable:create', 3, 2, @receivable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应收账款更新', 'erp:finance:receivable:update', 3, 3, @receivable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应收账款删除', 'erp:finance:receivable:delete', 3, 4, @receivable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应收账款导出', 'erp:finance:receivable:export', 3, 5, @receivable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 2. 应付账款菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('应付账款', 'erp:finance:payable:query', 2, 4, 2645, 'payable', 'ep:money', 'erp/finance/payable/index', 'ErpFinancePayable', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @payable_id = LAST_INSERT_ID();

-- 应付账款权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('应付账款查询', 'erp:finance:payable:query', 3, 1, @payable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应付账款创建', 'erp:finance:payable:create', 3, 2, @payable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应付账款更新', 'erp:finance:payable:update', 3, 3, @payable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应付账款删除', 'erp:finance:payable:delete', 3, 4, @payable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('应付账款导出', 'erp:finance:payable:export', 3, 5, @payable_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 3. 预付款菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('预付款', 'erp:finance:prepayment:query', 2, 5, 2645, 'prepayment', 'ep:money', 'erp/finance/prepayment/index', 'ErpFinancePrepayment', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @prepayment_id = LAST_INSERT_ID();

-- 预付款权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('预付款查询', 'erp:finance:prepayment:query', 3, 1, @prepayment_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预付款创建', 'erp:finance:prepayment:create', 3, 2, @prepayment_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预付款更新', 'erp:finance:prepayment:update', 3, 3, @prepayment_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预付款删除', 'erp:finance:prepayment:delete', 3, 4, @prepayment_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预付款导出', 'erp:finance:prepayment:export', 3, 5, @prepayment_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 4. 预收款菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('预收款', 'erp:finance:prereceipt:query', 2, 6, 2645, 'prereceipt', 'ep:money', 'erp/finance/prereceipt/index', 'ErpFinancePrereceipt', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @prereceipt_id = LAST_INSERT_ID();

-- 预收款权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('预收款查询', 'erp:finance:prereceipt:query', 3, 1, @prereceipt_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预收款创建', 'erp:finance:prereceipt:create', 3, 2, @prereceipt_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预收款更新', 'erp:finance:prereceipt:update', 3, 3, @prereceipt_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预收款删除', 'erp:finance:prereceipt:delete', 3, 4, @prereceipt_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('预收款导出', 'erp:finance:prereceipt:export', 3, 5, @prereceipt_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 5. 资产负债表菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('资产负债表', 'erp:finance:balance-sheet:query', 2, 7, 2645, 'balance-sheet', 'ep:document', 'erp/finance/balancesheet/index', 'ErpFinanceBalanceSheet', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @balance_sheet_id = LAST_INSERT_ID();

-- 资产负债表权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('资产负债表查询', 'erp:finance:balance-sheet:query', 3, 1, @balance_sheet_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('资产负债表创建', 'erp:finance:balance-sheet:create', 3, 2, @balance_sheet_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('资产负债表更新', 'erp:finance:balance-sheet:update', 3, 3, @balance_sheet_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('资产负债表删除', 'erp:finance:balance-sheet:delete', 3, 4, @balance_sheet_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('资产负债表导出', 'erp:finance:balance-sheet:export', 3, 5, @balance_sheet_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 6. 利润表菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('利润表', 'erp:finance:profit-statement:query', 2, 8, 2645, 'profit-statement', 'ep:document', 'erp/finance/profitstatement/index', 'ErpFinanceProfitStatement', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @profit_statement_id = LAST_INSERT_ID();

-- 利润表权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('利润表查询', 'erp:finance:profit-statement:query', 3, 1, @profit_statement_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('利润表创建', 'erp:finance:profit-statement:create', 3, 2, @profit_statement_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('利润表更新', 'erp:finance:profit-statement:update', 3, 3, @profit_statement_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('利润表删除', 'erp:finance:profit-statement:delete', 3, 4, @profit_statement_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('利润表导出', 'erp:finance:profit-statement:export', 3, 5, @profit_statement_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 使用自动递增ID，避免ID冲突
-- 2. 每个主菜单包含5个权限按钮（查询、创建、更新、删除、导出）
-- 3. 菜单父级为财务管理(2645)
-- 4. 执行此SQL后，需要在角色管理中为角色分配这些菜单权限
-- 5. 执行后可通过以下SQL查看创建的菜单：
--    SELECT id, name, path, component FROM system_menu WHERE parent_id = 2645 ORDER BY sort;

