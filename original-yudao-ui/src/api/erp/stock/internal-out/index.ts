import request from '@/config/axios'

// ERP 内部出库单 VO
export interface StockInternalOutVO {
  id: number // 编号
  no: string // 出库单号
  employeeId: number // 员工编号
  deptId: number // 部门编号
  internalType: number // 内部类型（1-部门调拨，2-员工领料，3-生产产品，4-其他）
  outTime: string // 出库时间
  totalCount: number // 合计数量
  totalPrice: number // 合计金额，单位：元
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  fileUrl: string // 附件 URL
  createTime: string // 创建时间
  employeeName?: string // 员工名称（关联字段）
  deptName?: string // 部门名称（关联字段）
}

// ERP 内部出库单 API
export const StockInternalOutApi = {
  // 查询内部出库单分页
  getStockInternalOutPage: async (params: any) => {
    return await request.get({ url: `/erp/stock-internal-out/page`, params })
  },

  // 查询内部出库单详情
  getStockInternalOut: async (id: number) => {
    return await request.get({ url: `/erp/stock-internal-out/get?id=` + id })
  },

  // 新增内部出库单
  createStockInternalOut: async (data: StockInternalOutVO) => {
    return await request.post({ url: `/erp/stock-internal-out/create`, data })
  },

  // 修改内部出库单
  updateStockInternalOut: async (data: StockInternalOutVO) => {
    return await request.put({ url: `/erp/stock-internal-out/update`, data })
  },

  // 删除内部出库单
  deleteStockInternalOut: async (id: number) => {
    return await request.delete({ url: `/erp/stock-internal-out/delete?id=` + id })
  },

  // 批量删除内部出库单
  deleteStockInternalOutList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/stock-internal-out/delete-list?ids=${ids.join(',')}` })
  },

  // 审核内部出库单
  updateStockInternalOutStatus: async (id: number, status: number) => {
    if (id === undefined || id === null || status === undefined || status === null) {
      throw new Error('参数不能为空：id 和 status 都是必需的')
    }
    const params = new URLSearchParams()
    params.append('id', String(id))
    params.append('status', String(status))
    return await request.put({
      url: `/erp/stock-internal-out/update-status?${params.toString()}`
    })
  },

  // 导出内部出库单 Excel
  exportStockInternalOut: async (params: any) => {
    return await request.download({ url: `/erp/stock-internal-out/export-excel`, params })
  }
}

