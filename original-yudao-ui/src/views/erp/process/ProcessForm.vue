<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1080">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工序编号" prop="processNo">
            <el-input v-model="formData.processNo" placeholder="请输入工序编号" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工序名称" prop="processName">
            <el-input v-model="formData.processName" placeholder="请输入工序名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工序类型" prop="processType">
            <el-select v-model="formData.processType" placeholder="请选择工序类型" class="!w-1/1">
              <el-option label="加工" :value="1" />
              <el-option label="装配" :value="2" />
              <el-option label="检验" :value="3" />
              <el-option label="包装" :value="4" />
              <el-option label="其他" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="工序分类" prop="category">
            <el-input v-model="formData.category" placeholder="请输入工序分类" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="标准工时(分钟)" prop="standardTime">
            <el-input-number
              v-model="formData.standardTime"
              :min="0"
              placeholder="请输入标准工时"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="准备时间(分钟)" prop="setupTime">
            <el-input-number
              v-model="formData.setupTime"
              :min="0"
              placeholder="请输入准备时间"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="标准人员数量" prop="workerCount">
            <el-input-number
              v-model="formData.workerCount"
              :min="1"
              placeholder="请输入标准人员数量"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="设备类型" prop="equipmentType">
            <el-input v-model="formData.equipmentType" placeholder="请输入设备类型" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="工作中心ID" prop="workCenterId">
            <el-input-number
              v-model="formData.workCenterId"
              :min="0"
              placeholder="请输入工作中心ID"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :value="1">启用</el-radio>
              <el-radio :value="2">停用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="人工费率(元/小时)" prop="laborRate">
            <el-input-number
              v-model="formData.laborRate"
              :min="0"
              :precision="2"
              placeholder="请输入人工费率"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="制造费率(元/小时)" prop="overheadRate">
            <el-input-number
              v-model="formData.overheadRate"
              :min="0"
              :precision="2"
              placeholder="请输入制造费率"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="是否需要质检" prop="qualityCheckRequired">
            <el-radio-group v-model="formData.qualityCheckRequired">
              <el-radio :value="true">是</el-radio>
              <el-radio :value="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否瓶颈工序" prop="isBottleneck">
            <el-radio-group v-model="formData.isBottleneck">
              <el-radio :value="true">是</el-radio>
              <el-radio :value="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="工序描述" prop="description">
        <el-input
          v-model="formData.description"
          type="textarea"
          :rows="3"
          placeholder="请输入工序描述"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { ProcessApi, Process } from '@/api/erp/process'

/** ERP 工序 表单 */
defineOptions({ name: 'ProcessForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  processNo: undefined,
  processName: undefined,
  processType: undefined,
  category: undefined,
  standardTime: undefined,
  setupTime: 0,
  workerCount: 1,
  equipmentType: undefined,
  workCenterId: undefined,
  laborRate: undefined,
  overheadRate: undefined,
  qualityCheckRequired: false,
  isBottleneck: false,
  description: undefined,
  remark: undefined,
  status: 1
})
const formRules = reactive({
  processNo: [{ required: true, message: '工序编号不能为空', trigger: 'blur' }],
  processName: [{ required: true, message: '工序名称不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 新增时，生成默认工序编号
  if (type === 'create') {
    try {
      const processNo = await ProcessApi.generateProcessNo()
      formData.value.processNo = processNo
    } catch (error) {
      console.error('生成工序编号失败:', error)
      // 如果生成失败，不设置默认值，让用户手动输入
    }
  }
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ProcessApi.getProcess(id)
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
    const data = formData.value as unknown as Process
    if (formType.value === 'create') {
      await ProcessApi.createProcess(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProcessApi.updateProcess(data)
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
    processNo: undefined,
    processName: undefined,
    processType: undefined,
    category: undefined,
    standardTime: undefined,
    setupTime: 0,
    workerCount: 1,
    equipmentType: undefined,
    workCenterId: undefined,
    laborRate: undefined,
    overheadRate: undefined,
    qualityCheckRequired: false,
    isBottleneck: false,
    description: undefined,
    remark: undefined,
    status: 1
  }
  formRef.value?.resetFields()
}
</script>

