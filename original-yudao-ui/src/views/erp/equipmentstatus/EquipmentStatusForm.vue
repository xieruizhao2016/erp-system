<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="设备ID" prop="equipmentId">
        <el-input v-model="formData.equipmentId" placeholder="请输入设备ID" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_EQUIPMENT_STATUS_RECORD)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="状态开始时间" prop="statusStartTime">
        <el-date-picker
          v-model="formData.statusStartTime"
          type="date"
          value-format="x"
          placeholder="选择状态开始时间"
        />
      </el-form-item>
      <el-form-item label="状态结束时间" prop="statusEndTime">
        <el-date-picker
          v-model="formData.statusEndTime"
          type="date"
          value-format="x"
          placeholder="选择状态结束时间"
        />
      </el-form-item>
      <el-form-item label="持续时间（分钟）" prop="duration">
        <el-input v-model="formData.duration" placeholder="请输入持续时间（分钟）" />
      </el-form-item>
      <el-form-item label="关联工单ID" prop="workOrderId">
        <el-input v-model="formData.workOrderId" placeholder="请输入关联工单ID" />
      </el-form-item>
      <el-form-item label="操作员ID" prop="operatorId">
        <el-input v-model="formData.operatorId" placeholder="请输入操作员ID" />
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
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { EquipmentStatusApi, EquipmentStatus } from '@/api/erp/equipmentstatus'

/** ERP 设备状态记录 表单 */
defineOptions({ name: 'EquipmentStatusForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  equipmentId: undefined,
  status: undefined,
  statusStartTime: undefined,
  statusEndTime: undefined,
  duration: undefined,
  workOrderId: undefined,
  operatorId: undefined,
  remark: undefined
})
const formRules = reactive({
  equipmentId: [{ required: true, message: '设备ID不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态：1-运行，2-待机，3-故障，4-维修，5-停机不能为空', trigger: 'blur' }],
  statusStartTime: [{ required: true, message: '状态开始时间不能为空', trigger: 'blur' }]
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
      formData.value = await EquipmentStatusApi.getEquipmentStatus(id)
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
    const data = formData.value as unknown as EquipmentStatus
    if (formType.value === 'create') {
      await EquipmentStatusApi.createEquipmentStatus(data)
      message.success(t('common.createSuccess'))
    } else {
      await EquipmentStatusApi.updateEquipmentStatus(data)
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
    equipmentId: undefined,
    status: undefined,
    statusStartTime: undefined,
    statusEndTime: undefined,
    duration: undefined,
    workOrderId: undefined,
    operatorId: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>