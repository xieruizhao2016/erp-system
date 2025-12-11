<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="单据号" prop="no">
                      <el-input v-model="formData.no" placeholder="请输入单据号" />
                    </el-form-item>
                    <el-form-item label="供应商编号（关联 erp_supplier.id）" prop="supplierId">
                      <el-select v-model="formData.supplierId" placeholder="请选择供应商编号（关联 erp_supplier.id）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="采购订单编号（关联 erp_purchase_order.id）" prop="orderId">
                      <el-select v-model="formData.orderId" placeholder="请选择采购订单编号（关联 erp_purchase_order.id）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="应付金额，单位：元" prop="amount">
                      <el-input v-model="formData.amount" placeholder="请输入应付金额，单位：元" />
                    </el-form-item>
                    <el-form-item label="已付金额，单位：元" prop="paidAmount">
                      <el-input v-model="formData.paidAmount" placeholder="请输入已付金额，单位：元" />
                    </el-form-item>
                    <el-form-item label="余额，单位：元（balance = amount - paid_amount）" prop="balance">
                      <el-input v-model="formData.balance" placeholder="请输入余额，单位：元（balance = amount - paid_amount）" />
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
  import * as FinancePayableApi from '@/api/erp/finance-payable';
      export default {
    name: "FinancePayableForm",
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
                            no: undefined,
                            supplierId: undefined,
                            orderId: undefined,
                            amount: undefined,
                            paidAmount: undefined,
                            balance: undefined,
                            dueDate: undefined,
                            status: undefined,
                            remark: undefined,
        },
        // 表单校验
        formRules: {
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
            const res = await FinancePayableApi.getFinancePayable(id);
            this.formData = res.data;
            this.title = "修改应付账款";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增应付账款";
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
            await FinancePayableApi.updateFinancePayable(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await FinancePayableApi.createFinancePayable(data);
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
                            no: undefined,
                            supplierId: undefined,
                            orderId: undefined,
                            amount: undefined,
                            paidAmount: undefined,
                            balance: undefined,
                            dueDate: undefined,
                            status: undefined,
                            remark: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>