#!/bin/bash

echo "=========================================="
echo "一键修复排程明细功能"
echo "=========================================="
echo ""

# 读取数据库配置
DB_HOST="localhost"
DB_PORT="3306"
DB_NAME="ruoyi-vue-pro"
DB_USER="root"

# 提示输入数据库密码
echo "请输入MySQL root密码："
read -s DB_PASSWORD

echo ""
echo "开始修复..."
echo ""

# 执行SQL修复
mysql -h${DB_HOST} -P${DB_PORT} -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} << 'EOF'

-- 1. 创建排程明细表
CREATE TABLE IF NOT EXISTS `erp_production_schedule_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `schedule_id` bigint NOT NULL COMMENT '排程ID',
  `production_order_id` bigint NOT NULL COMMENT '生产订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `quantity` decimal(20,4) NOT NULL COMMENT '排程数量',
  `planned_start_time` datetime NOT NULL COMMENT '计划开始时间',
  `planned_end_time` datetime NOT NULL COMMENT '计划结束时间',
  `work_center_id` bigint NULL COMMENT '工作中心ID',
  `equipment_id` bigint NULL COMMENT '设备ID',
  `process_sequence` int NULL COMMENT '工序序号',
  `priority` int NULL DEFAULT 3 COMMENT '优先级',
  `setup_time` int NULL DEFAULT 0 COMMENT '准备时间',
  `run_time` int NOT NULL COMMENT '运行时间',
  `wait_time` int NULL DEFAULT 0 COMMENT '等待时间',
  `status` int NULL DEFAULT 1 COMMENT '状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟',
  `actual_start_time` datetime NULL COMMENT '实际开始时间',
  `actual_end_time` datetime NULL COMMENT '实际结束时间',
  `completion_rate` decimal(5,2) NULL DEFAULT 0 COMMENT '完成率',
  `delay_reason` varchar(500) NULL DEFAULT '' COMMENT '延迟原因',
  `creator` varchar(64) NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_schedule` (`tenant_id`, `schedule_id`) USING BTREE,
  INDEX `idx_order` (`production_order_id`) USING BTREE,
  INDEX `idx_work_center` (`work_center_id`) USING BTREE,
  INDEX `idx_equipment` (`equipment_id`) USING BTREE,
  INDEX `idx_time_range` (`planned_start_time`, `planned_end_time`) USING BTREE,
  INDEX `idx_status` (`status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'ERP 生产排程明细表';

SELECT '✓ 排程明细表已创建' AS status;

-- 2. 配置字典
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2017, '排程明细状态', 'erp_production_schedule_item_status', 0, '排程明细的状态', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(3066, 1, '已计划', '1', 'erp_production_schedule_item_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0'),
(3067, 2, '已下达', '2', 'erp_production_schedule_item_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0'),
(3068, 3, '进行中', '3', 'erp_production_schedule_item_status', 0, 'primary', '', '', '1', NOW(), '1', NOW(), b'0'),
(3069, 4, '已完成', '4', 'erp_production_schedule_item_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0'),
(3070, 5, '已延迟', '5', 'erp_production_schedule_item_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');

SELECT '✓ 字典配置完成' AS status;

-- 3. 配置菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (6005, '排程明细', '', 2, 7, 5042, 'production-schedule-item', 'ep:list', 'erp/productionscheduleitem/index', 'ProductionScheduleItem', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

SET @menuId = (SELECT id FROM system_menu WHERE name = '排程明细' LIMIT 1);

INSERT IGNORE INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
('排程明细查询', 'erp:production-schedule-item:query', 3, 1, @menuId, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细创建', 'erp:production-schedule-item:create', 3, 2, @menuId, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细更新', 'erp:production-schedule-item:update', 3, 3, @menuId, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细删除', 'erp:production-schedule-item:delete', 3, 4, @menuId, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('排程明细导出', 'erp:production-schedule-item:export', 3, 5, @menuId, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

SELECT '✓ 菜单配置完成' AS status;

-- 4. 给超级管理员分配权限
SET @adminRoleId = (SELECT id FROM system_role WHERE code = 'super_admin' LIMIT 1);

INSERT IGNORE INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `tenant_id`)
SELECT @adminRoleId, id, '1', NOW(), '1', NOW(), b'0', 1
FROM system_menu 
WHERE (name = '排程明细' OR permission LIKE '%production-schedule-item%')
  AND deleted = 0;

SELECT '✓ 权限配置完成' AS status;

SELECT '========== 修复完成 ==========' AS summary;

EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ 数据库修复成功！"
    echo ""
    echo "接下来请执行："
    echo "1. 清除浏览器缓存或使用无痕模式"
    echo "2. 重新登录系统"
    echo "3. 访问【生产管理】-【排程明细】页面"
else
    echo ""
    echo "❌ 修复失败，请检查数据库连接"
fi

echo ""
echo "=========================================="

