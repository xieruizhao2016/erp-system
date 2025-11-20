# MRP参数一键生成功能说明

## 📋 功能概述

为了简化MRP参数的初始化配置，我们新增了**一键生成默认MRP参数**功能。用户只需点击一个按钮，系统就会自动创建10个常用的MRP参数，无需手工逐个录入。

---

## ✅ 已完成的功能

### 1. 后端开发
- ✅ `MrpParamsService.generateDefaultParams()` - 生成默认参数的Service方法
- ✅ `POST /erp/mrp-params/generate-default` - 生成默认参数的API接口

### 2. 前端开发
- ✅ `MrpParamsApi.generateDefaultParams()` - 前端API调用方法
- ✅ 在MRP参数页面添加"生成默认参数"按钮
- ✅ 带二次确认提示和加载状态

### 3. 权限配置
- ✅ `erp:mrp-params:generate-default` - 生成默认参数权限
- ✅ SQL脚本：`add_mrp_params_generate_permission.sql`

---

## 🎯 默认生成的10个MRP参数

### 系统参数（7个）

| 参数名称 | 参数编码 | 默认值 | 说明 |
|---------|---------|--------|------|
| 安全库存比例 | SAFETY_STOCK_RATIO | 0.1 | 10%安全库存，用于MRP运算 |
| 默认提前期 | DEFAULT_LEAD_TIME | 7 | 默认7天提前期（天数） |
| 批量规则 | LOT_SIZING_RULE | 2 | 2-按需，1-固定批量，3-最小-最大 |
| 计划展望期 | PLANNING_HORIZON | 90 | 未来90天（3个月） |
| 考虑在途数量 | CONSIDER_IN_TRANSIT | true | 是否考虑在途采购/生产 |
| 自动展开BOM | AUTO_EXPAND_BOM | true | 是否自动展开BOM层级 |
| BOM展开层级 | BOM_EXPAND_LEVEL | 10 | 最大展开10层 |

### 业务参数（3个）

| 参数名称 | 参数编码 | 默认值 | 说明 |
|---------|---------|--------|------|
| 固定批量数量 | FIXED_LOT_SIZE | 100 | 固定批量模式的数量 |
| 最小批量 | MIN_LOT_SIZE | 10 | 最小采购/生产批量 |
| 最大批量 | MAX_LOT_SIZE | 1000 | 最大采购/生产批量 |

---

## 🚀 使用步骤

### 1. 执行权限SQL

```bash
mysql -u root -p your_database < sql/mysql/add_mrp_params_generate_permission.sql
```

### 2. 分配权限

在【系统管理 → 角色管理】中，给相关角色分配 `MRP参数生成默认` 权限。

### 3. 一键生成

1. 进入【生产管理 → MRP参数】页面
2. 点击 🪄 **生成默认参数** 按钮
3. 确认生成
4. 系统自动创建10个默认参数

### 4. 查看和调整

生成后可以：
- 查看生成的参数列表
- 根据实际业务需求修改参数值
- 删除不需要的参数
- 新增自定义参数

---

## 💡 功能特点

### 1. 智能去重
- 系统会检查参数编码是否已存在
- 已存在的参数不会重复创建
- 可以多次点击生成，不会产生重复数据

### 2. 安全可靠
- 二次确认机制，防止误操作
- 事务保证，要么全部成功，要么全部回滚
- 详细的日志记录

### 3. 灵活配置
- 生成的参数都可以手工修改
- 支持删除不需要的参数
- 支持新增自定义参数

### 4. 用户友好
- 一键操作，简单快捷
- 显示生成数量反馈
- 按钮带加载状态

---

## 📊 参数说明

### 参数类型

| 类型值 | 类型名称 | 说明 |
|-------|---------|------|
| 1 | 字符串 | 文本类型参数 |
| 2 | 数字 | 数值类型参数 |
| 3 | 日期 | 日期时间类型 |
| 4 | 布尔 | true/false |

### 是否系统参数

- **系统参数（true）**：核心参数，建议不要删除
- **业务参数（false）**：可根据实际需求调整或删除

---

## 🔧 参数使用场景

### 1. MRP运算时使用的参数

```
执行MRP运算 → 读取参数：
├── SAFETY_STOCK_RATIO (计算安全库存)
├── DEFAULT_LEAD_TIME (计算需求日期)
├── LOT_SIZING_RULE (确定批量规则)
├── PLANNING_HORIZON (确定计划范围)
├── CONSIDER_IN_TRANSIT (是否考虑在途)
├── AUTO_EXPAND_BOM (是否展开BOM)
└── BOM_EXPAND_LEVEL (展开层级)
```

