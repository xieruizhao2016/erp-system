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
      <el-form-item label="运算批次号" prop="runNo">
        <el-input
          v-model="queryParams.runNo"
          placeholder="请输入运算批次号"
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
      <el-form-item label="仓库ID" prop="warehouseId">
        <el-input
          v-model="queryParams.warehouseId"
          placeholder="请输入仓库ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
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
      <el-form-item label="订单类型：1-生产订单，2-采购订单" prop="orderType">
        <el-select
          v-model="queryParams.orderType"
          placeholder="请选择订单类型：1-生产订单，2-采购订单"
          clearable
          class="!w-240px"
        >
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="批量规则：1-固定批量，2-按需，3-最小-最大" prop="lotSizingRule">
        <el-input
          v-model="queryParams.lotSizingRule"
          placeholder="请输入批量规则：1-固定批量，2-按需，3-最小-最大"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="提前期（天）" prop="leadTime">
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
      <el-form-item label="订单状态：1-建议，2-确认，3-下达" prop="orderStatus">
        <el-select
          v-model="queryParams.orderStatus"
          placeholder="请选择订单状态：1-建议，2-确认，3-下达"
          clearable
          class="!w-240px"
        >
          <el-option label="请选择字典生成" value="" />
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
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="仓库ID" align="center" prop="warehouseId" />
      <el-table-column label="周期开始日期" align="center" prop="periodStartDate" />
      <el-table-column label="周期结束日期" align="center" prop="periodEndDate" />
      <el-table-column label="毛需求" align="center" prop="grossRequirement" />
      <el-table-column label="计划接收量" align="center" prop="scheduledReceipts" />
      <el-table-column label="现有库存" align="center" prop="onHandInventory" />
      <el-table-column label="净需求" align="center" prop="netRequirement" />
      <el-table-column label="计划订单接收量" align="center" prop="plannedOrderReceipts" />
      <el-table-column label="计划订单发放量" align="center" prop="plannedOrderReleases" />
      <el-table-column label="订单类型：1-生产订单，2-采购订单" align="center" prop="orderType" />
      <el-table-column label="批量规则：1-固定批量，2-按需，3-最小-最大" align="center" prop="lotSizingRule" />
      <el-table-column label="提前期（天）" align="center" prop="leadTime" />
      <el-table-column label="安全库存" align="center" prop="safetyStock" />
      <el-table-column label="订单状态：1-建议，2-确认，3-下达" align="center" prop="orderStatus" />
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
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { MrpResultApi, MrpResult } from '@/api/erp/mrpresult'
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
    currentRow.value = {}
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

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>