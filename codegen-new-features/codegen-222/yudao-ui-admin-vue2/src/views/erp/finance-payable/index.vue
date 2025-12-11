<template>
  <div class="app-container">
    <!-- 搜索工作栏 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据号" prop="no">
        <el-input v-model="queryParams.no" placeholder="请输入单据号" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="供应商编号（关联 erp_supplier.id）" prop="supplierId">
        <el-select v-model="queryParams.supplierId" placeholder="请选择供应商编号（关联 erp_supplier.id）" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="采购订单编号（关联 erp_purchase_order.id）" prop="orderId">
        <el-select v-model="queryParams.orderId" placeholder="请选择采购订单编号（关联 erp_purchase_order.id）" clearable size="small">
          <el-option label="请选择字典生成" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="应付金额，单位：元" prop="amount">
        <el-input v-model="queryParams.amount" placeholder="请输入应付金额，单位：元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="已付金额，单位：元" prop="paidAmount">
        <el-input v-model="queryParams.paidAmount" placeholder="请输入已付金额，单位：元" clearable @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="余额，单位：元（balance = amount - paid_amount）" prop="balance">
        <el-input v-model="queryParams.balance" placeholder="请输入余额，单位：元（balance = amount - paid_amount）" clearable @keyup.enter.native="handleQuery"/>
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
                   v-hasPermi="['erp:finance-payable:create']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport" :loading="exportLoading"
                   v-hasPermi="['erp:finance-payable:export']">导出</el-button>
      </el-col>
          <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:finance-payable:delete']"
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
            <el-table-column label="单据号" align="center" prop="no" />
      <el-table-column label="供应商编号（关联 erp_supplier.id）" align="center" prop="supplierId" />
      <el-table-column label="采购订单编号（关联 erp_purchase_order.id）" align="center" prop="orderId" />
      <el-table-column label="应付金额，单位：元" align="center" prop="amount" />
      <el-table-column label="已付金额，单位：元" align="center" prop="paidAmount" />
      <el-table-column label="余额，单位：元（balance = amount - paid_amount）" align="center" prop="balance" />
      <el-table-column label="到期日" align="center" prop="dueDate" />
      <el-table-column label="状态（10-未审核，20-已审核）" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template v-slot="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="openForm(scope.row.${primaryColumn.javaField})"
                     v-hasPermi="['erp:finance-payable:update']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                     v-hasPermi="['erp:finance-payable:delete']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList"/>
    <!-- 对话框(添加 / 修改) -->
    <FinancePayableForm ref="formRef" @success="getList" />
    </div>
</template>

<script>
import * as FinancePayableApi from '@/api/erp/finance-payable';
import FinancePayableForm from './FinancePayableForm.vue';
export default {
  name: "FinancePayable",
  components: {
          FinancePayableForm
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
      // 应付账款列表
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
        no: null,
        supplierId: null,
        orderId: null,
        amount: null,
        paidAmount: null,
        balance: null,
        dueDate: null,
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
              const res = await FinancePayableApi.getFinancePayablePage(this.queryParams);
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
      await this.$modal.confirm('是否确认删除应付账款编号为"' + ${primaryColumn.javaField} + '"的数据项?')
      try {
       await FinancePayableApi.deleteFinancePayable(${primaryColumn.javaField});
       await this.getList();
       this.$modal.msgSuccess("删除成功");
      } catch {}
    },
    /** 批量删除应付账款 */
    async handleDeleteBatch() {
      await this.$modal.confirm('是否确认删除?')
      try {
        await FinancePayableApi.deleteFinancePayableList(this.checkedIds);
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
      await this.$modal.confirm('是否确认导出所有应付账款数据项?');
      try {
        this.exportLoading = true;
        const data = await FinancePayableApi.exportFinancePayableExcel(this.queryParams);
        this.$download.excel(data, '应付账款.xls');
      } catch {
      } finally {
        this.exportLoading = false;
      }
    },
              }
};
</script>