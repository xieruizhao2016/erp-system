import request from '@/utils/request'

// 创建资产负债表
export function createFinanceBalanceSheet(data) {
  return request({
    url: '/erp/finance-balance-sheet/create',
    method: 'post',
    data: data
  })
}

// 更新资产负债表
export function updateFinanceBalanceSheet(data) {
  return request({
    url: '/erp/finance-balance-sheet/update',
    method: 'put',
    data: data
  })
}

// 删除资产负债表
export function deleteFinanceBalanceSheet(id) {
  return request({
    url: '/erp/finance-balance-sheet/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除资产负债表 */
export function deleteFinanceBalanceSheetList(ids) {
  return request({
    url: `/erp/finance-balance-sheet/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得资产负债表
export function getFinanceBalanceSheet(id) {
  return request({
    url: '/erp/finance-balance-sheet/get?id=' + id,
    method: 'get'
  })
}

// 获得资产负债表分页
export function getFinanceBalanceSheetPage(params) {
  return request({
    url: '/erp/finance-balance-sheet/page',
    method: 'get',
    params
  })
}
// 导出资产负债表 Excel
export function exportFinanceBalanceSheetExcel(params) {
  return request({
    url: '/erp/finance-balance-sheet/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}