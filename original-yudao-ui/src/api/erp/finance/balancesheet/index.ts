import request from '@/config/axios'

// ERP 资产负债表 VO
export interface BalanceSheetVO {
  id: number // 编号
  periodDate: string // 期间日期（年月）
  assetTotal: number // 资产总计，单位：元
  liabilityTotal: number // 负债总计，单位：元
  equityTotal: number // 所有者权益总计，单位：元
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
}

// ERP 资产负债表 API
export const BalanceSheetApi = {
  // 查询资产负债表分页
  getBalanceSheetPage: async (params: any) => {
    return await request.get({ url: `/erp/finance-balance-sheet/page`, params })
  },

  // 查询资产负债表详情
  getBalanceSheet: async (id: number) => {
    return await request.get({ url: `/erp/finance-balance-sheet/get`, params: { id } })
  },

  // 新增资产负债表
  createBalanceSheet: async (data: BalanceSheetVO) => {
    return await request.post({ url: `/erp/finance-balance-sheet/create`, data })
  },

  // 修改资产负债表
  updateBalanceSheet: async (data: BalanceSheetVO) => {
    return await request.put({ url: `/erp/finance-balance-sheet/update`, data })
  },

  // 删除资产负债表
  deleteBalanceSheet: async (id: number) => {
    return await request.delete({ url: `/erp/finance-balance-sheet/delete`, params: { id } })
  },

  // 批量删除资产负债表
  deleteBalanceSheetList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-balance-sheet/delete-list`, params: { ids } })
  },

  // 计算资产负债表
  calculateBalanceSheet: async (periodDate: string) => {
    return await request.post({ url: `/erp/finance-balance-sheet/calculate`, params: { periodDate } })
  },

  // 导出资产负债表 Excel
  exportBalanceSheet: async (params: any) => {
    return await request.download({ url: `/erp/finance-balance-sheet/export-excel`, params })
  }
}

