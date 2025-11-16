<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="标准ID" prop="standardId">
        <el-input v-model="formData.standardId" placeholder="请输入标准ID" />
      </el-form-item>
      <el-form-item label="项目编号" prop="itemNo">
        <el-input v-model="formData.itemNo" placeholder="请输入项目编号" />
      </el-form-item>
      <el-form-item label="项目名称" prop="itemName">
        <el-input v-model="formData.itemName" placeholder="请输入项目名称" />
      </el-form-item>
      <el-form-item label="项目类型：1-定性，2-定量" prop="itemType">
        <el-select v-model="formData.itemType" placeholder="请选择项目类型：1-定性，2-定量">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="检验方法" prop="testMethod">
        <el-input v-model="formData.testMethod" placeholder="请输入检验方法" />
      </el-form-item>
      <el-form-item label="标准值" prop="standardValue">
        <el-input v-model="formData.standardValue" placeholder="请输入标准值" />
      </el-form-item>
      <el-form-item label="公差范围" prop="tolerance">
        <el-input v-model="formData.tolerance" placeholder="请输入公差范围" />
      </el-form-item>
      <el-form-item label="单位" prop="unit">
        <el-input v-model="formData.unit" placeholder="请输入单位" />
      </el-form-item>
      <el-form-item label="是否关键项" prop="isKeyItem">
        <el-radio-group v-model="formData.isKeyItem">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="检验序号" prop="sequence">
        <el-input v-model="formData.sequence" placeholder="请输入检验序号" />
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
import { QualityItemApi, QualityItem } from '@/api/erp/qualityitem'

/** ERP 质检项目 表单 */
defineOptions({ name: 'QualityItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  standardId: undefined,
  itemNo: undefined,
  itemName: undefined,
  itemType: undefined,
  testMethod: undefined,
  standardValue: undefined,
  tolerance: undefined,
  unit: undefined,
  isKeyItem: undefined,
  sequence: undefined,
  remark: undefined
})
const formRules = reactive({
  standardId: [{ required: true, message: '标准ID不能为空', trigger: 'blur' }],
  itemNo: [{ required: true, message: '项目编号不能为空', trigger: 'blur' }],
  itemName: [{ required: true, message: '项目名称不能为空', trigger: 'blur' }]
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
      formData.value = await QualityItemApi.getQualityItem(id)
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
    const data = formData.value as unknown as QualityItem
    if (formType.value === 'create') {
      await QualityItemApi.createQualityItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityItemApi.updateQualityItem(data)
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
    standardId: undefined,
    itemNo: undefined,
    itemName: undefined,
    itemType: undefined,
    testMethod: undefined,
    standardValue: undefined,
    tolerance: undefined,
    unit: undefined,
    isKeyItem: undefined,
    sequence: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>