-- ========================================
-- ERP 产品管理缺失模块 - 菜单配置
-- 创建时间: 2025-12-08
-- 说明: 补充产品管理下缺失的3个模块（SKU管理、产品包装、产品OEM）
-- ========================================

-- 产品管理菜单ID为2564
SET @product_parent_id = 2564;

-- ========== 1. SKU管理 ==========
-- 检查是否已存在，如果不存在则插入
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 2578, 'SKU管理', '', 2, 3, @product_parent_id, 'product-sku', 'ep:goods', 'erp/productsku/index', 'ErpProductSku', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 2578 OR (name = 'SKU管理' AND parent_id = @product_parent_id AND deleted = 0));

SET @sku_menu_id = (SELECT id FROM system_menu WHERE (id = 2578 OR (name = 'SKU管理' AND parent_id = @product_parent_id AND deleted = 0)) LIMIT 1);

-- SKU管理按钮权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 'SKU查询', 'erp:product-sku:query', 3, 1, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-sku:query' AND parent_id = @sku_menu_id AND deleted = 0)
UNION ALL
SELECT 'SKU创建', 'erp:product-sku:create', 3, 2, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-sku:create' AND parent_id = @sku_menu_id AND deleted = 0)
UNION ALL
SELECT 'SKU更新', 'erp:product-sku:update', 3, 3, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-sku:update' AND parent_id = @sku_menu_id AND deleted = 0)
UNION ALL
SELECT 'SKU删除', 'erp:product-sku:delete', 3, 4, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-sku:delete' AND parent_id = @sku_menu_id AND deleted = 0)
UNION ALL
SELECT 'SKU导出', 'erp:product-sku:export', 3, 5, @sku_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-sku:export' AND parent_id = @sku_menu_id AND deleted = 0);

-- ========== 2. 产品包装 ==========
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 2579, '产品包装', '', 2, 4, @product_parent_id, 'product-package', 'ep:box', 'erp/productpackage/index', 'ErpProductPackage', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 2579 OR (name = '产品包装' AND parent_id = @product_parent_id AND deleted = 0));

SET @package_menu_id = (SELECT id FROM system_menu WHERE (id = 2579 OR (name = '产品包装' AND parent_id = @product_parent_id AND deleted = 0)) LIMIT 1);

-- 产品包装按钮权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '产品包装查询', 'erp:product-package:query', 3, 1, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-package:query' AND parent_id = @package_menu_id AND deleted = 0)
UNION ALL
SELECT '产品包装创建', 'erp:product-package:create', 3, 2, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-package:create' AND parent_id = @package_menu_id AND deleted = 0)
UNION ALL
SELECT '产品包装更新', 'erp:product-package:update', 3, 3, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-package:update' AND parent_id = @package_menu_id AND deleted = 0)
UNION ALL
SELECT '产品包装删除', 'erp:product-package:delete', 3, 4, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-package:delete' AND parent_id = @package_menu_id AND deleted = 0)
UNION ALL
SELECT '产品包装导出', 'erp:product-package:export', 3, 5, @package_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-package:export' AND parent_id = @package_menu_id AND deleted = 0);

-- ========== 3. 产品OEM ==========
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 2580, '产品OEM', '', 2, 5, @product_parent_id, 'product-oem', 'ep:office-building', 'erp/productoem/index', 'ErpProductOem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE id = 2580 OR (name = '产品OEM' AND parent_id = @product_parent_id AND deleted = 0));

SET @oem_menu_id = (SELECT id FROM system_menu WHERE (id = 2580 OR (name = '产品OEM' AND parent_id = @product_parent_id AND deleted = 0)) LIMIT 1);

-- 产品OEM按钮权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '产品OEM查询', 'erp:product-oem:query', 3, 1, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-oem:query' AND parent_id = @oem_menu_id AND deleted = 0)
UNION ALL
SELECT '产品OEM创建', 'erp:product-oem:create', 3, 2, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-oem:create' AND parent_id = @oem_menu_id AND deleted = 0)
UNION ALL
SELECT '产品OEM更新', 'erp:product-oem:update', 3, 3, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-oem:update' AND parent_id = @oem_menu_id AND deleted = 0)
UNION ALL
SELECT '产品OEM删除', 'erp:product-oem:delete', 3, 4, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-oem:delete' AND parent_id = @oem_menu_id AND deleted = 0)
UNION ALL
SELECT '产品OEM导出', 'erp:product-oem:export', 3, 5, @oem_menu_id, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_menu WHERE permission = 'erp:product-oem:export' AND parent_id = @oem_menu_id AND deleted = 0);

-- ========================================
-- 验证插入结果
-- ========================================
SELECT 
    id,
    name,
    path,
    component,
    sort,
    parent_id
FROM system_menu 
WHERE parent_id = 2564 
AND deleted = 0
ORDER BY sort;
