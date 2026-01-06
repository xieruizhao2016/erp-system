<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="formData.workOrderNo" disabled placeholder="保存时自动生成" />
      </el-form-item>
      <el-form-item label="生产订单" prop="productionOrderId">
        <el-select
          v-model="formData.productionOrderId"
          clearable
          filterable
          placeholder="请选择生产订单"
          class="!w-1/1"
          @change="handleProductionOrderChange"
        >
          <el-option
            v-for="item in productionOrderList"
            :key="item.id"
            :label="item.no || `订单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select
          v-model="formData.productId"
          clearable
          filterable
          placeholder="请选择产品"
          class="!w-1/1"
          @change="handleProductChange"
        >
          <el-option
            v-for="item in filteredProductList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工艺路线" prop="routeId">
        <el-select
          v-model="formData.routeId"
          clearable
          filterable
          placeholder="请选择工艺路线（选择产品后自动加载）"
          class="!w-1/1"
          @change="handleRouteChange"
          :disabled="!formData.productId"
        >
          <el-option
            v-for="item in processRouteList"
            :key="item.id"
            :label="item.routeName || item.routeNo || `工艺路线${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工单数量" prop="quantity">
        <el-input v-model="formData.quantity" placeholder="请输入工单数量" />
      </el-form-item>
      <el-form-item label="完成数量" prop="completedQuantity">
        <el-input v-model="formData.completedQuantity" placeholder="请输入完成数量" />
      </el-form-item>
      <el-form-item label="合格数量" prop="qualifiedQuantity">
        <el-input v-model="formData.qualifiedQuantity" placeholder="请输入合格数量" />
      </el-form-item>
      <el-form-item label="工作中心" prop="workCenterId">
        <el-select
          v-model="formData.workCenterId"
          clearable
          filterable
          placeholder="请选择工作中心"
          class="!w-1/1"
        >
          <el-option
            v-for="item in workCenterList"
            :key="item.id"
            :label="item.workCenterName || item.workCenterNo"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="主管" prop="supervisorId">
        <el-select
          v-model="formData.supervisorId"
          clearable
          filterable
          placeholder="请选择主管"
          class="!w-1/1"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="计划开始时间" prop="plannedStartTime">
        <el-date-picker
          v-model="formData.plannedStartTime"
          type="date"
          value-format="x"
          placeholder="选择计划开始时间"
        />
      </el-form-item>
      <el-form-item label="计划结束时间" prop="plannedEndTime">
        <el-date-picker
          v-model="formData.plannedEndTime"
          type="date"
          value-format="x"
          placeholder="选择计划结束时间"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_ORDER_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input v-model="formData.priority" placeholder="请输入优先级" />
      </el-form-item>
      <el-form-item label="作业指导书" prop="instruction">
        <el-input v-model="formData.instruction" placeholder="请输入作业指导书" />
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
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { ProductionOrderApi, ProductionOrder } from '@/api/erp/productionorder'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'
import { WorkCenterApi, WorkCenter } from '@/api/erp/workcenter'
import { ProcessRouteApi, ProcessRoute } from '@/api/erp/processroute'

