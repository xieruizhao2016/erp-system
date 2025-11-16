#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
批量修复缺失的导入
"""

import os
import re
import sys

def fix_imports(file_path):
    """修复文件中的缺失导入"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 检查是否使用了 LocalDate 或 LocalTime
        uses_local_date = 'LocalDate' in content
        uses_local_time = 'LocalTime' in content
        
        if not (uses_local_date or uses_local_time):
            return False
        
        # 检查是否已经导入了
        has_local_date_import = 'import java.time.LocalDate;' in content
        has_local_time_import = 'import java.time.LocalTime;' in content
        
        if (uses_local_date and has_local_date_import) and (uses_local_time and has_local_time_import):
            return False
        
        # 找到最后一个 import 语句的位置
        import_pattern = r'^import\s+.*?;'
        imports = list(re.finditer(import_pattern, content, re.MULTILINE))
        
        if not imports:
            # 如果没有 import，在 package 后添加
            package_match = re.search(r'^package\s+.*?;', content, re.MULTILINE)
            if package_match:
                insert_pos = package_match.end()
                new_imports = []
                if uses_local_date and not has_local_date_import:
                    new_imports.append('import java.time.LocalDate;')
                if uses_local_time and not has_local_time_import:
                    new_imports.append('import java.time.LocalTime;')
                if new_imports:
                    content = content[:insert_pos] + '\n' + '\n'.join(new_imports) + content[insert_pos:]
        else:
            # 在最后一个 import 后添加
            last_import = imports[-1]
            insert_pos = last_import.end()
            new_imports = []
            if uses_local_date and not has_local_date_import:
                new_imports.append('import java.time.LocalDate;')
            if uses_local_time and not has_local_time_import:
                new_imports.append('import java.time.LocalTime;')
            if new_imports:
                content = content[:insert_pos] + '\n' + '\n'.join(new_imports) + content[insert_pos:]
        
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        print(f"错误处理文件 {file_path}: {e}", file=sys.stderr)
        return False

def main():
    """主函数"""
    if len(sys.argv) > 1:
        target_dir = sys.argv[1]
    else:
        target_dir = "/Users/RUIZHAO/Documents/Project/erp-system/yudao-module-erp/src/main/java"
    
    if not os.path.exists(target_dir):
        print(f"错误: 目录不存在: {target_dir}")
        sys.exit(1)
    
    print(f"扫描目录: {target_dir}")
    print("")
    
    fixed_count = 0
    
    # 遍历所有 Java 文件
    for root, dirs, files in os.walk(target_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                
                if fix_imports(file_path):
                    fixed_count += 1
                    print(f"✓ 已修复: {os.path.relpath(file_path, target_dir)}")
    
    print("")
    print(f"修复: {fixed_count} 个文件")
    
    if fixed_count > 0:
        print("\n修复完成！")
    else:
        print("\n未发现需要修复的文件。")

if __name__ == '__main__':
    main()