### 2. 批量计算时使用的参数

```
计算订单批量 → 读取参数：
├── FIXED_LOT_SIZE (固定批量数)
├── MIN_LOT_SIZE (最小批量)
└── MAX_LOT_SIZE (最大批量)
```

---

## 📝 代码示例

### 后端Service方法

```java
@Override
@Transactional(rollbackFor = Exception.class)
public Integer generateDefaultParams() {
    log.info("[MRP参数] 开始生成默认MRP参数");
    
    // 定义默认参数列表
    List<MrpParamsDO> defaultParams = new ArrayList<>();
    
    // 添加默认参数...
    
    // 批量插入（自动去重）
    int insertCount = 0;
    for (MrpParamsDO param : defaultParams) {
        List<MrpParamsDO> existingParams = 
            mrpParamsMapper.selectList("param_code", param.getParamCode());
        if (CollUtil.isEmpty(existingParams)) {
            mrpParamsMapper.insert(param);
            insertCount++;
        }
    }
    
    return insertCount;
}
```

### 前端调用示例

```typescript
/** 生成默认参数 */
const handleGenerateDefault = async () => {
  try {
    // 二次确认
    await message.confirm('确认要生成默认MRP参数吗？')
    
    // 调用API
    generateLoading.value = true
    const count = await MrpParamsApi.generateDefaultParams()
    
    // 显示结果
    message.success(`成功生成${count}个默认参数`)
    await getList()
  } finally {
    generateLoading.value = false
  }
}
```

---

## ⚙️ 参数调整建议

根据企业实际情况，建议调整以下参数：

### 制造型企业

```
安全库存比例：0.15（15%）- 生产周期长
默认提前期：10天 - 考虑生产准备时间
批量规则：1（固定批量）- 标准化生产
固定批量数量：50 - 根据产线产能
```

### 贸易型企业

```
安全库存比例：0.05（5%）- 库存周转快
默认提前期：3天 - 采购周期短
批量规则：2（按需）- 灵活采购
最小批量：1 - 小批量采购
```

### 定制型企业

```
安全库存比例：0（无安全库存）- 按单生产
批量规则：2（按需）- 完全按订单
自动展开BOM：true - 定制产品BOM复杂
```

---

## 🔍 常见问题

### Q1: 生成后可以修改参数值吗？
**A:** 可以。生成的参数都是可以编辑的，点击"编辑"按钮即可修改。

### Q2: 可以多次点击生成吗？
**A:** 可以。系统会自动检查参数编码，已存在的参数不会重复创建。

### Q3: 不需要某个参数可以删除吗？
**A:** 可以删除，但建议保留系统参数。如果删除了系统参数，MRP运算可能会使用默认值。

### Q4: 如何知道哪些参数是必需的？
**A:** 标记为"系统参数"的7个参数是MRP运算的核心参数，建议保留。

### Q5: 生成失败怎么办？
**A:** 检查数据库连接和权限配置。如果问题持续，查看系统日志获取详细错误信息。

---

## 📂 相关文件清单

### 后端文件

```
yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/
├── service/mrpparams/
│   ├── MrpParamsService.java           # Service接口（新增方法）
│   └── MrpParamsServiceImpl.java       # Service实现（生成逻辑）
└── controller/admin/mrpparams/
    └── MrpParamsController.java        # Controller（新增API）
```

### 前端文件

```
original-yudao-ui/src/
├── api/erp/mrpparams/
│   └── index.ts                        # API调用方法
└── views/erp/mrpparams/
    └── index.vue                       # 页面（新增按钮）
```

### 数据库文件

```
sql/mysql/
└── add_mrp_params_generate_permission.sql  # 权限配置
```

---

## 🎉 总结

通过这个功能，用户可以：
1. **快速初始化** - 一键生成10个常用MRP参数
2. **省时省力** - 无需手工逐个录入参数
3. **开箱即用** - 默认值经过优化，适合大多数场景
4. **灵活调整** - 生成后可根据实际需求修改

这极大地简化了MRP功能的初始化配置工作，让用户能够更快地开始使用MRP运算功能！

---

**开发完成时间**：2025-11-20  
**开发者**：芋道源码  
**版本**：v1.0

