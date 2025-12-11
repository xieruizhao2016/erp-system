<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="资产总计，单位：元" prop="assetTotal">
                      <el-input v-model="formData.assetTotal" placeholder="请输入资产总计，单位：元" />
                    </el-form-item>
                    <el-form-item label="负债总计，单位：元" prop="liabilityTotal">
                      <el-input v-model="formData.liabilityTotal" placeholder="请输入负债总计，单位：元" />
                    </el-form-item>
                    <el-form-item label="所有者权益总计，单位：元" prop="equityTotal">
                      <el-input v-model="formData.equityTotal" placeholder="请输入所有者权益总计，单位：元" />
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
  import * as FinanceBalanceSheetApi from '@/api/erp/finance-balance-sheet';
      export default {
    name: "FinanceBalanceSheetForm",
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
                            assetTotal: undefined,
                            liabilityTotal: undefined,
                            equityTotal: undefined,
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
            const res = await FinanceBalanceSheetApi.getFinanceBalanceSheet(id);
            this.formData = res.data;
            this.title = "修改资产负债表";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增资产负债表";
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
            await FinanceBalanceSheetApi.updateFinanceBalanceSheet(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await FinanceBalanceSheetApi.createFinanceBalanceSheet(data);
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
                            assetTotal: undefined,
                            liabilityTotal: undefined,
                            equityTotal: undefined,
                            status: undefined,
                            remark: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>