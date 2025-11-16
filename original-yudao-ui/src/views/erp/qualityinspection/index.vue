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
      <el-form-item label="检验单号" prop="inspectionNo">
        <el-input
          v-model="queryParams.inspectionNo"
          placeholder="请输入检验单号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="批次号" prop="batchNo">
        <el-input
          v-model="queryParams.batchNo"
          placeholder="请输入批次号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="产品ID" prop="productId">
        <el-input
          v-model="queryParams.productId"
          placeholder="请输入产品ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工序ID" prop="processId">
        <el-input
          v-model="queryParams.processId"
          placeholder="请输入工序ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工单ID" prop="workOrderId">
        <el-input
          v-model="queryParams.workOrderId"
          placeholder="请输入工单ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验类型：1-进料检验，2-过程检验，3-成品检验" prop="inspectionType">
        <el-select
          v-model="queryParams.inspectionType"
          placeholder="请选择检验类型：1-进料检验，2-过程检验，3-成品检验"
          clearable
          class="!w-240px"
        >
          <el-option label="进料检验" value="1" />
          <el-option label="过程检验" value="2" />
          <el-option label="成品检验" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="检验级别" prop="inspectionLevel">
        <el-select v-model="queryParams.inspectionLevel" placeholder="请选择检验级别" clearable class="!w-240px">
          <el-option label="全检" value="1" />
          <el-option label="抽检" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="检验级别（旧）" prop="inspectionLevelOld" style="display: none;">
        <el-input
          v-model="queryParams.inspectionLevel"
          placeholder="请输入检验级别：1-全检，2-抽检"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="批量大小" prop="lotSize">
        <el-input
          v-model="queryParams.lotSize"
          placeholder="请输入批量大小"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="样本数量" prop="sampleSize">
        <el-input
          v-model="queryParams.sampleSize"
          placeholder="请输入样本数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="合格数量" prop="qualifiedQuantity">
        <el-input
          v-model="queryParams.qualifiedQuantity"
          placeholder="请输入合格数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="不合格数量" prop="rejectedQuantity">
        <el-input
          v-model="queryParams.rejectedQuantity"
          placeholder="请输入不合格数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="报废数量" prop="scrapQuantity">
        <el-input
          v-model="queryParams.scrapQuantity"
          placeholder="请输入报废数量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验结果：1-合格，2-不合格，3-待复检" prop="inspectionResult">
        <el-input
          v-model="queryParams.inspectionResult"
          placeholder="请输入检验结果：1-合格，2-不合格，3-待复检"
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
      <el-form-item label="检验环境" prop="environment">
        <el-input
          v-model="queryParams.environment"
          placeholder="请输入检验环境"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验设备" prop="equipment">
        <el-input
          v-model="queryParams.equipment"
          placeholder="请输入检验设备"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="检验备注" prop="remark">
        <el-input
          v-model="queryParams.remark"
          placeholder="请输入检验备注"
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
          v-hasPermi="['erp:quality-inspection:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:quality-inspection:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:quality-inspection:delete']"
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
      <el-table-column label="检验单号" align="center" prop="inspectionNo" />
      <el-table-column label="批次号" align="center" prop="batchNo" />
      <el-table-column label="产品ID" align="center" prop="productId" />
      <el-table-column label="工序ID" align="center" prop="processId" />
      <el-table-column label="工单ID" align="center" prop="workOrderId" />
      <el-table-column label="检验类型：1-进料检验，2-过程检验，3-成品检验" align="center" prop="inspectionType" />
      <el-table-column label="检验级别：1-全检，2-抽检" align="center" prop="inspectionLevel" />
      <el-table-column label="批量大小" align="center" prop="lotSize" />
      <el-table-column label="样本数量" align="center" prop="sampleSize" />
      <el-table-column label="合格数量" align="center" prop="qualifiedQuantity" />
      <el-table-column label="不合格数量" align="center" prop="rejectedQuantity" />
      <el-table-column label="报废数量" align="center" prop="scrapQuantity" />
      <el-table-column label="检验结果" align="center" prop="inspectionResult">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_QUALITY_STATUS" :value="scope.row.inspectionResult" />
        </template>
      </el-table-column>
      <el-table-column label="检验员ID" align="center" prop="inspectorId" />
      <el-table-column
        label="检验时间"
        align="center"
        prop="inspectionTime"
        :formatter="dateFormatter"
        width="180px"
      />
      <el-table-column label="检验环境" align="center" prop="environment" />
      <el-table-column label="检验设备" align="center" prop="equipment" />
      <el-table-column label="检验备注" align="center" prop="remark" />
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
            v-hasPermi="['erp:quality-inspection:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:quality-inspection:delete']"
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
  <QualityInspectionForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { QualityInspectionApi, QualityInspection } from '@/api/erp/qualityinspection'
import QualityInspectionForm from './QualityInspectionForm.vue'

/** ERP 质检记录 列表 */
defineOptions({ name: 'QualityInspection' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<QualityInspection[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  inspectionNo: undefined,
  batchNo: undefined,
  productId: undefined,
  processId: undefined,
  workOrderId: undefined,
  inspectionType: undefined,
  inspectionLevel: undefined,
  lotSize: undefined,
  sampleSize: undefined,
  qualifiedQuantity: undefined,
  rejectedQuantity: undefined,
  scrapQuantity: undefined,
  inspectionResult: undefined,
  inspectorId: undefined,
  inspectionTime: [],
  environment: undefined,
  equipment: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QualityInspectionApi.getQualityInspectionPage(queryParams)
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
    await QualityInspectionApi.deleteQualityInspection(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 质检记录 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await QualityInspectionApi.deleteQualityInspectionList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: QualityInspection[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await QualityInspectionApi.exportQualityInspection(queryParams)
    download.excel(data, 'ERP 质检记录.xls')
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