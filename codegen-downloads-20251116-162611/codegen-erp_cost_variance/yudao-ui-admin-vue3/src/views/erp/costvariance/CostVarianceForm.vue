<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="实际成本ID" prop="costActualId">
        <el-input v-model="formData.costActualId" placeholder="请输入实际成本ID" />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input v-model="formData.productId" placeholder="请输入产品ID" />
      </el-form-item>
      <el-form-item label="生产数量" prop="productionQuantity">
        <el-input v-model="formData.productionQuantity" placeholder="请输入生产数量" />
      </el-form-item>
      <el-form-item label="标准总成本" prop="standardTotalCost">
        <el-input v-model="formData.standardTotalCost" placeholder="请输入标准总成本" />
      </el-form-item>
      <el-form-item label="实际总成本" prop="actualTotalCost">
        <el-input v-model="formData.actualTotalCost" placeholder="请输入实际总成本" />
      </el-form-item>
      <el-form-item label="总差异" prop="totalVariance">
        <el-input v-model="formData.totalVariance" placeholder="请输入总差异" />
      </el-form-item>
      <el-form-item label="总差异率" prop="totalVarianceRate">
        <el-input v-model="formData.totalVarianceRate" placeholder="请输入总差异率" />
      </el-form-item>
      <el-form-item label="材料成本差异" prop="materialVariance">
        <el-input v-model="formData.materialVariance" placeholder="请输入材料成本差异" />
      </el-form-item>
      <el-form-item label="材料差异率" prop="materialVarianceRate">
        <el-input v-model="formData.materialVarianceRate" placeholder="请输入材料差异率" />
      </el-form-item>
      <el-form-item label="人工成本差异" prop="laborVariance">
        <el-input v-model="formData.laborVariance" placeholder="请输入人工成本差异" />
      </el-form-item>
      <el-form-item label="人工差异率" prop="laborVarianceRate">
        <el-input v-model="formData.laborVarianceRate" placeholder="请输入人工差异率" />
      </el-form-item>
      <el-form-item label="制造费用差异" prop="overheadVariance">
        <el-input v-model="formData.overheadVariance" placeholder="请输入制造费用差异" />
      </el-form-item>
      <el-form-item label="制造费用差异率" prop="overheadVarianceRate">
        <el-input v-model="formData.overheadVarianceRate" placeholder="请输入制造费用差异率" />
      </el-form-item>
      <el-form-item label="差异类型：1-有利，2-不利" prop="varianceType">
        <el-select v-model="formData.varianceType" placeholder="请选择差异类型：1-有利，2-不利">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="分析日期" prop="analysisDate">
        <el-date-picker
          v-model="formData.analysisDate"
          type="date"
          value-format="x"
          placeholder="选择分析日期"
        />
      </el-form-item>
      <el-form-item label="差异原因" prop="varianceReason">
        <el-input v-model="formData.varianceReason" placeholder="请输入差异原因" />
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
import { CostVarianceApi, CostVariance } from '@/api/erp/costvariance'

/** ERP 成本差异分析 表单 */
defineOptions({ name: 'CostVarianceForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  costActualId: undefined,
  productId: undefined,
  productionQuantity: undefined,
  standardTotalCost: undefined,
  actualTotalCost: undefined,
  totalVariance: undefined,
  totalVarianceRate: undefined,
  materialVariance: undefined,
  materialVarianceRate: undefined,
  laborVariance: undefined,
  laborVarianceRate: undefined,
  overheadVariance: undefined,
  overheadVarianceRate: undefined,
  varianceType: undefined,
  analysisDate: undefined,
  varianceReason: undefined,
  remark: undefined
})
const formRules = reactive({
  costActualId: [{ required: true, message: '实际成本ID不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品ID不能为空', trigger: 'blur' }],
  productionQuantity: [{ required: true, message: '生产数量不能为空', trigger: 'blur' }],
  standardTotalCost: [{ required: true, message: '标准总成本不能为空', trigger: 'blur' }],
  actualTotalCost: [{ required: true, message: '实际总成本不能为空', trigger: 'blur' }],
  totalVariance: [{ required: true, message: '总差异不能为空', trigger: 'blur' }],
  totalVarianceRate: [{ required: true, message: '总差异率不能为空', trigger: 'blur' }],
  analysisDate: [{ required: true, message: '分析日期不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await CostVarianceApi.getCostVariance(id)
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
    const data = formData.value as unknown as CostVariance
    if (formType.value === 'create') {
      await CostVarianceApi.createCostVariance(data)
      message.success(t('common.createSuccess'))
    } else {
      await CostVarianceApi.updateCostVariance(data)
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
    costActualId: undefined,
    productId: undefined,
    productionQuantity: undefined,
    standardTotalCost: undefined,
    actualTotalCost: undefined,
    totalVariance: undefined,
    totalVarianceRate: undefined,
    materialVariance: undefined,
    materialVarianceRate: undefined,
    laborVariance: undefined,
    laborVarianceRate: undefined,
    overheadVariance: undefined,
    overheadVarianceRate: undefined,
    varianceType: undefined,
    analysisDate: undefined,
    varianceReason: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>