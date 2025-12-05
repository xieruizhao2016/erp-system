# SKU管理模块开发完成指南

## 📋 当前进度

### ✅ 已完成的工作

1. **数据库表创建** ✅
   - 创建了 `erp_product_sku` 表
   - 产品表添加了 `sku_id` 字段
   - 产品表添加了 `code` 字段（产品编码）
   - 所有字段和索引已创建成功

2. **SQL文件** ✅
   - 文件位置：`sql/mysql/erp_product_sku.sql`
   - 已执行成功

### 🔄 待完成的工作

1. **使用代码生成器生成SKU管理代码**
2. **修改产品表单，添加SKU选择功能**
3. **创建菜单和权限配置**
4. **测试功能**

---

## 🚀 完成步骤

### 步骤1：通过前端界面使用代码生成器

#### 1.1 访问代码生成器

1. 打开浏览器访问：http://localhost:80
2. 使用admin/admin123登录
3. 导航到：`系统管理` → `开发工具` → `代码生成`

#### 1.2 基于数据库创建

1. 点击页面右上角的 `基于数据库创建` 按钮
2. 在弹出的表格中，找到 `erp_product_sku` 表
3. 勾选该表，点击 `确定` 按钮
4. 系统会自动创建代码生成配置

#### 1.3 配置代码生成选项

1. 在代码生成列表中找到 `erp_product_sku` 记录
2. 点击 `编辑` 按钮
3. 配置以下信息：

**基本信息**：
- **模块名**：`erp`
- **业务名**：`productsku` 或 `product-sku`
- **业务描述**：`产品SKU`
- **类名称**：`ProductSku`
- **类描述**：`产品SKU`
- **作者**：`开发团队`
- **上级菜单**：选择 `产品管理` 菜单（如果有的话）

**生成信息**：
- **生成模板**：`标准 CRUD`
- **前端类型**：`Vue3 Element Plus`

**字段配置**：
配置各个字段的显示和操作选项：

| 字段名 | 列表显示 | 表单显示-新增 | 表单显示-编辑 | 查询 | 必填 | 表单类型 |
|--------|---------|--------------|--------------|------|------|----------|
| id | ❌ | ❌ | ❌ | ❌ | - | - |
| sku_code | ✅ | ✅ | ✅ | ✅ | ✅ | 输入框 |
| sku_name | ✅ | ✅ | ✅ | ✅ | ✅ | 输入框 |
| description | ✅ | ✅ | ✅ | ❌ | ❌ | 文本域 |
| status | ✅ | ✅ | ✅ | ✅ | ✅ | 单选框 |
| category_id | ✅ | ✅ | ✅ | ✅ | ❌ | 下拉框 |
| bar_code | ✅ | ✅ | ✅ | ✅ | ❌ | 输入框 |
| standard | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| unit_id | ✅ | ✅ | ✅ | ❌ | ❌ | 下拉框 |
| weight | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| volume | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| cost_price | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| purchase_price | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| sale_price | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| min_price | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| color | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| size | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| material | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| image_url | ✅ | ✅ | ✅ | ❌ | ❌ | 图片上传 |
| sort | ✅ | ✅ | ✅ | ❌ | ❌ | 输入框 |
| remark | ❌ | ✅ | ✅ | ❌ | ❌ | 文本域 |

4. 配置完成后，点击 `提交` 保存配置

#### 1.4 生成并下载代码

1. 回到代码生成列表页
2. 找到 `erp_product_sku` 记录
3. 点击 `生成代码` 按钮
4. 下载ZIP包到本地（例如：`codegen-product-sku.zip`）

#### 1.5 解压并复制代码

```bash
# 进入项目根目录
cd /Users/xierui/Documents/Project/erp-system

# 解压代码包（假设下载到了下载文件夹）
unzip ~/Downloads/codegen-product-sku.zip -d /tmp/codegen-sku

# 复制后端代码
# Controller
cp -r /tmp/codegen-sku/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/productsku \
  yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/

# Service
cp -r /tmp/codegen-sku/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/productsku \
  yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/

# DO
cp -r /tmp/codegen-sku/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/productsku \
  yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/

# Mapper
cp -r /tmp/codegen-sku/yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/productsku \
  yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/

# Mapper XML
cp -r /tmp/codegen-sku/yudao-module-erp/src/main/resources/mapper/productsku \
  yudao-module-erp/src/main/resources/mapper/

# 复制前端代码
# API文件
mkdir -p original-yudao-ui/src/api/erp/productsku
cp /tmp/codegen-sku/yudao-ui-admin-vue3/src/api/erp/productsku/index.ts \
  original-yudao-ui/src/api/erp/productsku/

# Vue页面
cp -r /tmp/codegen-sku/yudao-ui-admin-vue3/src/views/erp/productsku \
  original-yudao-ui/src/views/erp/
```

