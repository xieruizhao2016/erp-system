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
    <el-table :data="formData" class="-mt-10px" max-height="500">
      <el-table-column label="序号" align="center" width="80">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.sequence`" :rules="formRules.sequence" class="mb-0px!">
            <el-input-number
              v-model="row.sequence"
              :min="1"
              :precision="0"
              controls-position="right"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="工序" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.processId`" :rules="formRules.processId" class="mb-0px!">
            <el-select
              v-model="row.processId"
              clearable
              filterable
              @change="onChangeProcess($event, row)"
              placeholder="请选择工序"
              :disabled="disabled"
              class="!w-100%"
            >
              <el-option
                v-for="item in processList"
                :key="item.id"
                :label="item.processName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="工序名称" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.operationName`" :rules="formRules.operationName" class="mb-0px!">
            <el-input
              v-model="row.operationName"
              placeholder="请输入工序名称"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="标准工时(分钟)" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.standardTime`" :rules="formRules.standardTime" class="mb-0px!">
            <el-input-number
              v-model="row.standardTime"
              :min="0"
              :precision="0"
              controls-position="right"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="准备时间(分钟)" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.setupTime`" class="mb-0px!">
            <el-input-number
              v-model="row.setupTime"
              :min="0"
              :precision="0"
              controls-position="right"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="人员数量" min-width="100">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.workerCount`" class="mb-0px!">
            <el-input-number
              v-model="row.workerCount"
              :min="1"
              :precision="0"
              controls-position="right"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="设备" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.equipmentId`" class="mb-0px!">
            <el-select
              v-model="row.equipmentId"
              clearable
              filterable
              placeholder="请选择设备"
              :disabled="disabled"
              class="!w-100%"
            >
              <el-option
                v-for="item in equipmentList"
                :key="item.id"
                :label="item.equipmentName || item.equipmentNo || `设备${item.id}`"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="工作中心" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.workCenterId`" class="mb-0px!">
            <el-input-number
              v-model="row.workCenterId"
              :min="0"
              :precision="0"
              controls-position="right"
              class="!w-100%"
              :disabled="disabled"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="是否瓶颈" align="center" width="100">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.isBottleneck`" class="mb-0px!">
            <el-radio-group v-model="row.isBottleneck" :disabled="disabled">
              <el-radio :value="true">是</el-radio>
              <el-radio :value="false">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="需要质检" align="center" width="100">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.qualityCheckRequired`" class="mb-0px!">
            <el-radio-group v-model="row.qualityCheckRequired" :disabled="disabled">
              <el-radio :value="true">是</el-radio>
              <el-radio :value="false">否</el-radio>
            </el-radio-group>
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
    <el-button @click="handleAdd" round>+ 添加工序</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { ProcessApi } from '@/api/erp/process'
import { EquipmentApi } from '@/api/erp/equipment'

const props = defineProps<{
  items?: any[]
  disabled?: boolean
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref<any[]>([])
const formRules = reactive({
  processId: [{ required: true, message: '工序不能为空', trigger: 'change' }],
  sequence: [{ required: true, message: '序号不能为空', trigger: 'blur' }],
  operationName: [{ required: true, message: '工序名称不能为空', trigger: 'blur' }],
  standardTime: [{ required: true, message: '标准工时不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const processList = ref<any[]>([]) // 工序列表
const equipmentList = ref<any[]>([]) // 设备列表

/** 初始化设置明细项 */
watch(
  () => props.items,
  (val) => {
    if (val) {
      formData.value = val.map(item => ({
        ...item,
        isBottleneck: item.isBottleneck ?? false,
        qualityCheckRequired: item.qualityCheckRequired ?? false,
        setupTime: item.setupTime ?? 0,
        workerCount: item.workerCount ?? 1
      }))
    } else {
      formData.value = []
    }
  },
  { immediate: true }
)

/** 新增按钮操作 */
const handleAdd = () => {
  const maxSequence = formData.value.length > 0
    ? Math.max(...formData.value.map(item => item.sequence || 0))
    : 0
  const row = {
    id: undefined,
    processId: undefined,
    sequence: maxSequence + 1,
    operationName: undefined,
    standardTime: undefined,
    setupTime: 0,
    workerCount: 1,
    equipmentId: undefined,
    workCenterId: undefined,
    laborRate: undefined,
    overheadRate: undefined,
    isBottleneck: false,
    qualityCheckRequired: false,
    remark: undefined
  }
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index: number) => {
  formData.value.splice(index, 1)
  // 重新排序序号
  formData.value.forEach((item, idx) => {
    item.sequence = idx + 1
  })
}

/** 处理工序变更 */
const onChangeProcess = (processId: number | undefined, row: any) => {
  if (!processId) {
    row.operationName = undefined
    row.standardTime = undefined
    row.setupTime = 0
    row.workerCount = 1
    return
  }
  const process = processList.value.find((item) => item.id === processId)
  if (process) {
    // 自动填充工序名称
    if (!row.operationName) {
      row.operationName = process.processName
    }
    // 自动填充标准工时和准备时间（如果为空）
    if (row.standardTime === undefined && process.standardTime) {
      row.standardTime = process.standardTime
    }
    if (row.setupTime === undefined && process.setupTime) {
      row.setupTime = process.setupTime
    }
    if (row.workerCount === undefined && process.workerCount) {
      row.workerCount = process.workerCount
    }
  }
}

/** 表单校验 */
const validate = () => {
  return formRef.value?.validate()
}
defineExpose({ validate, formData })

/** 初始化 */
onMounted(async () => {
  try {
    // 加载工序列表
    processList.value = await ProcessApi.getProcessList() || []
    // 加载设备列表
    const equipmentData = await EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 })
    equipmentList.value = equipmentData.list || []
    // 默认添加一个（如果没有数据且不是禁用状态）
    if (formData.value.length === 0 && !props.disabled) {
      handleAdd()
    }
  } catch (error) {
    console.error('加载数据失败:', error)
  }
})
</script>

