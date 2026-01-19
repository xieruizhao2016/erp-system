<template>
  <ContentWrap>
    <DocAlert title="【财务】利润表" url="https://doc.iocoder.cn/erp/finance-profit-statement/" />

    <ContentWrap>
      <!-- 搜索工作栏 -->
      <el-form
        class="-mb-15px"
        :model="queryParams"
        ref="queryFormRef"
        :inline="true"
        label-width="120px"
      >
        <el-form-item label="期间日期" prop="periodDate">
          <el-date-picker
            v-model="queryParams.periodDate"
            type="month"
            value-format="YYYY-MM"
            placeholder="请选择期间日期"
            class="!w-240px"
          />
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
            @click="handleCalculate"
            v-hasPermi="['erp:finance-profit-statement:update']"
          >
            <Icon icon="ep:calculator" class="mr-5px" /> 计算
          </el-button>
          <el-button
            type="success"
            plain
            @click="handleExport"
            :loading="exportLoading"
            v-hasPermi="['erp:finance-profit-statement:export']"
          >
            <Icon icon="ep:download" class="mr-5px" /> 导出
          </el-button>
        </el-form-item>
      </el-form>
    </ContentWrap>

    <!-- 列表 -->
    <ContentWrap>
      <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
        <el-table-column label="期间日期" align="center" prop="periodDate" width="120">
          <template #default="scope">
            {{ scope.row.periodDate ? (scope.row.periodDate.length > 7 ? scope.row.periodDate.substring(0, 7) : scope.row.periodDate) : '' }}
          </template>
        </el-table-column>
        <el-table-column label="营业收入" align="center" prop="revenue" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="营业成本" align="center" prop="cost" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="毛利润" align="center" prop="grossProfit" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="营业费用" align="center" prop="operatingExpense" :formatter="erpPriceTableColumnFormatter" />
        <el-table-column label="净利润" align="center" prop="netProfit" :formatter="erpPriceTableColumnFormatter" />
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
        <el-table-column label="操作" align="center" fixed="right" width="200">
          <template #default="scope">
            <el-button
              link
              type="success"
              @click="handleCalculateOne(scope.row.periodDate)"
              v-hasPermi="['erp:finance-profit-statement:update']"
            >
              重新计算
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['erp:finance-profit-statement:delete']"
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

  </ContentWrap>
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { erpPriceTableColumnFormatter } from '@/utils'
import { ProfitStatementApi, ProfitStatementVO } from '@/api/erp/finance/profitstatement'

/** ERP 利润表 列表 */
defineOptions({ name: 'ErpFinanceProfitStatement' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProfitStatementVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  periodDate: undefined,
  status: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProfitStatementApi.getProfitStatementPage(queryParams)
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

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await ProfitStatementApi.deleteProfitStatement(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 计算按钮操作 */
const handleCalculate = async () => {
  try {
    const periodDate = queryParams.periodDate || new Date().toISOString().slice(0, 7)
    await message.confirm('确认要计算利润表吗？')
    await ProfitStatementApi.calculateProfitStatement(periodDate)
    message.success('计算成功')
    await getList()
  } catch {}
}

/** 重新计算单个 */
const handleCalculateOne = async (periodDate: any) => {
  try {
    console.log('handleCalculateOne 接收到的 periodDate:', periodDate, typeof periodDate)
    
    if (!periodDate) {
      message.error('期间日期不能为空')
      return
    }
    
    // 转换为字符串并去除空格
    let periodDateStr = String(periodDate).trim()
    console.log('转换后的 periodDateStr:', periodDateStr, '长度:', periodDateStr.length)
    
    // 处理各种可能的日期格式
    // 1. 格式为 yyyy-MM-dd（LocalDate 序列化后的格式，如 "2026-01-01"）
    if (periodDateStr.length >= 10 && periodDateStr.match(/^\d{4}-\d{2}-\d{2}/)) {
      periodDateStr = periodDateStr.substring(0, 7) // 提取 "yyyy-MM"
      console.log('从 yyyy-MM-dd 提取 yyyy-MM:', periodDateStr)
    } 
    // 2. 已经是 yyyy-MM 格式（如 "2026-01"）
    else if (periodDateStr.length === 7 && periodDateStr.match(/^\d{4}-\d{2}$/)) {
      // 直接使用
      console.log('已经是 yyyy-MM 格式:', periodDateStr)
    } 
    // 3. 格式为 yyyyMM（如 "202601"）
    else if (periodDateStr.length === 6 && periodDateStr.match(/^\d{6}$/)) {
      periodDateStr = periodDateStr.substring(0, 4) + '-' + periodDateStr.substring(4, 6)
      console.log('从 yyyyMM 转换为 yyyy-MM:', periodDateStr)
    } 
    // 4. 格式为 yyyy/MM（如 "2026/01"）
    else if (periodDateStr.match(/^\d{4}\/\d{2}/)) {
      periodDateStr = periodDateStr.replace(/\//g, '-').substring(0, 7)
      console.log('从 yyyy/MM 转换为 yyyy-MM:', periodDateStr)
    } 
    // 5. 尝试从 Date 对象或 ISO 字符串中提取
    else {
      try {
        // 尝试解析为 Date 对象
        const date = new Date(periodDateStr)
        if (!isNaN(date.getTime())) {
          const year = date.getFullYear()
          const month = String(date.getMonth() + 1).padStart(2, '0')
          periodDateStr = `${year}-${month}`
          console.log('从 Date 对象提取 yyyy-MM:', periodDateStr)
        } else {
          // 如果无法解析，尝试其他方式
          console.warn('无法解析日期格式，尝试其他方式:', periodDateStr)
          // 如果包含日期部分，尝试提取
          const match = periodDateStr.match(/(\d{4})[-\/](\d{2})/)
          if (match) {
            periodDateStr = `${match[1]}-${match[2]}`
            console.log('从字符串中提取 yyyy-MM:', periodDateStr)
          } else {
            throw new Error('无法解析日期格式: ' + periodDateStr)
          }
        }
      } catch (e) {
        console.error('期间日期格式错误，无法解析:', periodDateStr, e)
        message.error('期间日期格式错误，应为 yyyy-MM 格式，当前值: ' + periodDateStr + '，类型: ' + typeof periodDate)
        return
      }
    }
    
    // 最终验证格式（yyyy-MM）
    const datePattern = /^\d{4}-\d{2}$/
    if (!datePattern.test(periodDateStr)) {
      console.error('期间日期格式验证失败:', periodDateStr)
      message.error('期间日期格式错误，应为 yyyy-MM 格式，当前值: ' + periodDateStr)
      return
    }
    
    console.log('最终发送的 periodDateStr:', periodDateStr)
    await message.confirm('确认要重新计算该期间的利润表吗？')
    await ProfitStatementApi.calculateProfitStatement(periodDateStr)
    message.success('计算成功')
    await getList()
  } catch (error: any) {
    console.error('计算利润表失败:', error)
    message.error(error?.message || '计算失败，请稍后重试')
  }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProfitStatementApi.exportProfitStatement(queryParams)
    download.excel(data, 'ERP 利润表.xls')
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

