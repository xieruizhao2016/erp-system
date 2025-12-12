-- ERP库存管理模块 - 内部出入库菜单配置
-- 生成时间: 2025-12-12
-- 说明: 为库存管理模块添加内部入库和内部出库菜单

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 库存管理父菜单ID: 2583
-- 当前已有菜单: 仓库信息(2584), 产品库存(2590), 出入库明细(2593), 其它入库(2596), 其它出库(2610), 库存调拨(2624), 库存盘点(2631)

-- 1. 内部入库菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('内部入库', 'erp:stock:internal-in:query', 2, 5, 2583, 'internal-in', 'ep:box', 'erp/stock/internal-in/index', 'ErpStockInternalIn', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @internal_in_id = LAST_INSERT_ID();

-- 内部入库权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('内部入库查询', 'erp:stock:internal-in:query', 3, 1, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部入库创建', 'erp:stock:internal-in:create', 3, 2, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部入库更新', 'erp:stock:internal-in:update', 3, 3, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部入库删除', 'erp:stock:internal-in:delete', 3, 4, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部入库导出', 'erp:stock:internal-in:export', 3, 5, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部入库审批', 'erp:stock:internal-in:approve', 3, 6, @internal_in_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

-- 2. 内部出库菜单
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('内部出库', 'erp:stock:internal-out:query', 2, 6, 2583, 'internal-out', 'ep:box', 'erp/stock/internal-out/index', 'ErpStockInternalOut', 0, 1, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET @internal_out_id = LAST_INSERT_ID();

-- 内部出库权限按钮
INSERT INTO system_menu (name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
VALUES ('内部出库查询', 'erp:stock:internal-out:query', 3, 1, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部出库创建', 'erp:stock:internal-out:create', 3, 2, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部出库更新', 'erp:stock:internal-out:update', 3, 3, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部出库删除', 'erp:stock:internal-out:delete', 3, 4, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部出库导出', 'erp:stock:internal-out:export', 3, 5, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0),
       ('内部出库审批', 'erp:stock:internal-out:approve', 3, 6, @internal_out_id, '', '', '', '', 0, 0, 0, 0, 'admin', NOW(), 'admin', NOW(), 0);

SET FOREIGN_KEY_CHECKS = 1;

-- 说明：
-- 1. 使用自动递增ID，避免ID冲突
-- 2. 每个主菜单包含6个权限按钮（查询、创建、更新、删除、导出、审批）
-- 3. 菜单父级为库存管理(2583)
-- 4. 执行此SQL后，需要在角色管理中为角色分配这些菜单权限
-- 5. 执行后可通过以下SQL查看创建的菜单：
--    SELECT id, name, path, component FROM system_menu WHERE parent_id = 2583 ORDER BY sort;

