import request from '@/config/axios'

export interface ProductOemVO {
  id: number
  oemCode: string
  oemName: string
  factoryName: string
  factoryCode: string
  factoryAddress: string
  factoryContact: string
  factoryPhone: string
  factoryEmail: string
  productionCapacity: string
  certification: string
  cooperationStartDate: string
  cooperationEndDate: string
  status: number
  qualityLevel: string
  paymentTerms: string
  deliveryTerms: string
  logoUrl: string
  sort: number
  remark: string
  createTime: string
}

// ERP 产品OEM API
export const ProductOemApi = {
  // 查询产品OEM分页
  getPage: (params: any) => {
    return request.get({ url: '/erp/product-oem/page', params })
  },

  // 查询产品OEM精简列表
  getSimpleList: () => {
    return request.get({ url: '/erp/product-oem/simple-list' })
  },

  // 查询产品OEM详情
  get: (id: number) => {
    return request.get({ url: '/erp/product-oem/get?id=' + id })
  },

  // 新增产品OEM
  create: (data: ProductOemVO) => {
    return request.post({ url: '/erp/product-oem/create', data })
  },

  // 修改产品OEM
  update: (data: ProductOemVO) => {
    return request.put({ url: '/erp/product-oem/update', data })
  },

  // 删除产品OEM
  delete: (id: number) => {
    return request.delete({ url: '/erp/product-oem/delete?id=' + id })
  },

  // 导出产品OEM Excel
  export: (params: any) => {
    return request.download({ url: '/erp/product-oem/export-excel', params })
  }
}

