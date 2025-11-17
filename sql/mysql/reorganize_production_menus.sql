-- ========================================
-- 生产管理菜单重组SQL
-- 将相关子模块抽取为独立的大模块
-- ========================================

-- 1. 创建"质量管理"模块（一级菜单，放在ERP系统下）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5100, '质量管理', '', 1, 51, 2563, 'quality', 'ep:star', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='质量管理';

-- 2. 创建"设备管理"模块（一级菜单，放在ERP系统下）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5200, '设备管理', '', 1, 52, 2563, 'equipment', 'ep:tools', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='设备管理';

-- 3. 创建"成本管理"模块（一级菜单，放在ERP系统下）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5300, '成本管理', '', 1, 53, 2563, 'cost', 'ep:money', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='成本管理';

-- ========================================
-- 4. 将质检相关模块移动到"质量管理"下
-- ========================================

-- 质检标准 (5060) -> 质量管理 (5100)
UPDATE `system_menu` SET `parent_id` = 5100, `sort` = 1 WHERE `id` = 5060;

-- 质检项目 (5061) -> 质量管理 (5100)
UPDATE `system_menu` SET `parent_id` = 5100, `sort` = 2 WHERE `id` = 5061;

-- 质检记录 (5062) -> 质量管理 (5100)
UPDATE `system_menu` SET `parent_id` = 5100, `sort` = 3 WHERE `id` = 5062;

-- 质检明细 (5063) -> 质量管理 (5100)
UPDATE `system_menu` SET `parent_id` = 5100, `sort` = 4 WHERE `id` = 5063;

-- ========================================
-- 5. 将设备相关模块移动到"设备管理"下
-- ========================================

-- 设备管理 (5064) -> 设备管理模块 (5200)
-- 同时修复路径冲突：将子菜单的path改为equipment-manage，避免与一级菜单的path 'equipment'冲突
UPDATE `system_menu` SET `parent_id` = 5200, `sort` = 1, `path` = 'equipment-manage' WHERE `id` = 5064;

-- 设备状态 (5065) -> 设备管理模块 (5200)
UPDATE `system_menu` SET `parent_id` = 5200, `sort` = 2 WHERE `id` = 5065;

-- ========================================
-- 6. 将成本相关模块移动到"成本管理"下
-- ========================================

-- 标准成本 (5066) -> 成本管理 (5300)
UPDATE `system_menu` SET `parent_id` = 5300, `sort` = 1 WHERE `id` = 5066;

-- 实际成本 (5067) -> 成本管理 (5300)
UPDATE `system_menu` SET `parent_id` = 5300, `sort` = 2 WHERE `id` = 5067;

-- 成本差异 (5068) -> 成本管理 (5300)
UPDATE `system_menu` SET `parent_id` = 5300, `sort` = 3 WHERE `id` = 5068;

-- ========================================
-- 7. 重新排序生产管理下的剩余模块
-- ========================================

-- 生产订单 (5043) -> sort = 1
UPDATE `system_menu` SET `sort` = 1 WHERE `id` = 5043;

-- 产品BOM (5050) -> sort = 2
UPDATE `system_menu` SET `sort` = 2 WHERE `id` = 5050;

-- BOM明细 (5051) -> sort = 3
UPDATE `system_menu` SET `sort` = 3 WHERE `id` = 5051;

-- 工艺路线 (5052) -> sort = 4
UPDATE `system_menu` SET `sort` = 4 WHERE `id` = 5052;

-- 工艺路线明细 (5053) -> sort = 5
UPDATE `system_menu` SET `sort` = 5 WHERE `id` = 5053;

-- 生产排程 (5054) -> sort = 6
UPDATE `system_menu` SET `sort` = 6 WHERE `id` = 5054;

-- 排程明细 (5055) -> sort = 7
UPDATE `system_menu` SET `sort` = 7 WHERE `id` = 5055;

-- MRP参数 (5056) -> sort = 8
UPDATE `system_menu` SET `sort` = 8 WHERE `id` = 5056;

-- MRP运算结果 (5057) -> sort = 9
UPDATE `system_menu` SET `sort` = 9 WHERE `id` = 5057;

-- 工单 (5058) -> sort = 10
UPDATE `system_menu` SET `sort` = 10 WHERE `id` = 5058;

-- 工单进度 (5059) -> sort = 11
UPDATE `system_menu` SET `sort` = 11 WHERE `id` = 5059;

-- 工时统计 (5069) -> sort = 12
UPDATE `system_menu` SET `sort` = 12 WHERE `id` = 5069;

-- 生产KPI (5070) -> sort = 13
UPDATE `system_menu` SET `sort` = 13 WHERE `id` = 5070;

-- 生产报表 (5071) -> sort = 14
UPDATE `system_menu` SET `sort` = 14 WHERE `id` = 5071;

-- 看板配置 (5072) -> sort = 15
UPDATE `system_menu` SET `sort` = 15 WHERE `id` = 5072;

