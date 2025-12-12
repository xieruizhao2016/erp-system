<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1200px">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="formLoading"
      :disabled="disabled"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="入库单号" prop="no">
            <el-input disabled v-model="formData.no" placeholder="保存时自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="员工" prop="employeeId">
            <el-select
              v-model="formData.employeeId"
              clearable
              filterable
              placeholder="请选择员工"
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="部门" prop="deptId">
            <el-select
              v-model="formData.deptId"
              clearable
              filterable
              placeholder="请选择部门"
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="item in deptList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="内部类型" prop="internalType">
            <el-select
              v-model="formData.internalType"
              placeholder="请选择内部类型"
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option label="部门调拨" :value="1" />
              <el-option label="员工领用" :value="2" />
              <el-option label="其他" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="调入仓库" prop="warehouseId">
            <el-select
              v-model="formData.warehouseId"
              clearable
              filterable
              placeholder="请选择调入仓库"
              style="width: 100%"
              :disabled="disabled"
            >
              <el-option
                v-for="item in warehouseList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="入库时间" prop="inTime">
            <el-date-picker
              v-model="formData.inTime"
              type="datetime"
              value-format="x"
              placeholder="选择入库时间"
              style="width: 100%"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="备注" prop="remark">
            <el-input
              type="textarea"
              v-model="formData.remark"
              :rows="1"
              placeholder="请输入备注"
              :disabled="disabled"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="附件" prop="fileUrl">
            <UploadFile :is-show-tip="false" v-model="formData.fileUrl" :limit="1" :disabled="disabled" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <!-- 子表的表单 -->
    <ContentWrap>
      <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
        <el-tab-pane label="入库产品清单" name="item">
          <StockInternalInItemForm ref="itemFormRef" :items="formData.items" :disabled="disabled" />
        </el-tab-pane>
      </el-tabs>
    </ContentWrap>
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button v-if="!disabled" @click="submitForm" type="primary" :disabled="formLoading">
        确 定
      </el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { StockInternalInApi, StockInternalInVO } from '@/api/erp/stock/internal-in'
import * as UserApi from '@/api/system/user'
import * as DeptApi from '@/api/system/dept'
import { WarehouseApi, WarehouseVO } from '@/api/erp/stock/warehouse'
import { handleTree } from '@/utils/tree'
import StockInternalInItemForm from './components/StockInternalInItemForm.vue'

/** ERP 内部入库 表单 */
defineOptions({ name: 'ErpStockInternalInForm' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改；detail - 详情
const disabled = computed(() => formType.value === 'detail')
const subTabsName = ref('item')
const formData = ref({
  id: undefined,
  no: '',
  employeeId: undefined,
  deptId: undefined,
  internalType: 1,
  warehouseId: undefined,
  inTime: undefined,
  remark: '',
  fileUrl: '',
  items: []
})
const formRules = reactive({
  employeeId: [{ required: true, message: '员工不能为空', trigger: 'change' }],
  deptId: [{ required: true, message: '部门不能为空', trigger: 'change' }],
  internalType: [{ required: true, message: '内部类型不能为空', trigger: 'change' }],
  warehouseId: [{ required: true, message: '调入仓库不能为空', trigger: 'change' }],
  inTime: [{ required: true, message: '入库时间不能为空', trigger: 'change' }]
})
const formRef = ref() // 表单 Ref
const itemFormRef = ref() // 明细项表单 Ref
const userList = ref<UserApi.UserVO[]>([]) // 用户列表
const deptList = ref<DeptApi.DeptVO[]>([]) // 部门列表
const warehouseList = ref<WarehouseVO[]>([]) // 仓库列表

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
      formData.value = await StockInternalInApi.getStockInternalIn(id)
    } finally {
      formLoading.value = false
    }
  }
  // 加载用户和部门列表
  if (userList.value.length === 0) {
    UserApi.getSimpleUserList().then((data) => {
      userList.value = data
    })
  }
  if (deptList.value.length === 0) {
    DeptApi.getSimpleDeptList().then((data) => {
      deptList.value = handleTree(data)
    })
  }
  if (warehouseList.value.length === 0) {
    WarehouseApi.getWarehouseSimpleList().then((data) => {
      warehouseList.value = data
    })
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  await itemFormRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as StockInternalInVO
    if (formType.value === 'create') {
      await StockInternalInApi.createStockInternalIn(data)
      message.success(t('common.createSuccess'))
    } else {
      await StockInternalInApi.updateStockInternalIn(data)
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
    no: '',
    employeeId: undefined,
    deptId: undefined,
    internalType: 1,
    warehouseId: undefined,
    inTime: undefined,
    remark: '',
    fileUrl: '',
    items: []
  }
  formRef.value?.resetFields()
}
</script>

