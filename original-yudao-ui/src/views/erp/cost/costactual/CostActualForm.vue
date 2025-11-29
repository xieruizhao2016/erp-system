<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="成本单号" prop="costNo">
        <el-input v-model="formData.costNo" disabled placeholder="保存时自动生成" />
      </el-form-item>
      <el-form-item label="工单" prop="workOrderId">
        <el-select
          v-model="formData.workOrderId"
          clearable
          filterable
          placeholder="请选择工单"
          class="!w-1/1"
        >
          <el-option
            v-for="item in workOrderList"
            :key="item.id"
            :label="item.workOrderNo || `工单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产订单" prop="productionOrderId">
        <el-select
          v-model="formData.productionOrderId"
          clearable
          filterable
          placeholder="请选择生产订单"
          class="!w-1/1"
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
      <el-form-item label="生产数量" prop="productionQuantity">
        <el-input v-model="formData.productionQuantity" placeholder="请输入生产数量" />
      </el-form-item>
      <el-form-item label="材料成本" prop="materialCost">
        <el-input v-model="formData.materialCost" placeholder="请输入材料成本" />
      </el-form-item>
      <el-form-item label="材料成本调整" prop="materialCostAdjust">
        <el-input v-model="formData.materialCostAdjust" placeholder="请输入材料成本调整" />
      </el-form-item>
      <el-form-item label="人工成本" prop="laborCost">
        <el-input v-model="formData.laborCost" placeholder="请输入人工成本" />
      </el-form-item>
      <el-form-item label="人工成本调整" prop="laborCostAdjust">
        <el-input v-model="formData.laborCostAdjust" placeholder="请输入人工成本调整" />
      </el-form-item>
      <el-form-item label="制造费用" prop="overheadCost">
        <el-input v-model="formData.overheadCost" placeholder="请输入制造费用" />
      </el-form-item>
      <el-form-item label="制造费用调整" prop="overheadCostAdjust">
        <el-input v-model="formData.overheadCostAdjust" placeholder="请输入制造费用调整" />
      </el-form-item>
      <el-form-item label="总成本" prop="totalCost">
        <el-input v-model="formData.totalCost" placeholder="请输入总成本" />
      </el-form-item>
      <el-form-item label="单位成本" prop="unitCost">
        <el-input v-model="formData.unitCost" placeholder="请输入单位成本" />
      </el-form-item>
      <el-form-item label="成本币种" prop="costCurrency">
        <el-input v-model="formData.costCurrency" placeholder="请输入成本币种" />
      </el-form-item>
      <el-form-item label="成本期间" prop="costPeriod">
        <el-input v-model="formData.costPeriod" placeholder="请输入成本期间（YYYY-MM）" />
      </el-form-item>
      <el-form-item label="计算日期" prop="calculationDate">
        <el-date-picker
          v-model="formData.calculationDate"
          type="date"
          value-format="x"
          placeholder="选择计算日期"
        />
      </el-form-item>
      <el-form-item label="最后调整日期" prop="lastAdjustDate">
        <el-date-picker
          v-model="formData.lastAdjustDate"
          type="date"
          value-format="x"
          placeholder="选择最后调整日期"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_COST_ACTUAL_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
import { CostActualApi, CostActual } from '@/api/erp/costactual'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { ProductionOrderApi, ProductionOrder } from '@/api/erp/productionorder'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { defaultProps, handleTree } from '@/utils/tree'

/** ERP 实际成本 表单 */
defineOptions({ name: 'CostActualForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  costNo: undefined,
  workOrderId: undefined,
  productionOrderId: undefined,
  productId: undefined,
  productionQuantity: undefined,
  materialCost: undefined,
  materialCostAdjust: undefined,
  laborCost: undefined,
  laborCostAdjust: undefined,
  overheadCost: undefined,
  overheadCostAdjust: undefined,
  totalCost: undefined,
  unitCost: undefined,
  costCurrency: undefined,
  costPeriod: undefined,
  calculationDate: undefined,
  lastAdjustDate: undefined,
  status: undefined,
  remark: undefined
})
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  productionQuantity: [{ required: true, message: '生产数量不能为空', trigger: 'blur' }],
  materialCost: [{ required: true, message: '材料成本不能为空', trigger: 'blur' }],
  laborCost: [{ required: true, message: '人工成本不能为空', trigger: 'blur' }],
  overheadCost: [{ required: true, message: '制造费用不能为空', trigger: 'blur' }],
  totalCost: [{ required: true, message: '总成本不能为空', trigger: 'blur' }],
  unitCost: [{ required: true, message: '单位成本不能为空', trigger: 'blur' }],
  costPeriod: [{ required: true, message: '成本期间（YYYY-MM）不能为空', trigger: 'blur' }],
  calculationDate: [{ required: true, message: '计算日期不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const productionOrderList = ref<ProductionOrder[]>([]) // 生产订单列表
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树

/** 根据分类过滤后的产品列表 */
const filteredProductList = computed(() => {
  if (!selectedCategoryId.value) {
    return productList.value // 未选择分类，显示全部产品
  }
  // 选择分类后，只显示该分类下的产品
  return productList.value.filter((product) => product.categoryId === selectedCategoryId.value)
})

/** 处理分类变更 */
const handleCategoryChange = (categoryId: number | undefined) => {
  // 当分类改变时，如果已选择的产品不在新分类下，清空产品选择
  if (categoryId !== undefined && formData.value.productId) {
    const product = productList.value.find((item) => item.id === formData.value.productId)
    if (product && product.categoryId !== categoryId) {
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
    const [workOrderData, productionOrderData, products] = await Promise.all([
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      ProductionOrderApi.getProductionOrderPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList()
    ])
    workOrderList.value = workOrderData.list || []
    productionOrderList.value = productionOrderData.list || []
    productList.value = products || []
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
      formData.value = await CostActualApi.getCostActual(id)
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
    const data = formData.value as unknown as CostActual
    if (formType.value === 'create') {
      await CostActualApi.createCostActual(data)
      message.success(t('common.createSuccess'))
    } else {
      await CostActualApi.updateCostActual(data)
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
    costNo: undefined,
    workOrderId: undefined,
    productionOrderId: undefined,
    productId: undefined,
    productionQuantity: undefined,
    materialCost: undefined,
    materialCostAdjust: undefined,
    laborCost: undefined,
    laborCostAdjust: undefined,
    overheadCost: undefined,
    overheadCostAdjust: undefined,
    totalCost: undefined,
    unitCost: undefined,
    costCurrency: undefined,
    costPeriod: undefined,
    calculationDate: undefined,
    lastAdjustDate: undefined,
    status: undefined,
    remark: undefined
  }
  selectedCategoryId.value = undefined // 重置分类选择
  formRef.value?.resetFields()
}
</script>