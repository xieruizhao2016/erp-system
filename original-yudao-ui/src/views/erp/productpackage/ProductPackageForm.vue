<!-- ERP 产品包装的新增/修改 -->
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
          <el-form-item label="包装编码" prop="packageCode">
            <el-input v-model="formData.packageCode" placeholder="请输入包装编码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="包装名称" prop="packageName">
            <el-input v-model="formData.packageName" placeholder="请输入包装名称" />
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
          <el-form-item label="装箱数量" prop="quantityPerBox">
            <el-input-number
              v-model="formData.quantityPerBox"
              placeholder="请输入装箱数量"
              :min="1"
              :precision="0"
              class="!w-1/1"
            />
            <span class="ml-2 text-gray-500">件/箱</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单箱毛重(KG)" prop="grossWeight">
            <el-input-number
              v-model="formData.grossWeight"
              placeholder="请输入单箱毛重"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="单箱净重(KG)" prop="netWeight">
            <el-input-number
              v-model="formData.netWeight"
              placeholder="请输入单箱净重"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="内箱尺寸(CM)" prop="innerBoxSize">
            <el-input v-model="formData.innerBoxSize" placeholder="格式：长x宽x高，如30x20x15" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外箱尺寸(CM)" prop="outerBoxSize">
            <el-input v-model="formData.outerBoxSize" placeholder="格式：长x宽x高，如32x22x17" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="外箱体积(m³)" prop="boxVolume">
            <el-input-number
              v-model="formData.boxVolume"
              placeholder="请输入外箱体积"
              :min="0"
              :precision="4"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="托盘装箱数" prop="palletQuantity">
            <el-input-number
              v-model="formData.palletQuantity"
              placeholder="请输入托盘装箱数"
              :min="0"
              :precision="0"
              class="!w-1/1"
            />
            <span class="ml-2 text-gray-500">箱/托盘</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="包装材料" prop="material">
            <el-input v-model="formData.material" placeholder="请输入包装材料，如：瓦楞纸" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="包装类型" prop="packageType">
            <el-input v-model="formData.packageType" placeholder="请输入包装类型，如：纸箱" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="包装条码" prop="barcode">
            <el-input v-model="formData.barcode" placeholder="请输入包装条码" />
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
          <el-form-item label="包装描述" prop="description">
            <el-input
              v-model="formData.description"
              type="textarea"
              placeholder="请输入包装描述"
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
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductPackageApi, ProductPackageVO } from '@/api/erp/productpackage'
import { CommonStatusEnum } from '@/utils/constants'

/** ERP 产品包装表单 */
defineOptions({ name: 'ProductPackageForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  packageCode: '',
  packageName: '',
  description: '',
  status: CommonStatusEnum.ENABLE,
  quantityPerBox: 1,
  grossWeight: undefined,
  netWeight: undefined,
  innerBoxSize: '',
  outerBoxSize: '',
  boxVolume: undefined,
  palletQuantity: undefined,
  material: '',
  packageType: '',
  barcode: '',
  imageUrl: '',
  sort: 0,
  remark: ''
})
const formRules = reactive({
  packageCode: [{ required: true, message: '包装编码不能为空', trigger: 'blur' }],
  packageName: [{ required: true, message: '包装名称不能为空', trigger: 'blur' }],
  status: [{ required: true, message: '状态不能为空', trigger: 'change' }]
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
      formData.value = await ProductPackageApi.get(id)
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
    const data = formData.value as unknown as ProductPackageVO
    if (formType.value === 'create') {
      await ProductPackageApi.create(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProductPackageApi.update(data)
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
    packageCode: '',
    packageName: '',
    description: '',
    status: CommonStatusEnum.ENABLE,
    quantityPerBox: 1,
    grossWeight: undefined,
    netWeight: undefined,
    innerBoxSize: '',
    outerBoxSize: '',
    boxVolume: undefined,
    palletQuantity: undefined,
    material: '',
    packageType: '',
    barcode: '',
    imageUrl: '',
    sort: 0,
    remark: ''
  }
  formRef.value?.resetFields()
}
</script>

