<!-- 销售订单导入窗口 -->
<template>
  <Dialog v-model="dialogVisible" title="销售订单导入" width="400">
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
    >
      <Icon icon="ep:upload" />
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <template #tip>
        <div class="el-upload__tip text-center">
          <div class="el-upload__tip">
            <el-checkbox v-model="updateSupport" />
            是否更新已经存在的订单数据（"订单号"重复）
          </div>
          <span>仅允许导入 xls、xlsx 格式文件。</span>
          <el-link
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            type="primary"
            @click="importTemplate"
          >
            下载模板
          </el-link>
        </div>
      </template>
    </el-upload>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as SaleOrderApi from '@/api/erp/sale/order'
import download from '@/utils/download'
import type { UploadUserFile } from 'element-plus'

defineOptions({ name: 'ErpSaleOrderImportForm' })

const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const formLoading = ref(false) // 表单的加载中
const uploadRef = ref()
const fileList = ref<UploadUserFile[]>([]) // 文件列表
const updateSupport = ref(false) // 是否更新已经存在的订单数据

/** 打开弹窗 */
const open = async () => {
  dialogVisible.value = true
  await resetForm()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

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
    const res = await SaleOrderApi.importSaleOrder(formData)
    submitFormSuccess(res)
  } catch (error: any) {
    // 显示详细的错误信息
    console.error('销售订单导入失败:', error)
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
const emits = defineEmits(['success'])
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
  
  // 此时 response 应该是 ErpSaleOrderImportRespVO 对象
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
    formLoading.value = false
    dialogVisible.value = false
    // 发送操作成功的事件
    emits('success')
  })
}

/** 上传错误提示 */
const submitFormError = (): void => {
  message.error('上传失败，请您重新上传！')
  formLoading.value = false
}

/** 重置表单 */
const resetForm = async () => {
  // 重置上传状态和文件
  fileList.value = []
  updateSupport.value = false
  await nextTick()
  uploadRef.value?.clearFiles()
}

/** 文件数超出提示 */
const handleExceed = (): void => {
  message.error('最多只能上传一个文件！')
}

/** 下载模板操作 */
const importTemplate = async () => {
  const res = await SaleOrderApi.importSaleOrderTemplate()
  download.excel(res, '销售订单导入模版.xls')
}
</script>

