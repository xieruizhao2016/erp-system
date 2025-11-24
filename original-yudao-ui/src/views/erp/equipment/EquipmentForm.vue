<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="设备编号" prop="equipmentNo">
        <el-input v-model="formData.equipmentNo" placeholder="请输入设备编号" />
      </el-form-item>
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input v-model="formData.equipmentName" placeholder="请输入设备名称" />
      </el-form-item>
      <el-form-item label="设备类型" prop="equipmentType">
        <el-input v-model="formData.equipmentType" placeholder="请输入设备类型" />
      </el-form-item>
      <el-form-item label="设备型号" prop="model">
        <el-input v-model="formData.model" placeholder="请输入设备型号" />
      </el-form-item>
      <el-form-item label="制造商" prop="manufacturer">
        <el-input v-model="formData.manufacturer" placeholder="请输入制造商" />
      </el-form-item>
      <el-form-item label="序列号" prop="serialNumber">
        <el-input v-model="formData.serialNumber" placeholder="请输入序列号" />
      </el-form-item>
      <el-form-item label="购置日期" prop="purchaseDate">
        <el-date-picker
          v-model="formData.purchaseDate"
          type="date"
          value-format="x"
          placeholder="选择购置日期"
        />
      </el-form-item>
      <el-form-item label="购置价格" prop="purchasePrice">
        <el-input v-model="formData.purchasePrice" placeholder="请输入购置价格" />
      </el-form-item>
      <el-form-item label="设计寿命" prop="serviceLife">
        <el-input v-model="formData.serviceLife" placeholder="请输入设计寿命（年）" />
      </el-form-item>
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心" />
      </el-form-item>
      <el-form-item label="设备位置" prop="location">
        <el-input v-model="formData.location" placeholder="请输入设备位置" />
      </el-form-item>
      <el-form-item label="产能" prop="capacity">
        <el-input v-model="formData.capacity" placeholder="请输入产能（小时/天）" />
      </el-form-item>
      <el-form-item label="效率系数" prop="efficiencyRate">
        <el-input v-model="formData.efficiencyRate" placeholder="请输入效率系数" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="formData.status">
          <el-radio
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_EQUIPMENT_STATUS)"
            :key="dict.value"
            :value="dict.value"
          >
            {{ dict.label }}
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="责任人" prop="responsiblePerson">
        <el-input v-model="formData.responsiblePerson" placeholder="请输入责任人" />
      </el-form-item>
      <el-form-item label="技术规格" prop="specification">
        <el-input v-model="formData.specification" placeholder="请输入技术规格" />
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
import { EquipmentApi, Equipment } from '@/api/erp/equipment'

/** ERP 设备台账 表单 */
defineOptions({ name: 'EquipmentForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  equipmentNo: undefined,
  equipmentName: undefined,
  equipmentType: undefined,
  model: undefined,
  manufacturer: undefined,
  serialNumber: undefined,
  purchaseDate: undefined,
  purchasePrice: undefined,
  serviceLife: undefined,
  workCenterId: undefined,
  location: undefined,
  capacity: undefined,
  efficiencyRate: undefined,
  status: undefined,
  responsiblePerson: undefined,
  specification: undefined,
  remark: undefined
})
const formRules = reactive({
  equipmentNo: [{ required: true, message: '设备编号不能为空', trigger: 'blur' }],
  equipmentName: [{ required: true, message: '设备名称不能为空', trigger: 'blur' }]
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
      formData.value = await EquipmentApi.getEquipment(id)
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
    const data = formData.value as unknown as Equipment
    if (formType.value === 'create') {
      await EquipmentApi.createEquipment(data)
      message.success(t('common.createSuccess'))
    } else {
      await EquipmentApi.updateEquipment(data)
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
    equipmentNo: undefined,
    equipmentName: undefined,
    equipmentType: undefined,
    model: undefined,
    manufacturer: undefined,
    serialNumber: undefined,
    purchaseDate: undefined,
    purchasePrice: undefined,
    serviceLife: undefined,
    workCenterId: undefined,
    location: undefined,
    capacity: undefined,
    efficiencyRate: undefined,
    status: undefined,
    responsiblePerson: undefined,
    specification: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>

