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
      <el-form-item label="实际成本ID" prop="costActualId">
        <el-input
          v-model="queryParams.costActualId"
          placeholder="请输入实际成本ID"
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
      <el-form-item label="生产数量" prop="productionQuantity">
        <el-input
          v-model="queryParams.productionQuantity"
          placeholder="请输入生产数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="标准总成本" prop="standardTotalCost">
        <el-input
          v-model="queryParams.standardTotalCost"
          placeholder="请输入标准总成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="实际总成本" prop="actualTotalCost">
        <el-input
          v-model="queryParams.actualTotalCost"
          placeholder="请输入实际总成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总差异" prop="totalVariance">
        <el-input
          v-model="queryParams.totalVariance"
          placeholder="请输入总差异"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总差异率" prop="totalVarianceRate">
        <el-input
          v-model="queryParams.totalVarianceRate"
          placeholder="请输入总差异率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="材料成本差异" prop="materialVariance">
        <el-input
          v-model="queryParams.materialVariance"
          placeholder="请输入材料成本差异"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="材料差异率" prop="materialVarianceRate">
        <el-input
          v-model="queryParams.materialVarianceRate"
          placeholder="请输入材料差异率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="人工成本差异" prop="laborVariance">
        <el-input
          v-model="queryParams.laborVariance"
          placeholder="请输入人工成本差异"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="人工差异率" prop="laborVarianceRate">
        <el-input
          v-model="queryParams.laborVarianceRate"
          placeholder="请输入人工差异率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="制造费用差异" prop="overheadVariance">
        <el-input
          v-model="queryParams.overheadVariance"
          placeholder="请输入制造费用差异"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="制造费用差异率" prop="overheadVarianceRate">
        <el-input
          v-model="queryParams.overheadVarianceRate"
          placeholder="请输入制造费用差异率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="差异类型：1-有利，2-不利" prop="varianceType">
        <el-select
          v-model="queryParams.varianceType"
          placeholder="请选择差异类型：1-有利，2-不利"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_COST_VARIANCE_TYPE)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="分析日期" prop="analysisDate">
        <el-date-picker
          v-model="queryParams.analysisDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="差异原因" prop="varianceReason">
        <el-input
          v-model="queryParams.varianceReason"
          placeholder="请输入差异原因"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
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
          v-hasPermi="['erp:cost-variance:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:cost-variance:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:cost-variance:delete']"
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
      <el-table-column label="实际成本ID" align="center" prop="costActualId" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="生产数量" align="center" prop="productionQuantity" />
      <el-table-column label="标准总成本" align="center" prop="standardTotalCost" />
      <el-table-column label="实际总成本" align="center" prop="actualTotalCost" />
      <el-table-column label="总差异" align="center" prop="totalVariance" />
      <el-table-column label="总差异率" align="center" prop="totalVarianceRate" />
      <el-table-column label="材料成本差异" align="center" prop="materialVariance" />
      <el-table-column label="材料差异率" align="center" prop="materialVarianceRate" />
      <el-table-column label="人工成本差异" align="center" prop="laborVariance" />
      <el-table-column label="人工差异率" align="center" prop="laborVarianceRate" />
      <el-table-column label="制造费用差异" align="center" prop="overheadVariance" />
      <el-table-column label="制造费用差异率" align="center" prop="overheadVarianceRate" />
      <el-table-column label="差异类型" align="center" prop="varianceType">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_COST_VARIANCE_TYPE" :value="scope.row.varianceType" />
        </template>
      </el-table-column>
      <el-table-column
        label="分析日期"
        align="center"
        prop="analysisDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="差异原因" align="center" prop="varianceReason" />
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
            v-hasPermi="['erp:cost-variance:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:cost-variance:delete']"
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
  <CostVarianceForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { CostVarianceApi, CostVariance } from '@/api/erp/costvariance'
import CostVarianceForm from './CostVarianceForm.vue'

/** ERP 成本差异分析 列表 */
defineOptions({ name: 'CostVariance' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CostVariance[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  costActualId: undefined,
  productId: undefined,
  productionQuantity: undefined,
  standardTotalCost: undefined,
  actualTotalCost: undefined,
  totalVariance: undefined,
  totalVarianceRate: undefined,
  materialVariance: undefined,
  materialVarianceRate: undefined,
  laborVariance: undefined,
  laborVarianceRate: undefined,
  overheadVariance: undefined,
  overheadVarianceRate: undefined,
  varianceType: undefined,
  analysisDate: [],
  varianceReason: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CostVarianceApi.getCostVariancePage(queryParams)
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
    await CostVarianceApi.deleteCostVariance(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 成本差异分析 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await CostVarianceApi.deleteCostVarianceList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: CostVariance[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CostVarianceApi.exportCostVariance(queryParams)
    download.excel(data, 'ERP 成本差异分析.xls')
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