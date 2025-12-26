-- 修复财务管理模块权限字符串格式
-- 生成时间: 2025-01-27
-- 说明: 将权限字符串从 erp:finance:xxx 格式改为 erp:finance-xxx 格式，以匹配 Controller 中的权限检查

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 修复应收账款权限
UPDATE system_menu 
SET permission = 'erp:finance-receivable:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:query' AND name = '应收账款';

UPDATE system_menu 
SET permission = 'erp:finance-receivable:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:query' AND name = '应收账款查询';

UPDATE system_menu 
SET permission = 'erp:finance-receivable:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:create' AND name = '应收账款创建';

UPDATE system_menu 
SET permission = 'erp:finance-receivable:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:update' AND name = '应收账款更新';

UPDATE system_menu 
SET permission = 'erp:finance-receivable:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:delete' AND name = '应收账款删除';

UPDATE system_menu 
SET permission = 'erp:finance-receivable:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:receivable:export' AND name = '应收账款导出';

-- 2. 修复应付账款权限
UPDATE system_menu 
SET permission = 'erp:finance-payable:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:query' AND name = '应付账款';

UPDATE system_menu 
SET permission = 'erp:finance-payable:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:query' AND name = '应付账款查询';

UPDATE system_menu 
SET permission = 'erp:finance-payable:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:create' AND name = '应付账款创建';

UPDATE system_menu 
SET permission = 'erp:finance-payable:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:update' AND name = '应付账款更新';

UPDATE system_menu 
SET permission = 'erp:finance-payable:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:delete' AND name = '应付账款删除';

UPDATE system_menu 
SET permission = 'erp:finance-payable:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:payable:export' AND name = '应付账款导出';

-- 3. 修复预付款权限
UPDATE system_menu 
SET permission = 'erp:finance-prepayment:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:query' AND name = '预付款';

UPDATE system_menu 
SET permission = 'erp:finance-prepayment:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:query' AND name = '预付款查询';

UPDATE system_menu 
SET permission = 'erp:finance-prepayment:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:create' AND name = '预付款创建';

UPDATE system_menu 
SET permission = 'erp:finance-prepayment:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:update' AND name = '预付款更新';

UPDATE system_menu 
SET permission = 'erp:finance-prepayment:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:delete' AND name = '预付款删除';

UPDATE system_menu 
SET permission = 'erp:finance-prepayment:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prepayment:export' AND name = '预付款导出';

-- 4. 修复预收款权限
UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:query' AND name = '预收款';

UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:query' AND name = '预收款查询';

UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:create' AND name = '预收款创建';

UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:update' AND name = '预收款更新';

UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:delete' AND name = '预收款删除';

UPDATE system_menu 
SET permission = 'erp:finance-prereceipt:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:prereceipt:export' AND name = '预收款导出';

-- 5. 修复资产负债表权限
UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:query' AND name = '资产负债表';

UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:query' AND name = '资产负债表查询';

UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:create' AND name = '资产负债表创建';

UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:update' AND name = '资产负债表更新';

UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:delete' AND name = '资产负债表删除';

UPDATE system_menu 
SET permission = 'erp:finance-balance-sheet:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:balance-sheet:export' AND name = '资产负债表导出';

-- 6. 修复利润表权限
UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:query' AND name = '利润表';

UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:query', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:query' AND name = '利润表查询';

UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:create', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:create' AND name = '利润表创建';

UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:update', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:update' AND name = '利润表更新';

UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:delete', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:delete' AND name = '利润表删除';

UPDATE system_menu 
SET permission = 'erp:finance-profit-statement:export', updater = 'admin', update_time = NOW()
WHERE permission = 'erp:finance:profit-statement:export' AND name = '利润表导出';

SET FOREIGN_KEY_CHECKS = 1;

-- 验证修复结果
-- SELECT name, permission FROM system_menu WHERE permission LIKE 'erp:finance%' ORDER BY name;

