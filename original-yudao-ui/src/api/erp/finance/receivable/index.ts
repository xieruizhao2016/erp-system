import request from '@/config/axios'

// ERP 应收账款 VO
export interface ReceivableVO {
  id: number // 编号
  no: string // 单据号
  customerId: number // 客户编号
  orderId: number // 销售订单编号
  amount: number // 应收金额，单位：元
  receivedAmount: number // 已收金额，单位：元
  balance: number // 余额，单位：元
  dueDate: string // 到期日
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
  customerName?: string // 客户名称（关联字段）
  orderNo?: string // 销售订单号（关联字段）
}

// ERP 应收账款 API
export const ReceivableApi = {
  // 查询应收账款分页
  getReceivablePage: async (params: any) => {
    return await request.get({ url: `/erp/finance-receivable/page`, params })
  },

  // 查询应收账款详情
  getReceivable: async (id: number) => {
    return await request.get({ url: `/erp/finance-receivable/get`, params: { id } })
  },

  // 新增应收账款
  createReceivable: async (data: ReceivableVO) => {
    return await request.post({ url: `/erp/finance-receivable/create`, data })
  },

  // 修改应收账款
  updateReceivable: async (data: ReceivableVO) => {
    return await request.put({ url: `/erp/finance-receivable/update`, data })
  },

  // 删除应收账款
  deleteReceivable: async (id: number) => {
    return await request.delete({ url: `/erp/finance-receivable/delete`, params: { id } })
  },

  // 批量删除应收账款
  deleteReceivableList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-receivable/delete-list`, params: { ids } })
  },

  // 导出应收账款 Excel
  exportReceivable: async (params: any) => {
    return await request.download({ url: `/erp/finance-receivable/export-excel`, params })
  }
}

