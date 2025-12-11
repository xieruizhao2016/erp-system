import request from '@/utils/request'

// 创建预收款
export function createFinancePrereceipt(data) {
  return request({
    url: '/erp/finance-prereceipt/create',
    method: 'post',
    data: data
  })
}

// 更新预收款
export function updateFinancePrereceipt(data) {
  return request({
    url: '/erp/finance-prereceipt/update',
    method: 'put',
    data: data
  })
}

// 删除预收款
export function deleteFinancePrereceipt(id) {
  return request({
    url: '/erp/finance-prereceipt/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除预收款 */
export function deleteFinancePrereceiptList(ids) {
  return request({
    url: `/erp/finance-prereceipt/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得预收款
export function getFinancePrereceipt(id) {
  return request({
    url: '/erp/finance-prereceipt/get?id=' + id,
    method: 'get'
  })
}

// 获得预收款分页
export function getFinancePrereceiptPage(params) {
  return request({
    url: '/erp/finance-prereceipt/page',
    method: 'get',
    params
  })
}
// 导出预收款 Excel
export function exportFinancePrereceiptExcel(params) {
  return request({
    url: '/erp/finance-prereceipt/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}