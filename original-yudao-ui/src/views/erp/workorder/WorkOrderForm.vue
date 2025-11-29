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
      <el-form-item label="产品分类" prop="categoryId">
        <el-tree-select
          v-model="selectedCategoryId"
          :data="productCategoryTree"
          :props="defaultProps"
          check-strictly
          clearable
          placeholder="请选择产品分类（不选则显示全部）"
          class="!w-1/1"
          @change="handleCategoryChange"
        />
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select
          v-model="formData.productId"
          clearable
          filterable
          placeholder="请选择产品"
          class="!w-1/1"
        >
          <el-option
            v-for="item in filteredProductList"
            :key="item.id"
            :label="item.name"
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
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心" />
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
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { defaultProps, handleTree } from '@/utils/tree'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

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
  workCenterId: [{ required: true, message: '工作中心不能为空', trigger: 'blur' }],
  plannedStartTime: [{ required: true, message: '计划开始时间不能为空', trigger: 'blur' }],
  plannedEndTime: [{ required: true, message: '计划结束时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productionOrderList = ref<ProductionOrder[]>([]) // 生产订单列表
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树
const userList = ref<UserVO[]>([]) // 用户列表

/** 生产订单关联的产品ID列表 */
const productionOrderProductIds = ref<number[]>([])

/** 根据分类和生产订单过滤后的产品列表 */
const filteredProductList = computed(() => {
  let filtered = productList.value
  
  // 如果选择了生产订单，只显示该订单下的产品
  if (productionOrderProductIds.value.length > 0) {
    filtered = filtered.filter((product) => productionOrderProductIds.value.includes(product.id))
  }
  
  // 如果选择了分类，进一步过滤
  if (selectedCategoryId.value) {
    filtered = filtered.filter((product) => product.categoryId === selectedCategoryId.value)
  }
  
  return filtered
})

/** 处理生产订单变更 */
const handleProductionOrderChange = async (productionOrderId: number | undefined) => {
  // 清空之前的产品过滤
  productionOrderProductIds.value = []
  formData.value.productId = undefined
  
  if (!productionOrderId) {
    return
  }
  
  try {
    // 获取生产订单详情，包含产品列表
    const orderDetail = await ProductionOrderApi.getProductionOrder(productionOrderId)
    
    // 如果订单有items，提取产品ID列表
    if (orderDetail.items && orderDetail.items.length > 0) {
      const productIds = orderDetail.items
        .map((item: any) => item.productId)
        .filter((id: number) => id != null)
      
      productionOrderProductIds.value = productIds
      
      // 如果只有一个产品，自动选中
      if (productIds.length === 1) {
        formData.value.productId = productIds[0]
      }
    } else if (orderDetail.productId) {
      // 兼容旧数据：如果订单有productId字段，也加入过滤列表
      productionOrderProductIds.value = [orderDetail.productId]
      formData.value.productId = orderDetail.productId
    }
  } catch (error) {
    console.error('获取生产订单详情失败:', error)
    message.error('获取生产订单详情失败，请重试')
  }
}

/** 处理分类变更 */
const handleCategoryChange = (categoryId: number | undefined) => {
  // 当分类改变时，如果已选择的产品不在新分类下，清空产品选择
  if (categoryId !== undefined && formData.value.productId) {
    const product = filteredProductList.value.find((item) => item.id === formData.value.productId)
    if (!product) {
      formData.value.productId = undefined
    }
  }
}

/** 加载产品分类树 */
const loadProductCategoryTree = async () => {
  try {
    const data = await ProductCategoryApi.getProductCategoryList()
    const root: any = { id: 0, name: '顶级产品分类', children: [] }
    root.children = handleTree(data, 'id', 'parentId')
    productCategoryTree.value = [root]
  } catch (error) {
    console.error('加载产品分类失败:', error)
  }
}

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [productionOrderData, products, users] = await Promise.all([
      ProductionOrderApi.getProductionOrderPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList(),
      UserApi.getSimpleUserList()
    ])
    productionOrderList.value = productionOrderData.list || []
    productList.value = products || []
    userList.value = users || []
    // 加载产品分类树
    await loadProductCategoryTree()
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
    const data = submitData as unknown as WorkOrder
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
  selectedCategoryId.value = undefined // 重置分类选择
  productionOrderProductIds.value = [] // 重置生产订单产品过滤
  formRef.value?.resetFields()
}
</script>