-- ========================================
-- 修复仓库类型字典数据问题
-- 问题：仓库类型选择器中混入了"普通产品"选项
-- 解决：删除错误的字典数据，确保仓库类型字典正确
-- ========================================

-- 1. 首先查看当前仓库类型字典数据（用于诊断）
SELECT id, sort, label, value, dict_type, status 
FROM system_dict_data 
WHERE dict_type = 'erp_warehouse_type' 
ORDER BY sort;

-- 2. 删除可能错误关联到仓库类型的字典数据
-- 删除任何标签为"普通产品"且字典类型为"erp_warehouse_type"的数据
DELETE FROM `system_dict_data` 
WHERE dict_type = 'erp_warehouse_type' 
AND label = '普通产品';

-- 3. 确保仓库类型字典类型存在
INSERT IGNORE INTO `system_dict_type` 
(`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (2031, '仓库类型', 'erp_warehouse_type', 0, '仓库类型：普通仓库、原材料仓、半成品仓、成品仓（系统默认，不可修改）', '1', NOW(), '1', NOW(), b'0');

-- 4. 确保仓库类型字典数据正确（使用 INSERT IGNORE 避免重复）
-- 普通仓库
INSERT IGNORE INTO `system_dict_data` 
(`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3110, 1, '普通仓库', '1', 'erp_warehouse_type', 0, 'default', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

-- 原材料仓
INSERT IGNORE INTO `system_dict_data` 
(`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3111, 2, '原材料仓', '2', 'erp_warehouse_type', 0, 'info', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

-- 半成品仓
INSERT IGNORE INTO `system_dict_data` 
(`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3112, 3, '半成品仓', '3', 'erp_warehouse_type', 0, 'warning', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

-- 成品仓
INSERT IGNORE INTO `system_dict_data` 
(`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (3113, 4, '成品仓', '4', 'erp_warehouse_type', 0, 'success', '', '系统默认类型，不可修改', '1', NOW(), '1', NOW(), b'0');

-- 5. 验证修复结果
SELECT '修复后的仓库类型字典数据：' as title;
SELECT id, sort, label, value, dict_type, status 
FROM system_dict_data 
WHERE dict_type = 'erp_warehouse_type' 
ORDER BY sort;

-- 6. 同时确保产品类型字典数据正确（避免混淆）
SELECT '产品类型字典数据（供对比）：' as title;
SELECT id, sort, label, value, dict_type, status 
FROM system_dict_data 
WHERE dict_type = 'erp_product_type' 
ORDER BY sort;

