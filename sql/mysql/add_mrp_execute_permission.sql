-- ========== 添加MRP运算执行权限 ==========

-- 说明：在MRP运算结果菜单下添加"执行MRP运算"按钮权限
-- 菜单ID: 5057 (MRP运算结果) 或 6007 (根据实际菜单ID而定)
-- 创建时间：2025-11-20

-- 查找MRP运算结果的菜单ID
SET @mrpResultMenuId = (SELECT id FROM system_menu WHERE name = 'MRP运算结果' AND deleted = 0 LIMIT 1);

-- 如果找到了MRP运算结果菜单，则添加执行权限
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
SELECT 
    'MRP运算执行' AS name,
    'erp:mrp-result:execute' AS permission,
    3 AS type,
    6 AS sort,
    @mrpResultMenuId AS parent_id,
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
WHERE @mrpResultMenuId IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM system_menu 
    WHERE permission = 'erp:mrp-result:execute' 
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
WHERE permission = 'erp:mrp-result:execute' 
  AND deleted = 0;

-- 如果没有找到MRP运算结果菜单，显示提示
SELECT 
    CASE 
        WHEN @mrpResultMenuId IS NULL THEN '未找到MRP运算结果菜单，请先创建该菜单'
        ELSE CONCAT('已找到MRP运算结果菜单，ID为：', @mrpResultMenuId)
    END AS message;

