-- ============================================================
-- ERP 工序管理菜单配置
-- 创建时间: 2025-11-27
-- 说明: 添加工序管理模块的菜单和权限配置
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========== 工序 ==========
-- 菜单（放在基础数据菜单下，作为基础数据）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5049, '工序', '', 2, 1, 6100, 'process', 'ep:setting', 'erp/process/index', 'Process', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '工序',
  `path` = 'process',
  `icon` = 'ep:setting',
  `component` = 'erp/process/index',
  `component_name` = 'Process',
  `parent_id` = 6100,
  `sort` = 1,
  `update_time` = NOW();

-- 按钮权限
SET @parentId = 5049;
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工序查询', 'erp:process:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序创建', 'erp:process:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序更新', 'erp:process:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序删除', 'erp:process:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序导出', 'erp:process:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `update_time` = NOW();

-- 注意：工序放在基础数据菜单（ID: 6100）下，sort=1（最前面）
-- 基础数据下的其他菜单：工序(sort=1), 产品BOM(sort=2), BOM明细(sort=3), 工艺路线(sort=4)...

SET FOREIGN_KEY_CHECKS = 1;

