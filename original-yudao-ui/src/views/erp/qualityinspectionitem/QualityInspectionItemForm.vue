<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="检验ID" prop="inspectionId">
        <el-input v-model="formData.inspectionId" placeholder="请输入检验ID" />
      </el-form-item>
      <el-form-item label="检验项目ID" prop="itemId">
        <el-input v-model="formData.itemId" placeholder="请输入检验项目ID" />
      </el-form-item>
      <el-form-item label="样本编号" prop="sampleNo">
        <el-input v-model="formData.sampleNo" placeholder="请输入样本编号" />
      </el-form-item>
      <el-form-item label="测量值" prop="measuredValue">
        <el-input v-model="formData.measuredValue" placeholder="请输入测量值" />
      </el-form-item>
      <el-form-item label="实际数值" prop="actualValue">
        <el-input v-model="formData.actualValue" placeholder="请输入实际数值" />
      </el-form-item>
      <el-form-item label="检验结果：1-合格，2-不合格，3-超差" prop="testResult">
        <el-input v-model="formData.testResult" placeholder="请输入检验结果：1-合格，2-不合格，3-超差" />
      </el-form-item>
      <el-form-item label="缺陷类型" prop="defectType">
        <el-select v-model="formData.defectType" placeholder="请选择缺陷类型">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="缺陷描述" prop="defectDescription">
        <Editor v-model="formData.defectDescription" height="150px" />
      </el-form-item>
      <el-form-item label="缺陷图片URLs" prop="imageUrls">
        <el-input v-model="formData.imageUrls" placeholder="请输入缺陷图片URLs" />
      </el-form-item>
      <el-form-item label="检验员ID" prop="inspectorId">
        <el-input v-model="formData.inspectorId" placeholder="请输入检验员ID" />
      </el-form-item>
      <el-form-item label="检验时间" prop="inspectionTime">
        <el-date-picker
          v-model="formData.inspectionTime"
          type="date"
          value-format="x"
          placeholder="选择检验时间"
        />
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
import { QualityInspectionItemApi, QualityInspectionItem } from '@/api/erp/qualityinspectionitem'

/** ERP 质检明细 表单 */
defineOptions({ name: 'QualityInspectionItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  inspectionId: undefined,
  itemId: undefined,
  sampleNo: undefined,
  measuredValue: undefined,
  actualValue: undefined,
  testResult: undefined,
  defectType: undefined,
  defectDescription: undefined,
  imageUrls: undefined,
  inspectorId: undefined,
  inspectionTime: undefined,
  remark: undefined
})
const formRules = reactive({
  inspectionId: [{ required: true, message: '检验ID不能为空', trigger: 'blur' }],
  itemId: [{ required: true, message: '检验项目ID不能为空', trigger: 'blur' }],
  inspectionTime: [{ required: true, message: '检验时间不能为空', trigger: 'blur' }]
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
      formData.value = await QualityInspectionItemApi.getQualityInspectionItem(id)
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
    const data = formData.value as unknown as QualityInspectionItem
    if (formType.value === 'create') {
      await QualityInspectionItemApi.createQualityInspectionItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityInspectionItemApi.updateQualityInspectionItem(data)
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
    inspectionId: undefined,
    itemId: undefined,
    sampleNo: undefined,
    measuredValue: undefined,
    actualValue: undefined,
    testResult: undefined,
    defectType: undefined,
    defectDescription: undefined,
    imageUrls: undefined,
    inspectorId: undefined,
    inspectionTime: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>