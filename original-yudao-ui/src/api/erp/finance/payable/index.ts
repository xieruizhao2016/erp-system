import request from '@/config/axios'

// ERP 应付账款 VO
export interface PayableVO {
  id: number // 编号
  no: string // 单据号
  supplierId: number // 供应商编号
  orderId: number // 采购订单编号
  amount: number // 应付金额，单位：元
  paidAmount: number // 已付金额，单位：元
  balance: number // 余额，单位：元
  dueDate: string // 到期日
  status: number // 状态（10-未审核，20-已审核）
  remark: string // 备注
  createTime: string // 创建时间
  supplierName?: string // 供应商名称（关联字段）
  orderNo?: string // 采购订单号（关联字段）
}

// ERP 应付账款 API
export const PayableApi = {
  // 查询应付账款分页
  getPayablePage: async (params: any) => {
    return await request.get({ url: `/erp/finance-payable/page`, params })
  },

  // 查询应付账款详情
  getPayable: async (id: number) => {
    return await request.get({ url: `/erp/finance-payable/get`, params: { id } })
  },

  // 新增应付账款
  createPayable: async (data: PayableVO) => {
    return await request.post({ url: `/erp/finance-payable/create`, data })
  },

  // 修改应付账款
  updatePayable: async (data: PayableVO) => {
    return await request.put({ url: `/erp/finance-payable/update`, data })
  },

  // 删除应付账款
  deletePayable: async (id: number) => {
    return await request.delete({ url: `/erp/finance-payable/delete`, params: { id } })
  },

  // 批量删除应付账款
  deletePayableList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/finance-payable/delete-list`, params: { ids } })
  },

  // 导出应付账款 Excel
  exportPayable: async (params: any) => {
    return await request.download({ url: `/erp/finance-payable/export-excel`, params })
  }
}

