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
      <el-table-column label="调入仓库" min-width="125">
        <template #default="{ row, $index }">
          <el-form-item
            :prop="`${$index}.warehouseId`"
            :rules="formRules.warehouseId"
            class="mb-0px!"
          >
            <el-select
              v-model="row.warehouseId"
              clearable
              filterable
              placeholder="请选择调入仓库"
              style="width: 100%"
            >
              <el-option
                v-for="item in warehouseList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="产品名称" min-width="180">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productId`" :rules="formRules.productId" class="mb-0px!">
            <el-select
              v-model="row.productId"
              clearable
              filterable
              @change="onChangeProduct($event, row)"
              placeholder="请选择产品"
              style="width: 100%"
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
      <el-table-column label="数量" prop="count" fixed="right" min-width="140">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.count`" :rules="formRules.count" class="mb-0px!">
            <el-input-number
              v-model="row.count"
              controls-position="right"
              :min="0.001"
              :precision="3"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="产品单价" fixed="right" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productPrice`" class="mb-0px!">
            <el-input-number
              v-model="row.productPrice"
              controls-position="right"
              :min="0.01"
              :precision="2"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="合计金额" prop="totalPrice" fixed="right" min-width="100">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.totalPrice`" class="mb-0px!">
            <el-input disabled v-model="row.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="备注" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.remark`" class="mb-0px!">
            <el-input v-model="row.remark" placeholder="请输入备注" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3" v-if="!disabled">
    <el-button @click="handleAdd" round>+ 添加入库产品</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { defaultProps, handleTree } from '@/utils/tree'
import {
  erpCountInputFormatter,
  erpPriceInputFormatter,
  erpPriceMultiply,
  getSumValue
} from '@/utils'

const props = defineProps<{
  items: any[]
  disabled: boolean
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref<any[]>([])
const formRules = reactive({
  warehouseId: [{ required: true, message: '调入仓库不能为空', trigger: 'blur' }],
  productId: [{ required: true, message: '产品不能为空', trigger: 'blur' }],
  count: [{ required: true, message: '产品数量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表

/** 根据分类过滤后的产品列表 */
const filteredProductList = computed(() => {
  if (!selectedCategoryId.value) {
    return productList.value // 未选择分类，显示全部产品
  }
  // 选择分类后，只显示该分类下的产品
  return productList.value.filter((product) => product.categoryId === selectedCategoryId.value)
})

/** 初始化设置入库项 */
watch(
  () => props.items,
  async (val) => {
    formData.value = val || []
  },
  { immediate: true }
)

/** 监听产品变化，计算产品总价 */
watch(
  () => formData.value,
  (val) => {
    if (!val || val.length === 0) {
      return
    }
    // 循环处理
    val.forEach((item) => {
      item.totalPrice = erpPriceMultiply(item.productPrice, item.count)
    })
  },
  { deep: true }
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
    if (['count', 'totalPrice'].includes(column.property)) {
      const sum = getSumValue(data.map((item: any) => Number(item[column.property])))
      sums[index] =
        column.property === 'count' ? erpCountInputFormatter(sum) : erpPriceInputFormatter(sum)
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
    warehouseId: undefined,
    productId: undefined,
    productUnitName: undefined, // 产品单位
    productBarCode: undefined, // 产品条码
    productPrice: undefined,
    count: 1,
    totalPrice: undefined,
    remark: undefined
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
          row.productPrice = undefined
        }
      }
    })
  }
}

/** 处理产品变更 */
const onChangeProduct = (productId: number, row: any) => {
  const product = productList.value.find((item) => item.id === productId)
  if (product) {
    row.productUnitName = product.unitName
    row.productBarCode = product.barCode
    row.productPrice = product.minPrice
  }
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
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
  // 加载产品列表
  productList.value = await ProductApi.getProductSimpleList()
  // 加载产品分类树
  await loadProductCategoryTree()
  warehouseList.value = await WarehouseApi.getWarehouseSimpleList()
  // 默认添加一个
  if (formData.value.length === 0) {
    handleAdd()
  }
})
</script>

