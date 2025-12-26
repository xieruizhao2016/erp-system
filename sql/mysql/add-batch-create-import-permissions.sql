-- ========================================
-- 添加批量创建/导入权限菜单
-- 创建时间: 2025-01-XX
-- 说明: 为销售订单、采购订单、产品信息添加导入权限菜单项
-- ========================================

-- 1. 销售订单导入权限（在审批权限之后，sort=7）
-- 销售订单菜单ID: 2638
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('销售订单导入', 'erp:sale-out:import', 3, 7, 2638, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '销售订单导入',
  `permission` = 'erp:sale-out:import',
  `sort` = 7,
  `update_time` = NOW();

-- 2. 采购订单导入权限（在审批权限之后，sort=7）
-- 采购订单菜单ID: 2666
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('采购订单导入', 'erp:purchase-order:import', 3, 7, 2666, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '采购订单导入',
  `permission` = 'erp:purchase-order:import',
  `sort` = 7,
  `update_time` = NOW();

-- 3. 产品信息导入权限（需要查找产品信息的菜单ID和现有权限）
-- 产品信息菜单ID: 2565
-- 先查找产品信息现有的权限，确定sort值
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('产品信息导入', 'erp:product:import', 3, 4, 2565, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '产品信息导入',
  `permission` = 'erp:product:import',
  `sort` = 4,
  `update_time` = NOW();

-- ========================================
-- 说明：
-- 1. 这些权限菜单项会在系统菜单中显示
-- 2. 需要在租户套餐中勾选这些权限，用户才能看到批量创建按钮
-- 3. 执行此SQL后，需要在"系统管理 -> 租户管理 -> 租户套餐"中为套餐添加这些菜单权限
-- ========================================

