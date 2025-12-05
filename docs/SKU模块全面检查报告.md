# SKU 模块全面检查报告

## 一、数据库表结构检查

### 1.1 erp_product_sku 表
- **表名**: `erp_product_sku`
- **状态**: ✅ 已创建
- **关键字段**:
  - `id`: SKU编号（主键）
  - `product_id`: 产品编号（可选，支持多对多关系）
  - `sku_code`: SKU编码（唯一索引：tenant_id + sku_code + deleted）
  - `sku_name`: SKU名称
  - `status`: 状态（0-开启，1-关闭，已修复为与 CommonStatusEnum 一致）
  - `bar_code`: SKU条码
  - `standard`: SKU规格
  - `unit_id`: 单位编号
  - `sale_price`: 销售价格
  - `purchase_price`: 采购价格
  - 其他字段：weight, volume, cost_price, min_price, color, size, material, image_url, sort, remark

### 1.2 erp_product_sku_relation 表
- **表名**: `erp_product_sku_relation`
- **状态**: ✅ 已创建
- **用途**: 实现产品和SKU的多对多关联关系
- **关键字段**:
  - `id`: 关联编号（主键）
  - `product_id`: 产品编号
  - `sku_id`: SKU编号
  - `tenant_id`: 租户ID
  - **唯一索引**: `uk_tenant_product_sku` (tenant_id, product_id, sku_id, deleted)
- **关系**: 一个产品可以关联多个SKU，一个SKU可以被多个产品关联

### 1.3 数据库脚本文件
- ✅ `erp_product_sku.sql`: 创建SKU表
- ✅ `erp_product_sku_relation.sql`: 创建关联表
- ✅ `erp_product_sku_fix_status.sql`: 修复状态值（1->0, 0->1）
- ✅ `erp_product_sku_fix_fields.sql`: 修复缺失字段
- ✅ `erp_sale_order_items_add_sku_id.sql`: 销售订单项添加SKU字段

## 二、后端代码检查

### 2.1 数据对象（DO）
#### ProductSkuDO
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/productsku/ProductSkuDO.java`
- **状态**: ✅ 正确
- **表映射**: `@TableName("erp_product_sku")`
- **字段映射**: 所有字段正确映射
- **状态字段**: 注释已更新为 "0-开启，1-关闭"，与 CommonStatusEnum 一致

#### ProductSkuRelationDO
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/dataobject/productsku/ProductSkuRelationDO.java`
- **状态**: ✅ 正确
- **表映射**: `@TableName("erp_product_sku_relation")`
- **字段**: productId, skuId 正确映射

### 2.2 Mapper 层
#### ProductSkuMapper
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/productsku/ProductSkuMapper.java`
- **状态**: ✅ 正确
- **功能**: 分页查询、批量查询等

#### ProductSkuRelationMapper
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/dal/mysql/productsku/ProductSkuRelationMapper.java`
- **状态**: ✅ 正确
- **方法**:
  - `selectListByProductId`: 根据产品ID查询关联的SKU列表
  - `selectListByProductIds`: 批量查询
  - `selectListBySkuId`: 根据SKU ID查询关联的产品列表
  - `deleteByProductIdAndSkuId`: 删除特定关联关系

### 2.3 Service 层
#### ProductSkuService / ProductSkuServiceImpl
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/productsku/`
- **状态**: ✅ 正确
- **关键方法**:
  - `getProductSkuListByProductId`: 根据产品ID获取SKU列表（只返回启用的，status=0）
  - `getProductSkuListByProductIdAll`: 根据产品ID获取SKU列表（包含所有状态，用于编辑）
  - **实现逻辑**: 通过关联表查询，然后批量获取SKU，最后过滤和排序

### 2.4 Controller 层
#### ProductSkuController
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/productsku/ProductSkuController.java`
- **状态**: ⚠️ 部分接口权限检查已移除（用于调试）
- **路由**: `@RequestMapping("/erp/product-sku")`
- **接口**:
  - ✅ `/create`: 创建SKU
  - ✅ `/update`: 更新SKU
  - ✅ `/delete`: 删除SKU
  - ✅ `/get`: 获取SKU详情
  - ⚠️ `/page`: 分页查询（权限检查已注释）
  - ✅ `/simple-list`: 精简列表（无权限检查）
  - ⚠️ `/list-by-product`: 根据产品ID获取SKU列表（权限检查已注释）

