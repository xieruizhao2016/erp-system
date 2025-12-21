-- ============================================================
-- ERP 工作中心菜单配置
-- 创建时间: 2025-12-12
-- 说明: 添加工作中心管理模块的菜单和权限配置
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========== 工作中心 ==========
-- 菜单（放在基础数据菜单下）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5073, '工作中心', '', 2, 8, 6100, 'work-center', 'ep:office-building', 'erp/workcenter/index', 'WorkCenter', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '工作中心',
  `path` = 'work-center',
  `icon` = 'ep:office-building',
  `component` = 'erp/workcenter/index',
  `component_name` = 'WorkCenter',
  `parent_id` = 6100,
  `sort` = 8,
  `update_time` = NOW();

-- 按钮权限
SET @parentId = 5073;
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工作中心查询', 'erp:work-center:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工作中心创建', 'erp:work-center:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工作中心更新', 'erp:work-center:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工作中心删除', 'erp:work-center:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工作中心导出', 'erp:work-center:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `update_time` = NOW();

SET FOREIGN_KEY_CHECKS = 1;

