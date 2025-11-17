<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="工单" prop="workOrderId">
        <el-select
          v-model="formData.workOrderId"
          clearable
          filterable
          placeholder="请选择工单"
          class="!w-1/1"
        >
          <el-option
            v-for="item in workOrderList"
            :key="item.id"
            :label="item.workOrderNo || `工单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工序" prop="processId">
        <el-select
          v-model="formData.processId"
          clearable
          filterable
          placeholder="请选择工序"
          class="!w-1/1"
        >
          <el-option
            v-for="item in processRouteItemList"
            :key="item.id"
            :label="item.operationName || `工序${item.id}`"
            :value="item.processId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作员" prop="operatorId">
        <el-select
          v-model="formData.operatorId"
          clearable
          filterable
          placeholder="请选择操作员"
          class="!w-1/1"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工作日期" prop="workDate">
        <el-date-picker
          v-model="formData.workDate"
          type="date"
          value-format="x"
          placeholder="选择工作日期"
        />
      </el-form-item>
      <el-form-item label="班次" prop="shiftId">
        <el-input v-model="formData.shiftId" placeholder="请输入班次" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
          v-model="formData.startTime"
          type="date"
          value-format="x"
          placeholder="选择开始时间"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          v-model="formData.endTime"
          type="date"
          value-format="x"
          placeholder="选择结束时间"
        />
      </el-form-item>
      <el-form-item label="工作时长" prop="duration">
        <el-input v-model="formData.duration" placeholder="请输入工作时长（分钟）" />
      </el-form-item>
      <el-form-item label="标准工时" prop="standardDuration">
        <el-input v-model="formData.standardDuration" placeholder="请输入标准工时（分钟）" />
      </el-form-item>
      <el-form-item label="加班时长" prop="overtimeDuration">
        <el-input v-model="formData.overtimeDuration" placeholder="请输入加班时长（分钟）" />
      </el-form-item>
      <el-form-item label="机时" prop="machineHours">
        <el-input v-model="formData.machineHours" placeholder="请输入机时" />
      </el-form-item>
      <el-form-item label="操作员工时费率" prop="operatorRate">
        <el-input v-model="formData.operatorRate" placeholder="请输入操作员工时费率" />
      </el-form-item>
      <el-form-item label="设备时费率" prop="machineRate">
        <el-input v-model="formData.machineRate" placeholder="请输入设备时费率" />
      </el-form-item>
      <el-form-item label="人工成本" prop="laborCost">
        <el-input v-model="formData.laborCost" placeholder="请输入人工成本" />
      </el-form-item>
      <el-form-item label="设备成本" prop="machineCost">
        <el-input v-model="formData.machineCost" placeholder="请输入设备成本" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_HOURS_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
import { WorkHoursApi, WorkHours } from '@/api/erp/workhours'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 工时统计 表单 */
defineOptions({ name: 'WorkHoursForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  workOrderId: undefined,
  processId: undefined,
  operatorId: undefined,
  workDate: undefined,
  shiftId: undefined,
  startTime: undefined,
  endTime: undefined,
  duration: undefined,
  standardDuration: undefined,
  overtimeDuration: undefined,
  machineHours: undefined,
  operatorRate: undefined,
  machineRate: undefined,
  laborCost: undefined,
  machineCost: undefined,
  status: undefined,
  remark: undefined
})
const formRules = reactive({
  workOrderId: [{ required: true, message: '工单不能为空', trigger: 'change' }],
  processId: [{ required: true, message: '工序不能为空', trigger: 'change' }],
  workDate: [{ required: true, message: '工作日期不能为空', trigger: 'blur' }],
  duration: [{ required: true, message: '工作时长（分钟）不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [workOrderData, processRouteItemData, users] = await Promise.all([
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    workOrderList.value = workOrderData.list || []
    processRouteItemList.value = processRouteItemData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (workOrderList.value.length === 0) {
    await loadListData()
  }
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await WorkHoursApi.getWorkHours(id)
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
    const data = formData.value as unknown as WorkHours
    if (formType.value === 'create') {
      await WorkHoursApi.createWorkHours(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkHoursApi.updateWorkHours(data)
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
    workOrderId: undefined,
    processId: undefined,
    operatorId: undefined,
    workDate: undefined,
    shiftId: undefined,
    startTime: undefined,
    endTime: undefined,
    duration: undefined,
    standardDuration: undefined,
    overtimeDuration: undefined,
    machineHours: undefined,
    operatorRate: undefined,
    machineRate: undefined,
    laborCost: undefined,
    machineCost: undefined,
    status: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>