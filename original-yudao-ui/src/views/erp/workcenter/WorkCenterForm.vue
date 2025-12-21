<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工作中心编号" prop="workCenterNo">
            <el-input v-model="formData.workCenterNo" placeholder="请输入工作中心编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工作中心名称" prop="workCenterName">
            <el-input v-model="formData.workCenterName" placeholder="请输入工作中心名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入描述"
        />
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="位置" prop="location">
            <el-input v-model="formData.location" placeholder="请输入位置" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="responsiblePerson">
            <el-input v-model="formData.responsiblePerson" placeholder="请输入负责人" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { WorkCenterApi, WorkCenter } from '@/api/erp/workcenter'

/** ERP 工作中心 表单 */
defineOptions({ name: 'WorkCenterForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  workCenterNo: undefined,
  workCenterName: undefined,
  description: undefined,
  location: undefined,
  responsiblePerson: undefined,
  status: 1,
  remark: undefined
})
const formRules = reactive({
  workCenterNo: [{ required: true, message: '工作中心编号不能为空', trigger: 'blur' }],
  workCenterName: [{ required: true, message: '工作中心名称不能为空', trigger: 'blur' }]
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
      formData.value = await WorkCenterApi.getWorkCenter(id)
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
    const data = formData.value as unknown as WorkCenter
    if (formType.value === 'create') {
      await WorkCenterApi.createWorkCenter(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkCenterApi.updateWorkCenter(data)
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
    workCenterNo: undefined,
    workCenterName: undefined,
    description: undefined,
    location: undefined,
    responsiblePerson: undefined,
    status: 1,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>

