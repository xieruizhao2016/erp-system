# ERP系统菜单完整对比报告

生成时间: 2025-12-05

## 📊 数据库菜单统计

**总计**: 47个ERP相关菜单项

### 按父菜单分类

| 父菜单ID | 父菜单名称 | 子菜单数量 | 状态 |
|---------|-----------|-----------|------|
| 2702 | ERP 首页 | 1 | ✅ |
| 2564 | 产品管理 | 6 | ✅ |
| 2583 | 库存管理 | 7 | ✅ |
| 2602 | 采购管理 | 4 | ✅ |
| 2617 | 销售管理 | 4 | ✅ |
| 2645 | 财务管理 | 3 | ✅ |
| 5042 | 生产管理 | 15 | ✅ |
| 5100 | 质量管理 | 4 | ✅ |
| 5200 | 设备管理 | 2 | ✅ |
| 5300 | 成本管理 | 3 | ✅ |

---

## 📋 完整菜单列表

### 1. ERP首页 (parent_id = 2702)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2702 | ERP 首页 | home | erp/home/index |

### 2. 产品管理 (parent_id = 2564)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2565 | 产品信息 | product | erp/product/product/index |
| 2571 | 产品分类 | product-category | erp/product/category/index |
| 2577 | 产品单位 | unit | erp/product/unit/index |
| 5361 | SKU管理 | productsku | erp/productsku/index |
| 5355 | 产品包装 | productpackage | erp/productpackage/index |
| 5349 | 产品OEM | productoem | erp/productoem/index |

### 3. 库存管理 (parent_id = 2583)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2584 | 仓库信息 | warehouse | erp/stock/warehouse/index |
| 2590 | 产品库存 | stock | erp/stock/stock/index |
| 2593 | 出入库明细 | record | erp/stock/record/index |
| 2596 | 其它入库 | in | erp/stock/in/index |
| 2610 | 其它出库 | out | erp/stock/out/index |
| 2624 | 库存调拨 | move | erp/stock/move/index |
| 2631 | 库存盘点 | check | erp/stock/check/index |

### 4. 采购管理 (parent_id = 2602)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2603 | 供应商信息 | supplier | erp/purchase/supplier/index |
| 2666 | 采购订单 | order | erp/purchase/order/index |
| 2673 | 采购入库 | in | erp/purchase/in/index |
| 2680 | 采购退货 | return | erp/purchase/return/index |

### 5. 销售管理 (parent_id = 2617)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2618 | 客户信息 | customer | erp/sale/customer/index |
| 2638 | 销售订单 | order | erp/sale/order/index |
| 2652 | 销售出库 | out | erp/sale/out/index |
| 2659 | 销售退货 | return | erp/sale/return/index |

### 6. 财务管理 (parent_id = 2645)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 2646 | 结算账户 | account | erp/finance/account/index |
| 2687 | 付款单 | payment | erp/finance/payment/index |
| 2694 | 收款单 | receipt | erp/finance/receipt/index |

### 7. 生产管理 (parent_id = 5042)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 5049 | 生产订单 | production-order | erp/productionorder/index |
| 5050 | 产品BOM | product-bom | erp/productbom/index |
| 5051 | BOM明细 | product-bom-item | erp/productbomitem/index |
| 5052 | 工艺路线 | process-route | erp/processroute/index |
| 5053 | 工艺路线明细 | process-route-item | erp/processrouteitem/index |
| 5054 | 生产排程 | production-schedule | erp/productionschedule/index |
| 5055 | 排程明细 | production-schedule-item | erp/productionscheduleitem/index |
| 5056 | MRP参数 | mrp-params | erp/mrpparams/index |
| 5057 | MRP运算结果 | mrp-result | erp/mrpresult/index |
| 5058 | 工单 | work-order | erp/workorder/index |
| 5059 | 工单进度 | work-order-progress | erp/workorderprogress/index |
| 5069 | 工时统计 | work-hours | erp/workhours/index |
| 5070 | 生产KPI | production-kpi | erp/productionkpi/index |
| 5071 | 生产报表 | production-report | erp/productionreport/index |
| 5072 | 看板配置 | production-dashboard-config | erp/productiondashboardconfig/index |

### 8. 质量管理 (parent_id = 5100)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 5060 | 质检标准 | quality-standard | erp/quality/qualitystandard/index |
| 5061 | 质检项目 | quality-item | erp/quality/qualityitem/index |
| 5062 | 质检记录 | quality-inspection | erp/quality/qualityinspection/index |
| 5063 | 质检明细 | quality-inspection-item | erp/quality/qualityinspectionitem/index |