#### 1.6 手动合并ErrorCode

打开生成的ErrorCode文件，将错误码添加到：
`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/enums/ErrorCodeConstants.java`

```java
// ========== 产品SKU模块 1-XXX-XXX-XXX ==========
ErrorCode PRODUCT_SKU_NOT_EXISTS = new ErrorCode(1_011_XXX_XXX, "产品SKU不存在");
```

---

### 步骤2：修改产品表单，添加SKU选择功能

#### 2.1 修改产品API，添加SKU列表接口

编辑文件：`original-yudao-ui/src/api/erp/productsku/index.ts`

添加简单列表接口（如果生成器未生成）：

```typescript
// 获取产品SKU简单列表
export const getProductSkuSimpleList = async () => {
  return await request.get({ url: `/erp/product-sku/simple-list` })
}
```

#### 2.2 修改产品表单

编辑文件：`original-yudao-ui/src/views/erp/product/product/ProductForm.vue`

**2.2.1 导入SKU API**

在文件顶部添加：

```vue
<script setup lang="ts">
// ... 其他导入
import { getProductSkuSimpleList } from '@/api/erp/productsku'
</script>
```

**2.2.2 添加SKU选择字段到表单**

在产品表单中添加SKU选择框：

```vue
<template>
  <el-form-item label="产品SKU" prop="skuId">
    <el-select
      v-model="formData.skuId"
      clearable
      filterable
      placeholder="请选择产品SKU（可选）"
      class="!w-1/1"
    >
      <el-option
        v-for="item in skuList"
        :key="item.id"
        :label="item.skuName"
        :value="item.id"
      />
    </el-select>
  </el-form-item>
</template>
```

**2.2.3 添加SKU列表数据**

```vue
<script setup lang="ts">
// 在data定义部分添加
const skuList = ref([]) // SKU列表

// 在加载数据函数中添加
const loadSkuList = async () => {
  try {
    const list = await getProductSkuSimpleList()
    skuList.value = list || []
  } catch (error) {
    console.error('加载SKU列表失败:', error)
  }
}

// 在打开表单时加载
const open = async (type: string, id?: number) => {
  // ... 现有代码
  
  // 加载SKU列表
  if (skuList.value.length === 0) {
    await loadSkuList()
  }
  
  // ... 其他代码
}
</script>
```

**2.2.4 更新formData定义**

确保formData包含skuId字段：

```typescript
const formData = ref({
  id: undefined,
  // ... 其他字段
  skuId: undefined,  // 添加SKU ID字段
  // ... 其他字段
})
```

#### 2.3 修改产品列表，显示SKU信息

编辑文件：`original-yudao-ui/src/views/erp/product/product/index.vue`

在产品列表中添加SKU列：

```vue
<el-table-column label="关联SKU" align="center" prop="skuId">
  <template #default="scope">
    <span v-if="scope.row.skuId">SKU-{{ scope.row.skuId }}</span>
    <span v-else style="color: #ccc">-</span>
  </template>
</el-table-column>
```

---

### 步骤3：后端添加简单列表接口

#### 3.1 修改ProductSkuController

编辑文件：`yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/productsku/ProductSkuController.java`

添加简单列表接口：

```java
@GetMapping("/simple-list")
@Operation(summary = "获得产品SKU精简列表")
public CommonResult<List<ProductSkuRespVO>> getProductSkuSimpleList() {
    List<ProductSkuDO> list = productSkuService.getProductSkuSimpleList();
    return success(BeanUtils.toBean(list, ProductSkuRespVO.class));
}
```

#### 3.2 修改ProductSkuService

添加简单列表方法：

```java
// 接口
List<ProductSkuDO> getProductSkuSimpleList();

// 实现
@Override
public List<ProductSkuDO> getProductSkuSimpleList() {
    return productSkuMapper.selectList(
        new LambdaQueryWrapperX<ProductSkuDO>()
            .eq(ProductSkuDO::getStatus, 1) // 只返回启用的SKU
            .orderByAsc(ProductSkuDO::getSort)
            .orderByDesc(ProductSkuDO::getId)
    );
}
```

---

### 步骤4：创建菜单和权限配置

#### 4.1 通过前端界面创建菜单

