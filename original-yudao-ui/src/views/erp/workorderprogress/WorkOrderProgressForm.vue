<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
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
      <el-form-item label="工序名称" prop="processName">
        <el-input v-model="formData.processName" placeholder="请输入工序名称" />
      </el-form-item>
      <el-form-item label="工序序号" prop="sequence">
        <el-input v-model="formData.sequence" placeholder="请输入工序序号" />
      </el-form-item>
      <el-form-item label="计划开始时间" prop="plannedStartTime">
        <el-date-picker
          v-model="formData.plannedStartTime"
          type="date"
          value-format="x"
          placeholder="选择计划开始时间"
        />
      </el-form-item>
      <el-form-item label="计划结束时间" prop="plannedEndTime">
        <el-date-picker
          v-model="formData.plannedEndTime"
          type="date"
          value-format="x"
          placeholder="选择计划结束时间"
        />
      </el-form-item>
      <el-form-item label="实际开始时间" prop="actualStartTime">
        <el-date-picker
          v-model="formData.actualStartTime"
          type="date"
          value-format="x"
          placeholder="选择实际开始时间"
        />
      </el-form-item>
      <el-form-item label="实际结束时间" prop="actualEndTime">
        <el-date-picker
          v-model="formData.actualEndTime"
          type="date"
          value-format="x"
          placeholder="选择实际结束时间"
        />
      </el-form-item>
      <el-form-item label="计划数量" prop="plannedQuantity">
        <el-input v-model="formData.plannedQuantity" placeholder="请输入计划数量" />
      </el-form-item>
      <el-form-item label="完成数量" prop="completedQuantity">
        <el-input v-model="formData.completedQuantity" placeholder="请输入完成数量" />
      </el-form-item>
      <el-form-item label="合格数量" prop="qualifiedQuantity">
        <el-input v-model="formData.qualifiedQuantity" placeholder="请输入合格数量" />
      </el-form-item>
      <el-form-item label="不合格数量" prop="rejectedQuantity">
        <el-input v-model="formData.rejectedQuantity" placeholder="请输入不合格数量" />
      </el-form-item>
      <el-form-item label="报废数量" prop="scrapQuantity">
        <el-input v-model="formData.scrapQuantity" placeholder="请输入报废数量" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_ORDER_PROGRESS_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
      <el-form-item label="设备" prop="equipmentId">
        <el-select
          v-model="formData.equipmentId"
          clearable
          filterable
          placeholder="请选择设备"
          class="!w-1/1"
        >
          <el-option
            v-for="item in equipmentList"
            :key="item.id"
            :label="item.equipmentName || item.equipmentNo || `设备${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="实际工时" prop="workTime">
        <el-input-number v-model="formData.workTime" placeholder="请输入实际工时（分钟）" :min="0" class="!w-1/1" />
      </el-form-item>
      <el-form-item label="停机时间" prop="downtime">
        <el-input-number v-model="formData.downtime" placeholder="请输入停机时间（分钟）" :min="0" class="!w-1/1" />
      </el-form-item>
      <el-form-item label="质检状态" prop="qualityStatus">
        <el-radio-group v-model="formData.qualityStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_QUALITY_STATUS)"
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
import { WorkOrderProgressApi, WorkOrderProgress } from '@/api/erp/workorderprogress'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { EquipmentApi, Equipment } from '@/api/erp/equipment'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 工单进度 表单 */
defineOptions({ name: 'WorkOrderProgressForm' })

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
  processName: undefined,
  sequence: undefined,
  plannedStartTime: undefined,
  plannedEndTime: undefined,
  actualStartTime: undefined,
  actualEndTime: undefined,
  plannedQuantity: undefined,
  completedQuantity: undefined,
  qualifiedQuantity: undefined,
  rejectedQuantity: undefined,
  scrapQuantity: undefined,
  status: undefined,
  operatorId: undefined,
  equipmentId: undefined,
  workTime: undefined,
  downtime: undefined,
  qualityStatus: undefined,
  remark: undefined
})
const formRules = reactive({
  workOrderId: [{ required: true, message: '工单不能为空', trigger: 'change' }],
  processId: [{ required: true, message: '工序不能为空', trigger: 'change' }],
  processName: [{ required: true, message: '工序名称不能为空', trigger: 'blur' }],
  sequence: [{ required: true, message: '工序序号不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const equipmentList = ref<Equipment[]>([]) // 设备列表
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [workOrderData, equipmentData, processRouteItemData, users] = await Promise.all([
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 }),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    workOrderList.value = workOrderData.list || []
    equipmentList.value = equipmentData.list || []
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
      formData.value = await WorkOrderProgressApi.getWorkOrderProgress(id)
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
    const data = formData.value as unknown as WorkOrderProgress
    if (formType.value === 'create') {
      await WorkOrderProgressApi.createWorkOrderProgress(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkOrderProgressApi.updateWorkOrderProgress(data)
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
    processName: undefined,
    sequence: undefined,
    plannedStartTime: undefined,
    plannedEndTime: undefined,
    actualStartTime: undefined,
    actualEndTime: undefined,
    plannedQuantity: undefined,
    completedQuantity: undefined,
    qualifiedQuantity: undefined,
    rejectedQuantity: undefined,
    scrapQuantity: undefined,
    status: undefined,
    operatorId: undefined,
    equipmentId: undefined,
    workTime: undefined,
    downtime: undefined,
    qualityStatus: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>