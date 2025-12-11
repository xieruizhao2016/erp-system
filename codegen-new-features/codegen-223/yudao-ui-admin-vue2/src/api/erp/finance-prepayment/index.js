import request from '@/utils/request'

// 创建预付款
export function createFinancePrepayment(data) {
  return request({
    url: '/erp/finance-prepayment/create',
    method: 'post',
    data: data
  })
}

// 更新预付款
export function updateFinancePrepayment(data) {
  return request({
    url: '/erp/finance-prepayment/update',
    method: 'put',
    data: data
  })
}

// 删除预付款
export function deleteFinancePrepayment(id) {
  return request({
    url: '/erp/finance-prepayment/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除预付款 */
export function deleteFinancePrepaymentList(ids) {
  return request({
    url: `/erp/finance-prepayment/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得预付款
export function getFinancePrepayment(id) {
  return request({
    url: '/erp/finance-prepayment/get?id=' + id,
    method: 'get'
  })
}

// 获得预付款分页
export function getFinancePrepaymentPage(params) {
  return request({
    url: '/erp/finance-prepayment/page',
    method: 'get',
    params
  })
}
// 导出预付款 Excel
export function exportFinancePrepaymentExcel(params) {
  return request({
    url: '/erp/finance-prepayment/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}