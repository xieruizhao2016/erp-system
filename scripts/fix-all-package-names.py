#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
修复所有代码中的包名问题（去掉连字符）
"""

import os
import re
from pathlib import Path

PROJECT_ROOT = Path(__file__).parent.parent
ERP_MODULE = PROJECT_ROOT / "yudao-module-erp" / "src" / "main" / "java" / "cn" / "iocoder" / "yudao" / "module" / "erp"

# 包名映射
PACKAGE_MAPPINGS = {
    'finance-receivable': 'finance.receivable',
    'finance-payable': 'finance.payable',
    'finance-prepayment': 'finance.prepayment',
    'finance-prereceipt': 'finance.prereceipt',
    'finance-profit-statement': 'finance.profitstatement',
    'finance-balance-sheet': 'finance.balancesheet',
    'stock-internal-in': 'stock.internalin',
    'stock-internal-out': 'stock.internalout',
}

def fix_package_names(file_path):
    """修复文件中的包名"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
    except Exception as e:
        print(f"  跳过（无法读取）: {file_path} - {e}")
        return False
    
    original_content = content
    
    # 修复包声明
    for old_pkg, new_pkg in PACKAGE_MAPPINGS.items():
        # package声明
        content = re.sub(
            rf'package\s+cn\.iocoder\.yudao\.module\.erp\.(\w+)\.{re.escape(old_pkg)}',
            rf'package cn.iocoder.yudao.module.erp.\1.{new_pkg}',
            content
        )
        
        # import语句
        content = re.sub(
            rf'import\s+cn\.iocoder\.yudao\.module\.erp\.(\w+)\.{re.escape(old_pkg)}',
            rf'import cn.iocoder.yudao.module.erp.\1.{new_pkg}',
            content
        )
    
    if content != original_content:
        try:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        except Exception as e:
            print(f"  错误（无法写入）: {file_path} - {e}")
            return False
    
    return False

def main():
    """主函数"""
    print("=" * 60)
    print("修复所有包名问题")
    print("=" * 60)
    print()
    
    fixed_count = 0
    
    # 遍历所有Java文件
    for java_file in ERP_MODULE.rglob("*.java"):
        if fix_package_names(java_file):
            print(f"✓ {java_file.relative_to(PROJECT_ROOT)}")
            fixed_count += 1
    
    print()
    print("=" * 60)
    print(f"完成！修复了 {fixed_count} 个文件")
    print("=" * 60)

if __name__ == '__main__':
    main()

