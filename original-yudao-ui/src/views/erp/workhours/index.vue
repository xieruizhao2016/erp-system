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
      <el-form-item label="工单" prop="workOrderId">
        <el-select
          v-model="queryParams.workOrderId"
          clearable
          filterable
          placeholder="请选择工单"
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
      <el-form-item label="工序" prop="processId">
        <el-select
          v-model="queryParams.processId"
          clearable
          filterable
          placeholder="请选择工序"
          class="!w-240px"
        >
          <el-option
            v-for="item in processRouteItemList"
            :key="item.id"
            :label="item.operationName || `工序${item.id}`"
            :value="item.processId"
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
      <el-form-item label="工作日期" prop="workDate">
        <el-date-picker
          v-model="queryParams.workDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="班次" prop="shiftId">
        <el-input
          v-model="queryParams.shiftId"
          placeholder="请输入班次"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker
          v-model="queryParams.startTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker
          v-model="queryParams.endTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_HOURS_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
          v-hasPermi="['erp:work-hours:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:work-hours:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:work-hours:delete']"
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
      <el-table-column label="工单号" align="center" min-width="120">
        <template #default="scope">
          {{ getWorkOrderName(scope.row.workOrderId) }}
        </template>
      </el-table-column>
      <el-table-column label="工序名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProcessName(scope.row.processId) }}
        </template>
      </el-table-column>
      <el-table-column label="操作员" align="center" min-width="100">
        <template #default="scope">
          {{ getUserName(scope.row.operatorId) }}
        </template>
      </el-table-column>
      <el-table-column label="工作日期" align="center" prop="workDate" />
      <el-table-column label="班次" align="center" prop="shiftId" />
      <el-table-column label="开始时间" align="center" prop="startTime" />
      <el-table-column label="结束时间" align="center" prop="endTime" />
      <el-table-column label="工作时长" align="center" prop="duration" />
      <el-table-column label="标准工时" align="center" prop="standardDuration" />
      <el-table-column label="加班时长" align="center" prop="overtimeDuration" />
      <el-table-column label="机时" align="center" prop="machineHours" />
      <el-table-column label="操作员工时费率" align="center" prop="operatorRate" />
      <el-table-column label="设备时费率" align="center" prop="machineRate" />
      <el-table-column label="人工成本" align="center" prop="laborCost" />
      <el-table-column label="设备成本" align="center" prop="machineCost" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_WORK_HOURS_STATUS" :value="scope.row.status" />
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
            v-hasPermi="['erp:work-hours:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:work-hours:delete']"
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
  <WorkHoursForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { WorkHoursApi, WorkHours } from '@/api/erp/workhours'
import WorkHoursForm from './WorkHoursForm.vue'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 工时统计 列表 */
defineOptions({ name: 'WorkHours' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<WorkHours[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  workOrderId: undefined,
  processId: undefined,
  operatorId: undefined,
  workDate: [],
  shiftId: undefined,
  startTime: [],
  endTime: [],
  duration: undefined,
  standardDuration: undefined,
  overtimeDuration: undefined,
  machineHours: undefined,
  laborCost: undefined,
  machineCost: undefined,
  status: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await WorkHoursApi.getWorkHoursPage(queryParams)
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
    await WorkHoursApi.deleteWorkHours(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 工时统计 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await WorkHoursApi.deleteWorkHoursList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: WorkHours[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await WorkHoursApi.exportWorkHours(queryParams)
    download.excel(data, 'ERP 工时统计.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 获取工单名称 */
const getWorkOrderName = (id?: number) => {
  if (!id) return '-'
  const workOrder = workOrderList.value.find(item => item.id === id)
  return workOrder?.workOrderNo || `工单${id}`
}

/** 获取工序名称 */
const getProcessName = (id?: number) => {
  if (!id) return '-'
  const process = processRouteItemList.value.find(item => item.processId === id)
  return process?.operationName || `工序${id}`
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
  // 加载工单、工艺路线明细、用户列表
  try {
    const [workOrderData, processRouteItemData, users] = await Promise.all([
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 }),
      UserApi.getSimpleUserList()
    ])
    workOrderList.value = workOrderData.list || []
    processRouteItemList.value = processRouteItemData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
})
</script>