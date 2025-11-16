<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="工单号" prop="workOrderNo">
        <el-input v-model="formData.workOrderNo" placeholder="请输入工单号" />
      </el-form-item>
      <el-form-item label="生产订单ID" prop="productionOrderId">
        <el-input v-model="formData.productionOrderId" placeholder="请输入生产订单ID" />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input v-model="formData.productId" placeholder="请输入产品ID" />
      </el-form-item>
      <el-form-item label="产品名称" prop="productName">
        <el-input v-model="formData.productName" placeholder="请输入产品名称" />
      </el-form-item>
      <el-form-item label="工单数量" prop="quantity">
        <el-input v-model="formData.quantity" placeholder="请输入工单数量" />
      </el-form-item>
      <el-form-item label="完成数量" prop="completedQuantity">
        <el-input v-model="formData.completedQuantity" placeholder="请输入完成数量" />
      </el-form-item>
      <el-form-item label="合格数量" prop="qualifiedQuantity">
        <el-input v-model="formData.qualifiedQuantity" placeholder="请输入合格数量" />
      </el-form-item>
      <el-form-item label="工作中心ID" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心ID" />
      </el-form-item>
      <el-form-item label="主管ID" prop="supervisorId">
        <el-input v-model="formData.supervisorId" placeholder="请输入主管ID" />
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
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_ORDER_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input v-model="formData.priority" placeholder="请输入优先级" />
      </el-form-item>
      <el-form-item label="作业指导书" prop="instruction">
        <el-input v-model="formData.instruction" placeholder="请输入作业指导书" />
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
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'

/** ERP 工单主 表单 */
defineOptions({ name: 'WorkOrderForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  workOrderNo: undefined,
  productionOrderId: undefined,
  productId: undefined,
  productName: undefined,
  quantity: undefined,
  completedQuantity: undefined,
  qualifiedQuantity: undefined,
  workCenterId: undefined,
  supervisorId: undefined,
  plannedStartTime: undefined,
  plannedEndTime: undefined,
  actualStartTime: undefined,
  actualEndTime: undefined,
  status: undefined,
  priority: undefined,
  instruction: undefined,
  remark: undefined
})
const formRules = reactive({
  workOrderNo: [{ required: true, message: '工单号不能为空', trigger: 'blur' }],
  productionOrderId: [{ required: true, message: '生产订单ID不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品ID不能为空', trigger: 'blur' }],
  productName: [{ required: true, message: '产品名称不能为空', trigger: 'blur' }],
  quantity: [{ required: true, message: '工单数量不能为空', trigger: 'blur' }],
  workCenterId: [{ required: true, message: '工作中心ID不能为空', trigger: 'blur' }],
  plannedStartTime: [{ required: true, message: '计划开始时间不能为空', trigger: 'blur' }],
  plannedEndTime: [{ required: true, message: '计划结束时间不能为空', trigger: 'blur' }]
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
      formData.value = await WorkOrderApi.getWorkOrder(id)
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
    const data = formData.value as unknown as WorkOrder
    if (formType.value === 'create') {
      await WorkOrderApi.createWorkOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkOrderApi.updateWorkOrder(data)
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
    workOrderNo: undefined,
    productionOrderId: undefined,
    productId: undefined,
    productName: undefined,
    quantity: undefined,
    completedQuantity: undefined,
    qualifiedQuantity: undefined,
    workCenterId: undefined,
    supervisorId: undefined,
    plannedStartTime: undefined,
    plannedEndTime: undefined,
    actualStartTime: undefined,
    actualEndTime: undefined,
    status: undefined,
    priority: undefined,
    instruction: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>