# Phase 3 业务逻辑开发指南

**状态**: 待开始  
**前提条件**: Phase 2 代码生成完成

---

## 一、财务模块业务逻辑开发

### 1.1 应收账款自动生成

#### 位置
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/sale/ErpSaleOrderServiceImpl.java`

#### 修改方法
`updateSaleOrderStatus(Long id, Integer status)`

#### 实现步骤

1. **在审核通过时创建应收账款**

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void updateSaleOrderStatus(Long id, Integer status) {
    boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
    // ... 现有代码 ...
    
    // 2. 更新状态
    int updateCount = saleOrderMapper.updateByIdAndStatus(id, saleOrder.getStatus(),
            new ErpSaleOrderDO().setStatus(status));
    if (updateCount == 0) {
        throw exception(approve ? SALE_ORDER_APPROVE_FAIL : SALE_ORDER_PROCESS_FAIL);
    }
    
    // 3. 审核通过时，自动创建应收账款
    if (approve) {
        financeReceivableService.createReceivableFromSaleOrder(saleOrder);
    } else {
        // 反审核时，删除应收账款（如果存在）
        financeReceivableService.deleteReceivableByOrderId(id);
    }
}
```

2. **创建应收账款Service方法**

需要在 `ErpFinanceReceivableService` 中添加：

```java
/**
 * 从销售订单创建应收账款
 */
void createReceivableFromSaleOrder(ErpSaleOrderDO saleOrder);

/**
 * 根据订单ID删除应收账款
 */
void deleteReceivableByOrderId(Long orderId);
```

3. **实现应收账款创建逻辑**

在 `ErpFinanceReceivableServiceImpl` 中实现：

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void createReceivableFromSaleOrder(ErpSaleOrderDO saleOrder) {
    // 1. 检查是否已存在
    ErpFinanceReceivableDO existing = financeReceivableMapper.selectByOrderId(saleOrder.getId());
    if (existing != null) {
        return; // 已存在，不重复创建
    }
    
    // 2. 生成单据号
    String no = noRedisDAO.generate(ErpNoRedisDAO.FINANCE_RECEIVABLE_NO_PREFIX);
    
    // 3. 计算到期日（默认订单日期+30天）
    LocalDate dueDate = saleOrder.getOrderTime() != null 
        ? saleOrder.getOrderTime().toLocalDate().plusDays(30)
        : LocalDate.now().plusDays(30);
    
    // 4. 创建应收账款
    ErpFinanceReceivableDO receivable = new ErpFinanceReceivableDO();
    receivable.setNo(no);
    receivable.setCustomerId(saleOrder.getCustomerId());
    receivable.setOrderId(saleOrder.getId());
    receivable.setAmount(saleOrder.getTotalPrice());
    receivable.setReceivedAmount(BigDecimal.ZERO);
    receivable.setBalance(saleOrder.getTotalPrice());
    receivable.setDueDate(dueDate);
    receivable.setStatus(ErpAuditStatus.PROCESS.getStatus());
    receivable.setRemark("自动生成自销售订单：" + saleOrder.getNo());
    
    financeReceivableMapper.insert(receivable);
}
```

### 1.2 应收账款核销

#### 位置
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/finance/ErpFinanceReceiptServiceImpl.java`

#### 修改方法
`updateFinanceReceiptStatus(Long id, Integer status)`

#### 实现步骤

在收款单审核通过后，自动核销应收账款：

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void updateFinanceReceiptStatus(Long id, Integer status) {
    // ... 现有代码 ...
    
    // 审核通过后，核销应收账款
    if (ErpAuditStatus.APPROVE.getStatus().equals(status)) {
        financeReceivableService.writeOff(receipt.getCustomerId(), receipt.getReceiptPrice());
    }
}
```

在 `ErpFinanceReceivableService` 中添加核销方法：

```java
/**
 * 核销应收账款
 */
