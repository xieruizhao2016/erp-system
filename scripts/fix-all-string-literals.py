#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
批量修复字符串字面量未闭合错误
修复所有 Java 文件中的多行字符串问题
"""

import os
import re
import sys

def fix_string_literals(file_path):
    """修复文件中的字符串字面量问题"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 修复 @Tag(name = "...\n *")
        content = re.sub(r'@Tag\(name = "([^"]*)\n \*"\)', r'@Tag(name = "\1")', content)
        
        # 修复 @Operation(summary = "...\n *")
        content = re.sub(r'@Operation\(summary = "([^"]*)\n \*"\)', r'@Operation(summary = "\1")', content)
        
        # 修复 @Schema(description = "...\n *")
        content = re.sub(r'@Schema\(description = "([^"]*)\n \*([^"]*)"\)', r'@Schema(description = "\1\2")', content)
        
        # 修复 Excel 文件名中的多行字符串
        content = re.sub(r'ExcelUtils\.write\(response, "([^"]*)\n \*\.xls"', r'ExcelUtils.write(response, "\1.xls"', content)
        
        # 修复 @Parameter 中的多行字符串
        content = re.sub(r'@Parameter\(name = "([^"]*)\n \*"\)', r'@Parameter(name = "\1")', content)
        
        # 修复其他可能的模式：字符串后跟换行和 *
        content = re.sub(r'"([^"]*)\n \*"', r'"\1"', content)
        
        # 修复缩进问题
        content = re.sub(r'^\s+@PreAuthorize', r'    @PreAuthorize', content, flags=re.MULTILINE)
        
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
    total_count = 0
    
    # 遍历所有 Java 文件
    for root, dirs, files in os.walk(target_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                total_count += 1
                
                if fix_string_literals(file_path):
                    fixed_count += 1
                    print(f"✓ 已修复: {os.path.relpath(file_path, target_dir)}")
    
    print("")
    print(f"总计: {total_count} 个文件")
    print(f"修复: {fixed_count} 个文件")
    
    if fixed_count > 0:
        print("\n修复完成！")
    else:
        print("\n未发现需要修复的文件。")

if __name__ == '__main__':
    main()

