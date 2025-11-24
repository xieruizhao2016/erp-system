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
      <el-table-column label="SKU" min-width="180">
        <template #default="{ row, $index }">
          <el-form-item 
            :prop="`${$index}.skuId`" 
            :rules="getSkuRules(row)" 
            class="mb-0px!"
            v-if="row.skuList && row.skuList.length > 1"
          >
            <el-select
              v-model="row.skuId"
              clearable
              filterable
              @change="onChangeSku($event, row)"
              placeholder="请选择SKU"
              :disabled="!row.productId || !row.skuList || row.skuList.length === 0"
            >
              <el-option
                v-for="sku in row.skuList"
                :key="sku.id"
                :label="`${sku.skuName}${sku.standard ? ' - ' + sku.standard : ''}`"
                :value="sku.id"
              />
            </el-select>
          </el-form-item>
          <span v-else-if="row.skuList && row.skuList.length === 1" class="text-gray-500">
            {{ row.skuList[0].skuName }}{{ row.skuList[0].standard ? ' - ' + row.skuList[0].standard : '' }}
          </span>
          <span v-else class="text-gray-400">-</span>
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
      <el-table-column label="金额" prop="totalProductPrice" fixed="right" min-width="100">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.totalProductPrice`" class="mb-0px!">
            <el-input
              disabled
              v-model="row.totalProductPrice"
              :formatter="erpPriceInputFormatter"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="税率（%）" fixed="right" min-width="115">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.taxPercent`" class="mb-0px!">
            <el-input-number
              v-model="row.taxPercent"
              controls-position="right"
              :min="0"
              :precision="2"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="税额" prop="taxPrice" fixed="right" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.taxPrice`" class="mb-0px!">
            <el-form-item :prop="`${$index}.taxPrice`" class="mb-0px!">
              <el-input disabled v-model="row.taxPrice" :formatter="erpPriceInputFormatter" />
            </el-form-item>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="税额合计" prop="totalPrice" fixed="right" min-width="100">
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
    <el-button @click="handleAdd" round>+ 添加销售产品</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { ProductSkuApi, ProductSkuVO } from '@/api/erp/productsku'
import { ProductUnitApi, ProductUnitVO } from '@/api/erp/product/unit'
import { StockApi } from '@/api/erp/stock/stock'
import { defaultProps, handleTree } from '@/utils/tree'
import {
  erpCountInputFormatter,
  erpPriceInputFormatter,
  erpPriceMultiply,
  getSumValue
} from '@/utils'

const props = defineProps<{
  items: undefined
  disabled: false
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
  productId: [{ required: true, message: '产品不能为空', trigger: 'blur' }],
  count: [{ required: true, message: '产品数量不能为空', trigger: 'blur' }]
})
const formRef = ref([]) // 表单 Ref
const productList = ref<ProductVO[]>([]) // 产品列表（全部）
const selectedCategoryId = ref<number | undefined>(undefined) // 选中的分类ID
const productCategoryTree = ref<any[]>([]) // 产品分类树
const productUnitList = ref<ProductUnitVO[]>([]) // 产品单位列表

/** 获取SKU验证规则 */
const getSkuRules = (row: any) => {
  if (row.skuList && row.skuList.length > 1) {
    return [{ required: true, message: '请选择SKU', trigger: 'change' }]
  }
  return []
}

/** 根据分类过滤后的产品列表 */
const filteredProductList = computed(() => {
  if (!selectedCategoryId.value) {
    return productList.value // 未选择分类，显示全部产品
  }
  // 选择分类后，只显示该分类下的产品
  return productList.value.filter((product) => product.categoryId === selectedCategoryId.value)
})

/** 初始化设置出库项 */
watch(
  () => props.items,
  async (val) => {
    if (val && val.length > 0) {
      // 初始化时，为每行加载SKU列表（如果有productId）
      formData.value = val.map((item: any) => ({
        ...item,
        skuId: item.skuId || undefined, // 确保skuId存在
        skuList: item.skuList || [] // 保留已有的skuList
      }))
      
      // 异步加载SKU列表
      for (const row of formData.value) {
        if (row.productId && row.productId > 0) {
          try {
            // 尝试从产品列表缓存中获取SKU（如果产品列表已加载）
            const product = productList.value.find((item) => item.id === row.productId)
            if (product && product.skuList && product.skuList.length > 0) {
              // 使用产品列表中的SKU（只包含启用的）
              row.skuList = product.skuList
            } else {
              // 如果缓存中没有，尝试调用API（可能会失败，但会静默处理）
              try {
                const skuList = await ProductSkuApi.getListByProductId(row.productId)
                row.skuList = skuList || []
              } catch (error) {
                // API调用失败时，静默处理
                console.warn(`加载产品 ${row.productId} 的SKU列表失败，已跳过:`, error)
                row.skuList = []
              }
            }
            
            // 处理SKU选择逻辑
            if (row.skuList.length > 1) {
              // 如果有多个SKU，使用已有的skuId（如果存在）
              if (row.skuId) {
                const sku = row.skuList.find((s: any) => s.id === row.skuId)
                if (!sku) {
                  // 如果skuId不在列表中，清空它，让用户重新选择
                  row.skuId = undefined
                } else {
                  // 如果skuId存在，根据SKU更新相关信息
                  await updateRowBySku(row, sku)
                }
              }
            } else if (row.skuList.length === 1) {
              // 如果只有一个SKU，自动选择
              row.skuId = row.skuList[0].id
              await updateRowBySku(row, row.skuList[0])
            } else {
              // 如果没有SKU，使用产品信息
              row.skuId = undefined
              const product = productList.value.find((item) => item.id === row.productId)
              if (product) {
                row.productUnitName = row.productUnitName || product.unitName
                row.productBarCode = row.productBarCode || product.barCode
                row.productPrice = row.productPrice || product.salePrice
              }
            }
            
            // 加载库存
            await setStockCount(row)
          } catch (error) {
            console.warn('加载产品SKU列表失败:', error)
            row.skuList = []
          }
        } else {
          // 如果没有productId，清空SKU相关数据
          row.skuId = undefined
          row.skuList = []
        }
      }
    } else {
      formData.value = val || []
    }
  },
  { immediate: true }
)

/** 根据SKU更新行数据 */
const updateRowBySku = async (row: any, sku: ProductSkuVO) => {
  if (sku) {
    // 使用SKU的信息，但保留已有的价格（编辑时可能已修改）
    row.productBarCode = row.productBarCode || sku.barCode || ''
    row.productPrice = row.productPrice || sku.salePrice
    // 获取单位名称
    if (sku.unitId && sku.unitId > 0) {
      const unit = productUnitList.value.find((item) => item.id === sku.unitId)
      if (unit) {
        row.productUnitName = unit.name
        row.productUnitId = unit.id
      } else {
        // 如果缓存中没有，尝试从API获取
        try {
          const unitData = await ProductUnitApi.getProductUnit(sku.unitId)
          if (unitData) {
            row.productUnitName = unitData.name
            row.productUnitId = unitData.id
            // 添加到缓存
            if (!productUnitList.value.find((item) => item.id === unitData.id)) {
              productUnitList.value.push(unitData)
            }
          }
        } catch (error) {
          console.error('获取单位信息失败:', error)
          // 如果获取失败，尝试使用产品的单位
          const product = productList.value.find((item) => item.id === row.productId)
          if (product && product.unitName) {
            row.productUnitName = product.unitName
            row.productUnitId = product.unitId
          }
        }
      }
    } else {
      // 如果SKU没有单位，使用产品的单位
      const product = productList.value.find((item) => item.id === row.productId)
      if (product && product.unitName) {
        row.productUnitName = product.unitName
        row.productUnitId = product.unitId
      }
    }
  }
}

/** 监听合同产品变化，计算合同产品总价 */
watch(
  () => formData.value,
  (val) => {
    if (!val || val.length === 0) {
      return
    }
    // 循环处理
    val.forEach((item) => {
      item.totalProductPrice = erpPriceMultiply(item.productPrice, item.count)
      item.taxPrice = erpPriceMultiply(item.totalProductPrice, item.taxPercent / 100.0)
      if (item.totalProductPrice != null) {
        item.totalPrice = item.totalProductPrice + (item.taxPrice || 0)
      } else {
        item.totalPrice = undefined
      }
    })
  },
  { deep: true }
)

/** 合计 */
const getSummaries = (param: SummaryMethodProps) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    if (['count', 'totalProductPrice', 'taxPrice', 'totalPrice'].includes(column.property)) {
      const sum = getSumValue(data.map((item) => Number(item[column.property])))
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
    productId: undefined,
    skuId: undefined, // SKU编号
    skuList: [], // SKU列表
    productUnitName: undefined, // 产品单位
    productBarCode: undefined, // 产品条码
    productPrice: undefined,
    stockCount: undefined,
    count: 1,
    totalProductPrice: undefined,
    taxPercent: undefined,
    taxPrice: undefined,
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
          row.skuId = undefined
          row.skuList = []
          row.productUnitName = undefined
          row.productBarCode = undefined
          row.productPrice = undefined
          row.stockCount = undefined
        }
      }
    })
  }
}

/** 处理产品变更 */
const onChangeProduct = async (productId, row) => {
  // 清空SKU相关数据
  row.skuId = undefined
  row.skuList = []
  
  if (!productId) {
    // 清空产品相关数据
    row.productUnitName = undefined
    row.productBarCode = undefined
    row.productPrice = undefined
    row.stockCount = undefined
    return
  }

  const product = productList.value.find((item) => item.id === productId)
  if (product) {
    // 先设置产品基本信息（如果没有SKU，使用产品信息）
    row.productUnitName = product.unitName
    row.productBarCode = product.barCode
    row.productPrice = product.salePrice
  }

  // 只有在编辑模式下才加载SKU列表（新增时不需要）
  // 判断是否为编辑模式：如果 items 有数据且包含 id 字段，说明是编辑模式
  const isEditMode = props.items && props.items.length > 0 && props.items.some((item: any) => item.id != null)
  if (isEditMode) {
    // 加载该产品的SKU列表
    try {
      if (productId && productId > 0) {
        const skuList = await ProductSkuApi.getListByProductId(productId)
        row.skuList = skuList || []
      } else {
        row.skuList = []
      }
      
      // 如果产品有多个SKU，需要用户选择SKU
      if (row.skuList.length > 1) {
        // 清空当前信息，等待用户选择SKU
        row.skuId = undefined
        row.productUnitName = undefined
        row.productBarCode = undefined
        row.productPrice = undefined
      } else if (row.skuList.length === 1) {
        // 如果只有一个SKU，自动选择
        row.skuId = row.skuList[0].id
        await onChangeSku(row.skuId, row)
      } else {
        // 如果没有SKU，使用产品信息
        row.skuId = undefined
      }
    } catch (error) {
      console.error('加载产品SKU列表失败:', error)
      row.skuList = []
    }
  } else {
    // 新增模式：不加载SKU，直接使用产品信息
    row.skuId = undefined
    row.skuList = []
  }

  // 加载库存
  await setStockCount(row)
}

/** 处理SKU变更 */
const onChangeSku = async (skuId, row) => {
  if (!skuId) {
    // 如果没有选择SKU，使用产品信息
    const product = productList.value.find((item) => item.id === row.productId)
    if (product) {
      row.productUnitName = product.unitName
      row.productBarCode = product.barCode
      row.productPrice = product.salePrice
    }
    await setStockCount(row)
    return
  }

  // 根据选择的SKU填充信息
  const sku = row.skuList?.find((item) => item.id === skuId)
  if (sku) {
    // 使用SKU的信息
    row.productBarCode = sku.barCode || ''
    row.productPrice = sku.salePrice
    // 获取单位名称
    if (sku.unitId && sku.unitId > 0) {
      const unit = productUnitList.value.find((item) => item.id === sku.unitId)
      if (unit) {
        row.productUnitName = unit.name
        row.productUnitId = unit.id
      } else {
        // 如果缓存中没有，尝试从API获取
        try {
          const unitData = await ProductUnitApi.getProductUnit(sku.unitId)
          if (unitData) {
            row.productUnitName = unitData.name
            row.productUnitId = unitData.id
            // 添加到缓存
            if (!productUnitList.value.find((item) => item.id === unitData.id)) {
              productUnitList.value.push(unitData)
            }
          }
        } catch (error) {
          console.error('获取单位信息失败:', error)
          // 如果获取失败，尝试使用产品的单位
          const product = productList.value.find((item) => item.id === row.productId)
          if (product && product.unitName) {
            row.productUnitName = product.unitName
            row.productUnitId = product.unitId
          }
        }
      }
    } else {
      // 如果SKU没有单位，使用产品的单位
      const product = productList.value.find((item) => item.id === row.productId)
      if (product) {
        row.productUnitName = product.unitName
        row.productUnitId = product.unitId
      }
    }
  }

  // 加载库存（目前库存API只支持按productId查询，如果后续支持按skuId查询，可以修改这里）
  await setStockCount(row)
}

/** 加载库存 */
const setStockCount = async (row: any) => {
  if (!row.productId) {
    row.stockCount = 0
    return
  }
  try {
    // 目前库存API只支持按productId查询
    // 如果后续支持按skuId查询，可以优先使用skuId
    const count = await StockApi.getStockCount(row.productId)
    row.stockCount = count || 0
  } catch (error) {
    console.error('加载库存失败:', error)
    row.stockCount = 0
  }
}

/** 获取表单数据 */
const getItems = () => {
  return formData.value.map((item: any) => {
    // 只返回后端需要的字段，过滤掉前端使用的临时字段
    const result: any = {
      id: item.id,
      productId: item.productId,
      skuId: item.skuId,
      productUnitId: item.productUnitId,
      productPrice: item.productPrice,
      count: item.count,
      taxPercent: item.taxPercent,
      taxPrice: item.taxPrice,
      remark: item.remark
    }
    // 移除undefined字段
    Object.keys(result).forEach(key => {
      if (result[key] === undefined) {
        delete result[key]
      }
    })
    return result
  })
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}
defineExpose({ validate, getItems })

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
    // 加载产品单位列表
    try {
      productUnitList.value = await ProductUnitApi.getProductUnitSimpleList()
    } catch (error) {
      console.error('加载产品单位列表失败:', error)
    }
    // 默认添加一个
    if (formData.value.length === 0) {
      handleAdd()
    }
  } catch (error) {
    console.error('初始化失败:', error)
  }
})
</script>
