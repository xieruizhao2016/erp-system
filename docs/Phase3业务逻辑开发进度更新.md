# Phase 3 业务逻辑开发进度更新

**更新时间**: 2025-12-09  
**状态**: 🚧 进行中

---

## ✅ 最新完成

### 内部出入库业务逻辑（部分完成）

- ✅ **包名修复**: 修复了所有内部出入库相关文件的包名（去掉连字符）
- ✅ **单号生成**: 在`ErpNoRedisDAO`中添加了内部出入库单号前缀
  - `STOCK_INTERNAL_IN_NO_PREFIX = "NBRK"` (内部入库)
  - `STOCK_INTERNAL_OUT_NO_PREFIX = "NBCK"` (内部出库)
- ✅ **审核状态更新**: 添加了`updateStockInternalInStatus`方法
- ✅ **Mapper方法**: 添加了`selectByNo`和`updateByIdAndStatus`方法
- ⏳ **明细项处理**: 待实现（需要创建明细项DO和Mapper）
- ⏳ **库存记录创建**: 待实现（需要明细项支持）

---

## 📋 待完成事项

### 1. 内部出入库明细项

需要创建以下文件：
- `ErpStockInternalInItemDO` - 内部入库明细项DO
- `ErpStockInternalInItemMapper` - 内部入库明细项Mapper
- `ErpStockInternalOutItemDO` - 内部出库明细项DO
- `ErpStockInternalOutItemMapper` - 内部出库明细项Mapper

### 2. 完善内部出入库业务逻辑

- ⏳ 添加明细项的校验和处理
- ⏳ 添加库存记录创建（审核通过时）
- ⏳ 添加库存记录取消（反审核时）
- ⏳ 实现内部出库的完整业务逻辑（参考内部入库）

### 3. 预付款/预收款业务逻辑

- ⏳ 预付款：采购订单审核时创建预付款记录
- ⏳ 预收款：销售订单审核时创建预收款记录
- ⏳ 预付款核销：采购入库单审核时自动核销
- ⏳ 预收款核销：销售出库单审核时自动核销

### 4. 销售订单毛利率计算

- ⏳ 原材料成本计算服务
- ⏳ 员工成本计算服务
- ⏳ 在销售订单保存/更新时自动计算毛利率

### 5. 资产负债表/利润表计算

- ⏳ 资产负债表自动计算逻辑
- ⏳ 利润表自动计算逻辑

---

## 🔧 技术细节

### 内部入库审核流程

```java
// 1. 校验存在和状态
// 2. 更新状态
// 3. 创建库存记录（需要明细项支持）
Integer bizType = approve ? ErpStockRecordBizTypeEnum.INTERNAL_IN.getType()
        : ErpStockRecordBizTypeEnum.INTERNAL_IN_CANCEL.getType();
// 遍历明细项，创建库存记录
```

### 库存记录业务类型

已在`ErpStockRecordBizTypeEnum`中定义：
- `INTERNAL_IN(90, "内部入库")`
- `INTERNAL_IN_CANCEL(91, "内部入库（作废）")`
- `INTERNAL_OUT(92, "内部出库")`
- `INTERNAL_OUT_CANCEL(93, "内部出库（作废）")`

---

## 📝 注意事项

1. **明细项文件缺失**: 代码生成器可能没有生成明细项文件，需要手动创建
2. **错误码**: 部分错误码使用了占位符，需要添加对应的错误码定义
3. **库存记录**: 需要依赖`ErpStockRecordService`创建库存记录

---

**下一步**: 创建明细项文件，完善内部出入库的完整业务逻辑。

