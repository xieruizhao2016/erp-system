<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="单据号" prop="no">
        <el-input v-model="formData.no" placeholder="请输入单据号" />
      </el-form-item>
      <el-form-item label="供应商" prop="supplierId">
        <el-select
          v-model="formData.supplierId"
          clearable
          filterable
          placeholder="请选择供应商"
          style="width: 100%"
        >
          <el-option
            v-for="item in supplierList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="采购订单" prop="orderId">
        <el-input-number v-model="formData.orderId" placeholder="请输入采购订单编号" style="width: 100%" />
      </el-form-item>
      <el-form-item label="应付金额" prop="amount">
        <el-input-number
          v-model="formData.amount"
          :precision="2"
          :min="0"
          placeholder="请输入应付金额"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="已付金额" prop="paidAmount">
        <el-input-number
          v-model="formData.paidAmount"
          :precision="2"
          :min="0"
          placeholder="请输入已付金额"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="到期日" prop="dueDate">
        <el-date-picker
          v-model="formData.dueDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择到期日"
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
import { PayableApi, PayableVO } from '@/api/erp/finance/payable'
import { SupplierApi, SupplierVO } from '@/api/erp/purchase/supplier'

/** ERP 应付账款 表单 */
defineOptions({ name: 'ErpFinancePayableForm' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  no: '',
  supplierId: undefined,
  orderId: undefined,
  amount: 0,
  paidAmount: 0,
  balance: 0,
  dueDate: '',
  status: 10,
  remark: ''
})
const formRules = reactive({
  no: [{ required: true, message: '单据号不能为空', trigger: 'blur' }],
  supplierId: [{ required: true, message: '供应商不能为空', trigger: 'change' }],
  amount: [{ required: true, message: '应付金额不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const supplierList = ref<SupplierVO[]>([]) // 供应商列表
const initialNo = ref('') // 初始生成的单据号，用于判断用户是否修改

/** 生成单据号 */
const generateNo = () => {
  const now = new Date()
  const year = now.getFullYear()
  const month = String(now.getMonth() + 1).padStart(2, '0')
  const day = String(now.getDate()).padStart(2, '0')
  const dateStr = `${year}${month}${day}`
  // 生成6位随机序号（实际应该由后端生成，这里只是预览）
  const seq = String(Math.floor(Math.random() * 1000000)).padStart(6, '0')
  return `YFZK${dateStr}${seq}`
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 新增时，生成默认单据号
  if (type === 'create') {
    initialNo.value = generateNo()
    formData.value.no = initialNo.value
  } else {
    initialNo.value = ''
  }
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await PayableApi.getPayable(id)
      // 将后端返回的数据映射到表单数据
      formData.value = {
        id: data.id,
        no: data.no || '',
        supplierId: data.supplierId,
        orderId: data.orderId,
        amount: data.amount || 0,
        paidAmount: data.paidAmount || 0,
        balance: data.balance || 0,
        dueDate: data.dueDate || '',
        status: data.status || 10,
        remark: data.remark || ''
      }
      // 计算余额
      formData.value.balance = formData.value.amount - (formData.value.paidAmount || 0)
    } finally {
      formLoading.value = false
    }
  }
  // 加载供应商列表
  if (supplierList.value.length === 0) {
    SupplierApi.getSupplierSimpleList().then((data) => {
      supplierList.value = data
    })
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 计算余额
  formData.value.balance = formData.value.amount - (formData.value.paidAmount || 0)
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as PayableVO
    // 新增时，如果用户没有修改单据号，传空让后端自动生成正确的序号
    if (formType.value === 'create' && data.no === initialNo.value) {
      data.no = ''
    }
    if (formType.value === 'create') {
      await PayableApi.createPayable(data)
      message.success(t('common.createSuccess'))
    } else {
      await PayableApi.updatePayable(data)
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
    no: '',
    supplierId: undefined,
    orderId: undefined,
    amount: 0,
    paidAmount: 0,
    balance: 0,
    dueDate: '',
    status: 10,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

