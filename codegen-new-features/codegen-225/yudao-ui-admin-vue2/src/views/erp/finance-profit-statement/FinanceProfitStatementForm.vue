<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="营业收入，单位：元（从销售订单汇总）" prop="revenue">
                      <el-input v-model="formData.revenue" placeholder="请输入营业收入，单位：元（从销售订单汇总）" />
                    </el-form-item>
                    <el-form-item label="营业成本，单位：元（从采购订单/生产订单汇总）" prop="cost">
                      <el-input v-model="formData.cost" placeholder="请输入营业成本，单位：元（从采购订单/生产订单汇总）" />
                    </el-form-item>
                    <el-form-item label="毛利润，单位：元（gross_profit = revenue - cost）" prop="grossProfit">
                      <el-input v-model="formData.grossProfit" placeholder="请输入毛利润，单位：元（gross_profit = revenue - cost）" />
                    </el-form-item>
                    <el-form-item label="营业费用，单位：元" prop="operatingExpense">
                      <el-input v-model="formData.operatingExpense" placeholder="请输入营业费用，单位：元" />
                    </el-form-item>
                    <el-form-item label="净利润，单位：元（net_profit = gross_profit - operating_expense）" prop="netProfit">
                      <el-input v-model="formData.netProfit" placeholder="请输入净利润，单位：元（net_profit = gross_profit - operating_expense）" />
                    </el-form-item>
                    <el-form-item label="状态（10-未审核，20-已审核）" prop="status">
                      <el-select v-model="formData.status" placeholder="请选择状态（10-未审核，20-已审核）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                      <el-input v-model="formData.remark" placeholder="请输入备注" />
                    </el-form-item>
      </el-form>
              <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :disabled="formLoading">确 定</el-button>
        <el-button @click="dialogVisible = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import * as FinanceProfitStatementApi from '@/api/erp/finance-profit-statement';
      export default {
    name: "FinanceProfitStatementForm",
    components: {
                    },
    data() {
      return {
        // 弹出层标题
        dialogTitle: "",
        // 是否显示弹出层
        dialogVisible: false,
        // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
        formLoading: false,
        // 表单参数
        formData: {
                            periodDate: undefined,
                            revenue: undefined,
                            cost: undefined,
                            grossProfit: undefined,
                            operatingExpense: undefined,
                            netProfit: undefined,
                            status: undefined,
                            remark: undefined,
        },
        // 表单校验
        formRules: {
                        periodDate: [{ required: true, message: '期间日期（年月）不能为空', trigger: 'blur' }],
        },
                        };
    },
    methods: {
      /** 打开弹窗 */
     async open(id) {
        this.dialogVisible = true;
        this.reset();
        // 修改时，设置数据
        if (id) {
          this.formLoading = true;
          try {
            const res = await FinanceProfitStatementApi.getFinanceProfitStatement(id);
            this.formData = res.data;
            this.title = "修改利润表";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增利润表";
              },
      /** 提交按钮 */
      async submitForm() {
        // 校验主表
        await this.$refs["formRef"].validate();
                  this.formLoading = true;
        try {
          const data = this.formData;
                  // 修改的提交
          if (data.${primaryColumn.javaField}) {
            await FinanceProfitStatementApi.updateFinanceProfitStatement(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await FinanceProfitStatementApi.createFinanceProfitStatement(data);
          this.$modal.msgSuccess("新增成功");
          this.dialogVisible = false;
          this.$emit('success');
        } finally {
          this.formLoading = false;
        }
      },
                      /** 表单重置 */
      reset() {
        this.formData = {
                            periodDate: undefined,
                            revenue: undefined,
                            cost: undefined,
                            grossProfit: undefined,
                            operatingExpense: undefined,
                            netProfit: undefined,
                            status: undefined,
                            remark: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>