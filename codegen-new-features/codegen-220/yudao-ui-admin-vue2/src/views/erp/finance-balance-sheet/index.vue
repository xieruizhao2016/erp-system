<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资产总计，单位：元" prop="assetTotal">
        <el-input v-model="queryParams.assetTotal" placeholder="请输入资产总计，单位：元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="负债总计，单位：元" prop="liabilityTotal">
        <el-input v-model="queryParams.liabilityTotal" placeholder="请输入负债总计，单位：元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="所有者权益总计，单位：元" prop="equityTotal">
        <el-input v-model="queryParams.equityTotal" placeholder="请输入所有者权益总计，单位：元" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['erp:finance-balance-sheet:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['erp:finance-balance-sheet:export']">导出</el-button>
      </el-col>
          <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:finance-balance-sheet:delete']"
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
      <el-table-column label="资产总计，单位：元" align="center" prop="assetTotal" />
      <el-table-column label="负债总计，单位：元" align="center" prop="liabilityTotal" />
      <el-table-column label="所有者权益总计，单位：元" align="center" prop="equityTotal" />
      <el-table-column label="状态（10-未审核，20-已审核）" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.${primaryColumn.javaField})"
                     v-hasPermi="['erp:finance-balance-sheet:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['erp:finance-balance-sheet:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <FinanceBalanceSheetForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as FinanceBalanceSheetApi from '@/api/erp/finance-balance-sheet';
import FinanceBalanceSheetForm from './FinanceBalanceSheetForm.vue';
export default {
  name: "FinanceBalanceSheet",
  components: {
          FinanceBalanceSheetForm
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
      // 资产负债表列表
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
        assetTotal: null,
        liabilityTotal: null,
        equityTotal: null,
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
              const res = await FinanceBalanceSheetApi.getFinanceBalanceSheetPage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除资产负债表编号为"' + ${primaryColumn.javaField} + '"的数据项?')
      try {
       await FinanceBalanceSheetApi.deleteFinanceBalanceSheet(${primaryColumn.javaField});
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 批量删除资产负债表 */
    async handleDeleteBatch() {
      await this.$modal.confirm('是否确认删除?')
      try {
        await FinanceBalanceSheetApi.deleteFinanceBalanceSheetList(this.checkedIds);
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
      await this.$modal.confirm('是否确认导出所有资产负债表数据项?');
      try {
        this.exportLoading = true;
        const data = await FinanceBalanceSheetApi.exportFinanceBalanceSheetExcel(this.queryParams);
        this.$download.excel(data, '资产负债表.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>