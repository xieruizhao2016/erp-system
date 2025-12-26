<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>采购订单导入</span>
          <el-button @click="goBack">
            <Icon icon="ep:back" class="mr-5px" /> 返回
          </el-button>
        </div>
      </template>

      <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :auto-upload="false"
        :disabled="formLoading"
        :limit="1"
        :on-exceed="handleExceed"
        accept=".xlsx, .xls"
        action="none"
        drag
        class="upload-container"
      >
        <Icon icon="ep:upload" style="font-size: 48px; color: var(--el-color-primary)" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip text-center">
            <div class="el-upload__tip">
              <el-checkbox v-model="updateSupport" />
              是否更新已经存在的采购订单数据（"订单号"重复）
            </div>
            <span>仅允许导入 xls、xlsx 格式文件。</span>
            <el-link
              :underline="false"
              style="font-size: 12px; vertical-align: baseline; margin-left: 10px"
              type="primary"
              @click="importTemplate"
            >
              下载模板
            </el-link>
          </div>
        </template>
      </el-upload>

      <div class="mt-20px text-center">
        <el-button :disabled="formLoading" type="primary" size="large" @click="submitForm">
          <Icon icon="ep:upload" class="mr-5px" /> 开始导入
        </el-button>
        <el-button size="large" @click="goBack">取 消</el-button>
      </div>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import { PurchaseOrderApi } from '@/api/erp/purchase/order'
import download from '@/utils/download'
import type { UploadUserFile } from 'element-plus'

defineOptions({ name: 'ErpPurchaseOrderImport' })

const message = useMessage() // 消息弹窗
const router = useRouter() // 路由

const formLoading = ref(false) // 表单的加载中
const uploadRef = ref()
const fileList = ref<UploadUserFile[]>([]) // 文件列表
const updateSupport = ref(false) // 是否更新已经存在的采购订单数据

/** 返回 */
const goBack = () => {
  router.back()
}

/** 提交表单 */
const submitForm = async () => {
  if (fileList.value.length == 0) {
    message.error('请上传文件')
    return
  }

  formLoading.value = true
  try {
    const formData = new FormData()
    formData.append('updateSupport', String(updateSupport.value))
    formData.append('file', fileList.value[0].raw as Blob)
    const res = await PurchaseOrderApi.importPurchaseOrder(formData)
    submitFormSuccess(res)
  } catch (error: any) {
    // 显示详细的错误信息
    console.error('采购订单导入失败:', error)
    if (error?.response?.data?.msg) {
      message.error('导入失败：' + error.response.data.msg)
    } else if (error?.message) {
      message.error('导入失败：' + error.message)
    } else {
      submitFormError()
    }
  } finally {
    formLoading.value = false
  }
}

/** 文件上传成功 */
const submitFormSuccess = (response: any) => {
  // service 拦截器在成功时返回的是 CommonResult 对象 { code: 0, data: {...}, msg: "..." }
  // 检查响应格式
  if (!response) {
    message.error('导入失败：响应数据为空')
    formLoading.value = false
    return
  }
  
  // 如果 response 有 code 字段，说明是 CommonResult 格式
  if (response.code !== undefined && response.code !== null) {
    if (response.code !== 0) {
      message.error(response.msg || '导入失败')
      formLoading.value = false
      return
    }
    // 提取 data 字段
    if (!response.data) {
      message.error('导入失败：响应数据格式错误')
      formLoading.value = false
      return
    }
    response = response.data
  }
  
  // 此时 response 应该是 ErpPurchaseOrderImportRespVO 对象
  // 后端返回的字段名是：createOrderNos, updateOrderNos, failureOrderNos
  if (!response.createOrderNos || !response.updateOrderNos || !response.failureOrderNos) {
    message.error('导入失败：响应数据格式不正确')
    formLoading.value = false
    return
  }
  
  // 拼接提示语
  const data = response
  let text = '上传成功数量：' + (data.createOrderNos?.length || 0) + ';'
  if (data.createOrderNos && data.createOrderNos.length > 0) {
    for (let orderNo of data.createOrderNos) {
      text += '< ' + orderNo + ' >'
    }
  }
  text += '更新成功数量：' + (data.updateOrderNos?.length || 0) + ';'
  if (data.updateOrderNos && data.updateOrderNos.length > 0) {
    for (const orderNo of data.updateOrderNos) {
      text += '< ' + orderNo + ' >'
    }
  }
  text += '更新失败数量：' + (Object.keys(data.failureOrderNos || {}).length || 0) + ';'
  if (data.failureOrderNos && Object.keys(data.failureOrderNos).length > 0) {
    for (const orderNo in data.failureOrderNos) {
      text += '< ' + orderNo + ': ' + data.failureOrderNos[orderNo] + ' >'
    }
  }
  message.alert(text).then(() => {
    // 导入成功后清空上传的文件，停留在当前页面
    fileList.value = []
    updateSupport.value = false
    // 清空上传组件的文件列表
    uploadRef.value?.clearFiles()
  })
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

/** 下载模板操作 */
const importTemplate = async () => {
  try {
  const res = await PurchaseOrderApi.importPurchaseOrderTemplate()
  download.excel(res, '采购订单导入模版.xls')
  } catch (error) {
    console.error('下载模板失败:', error)
    message.error('下载模板失败，请稍后重试')
  }
}
</script>

<style lang="scss" scoped>
.upload-container {
  margin: 40px 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>

