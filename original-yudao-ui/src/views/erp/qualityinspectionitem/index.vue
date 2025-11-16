<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item label="检验ID" prop="inspectionId">
        <el-input
          v-model="queryParams.inspectionId"
          placeholder="请输入检验ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验项目ID" prop="itemId">
        <el-input
          v-model="queryParams.itemId"
          placeholder="请输入检验项目ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="样本编号" prop="sampleNo">
        <el-input
          v-model="queryParams.sampleNo"
          placeholder="请输入样本编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="测量值" prop="measuredValue">
        <el-input
          v-model="queryParams.measuredValue"
          placeholder="请输入测量值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="实际数值" prop="actualValue">
        <el-input
          v-model="queryParams.actualValue"
          placeholder="请输入实际数值"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验结果：1-合格，2-不合格，3-超差" prop="testResult">
        <el-input
          v-model="queryParams.testResult"
          placeholder="请输入检验结果：1-合格，2-不合格，3-超差"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="缺陷类型" prop="defectType">
        <el-select
          v-model="queryParams.defectType"
          placeholder="请选择缺陷类型"
          clearable
          class="!w-240px"
        >
          <el-option label="全部" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="缺陷图片URLs" prop="imageUrls">
        <el-input
          v-model="queryParams.imageUrls"
          placeholder="请输入缺陷图片URLs"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验员ID" prop="inspectorId">
        <el-input
          v-model="queryParams.inspectorId"
          placeholder="请输入检验员ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验时间" prop="inspectionTime">
        <el-date-picker
          v-model="queryParams.inspectionTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入备注"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker
          v-model="queryParams.createTime"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
        <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
        <el-button
          type="primary"
          plain
          @click="openForm('create')"
          v-hasPermi="['erp:quality-inspection-item:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:quality-inspection-item:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:quality-inspection-item:delete']"
        >
          <Icon icon="ep:delete" class="mr-5px" /> 批量删除
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table
        row-key="id"
        v-loading="loading"
        :data="list"
        :stripe="true"
        :show-overflow-tooltip="true"
        @selection-change="handleRowCheckboxChange"
    >
    <el-table-column type="selection" width="55" />
      <el-table-column label="编号" align="center" prop="id" />
      <el-table-column label="检验ID" align="center" prop="inspectionId" />
      <el-table-column label="检验项目ID" align="center" prop="itemId" />
      <el-table-column label="样本编号" align="center" prop="sampleNo" />
      <el-table-column label="测量值" align="center" prop="measuredValue" />
      <el-table-column label="实际数值" align="center" prop="actualValue" />
      <el-table-column label="检验结果：1-合格，2-不合格，3-超差" align="center" prop="testResult" />
      <el-table-column label="缺陷类型" align="center" prop="defectType" />
      <el-table-column label="缺陷描述" align="center" prop="defectDescription" />
      <el-table-column label="缺陷图片URLs" align="center" prop="imageUrls" />
      <el-table-column label="检验员ID" align="center" prop="inspectorId" />
      <el-table-column
        label="检验时间"
        align="center"
        prop="inspectionTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['erp:quality-inspection-item:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:quality-inspection-item:delete']"
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
  <QualityInspectionItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { QualityInspectionItemApi, QualityInspectionItem } from '@/api/erp/qualityinspectionitem'
import QualityInspectionItemForm from './QualityInspectionItemForm.vue'

/** ERP 质检明细 列表 */
defineOptions({ name: 'QualityInspectionItem' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<QualityInspectionItem[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  inspectionId: undefined,
  itemId: undefined,
  sampleNo: undefined,
  measuredValue: undefined,
  actualValue: undefined,
  testResult: undefined,
  defectType: undefined,
  defectDescription: undefined,
  imageUrls: undefined,
  inspectorId: undefined,
  inspectionTime: [],
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QualityInspectionItemApi.getQualityInspectionItemPage(queryParams)
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
    await QualityInspectionItemApi.deleteQualityInspectionItem(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 质检明细 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await QualityInspectionItemApi.deleteQualityInspectionItemList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: QualityInspectionItem[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await QualityInspectionItemApi.exportQualityInspectionItem(queryParams)
    download.excel(data, 'ERP 质检明细.xls')
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