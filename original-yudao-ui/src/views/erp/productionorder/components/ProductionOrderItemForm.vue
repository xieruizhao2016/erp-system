<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
    :disabled="disabled"
  >
    <!-- 产品分类选择器 -->
    <el-row class="mb-3" v-if="!disabled">
      <el-col :span="8">
        <el-form-item label="产品分类" label-width="80px">
          <el-tree-select
            v-model="selectedCategoryId"
            :data="productCategoryTree"
            :props="defaultProps"
            check-strictly
            clearable
            placeholder="请选择产品分类（不选则显示全部）"
            class="w-1/1"
            @change="handleCategoryChange"
          />
        </el-form-item>
      </el-col>
    </el-row>
    <el-table :data="formData" show-summary :summary-method="getSummaries" class="-mt-10px">
      <el-table-column label="序号" type="index" align="center" width="60" />
      <el-table-column label="产品名称" min-width="180">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productId`" :rules="formRules.productId" class="mb-0px!">
            <el-select
              v-model="row.productId"
              clearable
              filterable
              @change="onChangeProduct($event, row)"
              placeholder="请选择产品"
              :disabled="disabled"
            >
              <el-option
                v-for="item in filteredProductList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="库存" min-width="100">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.stockCount" :formatter="erpCountInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="条码" min-width="150">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productBarCode" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="单位" min-width="80">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productUnitName" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="生产数量" prop="quantity" fixed="right" min-width="140">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.quantity`" :rules="formRules.quantity" class="mb-0px!">
            <el-input-number
              v-model="row.quantity"
              controls-position="right"
              :min="0.001"
              :precision="3"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.remark`" class="mb-0px!">
            <el-input v-model="row.remark" placeholder="请输入备注" :disabled="disabled" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="60" v-if="!disabled">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3" v-if="!disabled">
    <el-button @click="handleAdd" round>+ 添加生产产品</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { StockApi } from '@/api/erp/stock/stock'
import { defaultProps, handleTree } from '@/utils/tree'
import { erpCountInputFormatter, getSumValue } from '@/utils'

const props = defineProps<{
  items?: any[]
  disabled?: boolean
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref<any[]>([])
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'blur' }],
  quantity: [{ required: true, message: '生产数量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树

/** 根据分类过滤后的产品列表 */
const filteredProductList = computed(() => {
  if (!selectedCategoryId.value) {
    return productList.value // 未选择分类，显示全部产品
  }
  // 选择分类后，只显示该分类下的产品
  return productList.value.filter((product) => product.categoryId === selectedCategoryId.value)
})

/** 初始化设置产品项 */
watch(
  () => props.items,
  async (val) => {
    if (val) {
      formData.value = val
    } else {
      formData.value = []
    }
  },
  { immediate: true }
)

/** 合计 */
const getSummaries = (param: any) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column: any, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (column.property === 'quantity') {
      const sum = getSumValue(data.map((item: any) => Number(item.quantity || 0)))
      sums[index] = erpCountInputFormatter(sum)
    } else {
      sums[index] = ''
    }
  })

  return sums
}

/** 新增按钮操作 */
const handleAdd = () => {
  const row = {
    id: undefined,
    productId: undefined,
    productUnitName: undefined, // 产品单位
    productBarCode: undefined, // 产品条码
    stockCount: undefined, // 库存数量
    quantity: 1, // 生产数量
    remark: undefined // 备注
  }
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index: number) => {
  formData.value.splice(index, 1)
}

/** 处理分类变更 */
const handleCategoryChange = (categoryId: number | undefined) => {
  // 当分类改变时，清空不在新分类下的产品选择
  if (categoryId !== undefined) {
    formData.value.forEach((row) => {
      if (row.productId) {
        const product = productList.value.find((item) => item.id === row.productId)
        if (product && product.categoryId !== categoryId) {
          // 如果已选择的产品不在新分类下，清空产品选择
          row.productId = undefined
          row.productUnitName = undefined
          row.productBarCode = undefined
          row.stockCount = undefined
        }
      }
    })
  }
}

/** 处理产品变更 */
const onChangeProduct = (productId: number | undefined, row: any) => {
  if (!productId) {
    row.productUnitName = undefined
    row.productBarCode = undefined
    row.stockCount = undefined
    return
  }
  const product = productList.value.find((item) => item.id === productId)
  if (product) {
    row.productUnitName = product.unitName
    row.productBarCode = product.barCode
  }
  // 加载库存
  setStockCount(row)
}

/** 加载库存 */
const setStockCount = async (row: any) => {
  if (!row.productId) {
    return
  }
  try {
    const count = await StockApi.getStockCount(row.productId)
    row.stockCount = count || 0
  } catch (error) {
    console.error('加载库存失败:', error)
    row.stockCount = 0
  }
}

/** 表单校验 */
const validate = () => {
  return formRef.value?.validate()
}
defineExpose({ validate })

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

/** 初始化 */
onMounted(async () => {
  try {
    // 加载产品列表
    productList.value = await ProductApi.getProductSimpleList()
    // 加载产品分类树
    await loadProductCategoryTree()
    // 默认添加一个（如果没有数据且不是禁用状态）
    if (formData.value.length === 0 && !props.disabled) {
      handleAdd()
    }
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
})
</script>

