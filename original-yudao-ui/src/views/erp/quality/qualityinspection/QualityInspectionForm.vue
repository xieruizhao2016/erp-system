<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="检验单号" prop="inspectionNo">
        <el-input v-model="formData.inspectionNo" disabled placeholder="保存时自动生成" />
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
        <el-input v-model="formData.batchNo" placeholder="请输入批次号" />
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select
          v-model="formData.productId"
          clearable
          filterable
          placeholder="请选择产品"
          class="!w-1/1"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
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
      <el-form-item label="检验类型" prop="inspectionType">
        <el-select v-model="formData.inspectionType" placeholder="请选择检验类型" clearable>
          <el-option label="进料检验" value="1" />
          <el-option label="过程检验" value="2" />
          <el-option label="成品检验" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="检验级别" prop="inspectionLevel">
        <el-select v-model="formData.inspectionLevel" placeholder="请选择检验级别" clearable>
          <el-option label="全检" value="1" />
          <el-option label="抽检" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="批量大小" prop="lotSize">
        <el-input v-model="formData.lotSize" placeholder="请输入批量大小" />
      </el-form-item>
      <el-form-item label="样本数量" prop="sampleSize">
        <el-input v-model="formData.sampleSize" placeholder="请输入样本数量" />
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
      <el-form-item label="检验结果" prop="inspectionResult">
        <el-select v-model="formData.inspectionResult" placeholder="请选择检验结果" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_QUALITY_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="检验员" prop="inspectorId">
        <el-select
          v-model="formData.inspectorId"
          clearable
          filterable
          placeholder="请选择检验员"
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
      <el-form-item label="检验时间" prop="inspectionTime">
        <el-date-picker
          v-model="formData.inspectionTime"
          type="date"
          value-format="x"
          placeholder="选择检验时间"
        />
      </el-form-item>
      <el-form-item label="检验环境" prop="environment">
        <el-input v-model="formData.environment" placeholder="请输入检验环境" />
      </el-form-item>
      <el-form-item label="检验设备" prop="equipment">
        <el-input v-model="formData.equipment" placeholder="请输入检验设备" />
      </el-form-item>
      <el-form-item label="检验备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入检验备注" />
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
import { QualityInspectionApi, QualityInspection } from '@/api/erp/qualityinspection'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 质检记录 表单 */
defineOptions({ name: 'QualityInspectionForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  inspectionNo: undefined,
  batchNo: undefined,
  productId: undefined,
  processId: undefined,
  workOrderId: undefined,
  inspectionType: undefined,
  inspectionLevel: undefined,
  lotSize: undefined,
  sampleSize: undefined,
  qualifiedQuantity: undefined,
  rejectedQuantity: undefined,
  scrapQuantity: undefined,
  inspectionResult: undefined,
  inspectorId: undefined,
  inspectionTime: undefined,
  environment: undefined,
  equipment: undefined,
  remark: undefined
})
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  inspectionTime: [{ required: true, message: '检验时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [products, processRouteItemData, workOrderData, users] = await Promise.all([
      ProductApi.getProductSimpleList(),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 }),
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    productList.value = products || []
    processRouteItemList.value = processRouteItemData.list || []
    workOrderList.value = workOrderData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (productList.value.length === 0) {
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
      formData.value = await QualityInspectionApi.getQualityInspection(id)
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
    const data = formData.value as unknown as QualityInspection
    if (formType.value === 'create') {
      await QualityInspectionApi.createQualityInspection(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityInspectionApi.updateQualityInspection(data)
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
    inspectionNo: undefined,
    batchNo: undefined,
    productId: undefined,
    processId: undefined,
    workOrderId: undefined,
    inspectionType: undefined,
    inspectionLevel: undefined,
    lotSize: undefined,
    sampleSize: undefined,
    qualifiedQuantity: undefined,
    rejectedQuantity: undefined,
    scrapQuantity: undefined,
    inspectionResult: undefined,
    inspectorId: undefined,
    inspectionTime: undefined,
    environment: undefined,
    equipment: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>