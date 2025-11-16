<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="配置名称" prop="configName">
        <el-input v-model="formData.configName" placeholder="请输入配置名称" />
      </el-form-item>
      <el-form-item label="配置类型：1-大屏，2-PC端，3-移动端" prop="configType">
        <el-select v-model="formData.configType" placeholder="请选择配置类型：1-大屏，2-PC端，3-移动端">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="屏幕分辨率" prop="screenResolution">
        <el-input v-model="formData.screenResolution" placeholder="请输入屏幕分辨率" />
      </el-form-item>
      <el-form-item label="布局配置（JSON）" prop="layoutConfig">
        <el-input v-model="formData.layoutConfig" placeholder="请输入布局配置（JSON）" />
      </el-form-item>
      <el-form-item label="组件配置（JSON）" prop="componentConfig">
        <el-input v-model="formData.componentConfig" placeholder="请输入组件配置（JSON）" />
      </el-form-item>
      <el-form-item label="数据刷新间隔（秒）" prop="dataRefreshInterval">
        <el-input v-model="formData.dataRefreshInterval" placeholder="请输入数据刷新间隔（秒）" />
      </el-form-item>
      <el-form-item label="是否默认配置" prop="isDefault">
        <el-radio-group v-model="formData.isDefault">
          <el-radio value="1">请选择字典生成</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否启用" prop="isActive">
        <el-radio-group v-model="formData.isActive">
          <el-radio value="1">请选择字典生成</el-radio>
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
import { ProductionDashboardConfigApi, ProductionDashboardConfig } from '@/api/erp/productiondashboardconfig'

/** ERP 看板配置 表单 */
defineOptions({ name: 'ProductionDashboardConfigForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  configName: undefined,
  configType: undefined,
  screenResolution: undefined,
  layoutConfig: undefined,
  componentConfig: undefined,
  dataRefreshInterval: undefined,
  isDefault: undefined,
  isActive: undefined
})
const formRules = reactive({
  configName: [{ required: true, message: '配置名称不能为空', trigger: 'blur' }],
  layoutConfig: [{ required: true, message: '布局配置（JSON）不能为空', trigger: 'blur' }],
  componentConfig: [{ required: true, message: '组件配置（JSON）不能为空', trigger: 'blur' }]
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
      formData.value = await ProductionDashboardConfigApi.getProductionDashboardConfig(id)
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
    const data = formData.value as unknown as ProductionDashboardConfig
    if (formType.value === 'create') {
      await ProductionDashboardConfigApi.createProductionDashboardConfig(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionDashboardConfigApi.updateProductionDashboardConfig(data)
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
    configName: undefined,
    configType: undefined,
    screenResolution: undefined,
    layoutConfig: undefined,
    componentConfig: undefined,
    dataRefreshInterval: undefined,
    isDefault: undefined,
    isActive: undefined
  }
  formRef.value?.resetFields()
}
</script>