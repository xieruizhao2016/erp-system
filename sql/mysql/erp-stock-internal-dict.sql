-- ============================================================
-- 添加内部入库和内部出库的字典数据
-- ============================================================
-- 说明: 为库存明细业务类型字典添加内部入库和内部出库类型
-- 执行时间: 2025-12-26
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 检查字典类型是否存在
SET @dict_type_id = (SELECT id FROM system_dict_type WHERE type = 'erp_stock_record_biz_type' AND deleted = 0 LIMIT 1);

-- 如果字典类型不存在，先创建
INSERT INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT '库存明细的业务类型', 'erp_stock_record_biz_type', 0, '库存明细的业务类型', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_dict_type WHERE type = 'erp_stock_record_biz_type' AND deleted = 0);

SET @dict_type_id = (SELECT id FROM system_dict_type WHERE type = 'erp_stock_record_biz_type' AND deleted = 0 LIMIT 1);

-- 添加内部入库字典数据（90）- 不指定ID，让数据库自动生成
INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 90, '内部入库', '90', 'erp_stock_record_biz_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_dict_data WHERE dict_type = 'erp_stock_record_biz_type' AND value = '90' AND deleted = 0);

-- 添加内部入库（作废）字典数据（91）
INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 91, '内部入库（作废）', '91', 'erp_stock_record_biz_type', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_dict_data WHERE dict_type = 'erp_stock_record_biz_type' AND value = '91' AND deleted = 0);

-- 添加内部出库字典数据（92）
INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 92, '内部出库', '92', 'erp_stock_record_biz_type', 0, '', '', '', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_dict_data WHERE dict_type = 'erp_stock_record_biz_type' AND value = '92' AND deleted = 0);

-- 添加内部出库（作废）字典数据（93）
INSERT INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
SELECT 93, '内部出库（作废）', '93', 'erp_stock_record_biz_type', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0'
WHERE NOT EXISTS (SELECT 1 FROM system_dict_data WHERE dict_type = 'erp_stock_record_biz_type' AND value = '93' AND deleted = 0);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- 验证查询
-- ============================================================
-- SELECT id, sort, label, value, dict_type 
-- FROM system_dict_data 
-- WHERE dict_type = 'erp_stock_record_biz_type' 
--   AND value IN ('90', '91', '92', '93')
--   AND deleted = 0
-- ORDER BY sort;

