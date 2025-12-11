import request from '@/utils/request'

// 创建内部出库单
export function createStockInternalOut(data) {
  return request({
    url: '/erp/stock-internal-out/create',
    method: 'post',
    data: data
  })
}

// 更新内部出库单
export function updateStockInternalOut(data) {
  return request({
    url: '/erp/stock-internal-out/update',
    method: 'put',
    data: data
  })
}

// 删除内部出库单
export function deleteStockInternalOut(id) {
  return request({
    url: '/erp/stock-internal-out/delete?id=' + id,
    method: 'delete'
  })
}

/** 批量删除内部出库单 */
export function deleteStockInternalOutList(ids) {
  return request({
    url: `/erp/stock-internal-out/delete-list?ids=${ids.join(',')}`,
    method: 'delete'
  })
}

// 获得内部出库单
export function getStockInternalOut(id) {
  return request({
    url: '/erp/stock-internal-out/get?id=' + id,
    method: 'get'
  })
}

// 获得内部出库单分页
export function getStockInternalOutPage(params) {
  return request({
    url: '/erp/stock-internal-out/page',
    method: 'get',
    params
  })
}
// 导出内部出库单 Excel
export function exportStockInternalOutExcel(params) {
  return request({
    url: '/erp/stock-internal-out/export-excel',
    method: 'get',
    params,
    responseType: 'blob'
  })
}