<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="生产订单号" prop="no">
        <el-input v-model="formData.no" disabled placeholder="保存时自动生成" />
      </el-form-item>
      <el-form-item label="来源类型" prop="sourceType">
        <el-select v-model="formData.sourceType" placeholder="请选择来源类型" clearable @change="handleSourceTypeChange">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_ORDER_SOURCE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item v-if="showSourceId" label="来源单据" prop="sourceId">
        <el-select
          v-model="formData.sourceId"
          clearable
          filterable
          placeholder="请选择未完成的销售订单"
          class="!w-1/1"
          @change="handleSourceIdChange"
        >
          <el-option
            v-for="item in saleOrderList"
            :key="item.id"
            :label="`${item.no} - ${getCustomerName(item.customerId)}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="客户" prop="customerId">
        <el-select
          v-model="formData.customerId"
          clearable
          filterable
          placeholder="请选择客户"
          class="!w-1/1"
          :disabled="isFromSaleOrder"
        >
          <el-option
            v-for="item in customerList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <!-- 子表的表单 -->
      <ContentWrap>
        <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
          <el-tab-pane label="生产产品清单" name="item">
            <ProductionOrderItemForm ref="itemFormRef" :items="formData.items" :disabled="isFromSaleOrder" />
          </el-tab-pane>
        </el-tabs>
      </ContentWrap>
      <el-form-item label="计划开始时间" prop="startTime">
        <el-date-picker
          v-model="formData.startTime"
          type="date"
          value-format="x"
          placeholder="选择计划开始时间"
        />
      </el-form-item>
      <el-form-item label="计划完成时间" prop="endTime">
        <el-date-picker
          v-model="formData.endTime"
          type="date"
          value-format="x"
          placeholder="选择计划完成时间"
        />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="formData.priority" placeholder="请选择优先级" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_ORDER_PRIORITY)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产说明" prop="description">
        <Editor v-model="formData.description" height="150px" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductionOrderApi, ProductionOrder } from '@/api/erp/productionorder'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'
import { SaleOrderApi, SaleOrderVO } from '@/api/erp/sale/order'
import { ProductApi } from '@/api/erp/product/product'
import ProductionOrderItemForm from './components/ProductionOrderItemForm.vue'

/** ERP 生产订单 DO
 * 表单 */
