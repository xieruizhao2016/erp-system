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
      <el-form-item label="BOM ID" prop="bomId">
        <el-input
          v-model="queryParams.bomId"
          placeholder="请输入BOM ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="父产品ID" prop="parentProductId">
        <el-input
          v-model="queryParams.parentProductId"
          placeholder="请输入父产品ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="子产品ID" prop="childProductId">
        <el-input
          v-model="queryParams.childProductId"
          placeholder="请输入子产品ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="子产品名称" prop="childProductName">
        <el-input
          v-model="queryParams.childProductName"
          placeholder="请输入子产品名称"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="子产品规格" prop="childProductSpec">
        <el-input
          v-model="queryParams.childProductSpec"
          placeholder="请输入子产品规格"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="单位ID" prop="unitId">
        <el-input
          v-model="queryParams.unitId"
          placeholder="请输入单位ID"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="用量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入用量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="损耗率" prop="lossRate">
        <el-input
          v-model="queryParams.lossRate"
          placeholder="请输入损耗率"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="有效用量" prop="effectiveQuantity">
        <el-input
          v-model="queryParams.effectiveQuantity"
          placeholder="请输入有效用量"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="是否关键物料" prop="isKeyMaterial">
        <el-select
          v-model="queryParams.isKeyMaterial"
          placeholder="请选择是否关键物料"
          clearable
          class="!w-240px"
        >
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item label="是否替代料" prop="isAlternative">
        <el-select v-model="queryParams.isAlternative" placeholder="请选择是否替代料" clearable class="!w-240px">
          <el-option label="是" :value="true" />
          <el-option label="否" :value="false" />
        </el-select>
      </el-form-item>
      <el-form-item label="替代料组" prop="alternativeGroup">
        <el-input
          v-model="queryParams.alternativeGroup"
          placeholder="请输入替代料组"
          clearable
          @keyup.enter="handleQuery"
          class="!w-240px"
        />
      </el-form-item>
      <el-form-item label="位号" prop="position">
        <el-input
          v-model="queryParams.position"
          placeholder="请输入位号"
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
          v-hasPermi="['erp:product-bom-item:create']"
        >
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button
          type="success"
          plain
          @click="handleExport"
          :loading="exportLoading"
          v-hasPermi="['erp:product-bom-item:export']"
        >
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
        <el-button
            type="danger"
            plain
            :disabled="isEmpty(checkedIds)"
            @click="handleDeleteBatch"
            v-hasPermi="['erp:product-bom-item:delete']"
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
      <el-table-column label="BOM ID" align="center" prop="bomId" />
      <el-table-column label="父产品ID" align="center" prop="parentProductId" />
      <el-table-column label="子产品ID" align="center" prop="childProductId" />
      <el-table-column label="子产品名称" align="center" prop="childProductName" />
      <el-table-column label="子产品规格" align="center" prop="childProductSpec" />
      <el-table-column label="单位ID" align="center" prop="unitId" />
      <el-table-column label="用量" align="center" prop="quantity" />
      <el-table-column label="损耗率" align="center" prop="lossRate" />
      <el-table-column label="有效用量" align="center" prop="effectiveQuantity" />
      <el-table-column label="是否关键物料" align="center" prop="isKeyMaterial" />
      <el-table-column label="是否替代料" align="center" prop="isAlternative" />
      <el-table-column label="替代料组" align="center" prop="alternativeGroup" />
      <el-table-column label="位号" align="center" prop="position" />
      <el-table-column label="工序ID" align="center" prop="processId" />
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
            v-hasPermi="['erp:product-bom-item:update']"
          >
            编辑
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['erp:product-bom-item:delete']"
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
  <ProductBomItemForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { isEmpty } from '@/utils/is'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ProductBomItemApi, ProductBomItem } from '@/api/erp/productbomitem'
import ProductBomItemForm from './ProductBomItemForm.vue'

/** ERP BOM明细 列表 */
defineOptions({ name: 'ProductBomItem' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ProductBomItem[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  bomId: undefined,
  parentProductId: undefined,
  childProductId: undefined,
  childProductName: undefined,
  childProductSpec: undefined,
  unitId: undefined,
  quantity: undefined,
  lossRate: undefined,
  effectiveQuantity: undefined,
  isKeyMaterial: undefined,
  isAlternative: undefined,
  alternativeGroup: undefined,
  position: undefined,
  processId: undefined,
  remark: undefined,
  createTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ProductBomItemApi.getProductBomItemPage(queryParams)
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
    await ProductBomItemApi.deleteProductBomItem(id)
    message.success(t('common.delSuccess'))
    currentRow.value = {}
    // 刷新列表
    await getList()
  } catch {}
}

/** 批量删除ERP BOM明细 */
const handleDeleteBatch = async () => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    await ProductBomItemApi.deleteProductBomItemList(checkedIds.value);
    checkedIds.value = [];
    message.success(t('common.delSuccess'))
    await getList();
  } catch {}
}

const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (records: ProductBomItem[]) => {
  checkedIds.value = records.map((item) => item.id!);
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ProductBomItemApi.exportProductBomItem(queryParams)
    download.excel(data, 'ERP BOM明细.xls')
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