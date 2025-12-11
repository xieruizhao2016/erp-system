# Phase 3 业务逻辑开发进度

**更新时间**: 2025-12-09  
**状态**: 🚧 进行中

---

## ✅ 已完成

### 1. 应收账款业务逻辑

- ✅ **自动生成**: 销售订单审核通过时自动创建应收账款
- ✅ **自动删除**: 销售订单反审核时删除未审核的应收账款
- ✅ **自动核销**: 收款单审核通过时自动核销应收账款（按到期日顺序）
- ✅ **Mapper方法**: 添加了`selectByOrderId`和`selectListByCustomerIdAndBalance`方法
- ✅ **单号生成**: 在`ErpNoRedisDAO`中添加了`FINANCE_RECEIVABLE_NO_PREFIX = "YSZK"`

### 2. 应付账款业务逻辑

- ✅ **自动生成**: 采购订单审核通过时自动创建应付账款
- ✅ **自动删除**: 采购订单反审核时删除未审核的应付账款
- ✅ **自动核销**: 付款单审核通过时自动核销应付账款（按到期日顺序）
- ✅ **Mapper方法**: 添加了`selectByOrderId`和`selectListBySupplierIdAndBalance`方法
- ✅ **单号生成**: 在`ErpNoRedisDAO`中添加了`FINANCE_PAYABLE_NO_PREFIX = "YFZK"`

### 3. 代码修复

- ✅ 修复了所有Service和Mapper的包名问题（去掉连字符）
- ✅ 修复了模板变量（`${primaryColumn.javaType}` → `Long`）
- ✅ 添加了必要的导入语句

---

## ⏳ 待完成

### 1. 预付款/预收款业务逻辑

- ⏳ 预付款：采购订单审核时，如果选择了预付款，需要创建预付款记录
- ⏳ 预收款：销售订单审核时，如果选择了预收款，需要创建预收款记录
- ⏳ 预付款核销：采购入库单审核时，自动核销预付款
- ⏳ 预收款核销：销售出库单审核时，自动核销预收款

### 2. 内部出入库业务逻辑

- ⏳ 参考现有的`ErpStockInService`和`ErpStockOutService`实现
- ⏳ 内部入库：审核时更新库存，创建库存记录
- ⏳ 内部出库：审核时更新库存，创建库存记录
- ⏳ 关联员工/部门信息

### 3. 销售订单毛利率计算

- ⏳ 原材料成本计算服务
- ⏳ 员工成本计算服务
- ⏳ 在销售订单保存/更新时自动计算毛利率
- ⏳ 总成本 = 原材料成本 + 员工成本
- ⏳ 毛利率 = (总价 - 总成本) / 总价 * 100

### 4. 资产负债表/利润表计算

- ⏳ 资产负债表自动计算逻辑
- ⏳ 利润表自动计算逻辑
- ⏳ 定时任务或手动触发计算

---

## 📝 实现细节

### 应收账款核销逻辑

```java
// 按到期日顺序核销，先到期的先核销
List<ErpFinanceReceivableDO> receivables = financeReceivableMapper
    .selectListByCustomerIdAndBalance(customerId, BigDecimal.ZERO);
// 遍历核销，直到金额用完
```

### 应付账款核销逻辑

```java
// 按到期日顺序核销，先到期的先核销
List<ErpFinancePayableDO> payables = financePayableMapper
    .selectListBySupplierIdAndBalance(supplierId, BigDecimal.ZERO);
// 遍历核销，直到金额用完
```

---

## 🔗 相关文件

- `ErpFinanceReceivableService` - 应收账款服务接口
- `ErpFinanceReceivableServiceImpl` - 应收账款服务实现
- `ErpFinancePayableService` - 应付账款服务接口
- `ErpFinancePayableServiceImpl` - 应付账款服务实现
- `ErpSaleOrderServiceImpl` - 销售订单服务（集成应收账款生成）
- `ErpPurchaseOrderServiceImpl` - 采购订单服务（集成应付账款生成）
- `ErpFinanceReceiptServiceImpl` - 收款单服务（集成应收账款核销）
- `ErpFinancePaymentServiceImpl` - 付款单服务（集成应付账款核销）

---

**下一步**: 继续实现预付款/预收款、内部出入库和毛利率计算逻辑。

