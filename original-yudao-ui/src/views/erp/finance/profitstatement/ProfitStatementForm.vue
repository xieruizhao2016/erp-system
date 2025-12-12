<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="期间日期" prop="periodDate">
        <el-date-picker
          v-model="formData.periodDate"
          type="month"
          value-format="YYYY-MM"
          placeholder="请选择期间日期"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="营业收入" prop="revenue">
        <el-input-number
          v-model="formData.revenue"
          :precision="2"
          :min="0"
          placeholder="请输入营业收入"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="营业成本" prop="cost">
        <el-input-number
          v-model="formData.cost"
          :precision="2"
          :min="0"
          placeholder="请输入营业成本"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="毛利润" prop="grossProfit">
        <el-input-number
          v-model="formData.grossProfit"
          :precision="2"
          placeholder="自动计算：营业收入 - 营业成本"
          style="width: 100%"
          :disabled="true"
        />
      </el-form-item>
      <el-form-item label="营业费用" prop="operatingExpense">
        <el-input-number
          v-model="formData.operatingExpense"
          :precision="2"
          :min="0"
          placeholder="请输入营业费用"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="净利润" prop="netProfit">
        <el-input-number
          v-model="formData.netProfit"
          :precision="2"
          placeholder="自动计算：毛利润 - 营业费用"
          style="width: 100%"
          :disabled="true"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_AUDIT_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
          type="textarea"
          placeholder="请输入备注"
          :rows="3"
        />
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
import { ProfitStatementApi, ProfitStatementVO } from '@/api/erp/finance/profitstatement'
import { watch } from 'vue'

/** ERP 利润表 表单 */
defineOptions({ name: 'ErpFinanceProfitStatementForm' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  periodDate: '',
  revenue: 0,
  cost: 0,
  grossProfit: 0,
  operatingExpense: 0,
  netProfit: 0,
  status: 10,
  remark: ''
})
const formRules = reactive({
  periodDate: [{ required: true, message: '期间日期不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref

// 监听收入和成本变化，自动计算毛利润
watch([() => formData.value.revenue, () => formData.value.cost], () => {
  formData.value.grossProfit = (formData.value.revenue || 0) - (formData.value.cost || 0)
})

// 监听毛利润和营业费用变化，自动计算净利润
watch([() => formData.value.grossProfit, () => formData.value.operatingExpense], () => {
  formData.value.netProfit = (formData.value.grossProfit || 0) - (formData.value.operatingExpense || 0)
})

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
      formData.value = await ProfitStatementApi.getProfitStatement(id)
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
    const data = formData.value as unknown as ProfitStatementVO
    if (formType.value === 'create') {
      await ProfitStatementApi.createProfitStatement(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProfitStatementApi.updateProfitStatement(data)
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
    periodDate: '',
    revenue: 0,
    cost: 0,
    grossProfit: 0,
    operatingExpense: 0,
    netProfit: 0,
    status: 10,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

