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
      <el-form-item label="排程" prop="scheduleId">
        <el-select
          v-model="queryParams.scheduleId"
          placeholder="请选择排程"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in productionScheduleList"
            :key="item.id"
            :label="item.scheduleName || item.scheduleNo || `排程${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="生产订单" prop="productionOrderId">
        <el-select
          v-model="queryParams.productionOrderId"
          placeholder="请选择生产订单"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in productionOrderList"
            :key="item.id"
            :label="item.no || `生产订单${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="产品" prop="productId">
        <el-select
          v-model="queryParams.productId"
          placeholder="请选择产品"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name || `产品${item.id}`"
            :value="item.id"
          />
        </el-select>
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
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input
          v-model="queryParams.workCenterId"
          placeholder="请输入工作中心"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="设备" prop="equipmentId">
        <el-select
          v-model="queryParams.equipmentId"
          placeholder="请选择设备"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in equipmentList"
            :key="item.id"
            :label="item.name || item.equipmentName || `设备${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工序序号" prop="processSequence">
        <el-input
          v-model="queryParams.processSequence"
          placeholder="请输入工序序号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-input
          v-model="queryParams.priority"
          placeholder="请输入优先级"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
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
      <el-form-item label="运行时间" prop="runTime">
        <el-date-picker
          v-model="queryParams.runTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="等待时间" prop="waitTime">
        <el-date-picker
          v-model="queryParams.waitTime"
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
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_PRODUCTION_SCHEDULE_ITEM_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      <el-form-item label="延迟原因" prop="delayReason">
        <el-input
          v-model="queryParams.delayReason"
          placeholder="请输入延迟原因"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
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
          v-hasPermi="['erp:production-schedule-item:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:production-schedule-item:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:production-schedule-item:delete']"
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
      <el-table-column label="排程名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductionScheduleName(scope.row.scheduleId) }}
        </template>
      </el-table-column>
      <el-table-column label="生产订单单号" align="center" min-width="120">
        <template #default="scope">
          {{ getProductionOrderName(scope.row.productionOrderId) }}
        </template>
      </el-table-column>
      <el-table-column label="产品名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductName(scope.row.productId) }}
        </template>
      </el-table-column>
      <el-table-column label="排程数量" align="center" prop="quantity" />
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
      <el-table-column label="工作中心" align="center" prop="workCenterId" />
      <el-table-column label="设备名称" align="center" min-width="120">
        <template #default="scope">
          {{ getEquipmentName(scope.row.equipmentId) }}
        </template>
      </el-table-column>
      <el-table-column label="工序序号" align="center" prop="processSequence" />
      <el-table-column label="优先级" align="center" prop="priority" />
      <el-table-column label="准备时间" align="center" prop="setupTime" />
      <el-table-column label="运行时间" align="center" prop="runTime" />
      <el-table-column label="等待时间" align="center" prop="waitTime" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_PRODUCTION_SCHEDULE_ITEM_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
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
      <el-table-column label="完成率" align="center" prop="completionRate" />
      <el-table-column label="延迟原因" align="center" prop="delayReason" />
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
            v-hasPermi="['erp:production-schedule-item:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:production-schedule-item:delete']"
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
  <ProductionScheduleItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductionScheduleItemApi, ProductionScheduleItem } from '@/api/erp/productionscheduleitem'
import { ProductionScheduleApi } from '@/api/erp/productionschedule'
import { ProductionOrderApi } from '@/api/erp/productionorder'
import { ProductApi } from '@/api/erp/product/product'
import { EquipmentApi } from '@/api/erp/equipment'
import ProductionScheduleItemForm from './ProductionScheduleItemForm.vue'

