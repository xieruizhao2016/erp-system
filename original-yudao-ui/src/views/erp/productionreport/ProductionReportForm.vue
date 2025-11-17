<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="报表编号" prop="reportNo">
        <el-input v-model="formData.reportNo" placeholder="请输入报表编号" />
      </el-form-item>
      <el-form-item label="报表名称" prop="reportName">
        <el-input v-model="formData.reportName" placeholder="请输入报表名称" />
      </el-form-item>
      <el-form-item label="报表类型" prop="reportType">
        <el-select v-model="formData.reportType" placeholder="请选择报表类型" clearable>
          <el-option label="日报" value="1" />
          <el-option label="周报" value="2" />
          <el-option label="月报" value="3" />
          <el-option label="年报" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="报表期间" prop="reportPeriod">
        <el-input v-model="formData.reportPeriod" placeholder="请输入报表期间" />
      </el-form-item>
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心" />
      </el-form-item>
      <el-form-item label="生产订单数" prop="productionOrders">
        <el-input v-model="formData.productionOrders" placeholder="请输入生产订单数" />
      </el-form-item>
      <el-form-item label="总计划数量" prop="totalPlanQuantity">
        <el-input v-model="formData.totalPlanQuantity" placeholder="请输入总计划数量" />
      </el-form-item>
      <el-form-item label="总完成数量" prop="totalCompletedQuantity">
        <el-input v-model="formData.totalCompletedQuantity" placeholder="请输入总完成数量" />
      </el-form-item>
      <el-form-item label="总合格数量" prop="totalQualifiedQuantity">
        <el-input v-model="formData.totalQualifiedQuantity" placeholder="请输入总合格数量" />
      </el-form-item>
      <el-form-item label="完成率" prop="completionRate">
        <el-input v-model="formData.completionRate" placeholder="请输入完成率" />
      </el-form-item>
      <el-form-item label="合格率" prop="qualityRate">
        <el-input v-model="formData.qualityRate" placeholder="请输入合格率" />
      </el-form-item>
      <el-form-item label="总工时" prop="totalWorkHours">
        <el-input v-model="formData.totalWorkHours" placeholder="请输入总工时" />
      </el-form-item>
      <el-form-item label="总机时" prop="totalEquipmentHours">
        <el-input v-model="formData.totalEquipmentHours" placeholder="请输入总机时" />
      </el-form-item>
      <el-form-item label="OEE" prop="oee">
        <el-input v-model="formData.oee" placeholder="请输入OEE" />
      </el-form-item>
      <el-form-item label="准时交付率" prop="onTimeDeliveryRate">
        <el-input v-model="formData.onTimeDeliveryRate" placeholder="请输入准时交付率" />
      </el-form-item>
      <el-form-item label="产值" prop="productionValue">
        <el-input v-model="formData.productionValue" placeholder="请输入产值" />
      </el-form-item>
      <el-form-item label="平均成本" prop="averageCost">
        <el-input v-model="formData.averageCost" placeholder="请输入平均成本" />
      </el-form-item>
      <el-form-item label="报表日期" prop="reportDate">
        <el-date-picker
          v-model="formData.reportDate"
          type="date"
          value-format="x"
          placeholder="选择报表日期"
        />
      </el-form-item>
      <el-form-item label="生成时间" prop="generateTime">
        <el-date-picker
          v-model="formData.generateTime"
          type="date"
          value-format="x"
          placeholder="选择生成时间"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_REPORT_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="详细数据" prop="reportData">
        <el-input v-model="formData.reportData" placeholder="请输入详细数据（JSON）" />
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
import { ProductionReportApi, ProductionReport } from '@/api/erp/productionreport'

/** ERP 生产报表 表单 */
defineOptions({ name: 'ProductionReportForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  reportNo: undefined,
  reportName: undefined,
  reportType: undefined,
  reportPeriod: undefined,
  workCenterId: undefined,
  productionOrders: undefined,
  totalPlanQuantity: undefined,
  totalCompletedQuantity: undefined,
  totalQualifiedQuantity: undefined,
  completionRate: undefined,
  qualityRate: undefined,
  totalWorkHours: undefined,
  totalEquipmentHours: undefined,
  oee: undefined,
  onTimeDeliveryRate: undefined,
  productionValue: undefined,
  averageCost: undefined,
  reportDate: undefined,
  generateTime: undefined,
  status: undefined,
  reportData: undefined
})
const formRules = reactive({
  reportNo: [{ required: true, message: '报表编号不能为空', trigger: 'blur' }],
  reportName: [{ required: true, message: '报表名称不能为空', trigger: 'blur' }],
  reportType: [{ required: true, message: '报表类型不能为空', trigger: 'change' }],
  reportPeriod: [{ required: true, message: '报表期间不能为空', trigger: 'blur' }],
  reportDate: [{ required: true, message: '报表日期不能为空', trigger: 'blur' }],
  generateTime: [{ required: true, message: '生成时间不能为空', trigger: 'blur' }]
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
      formData.value = await ProductionReportApi.getProductionReport(id)
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
    const data = formData.value as unknown as ProductionReport
    if (formType.value === 'create') {
      await ProductionReportApi.createProductionReport(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionReportApi.updateProductionReport(data)
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
    reportNo: undefined,
    reportName: undefined,
    reportType: undefined,
    reportPeriod: undefined,
    workCenterId: undefined,
    productionOrders: undefined,
    totalPlanQuantity: undefined,
    totalCompletedQuantity: undefined,
    totalQualifiedQuantity: undefined,
    completionRate: undefined,
    qualityRate: undefined,
    totalWorkHours: undefined,
    totalEquipmentHours: undefined,
    oee: undefined,
    onTimeDeliveryRate: undefined,
    productionValue: undefined,
    averageCost: undefined,
    reportDate: undefined,
    generateTime: undefined,
    status: undefined,
    reportData: undefined
  }
  formRef.value?.resetFields()
}
</script>