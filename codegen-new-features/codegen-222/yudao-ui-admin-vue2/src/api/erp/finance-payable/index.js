import request from '@/utils/request'

// 创建应付账款
export function createFinancePayable(data) {
  return request({
    url: '/erp/finance-payable/create',
    method: 'post',
    data: data
  })
}

// 更新应付账款
export function updateFinancePayable(data) {
  return request({
    url: '/erp/finance-payable/update',
    method: 'put',
    data: data
  })
}

// 删除应付账款
export function deleteFinancePayable(id) {
  return request({
    url: '/erp/finance-payable/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除应付账款 */
export function deleteFinancePayableList(ids) {
  return request({
    url: `/erp/finance-payable/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得应付账款
export function getFinancePayable(id) {
  return request({
    url: '/erp/finance-payable/get?id=' + id,
    method: 'get'
  })
}

// 获得应付账款分页
export function getFinancePayablePage(params) {
  return request({
    url: '/erp/finance-payable/page',
    method: 'get',
    params
  })
}
// 导出应付账款 Excel
export function exportFinancePayableExcel(params) {
  return request({
    url: '/erp/finance-payable/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}