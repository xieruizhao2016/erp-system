#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
生成生产管理模块的字典SQL
"""

from datetime import datetime

# 字典类型配置
DICT_TYPES = [
    # 生产订单相关
    {"id": 2008, "name": "生产订单状态", "type": "erp_production_order_status", "remark": "生产订单的状态"},
    {"id": 2009, "name": "生产订单优先级", "type": "erp_production_order_priority", "remark": "生产订单的优先级"},
    {"id": 2010, "name": "生产订单来源类型", "type": "erp_production_order_source_type", "remark": "生产订单的来源类型"},
    
    # 工单相关
    {"id": 2011, "name": "工单状态", "type": "erp_work_order_status", "remark": "工单的状态"},
    {"id": 2012, "name": "工单进度状态", "type": "erp_work_order_progress_status", "remark": "工单进度的状态"},
    
    # BOM相关
    {"id": 2013, "name": "产品BOM状态", "type": "erp_product_bom_status", "remark": "产品BOM的状态"},
    {"id": 2014, "name": "BOM类型", "type": "erp_bom_type", "remark": "BOM的类型"},
    
    # 工艺路线相关
    {"id": 2015, "name": "工艺路线状态", "type": "erp_process_route_status", "remark": "工艺路线的状态"},
    
    # 生产排程相关
    {"id": 2016, "name": "生产排程状态", "type": "erp_production_schedule_status", "remark": "生产排程的状态"},
    {"id": 2017, "name": "排程明细状态", "type": "erp_production_schedule_item_status", "remark": "排程明细的状态"},
    
    # MRP相关
    {"id": 2018, "name": "MRP订单类型", "type": "erp_mrp_order_type", "remark": "MRP运算结果的订单类型"},
    {"id": 2019, "name": "MRP批量规则", "type": "erp_mrp_lot_sizing_rule", "remark": "MRP的批量规则"},
    {"id": 2020, "name": "MRP订单状态", "type": "erp_mrp_order_status", "remark": "MRP订单的状态"},
    {"id": 2021, "name": "MRP参数类型", "type": "erp_mrp_param_type", "remark": "MRP参数的数据类型"},
    
    # 成本相关
    {"id": 2022, "name": "标准成本状态", "type": "erp_cost_standard_status", "remark": "标准成本的状态"},
    {"id": 2023, "name": "实际成本状态", "type": "erp_cost_actual_status", "remark": "实际成本的状态"},
    {"id": 2024, "name": "成本差异类型", "type": "erp_cost_variance_type", "remark": "成本差异的类型"},
    
    # 设备相关
    {"id": 2025, "name": "设备状态", "type": "erp_equipment_status", "remark": "设备的状态"},
    {"id": 2026, "name": "设备状态记录", "type": "erp_equipment_status_record", "remark": "设备状态记录的状态"},
    
    # 质检相关
    {"id": 2027, "name": "质检状态", "type": "erp_quality_status", "remark": "质检的状态"},
    
    # 其他
    {"id": 2028, "name": "工时统计状态", "type": "erp_work_hours_status", "remark": "工时统计的状态"},
    {"id": 2029, "name": "生产报表状态", "type": "erp_production_report_status", "remark": "生产报表的状态"},
]

# 字典数据配置
DICT_DATA = [
    # 生产订单状态
    {"type": "erp_production_order_status", "items": [
        {"sort": 1, "label": "待开始", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "进行中", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "已暂停", "value": "3", "color_type": "warning"},
        {"sort": 4, "label": "已完成", "value": "4", "color_type": "success"},
        {"sort": 5, "label": "已取消", "value": "5", "color_type": "danger"},
    ]},
    
    # 生产订单优先级
    {"type": "erp_production_order_priority", "items": [
        {"sort": 1, "label": "紧急", "value": "1", "color_type": "danger"},
        {"sort": 2, "label": "高", "value": "2", "color_type": "warning"},
        {"sort": 3, "label": "中", "value": "3", "color_type": "primary"},
        {"sort": 4, "label": "低", "value": "4", "color_type": "info"},
    ]},
    
    # 生产订单来源类型
    {"type": "erp_production_order_source_type", "items": [
        {"sort": 1, "label": "手动创建", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "销售订单", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "库存补充", "value": "3", "color_type": "success"},
    ]},
    
    # 工单状态
    {"type": "erp_work_order_status", "items": [
        {"sort": 1, "label": "已创建", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "已下达", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "进行中", "value": "3", "color_type": "primary"},
        {"sort": 4, "label": "已暂停", "value": "4", "color_type": "warning"},
        {"sort": 5, "label": "已完成", "value": "5", "color_type": "success"},
        {"sort": 6, "label": "已取消", "value": "6", "color_type": "danger"},
    ]},
    
    # 工单进度状态
    {"type": "erp_work_order_progress_status", "items": [
        {"sort": 1, "label": "待开始", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "进行中", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "已完成", "value": "3", "color_type": "success"},
        {"sort": 4, "label": "异常", "value": "4", "color_type": "danger"},
    ]},
    
    # 产品BOM状态
    {"type": "erp_product_bom_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "生效", "value": "2", "color_type": "success"},
        {"sort": 3, "label": "失效", "value": "3", "color_type": "danger"},
    ]},
    
    # BOM类型
    {"type": "erp_bom_type", "items": [
        {"sort": 1, "label": "生产BOM", "value": "1", "color_type": "primary"},
        {"sort": 2, "label": "设计BOM", "value": "2", "color_type": "info"},
        {"sort": 3, "label": "工艺BOM", "value": "3", "color_type": "success"},
    ]},
    
    # 工艺路线状态
    {"type": "erp_process_route_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "生效", "value": "2", "color_type": "success"},
        {"sort": 3, "label": "失效", "value": "3", "color_type": "danger"},
    ]},
    
    # 生产排程状态
    {"type": "erp_production_schedule_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "已发布", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "执行中", "value": "3", "color_type": "primary"},
        {"sort": 4, "label": "已完成", "value": "4", "color_type": "success"},
    ]},
    
    # 排程明细状态
    {"type": "erp_production_schedule_item_status", "items": [
        {"sort": 1, "label": "已计划", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "已下达", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "进行中", "value": "3", "color_type": "primary"},
        {"sort": 4, "label": "已完成", "value": "4", "color_type": "success"},
        {"sort": 5, "label": "已延迟", "value": "5", "color_type": "warning"},
    ]},
    
    # MRP订单类型
    {"type": "erp_mrp_order_type", "items": [
        {"sort": 1, "label": "生产订单", "value": "1", "color_type": "primary"},
        {"sort": 2, "label": "采购订单", "value": "2", "color_type": "info"},
    ]},
    
    # MRP批量规则
    {"type": "erp_mrp_lot_sizing_rule", "items": [
        {"sort": 1, "label": "固定批量", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "按需", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "最小-最大", "value": "3", "color_type": "success"},
    ]},
    
    # MRP订单状态
    {"type": "erp_mrp_order_status", "items": [
        {"sort": 1, "label": "建议", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "确认", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "下达", "value": "3", "color_type": "success"},
    ]},
    
    # MRP参数类型
    {"type": "erp_mrp_param_type", "items": [
        {"sort": 1, "label": "字符串", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "数字", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "日期", "value": "3", "color_type": "success"},
        {"sort": 4, "label": "布尔", "value": "4", "color_type": "warning"},
    ]},
    
    # 标准成本状态
    {"type": "erp_cost_standard_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "生效", "value": "2", "color_type": "success"},
        {"sort": 3, "label": "失效", "value": "3", "color_type": "danger"},
    ]},
    
    # 实际成本状态
    {"type": "erp_cost_actual_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "已计算", "value": "2", "color_type": "primary"},
        {"sort": 3, "label": "已确认", "value": "3", "color_type": "success"},
    ]},
    
    # 成本差异类型
    {"type": "erp_cost_variance_type", "items": [
        {"sort": 1, "label": "有利", "value": "1", "color_type": "success"},
        {"sort": 2, "label": "不利", "value": "2", "color_type": "danger"},
    ]},
    
    # 设备状态
    {"type": "erp_equipment_status", "items": [
        {"sort": 1, "label": "正常", "value": "1", "color_type": "success"},
        {"sort": 2, "label": "维修中", "value": "2", "color_type": "warning"},
        {"sort": 3, "label": "故障", "value": "3", "color_type": "danger"},
        {"sort": 4, "label": "报废", "value": "4", "color_type": "info"},
    ]},
    
    # 设备状态记录
    {"type": "erp_equipment_status_record", "items": [
        {"sort": 1, "label": "运行", "value": "1", "color_type": "success"},
        {"sort": 2, "label": "待机", "value": "2", "color_type": "info"},
        {"sort": 3, "label": "故障", "value": "3", "color_type": "danger"},
        {"sort": 4, "label": "维修", "value": "4", "color_type": "warning"},
        {"sort": 5, "label": "停机", "value": "5", "color_type": "info"},
    ]},
    
    # 质检状态
    {"type": "erp_quality_status", "items": [
        {"sort": 1, "label": "待检", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "合格", "value": "2", "color_type": "success"},
        {"sort": 3, "label": "不合格", "value": "3", "color_type": "danger"},
    ]},
    
    # 工时统计状态
    {"type": "erp_work_hours_status", "items": [
        {"sort": 1, "label": "有效", "value": "1", "color_type": "success"},
        {"sort": 2, "label": "无效", "value": "2", "color_type": "danger"},
    ]},
    
    # 生产报表状态
    {"type": "erp_production_report_status", "items": [
        {"sort": 1, "label": "草稿", "value": "1", "color_type": "info"},
        {"sort": 2, "label": "已发布", "value": "2", "color_type": "success"},
    ]},
]

def generate_sql():
    """生成SQL文件"""
    project_root = "/Users/RUIZHAO/Documents/Project/erp-system"
    output_file = f"{project_root}/sql/mysql/erp_production_dict.sql"
    
    current_data_id = 3031
    
    with open(output_file, 'w', encoding='utf-8') as f:
        f.write(f"""-- ========================================
