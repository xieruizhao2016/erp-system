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
      <el-form-item label="资产总计" prop="assetTotal">
        <el-input-number
          v-model="formData.assetTotal"
          :precision="2"
          :min="0"
          placeholder="请输入资产总计"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="负债总计" prop="liabilityTotal">
        <el-input-number
          v-model="formData.liabilityTotal"
          :precision="2"
          :min="0"
          placeholder="请输入负债总计"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="所有者权益" prop="equityTotal">
        <el-input-number
          v-model="formData.equityTotal"
          :precision="2"
          :min="0"
          placeholder="请输入所有者权益"
          style="width: 100%"
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
import { BalanceSheetApi, BalanceSheetVO } from '@/api/erp/finance/balancesheet'

/** ERP 资产负债表 表单 */
defineOptions({ name: 'ErpFinanceBalanceSheetForm' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  periodDate: '',
  assetTotal: 0,
  liabilityTotal: 0,
  equityTotal: 0,
  status: 10,
  remark: ''
})
const formRules = reactive({
  periodDate: [{ required: true, message: '期间日期不能为空', trigger: 'change' }]
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
      formData.value = await BalanceSheetApi.getBalanceSheet(id)
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
    const data = formData.value as unknown as BalanceSheetVO
    if (formType.value === 'create') {
      await BalanceSheetApi.createBalanceSheet(data)
      message.success(t('common.createSuccess'))
    } else {
      await BalanceSheetApi.updateBalanceSheet(data)
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
    assetTotal: 0,
    liabilityTotal: 0,
    equityTotal: 0,
    status: 10,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

