<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item label="设备" prop="equipmentId">
        <el-select
          v-model="queryParams.equipmentId"
          clearable
          filterable
          placeholder="请选择设备"
          class="!w-240px"
        >
          <el-option
            v-for="item in equipmentList"
            :key="item.id"
            :label="item.equipmentName || item.equipmentNo || `设备${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_EQUIPMENT_STATUS_RECORD)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态开始时间" prop="statusStartTime">
        <el-date-picker
          v-model="queryParams.statusStartTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="状态结束时间" prop="statusEndTime">
        <el-date-picker
          v-model="queryParams.statusEndTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="持续时间" prop="duration">
        <el-input
          v-model="queryParams.duration"
          placeholder="请输入持续时间（分钟）"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="关联工单" prop="workOrderId">
        <el-select
          v-model="queryParams.workOrderId"
          clearable
          filterable
          placeholder="请选择关联工单"
          class="!w-240px"
        >
          <el-option
            v-for="item in workOrderList"
            :key="item.id"
            :label="item.workOrderNo || `工单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="操作员" prop="operatorId">
        <el-select
          v-model="queryParams.operatorId"
          clearable
          filterable
          placeholder="请选择操作员"
          class="!w-240px"
        >
          <el-option
            v-for="item in userList"
            :key="item.id"
            :label="item.nickname"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['erp:equipment-status:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:equipment-status:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:equipment-status:delete']"
        >
          <Icon icon="ep:delete" class="mr-5px" /> 批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
        row-key="id"
        v-loading="loading"
        :data="list"
        :stripe="true"
        :show-overflow-tooltip="true"
        @selection-change="handleRowCheckboxChange"
    >
    <el-table-column type="selection" width="55" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="设备名称" align="center" min-width="120">
        <template #default="scope">
          {{ getEquipmentName(scope.row.equipmentId) }}
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_EQUIPMENT_STATUS_RECORD" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        label="状态开始时间"
        align="center"
        prop="statusStartTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="状态结束时间"
        align="center"
        prop="statusEndTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="持续时间" align="center" prop="duration" />
      <el-table-column label="关联工单号" align="center" min-width="120">
        <template #default="scope">
          {{ getWorkOrderName(scope.row.workOrderId) }}
        </template>
      </el-table-column>
      <el-table-column label="操作员" align="center" min-width="100">
        <template #default="scope">
          {{ getUserName(scope.row.operatorId) }}
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:equipment-status:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:equipment-status:delete']"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <EquipmentStatusForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { EquipmentStatusApi, EquipmentStatus } from '@/api/erp/equipmentstatus'
import EquipmentStatusForm from './EquipmentStatusForm.vue'
import { EquipmentApi, Equipment } from '@/api/erp/equipment'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 设备状态记录 列表 */
defineOptions({ name: 'EquipmentStatus' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<EquipmentStatus[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  equipmentId: undefined,
  status: undefined,
  statusStartTime: [],
  statusEndTime: [],
  duration: undefined,
  workOrderId: undefined,
  operatorId: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const equipmentList = ref<Equipment[]>([]) // 设备列表
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await EquipmentStatusApi.getEquipmentStatusPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await EquipmentStatusApi.deleteEquipmentStatus(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 设备状态记录 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await EquipmentStatusApi.deleteEquipmentStatusList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: EquipmentStatus[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await EquipmentStatusApi.exportEquipmentStatus(queryParams)
    download.excel(data, 'ERP 设备状态记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 获取设备名称 */
const getEquipmentName = (id?: number) => {
  if (!id) return '-'
  const equipment = equipmentList.value.find(item => item.id === id)
  return equipment?.equipmentName || equipment?.equipmentNo || `设备${id}`
}

/** 获取工单名称 */
const getWorkOrderName = (id?: number) => {
  if (!id) return '-'
  const workOrder = workOrderList.value.find(item => item.id === id)
  return workOrder?.workOrderNo || `工单${id}`
}

/** 获取用户名称 */
const getUserName = (id?: number) => {
  if (!id) return '-'
  const user = userList.value.find(item => item.id === id)
  return user?.nickname || `用户${id}`
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载设备、工单、用户列表
  try {
    const [equipmentData, workOrderData, users] = await Promise.all([
      EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 }),
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    equipmentList.value = equipmentData.list || []
    workOrderList.value = workOrderData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
})
</script>