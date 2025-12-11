import request from '@/utils/request'

// 创建内部入库单
export function createStockInternalIn(data) {
  return request({
    url: '/erp/stock-internal-in/create',
    method: 'post',
    data: data
  })
}

// 更新内部入库单
export function updateStockInternalIn(data) {
  return request({
    url: '/erp/stock-internal-in/update',
    method: 'put',
    data: data
  })
}

// 删除内部入库单
export function deleteStockInternalIn(id) {
  return request({
    url: '/erp/stock-internal-in/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除内部入库单 */
export function deleteStockInternalInList(ids) {
  return request({
    url: `/erp/stock-internal-in/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得内部入库单
export function getStockInternalIn(id) {
  return request({
    url: '/erp/stock-internal-in/get?id=' + id,
    method: 'get'
  })
}

// 获得内部入库单分页
export function getStockInternalInPage(params) {
  return request({
    url: '/erp/stock-internal-in/page',
    method: 'get',
    params
  })
}
// 导出内部入库单 Excel
export function exportStockInternalInExcel(params) {
  return request({
    url: '/erp/stock-internal-in/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}