<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="报表编号" prop="reportNo">
        <el-input
          v-model="queryParams.reportNo"
          placeholder="请输入报表编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="报表名称" prop="reportName">
        <el-input
          v-model="queryParams.reportName"
          placeholder="请输入报表名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="报表类型：1-日报，2-周报，3-月报，4-年报" prop="reportType">
        <el-select
          v-model="queryParams.reportType"
          placeholder="请选择报表类型：1-日报，2-周报，3-月报，4-年报"
          clearable
          class="!w-240px"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="报表期间" prop="reportPeriod">
        <el-input
          v-model="queryParams.reportPeriod"
          placeholder="请输入报表期间"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工作中心ID" prop="workCenterId">
        <el-input
          v-model="queryParams.workCenterId"
          placeholder="请输入工作中心ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="生产订单数" prop="productionOrders">
        <el-input
          v-model="queryParams.productionOrders"
          placeholder="请输入生产订单数"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总计划数量" prop="totalPlanQuantity">
        <el-input
          v-model="queryParams.totalPlanQuantity"
          placeholder="请输入总计划数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总完成数量" prop="totalCompletedQuantity">
        <el-input
          v-model="queryParams.totalCompletedQuantity"
          placeholder="请输入总完成数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总合格数量" prop="totalQualifiedQuantity">
        <el-input
          v-model="queryParams.totalQualifiedQuantity"
          placeholder="请输入总合格数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="完成率" prop="completionRate">
        <el-input
          v-model="queryParams.completionRate"
          placeholder="请输入完成率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="合格率" prop="qualityRate">
        <el-input
          v-model="queryParams.qualityRate"
          placeholder="请输入合格率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总工时" prop="totalWorkHours">
        <el-input
          v-model="queryParams.totalWorkHours"
          placeholder="请输入总工时"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总机时" prop="totalEquipmentHours">
        <el-input
          v-model="queryParams.totalEquipmentHours"
          placeholder="请输入总机时"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="OEE" prop="oee">
        <el-input
          v-model="queryParams.oee"
          placeholder="请输入OEE"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="准时交付率" prop="onTimeDeliveryRate">
        <el-input
          v-model="queryParams.onTimeDeliveryRate"
          placeholder="请输入准时交付率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="产值" prop="productionValue">
        <el-input
          v-model="queryParams.productionValue"
          placeholder="请输入产值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="平均成本" prop="averageCost">
        <el-input
          v-model="queryParams.averageCost"
          placeholder="请输入平均成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="报表日期" prop="reportDate">
        <el-date-picker
          v-model="queryParams.reportDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="生成时间" prop="generateTime">
        <el-date-picker
          v-model="queryParams.generateTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="状态：1-草稿，2-已发布" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态：1-草稿，2-已发布"
          clearable
          class="!w-240px"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="详细数据（JSON）" prop="reportData">
        <el-input
          v-model="queryParams.reportData"
          placeholder="请输入详细数据（JSON）"
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
          v-hasPermi="['erp:production-report:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:production-report:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:production-report:delete']"
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
      <el-table-column label="报表编号" align="center" prop="reportNo" />
      <el-table-column label="报表名称" align="center" prop="reportName" />
      <el-table-column label="报表类型：1-日报，2-周报，3-月报，4-年报" align="center" prop="reportType" />
      <el-table-column label="报表期间" align="center" prop="reportPeriod" />
      <el-table-column label="工作中心ID" align="center" prop="workCenterId" />
      <el-table-column label="生产订单数" align="center" prop="productionOrders" />
      <el-table-column label="总计划数量" align="center" prop="totalPlanQuantity" />
      <el-table-column label="总完成数量" align="center" prop="totalCompletedQuantity" />
      <el-table-column label="总合格数量" align="center" prop="totalQualifiedQuantity" />
      <el-table-column label="完成率" align="center" prop="completionRate" />
      <el-table-column label="合格率" align="center" prop="qualityRate" />
      <el-table-column label="总工时" align="center" prop="totalWorkHours" />
      <el-table-column label="总机时" align="center" prop="totalEquipmentHours" />
      <el-table-column label="OEE" align="center" prop="oee" />
      <el-table-column label="准时交付率" align="center" prop="onTimeDeliveryRate" />
      <el-table-column label="产值" align="center" prop="productionValue" />
      <el-table-column label="平均成本" align="center" prop="averageCost" />
      <el-table-column label="报表日期" align="center" prop="reportDate" />
      <el-table-column
        label="生成时间"
        align="center"
        prop="generateTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="状态：1-草稿，2-已发布" align="center" prop="status" />
      <el-table-column label="详细数据（JSON）" align="center" prop="reportData" />
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
            v-hasPermi="['erp:production-report:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:production-report:delete']"
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
  <ProductionReportForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { ProductionReportApi, ProductionReport } from '@/api/erp/productionreport'
import ProductionReportForm from './ProductionReportForm.vue'

/** ERP 生产报表 列表 */
defineOptions({ name: 'ProductionReport' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProductionReport[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  reportNo: undefined,
  reportName: undefined,
  reportType: undefined,
  reportPeriod: undefined,
  workCenterId: undefined,
  productionOrders: undefined,
  totalPlanQuantity: undefined,
  totalCompletedQuantity: undefined,
  totalQualifiedQuantity: undefined,
  completionRate: undefined,
  qualityRate: undefined,
  totalWorkHours: undefined,
  totalEquipmentHours: undefined,
  oee: undefined,
  onTimeDeliveryRate: undefined,
  productionValue: undefined,
  averageCost: undefined,
  reportDate: [],
  generateTime: [],
  status: undefined,
  reportData: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProductionReportApi.getProductionReportPage(queryParams)
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
    await ProductionReportApi.deleteProductionReport(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 生产报表 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ProductionReportApi.deleteProductionReportList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ProductionReport[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProductionReportApi.exportProductionReport(queryParams)
    download.excel(data, 'ERP 生产报表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>