### 9. 设备管理 (parent_id = 5200)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 5064 | 设备管理 | equipment-manage | erp/equipment/equipment/index |
| 5065 | 设备状态 | equipment-status | erp/equipment/equipmentstatus/index |

### 10. 成本管理 (parent_id = 5300)
| ID | 菜单名称 | 路径 | 组件路径 |
|----|---------|------|---------|
| 5066 | 标准成本 | cost-standard | erp/cost/coststandard/index |
| 5067 | 实际成本 | cost-actual | erp/cost/costactual/index |
| 5068 | 成本差异 | cost-variance | erp/cost/costvariance/index |

---

## 📁 前端目录结构

### 前端index.vue文件统计: 48个

#### 已匹配的组件 (47个)
所有数据库菜单都有对应的前端组件文件 ✅

#### 未匹配的前端目录 (1个)
- **process** - 工序管理
  - 路径: `src/views/erp/process/index.vue`
  - 状态: ⚠️ 前端有文件，但数据库中没有对应菜单

---

## 🔍 对比分析结果

### ✅ 匹配情况

1. **完全匹配**: 47个菜单都有对应的前端组件
2. **组件路径一致**: 所有菜单的component路径都与前端文件路径匹配
3. **目录结构清晰**: 前端按功能模块组织，结构合理

### ⚠️ 发现的问题

1. **工序管理菜单缺失**
   - 前端有: `src/views/erp/process/index.vue`
   - 数据库无: 没有对应的菜单项
   - 建议: 检查是否需要添加工序管理菜单，或者该功能已集成到其他模块

2. **设备管理路径不一致**
   - 数据库路径: `equipment-manage`
   - 组件路径: `erp/equipment/equipment/index`
   - 注意: 路径命名可能不够直观

3. **菜单结构扁平化**
   - 生产管理下有15个菜单，建议按分类组织
   - 已有重组SQL文件: `reorganize-production-menus-with-categories.sql`

---

## 📊 统计汇总

| 项目 | 数量 | 状态 |
|------|------|------|
| 数据库菜单总数 | 47 | ✅ |
| 前端组件文件 | 48 | ✅ |
| 完全匹配 | 47 | ✅ |
| 未匹配前端文件 | 1 (process) | ⚠️ |
| 菜单分类数 | 10 | ✅ |

---

## 💡 建议

### 1. 添加工序管理菜单（如果需要）

如果工序管理需要独立菜单，可以执行：
```sql
-- 参考 erp-process-menu.sql
INSERT INTO system_menu (id, name, permission, type, sort, parent_id, path, icon, component, component_name, status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) 
VALUES (6006, '工序管理', '', 2, 3, 5042, 'process', 'ep:setting', 'erp/process/index', 'Process', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
```

### 2. 菜单重组（可选）

如果需要更清晰的菜单结构，可以执行：
```bash
mysql -u root -p123456 ruoyi-vue-pro < sql/mysql/reorganize-production-menus-with-categories.sql
```

这将把生产管理下的菜单按分类组织：
- 基础数据
- 生产计划
- 生产执行
- 质量管理
- 成本管理
- 统计分析

### 3. 验证前端路由

确保所有前端路由配置正确：
```bash
# 检查路由文件
find original-yudao-ui/src/router -name "*.ts" -exec grep -l "erp" {} \;
```

---

## ✅ 结论

- **菜单完整性**: 47/48 (98%) ✅
- **路径匹配度**: 100% ✅
- **结构合理性**: 良好 ✅
- **建议**: 考虑添加工序管理菜单，或确认该功能是否已集成

---

## 📝 查询命令

### 查看所有ERP菜单
```sql
SELECT id, name, parent_id, path, component 
FROM system_menu 
WHERE parent_id IN (2702, 2564, 2583, 2602, 2617, 2645, 5042, 5100, 5200, 5300) 
AND deleted = 0 AND type = 2 
ORDER BY parent_id, sort;
```

### 查看前端组件
```bash
find original-yudao-ui/src/views/erp -name "index.vue" -type f | sort
```

### 对比检查
```bash
# 生成菜单路径列表
docker exec yudao-mysql mysql -uroot -p123456 ruoyi-vue-pro --default-character-set=utf8mb4 -e "SELECT component FROM system_menu WHERE component LIKE 'erp/%' AND deleted = 0 AND type = 2 ORDER BY component;" | tail -n +2 | sed 's|erp/|src/views/erp/|' | sed 's|$|/index.vue|' > /tmp/menu_paths.txt

# 检查文件是否存在
while read path; do
  if [ ! -f "original-yudao-ui/$path" ]; then
    echo "缺失: $path"
  fi
done < /tmp/menu_paths.txt
```

