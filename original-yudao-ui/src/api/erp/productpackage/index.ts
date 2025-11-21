import request from '@/config/axios'

export interface ProductPackageVO {
  id: number
  packageCode: string
  packageName: string
  description: string
  status: number
  quantityPerBox: number
  grossWeight: number
  netWeight: number
  innerBoxSize: string
  outerBoxSize: string
  boxVolume: number
  palletQuantity: number
  material: string
  packageType: string
  barcode: string
  imageUrl: string
  sort: number
  remark: string
  createTime: string
}

// ERP 产品包装 API
export const ProductPackageApi = {
  // 查询产品包装分页
  getPage: (params: any) => {
    return request.get({ url: '/erp/product-package/page', params })
  },

  // 查询产品包装精简列表
  getSimpleList: () => {
    return request.get({ url: '/erp/product-package/simple-list' })
  },

  // 查询产品包装详情
  get: (id: number) => {
    return request.get({ url: '/erp/product-package/get?id=' + id })
  },

  // 新增产品包装
  create: (data: ProductPackageVO) => {
    return request.post({ url: '/erp/product-package/create', data })
  },

  // 修改产品包装
  update: (data: ProductPackageVO) => {
    return request.put({ url: '/erp/product-package/update', data })
  },

  // 删除产品包装
  delete: (id: number) => {
    return request.delete({ url: '/erp/product-package/delete?id=' + id })
  },

  // 导出产品包装 Excel
  export: (params: any) => {
    return request.download({ url: '/erp/product-package/export-excel', params })
  }
}

