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
      <el-form-item label="运算批次号" prop="runNo">
        <el-input
          v-model="queryParams.runNo"
          placeholder="请输入运算批次号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
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
      <el-form-item label="仓库" prop="warehouseId">
        <el-select
          v-model="queryParams.warehouseId"
          placeholder="请选择仓库"
          clearable
          filterable
          class="!w-240px"
        >
          <el-option
            v-for="item in warehouseList"
            :key="item.id"
            :label="item.name || `仓库${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="周期开始日期" prop="periodStartDate">
        <el-date-picker
          v-model="queryParams.periodStartDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="周期结束日期" prop="periodEndDate">
        <el-date-picker
          v-model="queryParams.periodEndDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="毛需求" prop="grossRequirement">
        <el-input
          v-model="queryParams.grossRequirement"
          placeholder="请输入毛需求"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="计划接收量" prop="scheduledReceipts">
        <el-input
          v-model="queryParams.scheduledReceipts"
          placeholder="请输入计划接收量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="现有库存" prop="onHandInventory">
        <el-input
          v-model="queryParams.onHandInventory"
          placeholder="请输入现有库存"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="净需求" prop="netRequirement">
        <el-input
          v-model="queryParams.netRequirement"
          placeholder="请输入净需求"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="计划订单接收量" prop="plannedOrderReceipts">
        <el-input
          v-model="queryParams.plannedOrderReceipts"
          placeholder="请输入计划订单接收量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="计划订单发放量" prop="plannedOrderReleases">
        <el-input
          v-model="queryParams.plannedOrderReleases"
          placeholder="请输入计划订单发放量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="订单类型" prop="orderType">
        <el-select
          v-model="queryParams.orderType"
          placeholder="请选择订单类型"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_ORDER_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="批量规则" prop="lotSizingRule">
        <el-select v-model="queryParams.lotSizingRule" placeholder="请选择批量规则" clearable class="!w-240px">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_LOT_SIZING_RULE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="提前期" prop="leadTime">
        <el-date-picker
          v-model="queryParams.leadTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="安全库存" prop="safetyStock">
        <el-input
          v-model="queryParams.safetyStock"
          placeholder="请输入安全库存"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="订单状态" prop="orderStatus">
        <el-select
          v-model="queryParams.orderStatus"
          placeholder="请选择订单状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_MRP_ORDER_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="需求日期" prop="dueDate">
        <el-date-picker
          v-model="queryParams.dueDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
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
          type="warning"
          plain
          @click="openExecuteDialog"
          v-hasPermi="['erp:mrp-result:execute']"
        >
          <Icon icon="ep:cpu" class="mr-5px" /> 执行MRP运算
        </el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['erp:mrp-result:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:mrp-result:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:mrp-result:delete']"
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
      <el-table-column label="运算批次号" align="center" prop="runNo" />
      <el-table-column label="产品名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductName(scope.row.productId) }}
        </template>
      </el-table-column>
      <el-table-column label="仓库名称" align="center" min-width="120">
        <template #default="scope">
          {{ getWarehouseName(scope.row.warehouseId) }}
        </template>
      </el-table-column>
      <el-table-column label="周期开始日期" align="center" prop="periodStartDate" />
      <el-table-column label="周期结束日期" align="center" prop="periodEndDate" />
      <el-table-column label="毛需求" align="center" prop="grossRequirement" />
      <el-table-column label="计划接收量" align="center" prop="scheduledReceipts" />
      <el-table-column label="现有库存" align="center" prop="onHandInventory" />
      <el-table-column label="净需求" align="center" prop="netRequirement" />
      <el-table-column label="计划订单接收量" align="center" prop="plannedOrderReceipts" />
      <el-table-column label="计划订单发放量" align="center" prop="plannedOrderReleases" />
      <el-table-column label="订单类型" align="center" prop="orderType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_MRP_ORDER_TYPE" :value="scope.row.orderType" />
        </template>
      </el-table-column>
      <el-table-column label="批量规则" align="center" prop="lotSizingRule">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_MRP_LOT_SIZING_RULE" :value="scope.row.lotSizingRule" />
        </template>
      </el-table-column>
      <el-table-column label="提前期" align="center" prop="leadTime" />
      <el-table-column label="安全库存" align="center" prop="safetyStock" />
      <el-table-column label="订单状态" align="center" prop="orderStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_MRP_ORDER_STATUS" :value="scope.row.orderStatus" />
        </template>
      </el-table-column>
      <el-table-column label="需求日期" align="center" prop="dueDate" />
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
            v-hasPermi="['erp:mrp-result:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:mrp-result:delete']"
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
  <MrpResultForm ref="formRef" @success="getList" />
  
  <!-- MRP运算参数对话框 -->
  <Dialog title="执行MRP运算" v-model="executeDialogVisible" width="600px">
    <el-form
      ref="executeFormRef"
      :model="executeFormData"
      :rules="executeFormRules"
      label-width="120px"
      v-loading="executeLoading"
    >
      <el-form-item label="计划开始日期" prop="planStartDate">
        <el-date-picker
          v-model="executeFormData.planStartDate"
          type="datetime"
          value-format="x"
          placeholder="选择计划开始日期"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="计划结束日期" prop="planEndDate">
        <el-date-picker
          v-model="executeFormData.planEndDate"
          type="datetime"
          value-format="x"
          placeholder="选择计划结束日期"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="包含产品" prop="productIds">
        <el-select
          v-model="executeFormData.productIds"
          placeholder="请选择产品（不选则计算所有）"
          clearable
          filterable
          multiple
          style="width: 100%"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name || `产品${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="考虑安全库存" prop="considerSafetyStock">
        <el-switch v-model="executeFormData.considerSafetyStock" />
      </el-form-item>
      <el-form-item label="考虑在途数量" prop="considerInTransit">
        <el-switch v-model="executeFormData.considerInTransit" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleExecuteMrp" type="primary" :loading="executeLoading" :disabled="executeLoading">
        <Icon icon="ep:cpu" class="mr-5px" /> 开始运算
      </el-button>
      <el-button @click="executeDialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
  
  <!-- MRP运算结果对话框 -->
  <Dialog title="MRP运算结果" v-model="resultDialogVisible" width="1200px">
    <el-descriptions :column="2" border>
      <el-descriptions-item label="运算批次号">{{ mrpResult?.runNo }}</el-descriptions-item>
      <el-descriptions-item label="运算状态">
        <el-tag :type="mrpResult?.success ? 'success' : 'danger'">
          {{ mrpResult?.success ? '成功' : '失败' }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="运算耗时">{{ mrpResult?.calculationTime }}ms</el-descriptions-item>
      <el-descriptions-item label="处理产品数">{{ mrpResult?.statistics?.totalProducts }}</el-descriptions-item>
      <el-descriptions-item label="需要生产">{{ mrpResult?.statistics?.needProductionCount }}</el-descriptions-item>
      <el-descriptions-item label="需要采购">{{ mrpResult?.statistics?.needPurchaseCount }}</el-descriptions-item>
      <el-descriptions-item label="库存充足">{{ mrpResult?.statistics?.sufficientStockCount }}</el-descriptions-item>
      <el-descriptions-item label="运算消息" :span="2">{{ mrpResult?.message }}</el-descriptions-item>
    </el-descriptions>
    
    <el-divider content-position="left">运算明细</el-divider>
    
    <el-table :data="mrpResult?.results" border style="width: 100%" max-height="400">
      <el-table-column prop="productName" label="产品名称" min-width="120" />
      <el-table-column prop="grossRequirement" label="毛需求" width="100" align="right" />
      <el-table-column prop="onHandInventory" label="现有库存" width="100" align="right" />
      <el-table-column prop="scheduledReceipts" label="计划接收" width="100" align="right" />
      <el-table-column prop="netRequirement" label="净需求" width="100" align="right" />
      <el-table-column prop="plannedOrderReleases" label="计划订单" width="100" align="right" />
      <el-table-column prop="orderType" label="订单类型" width="100" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.orderType === 1 ? 'primary' : 'success'">
            {{ scope.row.orderType === 1 ? '生产订单' : '采购订单' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="suggestion" label="建议操作" min-width="200" />
    </el-table>
    
    <template #footer>
      <el-button @click="resultDialogVisible = false">关 闭</el-button>
      <el-button type="primary" @click="handleViewResults">查看运算结果</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { MrpResultApi, MrpResult } from '@/api/erp/mrpresult'
import { ProductApi } from '@/api/erp/product/product'
import { WarehouseApi } from '@/api/erp/stock/warehouse'
import MrpResultForm from './MrpResultForm.vue'

/** ERP MRP运算结果 列表 */
defineOptions({ name: 'MrpResult' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<MrpResult[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  runNo: undefined,
  productId: undefined,
  warehouseId: undefined,
  periodStartDate: [],
  periodEndDate: [],
  grossRequirement: undefined,
  scheduledReceipts: undefined,
  onHandInventory: undefined,
  netRequirement: undefined,
  plannedOrderReceipts: undefined,
  plannedOrderReleases: undefined,
  orderType: undefined,
  lotSizingRule: undefined,
  leadTime: [],
  safetyStock: undefined,
  orderStatus: undefined,
  dueDate: [],
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

// 数据列表
const productList = ref<any[]>([]) // 产品列表
const warehouseList = ref<any[]>([]) // 仓库列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await MrpResultApi.getMrpResultPage(queryParams)
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
    await MrpResultApi.deleteMrpResult(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP MRP运算结果 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await MrpResultApi.deleteMrpResultList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: MrpResult[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await MrpResultApi.exportMrpResult(queryParams)
    download.excel(data, 'ERP MRP运算结果.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// ==================== MRP运算相关 ====================

// MRP运算对话框
const executeDialogVisible = ref(false)
const executeLoading = ref(false)
const executeFormRef = ref()
const executeFormData = ref({
  planStartDate: undefined,
  planEndDate: undefined,
  productIds: [],
  considerSafetyStock: true,
  considerInTransit: true
})
const executeFormRules = reactive({
  planStartDate: [{ required: true, message: '计划开始日期不能为空', trigger: 'change' }],
  planEndDate: [{ required: true, message: '计划结束日期不能为空', trigger: 'change' }]
})

// MRP运算结果对话框
const resultDialogVisible = ref(false)
const mrpResult = ref<any>(null)

/** 打开MRP运算对话框 */
const openExecuteDialog = () => {
  // 设置默认日期：当前日期到3个月后
  const now = new Date()
  const threeMonthsLater = new Date()
  threeMonthsLater.setMonth(now.getMonth() + 3)
  
  executeFormData.value = {
    planStartDate: now.getTime(),
    planEndDate: threeMonthsLater.getTime(),
    productIds: [],
    considerSafetyStock: true,
    considerInTransit: true
  }
  executeDialogVisible.value = true
}

/** 执行MRP运算 */
const handleExecuteMrp = async () => {
  // 校验表单
  await executeFormRef.value.validate()
  
  try {
    executeLoading.value = true
    
    // 转换时间格式
    const requestData = {
      planStartDate: new Date(executeFormData.value.planStartDate).toISOString().replace('T', ' ').substring(0, 19),
      planEndDate: new Date(executeFormData.value.planEndDate).toISOString().replace('T', ' ').substring(0, 19),
      productIds: executeFormData.value.productIds.length > 0 ? executeFormData.value.productIds : null,
      considerSafetyStock: executeFormData.value.considerSafetyStock,
      considerInTransit: executeFormData.value.considerInTransit
    }
    
    // 调用API
    const result = await MrpResultApi.executeMrpCalculation(requestData)
    mrpResult.value = result
    
    // 关闭参数对话框，打开结果对话框
    executeDialogVisible.value = false
    resultDialogVisible.value = true
    
    if (result.success) {
      message.success('MRP运算完成')
    } else {
      message.error(result.message || 'MRP运算失败')
    }
  } catch (error) {
    console.error('MRP运算失败:', error)
    message.error('MRP运算失败')
  } finally {
    executeLoading.value = false
  }
}

/** 查看运算结果 */
const handleViewResults = () => {
  resultDialogVisible.value = false
  // 刷新列表，显示新的运算结果
  getList()
}

// 加载数据列表
const loadListData = async () => {
  try {
    // 加载产品列表
    const productData = await ProductApi.getProductPage({ pageNo: 1, pageSize: 100 })
    productList.value = productData.list || []

    // 加载仓库列表
    const warehouseData = await WarehouseApi.getWarehousePage({ pageNo: 1, pageSize: 100 })
    warehouseList.value = warehouseData.list || []
  } catch (error) {
    console.error('加载数据列表失败:', error)
  }
}

// 辅助函数：获取产品名称
const getProductName = (productId: number | undefined) => {
  if (!productId) return '-'
  const product = productList.value.find(item => item.id === productId)
  return product ? product.name || `产品${productId}` : `产品${productId}`
}

// 辅助函数：获取仓库名称
const getWarehouseName = (warehouseId: number | undefined) => {
  if (!warehouseId) return '-'
  const warehouse = warehouseList.value.find(item => item.id === warehouseId)
  return warehouse ? warehouse.name || `仓库${warehouseId}` : `仓库${warehouseId}`
}

/** 初始化 **/
onMounted(() => {
  loadListData()
  getList()
})
</script>