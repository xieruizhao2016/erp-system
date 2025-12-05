# 产品BOM自动填充功能说明

## 功能概述

在"生产管理 > 产品BOM > 新增"界面，当用户选择产品后，系统会自动填充以下字段的默认值，同时用户可以自行修改这些默认值。

## 自动填充字段

### 1. BOM编号 (`bomNo`)
- **生成规则**: `BOM-{产品编号}-{日期YYYYMMDD}-{序号}`
  - 示例: `BOM-P0001-20251121-01`
- **说明**: 
  - 使用产品的编号(code)，如果没有则使用产品ID补齐到4位
  - 日期为当前日期（YYYYMMDD格式）
  - 序号默认为01（后续可扩展为根据该产品已有BOM数量递增）
- **可修改**: ✅ 是

### 2. BOM名称 (`bomName`)
- **默认规则**: `{产品名称}-BOM`
  - 示例: 如果产品名称为"iPhone 15"，则BOM名称为"iPhone 15-BOM"
- **说明**: 自动取产品名称并追加"-BOM"后缀
- **可修改**: ✅ 是

### 3. 版本号 (`version`)
- **默认值**: `1.0`
- **说明**: 新增BOM时默认版本为1.0
- **可修改**: ✅ 是

### 4. 状态 (`status`)
- **默认值**: `2`（生效）
- **可选值**:
  - 1: 草稿
  - 2: 生效
  - 3: 失效
- **说明**: 新增BOM时默认为"生效"状态
- **可修改**: ✅ 是

### 5. BOM类型 (`bomType`)
- **默认值**: `1`（生产BOM）
- **可选值**:
  - 1: 生产BOM
  - 2: 设计BOM
  - 3: 工艺BOM
- **说明**: 新增BOM时默认为"生产BOM"
- **可修改**: ✅ 是

### 6. 生效日期 (`effectiveDate`)
- **默认值**: 当前日期
- **说明**: 自动填充为今天的日期
- **可修改**: ✅ 是

## 使用说明

### 操作步骤

1. 进入"生产管理 > 产品BOM"页面
2. 点击"新增"按钮
3. **选择产品**：在产品下拉框中选择需要创建BOM的产品
4. **自动填充**：系统自动填充以上字段的默认值
5. **修改（可选）**：如需修改默认值，可直接在对应输入框中编辑
6. 填写其他必填字段（如标准成本、总材料成本等）
7. 点击"确定"按钮提交

### 注意事项

1. **仅新增模式生效**：自动填充功能仅在"新增"模式下生效，"编辑"模式不会触发自动填充
2. **产品为必选项**：必须先选择产品才会触发自动填充
3. **可随时修改**：所有自动填充的字段都可以手动修改，系统不会强制使用默认值
4. **不会覆盖已填值**：如果用户已经手动填写了某个字段，再次更改产品时不会覆盖该字段

## 技术实现

### 核心代码
```vue
// 监听产品选择变化
watch(
  () => formData.value.productId,
  (newProductId) => {
    if (formType.value === 'create' && newProductId) {
      const selectedProduct = productList.value.find((p) => p.id === newProductId)
      if (selectedProduct) {
        // 自动填充各字段
        formData.value.bomNo = generateBomNo(selectedProduct)
        formData.value.bomName = `${selectedProduct.name}-BOM`
        formData.value.version = '1.0'
        formData.value.status = 2 // 生效
        formData.value.bomType = 1 // 生产BOM
        formData.value.effectiveDate = new Date().getTime()
      }
    }
  }
)
```

### 生成规则函数
```typescript
/**
 * 生成BOM编号
 * 规则：BOM + 产品编号 + 日期(YYYYMMDD) + 序号(2位)
 */
const generateBomNo = (product: ProductVO): string => {
  const dateStr = formatDate(new Date(), 'YYYYMMDD')
  const productNo = product.code || product.id?.toString().padStart(4, '0') || '0000'
  const seq = '01'
  return `BOM-${productNo}-${dateStr}-${seq}`
}
```

## 扩展建议

### 未来优化方向

1. **BOM编号序号优化**
   - 可以查询该产品当天已有的BOM数量
   - 自动递增序号（01、02、03...）
   - 避免编号冲突

2. **版本号智能递增**
   - 查询该产品最新的BOM版本
   - 自动建议下一个版本号（如当前最高版本为1.2，则建议1.3）

3. **BOM类型智能推荐**
   - 根据产品类型或产品分类推荐合适的BOM类型
   - 例如：原材料 → 设计BOM，成品 → 生产BOM

4. **模板功能**
   - 支持保存常用的BOM配置为模板
   - 选择产品后可以快速应用模板

## 测试场景

### 测试用例

1. **正常创建测试**
   - 步骤：新增BOM → 选择产品 → 查看自动填充
   - 预期：所有字段正确填充

2. **修改默认值测试**
   - 步骤：自动填充后 → 修改BOM名称 → 提交
   - 预期：使用修改后的值保存

3. **编辑模式测试**
   - 步骤：编辑已有BOM → 更改产品
   - 预期：不触发自动填充，保持原有数据

4. **切换产品测试**
   - 步骤：选择产品A → 切换到产品B
   - 预期：自动填充更新为产品B的值（如果字段未手动修改）

## 修改记录

| 日期 | 版本 | 修改内容 | 修改人 |
|------|------|---------|--------|
| 2025-11-21 | v1.0 | 初始实现，支持6个字段的自动填充 | - |

---

**相关文件**:
- 前端表单: `original-yudao-ui/src/views/erp/productbom/ProductBomForm.vue`
- 字典配置: `sql/mysql/erp_production_dict.sql`