defineOptions({ name: 'ProductionOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  no: undefined,
  customerId: undefined,
  items: [] as any[], // 生产产品列表
  completedQuantity: undefined,
  startTime: undefined,
  endTime: undefined,
  actualStartTime: undefined,
  actualEndTime: undefined,
  status: undefined,
  priority: undefined,
  sourceType: undefined,
  sourceId: undefined,
  description: undefined,
  remark: undefined
})
const formRules = reactive({
  customerId: [{ required: true, message: '客户不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerVO[]>([]) // 客户列表
const saleOrderList = ref<SaleOrderVO[]>([]) // 销售订单列表
const showSourceId = ref(false) // 是否显示来源单据
// 计算属性：是否来自销售订单（来源类型为销售订单且已选择来源单据）
const isFromSaleOrder = computed(() => {
  return formData.value.sourceType === 2 && !!formData.value.sourceId
})

/** 子表的表单 */
const subTabsName = ref('item')
const itemFormRef = ref()

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const customerData = await CustomerApi.getCustomerPage({ pageNo: 1, pageSize: 100 })
    customerList.value = customerData.list || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 加载未完成的销售订单 */
const loadUncompletedSaleOrders = async () => {
  try {
    // 查询状态为已审核的销售订单（状态为20表示已审核）
    // 使用最大允许的 pageSize: 100
    const data = await SaleOrderApi.getSaleOrderPage({
      pageNo: 1,
      pageSize: 100,
      status: 20 // 已审核状态
    })
    // 过滤未完成的订单：已审核且出库数量小于总数量（未全部出库）
    saleOrderList.value = (data.list || []).filter((order: SaleOrderVO) => {
      const outCount = order.outCount || 0
      const totalCount = order.totalCount || 0
      // 已审核且未全部出库的订单
      return order.status === 20 && outCount < totalCount
    })
  } catch (error) {
    console.error('加载销售订单列表失败:', error)
  }
}

/** 获取客户名称 */
const getCustomerName = (id?: number) => {
  if (!id) return ''
  const customer = customerList.value.find(item => item.id === id)
  return customer?.name || `客户${id}`
}

/** 来源类型变化处理 */
const handleSourceTypeChange = (value: number) => {
  // 来源类型为2表示销售订单
  if (value === 2) {
    showSourceId.value = true
    if (saleOrderList.value.length === 0) {
      loadUncompletedSaleOrders()
    }
  } else {
    showSourceId.value = false
    formData.value.sourceId = undefined
    // 清空自动填充的字段
    if (formType.value === 'create') {
      formData.value.customerId = undefined
      formData.value.items = []
    }
  }
}

/** 来源单据变化处理 - 当选择了销售订单时，自动填充客户、产品等信息 */
const handleSourceIdChange = async (sourceId: number | undefined) => {
  // 如果清空了来源单据，清空自动填充的字段（仅在新增模式下）
  if (!sourceId) {
    if (formType.value === 'create' && formData.value.sourceType === 2) {
      formData.value.customerId = undefined
      formData.value.items = []
    }
    return
  }

  // 如果来源类型不是销售订单，则不处理
  if (formData.value.sourceType !== 2) {
    return
  }

  // 如果是修改模式，不自动填充（避免覆盖已有数据）
  if (formType.value === 'update') {
    return
  }

  try {
    // 获取销售订单详情
    const saleOrderDetail = await SaleOrderApi.getSaleOrder(sourceId)
    if (!saleOrderDetail) {
      return
    }

    // 自动填充客户信息
    if (saleOrderDetail.customerId) {
      formData.value.customerId = saleOrderDetail.customerId
    }

    // 自动填充产品列表（将所有订单项转换为生产订单项）
    if (saleOrderDetail.items && saleOrderDetail.items.length > 0) {
      // 批量获取产品详情以获取完整的产品信息
      const productIds = saleOrderDetail.items.map((item: any) => item.productId).filter(Boolean)
      const productMap = new Map()
      
      // 批量获取产品详情
      if (productIds.length > 0) {
        try {
          const productPromises = productIds.map((productId: number) => 
            ProductApi.getProduct(productId).catch(() => null)
          )
          const products = await Promise.all(productPromises)
          products.forEach((product) => {
            if (product && product.id) {
              productMap.set(product.id, product)
            }
          })
        } catch (error) {
          console.error('批量获取产品详情失败:', error)
        }
      }
      
      formData.value.items = saleOrderDetail.items.map((item: any) => {
        // 计算剩余数量（订单数量 - 已出库数量）
        const outCount = item.outCount || 0
        const remainingCount = Number(item.count || 0) - Number(outCount)
        
        // 获取产品详情
        const product = productMap.get(item.productId)
        
        return {
          id: undefined,
          productId: item.productId,
          productName: item.productName || product?.name, // 产品名称（必填）
          productSpec: product?.standard, // 产品规格
          unitId: item.productUnitId || product?.unitId, // 单位ID
          productUnitName: item.productUnitName || product?.unitName, // 产品单位名称
          productBarCode: item.productBarCode || product?.barCode, // 产品条码
          stockCount: item.stockCount || 0,
          quantity: remainingCount > 0 ? remainingCount : item.count || 1,
          remark: item.remark || undefined
        }
      })
    } else {
      formData.value.items = []
    }
  } catch (error) {
    console.error('获取销售订单详情失败:', error)
    message.error('获取销售订单详情失败，请稍后重试')
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (customerList.value.length === 0) {
    await loadListData()
  }
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const orderData = await ProductionOrderApi.getProductionOrder(id)
      // 将单个产品转换为产品列表格式（兼容旧数据）
      if (orderData.productId) {
        formData.value = {
          ...orderData,
          items: [{
            id: undefined,
            productId: orderData.productId,
            productUnitName: orderData.productName,
            productBarCode: undefined,
            stockCount: undefined,
            quantity: orderData.quantity || 1,
            remark: undefined
          }]
        }
      } else {
        formData.value = {
          ...orderData,
          items: orderData.items || []
        }
      }
      // 如果是修改且来源类型是销售订单，显示来源单据并加载销售订单列表
      if (formData.value.sourceType === 2) {
        showSourceId.value = true
        await loadUncompletedSaleOrders()
      }
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验主表单
  await formRef.value.validate()
  // 校验产品列表表单
  if (itemFormRef.value) {
    await itemFormRef.value.validate()
  }
  // 校验产品列表不能为空
  if (!formData.value.items || formData.value.items.length === 0) {
    message.error('请至少添加一个生产产品')
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    // 验证产品列表
    if (!formData.value.items || formData.value.items.length === 0) {
      message.error('请至少添加一个生产产品')
      return
    }
    
    // 批量获取产品详情，补充缺失的产品信息
    const productIds = formData.value.items
      .map((item: any) => item.productId)
      .filter(Boolean)
    const productMap = new Map()
    
    if (productIds.length > 0) {
      try {
        const productPromises = productIds.map((productId: number) => 
          ProductApi.getProduct(productId).catch(() => null)
        )
        const products = await Promise.all(productPromises)
        products.forEach((product) => {
          if (product && product.id) {
            productMap.set(product.id, product)
          }
        })
      } catch (error) {
        console.error('批量获取产品详情失败:', error)
      }
    }
    
    // 补充产品信息并验证
    const items = formData.value.items.map((item: any) => {
      if (!item.productId) {
        throw new Error('产品不能为空')
      }
      
      const product = productMap.get(item.productId)
      const itemData = {
        id: item.id,
        productId: item.productId,
        productName: item.productName || product?.name,
        productSpec: item.productSpec || product?.standard,
        unitId: item.unitId || product?.unitId,
        quantity: Number(item.quantity || 0),
        completedQuantity: item.completedQuantity ? Number(item.completedQuantity) : undefined,
        remark: item.remark || undefined
      }
      
      // 验证必填字段
      if (!itemData.productName) {
        throw new Error(`产品 ${item.productId} 的名称不能为空，请重新选择产品`)
      }
      if (!itemData.quantity || itemData.quantity <= 0) {
        throw new Error(`产品 ${itemData.productName} 的生产数量必须大于0`)
      }
      
      return itemData
    })
    
    // 构建提交数据
    const data = {
      ...formData.value,
      items: items, // 提交多个产品项
      // 兼容旧数据：保留单个产品字段（使用第一个产品）
      productId: items[0]?.productId,
      productName: items[0]?.productName,
      productSpec: items[0]?.productSpec,
      unitId: items[0]?.unitId,
      quantity: items.reduce((sum: number, item: any) => sum + Number(item.quantity || 0), 0),
      // 新增时，no 由后端自动生成，status 需要设置默认值
      ...(formType.value === 'create' ? {
        no: undefined, // 新增时由后端自动生成
        status: 1 // 新增时默认为待开始状态（1-待开始）
      } : {})
    } as unknown as ProductionOrder
    
    if (formType.value === 'create') {
      await ProductionOrderApi.createProductionOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionOrderApi.updateProductionOrder(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } catch (error: any) {
    // 处理错误信息
    const errorMessage = error?.response?.data?.msg || error?.message || '操作失败，请稍后重试'
    message.error(errorMessage)
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    no: undefined,
    customerId: undefined,
    items: [],
    completedQuantity: undefined,
    startTime: undefined,
    endTime: undefined,
    actualStartTime: undefined,
    actualEndTime: undefined,
    status: undefined,
    priority: undefined,
    sourceType: undefined,
    sourceId: undefined,
    description: undefined,
    remark: undefined
  }
  showSourceId.value = false
  formRef.value?.resetFields()
}
</script>