void writeOff(Long customerId, BigDecimal amount);
```

实现核销逻辑：

```java
@Override
@Transactional(rollbackFor = Exception.class)
public void writeOff(Long customerId, BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        return;
    }
    
    // 获取该客户的未核销应收账款（按到期日排序）
    List<ErpFinanceReceivableDO> receivables = financeReceivableMapper.selectListByCustomerIdAndBalance(
        customerId, BigDecimal.ZERO);
    
    BigDecimal remainingAmount = amount;
    for (ErpFinanceReceivableDO receivable : receivables) {
        if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
            break;
        }
        
        BigDecimal receivableBalance = receivable.getBalance();
        if (receivableBalance.compareTo(BigDecimal.ZERO) <= 0) {
            continue;
        }
        
        // 计算本次核销金额
        BigDecimal writeOffAmount = remainingAmount.min(receivableBalance);
        
        // 更新应收账款
        BigDecimal newReceivedAmount = receivable.getReceivedAmount().add(writeOffAmount);
        BigDecimal newBalance = receivable.getBalance().subtract(writeOffAmount);
        
        financeReceivableMapper.updateById(
            new ErpFinanceReceivableDO()
                .setId(receivable.getId())
                .setReceivedAmount(newReceivedAmount)
                .setBalance(newBalance)
        );
        
        remainingAmount = remainingAmount.subtract(writeOffAmount);
    }
}
```

### 1.3 应付账款自动生成

#### 位置
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/purchase/ErpPurchaseOrderServiceImpl.java`

#### 修改方法
`updatePurchaseOrderStatus(Long id, Integer status)`

实现方式与应收账款类似，在采购订单审核通过时自动创建应付账款。

### 1.4 应付账款核销

#### 位置
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/finance/ErpFinancePaymentServiceImpl.java`

实现方式与应收账款核销类似。

### 1.5 资产负债表和利润表自动计算

需要在对应的Service中实现计算方法，并提供手动计算接口。

---

## 二、内部出入库业务逻辑开发

### 2.1 参考现有实现

参考以下文件：
- `ErpStockInServiceImpl.java` - 入库实现
- `ErpStockOutServiceImpl.java` - 出库实现

### 2.2 实现要点

1. **创建单据**: 生成单号，设置初始状态为"未审核"
2. **审核通过**: 更新库存，创建库存记录（使用 `ErpStockRecordBizTypeEnum.INTERNAL_IN` 或 `INTERNAL_OUT`）
3. **审核不通过**: 不更新库存

### 2.3 库存更新逻辑

```java
// 审核通过后更新库存
if (ErpAuditStatus.APPROVE.getStatus().equals(status)) {
    for (ErpStockInternalInItemDO item : items) {
        stockService.addStock(
            item.getProductId(),
            item.getWarehouseId(),
            item.getCount(),
            ErpStockRecordBizTypeEnum.INTERNAL_IN.getType(),
            internalIn.getId(),
            item.getId(),
            internalIn.getNo()
        );
    }
}
```

---

## 三、销售订单毛利率计算

### 3.1 成本计算服务

#### 位置
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/sale/ErpSaleOrderServiceImpl.java`

#### 添加方法

