<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="参数名称" prop="paramName">
        <el-input v-model="formData.paramName" placeholder="请输入参数名称" />
      </el-form-item>
      <el-form-item label="参数编码" prop="paramCode">
        <el-input v-model="formData.paramCode" placeholder="请输入参数编码" />
      </el-form-item>
      <el-form-item label="参数值" prop="paramValue">
        <el-input v-model="formData.paramValue" placeholder="请输入参数值" />
      </el-form-item>
      <el-form-item label="参数类型" prop="paramType">
        <el-select v-model="formData.paramType" placeholder="请选择参数类型" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_PARAM_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="参数描述" prop="description">
        <Editor v-model="formData.description" height="150px" />
      </el-form-item>
      <el-form-item label="是否系统参数" prop="isSystem">
        <el-radio-group v-model="formData.isSystem">
          <el-radio :value="true">是</el-radio>
          <el-radio :value="false">否</el-radio>
        </el-radio-group>
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
import { MrpParamsApi, MrpParams } from '@/api/erp/mrpparams'

/** ERP MRP参数 表单 */
defineOptions({ name: 'MrpParamsForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  paramName: undefined,
  paramCode: undefined,
  paramValue: undefined,
  paramType: undefined,
  description: undefined,
  isSystem: undefined
})
const formRules = reactive({
  paramName: [{ required: true, message: '参数名称不能为空', trigger: 'blur' }],
  paramCode: [{ required: true, message: '参数编码不能为空', trigger: 'blur' }],
  paramValue: [{ required: true, message: '参数值不能为空', trigger: 'blur' }]
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
      formData.value = await MrpParamsApi.getMrpParams(id)
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
    const data = formData.value as unknown as MrpParams
    if (formType.value === 'create') {
      await MrpParamsApi.createMrpParams(data)
      message.success(t('common.createSuccess'))
    } else {
      await MrpParamsApi.updateMrpParams(data)
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
    paramName: undefined,
    paramCode: undefined,
    paramValue: undefined,
    paramType: undefined,
    description: undefined,
    isSystem: undefined
  }
  formRef.value?.resetFields()
}
</script>