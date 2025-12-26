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
      <el-form-item label="工序名称" prop="processName">
        <el-input
          v-model="queryParams.processName"
          placeholder="请输入工序名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工序序号" prop="sequence">
        <el-input
          v-model="queryParams.sequence"
          placeholder="请输入工序序号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="计划开始时间" prop="plannedStartTime">
        <el-date-picker
          v-model="queryParams.plannedStartTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="计划结束时间" prop="plannedEndTime">
        <el-date-picker
          v-model="queryParams.plannedEndTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="实际开始时间" prop="actualStartTime">
        <el-date-picker
          v-model="queryParams.actualStartTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="实际结束时间" prop="actualEndTime">
        <el-date-picker
          v-model="queryParams.actualEndTime"
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
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_WORK_ORDER_PROGRESS_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
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
      <el-form-item label="实际工时（分钟）" prop="workTime">
        <el-input-number
          v-model="queryParams.workTimeMin"
          placeholder="最小工时"
          :min="0"
          class="!w-120px"
        />
        <span class="mx-2">-</span>
        <el-input-number
          v-model="queryParams.workTimeMax"
          placeholder="最大工时"
          :min="0"
          class="!w-120px"
        />
      </el-form-item>
      <el-form-item label="停机时间（分钟）" prop="downtime">
        <el-input-number
          v-model="queryParams.downtimeMin"
          placeholder="最小时间"
          :min="0"
          class="!w-120px"
        />
        <span class="mx-2">-</span>
        <el-input-number
          v-model="queryParams.downtimeMax"
          placeholder="最大时间"
          :min="0"
          class="!w-120px"
        />
      </el-form-item>
      <el-form-item label="质检状态" prop="qualityStatus">
        <el-select
          v-model="queryParams.qualityStatus"
          placeholder="请选择质检状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_QUALITY_STATUS)"
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
          v-hasPermi="['erp:work-order-progress:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:work-order-progress:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:work-order-progress:delete']"
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
      <el-table-column label="工序名称" align="center" prop="processName" min-width="120" />
      <el-table-column label="工序序号" align="center" prop="sequence" />
      <el-table-column
        label="计划开始时间"
        align="center"
        prop="plannedStartTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="计划结束时间"
        align="center"
        prop="plannedEndTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="实际开始时间"
        align="center"
        prop="actualStartTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="实际结束时间"
        align="center"
        prop="actualEndTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="计划数量" align="center" prop="plannedQuantity" />
      <el-table-column label="完成数量" align="center" prop="completedQuantity" />
      <el-table-column label="合格数量" align="center" prop="qualifiedQuantity" />
      <el-table-column label="不合格数量" align="center" prop="rejectedQuantity" />
      <el-table-column label="报废数量" align="center" prop="scrapQuantity" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_WORK_ORDER_PROGRESS_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作员" align="center" min-width="100">
        <template #default="scope">
          {{ getUserName(scope.row.operatorId) }}
        </template>
      </el-table-column>
      <el-table-column label="设备名称" align="center" min-width="120">
        <template #default="scope">
          {{ getEquipmentName(scope.row.equipmentId) }}
        </template>
      </el-table-column>
      <el-table-column label="实际工时" align="center" prop="workTime" width="120">
        <template #default="scope">
          <span v-if="scope.row.workTime !== null && scope.row.workTime !== undefined">
            {{ formatMinutes(scope.row.workTime) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="停机时间" align="center" prop="downtime" width="120">
        <template #default="scope">
          <span v-if="scope.row.downtime !== null && scope.row.downtime !== undefined">
            {{ formatMinutes(scope.row.downtime) }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="质检状态" align="center" prop="qualityStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_QUALITY_STATUS" :value="scope.row.qualityStatus" />
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
            v-hasPermi="['erp:work-order-progress:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:work-order-progress:delete']"
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
  <WorkOrderProgressForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { WorkOrderProgressApi, WorkOrderProgress } from '@/api/erp/workorderprogress'
import WorkOrderProgressForm from './WorkOrderProgressForm.vue'
import { WorkOrderApi, WorkOrder } from '@/api/erp/workorder'
import { EquipmentApi, Equipment } from '@/api/erp/equipment'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import * as UserApi from '@/api/system/user'
import { UserVO } from '@/api/system/user'

/** ERP 工单进度 列表 */
defineOptions({ name: 'WorkOrderProgress' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<WorkOrderProgress[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  workOrderId: undefined,
  processId: undefined,
  processName: undefined,
  sequence: undefined,
  plannedStartTime: [],
  plannedEndTime: [],
  actualStartTime: [],
  actualEndTime: [],
  status: undefined,
  operatorId: undefined,
  equipmentId: undefined,
  workTime: undefined, // 将在查询时构建为数组
  workTimeMin: undefined,
  workTimeMax: undefined,
  downtime: undefined, // 将在查询时构建为数组
  downtimeMin: undefined,
  downtimeMax: undefined,
  qualityStatus: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const workOrderList = ref<WorkOrder[]>([]) // 工单列表
const equipmentList = ref<Equipment[]>([]) // 设备列表
const processRouteItemList = ref<ProcessRouteItem[]>([]) // 工艺路线明细列表
const userList = ref<UserVO[]>([]) // 用户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    // 构建查询参数，将工时和停机时间的范围转换为数组
    const params: any = { ...queryParams }
    
    // 处理工时范围：确保数组长度为2或undefined
    if (params.workTimeMin !== undefined || params.workTimeMax !== undefined) {
      const workTimeArray = [params.workTimeMin, params.workTimeMax]
      // 如果两个值都存在，使用数组；否则设为undefined
      if (workTimeArray[0] !== undefined && workTimeArray[1] !== undefined) {
        params.workTime = workTimeArray
      } else {
        params.workTime = undefined
      }
    } else {
      params.workTime = undefined
    }
    
    // 处理停机时间范围：确保数组长度为2或undefined
    if (params.downtimeMin !== undefined || params.downtimeMax !== undefined) {
      const downtimeArray = [params.downtimeMin, params.downtimeMax]
      // 如果两个值都存在，使用数组；否则设为undefined
      if (downtimeArray[0] !== undefined && downtimeArray[1] !== undefined) {
        params.downtime = downtimeArray
      } else {
        params.downtime = undefined
      }
    } else {
      params.downtime = undefined
    }
    
    // 处理日期时间数组：空数组转换为undefined
    if (Array.isArray(params.plannedStartTime) && params.plannedStartTime.length === 0) {
      params.plannedStartTime = undefined
    }
    if (Array.isArray(params.plannedEndTime) && params.plannedEndTime.length === 0) {
      params.plannedEndTime = undefined
    }
    if (Array.isArray(params.actualStartTime) && params.actualStartTime.length === 0) {
      params.actualStartTime = undefined
    }
    if (Array.isArray(params.actualEndTime) && params.actualEndTime.length === 0) {
      params.actualEndTime = undefined
    }
    if (Array.isArray(params.createTime) && params.createTime.length === 0) {
      params.createTime = undefined
    }
    
    // 移除临时字段
    delete params.workTimeMin
    delete params.workTimeMax
    delete params.downtimeMin
    delete params.downtimeMax
    
    const data = await WorkOrderProgressApi.getWorkOrderProgressPage(params)
    list.value = data.list || []
    total.value = data.total || 0
  } catch (error: any) {
    console.error('获取工单进度列表失败:', error)
    // 显示更详细的错误信息
    const errorMsg = error?.response?.data?.msg || error?.message || '获取工单进度列表失败，请稍后重试'
    message.error(errorMsg)
    list.value = []
    total.value = 0
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
    await WorkOrderProgressApi.deleteWorkOrderProgress(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 工单进度 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await WorkOrderProgressApi.deleteWorkOrderProgressList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: WorkOrderProgress[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await WorkOrderProgressApi.exportWorkOrderProgress(queryParams)
    download.excel(data, 'ERP 工单进度.xls')
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

/** 获取设备名称 */
const getEquipmentName = (id?: number) => {
  if (!id) return '-'
  const equipment = equipmentList.value.find(item => item.id === id)
  return equipment?.equipmentName || equipment?.equipmentNo || `设备${id}`
}

/** 获取用户名称 */
const getUserName = (id?: number) => {
  if (!id) return '-'
  const user = userList.value.find(item => item.id === id)
  return user?.nickname || `用户${id}`
}

/** 格式化分钟显示：超过60分钟显示为小时格式 */
const formatMinutes = (minutes?: number | null) => {
  if (minutes === null || minutes === undefined || minutes === 0) return '0分钟'
  
  // 如果超过60分钟，转换为小时和分钟
  if (minutes >= 60) {
    const hours = Math.floor(minutes / 60)
    const mins = minutes % 60
    if (mins > 0) {
      return `${hours}小时${mins}分钟`
    } else {
      return `${hours}小时`
    }
  }
  
  // 小于60分钟，直接显示分钟
  return `${minutes}分钟`
}

/** 初始化 **/
onMounted(async () => {
  // 先加载下拉列表数据，再加载主列表数据
  try {
    const [workOrderData, equipmentData, processRouteItemData, users] = await Promise.all([
      WorkOrderApi.getWorkOrderPage({ pageNo: 1, pageSize: 100 }).catch(err => {
        console.error('加载工单列表失败:', err)
        return { list: [] }
      }),
      EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 }).catch(err => {
        console.error('加载设备列表失败:', err)
        return { list: [] }
      }),
      ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 }).catch(err => {
        console.error('加载工艺路线明细列表失败:', err)
        return { list: [] }
      }),
      UserApi.getSimpleUserList().catch(err => {
        console.error('加载用户列表失败:', err)
        return []
      })
    ])
    workOrderList.value = workOrderData.list || []
    equipmentList.value = equipmentData.list || []
    processRouteItemList.value = processRouteItemData.list || []
    userList.value = users || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
  await getList()
})
</script>