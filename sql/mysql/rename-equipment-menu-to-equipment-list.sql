-- ========================================
-- 修改菜单名称：设备管理 -> 设备列表
-- ========================================

-- 1. 修改菜单名称
UPDATE `system_menu`
SET `name` = '设备列表',
    `updater` = 'admin',
    `update_time` = NOW()
WHERE `name` = '设备管理'
  AND `deleted` = 0;

-- 2. 修改按钮权限名称
UPDATE `system_menu`
SET `name` = CASE
    WHEN `name` = '设备管理查询' THEN '设备列表查询'
    WHEN `name` = '设备管理创建' THEN '设备列表创建'
    WHEN `name` = '设备管理更新' THEN '设备列表更新'
    WHEN `name` = '设备管理删除' THEN '设备列表删除'
    WHEN `name` = '设备管理导出' THEN '设备列表导出'
    ELSE `name`
  END,
  `updater` = 'admin',
  `update_time` = NOW()
WHERE `name` IN ('设备管理查询', '设备管理创建', '设备管理更新', '设备管理删除', '设备管理导出')
  AND `deleted` = 0;

-- 3. 验证修改结果
SELECT
    id,
    name,
    permission,
    type,
    sort,
    parent_id,
    path,
    component
FROM `system_menu`
WHERE `name` LIKE '%设备列表%'
   OR `name` LIKE '%设备管理%'
ORDER BY parent_id, sort;

-- ========================================
-- 成功修改完成！
-- 说明：
-- 1. 已将菜单"设备管理"改名为"设备列表"
-- 2. 已将相关按钮权限名称同步更新
-- 3. 需要重启后端服务并重新登录前端以生效
-- ========================================
