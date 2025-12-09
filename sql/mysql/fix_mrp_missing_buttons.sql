-- ========================================
-- MRP 一键生成按钮权限补充
-- 创建时间: 2025-12-09
-- 说明: 补充MRP参数和MRP运算结果页面缺失的一键生成按钮权限
-- ========================================

SET NAMES utf8mb4;

-- 获取MRP参数菜单ID（5056）
SET @mrp_params_menu_id = 5056;

-- 获取MRP运算结果菜单ID（5057）
SET @mrp_result_menu_id = 5057;

-- 获取当前最大的菜单ID，用于生成新的菜单ID
SET @max_menu_id = (SELECT IFNULL(MAX(id), 0) FROM system_menu);
SET @next_id = GREATEST(@max_menu_id + 1, 6600); -- 确保从6600开始，或者从最大ID+1开始

-- ========================================
-- 1. MRP参数 - 生成默认参数按钮权限
-- ========================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(@next_id, 'MRP参数生成默认', 'erp:mrp-params:generate-default', 3, 6, @mrp_params_menu_id, '', '', NULL, NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

SET @mrp_params_generate_id = @next_id;
SET @next_id = @next_id + 1;

-- ========================================
-- 2. MRP运算结果 - 执行MRP运算按钮权限
-- ========================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(@next_id, 'MRP运算执行', 'erp:mrp-result:execute', 3, 6, @mrp_result_menu_id, '', '', NULL, NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

SET @mrp_result_execute_id = @next_id;

-- ========================================
-- 验证插入结果
-- ========================================
SELECT
    id,
    name,
    permission,
    parent_id,
    sort
FROM system_menu
WHERE permission IN ('erp:mrp-params:generate-default', 'erp:mrp-result:execute')
AND deleted = 0
ORDER BY permission;

