<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="工艺路线ID" prop="routeId">
        <el-input v-model="formData.routeId" placeholder="请输入工艺路线ID" />
      </el-form-item>
      <el-form-item label="工序ID" prop="processId">
        <el-input v-model="formData.processId" placeholder="请输入工序ID" />
      </el-form-item>
      <el-form-item label="序号" prop="sequence">
        <el-input v-model="formData.sequence" placeholder="请输入序号" />
      </el-form-item>
      <el-form-item label="工序名称" prop="operationName">
        <el-input v-model="formData.operationName" placeholder="请输入工序名称" />
      </el-form-item>
      <el-form-item label="标准工时（分钟）" prop="standardTime">
        <el-date-picker
          v-model="formData.standardTime"
          type="date"
          value-format="x"
          placeholder="选择标准工时（分钟）"
        />
      </el-form-item>
      <el-form-item label="准备时间（分钟）" prop="setupTime">
        <el-date-picker
          v-model="formData.setupTime"
          type="date"
          value-format="x"
          placeholder="选择准备时间（分钟）"
        />
      </el-form-item>
      <el-form-item label="人员数量" prop="workerCount">
        <el-input v-model="formData.workerCount" placeholder="请输入人员数量" />
      </el-form-item>
      <el-form-item label="设备ID" prop="equipmentId">
        <el-input v-model="formData.equipmentId" placeholder="请输入设备ID" />
      </el-form-item>
      <el-form-item label="工作中心ID" prop="workCenterId">
        <el-input v-model="formData.workCenterId" placeholder="请输入工作中心ID" />
      </el-form-item>
      <el-form-item label="人工费率" prop="laborRate">
        <el-input v-model="formData.laborRate" placeholder="请输入人工费率" />
      </el-form-item>
      <el-form-item label="制造费率" prop="overheadRate">
        <el-input v-model="formData.overheadRate" placeholder="请输入制造费率" />
      </el-form-item>
      <el-form-item label="是否瓶颈工序" prop="isBottleneck">
        <el-radio-group v-model="formData.isBottleneck">
          <el-radio :value="true">是</el-radio>
          <el-radio :value="false">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="是否需要质检" prop="qualityCheckRequired">
        <el-radio-group v-model="formData.qualityCheckRequired">
          <el-radio :value="true">是</el-radio>
          <el-radio :value="false">否</el-radio>
        </el-radio-group>
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
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'

/** ERP 工艺路线明细 表单 */
defineOptions({ name: 'ProcessRouteItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  routeId: undefined,
  processId: undefined,
  sequence: undefined,
  operationName: undefined,
  standardTime: undefined,
  setupTime: undefined,
  workerCount: undefined,
  equipmentId: undefined,
  workCenterId: undefined,
  laborRate: undefined,
  overheadRate: undefined,
  isBottleneck: undefined,
  qualityCheckRequired: undefined,
  remark: undefined
})
const formRules = reactive({
  routeId: [{ required: true, message: '工艺路线ID不能为空', trigger: 'blur' }],
  processId: [{ required: true, message: '工序ID不能为空', trigger: 'blur' }],
  sequence: [{ required: true, message: '序号不能为空', trigger: 'blur' }],
  operationName: [{ required: true, message: '工序名称不能为空', trigger: 'blur' }],
  standardTime: [{ required: true, message: '标准工时（分钟）不能为空', trigger: 'blur' }]
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
      formData.value = await ProcessRouteItemApi.getProcessRouteItem(id)
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
    const data = formData.value as unknown as ProcessRouteItem
    if (formType.value === 'create') {
      await ProcessRouteItemApi.createProcessRouteItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await ProcessRouteItemApi.updateProcessRouteItem(data)
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
    routeId: undefined,
    processId: undefined,
    sequence: undefined,
    operationName: undefined,
    standardTime: undefined,
    setupTime: undefined,
    workerCount: undefined,
    equipmentId: undefined,
    workCenterId: undefined,
    laborRate: undefined,
    overheadRate: undefined,
    isBottleneck: undefined,
    qualityCheckRequired: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>