import request from '@/config/axios'

// ERP 内部入库单 VO
export interface StockInternalInVO {
  id: number // 编号
  no: string // 入库单号
  employeeId: number // 员工编号
  deptId: number // 部门编号
  internalType: number // 内部类型（1-部门调拨，2-员工领料，3-生产产品，4-其他）
  inTime: string // 入库时间
  totalCount: number // 合计数量
  totalPrice: number // 合计金额，单位：元
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  fileUrl: string // 附件 URL
  createTime: string // 创建时间
  employeeName?: string // 员工名称（关联字段）
  deptName?: string // 部门名称（关联字段）
}

// ERP 内部入库单 API
export const StockInternalInApi = {
  // 查询内部入库单分页
  getStockInternalInPage: async (params: any) => {
    return await request.get({ url: `/erp/stock-internal-in/page`, params })
  },

  // 查询内部入库单详情
  getStockInternalIn: async (id: number) => {
    return await request.get({ url: `/erp/stock-internal-in/get?id=` + id })
  },

  // 新增内部入库单
  createStockInternalIn: async (data: StockInternalInVO) => {
    return await request.post({ url: `/erp/stock-internal-in/create`, data })
  },

  // 修改内部入库单
  updateStockInternalIn: async (data: StockInternalInVO) => {
    return await request.put({ url: `/erp/stock-internal-in/update`, data })
  },

  // 删除内部入库单
  deleteStockInternalIn: async (id: number) => {
    return await request.delete({ url: `/erp/stock-internal-in/delete?id=` + id })
  },

  // 批量删除内部入库单
  deleteStockInternalInList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/stock-internal-in/delete-list?ids=${ids.join(',')}` })
  },

  // 审核内部入库单
  updateStockInternalInStatus: async (id: number, status: number) => {
    if (id === undefined || id === null || status === undefined || status === null) {
      throw new Error('参数不能为空：id 和 status 都是必需的')
    }
    const params = new URLSearchParams()
    params.append('id', String(id))
    params.append('status', String(status))
    return await request.put({
      url: `/erp/stock-internal-in/update-status?${params.toString()}`
    })
  },

  // 导出内部入库单 Excel
  exportStockInternalIn: async (params: any) => {
    return await request.download({ url: `/erp/stock-internal-in/export-excel`, params })
  }
}

