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
      <el-form-item label="产品" prop="productId">
        <el-select
          v-model="queryParams.productId"
          clearable
          filterable
          placeholder="请选择产品"
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="版本号" prop="version">
        <el-input
          v-model="queryParams.version"
          placeholder="请输入版本号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="生效日期" prop="effectiveDate">
        <el-date-picker
          v-model="queryParams.effectiveDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="失效日期" prop="expireDate">
        <el-date-picker
          v-model="queryParams.expireDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
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
      <el-form-item label="人工成本" prop="laborCost">
        <el-input
          v-model="queryParams.laborCost"
          placeholder="请输入人工成本"
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
      <el-form-item label="总成本" prop="totalCost">
        <el-input
          v-model="queryParams.totalCost"
          placeholder="请输入总成本"
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
      <el-form-item label="关联BOM版本" prop="bomVersion">
        <el-input
          v-model="queryParams.bomVersion"
          placeholder="请输入关联BOM版本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="关联工艺版本" prop="routeVersion">
        <el-input
          v-model="queryParams.routeVersion"
          placeholder="请输入关联工艺版本"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
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
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_COST_STANDARD_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
          v-hasPermi="['erp:cost-standard:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:cost-standard:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:cost-standard:delete']"
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
      <el-table-column label="产品名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductName(scope.row.productId) }}
        </template>
      </el-table-column>
      <el-table-column label="版本号" align="center" prop="version" />
      <el-table-column
        label="生效日期"
        align="center"
        prop="effectiveDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column
        label="失效日期"
        align="center"
        prop="expireDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="材料成本" align="center" prop="materialCost" />
      <el-table-column label="人工成本" align="center" prop="laborCost" />
      <el-table-column label="制造费用" align="center" prop="overheadCost" />
      <el-table-column label="总成本" align="center" prop="totalCost" />
      <el-table-column label="成本币种" align="center" prop="costCurrency" />
      <el-table-column
        label="计算日期"
        align="center"
        prop="calculationDate"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="关联BOM版本" align="center" prop="bomVersion" />
      <el-table-column label="关联工艺版本" align="center" prop="routeVersion" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_COST_STANDARD_STATUS" :value="scope.row.status" />
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
            v-hasPermi="['erp:cost-standard:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:cost-standard:delete']"
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
  <CostStandardForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { CostStandardApi, CostStandard } from '@/api/erp/coststandard'
import CostStandardForm from './CostStandardForm.vue'
import { ProductApi, ProductVO } from '@/api/erp/product/product'

/** ERP 标准成本 列表 */
defineOptions({ name: 'CostStandard' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<CostStandard[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  productId: undefined,
  version: undefined,
  effectiveDate: [],
  expireDate: [],
  materialCost: undefined,
  laborCost: undefined,
  overheadCost: undefined,
  totalCost: undefined,
  costCurrency: undefined,
  calculationDate: [],
  bomVersion: undefined,
  routeVersion: undefined,
  status: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const productList = ref<ProductVO[]>([]) // 产品列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CostStandardApi.getCostStandardPage(queryParams)
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
    await CostStandardApi.deleteCostStandard(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 标准成本 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await CostStandardApi.deleteCostStandardList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: CostStandard[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CostStandardApi.exportCostStandard(queryParams)
    download.excel(data, 'ERP 标准成本.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 获取产品名称 */
const getProductName = (id?: number) => {
  if (!id) return '-'
  const product = productList.value.find(item => item.id === id)
  return product?.name || `产品${id}`
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载产品列表
  try {
    const products = await ProductApi.getProductSimpleList()
    productList.value = products || []
  } catch (error) {
    console.error('加载产品列表失败:', error)
  }
})
</script>