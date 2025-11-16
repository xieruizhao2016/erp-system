#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成所有新模块的菜单SQL
"""

import os
from datetime import datetime

# 模块配置（模块名 -> (显示名称, 路径, 图标)）
MODULES = {
    "production_order": ("生产订单", "production-order", "ep:document"),
    "product_bom": ("产品BOM", "product-bom", "ep:files"),
    "product_bom_item": ("BOM明细", "product-bom-item", "ep:list"),
    "process_route": ("工艺路线", "process-route", "ep:connection"),
    "process_route_item": ("工艺路线明细", "process-route-item", "ep:list"),
    "production_schedule": ("生产排程", "production-schedule", "ep:calendar"),
    "production_schedule_item": ("排程明细", "production-schedule-item", "ep:list"),
    "mrp_params": ("MRP参数", "mrp-params", "ep:setting"),
    "mrp_result": ("MRP运算结果", "mrp-result", "ep:data-analysis"),
    "work_order": ("工单", "work-order", "ep:document"),
    "work_order_progress": ("工单进度", "work-order-progress", "ep:loading"),
    "quality_standard": ("质检标准", "quality-standard", "ep:star"),
    "quality_item": ("质检项目", "quality-item", "ep:list"),
    "quality_inspection": ("质检记录", "quality-inspection", "ep:edit"),
    "quality_inspection_item": ("质检明细", "quality-inspection-item", "ep:list"),
    "equipment": ("设备管理", "equipment", "ep:tools"),
    "equipment_status": ("设备状态", "equipment-status", "ep:monitor"),
    "cost_standard": ("标准成本", "cost-standard", "ep:money"),
    "cost_actual": ("实际成本", "cost-actual", "ep:money"),
    "cost_variance": ("成本差异", "cost-variance", "ep:data-analysis"),
    "work_hours": ("工时统计", "work-hours", "ep:time"),
    "production_kpi": ("生产KPI", "production-kpi", "ep:data-line"),
    "production_report": ("生产报表", "production-report", "ep:document"),
    "production_dashboard_config": ("看板配置", "production-dashboard-config", "ep:setting"),
}

# ERP系统父菜单ID
ERP_PARENT_ID = 2563

# 生产管理目录ID
PRODUCTION_PARENT_ID = 5042

# 当前菜单ID（从5043开始）
CURRENT_MENU_ID = 5043
CURRENT_SORT = 1

def to_class_name(module_key):
    """转换为类名"""
    parts = module_key.split('_')
    return ''.join(word.capitalize() for word in parts)

def generate_sql():
    """生成SQL文件"""
    project_root = "/Users/RUIZHAO/Documents/Project/erp-system"
    output_file = os.path.join(project_root, "sql/mysql/erp_production_menus.sql")
    
    # 确保目录存在
    os.makedirs(os.path.dirname(output_file), exist_ok=True)
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(f"""-- ========================================
-- ERP 生产管理模块菜单配置
-- 生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
-- ========================================

-- 1. 创建生产管理目录（如果不存在）
-- 注意：如果已存在，请手动调整ID
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES ({PRODUCTION_PARENT_ID}, '生产管理', '', 1, 50, {ERP_PARENT_ID}, 'production', 'ep:operation', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='生产管理';

""")
        
        current_menu_id = CURRENT_MENU_ID
        current_sort = CURRENT_SORT
        
        for module_key, (display_name, path, icon) in MODULES.items():
            # 转换为业务名称（用于权限标识）
            business_name = module_key.replace('_', '-')
            
            # 转换为类名（用于组件名）
            class_name = to_class_name(module_key)
            
            f.write(f"""
-- ========== {display_name} ==========
-- 菜单
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES ({current_menu_id}, '{display_name}', '', 2, {current_sort}, {PRODUCTION_PARENT_ID}, '{path}', '{icon}', 'erp/{path}/index', '{class_name}', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = {current_menu_id};
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES 
('{display_name}查询', 'erp:{business_name}:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('{display_name}创建', 'erp:{business_name}:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('{display_name}更新', 'erp:{business_name}:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('{display_name}删除', 'erp:{business_name}:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('{display_name}导出', 'erp:{business_name}:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

""")
            
            current_menu_id += 1
            current_sort += 1
    
    print(f"✅ 菜单SQL已生成: {output_file}")
    print(f"📊 生成的模块数: {len(MODULES)}")
    print(f"📋 菜单ID范围: {CURRENT_MENU_ID} - {current_menu_id - 1}")
    print(f"\n⚠️  注意: 请检查并调整菜单ID，确保不与现有菜单冲突")

if __name__ == '__main__':
    generate_sql()

