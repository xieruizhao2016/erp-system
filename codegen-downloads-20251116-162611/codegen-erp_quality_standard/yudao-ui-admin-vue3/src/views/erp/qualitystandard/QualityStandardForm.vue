<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="标准编号" prop="standardNo">
        <el-input v-model="formData.standardNo" placeholder="请输入标准编号" />
      </el-form-item>
      <el-form-item label="标准名称" prop="standardName">
        <el-input v-model="formData.standardName" placeholder="请输入标准名称" />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input v-model="formData.productId" placeholder="请输入产品ID" />
      </el-form-item>
      <el-form-item label="工序ID" prop="processId">
        <el-input v-model="formData.processId" placeholder="请输入工序ID" />
      </el-form-item>
      <el-form-item label="检验类型：1-进料检验，2-过程检验，3-成品检验" prop="inspectionType">
        <el-select v-model="formData.inspectionType" placeholder="请选择检验类型：1-进料检验，2-过程检验，3-成品检验">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="标准版本" prop="standardVersion">
        <el-input v-model="formData.standardVersion" placeholder="请输入标准版本" />
      </el-form-item>
      <el-form-item label="AQL水平" prop="aqlLevel">
        <el-input v-model="formData.aqlLevel" placeholder="请输入AQL水平" />
      </el-form-item>
      <el-form-item label="抽样方法" prop="samplingMethod">
        <el-input v-model="formData.samplingMethod" placeholder="请输入抽样方法" />
      </el-form-item>
      <el-form-item label="状态：1-草稿，2-生效，3-失效" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="标准描述" prop="description">
        <Editor v-model="formData.description" height="150px" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { QualityStandardApi, QualityStandard } from '@/api/erp/qualitystandard'

/** ERP 质检标准 表单 */
defineOptions({ name: 'QualityStandardForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  standardNo: undefined,
  standardName: undefined,
  productId: undefined,
  processId: undefined,
  inspectionType: undefined,
  standardVersion: undefined,
  aqlLevel: undefined,
  samplingMethod: undefined,
  status: undefined,
  description: undefined
})
const formRules = reactive({
  standardNo: [{ required: true, message: '标准编号不能为空', trigger: 'blur' }],
  standardName: [{ required: true, message: '标准名称不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品ID不能为空', trigger: 'blur' }]
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
      formData.value = await QualityStandardApi.getQualityStandard(id)
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
    const data = formData.value as unknown as QualityStandard
    if (formType.value === 'create') {
      await QualityStandardApi.createQualityStandard(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityStandardApi.updateQualityStandard(data)
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
    standardNo: undefined,
    standardName: undefined,
    productId: undefined,
    processId: undefined,
    inspectionType: undefined,
    standardVersion: undefined,
    aqlLevel: undefined,
    samplingMethod: undefined,
    status: undefined,
    description: undefined
  }
  formRef.value?.resetFields()
}
</script>