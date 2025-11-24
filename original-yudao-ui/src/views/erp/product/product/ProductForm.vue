<!-- ERP 产品的新增/修改 -->
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
          <el-form-item label="名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="条码" prop="barCode">
            <el-input v-model="formData.barCode" placeholder="请输入条码" />
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
          <el-form-item label="产品包装" prop="packageId">
            <el-select
              v-model="formData.packageId"
              clearable
              filterable
              placeholder="请选择产品包装（可选）"
              class="w-1/1"
            >
              <el-option
                v-for="pkg in packageList"
                :key="pkg.id"
                :label="pkg.packageName"
                :value="pkg.id"
              >
                <span>{{ pkg.packageCode }} - {{ pkg.packageName }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="产品OEM" prop="oemId">
            <el-select
              v-model="formData.oemId"
              clearable
              filterable
              placeholder="请选择产品OEM（可选）"
              class="w-1/1"
            >
              <el-option
                v-for="oem in oemList"
                :key="oem.id"
                :label="oem.oemName"
                :value="oem.id"
              >
                <span>{{ oem.oemCode }} - {{ oem.oemName }}</span>
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio
                v-for="dict in getIntDictOptions(DICT_TYPE.COMMON_STATUS)"
                :key="dict.value"
                :value="dict.value"
              >
                {{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="规格" prop="standard">
            <el-input v-model="formData.standard" placeholder="请输入规格" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保质期天数" prop="expiryDay">
            <el-input-number
              v-model="formData.expiryDay"
              placeholder="请输入保质期天数"
              :min="0"
              :precision="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="重量（kg）" prop="weight">
            <el-input-number
              v-model="formData.weight"
              placeholder="请输入重量（kg）"
              :min="0"
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
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="formData.remark" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
      
      <!-- SKU管理区域 -->
      <el-divider content-position="left">SKU管理</el-divider>
      <el-table :data="skuList" border style="width: 100%">
        <el-table-column label="SKU编码" prop="skuCode" min-width="120" />
        <el-table-column label="SKU名称" prop="skuName" min-width="120" />
        <el-table-column label="条码" prop="barCode" min-width="120" />
        <el-table-column label="规格" prop="standard" min-width="100" />
        <el-table-column label="单位" min-width="100">
          <template #default="{ row }">
            {{ unitList.find(u => u.id === row.unitId)?.name || '-' }}
          </template>
        </el-table-column>
        <el-table-column label="销售价格" min-width="120">
          <template #default="{ row }">
            {{ row.salePrice ? `¥${row.salePrice}` : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="100">
          <template #default="{ row }">
            <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ $index }">
            <el-button
              link
              type="primary"
              size="small"
              @click="handleEditSku($index)"
            >
              编辑
            </el-button>
            <el-button
              link
              type="danger"
              size="small"
              @click="handleDeleteSku($index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="mt-2">
        <el-button type="primary" size="small" @click="handleAddSku">
          <Icon icon="ep:plus" class="mr-5px" /> 添加SKU
        </el-button>
      </div>
      
      <!-- SKU选择器弹窗 -->
      <Dialog title="选择SKU" v-model="skuDialogVisible" width="900">
        <el-form
          class="-mb-15px"
          :model="skuQueryParams"
          :inline="true"
          label-width="80px"
        >
          <el-form-item label="SKU编码">
            <el-input
              v-model="skuQueryParams.skuCode"
              placeholder="请输入SKU编码"
              clearable
              @keyup.enter="handleSkuQuery"
              class="!w-200px"
            />
          </el-form-item>
          <el-form-item label="SKU名称">
            <el-input
              v-model="skuQueryParams.skuName"
              placeholder="请输入SKU名称"
              clearable
              @keyup.enter="handleSkuQuery"
              class="!w-200px"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleSkuQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
            <el-button @click="handleSkuReset"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          v-loading="skuLoading"
          :data="skuSelectList"
          border
          @selection-change="handleSkuSelectionChange"
          style="width: 100%"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column label="SKU编码" prop="skuCode" min-width="120" />
          <el-table-column label="SKU名称" prop="skuName" min-width="150" />
          <el-table-column label="条码" prop="barCode" min-width="120" />
          <el-table-column label="规格" prop="standard" min-width="100" />
          <el-table-column label="单位" min-width="100">
            <template #default="{ row }">
              {{ unitList.find(u => u.id === row.unitId)?.name || '-' }}
            </template>
          </el-table-column>
          <el-table-column label="销售价格" min-width="100">
            <template #default="{ row }">
              {{ row.salePrice ? `¥${row.salePrice}` : '-' }}
            </template>
          </el-table-column>
          <el-table-column label="状态" min-width="80">
            <template #default="{ row }">
              <dict-tag :type="DICT_TYPE.COMMON_STATUS" :value="row.status" />
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <Pagination
          :total="skuTotal"
          v-model:page="skuQueryParams.pageNo"
          v-model:limit="skuQueryParams.pageSize"
          @pagination="loadSkuList"
          class="mt-3"
        />
        <template #footer>
          <el-button @click="handleConfirmSkuSelection" type="primary" :disabled="selectedSkuList.length === 0">
            确 定 (已选择 {{ selectedSkuList.length }} 个)
          </el-button>
          <el-button @click="skuDialogVisible = false">取 消</el-button>
        </template>
      </Dialog>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductCategoryApi, ProductCategoryVO } from '@/api/erp/product/category'
import { ProductUnitApi, ProductUnitVO } from '@/api/erp/product/unit'
import { ProductPackageApi, ProductPackageVO } from '@/api/erp/productpackage'
import { ProductOemApi, ProductOemVO } from '@/api/erp/productoem'
import { ProductSkuApi, ProductSkuVO } from '@/api/erp/productsku'
import { CommonStatusEnum } from '@/utils/constants'
import { defaultProps, handleTree } from '@/utils/tree'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

/** ERP 产品 表单 */
defineOptions({ name: 'ProductForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  barCode: undefined,
  categoryId: undefined,
  unitId: undefined,
  skuId: undefined,
  packageId: undefined,
  oemId: undefined,
  status: undefined,
  standard: undefined,
  remark: undefined,
  expiryDay: undefined,
  weight: undefined,
  purchasePrice: undefined,
  salePrice: undefined,
  minPrice: undefined
})
const formRules = reactive({
  name: [{ required: true, message: '产品名称不能为空', trigger: 'blur' }],
  barCode: [{ required: true, message: '产品条码不能为空', trigger: 'blur' }],
  categoryId: [{ required: true, message: '产品分类编号不能为空', trigger: 'blur' }],
  unitId: [{ required: true, message: '单位编号不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '产品状态不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const categoryList = ref<ProductCategoryVO[]>([]) // 产品分类列表
const unitList = ref<ProductUnitVO[]>([]) // 产品单位列表
const packageList = ref<ProductPackageVO[]>([]) // 产品包装列表
const oemList = ref<ProductOemVO[]>([]) // 产品OEM列表
const skuList = ref<ProductSkuVO[]>([]) // SKU列表
// 多对多关系下，不再需要originalSkuIds

// SKU选择器弹窗相关
const skuDialogVisible = ref(false) // SKU弹窗是否显示
const skuLoading = ref(false) // SKU列表加载中
const skuSelectList = ref<ProductSkuVO[]>([]) // SKU选择列表
const skuTotal = ref(0) // SKU总数
const selectedSkuList = ref<ProductSkuVO[]>([]) // 选中的SKU列表
const skuQueryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  skuCode: undefined,
  skuName: undefined,
  status: undefined
})
const currentSkuIndex = ref<number | null>(null) // 当前编辑的SKU索引（用于编辑功能）

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  
  // 先加载基础数据（分类、单位、包装、OEM），这些数据在新增和编辑时都需要
  try {
    // 并行加载所有基础数据
    const [categoryData, unitData, packageData, oemData] = await Promise.all([
      ProductCategoryApi.getProductCategorySimpleList().catch(err => {
        console.error('加载产品分类失败:', err)
        return []
      }),
      ProductUnitApi.getProductUnitSimpleList().catch(err => {
        console.error('加载产品单位失败:', err)
        return []
      }),
      ProductPackageApi.getSimpleList().catch(err => {
        console.error('加载产品包装列表失败:', err)
        return []
      }),
      ProductOemApi.getSimpleList().catch(err => {
        console.error('加载产品OEM列表失败:', err)
        return []
      })
    ])
    
    // 设置数据
    categoryList.value = handleTree(categoryData || [], 'id', 'parentId')
    unitList.value = unitData || []
    packageList.value = packageData || []
    oemList.value = oemData || []
  } catch (error) {
    console.error('加载基础数据失败:', error)
  }
  
  // 修改时，加载产品数据
  if (id) {
    formLoading.value = true
    try {
      const productData = await ProductApi.getProduct(id)
      formData.value = productData
      // 从返回的产品数据中获取SKU列表（后端已包含，使用 getProductSkuListByProductIdAll 获取所有状态的SKU）
      if (productData && productData.skuList) {
        skuList.value = productData.skuList
      } else {
        // 如果后端没有返回SKU列表，设置为空（后端应该已经返回了）
        skuList.value = []
      }
    } finally {
      formLoading.value = false
    }
  } else {
    skuList.value = []
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
    // 提取SKU ID列表（多对多关系，只传递ID）
    // 注意：需要转换为普通数组，避免Vue Proxy对象导致序列化问题
    const skuIdsArray = skuList.value
      .map(sku => sku.id)
      .filter(id => id !== undefined && id !== null)
    // 使用 JSON 序列化/反序列化确保创建真正的普通数组
    const plainSkuIds = skuIdsArray.length > 0 ? JSON.parse(JSON.stringify(skuIdsArray)) : undefined
    
    // 创建全新的普通对象，避免Vue响应式系统的影响
    const submitData: ProductVO = {
      ...formData.value,
      skuIds: plainSkuIds
    }
    
    // 调试日志
    console.log('提交产品数据:', {
      productId: submitData.id,
      productName: submitData.name,
      skuListCount: skuList.value.length,
      skuIds: submitData.skuIds,
      skuIdsType: Array.isArray(submitData.skuIds) ? 'Array' : typeof submitData.skuIds,
      skuIdsString: JSON.stringify(submitData.skuIds),
      skuIdsIsProxy: submitData.skuIds && typeof submitData.skuIds === 'object' && !Array.isArray(submitData.skuIds) ? '可能是Proxy' : '普通数组'
    })
    
    if (formType.value === 'create') {
      await ProductApi.createProduct(submitData)
      message.success(t('common.createSuccess'))
    } else {
      await ProductApi.updateProduct(submitData)
      message.success(t('common.updateSuccess'))
    }
    
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 添加SKU - 打开选择器 */
const handleAddSku = async () => {
  skuDialogVisible.value = true
  selectedSkuList.value = []
  // 重置查询参数
  skuQueryParams.pageNo = 1
  skuQueryParams.skuCode = undefined
  skuQueryParams.skuName = undefined
  skuQueryParams.status = undefined
  // 加载SKU列表
  await loadSkuList()
}

/** 加载SKU列表 */
const loadSkuList = async () => {
  skuLoading.value = true
  try {
    const data = await ProductSkuApi.getPage(skuQueryParams)
    // 过滤掉已经添加到产品的SKU
    const existingSkuIds = skuList.value.map(sku => sku.id).filter(id => id !== undefined)
    skuSelectList.value = (data.list || []).filter(sku => !existingSkuIds.includes(sku.id))
    skuTotal.value = data.total
  } catch (error) {
    console.error('加载SKU列表失败:', error)
    skuSelectList.value = []
    skuTotal.value = 0
  } finally {
    skuLoading.value = false
  }
}

/** SKU搜索 */
const handleSkuQuery = () => {
  skuQueryParams.pageNo = 1
  loadSkuList()
}

/** SKU搜索重置 */
const handleSkuReset = () => {
  skuQueryParams.pageNo = 1
  skuQueryParams.skuCode = undefined
  skuQueryParams.skuName = undefined
  skuQueryParams.status = undefined
  loadSkuList()
}

/** SKU选择变化 */
const handleSkuSelectionChange = (selection: ProductSkuVO[]) => {
  selectedSkuList.value = selection
}

/** 确认选择SKU */
const handleConfirmSkuSelection = () => {
  if (selectedSkuList.value.length === 0) {
    message.warning('请至少选择一个SKU')
    return
  }
  
  // 将选中的SKU添加到产品SKU列表（过滤掉已存在的）
  const existingSkuIds = skuList.value.map(sku => sku.id).filter(id => id !== undefined)
  const newSkus = selectedSkuList.value.filter(sku => !existingSkuIds.includes(sku.id!))
  
  if (newSkus.length > 0) {
    skuList.value.push(...newSkus)
    message.success(`成功添加 ${newSkus.length} 个SKU`)
  } else {
    message.warning('所选SKU已全部添加，无需重复添加')
  }
  
  // 关闭弹窗
  skuDialogVisible.value = false
  selectedSkuList.value = []
}

/** 编辑SKU - 暂时保留，但改为只读显示，实际编辑需要在SKU管理页面进行 */
const handleEditSku = (index: number) => {
  message.info('SKU编辑功能请在"产品SKU管理"页面进行操作')
}

/** 删除SKU */
const handleDeleteSku = (index: number) => {
  skuList.value.splice(index, 1)
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: undefined,
    barCode: undefined,
    categoryId: undefined,
    unitId: undefined,
    skuId: undefined,
    packageId: undefined,
    oemId: undefined,
    status: CommonStatusEnum.ENABLE,
    standard: undefined,
    remark: undefined,
    expiryDay: undefined,
    weight: undefined,
    purchasePrice: undefined,
    salePrice: undefined,
    minPrice: undefined
  }
  skuList.value = []
  formRef.value?.resetFields()
}
</script>
