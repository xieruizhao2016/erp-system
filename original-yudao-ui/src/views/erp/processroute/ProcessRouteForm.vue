<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="工艺路线编号" prop="routeNo">
        <el-input v-model="formData.routeNo" placeholder="请输入工艺路线编号" />
      </el-form-item>
      <el-form-item label="工艺路线名称" prop="routeName">
        <el-input v-model="formData.routeName" placeholder="请输入工艺路线名称" />
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
      <el-form-item label="版本号" prop="version">
        <el-input v-model="formData.version" placeholder="请输入版本号" />
      </el-form-item>
      <el-form-item label="标准周期时间" prop="standardCycleTime">
        <el-input-number
          v-model="calculatedMetrics.standardCycleTime"
          :min="0"
          :precision="0"
          placeholder="根据工序明细自动计算（分钟）"
          style="width: 100%"
          readonly
          disabled
        />
        <div class="text-xs text-gray-500 mt-1">根据工序明细的标准工时和准备时间自动计算</div>
      </el-form-item>
      <el-form-item label="标准人工成本" prop="standardLaborCost">
        <el-input-number
          v-model="calculatedMetrics.standardLaborCost"
          :min="0"
          :precision="2"
          placeholder="根据工序明细自动计算（元）"
          style="width: 100%"
          readonly
          disabled
        />
        <div class="text-xs text-gray-500 mt-1">根据工序明细的标准工时和人工费率自动计算</div>
      </el-form-item>
      <el-form-item label="标准制造费用" prop="standardOverheadCost">
        <el-input-number
          v-model="calculatedMetrics.standardOverheadCost"
          :min="0"
          :precision="2"
          placeholder="根据工序明细自动计算（元）"
          style="width: 100%"
          readonly
          disabled
        />
        <div class="text-xs text-gray-500 mt-1">根据工序明细的标准工时和制造费率自动计算</div>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PROCESS_ROUTE_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- 明细列表 -->
      <ContentWrap>
        <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
          <el-tab-pane label="工序明细" name="item">
            <ProcessRouteItemForm ref="itemFormRef" :items="formData.items" @change="handleItemsChange" />
          </el-tab-pane>
        </el-tabs>
      </ContentWrap>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProcessRouteApi, ProcessRoute } from '@/api/erp/processroute'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import ProcessRouteItemForm from './components/ProcessRouteItemForm.vue'

/** ERP 工艺路线主 表单 */
defineOptions({ name: 'ProcessRouteForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  routeNo: undefined,
  routeName: undefined,
  productId: undefined,
  version: undefined,
  standardCycleTime: undefined,
  standardLaborCost: undefined,
  standardOverheadCost: undefined,
  status: undefined,
  items: [] as any[] // 工艺路线明细列表
})
const formRules = reactive({
  routeNo: [{ required: true, message: '工艺路线编号不能为空', trigger: 'blur' }],
  routeName: [{ required: true, message: '工艺路线名称不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const itemFormRef = ref() // 明细表单 Ref
const subTabsName = ref('item') // 子表Tab名称
const productList = ref<ProductVO[]>([]) // 产品列表

// 计算后的指标（只读显示）
const calculatedMetrics = ref({
  standardCycleTime: 0,
  standardLaborCost: 0,
  standardOverheadCost: 0
})

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const products = await ProductApi.getProductSimpleList()
    productList.value = products || []
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
}

/** 计算工艺路线指标（根据工序明细） */
const calculateMetrics = () => {
  const items = itemFormRef.value?.formData || formData.value.items || []
  
  let totalCycleTime = 0
  let totalLaborCost = 0
  let totalOverheadCost = 0

  items.forEach((item: any) => {
    // 标准周期时间 = 标准工时 + 准备时间
    if (item.standardTime) {
      totalCycleTime += item.standardTime
    }
    if (item.setupTime) {
      totalCycleTime += item.setupTime
    }

    // 标准人工成本 = 标准工时（分钟） × 人工费率（元/小时） / 60
    if (item.standardTime && item.laborRate) {
      const hours = item.standardTime / 60
      totalLaborCost += hours * item.laborRate
    }

    // 标准制造费用 = 标准工时（分钟） × 制造费率（元/小时） / 60
    if (item.standardTime && item.overheadRate) {
      const hours = item.standardTime / 60
      totalOverheadCost += hours * item.overheadRate
    }
  })

  calculatedMetrics.value = {
    standardCycleTime: totalCycleTime,
    standardLaborCost: Number(totalLaborCost.toFixed(2)),
    standardOverheadCost: Number(totalOverheadCost.toFixed(2))
  }

  // 同步到formData，用于提交
  formData.value.standardCycleTime = calculatedMetrics.value.standardCycleTime
  formData.value.standardLaborCost = calculatedMetrics.value.standardLaborCost
  formData.value.standardOverheadCost = calculatedMetrics.value.standardOverheadCost
}

/** 处理工序明细变化 */
const handleItemsChange = () => {
  // 延迟计算，确保数据已更新
  nextTick(() => {
    calculateMetrics()
  })
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
  // 新增时，自动生成工艺路线编号和默认版本号
  if (type === 'create') {
    // 默认版本号为 1.0.0
    formData.value.version = '1.0.0'
    // 默认状态为生效(2)
    formData.value.status = 2
    // 尝试生成工艺路线编号，如果失败则留空让用户手动输入
    try {
      const routeNo = await ProcessRouteApi.generateRouteNo()
      if (routeNo) {
        formData.value.routeNo = routeNo
      }
    } catch (error) {
      console.error('生成工艺路线编号失败，请手动输入:', error)
      // 接口调用失败时，不设置routeNo，让用户手动输入
    }
  }
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await ProcessRouteApi.getProcessRoute(id)
      formData.value = {
        ...data,
        items: data.items || []
      }
      // 计算指标
      nextTick(() => {
        calculateMetrics()
      })
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验主表单
  await formRef.value.validate()
  // 校验明细表单
  if (itemFormRef.value) {
    await itemFormRef.value.validate()
  }
  // 获取明细数据
  const items = itemFormRef.value?.formData || []
  // 重新计算指标（确保是最新的）
  calculateMetrics()
  // 提交请求
  formLoading.value = true
  try {
    const data = {
      ...formData.value,
      items: items,
      // 使用计算后的指标
      standardCycleTime: calculatedMetrics.value.standardCycleTime,
      standardLaborCost: calculatedMetrics.value.standardLaborCost,
      standardOverheadCost: calculatedMetrics.value.standardOverheadCost
    } as unknown as ProcessRoute
    if (formType.value === 'create') {
      await ProcessRouteApi.createProcessRoute(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProcessRouteApi.updateProcessRoute(data)
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
    routeNo: undefined,
    routeName: undefined,
    productId: undefined,
    version: undefined,
    standardCycleTime: undefined,
    standardLaborCost: undefined,
    standardOverheadCost: undefined,
    status: undefined,
    items: []
  }
  formRef.value?.resetFields()
}
</script>