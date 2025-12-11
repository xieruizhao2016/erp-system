<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="营业收入，单位：元（从销售订单汇总）" prop="revenue">
        <el-input v-model="queryParams.revenue" placeholder="请输入营业收入，单位：元（从销售订单汇总）" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="营业成本，单位：元（从采购订单/生产订单汇总）" prop="cost">
        <el-input v-model="queryParams.cost" placeholder="请输入营业成本，单位：元（从采购订单/生产订单汇总）" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="毛利润，单位：元（gross_profit = revenue - cost）" prop="grossProfit">
        <el-input v-model="queryParams.grossProfit" placeholder="请输入毛利润，单位：元（gross_profit = revenue - cost）" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="营业费用，单位：元" prop="operatingExpense">
        <el-input v-model="queryParams.operatingExpense" placeholder="请输入营业费用，单位：元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="净利润，单位：元（net_profit = gross_profit - operating_expense）" prop="netProfit">
        <el-input v-model="queryParams.netProfit" placeholder="请输入净利润，单位：元（net_profit = gross_profit - operating_expense）" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态（10-未审核，20-已审核）" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态（10-未审核，20-已审核）" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="queryParams.remark" placeholder="请输入备注" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="openForm(undefined)"
                   v-hasPermi="['erp:finance-profit-statement:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['erp:finance-profit-statement:export']">导出</el-button>
      </el-col>
          <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:finance-profit-statement:delete']"
        >
          批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

            <el-table
          v-loading="loading"
          :data="list"
          :stripe="true"
          :show-overflow-tooltip="true"
          @selection-change="handleRowCheckboxChange"
      >
        <el-table-column type="selection" width="55" />
            <el-table-column label="期间日期（年月）" align="center" prop="periodDate" />
      <el-table-column label="营业收入，单位：元（从销售订单汇总）" align="center" prop="revenue" />
      <el-table-column label="营业成本，单位：元（从采购订单/生产订单汇总）" align="center" prop="cost" />
      <el-table-column label="毛利润，单位：元（gross_profit = revenue - cost）" align="center" prop="grossProfit" />
      <el-table-column label="营业费用，单位：元" align="center" prop="operatingExpense" />
      <el-table-column label="净利润，单位：元（net_profit = gross_profit - operating_expense）" align="center" prop="netProfit" />
      <el-table-column label="状态（10-未审核，20-已审核）" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.${primaryColumn.javaField})"
                     v-hasPermi="['erp:finance-profit-statement:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['erp:finance-profit-statement:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <FinanceProfitStatementForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as FinanceProfitStatementApi from '@/api/erp/finance-profit-statement';
import FinanceProfitStatementForm from './FinanceProfitStatementForm.vue';
export default {
  name: "FinanceProfitStatement",
  components: {
          FinanceProfitStatementForm
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
              // 总条数
        total: 0,
      // 利润表列表
      list: [],
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 选中行
      currentRow: {},
      checkedIds: [],
      // 查询参数
      queryParams: {
                    pageNo: 1,
            pageSize: 10,
        periodDate: null,
        revenue: null,
        cost: null,
        grossProfit: null,
        operatingExpense: null,
        netProfit: null,
        status: null,
        remark: null,
      },
            };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    async getList() {
      try {
      this.loading = true;
              const res = await FinanceProfitStatementApi.getFinanceProfitStatementPage(this.queryParams);
        this.list = res.data.list;
        this.total = res.data.total;
      } finally {
        this.loading = false;
      }
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 添加/修改操作 */
    openForm(id) {
      this.$refs["formRef"].open(id);
    },
    /** 删除按钮操作 */
    async handleDelete(row) {
      const ${primaryColumn.javaField} = row.${primaryColumn.javaField};
      await this.$modal.confirm('是否确认删除利润表编号为"' + ${primaryColumn.javaField} + '"的数据项?')
      try {
       await FinanceProfitStatementApi.deleteFinanceProfitStatement(${primaryColumn.javaField});
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 批量删除利润表 */
    async handleDeleteBatch() {
      await this.$modal.confirm('是否确认删除?')
      try {
        await FinanceProfitStatementApi.deleteFinanceProfitStatementList(this.checkedIds);
        this.checkedIds = [];
        await this.getList();
        this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    handleRowCheckboxChange(records) {
      this.checkedIds = records.map((item) => item.id);
    },
    /** 导出按钮操作 */
    async handleExport() {
      await this.$modal.confirm('是否确认导出所有利润表数据项?');
      try {
        this.exportLoading = true;
        const data = await FinanceProfitStatementApi.exportFinanceProfitStatementExcel(this.queryParams);
        this.$download.excel(data, '利润表.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>