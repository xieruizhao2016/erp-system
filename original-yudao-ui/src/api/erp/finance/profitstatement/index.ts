import request from '@/config/axios'

// ERP 利润表 VO
export interface ProfitStatementVO {
  id: number // 编号
  periodDate: string // 期间日期（年月）
  revenue: number // 营业收入，单位：元
  cost: number // 营业成本，单位：元
  grossProfit: number // 毛利润，单位：元
  operatingExpense: number // 营业费用，单位：元
  netProfit: number // 净利润，单位：元
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
}

// ERP 利润表 API
export const ProfitStatementApi = {
  // 查询利润表分页
  getProfitStatementPage: async (params: any) => {
    return await request.get({ url: `/erp/finance-profit-statement/page`, params })
  },

  // 查询利润表详情
  getProfitStatement: async (id: number) => {
    return await request.get({ url: `/erp/finance-profit-statement/get`, params: { id } })
  },

  // 新增利润表
  createProfitStatement: async (data: ProfitStatementVO) => {
    return await request.post({ url: `/erp/finance-profit-statement/create`, data })
  },

  // 修改利润表
  updateProfitStatement: async (data: ProfitStatementVO) => {
    return await request.put({ url: `/erp/finance-profit-statement/update`, data })
  },

  // 删除利润表
  deleteProfitStatement: async (id: number) => {
    return await request.delete({ url: `/erp/finance-profit-statement/delete`, params: { id } })
  },

  // 批量删除利润表
  deleteProfitStatementList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-profit-statement/delete-list`, params: { ids } })
  },

  // 计算利润表
  calculateProfitStatement: async (periodDate: string) => {
    return await request.post({ url: `/erp/finance-profit-statement/calculate`, params: { periodDate } })
  },

  // 导出利润表 Excel
  exportProfitStatement: async (params: any) => {
    return await request.download({ url: `/erp/finance-profit-statement/export-excel`, params })
  }
}