/** ERP 工单主 表单 */
defineOptions({ name: 'WorkOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  workOrderNo: undefined,
  productionOrderId: undefined,
  productId: undefined,
  routeId: undefined,
  quantity: undefined,
  completedQuantity: undefined,
  qualifiedQuantity: undefined,
  workCenterId: undefined,
  supervisorId: undefined,
  plannedStartTime: undefined,
  plannedEndTime: undefined,
  // 实际开始时间和实际结束时间由系统根据状态自动设置，不在表单中显示
  // actualStartTime: undefined,
  // actualEndTime: undefined,
  status: 1, // 默认状态：1-已创建
  priority: undefined,
  instruction: undefined,
  remark: undefined
})
const formRules = reactive({
  productionOrderId: [{ required: true, message: '生产订单不能为空', trigger: 'change' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  quantity: [{ required: true, message: '工单数量不能为空', trigger: 'blur' }],
  plannedStartTime: [{ required: true, message: '计划开始时间不能为空', trigger: 'blur' }],
  plannedEndTime: [{ required: true, message: '计划结束时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productionOrderList = ref<ProductionOrder[]>([]) // 生产订单列表
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const userList = ref<UserVO[]>([]) // 用户列表
const workCenterList = ref<WorkCenter[]>([]) // 工作中心列表
const processRouteList = ref<ProcessRoute[]>([]) // 工艺路线列表

/** 生产订单关联的产品ID列表 */
const productionOrderProductIds = ref<number[]>([])

/** 根据生产订单过滤后的产品列表 */
const filteredProductList = computed(() => {
  // 如果选择了生产订单，只显示该订单下的产品
  if (productionOrderProductIds.value.length > 0) {
    return productList.value.filter((product) => productionOrderProductIds.value.includes(product.id))
  }
  
  return productList.value
})

/** 处理生产订单变更 */
const handleProductionOrderChange = async (productionOrderId: number | undefined) => {
  // 清空之前的产品过滤和数据
  productionOrderProductIds.value = []
  formData.value.productId = undefined
  formData.value.quantity = undefined
  formData.value.routeId = undefined
  
  if (!productionOrderId) {
    return
  }
  
  try {
    // 获取生产订单详情，包含产品列表
    const orderDetail = await ProductionOrderApi.getProductionOrder(productionOrderId)
    
    // 自动填充工单数量（从生产订单数量）
    if (orderDetail.quantity) {
      formData.value.quantity = orderDetail.quantity
    }
    
    // 如果订单有items，提取产品ID列表
    if (orderDetail.items && orderDetail.items.length > 0) {
      const productIds = orderDetail.items
        .map((item: any) => item.productId)
        .filter((id: number) => id != null)
      
      productionOrderProductIds.value = productIds
      
      // 如果只有一个产品，自动选中并触发产品变更（自动填充工艺路线和工作中心）
      if (productIds.length === 1) {
        formData.value.productId = productIds[0]
        // 触发产品变更，自动填充工艺路线和工作中心
        await handleProductChange(productIds[0])
      }
    } else if (orderDetail.productId) {
      // 兼容旧数据：如果订单有productId字段，也加入过滤列表
      productionOrderProductIds.value = [orderDetail.productId]
      formData.value.productId = orderDetail.productId
      // 触发产品变更，自动填充工艺路线和工作中心
      await handleProductChange(orderDetail.productId)
    }
  } catch (error) {
    console.error('获取生产订单详情失败:', error)
    message.error('获取生产订单详情失败，请重试')
  }
}


/** 处理产品变更 - 加载工艺路线列表 */
const handleProductChange = async (productId: number | undefined) => {
  // 清空工艺路线相关数据
  formData.value.routeId = undefined
  processRouteList.value = []
  
  // 如果清空了产品，清空工作中心
  if (!productId) {
    formData.value.workCenterId = undefined
    return
  }
  
  try {
    // 根据产品ID查询生效的工艺路线（status=2）
    const routePageResult = await ProcessRouteApi.getProcessRoutePage({
      productId: productId,
      status: 2, // 2-生效
      pageNo: 1,
      pageSize: 100 // 加载所有生效的工艺路线
    })
    
    // 加载工艺路线列表
    if (routePageResult.list && routePageResult.list.length > 0) {
      processRouteList.value = routePageResult.list
      
      // 如果是新增模式，自动选择第一个工艺路线
      if (formType.value === 'create' && routePageResult.list.length > 0 && !formData.value.routeId) {
        formData.value.routeId = routePageResult.list[0].id
        // 自动填充工作中心
        await handleRouteChange(routePageResult.list[0].id)
      }
      // 修改模式下，如果已有routeId且在当前列表中，保持不变；如果不在列表中，清空
      else if (formType.value === 'update' && formData.value.routeId) {
        const routeExists = routePageResult.list.some(route => route.id === formData.value.routeId)
        if (!routeExists) {
          formData.value.routeId = undefined
          message.warning('当前工单关联的工艺路线已不在生效列表中')
        }
      }
    } else {
      processRouteList.value = []
      if (formType.value === 'create') {
        message.warning('该产品没有生效的工艺路线')
      }
    }
  } catch (error) {
    console.error('查询工艺路线失败:', error)
    message.error('加载工艺路线列表失败')
  }
}

/** 处理工艺路线变更 - 自动填充工作中心 */
const handleRouteChange = async (routeId: number | undefined) => {
  // 如果是修改模式，不自动填充（避免覆盖已有数据）
  if (formType.value === 'update') {
    return
  }
  
  // 如果清空了工艺路线，清空工作中心
  if (!routeId) {
    formData.value.workCenterId = undefined
    return
  }
  
  try {
    // 获取工艺路线详情（包含明细）
    const routeDetail = await ProcessRouteApi.getProcessRoute(routeId)
    
    // 如果工艺路线有明细，获取第一个工序的工作中心
    if (routeDetail.items && routeDetail.items.length > 0) {
      // 按序号排序，获取第一个工序
      const sortedItems = [...routeDetail.items].sort((a, b) => (a.sequence || 0) - (b.sequence || 0))
      const firstItem = sortedItems[0]
      
      if (firstItem.workCenterId) {
        formData.value.workCenterId = firstItem.workCenterId
        message.success('已自动填充工作中心')
      }
    }
  } catch (error) {
    console.error('查询工艺路线详情失败:', error)
    // 查询失败不影响其他操作，静默处理
  }
}


/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [productionOrderData, products, users, workCenters] = await Promise.all([
      ProductionOrderApi.getProductionOrderPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList(),
      UserApi.getSimpleUserList(),
      WorkCenterApi.getWorkCenterList()
    ])
    productionOrderList.value = productionOrderData.list || []
    productList.value = products || []
    userList.value = users || []
    workCenterList.value = workCenters || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (productList.value.length === 0) {
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
      formData.value = await WorkOrderApi.getWorkOrder(id)
      // 如果已有产品ID，加载工艺路线列表（不触发自动选择）
      if (formData.value.productId) {
        try {
          const routePageResult = await ProcessRouteApi.getProcessRoutePage({
            productId: formData.value.productId,
            status: 2, // 2-生效
            pageNo: 1,
            pageSize: 100
          })
          if (routePageResult.list && routePageResult.list.length > 0) {
            processRouteList.value = routePageResult.list
          }
        } catch (error) {
          console.error('加载工艺路线列表失败:', error)
        }
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
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    // 复制表单数据，排除实际开始时间和实际结束时间（由系统根据状态自动设置）
    const { actualStartTime, actualEndTime, ...submitData } = formData.value
    
    // 补充产品名称（后端需要）
    if (submitData.productId) {
      const product = productList.value.find(p => p.id === submitData.productId)
      if (product) {
        submitData.productName = product.name
      } else {
        message.error('产品信息不存在，请重新选择产品')
        return
      }
    }
    
    const data = submitData as unknown as WorkOrder
    console.log('提交工单数据:', data)
    console.log('工艺路线ID (routeId):', data.routeId)
    if (formType.value === 'create') {
      await WorkOrderApi.createWorkOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkOrderApi.updateWorkOrder(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    workOrderNo: undefined,
    productionOrderId: undefined,
    productId: undefined,
    routeId: undefined,
    quantity: undefined,
    completedQuantity: undefined,
    qualifiedQuantity: undefined,
    workCenterId: undefined,
    supervisorId: undefined,
    plannedStartTime: undefined,
    plannedEndTime: undefined,
    // 实际开始时间和实际结束时间由系统根据状态自动设置，不在表单中显示
    // actualStartTime: undefined,
    // actualEndTime: undefined,
    status: 1, // 默认状态：1-已创建
    priority: undefined,
    instruction: undefined,
    remark: undefined
  }
  productionOrderProductIds.value = [] // 重置生产订单产品过滤
  processRouteList.value = [] // 重置工艺路线列表
  formRef.value?.resetFields()
}
</script>