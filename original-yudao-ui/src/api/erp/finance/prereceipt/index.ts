import request from '@/config/axios'

// ERP 预收款 VO
export interface PrereceiptVO {
  id: number // 编号
  no: string // 单据号
  customerId: number // 客户编号
  orderId: number // 销售订单编号（可选）
  amount: number // 预收款金额，单位：元
  usedAmount: number // 已使用金额，单位：元
  balance: number // 余额，单位：元
  prereceiptDate: string // 预收日期
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
  customerName?: string // 客户名称（关联字段）
  orderNo?: string // 销售订单号（关联字段）
}

// ERP 预收款 API
export const PrereceiptApi = {
  // 查询预收款分页
  getPrereceiptPage: async (params: any) => {
    return await request.get({ url: `/erp/finance-prereceipt/page`, params })
  },

  // 查询预收款详情
  getPrereceipt: async (id: number) => {
    return await request.get({ url: `/erp/finance-prereceipt/get`, params: { id } })
  },

  // 新增预收款
  createPrereceipt: async (data: PrereceiptVO) => {
    return await request.post({ url: `/erp/finance-prereceipt/create`, data })
  },

  // 修改预收款
  updatePrereceipt: async (data: PrereceiptVO) => {
    return await request.put({ url: `/erp/finance-prereceipt/update`, data })
  },

  // 删除预收款
  deletePrereceipt: async (id: number) => {
    return await request.delete({ url: `/erp/finance-prereceipt/delete`, params: { id } })
  },

  // 批量删除预收款
  deletePrereceiptList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-prereceipt/delete-list`, params: { ids } })
  },

  // 导出预收款 Excel
  exportPrereceipt: async (params: any) => {
    return await request.download({ url: `/erp/finance-prereceipt/export-excel`, params })
  }
}

