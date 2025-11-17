<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
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
      <el-form-item label="版本号" prop="version">
        <el-input v-model="formData.version" placeholder="请输入版本号" />
      </el-form-item>
      <el-form-item label="生效日期" prop="effectiveDate">
        <el-date-picker
          v-model="formData.effectiveDate"
          type="date"
          value-format="x"
          placeholder="选择生效日期"
        />
      </el-form-item>
      <el-form-item label="失效日期" prop="expireDate">
        <el-date-picker
          v-model="formData.expireDate"
          type="date"
          value-format="x"
          placeholder="选择失效日期"
        />
      </el-form-item>
      <el-form-item label="材料成本" prop="materialCost">
        <el-input v-model="formData.materialCost" placeholder="请输入材料成本" />
      </el-form-item>
      <el-form-item label="人工成本" prop="laborCost">
        <el-input v-model="formData.laborCost" placeholder="请输入人工成本" />
      </el-form-item>
      <el-form-item label="制造费用" prop="overheadCost">
        <el-input v-model="formData.overheadCost" placeholder="请输入制造费用" />
      </el-form-item>
      <el-form-item label="总成本" prop="totalCost">
        <el-input v-model="formData.totalCost" placeholder="请输入总成本" />
      </el-form-item>
      <el-form-item label="成本币种" prop="costCurrency">
        <el-input v-model="formData.costCurrency" placeholder="请输入成本币种" />
      </el-form-item>
      <el-form-item label="计算日期" prop="calculationDate">
        <el-date-picker
          v-model="formData.calculationDate"
          type="date"
          value-format="x"
          placeholder="选择计算日期"
        />
      </el-form-item>
      <el-form-item label="关联BOM版本" prop="bomVersion">
        <el-input v-model="formData.bomVersion" placeholder="请输入关联BOM版本" />
      </el-form-item>
      <el-form-item label="关联工艺版本" prop="routeVersion">
        <el-input v-model="formData.routeVersion" placeholder="请输入关联工艺版本" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_COST_STANDARD_STATUS)"
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
import { CostStandardApi, CostStandard } from '@/api/erp/coststandard'
import { ProductApi, ProductVO } from '@/api/erp/product/product'

/** ERP 标准成本 表单 */
defineOptions({ name: 'CostStandardForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  productId: undefined,
  version: undefined,
  effectiveDate: undefined,
  expireDate: undefined,
  materialCost: undefined,
  laborCost: undefined,
  overheadCost: undefined,
  totalCost: undefined,
  costCurrency: undefined,
  calculationDate: undefined,
  bomVersion: undefined,
  routeVersion: undefined,
  status: undefined,
  remark: undefined
})
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  effectiveDate: [{ required: true, message: '生效日期不能为空', trigger: 'blur' }],
  materialCost: [{ required: true, message: '材料成本不能为空', trigger: 'blur' }],
  laborCost: [{ required: true, message: '人工成本不能为空', trigger: 'blur' }],
  overheadCost: [{ required: true, message: '制造费用不能为空', trigger: 'blur' }],
  totalCost: [{ required: true, message: '总成本不能为空', trigger: 'blur' }],
  calculationDate: [{ required: true, message: '计算日期不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const products = await ProductApi.getProductSimpleList()
    productList.value = products || []
  } catch (error) {
    console.error('加载产品列表失败:', error)
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
      formData.value = await CostStandardApi.getCostStandard(id)
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
    const data = formData.value as unknown as CostStandard
    if (formType.value === 'create') {
      await CostStandardApi.createCostStandard(data)
      message.success(t('common.createSuccess'))
    } else {
      await CostStandardApi.updateCostStandard(data)
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
    productId: undefined,
    version: undefined,
    effectiveDate: undefined,
    expireDate: undefined,
    materialCost: undefined,
    laborCost: undefined,
    overheadCost: undefined,
    totalCost: undefined,
    costCurrency: undefined,
    calculationDate: undefined,
    bomVersion: undefined,
    routeVersion: undefined,
    status: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>