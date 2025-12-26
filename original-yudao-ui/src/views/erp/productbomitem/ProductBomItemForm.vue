<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="BOM" prop="bomId">
        <el-select
          v-model="formData.bomId"
          clearable
          filterable
          placeholder="请选择BOM"
          class="!w-1/1"
        >
          <el-option
            v-for="item in productBomList"
            :key="item.id"
            :label="item.bomName || item.bomNo || `BOM${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="父产品" prop="parentProductId">
        <el-select
          v-model="formData.parentProductId"
          clearable
          filterable
          placeholder="请选择父产品"
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
      <el-form-item label="子产品" prop="childProductId">
        <el-select
          v-model="formData.childProductId"
          clearable
          filterable
          placeholder="请选择子产品"
          class="!w-1/1"
          @change="handleChildProductChange"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="子产品名称" prop="childProductName">
        <el-input v-model="formData.childProductName" placeholder="选择子产品后自动填充" readonly />
      </el-form-item>
      <el-form-item label="子产品规格" prop="childProductSpec">
        <el-input v-model="formData.childProductSpec" placeholder="选择子产品后自动填充" readonly />
      </el-form-item>
      <el-form-item label="单位" prop="unitId">
        <el-select
          v-model="formData.unitId"
          clearable
          filterable
          placeholder="选择子产品后自动填充"
          class="!w-1/1"
          :disabled="!!formData.childProductId"
        >
          <el-option
            v-for="item in productUnitList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="用量" prop="quantity">
        <el-input v-model="formData.quantity" placeholder="请输入用量" />
      </el-form-item>
      <el-form-item label="损耗率" prop="lossRate">
        <el-input v-model="formData.lossRate" placeholder="请输入损耗率" />
      </el-form-item>
      <el-form-item label="有效用量" prop="effectiveQuantity">
        <el-input v-model="formData.effectiveQuantity" placeholder="请输入有效用量" />
      </el-form-item>
      <el-form-item label="是否关键物料" prop="isKeyMaterial">
        <el-radio-group v-model="formData.isKeyMaterial">
          <el-radio :value="true">是</el-radio>
          <el-radio :value="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否替代料" prop="isAlternative">
        <el-radio-group v-model="formData.isAlternative">
          <el-radio :value="true">是</el-radio>
          <el-radio :value="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="替代料组" prop="alternativeGroup">
        <el-input v-model="formData.alternativeGroup" placeholder="请输入替代料组" />
      </el-form-item>
      <el-form-item label="位号" prop="position">
        <el-input v-model="formData.position" placeholder="请输入位号" />
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
            v-for="item in processList"
            :key="item.id"
            :label="item.processName || item.processNo || `工序${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" placeholder="请输入备注" />
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
import { ProductBomItemApi, ProductBomItem } from '@/api/erp/productbomitem'
import { ProductBomApi, ProductBom } from '@/api/erp/productbom'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductUnitApi, ProductUnit } from '@/api/erp/product/unit'
import { ProcessApi, Process } from '@/api/erp/process'

/** ERP BOM明细 表单 */
defineOptions({ name: 'ProductBomItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  bomId: undefined,
  parentProductId: undefined,
  childProductId: undefined,
  childProductName: undefined,
  childProductSpec: undefined,
  unitId: undefined,
  quantity: undefined,
  lossRate: undefined,
  effectiveQuantity: undefined,
  isKeyMaterial: undefined,
  isAlternative: undefined,
  alternativeGroup: undefined,
  position: undefined,
  processId: undefined,
  remark: undefined
})
const formRules = reactive({
  bomId: [{ required: true, message: 'BOM不能为空', trigger: 'change' }],
  parentProductId: [{ required: true, message: '父产品不能为空', trigger: 'change' }],
  childProductId: [{ required: true, message: '子产品不能为空', trigger: 'change' }],
  quantity: [{ required: true, message: '用量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const productBomList = ref<ProductBom[]>([]) // BOM列表
const productList = ref<ProductVO[]>([]) // 产品列表
const productUnitList = ref<ProductUnit[]>([]) // 产品单位列表
const processList = ref<Process[]>([]) // 工序列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [bomData, products, unitData, processes] = await Promise.all([
      ProductBomApi.getProductBomPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList(),
      ProductUnitApi.getProductUnitPage({ pageNo: 1, pageSize: 100 }),
      ProcessApi.getProcessList()
    ])
    productBomList.value = bomData.list || []
    productList.value = products || []
    productUnitList.value = unitData.list || []
    processList.value = processes || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 处理子产品选择变化 */
const handleChildProductChange = (productId: number | undefined) => {
  if (productId) {
    // 根据选择的子产品ID，从产品列表中查找对应的产品信息
    const selectedProduct = productList.value.find(item => item.id === productId)
    if (selectedProduct) {
      // 自动填充子产品信息
      formData.value.childProductName = selectedProduct.name || ''
      formData.value.childProductSpec = selectedProduct.standard || ''
      formData.value.unitId = selectedProduct.unitId
    }
  } else {
    // 清空子产品选择时，清空相关信息
    formData.value.childProductName = undefined
    formData.value.childProductSpec = undefined
    formData.value.unitId = undefined
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (productBomList.value.length === 0) {
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
      const data = await ProductBomItemApi.getProductBomItem(id)
      formData.value = data
      // 如果已有子产品ID，但子产品信息不完整，则自动填充（用于编辑场景）
      if (data.childProductId && (!data.childProductName || !data.childProductSpec || !data.unitId)) {
        handleChildProductChange(data.childProductId)
      }
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
    // 构建提交数据，确保类型正确
    // 辅助函数：安全地转换为数字
    const toNumber = (value: any): number | undefined => {
      if (value == null || value === '') return undefined
      const num = Number(value)
      return isNaN(num) ? undefined : num
    }
    
    const data: ProductBomItem = {
      ...formData.value,
      // 确保 unitId 是数字类型
      unitId: toNumber(formData.value.unitId),
      // 确保其他数字字段也是正确的类型
      quantity: toNumber(formData.value.quantity),
      lossRate: toNumber(formData.value.lossRate),
      // effectiveQuantity 是数据库生成的列，不能手动指定，移除该字段
      effectiveQuantity: undefined,
      position: toNumber(formData.value.position),
      // 处理 processId：如果已选择工序，转换为数字；如果未选择，设为 undefined
      processId: toNumber(formData.value.processId)
    }
    // 删除 effectiveQuantity 字段，因为它是数据库生成的列
    delete data.effectiveQuantity
    
    if (formType.value === 'create') {
      await ProductBomItemApi.createProductBomItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductBomItemApi.updateProductBomItem(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } catch (error: any) {
    // 处理错误信息
    const errorMessage = error?.response?.data?.msg || error?.message || '操作失败，请稍后重试'
    message.error(errorMessage)
    console.error('提交BOM明细失败:', error)
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    bomId: undefined,
    parentProductId: undefined,
    childProductId: undefined,
    childProductName: undefined,
    childProductSpec: undefined,
    unitId: undefined,
    quantity: undefined,
    lossRate: undefined,
    effectiveQuantity: undefined,
    isKeyMaterial: undefined,
    isAlternative: undefined,
    alternativeGroup: undefined,
    position: undefined,
    processId: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>