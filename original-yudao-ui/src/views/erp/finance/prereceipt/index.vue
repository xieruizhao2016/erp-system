<template>
  <ContentWrap>
    <DocAlert title="【财务】预收款" url="https://doc.iocoder.cn/erp/finance-prereceipt/" />

    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="120px"
      >
        <el-form-item label="单据号" prop="no">
          <el-input
            v-model="queryParams.no"
            placeholder="请输入单据号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="客户" prop="customerId">
          <el-select
            v-model="queryParams.customerId"
            clearable
            filterable
            placeholder="请选择客户"
            class="!w-240px"
          >
            <el-option
              v-for="item in customerList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="!w-240px">
            <el-option
              v-for="dict in getIntDictOptions(DICT_TYPE.ERP_AUDIT_STATUS)"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
          <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
          <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['erp:finance-prereceipt:export']"
          >
            <Icon icon="ep:download" class="mr-5px" /> 导出
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column label="单据号" align="center" prop="no" min-width="150" />
        <el-table-column label="客户" align="center" prop="customerName" min-width="120" />
        <el-table-column label="预收金额" align="center" prop="amount" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="已核销金额" align="center" prop="usedAmount" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="余额" align="center" prop="balance" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="到期日" align="center" prop="dueDate" :formatter="dateFormatter" width="120" />
        <el-table-column label="状态" align="center" prop="status" width="100">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.ERP_AUDIT_STATUS" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          :formatter="dateFormatter"
          width="180px"
        />
        <el-table-column label="操作" align="center" fixed="right" width="150">
          <template #default="scope">
            <el-button
              link
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['erp:finance-prereceipt:update']"
            >
              编辑
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['erp:finance-prereceipt:delete']"
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
    <PrereceiptForm ref="formRef" @success="getList" />
  </ContentWrap>
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { erpPriceTableColumnFormatter } from '@/utils'
import { PrereceiptApi, PrereceiptVO } from '@/api/erp/finance/prereceipt'
import { CustomerApi, CustomerVO } from '@/api/erp/sale/customer'
import PrereceiptForm from './PrereceiptForm.vue'

/** ERP 预收款 列表 */
defineOptions({ name: 'ErpFinancePrereceipt' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<PrereceiptVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  customerId: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const customerList = ref<CustomerVO[]>([]) // 客户列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await PrereceiptApi.getPrereceiptPage(queryParams)
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
    await PrereceiptApi.deletePrereceipt(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await PrereceiptApi.exportPrereceipt(queryParams)
    download.excel(data, 'ERP 预收款.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(() => {
  getList()
  // 加载客户列表
  CustomerApi.getCustomerSimpleList().then((data) => {
    customerList.value = data
  })
})
</script>

