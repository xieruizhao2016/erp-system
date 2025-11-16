#!/bin/bash
# 修复字符串字面量未闭合错误
# 使用方法: ./scripts/fix-string-literals.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

PROJECT_ROOT="/Users/RUIZHAO/Documents/Project/erp-system"
BACKEND_DIR="${PROJECT_ROOT}/yudao-module-erp/src/main/java"

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}修复字符串字面量未闭合错误${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 查找所有有问题的 Java 文件
FIXED_COUNT=0

# 修复函数：将多行字符串合并为单行
fix_file() {
    local file="$1"
    local temp_file="${file}.tmp"
    
    # 使用 sed 修复常见的多行字符串问题
    # 1. 修复 @Tag 和 @Operation 中的多行字符串
    # 2. 修复 Excel 文件名中的多行字符串
    
    sed -E '
        # 修复 @Tag(name = "...\n *")
        /@Tag\(name = ".*$/,/\*"\)/ {
            N
            s/@Tag\(name = "([^"]*)\n \*"\)/@Tag(name = "\1")/
        }
        
        # 修复 @Operation(summary = "...\n *")
        /@Operation\(summary = ".*$/,/\*"\)/ {
            N
            s/@Operation\(summary = "([^"]*)\n \*"\)/@Operation(summary = "\1")/
        }
        
        # 修复 Excel 文件名中的多行字符串
        /ExcelUtils\.write\(response, ".*$/,/\*\.xls"/ {
            N
            s/ExcelUtils\.write\(response, "([^"]*)\n \*\.xls"/ExcelUtils.write(response, "\1.xls"/
        }
    ' "$file" > "$temp_file" 2>/dev/null || cp "$file" "$temp_file"
    
    # 更简单的方法：直接替换常见的模式
    python3 << 'PYTHON_SCRIPT'
import sys
import re

file_path = sys.argv[1]
temp_path = sys.argv[2]

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# 修复 @Tag(name = "...\n *")
content = re.sub(r'@Tag\(name = "([^"]*)\n \*"\)', r'@Tag(name = "\1")', content)

# 修复 @Operation(summary = "...\n *")
content = re.sub(r'@Operation\(summary = "([^"]*)\n \*"\)', r'@Operation(summary = "\1")', content)

# 修复 Excel 文件名中的多行字符串
content = re.sub(r'ExcelUtils\.write\(response, "([^"]*)\n \*\.xls"', r'ExcelUtils.write(response, "\1.xls"', content)

# 修复 @Parameter 中的多行字符串
content = re.sub(r'@Parameter\(name = "([^"]*)\n \*"\)', r'@Parameter(name = "\1")', content)

with open(temp_path, 'w', encoding='utf-8') as f:
    f.write(content)
PYTHON_SCRIPT
    "$file" "$temp_file"
    
    if [ -f "$temp_file" ]; then
        mv "$temp_file" "$file"
        return 0
    fi
    return 1
}

# 查找所有需要修复的文件
echo -e "${YELLOW}查找需要修复的文件...${NC}"
FILES_TO_FIX=$(find "$BACKEND_DIR" -name "*.java" -type f | xargs grep -l "DO\n \*" 2>/dev/null || true)

if [ -z "$FILES_TO_FIX" ]; then
    echo -e "${GREEN}未找到需要修复的文件${NC}"
    exit 0
fi

echo "找到 $(echo "$FILES_TO_FIX" | wc -l | tr -d ' ') 个文件需要修复"
echo ""

# 修复每个文件
for file in $FILES_TO_FIX; do
    echo -e "${BLUE}修复: $(basename "$file")${NC}"
    
    # 使用 Python 修复
    python3 << 'PYTHON_SCRIPT'
import sys
import re

file_path = sys.argv[1]

with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

original_content = content

# 修复 @Tag(name = "...\n *")
content = re.sub(r'@Tag\(name = "([^"]*)\n \*"\)', r'@Tag(name = "\1")', content)

# 修复 @Operation(summary = "...\n *")
content = re.sub(r'@Operation\(summary = "([^"]*)\n \*"\)', r'@Operation(summary = "\1")', content)

# 修复 Excel 文件名中的多行字符串
content = re.sub(r'ExcelUtils\.write\(response, "([^"]*)\n \*\.xls"', r'ExcelUtils.write(response, "\1.xls"', content)

# 修复 @Parameter 中的多行字符串
content = re.sub(r'@Parameter\(name = "([^"]*)\n \*"\)', r'@Parameter(name = "\1")', content)

# 修复其他可能的模式
content = re.sub(r'"([^"]*)\n \*"', r'"\1"', content)

if content != original_content:
    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    print(f"✓ 已修复: {file_path}")
    sys.exit(0)
else:
    print(f"- 无需修复: {file_path}")
    sys.exit(1)
PYTHON_SCRIPT
    "$file"
    
    if [ $? -eq 0 ]; then
        FIXED_COUNT=$((FIXED_COUNT + 1))
    fi
done

echo ""
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}修复完成！${NC}"
echo -e "${GREEN}========================================${NC}"
echo "修复文件数: ${FIXED_COUNT}"