### 2.5 产品与SKU关联关系实现
#### ErpProductServiceImpl
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/service/product/ErpProductServiceImpl.java`
- **状态**: ✅ 正确
- **关键方法**:
  - `saveProductSkuRelations`: 保存产品SKU关联关系（创建产品时）
  - `updateProductSkuRelations`: 更新产品SKU关联关系（更新产品时）
  - `getProductSkuMapByProductIds`: 批量获取产品的SKU列表（用于分页查询）
- **实现逻辑**:
  1. 通过关联表批量查询关系
  2. 批量获取SKU数据
  3. 过滤启用的SKU（status=0）
  4. 按产品ID分组
  5. 排序（sort字段，然后id倒序）

#### ErpProductController
- **文件**: `yudao-module-erp/src/main/java/cn/iocoder/yudao/module/erp/controller/admin/product/ErpProductController.java`
- **状态**: ✅ 正确
- **getProduct 接口**: 使用 `getProductSkuListByProductIdAll` 返回所有状态的SKU（用于编辑）

## 三、前端代码检查

### 3.1 API 接口
#### ProductSkuApi
- **文件**: `original-yudao-ui/src/api/erp/productsku/index.ts`
- **状态**: ✅ 正确
- **接口**:
  - `getPage`: 分页查询
  - `get`: 获取详情
  - `create`: 创建
  - `update`: 更新
  - `delete`: 删除
  - `getSimpleList`: 精简列表
  - `getListByProductId`: 根据产品ID获取SKU列表

### 3.2 页面组件
#### SKU管理页面
- **文件**: `original-yudao-ui/src/views/erp/productsku/index.vue`
- **状态**: ✅ 正确
- **功能**: 列表展示、搜索、新增、编辑、删除、导出
- **状态显示**: 使用 `DICT_TYPE.COMMON_STATUS` 正确显示

#### SKU表单组件
- **文件**: `original-yudao-ui/src/views/erp/productsku/ProductSkuForm.vue`
- **状态**: ✅ 正确
- **功能**: 创建/编辑SKU
- **状态选项**: 使用 `getIntDictOptions(DICT_TYPE.COMMON_STATUS)`
- **默认状态**: `CommonStatusEnum.ENABLE` (0)

#### 产品表单中的SKU管理
- **文件**: `original-yudao-ui/src/views/erp/product/product/ProductForm.vue`
- **状态**: ✅ 正确
- **功能**: 
  - SKU选择器（弹窗选择已有SKU）
  - SKU列表展示
  - 添加/删除SKU关联
- **数据提交**: 提交 `skuIds` 数组给后端

#### 产品列表页面
- **文件**: `original-yudao-ui/src/views/erp/product/product/index.vue`
- **状态**: ✅ 正确
- **功能**: 显示每个产品关联的SKU列表（从后端分页接口返回）

#### 销售订单项表单
- **文件**: `original-yudao-ui/src/views/erp/sale/order/components/SaleOrderItemForm.vue`
- **状态**: ✅ 正确
- **功能**: 
  - 编辑模式下加载SKU列表
  - 新增模式下不加载SKU（避免404错误）
  - SKU选择器（当产品有多个SKU时显示）

## 四、发现的问题

### 4.1 已修复的问题
1. ✅ **状态值不一致**: 已通过 `erp_product_sku_fix_status.sql` 修复
2. ✅ **产品列表SKU加载**: 已改为从后端分页接口直接返回，避免前端单独调用API
3. ✅ **产品编辑SKU回显**: 已使用 `getProductSkuListByProductIdAll` 返回所有状态的SKU
4. ✅ **销售订单新增**: 已改为只在编辑模式下加载SKU，避免404错误

### 4.2 待解决的问题
1. ⚠️ **权限检查**: `/page` 和 `/list-by-product` 接口的权限检查已移除（用于调试404问题）
   - **建议**: 问题解决后恢复权限检查，并确保用户有 `erp:product-sku:query` 权限

### 4.3 潜在问题
1. ⚠️ **404错误**: 前端访问 `/admin-api/erp/product-sku/page` 时返回404
   - **可能原因**: 
     - 后端服务未完全启动
     - 路由映射问题
     - Spring Security 配置问题
     - 前端缓存问题
   - **当前状态**: curl 测试返回 401（说明路由存在），但前端收到 404
   - **建议**: 
     - 清除浏览器缓存（硬刷新：Cmd+Shift+R）
     - 重新登录
     - 检查后端日志确认路由注册情况
     - 检查前端请求的完整URL是否正确

2. ⚠️ **SQL脚本注释不一致**: `erp_product_sku.sql` 中的状态字段注释为 "0-禁用，1-启用"
   - **当前状态**: 已通过 `erp_product_sku_fix_status.sql` 修复数据库中的实际值
   - **建议**: 更新原始SQL脚本的注释为 "0-开启，1-关闭"，保持一致性

## 五、架构设计总结

### 5.1 数据模型
- **SKU**: 独立实体，可以独立创建和管理
- **产品-SKU关系**: 多对多关系，通过 `erp_product_sku_relation` 表维护
- **设计优势**: 
  - SKU可以复用（多个产品可以共享同一个SKU）
  - 产品可以关联多个SKU
  - 关系灵活，易于扩展

### 5.2 业务逻辑
1. **SKU创建**: 独立创建，不需要关联产品
2. **产品创建/编辑**: 可以选择关联多个SKU
3. **SKU查询**: 
   - 列表查询：只返回启用的SKU（status=0）
   - 编辑查询：返回所有状态的SKU（用于编辑产品时显示）
4. **关联关系管理**: 
   - 创建时：直接插入关联关系
   - 更新时：计算差异，删除旧关系，插入新关系
   - 防止重复：插入前检查唯一索引

### 5.3 性能优化
1. **批量查询**: 使用 `selectListByProductIds` 批量查询关联关系
2. **批量获取**: 使用 `selectBatchIds` 批量获取SKU数据
3. **数据缓存**: 产品列表中的SKU数据从分页接口直接返回，避免额外API调用

## 六、建议

### 6.1 立即执行
1. ✅ 恢复 `/page` 和 `/list-by-product` 接口的权限检查（问题解决后）
2. ✅ 确保用户有正确的权限配置
3. ✅ 清除浏览器缓存并重新测试

### 6.2 长期优化
1. 考虑添加SKU缓存机制
2. 考虑添加SKU使用情况统计（哪些产品在使用该SKU）
3. 考虑添加SKU删除时的关联检查（如果SKU被产品使用，不允许删除）

## 七、测试建议

### 7.1 功能测试
1. ✅ SKU创建、编辑、删除
2. ✅ 产品创建时关联SKU
3. ✅ 产品编辑时修改SKU关联
4. ✅ 产品列表显示SKU
5. ✅ 销售订单选择SKU

### 7.2 边界测试
1. 产品关联多个SKU
2. 一个SKU被多个产品关联
3. SKU状态变更对产品的影响
4. 删除被产品关联的SKU

### 7.3 性能测试
1. 产品列表加载大量SKU数据
2. 批量创建产品并关联SKU
3. 批量更新产品SKU关联关系