/** ERP 排程明细 列表 */
defineOptions({ name: 'ProductionScheduleItem' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProductionScheduleItem[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  scheduleId: undefined,
  productionOrderId: undefined,
  productId: undefined,
  plannedStartTime: [],
  plannedEndTime: [],
  workCenterId: undefined,
  equipmentId: undefined,
  processSequence: undefined,
  priority: undefined,
  setupTime: [],
  runTime: [],
  waitTime: [],
  status: undefined,
  actualStartTime: [],
  actualEndTime: [],
  completionRate: undefined,
  delayReason: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

// 数据列表
const productionScheduleList = ref<any[]>([]) // 生产排程列表
const productionOrderList = ref<any[]>([]) // 生产订单列表
const productList = ref<any[]>([]) // 产品列表
const equipmentList = ref<any[]>([]) // 设备列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProductionScheduleItemApi.getProductionScheduleItemPage(queryParams)
    list.value = data.list || []
    total.value = data.total || 0
  } catch (error) {
    console.error('获取排程明细列表失败:', error)
    message.error('获取排程明细列表失败，请稍后重试')
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
    await ProductionScheduleItemApi.deleteProductionScheduleItem(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 排程明细 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ProductionScheduleItemApi.deleteProductionScheduleItemList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ProductionScheduleItem[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProductionScheduleItemApi.exportProductionScheduleItem(queryParams)
    download.excel(data, 'ERP 排程明细.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 加载数据列表
const loadListData = async () => {
  // 并行加载所有下拉列表数据，单个失败不影响其他
  const loadPromises = [
    // 加载生产排程列表
    ProductionScheduleApi.getProductionSchedulePage({ pageNo: 1, pageSize: 100 })
      .then(data => {
        productionScheduleList.value = data.list || []
      })
      .catch(error => {
        console.error('加载生产排程列表失败:', error)
        productionScheduleList.value = []
      }),

    // 加载生产订单列表
    ProductionOrderApi.getProductionOrderPage({ pageNo: 1, pageSize: 100 })
      .then(data => {
        productionOrderList.value = data.list || []
      })
      .catch(error => {
        console.error('加载生产订单列表失败:', error)
        productionOrderList.value = []
      }),

    // 加载产品列表
    ProductApi.getProductPage({ pageNo: 1, pageSize: 100 })
      .then(data => {
        productList.value = data.list || []
      })
      .catch(error => {
        console.error('加载产品列表失败:', error)
        productList.value = []
      }),

    // 加载设备列表
    EquipmentApi.getEquipmentPage({ pageNo: 1, pageSize: 100 })
      .then(data => {
        equipmentList.value = data.list || []
      })
      .catch(error => {
        console.error('加载设备列表失败:', error)
        equipmentList.value = []
      })
  ]
  
  // 等待所有请求完成（无论成功或失败）
  await Promise.allSettled(loadPromises)
}

// 辅助函数：获取生产排程名称
const getProductionScheduleName = (scheduleId: number | undefined) => {
  if (!scheduleId) return '-'
  const schedule = productionScheduleList.value.find(item => item.id === scheduleId)
  return schedule ? schedule.scheduleName || schedule.scheduleNo || `排程${scheduleId}` : `排程${scheduleId}`
}

// 辅助函数：获取生产订单单号
const getProductionOrderName = (productionOrderId: number | undefined) => {
  if (!productionOrderId) return '-'
  const order = productionOrderList.value.find(item => item.id === productionOrderId)
  return order ? order.no || `生产订单${productionOrderId}` : `生产订单${productionOrderId}`
}

// 辅助函数：获取产品名称
const getProductName = (productId: number | undefined) => {
  if (!productId) return '-'
  const product = productList.value.find(item => item.id === productId)
  return product ? product.name || `产品${productId}` : `产品${productId}`
}

// 辅助函数：获取设备名称
const getEquipmentName = (equipmentId: number | undefined) => {
  if (!equipmentId) return '-'
  const equipment = equipmentList.value.find(item => item.id === equipmentId)
  return equipment ? equipment.name || equipment.equipmentName || `设备${equipmentId}` : `设备${equipmentId}`
}

/** 初始化 **/
onMounted(async () => {
  // 先加载下拉列表数据，再加载主列表数据
  try {
    await loadListData()
  } catch (error) {
    console.error('加载下拉列表数据失败:', error)
    // 下拉列表加载失败不影响主列表加载
  }
  await getList()
})
</script>