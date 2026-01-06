# ERP系统表检查报告

**检查日期：** 2025-01-03  
**数据库：** ruoyi-vue-pro  
**服务器：** 腾讯云 (101.33.244.240)

## 📊 总体统计

- **ERP表总数：** 74 个
- **数据总大小：** 约 1.2 MB
- **索引总大小：** 约 1.5 MB
- **总大小：** 约 2.7 MB

## 📋 表分类统计

### 1. 基础数据表 (13个)
- erp_account (账户)
- erp_customer (客户)
- erp_supplier (供应商)
- erp_warehouse (仓库)
- erp_product (产品)
- erp_product_category (产品分类)
- erp_product_unit (产品单位)
- erp_product_sku (产品SKU)
- erp_product_sku_relation (产品SKU关联)
- erp_product_oem (产品OEM)
- erp_product_package (产品包装)
- erp_product_bom (产品BOM)
- erp_product_bom_item (产品BOM项)

### 2. 销售管理表 (9个)
- erp_sale_order (销售订单)
- erp_sale_order_items (销售订单项)
- erp_sale_out (销售出库)
- erp_sale_out_items (销售出库项)
- erp_sale_return (销售退货)
- erp_sale_return_items (销售退货项)

### 3. 采购管理表 (6个)
- erp_purchase_order (采购订单)
- erp_purchase_order_items (采购订单项)
- erp_purchase_in (采购入库)
- erp_purchase_in_items (采购入库项)
- erp_purchase_return (采购退货)
- erp_purchase_return_items (采购退货项)

### 4. 库存管理表 (10个)
- erp_stock (库存)
- erp_stock_record (库存记录)
- erp_stock_in (其它入库)
- erp_stock_in_item (其它入库项)
- erp_stock_out (其它出库)
- erp_stock_out_item (其它出库项)
- erp_stock_move (库存调拨)
- erp_stock_move_item (库存调拨项)
- erp_stock_check (库存盘点)
- erp_stock_check_item (库存盘点项)
- erp_stock_internal_in (内部入库)
- erp_stock_internal_in_item (内部入库项)
- erp_stock_internal_out (内部出库)
- erp_stock_internal_out_item (内部出库项)

### 5. 财务管理表 (9个)
- erp_finance_receivable (应收账款)
- erp_finance_payable (应付账款)
- erp_finance_receipt (收款单)
- erp_finance_receipt_item (收款单项)
- erp_finance_payment (付款单)
- erp_finance_payment_item (付款单项)
- erp_finance_prereceipt (预收款)
- erp_finance_prepayment (预付款)
- erp_finance_balance_sheet (资产负债表)
- erp_finance_profit_statement (利润表)

### 6. 生产管理表 (12个)
- erp_production_order (生产订单)
- erp_production_order_items (生产订单项)
- erp_work_order (工单)
- erp_work_order_progress (工单进度)
- erp_process (工序)
- erp_process_route (工艺路线)
- erp_process_route_item (工艺路线项)
- erp_work_center (工作中心)
- erp_work_hours (工时)
- erp_production_schedule (生产计划)
- erp_production_schedule_item (生产计划项)
- erp_production_dashboard_config (生产看板配置)
- erp_production_kpi (生产KPI)
- erp_production_report (生产报表)

### 7. MRP管理表 (2个)
- erp_mrp_params (MRP参数)
- erp_mrp_result (MRP结果)

### 8. 质量管理表 (4个)
- erp_quality_inspection (质量检验)
- erp_quality_inspection_item (质量检验项)
- erp_quality_item (质量项)
- erp_quality_standard (质量标准)

### 9. 成本管理表 (3个)
- erp_cost_actual (实际成本)
- erp_cost_standard (标准成本)
- erp_cost_variance (成本差异)

### 10. 设备管理表 (2个)
- erp_equipment (设备)
- erp_equipment_status (设备状态)

## ⚠️ 字段检查结果

### 标准字段完整性检查

经过验证，所有74个ERP表的标准字段（creator, updater, create_time, update_time, deleted）都已完整。

**已验证的表：**
- erp_cost_variance ✓ (creator, updater 已存在)
- erp_production_kpi ✓ (creator, updater 已存在)
- erp_production_report ✓ (creator, updater 已存在)
- erp_production_schedule ✓ (creator, updater 已存在)
- erp_quality_inspection_item ✓ (creator, updater 已存在)
- erp_quality_item ✓ (creator, updater 已存在)

**结论：** 所有ERP表的标准字段完整，无需修复。

## 📈 数据量统计

### 数据量较大的表（估算行数 > 10）：
- erp_sale_order_items: 13 行
- erp_mrp_params: 30 行
- erp_process: 6 行
- erp_product_bom_item: 12 行
- erp_product_category: 12 行
- erp_stock_record: 10 行

### 空表（0行数据）：
大部分表当前没有数据，这是正常的，因为系统可能刚部署或处于测试阶段。

## ✅ 检查结论

1. **表结构完整性：** ✅ 74个ERP表全部存在
2. **标准字段完整性：** ✅ 所有74个表的标准字段完整（creator, updater, create_time, update_time, deleted）
3. **数据完整性：** 数据量较小，符合新部署系统的特征
4. **表结构状态：** 所有表结构正常，无需修复

## 🔧 后续操作建议

1. ✅ 表结构检查完成，所有字段完整
2. 定期检查表结构完整性（建议每月一次）
3. 监控数据增长情况
4. 根据需要优化索引
5. 定期备份数据库

