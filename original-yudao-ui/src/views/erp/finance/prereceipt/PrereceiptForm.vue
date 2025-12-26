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
      <el-form-item label="客户" prop="customerId">
        <el-select
          v-model="formData.customerId"
          clearable
          filterable
          placeholder="请选择客户"
          style="width: 100%"
        >
          <el-option
            v-for="item in customerList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="销售订单" prop="orderId">
        <el-input-number v-model="formData.orderId" placeholder="请输入销售订单编号" style="width: 100%" />
      </el-form-item>
      <el-form-item label="预收金额" prop="amount">
        <el-input-number
          v-model="formData.amount"
          :precision="2"
          :min="0"
          placeholder="请输入预收金额"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="已核销金额" prop="usedAmount">
        <el-input-number
          v-model="formData.usedAmount"
          :precision="2"
          :min="0"
          placeholder="请输入已核销金额"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="预收日期" prop="prereceiptDate">
        <el-date-picker
          v-model="formData.prereceiptDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择预收日期"
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
import { PrereceiptApi, PrereceiptVO } from '@/api/erp/finance/prereceipt'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'

/** ERP 预收款 表单 */
defineOptions({ name: 'ErpFinancePrereceiptForm' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  no: '',
  customerId: undefined,
  orderId: undefined,
  amount: 0,
  usedAmount: 0,
  balance: 0,
  prereceiptDate: '',
  status: 10,
  remark: ''
})
const formRules = reactive({
  no: [{ required: true, message: '单据号不能为空', trigger: 'blur' }],
  customerId: [{ required: true, message: '客户不能为空', trigger: 'change' }],
  amount: [{ required: true, message: '预收金额不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const customerList = ref<CustomerVO[]>([]) // 客户列表

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
      const data = await PrereceiptApi.getPrereceipt(id)
      // 将后端返回的数据映射到表单数据
      formData.value = {
        id: data.id,
        no: data.no || '',
        customerId: data.customerId,
        orderId: data.orderId,
        amount: data.amount || 0,
        usedAmount: data.usedAmount || 0,
        balance: data.balance || 0,
        prereceiptDate: data.prereceiptDate || '',
        status: data.status || 10,
        remark: data.remark || ''
      }
      // 计算余额
      formData.value.balance = formData.value.amount - (formData.value.usedAmount || 0)
    } finally {
      formLoading.value = false
    }
  }
  // 加载客户列表
  if (customerList.value.length === 0) {
    CustomerApi.getCustomerSimpleList().then((data) => {
      customerList.value = data
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
  formData.value.balance = formData.value.amount - (formData.value.usedAmount || 0)
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as PrereceiptVO
    if (formType.value === 'create') {
      await PrereceiptApi.createPrereceipt(data)
      message.success(t('common.createSuccess'))
    } else {
      await PrereceiptApi.updatePrereceipt(data)
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
    customerId: undefined,
    orderId: undefined,
    amount: 0,
    usedAmount: 0,
    balance: 0,
    prereceiptDate: '',
    status: 10,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

