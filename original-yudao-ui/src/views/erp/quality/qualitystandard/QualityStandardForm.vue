<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="标准编号" prop="standardNo">
        <el-input v-model="formData.standardNo" placeholder="请输入标准编号" />
      </el-form-item>
      <el-form-item label="标准名称" prop="standardName">
        <el-input v-model="formData.standardName" placeholder="请输入标准名称" />
      </el-form-item>
      <el-form-item label="产品分类" prop="categoryId">
        <el-tree-select
          v-model="selectedCategoryId"
          :data="productCategoryTree"
          :props="defaultProps"
          check-strictly
          clearable
          placeholder="请选择产品分类（不选则显示全部）"
          class="!w-1/1"
          @change="handleCategoryChange"
        />
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
            v-for="item in filteredProductList"
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
      <el-form-item label="检验类型" prop="inspectionType">
        <el-select v-model="formData.inspectionType" placeholder="请选择检验类型" clearable>
          <el-option label="进料检验" value="1" />
          <el-option label="过程检验" value="2" />
          <el-option label="成品检验" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="标准版本" prop="standardVersion">
        <el-input v-model="formData.standardVersion" placeholder="请输入标准版本" />
      </el-form-item>
      <el-form-item label="AQL水平" prop="aqlLevel">
        <el-input v-model="formData.aqlLevel" placeholder="请输入AQL水平" />
      </el-form-item>
      <el-form-item label="抽样方法" prop="samplingMethod">
        <el-input v-model="formData.samplingMethod" placeholder="请输入抽样方法" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCT_BOM_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="标准描述" prop="description">
        <Editor v-model="formData.description" height="150px" />
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
import { QualityStandardApi, QualityStandard } from '@/api/erp/qualitystandard'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import { defaultProps, handleTree } from '@/utils/tree'

/** ERP 质检标准 表单 */
defineOptions({ name: 'QualityStandardForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  standardNo: undefined,
  standardName: undefined,
  productId: undefined,
  processId: undefined,
  inspectionType: undefined,
  standardVersion: undefined,
  aqlLevel: undefined,
  samplingMethod: undefined,
  status: undefined,
  description: undefined
})
const formRules = reactive({
  standardNo: [{ required: true, message: '标准编号不能为空', trigger: 'blur' }],
  standardName: [{ required: true, message: '标准名称不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表

/** 根据分类过滤后的产品列表 */
const filteredProductList = computed(() => {
  if (!selectedCategoryId.value) {
    return productList.value // 未选择分类，显示全部产品
  }
  // 选择分类后，只显示该分类下的产品
  return productList.value.filter((product) => product.categoryId === selectedCategoryId.value)
})

/** 处理分类变更 */
const handleCategoryChange = (categoryId: number | undefined) => {
  // 当分类改变时，如果已选择的产品不在新分类下，清空产品选择
  if (categoryId !== undefined && formData.value.productId) {
    const product = productList.value.find((item) => item.id === formData.value.productId)
    if (product && product.categoryId !== categoryId) {
      formData.value.productId = undefined
    }
  }
}

/** 加载产品分类树 */
const loadProductCategoryTree = async () => {
  try {
    const data = await ProductCategoryApi.getProductCategoryList()
    const root: any = { id: 0, name: '顶级产品分类', children: [] }
    root.children = handleTree(data, 'id', 'parentId')
    productCategoryTree.value = [root]
  } catch (error) {
    console.error('加载产品分类失败:', error)
  }
}

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [products, processRouteItemData] = await Promise.all([
      ProductApi.getProductSimpleList(),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 })
    ])
    productList.value = products || []
    processRouteItemList.value = processRouteItemData.list || []
    // 加载产品分类树
    await loadProductCategoryTree()
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
      formData.value = await QualityStandardApi.getQualityStandard(id)
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
    const data = formData.value as unknown as QualityStandard
    if (formType.value === 'create') {
      await QualityStandardApi.createQualityStandard(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityStandardApi.updateQualityStandard(data)
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
    standardNo: undefined,
    standardName: undefined,
    productId: undefined,
    processId: undefined,
    inspectionType: undefined,
    standardVersion: undefined,
    aqlLevel: undefined,
    samplingMethod: undefined,
    status: undefined,
    description: undefined
  }
  selectedCategoryId.value = undefined // 重置分类选择
  formRef.value?.resetFields()
}
</script>