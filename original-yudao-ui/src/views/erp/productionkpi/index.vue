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
      <el-form-item label="KPI编号" prop="kpiNo">
        <el-input
          v-model="queryParams.kpiNo"
          placeholder="请输入KPI编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="KPI名称" prop="kpiName">
        <el-input
          v-model="queryParams.kpiName"
          placeholder="请输入KPI名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="KPI类型：1-OEE，2-合格率，3-达成率，4-交期率" prop="kpiType">
        <el-select
          v-model="queryParams.kpiType"
          placeholder="请选择KPI类型：1-OEE，2-合格率，3-达成率，4-交期率"
          clearable
          class="!w-240px"
        >
          <el-option label="OEE" value="1" />
          <el-option label="合格率" value="2" />
          <el-option label="达成率" value="3" />
          <el-option label="交期率" value="4" />
        </el-select>
      </el-form-item>
      <el-form-item label="分类：1-效率，2-质量，3-交付，4-成本" prop="category">
        <el-input
          v-model="queryParams.category"
          placeholder="请输入分类：1-效率，2-质量，3-交付，4-成本"
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
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="统计周期：1-小时，2-天，3-周，4-月" prop="measurementPeriod">
        <el-input
          v-model="queryParams.measurementPeriod"
          placeholder="请输入统计周期：1-小时，2-天，3-周，4-月"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="目标值" prop="targetValue">
        <el-input
          v-model="queryParams.targetValue"
          placeholder="请输入目标值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="实际值" prop="actualValue">
        <el-input
          v-model="queryParams.actualValue"
          placeholder="请输入实际值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="差异值" prop="variance">
        <el-input
          v-model="queryParams.variance"
          placeholder="请输入差异值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="差异率" prop="varianceRate">
        <el-input
          v-model="queryParams.varianceRate"
          placeholder="请输入差异率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="计算日期" prop="calculationDate">
        <el-date-picker
          v-model="queryParams.calculationDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="周期开始时间" prop="periodStartTime">
        <el-date-picker
          v-model="queryParams.periodStartTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="周期结束时间" prop="periodEndTime">
        <el-date-picker
          v-model="queryParams.periodEndTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="数据来源" prop="dataSource">
        <el-input
          v-model="queryParams.dataSource"
          placeholder="请输入数据来源"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remarks">
        <el-input
          v-model="queryParams.remarks"
          placeholder="请输入备注"
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
          v-hasPermi="['erp:production-kpi:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:production-kpi:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:production-kpi:delete']"
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
      <el-table-column label="KPI编号" align="center" prop="kpiNo" />
      <el-table-column label="KPI名称" align="center" prop="kpiName" />
      <el-table-column label="KPI类型" align="center" prop="kpiType">
        <template #default="scope">
          <el-tag v-if="scope.row.kpiType === 1" type="primary">OEE</el-tag>
          <el-tag v-else-if="scope.row.kpiType === 2" type="success">合格率</el-tag>
          <el-tag v-else-if="scope.row.kpiType === 3" type="info">达成率</el-tag>
          <el-tag v-else-if="scope.row.kpiType === 4" type="warning">交期率</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分类：1-效率，2-质量，3-交付，4-成本" align="center" prop="category" />
      <el-table-column label="工作中心ID" align="center" prop="workCenterId" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="统计周期：1-小时，2-天，3-周，4-月" align="center" prop="measurementPeriod" />
      <el-table-column label="目标值" align="center" prop="targetValue" />
      <el-table-column label="实际值" align="center" prop="actualValue" />
      <el-table-column label="差异值" align="center" prop="variance" />
      <el-table-column label="差异率" align="center" prop="varianceRate" />
      <el-table-column
        label="计算日期"
        align="center"
        prop="calculationDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="周期开始时间"
        align="center"
        prop="periodStartTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="周期结束时间"
        align="center"
        prop="periodEndTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="数据来源" align="center" prop="dataSource" />
      <el-table-column label="备注" align="center" prop="remarks" />
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
            v-hasPermi="['erp:production-kpi:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:production-kpi:delete']"
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
  <ProductionKpiForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductionKpiApi, ProductionKpi } from '@/api/erp/productionkpi'
import ProductionKpiForm from './ProductionKpiForm.vue'

/** ERP 生产KPI 列表 */
defineOptions({ name: 'ProductionKpi' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProductionKpi[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  kpiNo: undefined,
  kpiName: undefined,
  kpiType: undefined,
  category: undefined,
  workCenterId: undefined,
  productId: undefined,
  measurementPeriod: undefined,
  targetValue: undefined,
  actualValue: undefined,
  variance: undefined,
  varianceRate: undefined,
  calculationDate: [],
  periodStartTime: [],
  periodEndTime: [],
  dataSource: undefined,
  remarks: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProductionKpiApi.getProductionKpiPage(queryParams)
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
    await ProductionKpiApi.deleteProductionKpi(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 生产KPI */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ProductionKpiApi.deleteProductionKpiList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ProductionKpi[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProductionKpiApi.exportProductionKpi(queryParams)
    download.excel(data, 'ERP 生产KPI.xls')
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