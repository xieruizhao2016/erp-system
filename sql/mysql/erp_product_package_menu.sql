-- ========================================
-- ERP 产品包装管理 - 菜单配置
-- 创建时间: 2025-11-21
-- 说明: 产品包装管理菜单和权限配置
-- ========================================

-- 注意：执行前请根据实际情况修改parent_id
-- 这里假设产品管理菜单ID为2000，请根据实际情况调整

-- 1. 插入产品包装主菜单
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装', '', 2, 6, 0, 'productpackage', 'ep:box', 'erp/productpackage/index', 'ErpProductPackage', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 获取刚插入的菜单ID
SET @package_menu_id = LAST_INSERT_ID();

-- 2. 插入查询权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装查询', 'erp:product-package:query', 3, 1, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 3. 插入创建权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装创建', 'erp:product-package:create', 3, 2, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 插入更新权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装更新', 'erp:product-package:update', 3, 3, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 插入删除权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装删除', 'erp:product-package:delete', 3, 4, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 6. 插入导出权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品包装导出', 'erp:product-package:export', 3, 5, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- ========================================
-- 查询结果
-- ========================================
SELECT 
    m1.id as menu_id,
    m1.name as menu_name,
    m1.permission,
    m1.type,
    m1.path,
    m1.component
FROM system_menu m1
WHERE m1.name LIKE '%产品包装%'
AND m1.deleted = 0
ORDER BY m1.parent_id, m1.sort;

