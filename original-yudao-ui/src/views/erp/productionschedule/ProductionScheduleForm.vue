<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="排程单号" prop="scheduleNo">
        <el-input v-model="formData.scheduleNo" disabled placeholder="保存时自动生成" />
      </el-form-item>
      <el-form-item label="排程名称" prop="scheduleName">
        <el-input v-model="formData.scheduleName" placeholder="请输入排程名称" />
      </el-form-item>
      <el-form-item label="排程类型" prop="scheduleType">
        <el-select v-model="formData.scheduleType" placeholder="请选择排程类型" clearable>
          <el-option label="主排程" value="1" />
          <el-option label="详细排程" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="计划天数" prop="planningHorizonDays">
        <el-input v-model="formData.planningHorizonDays" placeholder="请输入计划天数" />
      </el-form-item>
      <el-form-item label="计划开始日期" prop="startDate">
        <el-date-picker
          v-model="formData.startDate"
          type="date"
          value-format="x"
          placeholder="选择计划开始日期"
        />
      </el-form-item>
      <el-form-item label="计划结束日期" prop="endDate">
        <el-date-picker
          v-model="formData.endDate"
          type="date"
          value-format="x"
          placeholder="选择计划结束日期"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_SCHEDULE_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="总订单数" prop="totalOrders">
        <el-input v-model="formData.totalOrders" placeholder="请输入总订单数" />
      </el-form-item>
      <el-form-item label="总数量" prop="totalQuantity">
        <el-input v-model="formData.totalQuantity" placeholder="请输入总数量" />
      </el-form-item>
      <el-form-item label="总工时" prop="totalWorkHours">
        <el-input v-model="formData.totalWorkHours" placeholder="请输入总工时" />
      </el-form-item>
      <el-form-item label="创建人" prop="createdBy">
        <el-input v-model="formData.createdBy" placeholder="请输入创建人" />
      </el-form-item>
      <el-form-item label="更新人" prop="updatedBy">
        <el-input v-model="formData.updatedBy" placeholder="请输入更新人" />
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
import { ProductionScheduleApi, ProductionSchedule } from '@/api/erp/productionschedule'

/** ERP 生产排程主 表单 */
defineOptions({ name: 'ProductionScheduleForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  scheduleNo: undefined,
  scheduleName: undefined,
  scheduleType: undefined,
  planningHorizonDays: undefined,
  startDate: undefined,
  endDate: undefined,
  status: undefined,
  totalOrders: undefined,
  totalQuantity: undefined,
  totalWorkHours: undefined,
  createdBy: undefined,
  updatedBy: undefined
})
const formRules = reactive({
  scheduleName: [{ required: true, message: '排程名称不能为空', trigger: 'blur' }],
  startDate: [{ required: true, message: '计划开始日期不能为空', trigger: 'blur' }],
  endDate: [{ required: true, message: '计划结束日期不能为空', trigger: 'blur' }]
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
      formData.value = await ProductionScheduleApi.getProductionSchedule(id)
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
    const data = formData.value as unknown as ProductionSchedule
    if (formType.value === 'create') {
      await ProductionScheduleApi.createProductionSchedule(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionScheduleApi.updateProductionSchedule(data)
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
    scheduleNo: undefined,
    scheduleName: undefined,
    scheduleType: undefined,
    planningHorizonDays: undefined,
    startDate: undefined,
    endDate: undefined,
    status: undefined,
    totalOrders: undefined,
    totalQuantity: undefined,
    totalWorkHours: undefined,
    createdBy: undefined,
    updatedBy: undefined
  }
  formRef.value?.resetFields()
}
</script>