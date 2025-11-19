<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="800">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
    >
      <el-form-item label="检验记录" prop="inspectionId">
        <el-select
          v-model="formData.inspectionId"
          clearable
          filterable
          placeholder="请选择检验记录"
          class="!w-1/1"
        >
          <el-option
            v-for="item in qualityInspectionList"
            :key="item.id"
            :label="item.inspectionNo || `检验${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="检验项目" prop="itemId">
        <el-select
          v-model="formData.itemId"
          clearable
          filterable
          placeholder="请选择检验项目"
          class="!w-1/1"
        >
          <el-option
            v-for="item in qualityItemList"
            :key="item.id"
            :label="item.itemName || item.itemNo || `项目${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="样本编号" prop="sampleNo">
        <el-input v-model="formData.sampleNo" placeholder="请输入样本编号" />
      </el-form-item>
      <el-form-item label="测量值" prop="measuredValue">
        <el-input v-model="formData.measuredValue" placeholder="请输入测量值" />
      </el-form-item>
      <el-form-item label="实际数值" prop="actualValue">
        <el-input v-model="formData.actualValue" placeholder="请输入实际数值" />
      </el-form-item>
      <el-form-item label="检验结果" prop="testResult">
        <el-select v-model="formData.testResult" placeholder="请选择检验结果" clearable>
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_QUALITY_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="缺陷类型" prop="defectType">
        <el-input v-model="formData.defectType" placeholder="请输入缺陷类型" />
      </el-form-item>
      <el-form-item label="缺陷描述" prop="defectDescription">
        <Editor v-model="formData.defectDescription" height="150px" />
      </el-form-item>
      <el-form-item label="缺陷图片URLs" prop="imageUrls">
        <el-input v-model="formData.imageUrls" placeholder="请输入缺陷图片URLs" />
      </el-form-item>
      <el-form-item label="检验员" prop="inspectorId">
        <el-select
          v-model="formData.inspectorId"
          clearable
          filterable
          placeholder="请选择检验员"
          class="!w-1/1"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="检验时间" prop="inspectionTime">
        <el-date-picker
          v-model="formData.inspectionTime"
          type="date"
          value-format="x"
          placeholder="选择检验时间"
        />
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
import { QualityInspectionItemApi, QualityInspectionItem } from '@/api/erp/qualityinspectionitem'
import { QualityInspectionApi, QualityInspection } from '@/api/erp/qualityinspection'
import { QualityItemApi, QualityItem } from '@/api/erp/qualityitem'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 质检明细 表单 */
defineOptions({ name: 'QualityInspectionItemForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  inspectionId: undefined,
  itemId: undefined,
  sampleNo: undefined,
  measuredValue: undefined,
  actualValue: undefined,
  testResult: undefined,
  defectType: undefined,
  defectDescription: undefined,
  imageUrls: undefined,
  inspectorId: undefined,
  inspectionTime: undefined,
  remark: undefined
})
const formRules = reactive({
  inspectionId: [{ required: true, message: '检验记录不能为空', trigger: 'change' }],
  itemId: [{ required: true, message: '检验项目不能为空', trigger: 'change' }],
  inspectionTime: [{ required: true, message: '检验时间不能为空', trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref
const qualityInspectionList = ref<QualityInspection[]>([]) // 质检记录列表
const qualityItemList = ref<QualityItem[]>([]) // 质检项目列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 加载列表数据 */
const loadListData = async () => {
  try {
    const [inspectionData, itemData, users] = await Promise.all([
      QualityInspectionApi.getQualityInspectionPage({ pageNo: 1, pageSize: 100 }),
      QualityItemApi.getQualityItemPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    qualityInspectionList.value = inspectionData.list || []
    qualityItemList.value = itemData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
}

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  // 首次打开时加载列表数据
  if (qualityInspectionList.value.length === 0) {
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
      formData.value = await QualityInspectionItemApi.getQualityInspectionItem(id)
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
    const data = formData.value as unknown as QualityInspectionItem
    if (formType.value === 'create') {
      await QualityInspectionItemApi.createQualityInspectionItem(data)
      message.success(t('common.createSuccess'))
    } else {
      await QualityInspectionItemApi.updateQualityInspectionItem(data)
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
    inspectionId: undefined,
    itemId: undefined,
    sampleNo: undefined,
    measuredValue: undefined,
    actualValue: undefined,
    testResult: undefined,
    defectType: undefined,
    defectDescription: undefined,
    imageUrls: undefined,
    inspectorId: undefined,
    inspectionTime: undefined,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>