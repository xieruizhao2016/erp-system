#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
修复菜单SQL中的ID，避免与现有菜单冲突
"""

import re
import sys

def fix_menu_ids(sql_file, start_id):
    """修复SQL文件中的菜单ID"""
    with open(sql_file, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # 当前ID计数器
    current_id = start_id
    
    # 替换生产管理目录ID
    content = re.sub(
        r'VALUES \(5042,',
        f'VALUES ({current_id},',
        content
    )
    content = re.sub(
        r'parent_id = 5042',
        f'parent_id = {current_id}',
        content
    )
    current_id += 1
    
    # 替换所有主菜单ID（5043-5066）
    for old_id in range(5043, 5067):
        # 替换菜单定义中的ID
        content = re.sub(
            rf'VALUES \({old_id},',
            f'VALUES ({current_id},',
            content
        )
        # 替换SET @parentId中的ID
        content = re.sub(
            rf'SET @parentId = {old_id};',
            f'SET @parentId = {current_id};',
            content
        )
        current_id += 1
    
    # 写回文件
    with open(sql_file, 'w', encoding='utf-8') as f:
        f.write(content)
    
    print(f"✅ 已修复菜单ID，从 {start_id} 开始")
    print(f"📋 菜单ID范围: {start_id} - {current_id - 1}")

if __name__ == '__main__':
    sql_file = 'sql/mysql/erp_production_menus.sql'
    
    if len(sys.argv) > 1:
        start_id = int(sys.argv[1])
    else:
        # 默认从5042开始，如果冲突可以调整
        start_id = 5042
    
    fix_menu_ids(sql_file, start_id)

