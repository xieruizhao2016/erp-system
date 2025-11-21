-- ========================================
-- ERP 产品OEM管理 - 菜单配置
-- 创建时间: 2025-11-21
-- 说明: 产品OEM管理菜单和权限配置
-- ========================================

-- 1. 插入产品OEM主菜单
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM', '', 2, 7, 0, 'productoem', 'ep:office-building', 'erp/productoem/index', 'ErpProductOem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 获取刚插入的菜单ID
SET @oem_menu_id = LAST_INSERT_ID();

-- 2. 插入查询权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM查询', 'erp:product-oem:query', 3, 1, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 3. 插入创建权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM创建', 'erp:product-oem:create', 3, 2, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 插入更新权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM更新', 'erp:product-oem:update', 3, 3, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 插入删除权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM删除', 'erp:product-oem:delete', 3, 4, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 6. 插入导出权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('产品OEM导出', 'erp:product-oem:export', 3, 5, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

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
WHERE m1.name LIKE '%产品OEM%'
AND m1.deleted = 0
ORDER BY m1.parent_id, m1.sort;

