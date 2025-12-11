#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
修复代码生成器生成的模板变量
将 ${primaryColumn.javaType} 替换为 Long
"""

import os
import re
from pathlib import Path

PROJECT_ROOT = Path(__file__).parent.parent
SERVICE_DIR = PROJECT_ROOT / "yudao-module-erp" / "src" / "main" / "java" / "cn" / "iocoder" / "yudao" / "module" / "erp" / "service"

def fix_template_variables(file_path):
    """修复文件中的模板变量"""
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original_content = content
    
    # 替换模板变量
    content = content.replace('${primaryColumn.javaType}', 'Long')
    content = content.replace('${table.className}', '')  # 清理其他可能的模板变量
    
    # 修复包名（finance-receivable -> finance.receivable）
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-receivable',
        'package cn.iocoder.yudao.module.erp.service.finance.receivable',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-payable',
        'package cn.iocoder.yudao.module.erp.service.finance.payable',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-prepayment',
        'package cn.iocoder.yudao.module.erp.service.finance.prepayment',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-prereceipt',
        'package cn.iocoder.yudao.module.erp.service.finance.prereceipt',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-profit-statement',
        'package cn.iocoder.yudao.module.erp.service.finance.profitstatement',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.finance-balance-sheet',
        'package cn.iocoder.yudao.module.erp.service.finance.balancesheet',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.stock-internal-in',
        'package cn.iocoder.yudao.module.erp.service.stock.internalin',
        content
    )
    content = re.sub(
        r'package\s+cn\.iocoder\.yudao\.module\.erp\.service\.stock-internal-out',
        'package cn.iocoder.yudao.module.erp.service.stock.internalout',
        content
    )
    
    # 修复导入路径
    content = re.sub(
        r'import\s+cn\.iocoder\.yudao\.module\.erp\.controller\.admin\.finance-receivable',
        'import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable',
        content
    )
    content = re.sub(
        r'import\s+cn\.iocoder\.yudao\.module\.erp\.dal\.dataobject\.finance-receivable',
        'import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable',
        content
    )
    content = re.sub(
        r'import\s+cn\.iocoder\.yudao\.module\.erp\.dal\.mysql\.finance-receivable',
        'import cn.iocoder.yudao.module.erp.dal.mysql.finance.receivable',
        content
    )
    
    # 类似的修复其他模块
    modules = [
        ('finance-payable', 'finance.payable'),
        ('finance-prepayment', 'finance.prepayment'),
        ('finance-prereceipt', 'finance.prereceipt'),
        ('finance-profit-statement', 'finance.profitstatement'),
        ('finance-balance-sheet', 'finance.balancesheet'),
        ('stock-internal-in', 'stock.internalin'),
        ('stock-internal-out', 'stock.internalout'),
    ]
    
    for old_module, new_module in modules:
        content = re.sub(
            f'import\\s+cn\\.iocoder\\.yudao\\.module\\.erp\\.controller\\.admin\\.{re.escape(old_module)}',
            f'import cn.iocoder.yudao.module.erp.controller.admin.{new_module}',
            content
        )
        content = re.sub(
            f'import\\s+cn\\.iocoder\\.yudao\\.module\\.erp\\.dal\\.dataobject\\.{re.escape(old_module)}',
            f'import cn.iocoder.yudao.module.erp.dal.dataobject.{new_module}',
            content
        )
        content = re.sub(
            f'import\\s+cn\\.iocoder\\.yudao\\.module\\.erp\\.dal\\.mysql\\.{re.escape(old_module)}',
            f'import cn.iocoder.yudao.module.erp.dal.mysql.{new_module}',
            content
        )
    
    if content != original_content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    """主函数"""
    print("=" * 60)
    print("修复代码生成器模板变量")
    print("=" * 60)
    print()
    
    fixed_count = 0
    
    # 遍历所有Java文件
    for java_file in SERVICE_DIR.rglob("*.java"):
        if fix_template_variables(java_file):
            print(f"✓ 修复: {java_file.relative_to(PROJECT_ROOT)}")
            fixed_count += 1
    
    print()
    print("=" * 60)
    print(f"完成！修复了 {fixed_count} 个文件")
    print("=" * 60)

if __name__ == '__main__':
    main()