-- ERP 生产管理模块字典配置
-- 生成时间: {datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
-- ========================================

-- 字典类型
""")
        
        # 生成字典类型SQL
        for dict_type in DICT_TYPES:
            f.write(f"""INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES ({dict_type['id']}, '{dict_type['name']}', '{dict_type['type']}', 0, '{dict_type['remark']}', '1', NOW(), '1', NOW(), b'0');

""")
        
        f.write("-- 字典数据\n")
        
        # 生成字典数据SQL
        for dict_data in DICT_DATA:
            dict_type = dict_data['type']
            for item in dict_data['items']:
                f.write(f"""INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES ({current_data_id}, {item['sort']}, '{item['label']}', '{item['value']}', '{dict_type}', 0, '{item['color_type']}', '', '', '1', NOW(), '1', NOW(), b'0');

""")
                current_data_id += 1
    
    print(f"✅ 字典SQL已生成: {output_file}")
    print(f"📊 生成的字典类型数: {len(DICT_TYPES)}")
    print(f"📊 生成的字典数据数: {current_data_id - 3031}")
    print(f"📋 字典类型ID范围: {DICT_TYPES[0]['id']} - {DICT_TYPES[-1]['id']}")
    print(f"📋 字典数据ID范围: 3031 - {current_data_id - 1}")

if __name__ == '__main__':
    generate_sql()

