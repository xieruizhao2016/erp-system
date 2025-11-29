-- ============================================================
-- ERP 工序管理菜单配置
-- 创建时间: 2025-11-27
-- 说明: 添加工序管理模块的菜单和权限配置
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========== 工序管理 ==========
-- 菜单（插入在工艺路线之前，因为工序是基础数据）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6006, '工序管理', '', 2, 3, 5042, 'process', 'ep:setting', 'erp/process/index', 'Process', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `name` = '工序管理',
  `path` = 'process',
  `icon` = 'ep:setting',
  `component` = 'erp/process/index',
  `component_name` = 'Process',
  `sort` = 3,
  `update_time` = NOW();

-- 按钮权限
SET @parentId = 6006;
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('工序查询', 'erp:process:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序创建', 'erp:process:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序更新', 'erp:process:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序删除', 'erp:process:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('工序导出', 'erp:process:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE 
  `update_time` = NOW();

-- 调整其他菜单的sort顺序（如果需要）
-- 注意：如果BOM明细的sort是3，工序也是3，可能需要调整
-- 这里假设BOM明细保持sort=3，工序也使用sort=3（在同一层级可以相同）

SET FOREIGN_KEY_CHECKS = 1;

