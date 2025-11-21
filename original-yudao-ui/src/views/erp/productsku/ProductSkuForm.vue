<!-- ERP 产品SKU的新增/修改 -->
<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="70%">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="关联产品" prop="productId">
            <el-select
              v-model="formData.productId"
              clearable
              filterable
              placeholder="请选择产品（必填）"
              class="w-1/1"
            >
              <el-option
                v-for="product in productList"
                :key="product.id"
                :label="product.name"
                :value="product.id"
              >
                <span>{{ product.name }} ({{ product.code || product.barCode || '' }})</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="SKU编码" prop="skuCode">
            <el-input v-model="formData.skuCode" placeholder="请输入SKU编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="SKU名称" prop="skuName">
            <el-input v-model="formData.skuName" placeholder="请输入SKU名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="条码" prop="barCode">
            <el-input v-model="formData.barCode" placeholder="请输入条码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="分类" prop="categoryId">
            <el-tree-select
              v-model="formData.categoryId"
              :data="categoryList"
              :props="defaultProps"
              check-strictly
              default-expand-all
              placeholder="请选择分类"
              class="w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单位" prop="unitId">
            <el-select v-model="formData.unitId" clearable placeholder="请选择单位" class="w-1/1">
              <el-option
                v-for="unit in unitList"
                :key="unit.id"
                :label="unit.name"
                :value="unit.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规格" prop="standard">
            <el-input v-model="formData.standard" placeholder="请输入规格" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="颜色" prop="color">
            <el-input v-model="formData.color" placeholder="请输入颜色" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="尺寸" prop="size">
            <el-input v-model="formData.size" placeholder="请输入尺寸" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="材质" prop="material">
            <el-input v-model="formData.material" placeholder="请输入材质" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重量（kg）" prop="weight">
            <el-input-number
              v-model="formData.weight"
              placeholder="请输入重量（kg）"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="体积（m³）" prop="volume">
            <el-input-number
              v-model="formData.volume"
              placeholder="请输入体积"
              :min="0"
              :precision="4"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="成本价格" prop="costPrice">
            <el-input-number
              v-model="formData.costPrice"
              placeholder="请输入成本价格，单位：元"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="采购价格" prop="purchasePrice">
            <el-input-number
              v-model="formData.purchasePrice"
              placeholder="请输入采购价格，单位：元"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="销售价格" prop="salePrice">
            <el-input-number
              v-model="formData.salePrice"
              placeholder="请输入销售价格，单位：元"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="最低价格" prop="minPrice">
            <el-input-number
              v-model="formData.minPrice"
              placeholder="请输入最低价格，单位：元"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="formData.sort"
              placeholder="请输入排序"
              :min="0"
              :precision="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="SKU描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              placeholder="请输入SKU描述"
              :rows="3"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="formData.remark"
              type="textarea"
              placeholder="请输入备注"
              :rows="2"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { ProductSkuApi, ProductSkuVO } from '@/api/erp/productsku'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { ProductUnitApi, ProductUnitVO } from '@/api/erp/product/unit'
import { ProductApi } from '@/api/erp/product'
import { defaultProps, handleTree } from '@/utils/tree'

/** ERP 产品SKU表单 */
defineOptions({ name: 'ProductSkuForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  skuCode: '',
  skuName: '',
  description: '',
  status: 1,
  categoryId: undefined,
  barCode: '',
  standard: '',
  unitId: undefined,
  weight: undefined,
  volume: undefined,
  costPrice: undefined,
  purchasePrice: undefined,
  salePrice: undefined,
  minPrice: undefined,
  color: '',
  size: '',
  material: '',
  imageUrl: '',
  sort: 0,
  remark: ''
})
const formRules = reactive({
  skuCode: [{ required: true, message: 'SKU编码不能为空', trigger: 'blur' }],
  skuName: [{ required: true, message: 'SKU名称不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const categoryList = ref<ProductCategoryVO[]>([]) // 分类列表
const unitList = ref<ProductUnitVO[]>([]) // 单位列表
const productList = ref<any[]>([]) // 产品列表

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载分类、单位和产品列表
  if (categoryList.value.length === 0) {
    const categoryData = await ProductCategoryApi.getProductCategorySimpleList()
    categoryList.value = handleTree(categoryData, 'id', 'parentId')
  }
  if (unitList.value.length === 0) {
    unitList.value = await ProductUnitApi.getProductUnitSimpleList()
  }
  if (productList.value.length === 0) {
    try {
      const productData = await ProductApi.getProductSimpleList()
      productList.value = productData || []
    } catch (error) {
      console.error('加载产品列表失败:', error)
    }
  }

  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProductSkuApi.get(id)
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
    const data = formData.value as unknown as ProductSkuVO
    if (formType.value === 'create') {
      await ProductSkuApi.create(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductSkuApi.update(data)
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
    skuCode: '',
    skuName: '',
    description: '',
    status: 1,
    categoryId: undefined,
    barCode: '',
    standard: '',
    unitId: undefined,
    weight: undefined,
    volume: undefined,
    costPrice: undefined,
    purchasePrice: undefined,
    salePrice: undefined,
    minPrice: undefined,
    color: '',
    size: '',
    material: '',
    imageUrl: '',
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