```java
/**
 * 计算销售订单成本
 */
private void calculateCost(ErpSaleOrderDO saleOrder, List<ErpSaleOrderItemDO> items) {
    BigDecimal totalMaterialCost = BigDecimal.ZERO;
    BigDecimal totalLaborCost = BigDecimal.ZERO;
    
    for (ErpSaleOrderItemDO item : items) {
        // 计算原材料成本（从BOM获取）
        BigDecimal materialCost = calculateMaterialCost(item.getProductId(), item.getCount());
        item.setMaterialCost(materialCost);
        
        // 计算员工成本（从工艺路线获取）
        BigDecimal laborCost = calculateLaborCost(item.getProductId(), item.getCount());
        item.setLaborCost(laborCost);
        
        // 计算行毛利率
        BigDecimal itemTotalCost = materialCost.add(laborCost);
        BigDecimal itemGrossProfitRate = calculateGrossProfitRate(
            item.getTotalPrice(), itemTotalCost);
        item.setGrossProfitRate(itemGrossProfitRate);
        
        totalMaterialCost = totalMaterialCost.add(materialCost);
        totalLaborCost = totalLaborCost.add(laborCost);
    }
    
    // 计算订单总成本
    saleOrder.setMaterialCost(totalMaterialCost);
    saleOrder.setLaborCost(totalLaborCost);
    saleOrder.setTotalCost(totalMaterialCost.add(totalLaborCost));
    
    // 计算订单毛利率
    saleOrder.setGrossProfitRate(calculateGrossProfitRate(
        saleOrder.getTotalPrice(), saleOrder.getTotalCost()));
}

/**
 * 计算原材料成本
 */
private BigDecimal calculateMaterialCost(Long productId, BigDecimal quantity) {
    // 1. 获取产品BOM
    ProductBomDO bom = productBomService.getActiveBom(productId);
    if (bom == null) {
        return BigDecimal.ZERO;
    }
    
    // 2. 遍历BOM明细，计算原材料成本
    BigDecimal totalCost = BigDecimal.ZERO;
    List<ProductBomItemDO> bomItems = productBomItemService.getBomItems(bom.getId());
    for (ProductBomItemDO bomItem : bomItems) {
        // 获取原材料的最新采购价格
        BigDecimal purchasePrice = purchaseOrderService.getLatestPrice(bomItem.getChildProductId());
        if (purchasePrice == null) {
            purchasePrice = BigDecimal.ZERO;
        }
        
        // 计算该原材料的成本 = 采购价格 * BOM数量 * 订单数量
        BigDecimal itemCost = purchasePrice
            .multiply(bomItem.getQuantity())
            .multiply(quantity);
        totalCost = totalCost.add(itemCost);
    }
    
    return totalCost;
}

/**
 * 计算员工成本
 */
private BigDecimal calculateLaborCost(Long productId, BigDecimal quantity) {
    // 1. 获取产品的工艺路线
    ProcessRouteDO route = processRouteService.getActiveRoute(productId);
    if (route == null) {
        return BigDecimal.ZERO;
    }
    
    // 2. 遍历工艺路线明细，计算员工成本
    BigDecimal totalCost = BigDecimal.ZERO;
    List<ProcessRouteItemDO> routeItems = processRouteItemService.getRouteItems(route.getId());
    for (ProcessRouteItemDO routeItem : routeItems) {
        // 获取工序的标准工时
        BigDecimal standardTime = routeItem.getStandardTime();
        
        // 获取员工时薪（从系统配置或员工表获取）
        BigDecimal hourlyRate = getEmployeeHourlyRate(routeItem.getProcessId());
        if (hourlyRate == null) {
            hourlyRate = BigDecimal.ZERO;
        }
        
        // 计算该工序的成本 = 时薪 * 标准工时 * 订单数量
        BigDecimal itemCost = hourlyRate
            .multiply(standardTime)
            .multiply(quantity);
        totalCost = totalCost.add(itemCost);
    }
    
    return totalCost;
}

/**
 * 计算毛利率
 */
private BigDecimal calculateGrossProfitRate(BigDecimal salePrice, BigDecimal totalCost) {
    if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) == 0) {
        return BigDecimal.ZERO;
    }
    if (totalCost == null) {
        totalCost = BigDecimal.ZERO;
    }
    
    // 毛利率 = (销售价格 - 总成本) / 销售价格 × 100%
    BigDecimal profit = salePrice.subtract(totalCost);
    return profit.divide(salePrice, 4, RoundingMode.HALF_UP)
        .multiply(new BigDecimal("100"));
}
```

### 3.2 在保存/更新时自动计算

在 `createSaleOrder` 和 `updateSaleOrder` 方法中调用：

```java
// 计算成本
calculateCost(saleOrder, saleOrderItems);
```

---

## 四、开发顺序建议

1. **先完成代码生成**（Phase 2）
2. **应收账款/应付账款自动生成**（相对独立，可以先做）
3. **应收账款/应付账款核销**（依赖自动生成）
4. **内部出入库业务逻辑**（参考现有实现）
5. **销售订单毛利率计算**（需要BOM和工艺路线数据）

---

## 五、注意事项

1. **事务管理**: 所有涉及数据库操作的方法都要使用 `@Transactional`
2. **异常处理**: 确保异常时能正确回滚
3. **数据校验**: 在创建应收账款/应付账款前，检查是否已存在
4. **反审核处理**: 反审核时要删除或回退自动生成的记录
5. **成本计算**: BOM和工艺路线不存在时，成本为0，毛利率为100%

---

完成以上业务逻辑开发后，Phase 3 就完成了！

