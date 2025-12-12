import request from '@/config/axios'

// ERP 预付款 VO
export interface PrepaymentVO {
  id: number // 编号
  no: string // 单据号
  supplierId: number // 供应商编号
  orderId: number // 采购订单编号（可选）
  amount: number // 预付款金额，单位：元
  usedAmount: number // 已使用金额，单位：元
  balance: number // 余额，单位：元
  prepayDate: string // 预付日期
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
  supplierName?: string // 供应商名称（关联字段）
  orderNo?: string // 采购订单号（关联字段）
}

// ERP 预付款 API
export const PrepaymentApi = {
  // 查询预付款分页
  getPrepaymentPage: async (params: any) => {
    return await request.get({ url: `/erp/finance-prepayment/page`, params })
  },

  // 查询预付款详情
  getPrepayment: async (id: number) => {
    return await request.get({ url: `/erp/finance-prepayment/get`, params: { id } })
  },

  // 新增预付款
  createPrepayment: async (data: PrepaymentVO) => {
    return await request.post({ url: `/erp/finance-prepayment/create`, data })
  },

  // 修改预付款
  updatePrepayment: async (data: PrepaymentVO) => {
    return await request.put({ url: `/erp/finance-prepayment/update`, data })
  },

  // 删除预付款
  deletePrepayment: async (id: number) => {
    return await request.delete({ url: `/erp/finance-prepayment/delete`, params: { id } })
  },

  // 批量删除预付款
  deletePrepaymentList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-prepayment/delete-list`, params: { ids } })
  },

  // 导出预付款 Excel
  exportPrepayment: async (params: any) => {
    return await request.download({ url: `/erp/finance-prepayment/export-excel`, params })
  }
}

