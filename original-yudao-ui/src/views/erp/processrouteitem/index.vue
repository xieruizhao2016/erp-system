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
      <el-form-item label="工艺路线" prop="routeId">
        <el-select
          v-model="queryParams.routeId"
          clearable
          filterable
          placeholder="请选择工艺路线"
          class="!w-240px"
        >
          <el-option
            v-for="item in processRouteList"
            :key="item.id"
            :label="item.routeName || item.routeNo || `路线${item.id}`"
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
      <el-form-item label="序号" prop="sequence">
        <el-input
          v-model="queryParams.sequence"
          placeholder="请输入序号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="标准工时" prop="standardTime">
        <el-date-picker
          v-model="queryParams.standardTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="准备时间" prop="setupTime">
        <el-date-picker
          v-model="queryParams.setupTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
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
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input
          v-model="queryParams.workCenterId"
          placeholder="请输入工作中心"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="是否瓶颈工序" prop="isBottleneck">
        <el-select
          v-model="queryParams.isBottleneck"
          placeholder="请选择是否瓶颈工序"
          clearable
          class="!w-240px"
        >
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否需要质检" prop="qualityCheckRequired">
        <el-select v-model="queryParams.qualityCheckRequired" placeholder="请选择是否需要质检" clearable class="!w-240px">
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
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
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:process-route-item:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:process-route-item:delete']"
        >
          <Icon icon="ep:delete" class="mr-5px" /> 批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 提示信息 -->
  <ContentWrap>
    <el-alert
      type="info"
      :closable="false"
      show-icon
    >
      <template #title>
        <span>💡 提示：新增工序明细请在「工艺路线」主表单的「工序明细」标签页中操作，此处主要用于查看、编辑和删除已有明细。</span>
      </template>
    </el-alert>
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
      <el-table-column label="工艺路线名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProcessRouteName(scope.row.routeId) }}
        </template>
      </el-table-column>
      <el-table-column label="工序名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProcessName(scope.row.processId) }}
        </template>
      </el-table-column>
      <el-table-column label="序号" align="center" prop="sequence" />
      <el-table-column label="工序名称" align="center" prop="operationName" />
      <el-table-column label="标准工时" align="center" prop="standardTime" />
      <el-table-column label="准备时间" align="center" prop="setupTime" />
      <el-table-column label="人员数量" align="center" prop="workerCount" />
      <el-table-column label="设备名称" align="center" min-width="120">
        <template #default="scope">
          {{ getEquipmentName(scope.row.equipmentId) }}
        </template>
      </el-table-column>
      <el-table-column label="工作中心" align="center" prop="workCenterId" />
      <el-table-column label="人工费率" align="center" prop="laborRate" />
      <el-table-column label="制造费率" align="center" prop="overheadRate" />
      <el-table-column label="是否瓶颈工序" align="center" prop="isBottleneck" />
      <el-table-column label="是否需要质检" align="center" prop="qualityCheckRequired" />
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
            v-hasPermi="['erp:process-route-item:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:process-route-item:delete']"
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
  <ProcessRouteItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProcessRouteItemApi, ProcessRouteItem } from '@/api/erp/processrouteitem'
import { ProcessRouteApi } from '@/api/erp/processroute'
import { EquipmentApi } from '@/api/erp/equipment'
import ProcessRouteItemForm from './ProcessRouteItemForm.vue'

/** ERP 工艺路线明细 列表 */
defineOptions({ name: 'ProcessRouteItem' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProcessRouteItem[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  routeId: undefined,
  processId: undefined,
  sequence: undefined,
  standardTime: [],
  setupTime: [],
  equipmentId: undefined,
  workCenterId: undefined,
  isBottleneck: undefined,
  qualityCheckRequired: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

// 数据列表
const processRouteList = ref<any[]>([]) // 工艺路线列表
const processRouteItemList = ref<any[]>([]) // 工艺路线明细列表（用于工序）
const equipmentList = ref<any[]>([]) // 设备列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProcessRouteItemApi.getProcessRouteItemPage(queryParams)
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
    await ProcessRouteItemApi.deleteProcessRouteItem(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 工艺路线明细 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ProcessRouteItemApi.deleteProcessRouteItemList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ProcessRouteItem[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProcessRouteItemApi.exportProcessRouteItem(queryParams)
    download.excel(data, 'ERP 工艺路线明细.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 加载数据列表
const loadListData = async () => {
  try {
    // 加载工艺路线列表
    const processRouteData = await ProcessRouteApi.getProcessRoutePage({ pageNo: 1, pageSize: 100 })
    processRouteList.value = processRouteData.list || []

    // 加载工艺路线明细列表（用于工序）
    const processRouteItemData = await ProcessRouteItemApi.getProcessRouteItemPage({ pageNo: 1, pageSize: 100 })
    processRouteItemList.value = processRouteItemData.list || []

    // 加载设备列表
    const equipmentData = await EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 })
    equipmentList.value = equipmentData.list || []
  } catch (error) {
    console.error('加载数据列表失败:', error)
  }
}

// 辅助函数：获取工艺路线名称
const getProcessRouteName = (routeId: number | undefined) => {
  if (!routeId) return '-'
  const route = processRouteList.value.find(item => item.id === routeId)
  return route ? route.name || route.routeName || `工艺路线${routeId}` : `工艺路线${routeId}`
}

// 辅助函数：获取工序名称
const getProcessName = (processId: number | undefined) => {
  if (!processId) return '-'
  const process = processRouteItemList.value.find(item => item.id === processId)
  return process ? process.operationName || `工序${processId}` : `工序${processId}`
}

// 辅助函数：获取设备名称
const getEquipmentName = (equipmentId: number | undefined) => {
  if (!equipmentId) return '-'
  const equipment = equipmentList.value.find(item => item.id === equipmentId)
  return equipment ? equipment.name || equipment.equipmentName || `设备${equipmentId}` : `设备${equipmentId}`
}

/** 初始化 **/
onMounted(() => {
  loadListData()
  getList()
})
</script>