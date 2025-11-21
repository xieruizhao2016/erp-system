-- ========================================
-- ERP 产品SKU管理 - 菜单配置
-- 创建时间: 2025-11-21
-- 说明: SKU管理菜单和权限配置
-- ========================================

-- 注意：执行前请根据实际情况修改parent_id
-- 这里假设产品管理菜单ID为2000，请根据实际情况调整

-- 1. 插入SKU管理主菜单
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU管理', '', 2, 5, 2000, 'productsku', 'ep:goods', 'erp/productsku/index', 'ErpProductSku', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 获取刚插入的菜单ID
SET @sku_menu_id = LAST_INSERT_ID();

-- 2. 插入查询权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU查询', 'erp:product-sku:query', 3, 1, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 3. 插入创建权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU创建', 'erp:product-sku:create', 3, 2, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 4. 插入更新权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU更新', 'erp:product-sku:update', 3, 3, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 5. 插入删除权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU删除', 'erp:product-sku:delete', 3, 4, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 6. 插入导出权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU导出', 'erp:product-sku:export', 3, 5, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

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
WHERE m1.name LIKE '%SKU%'
AND m1.deleted = 0
ORDER BY m1.parent_id, m1.sort;

-- ========================================
-- 如何查找产品管理菜单ID
-- ========================================
-- 执行以下SQL查找产品管理的菜单ID，然后修改上面的parent_id
-- SELECT id, name, path, component FROM system_menu WHERE name LIKE '%产品%' AND deleted = 0;

