<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="BOM ID" prop="bomId">
        <el-input v-model="formData.bomId" placeholder="请输入BOM ID" />
      </el-form-item>
      <el-form-item label="父产品ID" prop="parentProductId">
        <el-input v-model="formData.parentProductId" placeholder="请输入父产品ID" />
      </el-form-item>
      <el-form-item label="子产品ID" prop="childProductId">
        <el-input v-model="formData.childProductId" placeholder="请输入子产品ID" />
      </el-form-item>
      <el-form-item label="子产品名称" prop="childProductName">
        <el-input v-model="formData.childProductName" placeholder="请输入子产品名称" />
      </el-form-item>
      <el-form-item label="子产品规格" prop="childProductSpec">
        <el-input v-model="formData.childProductSpec" placeholder="请输入子产品规格" />
      </el-form-item>
      <el-form-item label="单位ID" prop="unitId">
        <el-input v-model="formData.unitId" placeholder="请输入单位ID" />
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
      <el-form-item label="工序ID" prop="processId">
        <el-input v-model="formData.processId" placeholder="请输入工序ID" />
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
  bomId: [{ required: true, message: 'BOM ID不能为空', trigger: 'blur' }],
  parentProductId: [{ required: true, message: '父产品ID不能为空', trigger: 'blur' }],
  childProductId: [{ required: true, message: '子产品ID不能为空', trigger: 'blur' }],
  childProductName: [{ required: true, message: '子产品名称不能为空', trigger: 'blur' }],
  quantity: [{ required: true, message: '用量不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProductBomItemApi.getProductBomItem(id)
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
    const data = formData.value as unknown as ProductBomItem
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