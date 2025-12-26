#!/bin/bash
# 对比本地和服务器上的菜单配置

set -e

GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}对比本地和服务器上的菜单配置${NC}"
echo -e "${GREEN}========================================${NC}"
echo ""

# 检查服务器上的财务管理菜单
echo -e "${YELLOW}[1/3] 检查服务器上的财务管理菜单...${NC}"
SERVER_MENUS=$(ssh -i tengxunyun.pem ubuntu@101.33.244.240 \
  "docker exec yudao-server-prod sh -c 'mysql -uroot -p123456 ruoyi-vue-pro -N -e \"SELECT name FROM system_menu WHERE parent_id = (SELECT id FROM system_menu WHERE name = \\\"财务管理\\\" LIMIT 1) AND status = 0 AND deleted = 0 ORDER BY sort;\" 2>/dev/null'")

if [ -z "$SERVER_MENUS" ]; then
  echo -e "${RED}✗ 无法连接到服务器或查询失败${NC}"
else
  echo "服务器上的财务管理子菜单："
  echo "$SERVER_MENUS" | while read menu; do
    echo "  - $menu"
  done
  SERVER_COUNT=$(echo "$SERVER_MENUS" | wc -l)
  echo -e "${GREEN}服务器菜单数量: $SERVER_COUNT${NC}"
fi
echo ""

# 本地期望的菜单
echo -e "${YELLOW}[2/3] 本地期望的财务管理菜单...${NC}"
EXPECTED_MENUS=("付款单" "收款单" "应收账款" "应付账款" "预付款" "预收款" "资产负债表" "利润表" "结算账户")
echo "本地期望的菜单："
for menu in "${EXPECTED_MENUS[@]}"; do
  echo "  - $menu"
done
echo -e "${GREEN}本地期望菜单数量: ${#EXPECTED_MENUS[@]}${NC}"
echo ""

# 对比
echo -e "${YELLOW}[3/3] 对比结果...${NC}"
if [ ! -z "$SERVER_MENUS" ]; then
  MISSING_MENUS=()
  for menu in "${EXPECTED_MENUS[@]}"; do
    if ! echo "$SERVER_MENUS" | grep -q "$menu"; then
      MISSING_MENUS+=("$menu")
    fi
  done
  
  if [ ${#MISSING_MENUS[@]} -eq 0 ]; then
    echo -e "${GREEN}✓ 服务器上的菜单配置完整${NC}"
  else
    echo -e "${RED}✗ 服务器上缺少以下菜单:${NC}"
    for menu in "${MISSING_MENUS[@]}"; do
      echo -e "  ${RED}- $menu${NC}"
    done
    echo ""
    echo -e "${YELLOW}建议：执行以下SQL脚本更新服务器上的菜单配置${NC}"
    echo "  sql/mysql/erp-finance-menus.sql"
  fi
else
  echo -e "${YELLOW}⚠ 无法获取服务器菜单数据，请手动检查${NC}"
fi

