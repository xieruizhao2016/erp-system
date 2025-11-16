#!/bin/bash
# 生成所有新模块的菜单SQL
# 使用方法: ./scripts/generate-menu-sql.sh

set -e

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

PROJECT_ROOT="/Users/RUIZHAO/Documents/Project/erp-system"
CODEGEN_DIR="${PROJECT_ROOT}/codegen-downloads-20251116-162611"
OUTPUT_FILE="${PROJECT_ROOT}/sql/mysql/erp_production_menus.sql"

# ERP系统父菜单ID
ERP_PARENT_ID=2563

# 生产管理目录ID（如果不存在，需要创建）
PRODUCTION_PARENT_ID=5042

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}生成生产管理模块菜单SQL${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 模块配置（模块名 -> 显示名称 -> 路径 -> 图标）
declare -A MODULES=(
    ["production_order"]="生产订单|production-order|ep:document"
    ["product_bom"]="产品BOM|product-bom|ep:files"
    ["product_bom_item"]="BOM明细|product-bom-item|ep:list"
    ["process_route"]="工艺路线|process-route|ep:connection"
    ["process_route_item"]="工艺路线明细|process-route-item|ep:list"
    ["production_schedule"]="生产排程|production-schedule|ep:calendar"
    ["production_schedule_item"]="排程明细|production-schedule-item|ep:list"
    ["mrp_params"]="MRP参数|mrp-params|ep:setting"
    ["mrp_result"]="MRP运算结果|mrp-result|ep:data-analysis"
    ["work_order"]="工单|work-order|ep:document"
    ["work_order_progress"]="工单进度|work-order-progress|ep:loading"
    ["quality_standard"]="质检标准|quality-standard|ep:star"
    ["quality_item"]="质检项目|quality-item|ep:list"
    ["quality_inspection"]="质检记录|quality-inspection|ep:edit"
    ["quality_inspection_item"]="质检明细|quality-inspection-item|ep:list"
    ["equipment"]="设备管理|equipment|ep:tools"
    ["equipment_status"]="设备状态|equipment-status|ep:monitor"
    ["cost_standard"]="标准成本|cost-standard|ep:money"
    ["cost_actual"]="实际成本|cost-actual|ep:money"
    ["cost_variance"]="成本差异|cost-variance|ep:data-analysis"
    ["work_hours"]="工时统计|work-hours|ep:time"
    ["production_kpi"]="生产KPI|production-kpi|ep:data-line"
    ["production_report"]="生产报表|production-report|ep:document"
    ["production_dashboard_config"]="看板配置|production-dashboard-config|ep:setting"
)

# 生成SQL文件
cat > "$OUTPUT_FILE" << 'EOF'
-- ========================================
-- ERP 生产管理模块菜单配置
-- 生成时间: $(date '+%Y-%m-%d %H:%M:%S')
-- ========================================

-- 1. 创建生产管理目录（如果不存在）
-- 注意：如果已存在，请手动调整ID
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) 
VALUES (5042, '生产管理', '', 1, 50, 2563, 'production', 'ep:operation', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0')
ON DUPLICATE KEY UPDATE `name`='生产管理';

EOF

# 当前菜单ID（从5043开始）
CURRENT_MENU_ID=5043
CURRENT_SORT=1

# 为每个模块生成菜单SQL
for module_key in "${!MODULES[@]}"; do
    IFS='|' read -r display_name path icon <<< "${MODULES[$module_key]}"
    
    # 转换为业务名称（用于权限标识）
    business_name=$(echo "$module_key" | sed 's/_/-/g')
    
    # 转换为类名（用于组件名）
    class_name=$(echo "$module_key" | sed 's/_\([a-z]\)/\U\1/g' | sed 's/^\([a-z]\)/\U\1/')
    
    # 生成菜单SQL
    cat >> "$OUTPUT_FILE" << EOF

-- ========== ${display_name} ==========
-- 菜单
INSERT INTO \`system_menu\` (\`id\`, \`name\`, \`permission\`, \`type\`, \`sort\`, \`parent_id\`, \`path\`, \`icon\`, \`component\`, \`component_name\`, \`status\`, \`visible\`, \`keep_alive\`, \`always_show\`, \`creator\`, \`create_time\`, \`updater\`, \`update_time\`, \`deleted\`) 
VALUES (${CURRENT_MENU_ID}, '${display_name}', '', 2, ${CURRENT_SORT}, ${PRODUCTION_PARENT_ID}, '${path}', '${icon}', 'erp/${path}/index', '${class_name}', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 按钮权限
SET @parentId = ${CURRENT_MENU_ID};
INSERT INTO \`system_menu\` (\`name\`, \`permission\`, \`type\`, \`sort\`, \`parent_id\`, \`path\`, \`icon\`, \`component\`, \`status\`, \`visible\`, \`keep_alive\`, \`always_show\`, \`creator\`, \`create_time\`, \`updater\`, \`update_time\`, \`deleted\`) 
VALUES 
('${display_name}查询', 'erp:${business_name}:query', 3, 1, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('${display_name}创建', 'erp:${business_name}:create', 3, 2, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('${display_name}更新', 'erp:${business_name}:update', 3, 3, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('${display_name}删除', 'erp:${business_name}:delete', 3, 4, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('${display_name}导出', 'erp:${business_name}:export', 3, 5, @parentId, '', '', '', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

EOF
    
    CURRENT_MENU_ID=$((CURRENT_MENU_ID + 1))
    CURRENT_SORT=$((CURRENT_SORT + 1))
done

echo -e "${GREEN}菜单SQL已生成: ${OUTPUT_FILE}${NC}"
echo -e "${YELLOW}注意: 请检查并调整菜单ID，确保不与现有菜单冲突${NC}"
echo ""
echo "生成的模块数: ${#MODULES[@]}"
echo "菜单ID范围: 5043 - $((CURRENT_MENU_ID - 1))"

