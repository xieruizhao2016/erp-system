<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
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
        <el-date-picker
          v-model="formData.standardCycleTime"
          type="date"
          value-format="x"
          placeholder="选择标准周期时间（分钟）"
        />
      </el-form-item>
      <el-form-item label="标准人工成本" prop="standardLaborCost">
        <el-input v-model="formData.standardLaborCost" placeholder="请输入标准人工成本" />
      </el-form-item>
      <el-form-item label="标准制造费用" prop="standardOverheadCost">
        <el-input v-model="formData.standardOverheadCost" placeholder="请输入标准制造费用" />
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
  status: undefined
})
const formRules = reactive({
  routeNo: [{ required: true, message: '工艺路线编号不能为空', trigger: 'blur' }],
  routeName: [{ required: true, message: '工艺路线名称不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }]
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
      formData.value = await ProcessRouteApi.getProcessRoute(id)
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
    const data = formData.value as unknown as ProcessRoute
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
    status: undefined
  }
  formRef.value?.resetFields()
}
</script>