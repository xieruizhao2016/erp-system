#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
部署并修复生成的代码
1. 复制代码到正确位置
2. 修复包名（去掉连字符）
3. 修复导入路径
"""

import os
import shutil
import re
from pathlib import Path

# 配置
PROJECT_ROOT = Path(__file__).parent.parent
CODEGEN_DIR = PROJECT_ROOT / "codegen-new-features"
TARGET_BASE = PROJECT_ROOT / "yudao-module-erp" / "src" / "main" / "java" / "cn" / "iocoder" / "yudao" / "module" / "erp"

# 表配置：ID -> (旧包名, 新包名, 模块类型)
TABLE_CONFIGS = {
    "220": ("finance-balance-sheet", "balancesheet", "finance"),
    "221": ("finance-receivable", "receivable", "finance"),
    "222": ("finance-payable", "payable", "finance"),
    "223": ("finance-prepayment", "prepayment", "finance"),
    "224": ("finance-prereceipt", "prereceipt", "finance"),
    "225": ("finance-profit-statement", "profitstatement", "finance"),
    "226": ("stock-internal-in", "internalin", "stock"),
    "227": ("stock-internal-out", "internalout", "stock"),
}

def fix_package_name(content, old_package, new_package):
    """修复Java文件中的包名和导入路径"""
    # 修复包声明
    content = re.sub(
        rf'package\s+cn\.iocoder\.yudao\.module\.erp\.(\w+)\.{re.escape(old_package)}',
        rf'package cn.iocoder.yudao.module.erp.\1.{new_package}',
        content
    )
    
    # 修复导入语句
    content = re.sub(
        rf'import\s+cn\.iocoder\.yudao\.module\.erp\.(\w+)\.{re.escape(old_package)}',
        rf'import cn.iocoder.yudao.module.erp.\1.{new_package}',
        content
    )
    
    return content

def copy_and_fix_directory(source_dir, target_dir, old_package, new_package):
    """复制目录并修复包名"""
    if not source_dir.exists():
        return
    
    target_dir.mkdir(parents=True, exist_ok=True)
    
    for file_path in source_dir.rglob("*.java"):
        # 计算相对路径
        rel_path = file_path.relative_to(source_dir)
        
        # 构建目标路径（替换包名）
        target_path = target_dir / rel_path
        
        # 创建目标目录
        target_path.parent.mkdir(parents=True, exist_ok=True)
        
        # 读取并修复文件内容
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 修复包名
        content = fix_package_name(content, old_package, new_package)
        
        # 写入目标文件
        with open(target_path, 'w', encoding='utf-8') as f:
            f.write(content)
        
        print(f"  ✓ {rel_path}")

def deploy_table(table_id, old_package, new_package, module_type):
    """部署单个表的代码"""
    source_base = CODEGEN_DIR / f"codegen-{table_id}" / "yudao-module-erp" / "src" / "main" / "java" / "cn" / "iocoder" / "yudao" / "module" / "erp"
    
    if not source_base.exists():
        print(f"跳过: codegen-{table_id} 不存在")
        return
    
    print(f"\n处理表ID: {table_id} ({old_package} -> {module_type}.{new_package})")
    
    # 1. Controller
    source_controller = source_base / "controller" / "admin" / old_package
    target_controller = TARGET_BASE / "controller" / "admin" / module_type / new_package
    if source_controller.exists():
        print("  复制 Controller...")
        copy_and_fix_directory(source_controller, target_controller, old_package, new_package)
    
    # 2. Service
    source_service = source_base / "service" / old_package
    target_service = TARGET_BASE / "service" / module_type / new_package
    if source_service.exists():
        print("  复制 Service...")
        copy_and_fix_directory(source_service, target_service, old_package, new_package)
    
    # 3. DO
    source_do = source_base / "dal" / "dataobject" / old_package
    target_do = TARGET_BASE / "dal" / "dataobject" / module_type / new_package
    if source_do.exists():
        print("  复制 DO...")
        copy_and_fix_directory(source_do, target_do, old_package, new_package)
    
    # 4. Mapper
    source_mapper = source_base / "dal" / "mysql" / old_package
    target_mapper = TARGET_BASE / "dal" / "mysql" / module_type / new_package
    if source_mapper.exists():
        print("  复制 Mapper...")
        copy_and_fix_directory(source_mapper, target_mapper, old_package, new_package)
    
    # 5. Mapper XML
    source_xml = CODEGEN_DIR / f"codegen-{table_id}" / "yudao-module-erp" / "src" / "main" / "resources" / "mapper"
    target_xml = PROJECT_ROOT / "yudao-module-erp" / "src" / "main" / "resources" / "mapper"
    if source_xml.exists():
        print("  复制 Mapper XML...")
        target_xml.mkdir(parents=True, exist_ok=True)
        for xml_file in source_xml.glob("*.xml"):
            shutil.copy2(xml_file, target_xml / xml_file.name)
            print(f"  ✓ {xml_file.name}")

def main():
    """主函数"""
    print("=" * 60)
    print("部署并修复生成的代码")
    print("=" * 60)
    print()
    
    if not CODEGEN_DIR.exists():
        print(f"错误: 代码生成目录不存在: {CODEGEN_DIR}")
        return
    
    # 部署所有表
    for table_id, (old_package, new_package, module_type) in TABLE_CONFIGS.items():
        deploy_table(table_id, old_package, new_package, module_type)
    
    print()
    print("=" * 60)
    print("代码部署完成！")
    print("=" * 60)
    print()
    print("下一步:")
    print("  1. 合并 ErrorCode（手动操作）")
    print("  2. 编译测试")
    print("  3. 修复可能的编译错误")

if __name__ == '__main__':
    main()

