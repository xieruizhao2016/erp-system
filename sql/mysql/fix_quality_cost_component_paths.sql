-- ========================================
-- 质量管理和成本管理模块组件路径修复
-- 创建时间: 2025-12-09
-- 说明: 修复质量管理和成本管理子模块的component路径，使其指向正确的前端组件
-- ========================================

SET NAMES utf8mb4;

-- ========================================
-- 质量管理模块路径修复
-- ========================================

-- 质检标准: erp/qualitystandard/index -> erp/quality/qualitystandard/index
UPDATE system_menu 
SET component = 'erp/quality/qualitystandard/index',
    update_time = NOW()
WHERE id = 5060 
  AND name = '质检标准'
  AND deleted = 0;

-- 质检项目: erp/qualityitem/index -> erp/quality/qualityitem/index
UPDATE system_menu 
SET component = 'erp/quality/qualityitem/index',
    update_time = NOW()
WHERE id = 5061 
  AND name = '质检项目'
  AND deleted = 0;

-- 质检记录: erp/qualityinspection/index -> erp/quality/qualityinspection/index
UPDATE system_menu 
SET component = 'erp/quality/qualityinspection/index',
    update_time = NOW()
WHERE id = 6000 
  AND name = '质检记录'
  AND deleted = 0;

-- 质检明细: erp/qualityinspectionitem/index -> erp/quality/qualityinspectionitem/index
UPDATE system_menu 
SET component = 'erp/quality/qualityinspectionitem/index',
    update_time = NOW()
WHERE id = 6001 
  AND name = '质检明细'
  AND deleted = 0;

-- ========================================
-- 成本管理模块路径修复
-- ========================================

-- 标准成本: erp/coststandard/index -> erp/cost/coststandard/index
UPDATE system_menu 
SET component = 'erp/cost/coststandard/index',
    update_time = NOW()
WHERE id = 5066 
  AND name = '标准成本'
  AND deleted = 0;

-- 实际成本: erp/costactual/index -> erp/cost/costactual/index
UPDATE system_menu 
SET component = 'erp/cost/costactual/index',
    update_time = NOW()
WHERE id = 5067 
  AND name = '实际成本'
  AND deleted = 0;

-- 成本差异: erp/costvariance/index -> erp/cost/costvariance/index
UPDATE system_menu 
SET component = 'erp/cost/costvariance/index',
    update_time = NOW()
WHERE id = 5068 
  AND name = '成本差异'
  AND deleted = 0;

-- ========================================
-- 验证更新结果
-- ========================================
SELECT
    id,
    name,
    component,
    parent_id
FROM system_menu
WHERE id IN (5060, 5061, 6000, 6001, 5066, 5067, 5068)
AND deleted = 0
ORDER BY id;

