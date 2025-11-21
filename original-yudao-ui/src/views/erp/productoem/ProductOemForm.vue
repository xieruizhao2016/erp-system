<!-- ERP 产品OEM的新增/修改 -->
<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="70%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="OEM编码" prop="oemCode">
            <el-input v-model="formData.oemCode" placeholder="请输入OEM编码，如：OEM-001" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="OEM名称" prop="oemName">
            <el-input v-model="formData.oemName" placeholder="请输入OEM名称，如：富士康OEM" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工厂名称" prop="factoryName">
            <el-input v-model="formData.factoryName" placeholder="请输入工厂名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工厂编码" prop="factoryCode">
            <el-input v-model="formData.factoryCode" placeholder="请输入工厂编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="质量等级" prop="qualityLevel">
            <el-select v-model="formData.qualityLevel" placeholder="请选择质量等级" clearable class="!w-1/1">
              <el-option label="A级" value="A级" />
              <el-option label="B级" value="B级" />
              <el-option label="C级" value="C级" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="工厂地址" prop="factoryAddress">
            <el-input v-model="formData.factoryAddress" placeholder="请输入工厂地址" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工厂联系人" prop="factoryContact">
            <el-input v-model="formData.factoryContact" placeholder="请输入联系人姓名" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="factoryPhone">
            <el-input v-model="formData.factoryPhone" placeholder="请输入联系电话" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工厂邮箱" prop="factoryEmail">
            <el-input v-model="formData.factoryEmail" placeholder="请输入工厂邮箱" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="生产能力" prop="productionCapacity">
            <el-input v-model="formData.productionCapacity" placeholder="如：年产100万件" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="认证资质" prop="certification">
            <el-input v-model="formData.certification" placeholder="如：ISO9001、ISO14001" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="付款条款" prop="paymentTerms">
            <el-input v-model="formData.paymentTerms" placeholder="如：30天付款" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="交货条款" prop="deliveryTerms">
            <el-input v-model="formData.deliveryTerms" placeholder="如：FOB、CIF" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合作开始日期" prop="cooperationStartDate">
            <el-date-picker
              v-model="formData.cooperationStartDate"
              type="date"
              value-format="x"
              placeholder="选择日期"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="合作结束日期" prop="cooperationEndDate">
            <el-date-picker
              v-model="formData.cooperationEndDate"
              type="date"
              value-format="x"
              placeholder="选择日期"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="formData.sort"
              placeholder="请输入排序"
              :min="0"
              :precision="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              placeholder="请输入备注"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ProductOemApi, ProductOemVO } from '@/api/erp/productoem'

/** ERP 产品OEM表单 */
defineOptions({ name: 'ProductOemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  oemCode: '',
  oemName: '',
  factoryName: '',
  factoryCode: '',
  factoryAddress: '',
  factoryContact: '',
  factoryPhone: '',
  factoryEmail: '',
  productionCapacity: '',
  certification: '',
  cooperationStartDate: undefined,
  cooperationEndDate: undefined,
  status: 1,
  qualityLevel: '',
  paymentTerms: '',
  deliveryTerms: '',
  logoUrl: '',
  sort: 0,
  remark: ''
})
const formRules = reactive({
  oemCode: [{ required: true, message: 'OEM编码不能为空', trigger: 'blur' }],
  oemName: [{ required: true, message: 'OEM名称不能为空', trigger: 'blur' }],
  factoryName: [{ required: true, message: '工厂名称不能为空', trigger: 'blur' }],
  factoryCode: [{ required: true, message: '工厂编码不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
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
      formData.value = await ProductOemApi.get(id)
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
    const data = formData.value as unknown as ProductOemVO
    if (formType.value === 'create') {
      await ProductOemApi.create(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductOemApi.update(data)
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
    oemCode: '',
    oemName: '',
    factoryName: '',
    factoryCode: '',
    factoryAddress: '',
    factoryContact: '',
    factoryPhone: '',
    factoryEmail: '',
    productionCapacity: '',
    certification: '',
    cooperationStartDate: undefined,
    cooperationEndDate: undefined,
    status: 1,
    qualityLevel: '',
    paymentTerms: '',
    deliveryTerms: '',
    logoUrl: '',
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

