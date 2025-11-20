-- ========== 添加MRP参数生成默认参数权限 ==========

-- 说明：在MRP参数菜单下添加"生成默认参数"按钮权限
-- 菜单ID: 6006 (MRP参数) 或根据实际菜单ID而定
-- 创建时间：2025-11-20

-- 查找MRP参数的菜单ID
SET @mrpParamsMenuId = (SELECT id FROM system_menu WHERE name = 'MRP参数' AND deleted = 0 LIMIT 1);

-- 如果找到了MRP参数菜单，则添加生成默认参数权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
SELECT 
    'MRP参数生成默认' AS name,
    'erp:mrp-params:generate-default' AS permission,
    3 AS type,
    6 AS sort,
    @mrpParamsMenuId AS parent_id,
    '' AS path,
    '' AS icon,
    '' AS component,
    0 AS status,
    b'1' AS visible,
    b'1' AS keep_alive,
    b'1' AS always_show,
    '1' AS creator,
    NOW() AS create_time,
    '1' AS updater,
    NOW() AS update_time,
    b'0' AS deleted
WHERE @mrpParamsMenuId IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM system_menu 
    WHERE permission = 'erp:mrp-params:generate-default' 
    AND deleted = 0
  );

-- 查询结果，验证是否插入成功
SELECT 
    id,
    name,
    permission,
    parent_id,
    create_time
FROM system_menu 
WHERE permission = 'erp:mrp-params:generate-default' 
  AND deleted = 0;

-- 如果没有找到MRP参数菜单，显示提示
SELECT 
    CASE 
        WHEN @mrpParamsMenuId IS NULL THEN '未找到MRP参数菜单，请先创建该菜单'
        ELSE CONCAT('已找到MRP参数菜单，ID为：', @mrpParamsMenuId)
    END AS message;