1. 访问：`系统管理` → `菜单管理`
2. 找到 `产品管理` 菜单（或ERP模块菜单）
3. 点击 `新增` 按钮，添加 `SKU管理` 菜单：

**菜单信息**：
- **菜单类型**：目录 / 菜单
- **菜单名称**：SKU管理
- **上级菜单**：产品管理（或选择合适的父菜单）
- **路由地址**：`productsku`
- **组件路径**：`erp/productsku/index`
- **权限标识**：`erp:product-sku:query`
- **菜单图标**：选择合适的图标
- **显示排序**：设置合适的顺序

4. 点击 `确定` 保存

#### 4.2 添加按钮权限

为SKU管理菜单添加操作按钮权限：

创建以下按钮（在SKU管理菜单下）：

| 按钮名称 | 权限标识 | 类型 |
|---------|---------|------|
| 新增 | `erp:product-sku:create` | 按钮 |
| 编辑 | `erp:product-sku:update` | 按钮 |
| 删除 | `erp:product-sku:delete` | 按钮 |
| 导出 | `erp:product-sku:export` | 按钮 |

#### 4.3 SQL方式创建菜单（可选）

如果需要批量创建，可以执行以下SQL：

```sql
-- 插入SKU管理菜单（假设产品管理的菜单ID为2000）
INSERT INTO `system_menu` (`name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES 
('SKU管理', '', 2, 5, 2000, 'productsku', 'ep:goods', 'erp/productsku/index', 'ErpProductSku', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('SKU查询', 'erp:product-sku:query', 3, 1, LAST_INSERT_ID(), '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('SKU创建', 'erp:product-sku:create', 3, 2, LAST_INSERT_ID()-1, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('SKU更新', 'erp:product-sku:update', 3, 3, LAST_INSERT_ID()-2, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('SKU删除', 'erp:product-sku:delete', 3, 4, LAST_INSERT_ID()-3, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
('SKU导出', 'erp:product-sku:export', 3, 5, LAST_INSERT_ID()-4, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
```

---

### 步骤5：编译和测试

#### 5.1 后端编译

```bash
cd /Users/xierui/Documents/Project/erp-system

# 编译后端
mvn clean package -DskipTests

# 重启后端服务
./scripts/restart-all-services.sh
```

#### 5.2 前端热更新

前端在开发模式下会自动热更新，如需重启：

```bash
cd original-yudao-ui
npm run dev
```

#### 5.3 测试功能

1. **测试SKU管理页面**
   - 访问：`产品管理` → `SKU管理`
   - 测试新增、编辑、删除、查询功能
   - 验证所有字段正确显示和保存

2. **测试产品表单的SKU选择**
   - 访问：`产品管理` → `产品`
   - 点击 `新增` 或 `编辑` 产品
   - 确认SKU下拉框显示正常
   - 选择SKU并保存
   - 验证产品列表中SKU信息正确显示

3. **测试API接口**
   ```bash
   # 获取SKU列表
   curl -X GET "http://localhost:48080/admin-api/erp/product-sku/page?pageNo=1&pageSize=10" \
     -H "Authorization: Bearer YOUR_TOKEN"
   
   # 创建SKU
   curl -X POST "http://localhost:48080/admin-api/erp/product-sku/create" \
     -H "Authorization: Bearer YOUR_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{
       "skuCode": "SKU-001",
       "skuName": "测试SKU",
       "description": "这是一个测试SKU",
       "status": 1
     }'
   ```

---

## 📝 SKU表字段说明

| 字段名 | 类型 | 说明 | 示例 |
|--------|------|------|------|
| `id` | bigint | SKU编号 | 1 |
| `sku_code` | varchar(50) | SKU编码（唯一） | SKU-001 |
| `sku_name` | varchar(100) | SKU名称 | iPhone 15 Pro 256GB 黑色 |
| `description` | varchar(500) | SKU描述 | 这是iPhone 15 Pro的256GB黑色版本 |
| `status` | int | 状态：0-禁用，1-启用 | 1 |
| `category_id` | bigint | 分类编号 | 10 |
| `bar_code` | varchar(50) | SKU条码 | 1234567890123 |
| `standard` | varchar(255) | SKU规格 | 256GB/6.7英寸 |
| `unit_id` | bigint | 单位编号 | 1 |
| `weight` | decimal(20,2) | 重量（kg） | 0.221 |
| `volume` | decimal(20,2) | 体积（立方米） | 0.0001 |
| `cost_price` | decimal(20,2) | 成本价格 | 7000.00 |
| `purchase_price` | decimal(20,2) | 采购价格 | 7500.00 |
| `sale_price` | decimal(20,2) | 销售价格 | 9999.00 |
| `min_price` | decimal(20,2) | 最低价格 | 8999.00 |
| `color` | varchar(50) | 颜色 | 黑色 |
| `size` | varchar(50) | 尺寸 | 6.7英寸 |
| `material` | varchar(100) | 材质 | 钛金属 |
| `image_url` | varchar(500) | 图片URL | /upload/sku/001.jpg |
| `sort` | int | 排序 | 1 |
| `remark` | varchar(500) | 备注 | 热销款 |

---

## 🔍 产品与SKU的关系

### 设计说明

- **一对多关系**：一个产品可以关联多个SKU
- **可选关联**：产品的`sku_id`字段可以为NULL，表示该产品不使用SKU管理
- **使用场景**：
  - 简单产品：不使用SKU，直接管理产品
  - 复杂产品：关联SKU，通过SKU管理不同规格、颜色、尺寸等变体

### 典型使用案例

**案例1：简单产品（不使用SKU）**
- 产品：标准螺丝
- sku_id：NULL
- 说明：产品规格单一，无需SKU管理

**案例2：多规格产品（使用SKU）**
- 产品：iPhone 15 Pro
- SKU列表：
  - SKU-001: iPhone 15 Pro 128GB 黑色
  - SKU-002: iPhone 15 Pro 256GB 黑色
  - SKU-003: iPhone 15 Pro 128GB 白色
  - SKU-004: iPhone 15 Pro 256GB 白色

---

## ⚠️ 注意事项

1. **SKU编码唯一性**
   - SKU编码必须唯一
   - 建议使用规则：`SKU-{分类}-{序号}`

2. **产品与SKU关联**
   - 产品的sku_id可以为空
   - 建议为常用产品创建SKU以便管理

3. **价格管理**
   - SKU有独立的价格字段
   - 如果产品关联了SKU，优先使用SKU的价格

4. **库存管理**
   - 如需SKU级别的库存管理，需要进一步扩展库存表

5. **权限控制**
   - 确保为相应角色分配SKU管理权限
   - 在角色管理中勾选SKU相关菜单和按钮权限

---

## 🎯 快速检查清单

- [ ] 数据库表创建成功
- [ ] 产品表添加sku_id字段成功
- [ ] 通过代码生成器生成SKU管理代码
- [ ] 后端代码复制到正确位置
- [ ] 前端代码复制到正确位置
- [ ] ErrorCode已手动合并
- [ ] 产品表单添加SKU选择框
- [ ] 后端添加SKU简单列表接口
- [ ] 菜单权限配置完成
- [ ] 后端编译通过
- [ ] 前端无编译错误
- [ ] SKU管理页面功能正常
- [ ] 产品关联SKU功能正常
- [ ] 所有CRUD操作测试通过

---

## 📞 遇到问题？

### 常见问题

**Q1: 代码生成器找不到erp_product_sku表**
- 确认表已创建：`SHOW TABLES LIKE 'erp_product_sku'`
- 刷新代码生成器页面
- 检查数据源配置

**Q2: 产品表单中SKU下拉框为空**
- 检查后端简单列表接口是否正确
- 查看浏览器控制台网络请求
- 确认SKU表中有数据

**Q3: 保存产品时sku_id没有保存**
- 检查ProductDO是否添加了skuId字段
- 确认表单formData包含skuId
- 查看后端日志确认字段映射

**Q4: 菜单权限不生效**
- 清除浏览器缓存
- 重新登录系统
- 检查角色是否分配了相应权限

---

## 📚 相关文件

### SQL文件
- `sql/mysql/erp_product_sku.sql` - SKU表创建脚本

### 后端代码（生成后）
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/productsku/`
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/productsku/`
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/productsku/`
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/productsku/`
- `yudao-module-erp/src/main/resources/mapper/productsku/`

### 前端代码（生成后）
- `original-yudao-ui/src/api/erp/productsku/index.ts`
- `original-yudao-ui/src/views/erp/productsku/index.vue`
- `original-yudao-ui/src/views/erp/productsku/ProductSkuForm.vue`

### 需要手动修改的文件
- `original-yudao-ui/src/views/erp/product/product/ProductForm.vue` - 添加SKU选择
- `original-yudao-ui/src/views/erp/product/product/index.vue` - 显示SKU信息
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/productsku/ProductSkuController.java` - 添加简单列表接口
- `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/productsku/ProductSkuService.java` - 添加简单列表方法

---

**开发完成日期**: 2025-11-21  
**开发人员**: AI助手  
**版本**: v1.0

