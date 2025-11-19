<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="KPI编号" prop="kpiNo">
        <el-input v-model="formData.kpiNo" placeholder="请输入KPI编号" />
      </el-form-item>
      <el-form-item label="KPI名称" prop="kpiName">
        <el-input v-model="formData.kpiName" placeholder="请输入KPI名称" />
      </el-form-item>
      <el-form-item label="KPI类型" prop="kpiType">
        <el-select v-model="formData.kpiType" placeholder="请选择KPI类型" clearable>
          <el-option label="OEE" value="1" />
          <el-option label="合格率" value="2" />
          <el-option label="达成率" value="3" />
          <el-option label="交期率" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类" prop="category">
        <el-input v-model="formData.category" placeholder="请输入分类" />
      </el-form-item>
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心" />
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
      <el-form-item label="统计周期" prop="measurementPeriod">
        <el-input v-model="formData.measurementPeriod" placeholder="请输入统计周期" />
      </el-form-item>
      <el-form-item label="目标值" prop="targetValue">
        <el-input v-model="formData.targetValue" placeholder="请输入目标值" />
      </el-form-item>
      <el-form-item label="实际值" prop="actualValue">
        <el-input v-model="formData.actualValue" placeholder="请输入实际值" />
      </el-form-item>
      <el-form-item label="差异值" prop="variance">
        <el-input v-model="formData.variance" placeholder="请输入差异值" />
      </el-form-item>
      <el-form-item label="差异率" prop="varianceRate">
        <el-input v-model="formData.varianceRate" placeholder="请输入差异率" />
      </el-form-item>
      <el-form-item label="计算日期" prop="calculationDate">
        <el-date-picker
          v-model="formData.calculationDate"
          type="date"
          value-format="x"
          placeholder="选择计算日期"
        />
      </el-form-item>
      <el-form-item label="周期开始时间" prop="periodStartTime">
        <el-date-picker
          v-model="formData.periodStartTime"
          type="date"
          value-format="x"
          placeholder="选择周期开始时间"
        />
      </el-form-item>
      <el-form-item label="周期结束时间" prop="periodEndTime">
        <el-date-picker
          v-model="formData.periodEndTime"
          type="date"
          value-format="x"
          placeholder="选择周期结束时间"
        />
      </el-form-item>
      <el-form-item label="数据来源" prop="dataSource">
        <el-input v-model="formData.dataSource" placeholder="请输入数据来源" />
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input v-model="formData.remarks" placeholder="请输入备注" />
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
import { ProductionKpiApi, ProductionKpi } from '@/api/erp/productionkpi'
import { ProductApi, ProductVO } from '@/api/erp/product/product'

/** ERP 生产KPI 表单 */
defineOptions({ name: 'ProductionKpiForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  kpiNo: undefined,
  kpiName: undefined,
  kpiType: undefined,
  category: undefined,
  workCenterId: undefined,
  productId: undefined,
  measurementPeriod: undefined,
  targetValue: undefined,
  actualValue: undefined,
  variance: undefined,
  varianceRate: undefined,
  calculationDate: undefined,
  periodStartTime: undefined,
  periodEndTime: undefined,
  dataSource: undefined,
  remarks: undefined
})
const formRules = reactive({
  kpiNo: [{ required: true, message: 'KPI编号不能为空', trigger: 'blur' }],
  kpiName: [{ required: true, message: 'KPI名称不能为空', trigger: 'blur' }],
  kpiType: [{ required: true, message: 'KPI类型不能为空', trigger: 'change' }],
  measurementPeriod: [{ required: true, message: '统计周期不能为空', trigger: 'blur' }],
  actualValue: [{ required: true, message: '实际值不能为空', trigger: 'blur' }],
  calculationDate: [{ required: true, message: '计算日期不能为空', trigger: 'blur' }],
  periodStartTime: [{ required: true, message: '周期开始时间不能为空', trigger: 'blur' }],
  periodEndTime: [{ required: true, message: '周期结束时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const products = await ProductApi.getProductSimpleList()
    productList.value = products || []
  } catch (error) {
    console.error('加载产品列表失败:', error)
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
      formData.value = await ProductionKpiApi.getProductionKpi(id)
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
    const data = formData.value as unknown as ProductionKpi
    if (formType.value === 'create') {
      await ProductionKpiApi.createProductionKpi(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductionKpiApi.updateProductionKpi(data)
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
    kpiNo: undefined,
    kpiName: undefined,
    kpiType: undefined,
    category: undefined,
    workCenterId: undefined,
    productId: undefined,
    measurementPeriod: undefined,
    targetValue: undefined,
    actualValue: undefined,
    variance: undefined,
    varianceRate: undefined,
    calculationDate: undefined,
    periodStartTime: undefined,
    periodEndTime: undefined,
    dataSource: undefined,
    remarks: undefined
  }
  formRef.value?.resetFields()
}
</script>