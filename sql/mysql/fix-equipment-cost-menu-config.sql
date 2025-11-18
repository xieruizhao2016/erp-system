-- ========================================
-- 修复设备管理和成本管理菜单配置
-- ========================================

-- 修复设备管理菜单配置
UPDATE `system_menu` 
SET 
    `path` = 'equipment',
    `icon` = 'ep:tools',
    `sort` = 52,
    `type` = 1,
    `always_show` = 1,
    `keep_alive` = 1,
    `visible` = 1,
    `status` = 0
WHERE `id` = 5200 AND `deleted` = 0;

-- 修复成本管理菜单配置
UPDATE `system_menu` 
SET 
    `path` = 'cost',
    `icon` = 'ep:money',
    `sort` = 53,
    `type` = 1,
    `always_show` = 1,
    `keep_alive` = 1,
    `visible` = 1,
    `status` = 0
WHERE `id` = 5300 AND `deleted` = 0;

-- 确保质量管理菜单配置完整
UPDATE `system_menu` 
SET 
    `path` = 'quality',
    `icon` = 'ep:star',
    `sort` = 51,
    `type` = 1,
    `always_show` = 1,
    `keep_alive` = 1,
    `visible` = 1,
    `status` = 0
WHERE `id` = 5100 AND `deleted` = 0;

-- 验证配置
SELECT 
    id, 
    name, 
    parent_id, 
    path, 
    icon, 
    sort, 
    type, 
    visible, 
    status,
    always_show,
    keep_alive
FROM system_menu 
WHERE id IN (5100, 5200, 5300) AND deleted = 0 
ORDER BY sort;

