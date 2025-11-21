import request from '@/config/axios'

export interface ProductSkuVO {
  id?: number
  productId?: number
  skuCode: string
  skuName: string
  description?: string
  status: number
  categoryId?: number
  barCode?: string
  standard?: string
  unitId?: number
  weight?: number
  volume?: number
  costPrice?: number
  purchasePrice?: number
  salePrice?: number
  minPrice?: number
  color?: string
  size?: string
  material?: string
  imageUrl?: string
  sort?: number
  remark?: string
}

// ERP 产品SKU API
export const ProductSkuApi = {
  // 查询产品SKU分页
  getPage: async (params: any) => {
    return await request.get({ url: `/erp/product-sku/page`, params })
  },

  // 查询产品SKU详情
  get: async (id: number) => {
    return await request.get({ url: `/erp/product-sku/get?id=` + id })
  },

  // 新增产品SKU
  create: async (data: ProductSkuVO) => {
    return await request.post({ url: `/erp/product-sku/create`, data })
  },

  // 修改产品SKU
  update: async (data: ProductSkuVO) => {
    return await request.put({ url: `/erp/product-sku/update`, data })
  },

  // 删除产品SKU
  delete: async (id: number) => {
    return await request.delete({ url: `/erp/product-sku/delete?id=` + id })
  },

  // 导出产品SKU Excel
  export: async (params: any) => {
    return await request.download({ url: `/erp/product-sku/export-excel`, params })
  },

  // 获取产品SKU精简列表
  getSimpleList: async () => {
    return await request.get({ url: `/erp/product-sku/simple-list` })
  },

  // 根据产品ID获取产品SKU列表
  getListByProductId: async (productId: number) => {
    return await request.get({ url: `/erp/product-sku/list-by-product?productId=` + productId })
  }
}

