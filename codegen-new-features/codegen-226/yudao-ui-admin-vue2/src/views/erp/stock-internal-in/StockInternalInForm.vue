<template>
  <div class="app-container">
    <!-- 对话框(添加 / 修改) -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="45%" v-dialogDrag append-to-body>
      <el-form ref="formRef" :model="formData" :rules="formRules" v-loading="formLoading" label-width="100px">
                    <el-form-item label="入库单号" prop="no">
                      <el-input v-model="formData.no" placeholder="请输入入库单号" />
                    </el-form-item>
                    <el-form-item label="员工编号（关联 system_users.id）" prop="employeeId">
                      <el-select v-model="formData.employeeId" placeholder="请选择员工编号（关联 system_users.id）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="部门编号（关联 system_dept.id）" prop="deptId">
                      <el-select v-model="formData.deptId" placeholder="请选择部门编号（关联 system_dept.id）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="内部类型（1-部门调拨，2-员工领用，3-其他）" prop="internalType">
                      <el-select v-model="formData.internalType" placeholder="请选择内部类型（1-部门调拨，2-员工领用，3-其他）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="合计数量" prop="totalCount">
                      <el-input v-model="formData.totalCount" placeholder="请输入合计数量" />
                    </el-form-item>
                    <el-form-item label="合计金额，单位：元" prop="totalPrice">
                      <el-input v-model="formData.totalPrice" placeholder="请输入合计金额，单位：元" />
                    </el-form-item>
                    <el-form-item label="状态（10-未审核，20-已审核）" prop="status">
                      <el-select v-model="formData.status" placeholder="请选择状态（10-未审核，20-已审核）">
                            <el-option label="请选择字典生成" value="" />
                      </el-select>
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                      <el-input v-model="formData.remark" placeholder="请输入备注" />
                    </el-form-item>
                    <el-form-item label="附件 URL" prop="fileUrl">
                      <el-input v-model="formData.fileUrl" placeholder="请输入附件 URL" />
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
  import * as StockInternalInApi from '@/api/erp/stock-internal-in';
      export default {
    name: "StockInternalInForm",
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
                            employeeId: undefined,
                            deptId: undefined,
                            internalType: undefined,
                            inTime: undefined,
                            totalCount: undefined,
                            totalPrice: undefined,
                            status: undefined,
                            remark: undefined,
                            fileUrl: undefined,
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
            const res = await StockInternalInApi.getStockInternalIn(id);
            this.formData = res.data;
            this.title = "修改内部入库单";
          } finally {
            this.formLoading = false;
          }
        }
        this.title = "新增内部入库单";
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
            await StockInternalInApi.updateStockInternalIn(data);
            this.$modal.msgSuccess("修改成功");
            this.dialogVisible = false;
            this.$emit('success');
            return;
          }
          // 添加的提交
          await StockInternalInApi.createStockInternalIn(data);
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
                            employeeId: undefined,
                            deptId: undefined,
                            internalType: undefined,
                            inTime: undefined,
                            totalCount: undefined,
                            totalPrice: undefined,
                            status: undefined,
                            remark: undefined,
                            fileUrl: undefined,
        };
        this.resetForm("formRef");
      }
    }
  };
</script>