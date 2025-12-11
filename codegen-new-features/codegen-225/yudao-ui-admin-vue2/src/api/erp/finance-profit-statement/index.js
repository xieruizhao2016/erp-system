import request from '@/utils/request'

// 创建利润表
export function createFinanceProfitStatement(data) {
  return request({
    url: '/erp/finance-profit-statement/create',
    method: 'post',
    data: data
  })
}

// 更新利润表
export function updateFinanceProfitStatement(data) {
  return request({
    url: '/erp/finance-profit-statement/update',
    method: 'put',
    data: data
  })
}

// 删除利润表
export function deleteFinanceProfitStatement(id) {
  return request({
    url: '/erp/finance-profit-statement/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除利润表 */
export function deleteFinanceProfitStatementList(ids) {
  return request({
    url: `/erp/finance-profit-statement/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得利润表
export function getFinanceProfitStatement(id) {
  return request({
    url: '/erp/finance-profit-statement/get?id=' + id,
    method: 'get'
  })
}

// 获得利润表分页
export function getFinanceProfitStatementPage(params) {
  return request({
    url: '/erp/finance-profit-statement/page',
    method: 'get',
    params
  })
}
// 导出利润表 Excel
export function exportFinanceProfitStatementExcel(params) {
  return request({
    url: '/erp/finance-profit-statement/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}