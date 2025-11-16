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
      <el-form-item label="成本单号" prop="costNo">
        <el-input
          v-model="queryParams.costNo"
          placeholder="请输入成本单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工单ID" prop="workOrderId">
        <el-input
          v-model="queryParams.workOrderId"
          placeholder="请输入工单ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="生产订单ID" prop="productionOrderId">
        <el-input
          v-model="queryParams.productionOrderId"
          placeholder="请输入生产订单ID"
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
      <el-form-item label="材料成本" prop="materialCost">
        <el-input
          v-model="queryParams.materialCost"
          placeholder="请输入材料成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="材料成本调整" prop="materialCostAdjust">
        <el-input
          v-model="queryParams.materialCostAdjust"
          placeholder="请输入材料成本调整"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="人工成本" prop="laborCost">
        <el-input
          v-model="queryParams.laborCost"
          placeholder="请输入人工成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="人工成本调整" prop="laborCostAdjust">
        <el-input
          v-model="queryParams.laborCostAdjust"
          placeholder="请输入人工成本调整"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="制造费用" prop="overheadCost">
        <el-input
          v-model="queryParams.overheadCost"
          placeholder="请输入制造费用"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="制造费用调整" prop="overheadCostAdjust">
        <el-input
          v-model="queryParams.overheadCostAdjust"
          placeholder="请输入制造费用调整"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="总成本" prop="totalCost">
        <el-input
          v-model="queryParams.totalCost"
          placeholder="请输入总成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="单位成本" prop="unitCost">
        <el-input
          v-model="queryParams.unitCost"
          placeholder="请输入单位成本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="成本币种" prop="costCurrency">
        <el-input
          v-model="queryParams.costCurrency"
          placeholder="请输入成本币种"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="成本期间（YYYY-MM）" prop="costPeriod">
        <el-input
          v-model="queryParams.costPeriod"
          placeholder="请输入成本期间（YYYY-MM）"
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
      <el-form-item label="最后调整日期" prop="lastAdjustDate">
        <el-date-picker
          v-model="queryParams.lastAdjustDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="状态：1-草稿，2-已计算，3-已确认" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态：1-草稿，2-已计算，3-已确认"
          clearable
          class="!w-240px"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
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
          v-hasPermi="['erp:cost-actual:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:cost-actual:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:cost-actual:delete']"
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
      <el-table-column label="成本单号" align="center" prop="costNo" />
      <el-table-column label="工单ID" align="center" prop="workOrderId" />
      <el-table-column label="生产订单ID" align="center" prop="productionOrderId" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="生产数量" align="center" prop="productionQuantity" />
      <el-table-column label="材料成本" align="center" prop="materialCost" />
      <el-table-column label="材料成本调整" align="center" prop="materialCostAdjust" />
      <el-table-column label="人工成本" align="center" prop="laborCost" />
      <el-table-column label="人工成本调整" align="center" prop="laborCostAdjust" />
      <el-table-column label="制造费用" align="center" prop="overheadCost" />
      <el-table-column label="制造费用调整" align="center" prop="overheadCostAdjust" />
      <el-table-column label="总成本" align="center" prop="totalCost" />
      <el-table-column label="单位成本" align="center" prop="unitCost" />
      <el-table-column label="成本币种" align="center" prop="costCurrency" />
      <el-table-column label="成本期间（YYYY-MM）" align="center" prop="costPeriod" />
      <el-table-column
        label="计算日期"
        align="center"
        prop="calculationDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="最后调整日期"
        align="center"
        prop="lastAdjustDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="状态：1-草稿，2-已计算，3-已确认" align="center" prop="status" />
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
            v-hasPermi="['erp:cost-actual:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:cost-actual:delete']"
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
  <CostActualForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { CostActualApi, CostActual } from '@/api/erp/costactual'
import CostActualForm from './CostActualForm.vue'

/** ERP 实际成本 列表 */
defineOptions({ name: 'CostActual' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CostActual[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  costNo: undefined,
  workOrderId: undefined,
  productionOrderId: undefined,
  productId: undefined,
  productionQuantity: undefined,
  materialCost: undefined,
  materialCostAdjust: undefined,
  laborCost: undefined,
  laborCostAdjust: undefined,
  overheadCost: undefined,
  overheadCostAdjust: undefined,
  totalCost: undefined,
  unitCost: undefined,
  costCurrency: undefined,
  costPeriod: undefined,
  calculationDate: [],
  lastAdjustDate: [],
  status: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CostActualApi.getCostActualPage(queryParams)
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
    await CostActualApi.deleteCostActual(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 实际成本 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await CostActualApi.deleteCostActualList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: CostActual[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CostActualApi.exportCostActual(queryParams)
    download.excel(data, 'ERP 实际成本.xls')
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