import request from '@/utils/request'

// 创建应收账款
export function createFinanceReceivable(data) {
  return request({
    url: '/erp/finance-receivable/create',
    method: 'post',
    data: data
  })
}

// 更新应收账款
export function updateFinanceReceivable(data) {
  return request({
    url: '/erp/finance-receivable/update',
    method: 'put',
    data: data
  })
}

// 删除应收账款
export function deleteFinanceReceivable(id) {
  return request({
    url: '/erp/finance-receivable/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除应收账款 */
export function deleteFinanceReceivableList(ids) {
  return request({
    url: `/erp/finance-receivable/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得应收账款
export function getFinanceReceivable(id) {
  return request({
    url: '/erp/finance-receivable/get?id=' + id,
    method: 'get'
  })
}

// 获得应收账款分页
export function getFinanceReceivablePage(params) {
  return request({
    url: '/erp/finance-receivable/page',
    method: 'get',
    params
  })
}
// 导出应收账款 Excel
export function exportFinanceReceivableExcel(params) {
  return request({
    url: '/erp/finance-receivable/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}