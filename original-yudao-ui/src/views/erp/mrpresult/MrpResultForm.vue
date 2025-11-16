<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="运算批次号" prop="runNo">
        <el-input v-model="formData.runNo" placeholder="请输入运算批次号" />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input v-model="formData.productId" placeholder="请输入产品ID" />
      </el-form-item>
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input v-model="formData.warehouseId" placeholder="请输入仓库ID" />
      </el-form-item>
      <el-form-item label="周期开始日期" prop="periodStartDate">
        <el-date-picker
          v-model="formData.periodStartDate"
          type="date"
          value-format="x"
          placeholder="选择周期开始日期"
        />
      </el-form-item>
      <el-form-item label="周期结束日期" prop="periodEndDate">
        <el-date-picker
          v-model="formData.periodEndDate"
          type="date"
          value-format="x"
          placeholder="选择周期结束日期"
        />
      </el-form-item>
      <el-form-item label="毛需求" prop="grossRequirement">
        <el-input v-model="formData.grossRequirement" placeholder="请输入毛需求" />
      </el-form-item>
      <el-form-item label="计划接收量" prop="scheduledReceipts">
        <el-input v-model="formData.scheduledReceipts" placeholder="请输入计划接收量" />
      </el-form-item>
      <el-form-item label="现有库存" prop="onHandInventory">
        <el-input v-model="formData.onHandInventory" placeholder="请输入现有库存" />
      </el-form-item>
      <el-form-item label="净需求" prop="netRequirement">
        <el-input v-model="formData.netRequirement" placeholder="请输入净需求" />
      </el-form-item>
      <el-form-item label="计划订单接收量" prop="plannedOrderReceipts">
        <el-input v-model="formData.plannedOrderReceipts" placeholder="请输入计划订单接收量" />
      </el-form-item>
      <el-form-item label="计划订单发放量" prop="plannedOrderReleases">
        <el-input v-model="formData.plannedOrderReleases" placeholder="请输入计划订单发放量" />
      </el-form-item>
      <el-form-item label="订单类型" prop="orderType">
        <el-select v-model="formData.orderType" placeholder="请选择订单类型" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_ORDER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="批量规则" prop="lotSizingRule">
        <el-select v-model="formData.lotSizingRule" placeholder="请选择批量规则" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_LOT_SIZING_RULE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="提前期（天）" prop="leadTime">
        <el-input-number v-model="formData.leadTime" placeholder="请输入提前期（天）" :min="0" class="!w-1/1" />
      </el-form-item>
      <el-form-item label="安全库存" prop="safetyStock">
        <el-input-number v-model="formData.safetyStock" placeholder="请输入安全库存" :min="0" class="!w-1/1" />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-radio-group v-model="formData.orderStatus">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_ORDER_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="需求日期" prop="dueDate">
        <el-date-picker
          v-model="formData.dueDate"
          type="date"
          value-format="x"
          placeholder="选择需求日期"
        />
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
import { MrpResultApi, MrpResult } from '@/api/erp/mrpresult'

/** ERP MRP运算结果 表单 */
defineOptions({ name: 'MrpResultForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  runNo: undefined,
  productId: undefined,
  warehouseId: undefined,
  periodStartDate: undefined,
  periodEndDate: undefined,
  grossRequirement: undefined,
  scheduledReceipts: undefined,
  onHandInventory: undefined,
  netRequirement: undefined,
  plannedOrderReceipts: undefined,
  plannedOrderReleases: undefined,
  orderType: undefined,
  lotSizingRule: undefined,
  leadTime: undefined,
  safetyStock: undefined,
  orderStatus: undefined,
  dueDate: undefined
})
const formRules = reactive({
  runNo: [{ required: true, message: '运算批次号不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品ID不能为空', trigger: 'blur' }],
  periodStartDate: [{ required: true, message: '周期开始日期不能为空', trigger: 'blur' }],
  periodEndDate: [{ required: true, message: '周期结束日期不能为空', trigger: 'blur' }]
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
      formData.value = await MrpResultApi.getMrpResult(id)
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
    const data = formData.value as unknown as MrpResult
    if (formType.value === 'create') {
      await MrpResultApi.createMrpResult(data)
      message.success(t('common.createSuccess'))
    } else {
      await MrpResultApi.updateMrpResult(data)
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
    runNo: undefined,
    productId: undefined,
    warehouseId: undefined,
    periodStartDate: undefined,
    periodEndDate: undefined,
    grossRequirement: undefined,
    scheduledReceipts: undefined,
    onHandInventory: undefined,
    netRequirement: undefined,
    plannedOrderReceipts: undefined,
    plannedOrderReleases: undefined,
    orderType: undefined,
    lotSizingRule: undefined,
    leadTime: undefined,
    safetyStock: undefined,
    orderStatus: undefined,
    dueDate: undefined
  }
  formRef.value?.resetFields()
}
</script>