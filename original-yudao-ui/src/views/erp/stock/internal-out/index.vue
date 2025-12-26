<template>
  <ContentWrap>
    <DocAlert title="【库存】内部出库" url="https://doc.iocoder.cn/erp/stock-internal-out/" />

    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="120px"
      >
        <el-form-item label="出库单号" prop="no">
          <el-input
            v-model="queryParams.no"
            placeholder="请输入出库单号"
            clearable
            @keyup.enter="handleQuery"
            class="!w-240px"
          />
        </el-form-item>
        <el-form-item label="员工" prop="employeeId">
          <el-select
            v-model="queryParams.employeeId"
            clearable
            filterable
            placeholder="请选择员工"
            class="!w-240px"
          >
            <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.nickname"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="deptId">
          <el-select
            v-model="queryParams.deptId"
            clearable
            filterable
            placeholder="请选择部门"
            class="!w-240px"
          >
            <el-option
              v-for="item in deptList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="内部类型" prop="internalType">
          <el-select v-model="queryParams.internalType" placeholder="请选择内部类型" clearable class="!w-240px">
            <el-option label="部门调拨" :value="1" />
            <el-option label="员工领料" :value="2" />
            <el-option label="生产产品" :value="3" />
            <el-option label="其他" :value="4" />
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
            type="primary"
            plain
            @click="openForm('create')"
            v-hasPermi="['erp:stock:internal-out:create']"
          >
            <Icon icon="ep:plus" class="mr-5px" /> 新增
          </el-button>
          <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['erp:stock:internal-out:export']"
          >
            <Icon icon="ep:download" class="mr-5px" /> 导出
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-table
        v-loading="loading"
        :data="list"
        :stripe="true"
        :show-overflow-tooltip="true"
        @selection-change="handleSelectionChange"
      >
        <el-table-column width="30" label="选择" type="selection" />
        <el-table-column min-width="180" label="出库单号" align="center" prop="no" />
        <el-table-column label="员工" align="center" prop="employeeName" width="120" />
        <el-table-column label="部门" align="center" prop="deptName" width="120" />
        <el-table-column label="内部类型" align="center" prop="internalType" width="100">
          <template #default="scope">
            <span v-if="scope.row.internalType === 1">部门调拨</span>
            <span v-else-if="scope.row.internalType === 2">员工领料</span>
            <span v-else-if="scope.row.internalType === 3">生产产品</span>
            <span v-else-if="scope.row.internalType === 4">其他</span>
          </template>
        </el-table-column>
        <el-table-column
          label="出库时间"
          align="center"
          prop="outTime"
          :formatter="dateFormatter"
          width="180px"
        />
        <el-table-column
          label="合计数量"
          align="center"
          prop="totalCount"
          :formatter="erpCountTableColumnFormatter"
        />
        <el-table-column
          label="合计金额"
          align="center"
          prop="totalPrice"
          :formatter="erpPriceTableColumnFormatter"
        />
        <el-table-column label="状态" align="center" fixed="right" width="90" prop="status">
          <template #default="scope">
            <dict-tag :type="DICT_TYPE.ERP_AUDIT_STATUS" :value="scope.row.status" />
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="220">
          <template #default="scope">
            <el-button
              link
              @click="openForm('detail', scope.row.id)"
              v-hasPermi="['erp:stock:internal-out:query']"
            >
              详情
            </el-button>
            <el-button
              link
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['erp:stock:internal-out:update']"
              :disabled="scope.row.status === 20"
            >
              编辑
            </el-button>
            <el-button
              link
              type="primary"
              @click="handleUpdateStatus(scope.row.id, 20)"
              v-hasPermi="['erp:stock:internal-out:approve']"
              v-if="scope.row.status === 10"
            >
              审批
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleUpdateStatus(scope.row.id, 10)"
              v-hasPermi="['erp:stock:internal-out:approve']"
              v-else
            >
              反审批
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['erp:stock:internal-out:delete']"
              :disabled="scope.row.status === 20"
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
    <StockInternalOutForm ref="formRef" @success="getList" />
  </ContentWrap>
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { erpPriceTableColumnFormatter, erpCountTableColumnFormatter } from '@/utils'
import { StockInternalOutApi, StockInternalOutVO } from '@/api/erp/stock/internal-out'
import * as UserApi from '@/api/system/user'
import * as DeptApi from '@/api/system/dept'
import { handleTree } from '@/utils/tree'
import StockInternalOutForm from './StockInternalOutForm.vue'

/** ERP 内部出库 列表 */
defineOptions({ name: 'ErpStockInternalOut' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<StockInternalOutVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  employeeId: undefined,
  deptId: undefined,
  internalType: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const selectionList = ref<StockInternalInVO[]>([]) // 列表的选中项
const userList = ref<UserApi.UserVO[]>([]) // 用户列表
const deptList = ref<DeptApi.DeptVO[]>([]) // 部门列表

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await StockInternalOutApi.getStockInternalOutPage(queryParams)
    list.value = data.list
    total.value = data.total
  } catch (error) {
    console.error('获取内部出库单列表失败:', error)
    // 错误已由 axios 拦截器处理，这里只记录日志
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
    await StockInternalOutApi.deleteStockInternalOut(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 更新状态 */
const handleUpdateStatus = async (id: number, status: number) => {
  try {
    // 参数验证
    if (!id || id === undefined || id === null) {
      message.error('单据ID不能为空')
      return
    }
    if (status === undefined || status === null) {
      message.error('状态不能为空')
      return
    }
    await message.confirm(status === 20 ? '确认要审批该单据吗？' : '确认要反审批该单据吗？')
    await StockInternalOutApi.updateStockInternalOutStatus(id, status)
    message.success('操作成功')
    await getList()
  } catch (error: any) {
    console.error('更新状态失败:', error)
    message.error(error?.message || '操作失败，请稍后重试')
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await StockInternalOutApi.exportStockInternalOut(queryParams)
    download.excel(data, 'ERP 内部出库.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: StockInternalOutVO[]) => {
  selectionList.value = selection
}

/** 初始化 **/
onMounted(() => {
  getList()
  // 加载用户列表
  UserApi.getSimpleUserList().then((data) => {
    userList.value = data
  })
  // 加载部门列表
  DeptApi.getSimpleDeptList().then((data) => {
    deptList.value = handleTree(data)
  })
})
</script>

