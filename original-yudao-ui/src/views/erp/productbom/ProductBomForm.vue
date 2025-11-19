<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
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
      <el-form-item label="BOM编号" prop="bomNo">
        <el-input v-model="formData.bomNo" placeholder="请输入BOM编号" />
      </el-form-item>
      <el-form-item label="BOM名称" prop="bomName">
        <el-input v-model="formData.bomName" placeholder="请输入BOM名称" />
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input v-model="formData.version" placeholder="请输入版本号" />
      </el-form-item>
      <el-form-item label="生效日期" prop="effectiveDate">
        <el-date-picker
          v-model="formData.effectiveDate"
          type="date"
          value-format="x"
          placeholder="选择生效日期"
        />
      </el-form-item>
      <el-form-item label="失效日期" prop="expireDate">
        <el-date-picker
          v-model="formData.expireDate"
          type="date"
          value-format="x"
          placeholder="选择失效日期"
        />
      </el-form-item>
      <el-form-item label="BOM类型" prop="bomType">
        <el-select v-model="formData.bomType" placeholder="请选择BOM类型" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_BOM_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="标准成本" prop="standardCost">
        <el-input v-model="formData.standardCost" placeholder="请输入标准成本" />
      </el-form-item>
      <el-form-item label="总材料成本" prop="totalMaterialCost">
        <el-input v-model="formData.totalMaterialCost" placeholder="请输入总材料成本" />
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
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductBomApi, ProductBom } from '@/api/erp/productbom'
import { ProductApi, ProductVO } from '@/api/erp/product/product'

/** ERP BOM主 表单 */
defineOptions({ name: 'ProductBomForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  productId: undefined,
  bomNo: undefined,
  bomName: undefined,
  version: undefined,
  effectiveDate: undefined,
  expireDate: undefined,
  bomType: undefined,
  standardCost: undefined,
  totalMaterialCost: undefined,
  status: undefined
})
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'change' }],
  bomNo: [{ required: true, message: 'BOM编号不能为空', trigger: 'blur' }],
  bomName: [{ required: true, message: 'BOM名称不能为空', trigger: 'blur' }]
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
      formData.value = await ProductBomApi.getProductBom(id)
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
    const data = formData.value as unknown as ProductBom
    if (formType.value === 'create') {
      await ProductBomApi.createProductBom(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductBomApi.updateProductBom(data)
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
    productId: undefined,
    bomNo: undefined,
    bomName: undefined,
    version: undefined,
    effectiveDate: undefined,
    expireDate: undefined,
    bomType: undefined,
    standardCost: undefined,
    totalMaterialCost: undefined,
    status: undefined
  }
  formRef.value?.resetFields()
}
</script>