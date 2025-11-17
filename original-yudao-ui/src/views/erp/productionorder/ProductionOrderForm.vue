<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="生产订单号" prop="no">
        <el-input v-model="formData.no" placeholder="请输入生产订单号" />
      </el-form-item>
      <el-form-item label="客户" prop="customerId">
        <el-select
          v-model="formData.customerId"
          clearable
          filterable
          placeholder="请选择客户"
          class="!w-1/1"
        >
          <el-option
            v-for="item in customerList"
            :key="item.id"
            :label="item.name"
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
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="单位" prop="unitId">
        <el-select
          v-model="formData.unitId"
          clearable
          filterable
          placeholder="请选择单位"
          class="!w-1/1"
        >
          <el-option
            v-for="item in productUnitList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产数量" prop="quantity">
        <el-input v-model="formData.quantity" placeholder="请输入生产数量" />
      </el-form-item>
      <el-form-item label="已完成数量" prop="completedQuantity">
        <el-input v-model="formData.completedQuantity" placeholder="请输入已完成数量" />
      </el-form-item>
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
      <el-form-item label="实际开始时间" prop="actualStartTime">
        <el-date-picker
          v-model="formData.actualStartTime"
          type="date"
          value-format="x"
          placeholder="选择实际开始时间"
        />
      </el-form-item>
      <el-form-item label="实际完成时间" prop="actualEndTime">
        <el-date-picker
          v-model="formData.actualEndTime"
          type="date"
          value-format="x"
          placeholder="选择实际完成时间"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_ORDER_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
      <el-form-item label="来源类型" prop="sourceType">
        <el-select v-model="formData.sourceType" placeholder="请选择来源类型" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_ORDER_SOURCE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="来源单据" prop="sourceId">
        <el-input v-model="formData.sourceId" placeholder="请输入来源单据" />
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
import { CustomerApi, Customer } from '@/api/erp/sale/customer'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductUnitApi, ProductUnit } from '@/api/erp/product/unit'

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
  productId: undefined,
  unitId: undefined,
  quantity: undefined,
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
  no: [{ required: true, message: '生产订单号不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  quantity: [{ required: true, message: '生产数量不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const customerList = ref<Customer[]>([]) // 客户列表
const productList = ref<ProductVO[]>([]) // 产品列表
const productUnitList = ref<ProductUnit[]>([]) // 产品单位列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [customerData, products, unitData] = await Promise.all([
      CustomerApi.getCustomerPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList(),
      ProductUnitApi.getProductUnitPage({ pageNo: 1, pageSize: 100 })
    ])
    customerList.value = customerData.list || []
    productList.value = products || []
    productUnitList.value = unitData.list || []
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
      formData.value = await ProductionOrderApi.getProductionOrder(id)
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
    const data = formData.value as unknown as ProductionOrder
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
    productId: undefined,
    unitId: undefined,
    quantity: undefined,
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
  formRef.value?.resetFields()
}
</script>