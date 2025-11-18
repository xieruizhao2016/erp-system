<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="排程" prop="scheduleId">
        <el-select v-model="formData.scheduleId" placeholder="请选择排程" clearable filterable style="width: 100%">
          <el-option
            v-for="item in productionScheduleList"
            :key="item.id"
            :label="item.scheduleName || item.scheduleNo || `排程${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产订单" prop="productionOrderId">
        <el-select v-model="formData.productionOrderId" placeholder="请选择生产订单" clearable filterable style="width: 100%">
          <el-option
            v-for="item in productionOrderList"
            :key="item.id"
            :label="item.orderNo || `生产订单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select v-model="formData.productId" placeholder="请选择产品" clearable filterable style="width: 100%">
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name || `产品${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="排程数量" prop="quantity">
        <el-input v-model="formData.quantity" placeholder="请输入排程数量" />
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
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心" />
      </el-form-item>
      <el-form-item label="设备" prop="equipmentId">
        <el-select v-model="formData.equipmentId" placeholder="请选择设备" clearable filterable style="width: 100%">
          <el-option
            v-for="item in equipmentList"
            :key="item.id"
            :label="item.name || item.equipmentName || `设备${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工序序号" prop="processSequence">
        <el-input v-model="formData.processSequence" placeholder="请输入工序序号" />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input v-model="formData.priority" placeholder="请输入优先级" />
      </el-form-item>
      <el-form-item label="准备时间" prop="setupTime">
        <el-date-picker
          v-model="formData.setupTime"
          type="date"
          value-format="x"
          placeholder="选择准备时间"
        />
      </el-form-item>
      <el-form-item label="运行时间" prop="runTime">
        <el-date-picker
          v-model="formData.runTime"
          type="date"
          value-format="x"
          placeholder="选择运行时间"
        />
      </el-form-item>
      <el-form-item label="等待时间" prop="waitTime">
        <el-date-picker
          v-model="formData.waitTime"
          type="date"
          value-format="x"
          placeholder="选择等待时间"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_SCHEDULE_ITEM_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
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
      <el-form-item label="完成率" prop="completionRate">
        <el-input v-model="formData.completionRate" placeholder="请输入完成率" />
      </el-form-item>
      <el-form-item label="延迟原因" prop="delayReason">
        <el-input v-model="formData.delayReason" placeholder="请输入延迟原因" />
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
import { ProductionScheduleItemApi, ProductionScheduleItem } from '@/api/erp/productionscheduleitem'
import { ProductionScheduleApi } from '@/api/erp/productionschedule'
import { ProductionOrderApi } from '@/api/erp/productionorder'
import { ProductApi } from '@/api/erp/product/product'
import { EquipmentApi } from '@/api/erp/equipment'

/** ERP 排程明细 表单 */
defineOptions({ name: 'ProductionScheduleItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  scheduleId: undefined,
  productionOrderId: undefined,
  productId: undefined,
  quantity: undefined,
  plannedStartTime: undefined,
  plannedEndTime: undefined,
  workCenterId: undefined,
  equipmentId: undefined,
  processSequence: undefined,
  priority: undefined,
  setupTime: undefined,
  runTime: undefined,
  waitTime: undefined,
  status: undefined,
  actualStartTime: undefined,
  actualEndTime: undefined,
  completionRate: undefined,
  delayReason: undefined
})
const formRules = reactive({
  scheduleId: [{ required: true, message: '排程不能为空', trigger: 'change' }],
  productionOrderId: [{ required: true, message: '生产订单不能为空', trigger: 'change' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  quantity: [{ required: true, message: '排程数量不能为空', trigger: 'blur' }],
  plannedStartTime: [{ required: true, message: '计划开始时间不能为空', trigger: 'blur' }],
  plannedEndTime: [{ required: true, message: '计划结束时间不能为空', trigger: 'blur' }],
  runTime: [{ required: true, message: '运行时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

// 数据列表
const productionScheduleList = ref<any[]>([]) // 生产排程列表
const productionOrderList = ref<any[]>([]) // 生产订单列表
const productList = ref<any[]>([]) // 产品列表
const equipmentList = ref<any[]>([]) // 设备列表

// 加载数据列表
const loadListData = async () => {
  try {
    // 加载生产排程列表
    const productionScheduleData = await ProductionScheduleApi.getProductionSchedulePage({ pageNo: 1, pageSize: 100 })
    productionScheduleList.value = productionScheduleData.list || []

    // 加载生产订单列表
    const productionOrderData = await ProductionOrderApi.getProductionOrderPage({ pageNo: 1, pageSize: 100 })
    productionOrderList.value = productionOrderData.list || []

    // 加载产品列表
    const productData = await ProductApi.getProductPage({ pageNo: 1, pageSize: 100 })
    productList.value = productData.list || []

    // 加载设备列表
    const equipmentData = await EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 })
    equipmentList.value = equipmentData.list || []
  } catch (error) {
    console.error('加载数据列表失败:', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 加载数据列表
  await loadListData()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProductionScheduleItemApi.getProductionScheduleItem(id)
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
    const data = formData.value as unknown as ProductionScheduleItem
    if (formType.value === 'create') {
      await ProductionScheduleItemApi.createProductionScheduleItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionScheduleItemApi.updateProductionScheduleItem(data)
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
    scheduleId: undefined,
    productionOrderId: undefined,
    productId: undefined,
    quantity: undefined,
    plannedStartTime: undefined,
    plannedEndTime: undefined,
    workCenterId: undefined,
    equipmentId: undefined,
    processSequence: undefined,
    priority: undefined,
    setupTime: undefined,
    runTime: undefined,
    waitTime: undefined,
    status: undefined,
    actualStartTime: undefined,
    actualEndTime: undefined,
    completionRate: undefined,
    delayReason: undefined
  }
  formRef.value?.resetFields()
}
</script>