<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="120px"
    >
      <el-form-item label="设备编号" prop="equipmentNo">
        <el-input
          v-model="queryParams.equipmentNo"
          placeholder="请输入设备编号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="设备名称" prop="equipmentName">
        <el-input
          v-model="queryParams.equipmentName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="设备类型" prop="equipmentType">
        <el-select
          v-model="queryParams.equipmentType"
          placeholder="请选择设备类型"
          clearable
          class="!w-240px"
        >
          <el-option label="全部" value="" />
        </el-select>
      </el-form-item>
      <el-form-item label="设备型号" prop="model">
        <el-input
          v-model="queryParams.model"
          placeholder="请输入设备型号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="制造商" prop="manufacturer">
        <el-input
          v-model="queryParams.manufacturer"
          placeholder="请输入制造商"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="序列号" prop="serialNumber">
        <el-input
          v-model="queryParams.serialNumber"
          placeholder="请输入序列号"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="购置日期" prop="purchaseDate">
        <el-date-picker
          v-model="queryParams.purchaseDate"
          value-format="YYYY-MM-DD HH:mm:ss"
          type="daterange"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
          class="!w-220px"
        />
      </el-form-item>
      <el-form-item label="购置价格" prop="purchasePrice">
        <el-input
          v-model="queryParams.purchasePrice"
          placeholder="请输入购置价格"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="设计寿命" prop="serviceLife">
        <el-input
          v-model="queryParams.serviceLife"
          placeholder="请输入设计寿命（年）"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="工作中心" prop="workCenterId">
        <el-input
          v-model="queryParams.workCenterId"
          placeholder="请输入工作中心"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="设备位置" prop="location">
        <el-input
          v-model="queryParams.location"
          placeholder="请输入设备位置"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="产能" prop="capacity">
        <el-input
          v-model="queryParams.capacity"
          placeholder="请输入产能（小时/天）"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="效率系数" prop="efficiencyRate">
        <el-input
          v-model="queryParams.efficiencyRate"
          placeholder="请输入效率系数"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择状态"
          clearable
          class="!w-240px"
        >
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.ERP_EQUIPMENT_STATUS)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="责任人" prop="responsiblePerson">
        <el-input
          v-model="queryParams.responsiblePerson"
          placeholder="请输入责任人"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="技术规格" prop="specification">
        <el-input
          v-model="queryParams.specification"
          placeholder="请输入技术规格"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
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
          v-hasPermi="['erp:equipment:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:equipment:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:equipment:delete']"
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
      <el-table-column label="设备编号" align="center" prop="equipmentNo" />
      <el-table-column label="设备名称" align="center" prop="equipmentName" />
      <el-table-column label="设备类型" align="center" prop="equipmentType" />
      <el-table-column label="设备型号" align="center" prop="model" />
      <el-table-column label="制造商" align="center" prop="manufacturer" />
      <el-table-column label="序列号" align="center" prop="serialNumber" />
      <el-table-column label="购置日期" align="center" prop="purchaseDate" />
      <el-table-column label="购置价格" align="center" prop="purchasePrice" />
      <el-table-column label="设计寿命" align="center" prop="serviceLife" />
      <el-table-column label="工作中心" align="center" prop="workCenterId" />
      <el-table-column label="设备位置" align="center" prop="location" />
      <el-table-column label="产能" align="center" prop="capacity" />
      <el-table-column label="效率系数" align="center" prop="efficiencyRate" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.ERP_EQUIPMENT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="责任人" align="center" prop="responsiblePerson" />
      <el-table-column label="技术规格" align="center" prop="specification" />
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
            v-hasPermi="['erp:equipment:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:equipment:delete']"
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
  <EquipmentForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { EquipmentApi, Equipment } from '@/api/erp/equipment'
import EquipmentForm from './EquipmentForm.vue'

/** ERP 设备台账 列表 */
defineOptions({ name: 'Equipment' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<Equipment[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  equipmentNo: undefined,
  equipmentName: undefined,
  equipmentType: undefined,
  model: undefined,
  manufacturer: undefined,
  serialNumber: undefined,
  purchaseDate: [],
  purchasePrice: undefined,
  serviceLife: undefined,
  workCenterId: undefined,
  location: undefined,
  capacity: undefined,
  efficiencyRate: undefined,
  status: undefined,
  responsiblePerson: undefined,
  specification: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await EquipmentApi.getEquipmentPage(queryParams)
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
    await EquipmentApi.deleteEquipment(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP 设备台账 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await EquipmentApi.deleteEquipmentList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: Equipment[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await EquipmentApi.exportEquipment(queryParams)
    download.excel(data, 'ERP 设备台账.xls')
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