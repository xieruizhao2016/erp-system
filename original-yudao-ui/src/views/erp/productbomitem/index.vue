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
      <el-form-item label="BOM" prop="bomId">
        <el-select
          v-model="queryParams.bomId"
          clearable
          filterable
          placeholder="请选择BOM"
          class="!w-240px"
        >
          <el-option
            v-for="item in productBomList"
            :key="item.id"
            :label="item.bomName || item.bomNo || `BOM${item.id}`"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="父产品" prop="parentProductId">
        <el-select
          v-model="queryParams.parentProductId"
          clearable
          filterable
          placeholder="请选择父产品"
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="子产品" prop="childProductId">
        <el-select
          v-model="queryParams.childProductId"
          clearable
          filterable
          placeholder="请选择子产品"
          class="!w-240px"
        >
          <el-option
            v-for="item in productList"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
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
      <el-form-item label="工序" prop="processId">
        <el-select
          v-model="queryParams.processId"
          clearable
          filterable
          placeholder="请选择工序"
          class="!w-240px"
        >
          <el-option
            v-for="item in processList"
            :key="item.id"
            :label="item.processName || item.processNo || `工序${item.id}`"
            :value="item.id"
          />
        </el-select>
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
      <el-table-column label="BOM名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductBomName(scope.row.bomId) }}
        </template>
      </el-table-column>
      <el-table-column label="父产品名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductName(scope.row.parentProductId) }}
        </template>
      </el-table-column>
      <el-table-column label="子产品名称" align="center" min-width="120">
        <template #default="scope">
          {{ getProductName(scope.row.childProductId) }}
        </template>
      </el-table-column>
      <el-table-column label="子产品规格" align="center" prop="childProductSpec" />
      <el-table-column label="单位" align="center" min-width="100">
        <template #default="scope">
          {{ getProductUnitName(scope.row.unitId) }}
        </template>
      </el-table-column>
      <el-table-column label="用量" align="center" prop="quantity" />
      <el-table-column label="损耗率" align="center" prop="lossRate" />
      <el-table-column label="有效用量" align="center" prop="effectiveQuantity" />
      <el-table-column label="是否关键物料" align="center" prop="isKeyMaterial" />
      <el-table-column label="是否替代料" align="center" prop="isAlternative" />
      <el-table-column label="替代料组" align="center" prop="alternativeGroup" />
      <el-table-column label="位号" align="center" prop="position" />
      <el-table-column label="工序名称" align="center" min-width="120" prop="processName">
        <template #default="scope">
          {{ scope.row.processName || '-' }}
        </template>
      </el-table-column>
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
import { ProductBomApi, ProductBom } from '@/api/erp/productbom'
import { ProductApi, ProductVO } from '@/api/erp/product/product'
import { ProductUnitApi, ProductUnit } from '@/api/erp/product/unit'
import { ProcessApi, Process } from '@/api/erp/process'

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
const productBomList = ref<ProductBom[]>([]) // BOM列表
const productList = ref<ProductVO[]>([]) // 产品列表
const productUnitList = ref<ProductUnit[]>([]) // 产品单位列表
const processList = ref<Process[]>([]) // 工序列表（仅用于搜索条件）

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

/** 获取BOM名称 */
const getProductBomName = (id?: number) => {
  if (!id) return '-'
  const bom = productBomList.value.find(item => item.id === id)
  return bom?.bomName || bom?.bomNo || `BOM${id}`
}

/** 获取产品名称 */
const getProductName = (id?: number) => {
  if (!id) return '-'
  const product = productList.value.find(item => item.id === id)
  return product?.name || `产品${id}`
}

/** 获取产品单位名称 */
const getProductUnitName = (id?: number) => {
  if (!id) return '-'
  const unit = productUnitList.value.find(item => item.id === id)
  return unit?.name || `单位${id}`
}

// 已移除 getProcessName 函数，直接使用 processName 字段（冗余字段）

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 加载BOM、产品、单位、工序列表（工序列表仅用于搜索条件）
  try {
    const [bomData, products, unitData, processes] = await Promise.all([
      ProductBomApi.getProductBomPage({ pageNo: 1, pageSize: 100 }),
      ProductApi.getProductSimpleList(),
      ProductUnitApi.getProductUnitPage({ pageNo: 1, pageSize: 100 }),
      ProcessApi.getProcessList()
    ])
    productBomList.value = bomData.list || []
    productList.value = products || []
    productUnitList.value = unitData.list || []
    processList.value = processes || []
  } catch (error) {
    console.error('加载列表数据失败:', error)
  }
})
</script>