import request from '@/config/axios'

import { ProductSkuVO } from '../productsku'

// ERP 产品 VO
export interface ProductVO {
  id: number // 产品编号
  name: string // 产品名称
  barCode: string // 产品条码
  categoryId: number // 产品类型编号
  unitId: number // 单位编号
  unitName?: string // 单位名字
  status: number // 产品状态
  standard: string // 产品规格
  remark: string // 产品备注
  expiryDay: number // 保质期天数
  weight: number // 重量（kg）
  purchasePrice: number // 采购价格，单位：元
  salePrice: number // 销售价格，单位：元
  minPrice: number // 最低价格，单位：元
  skuList?: ProductSkuVO[] // SKU列表（前端使用）
  skuIds?: number[] // 关联的SKU ID列表（多对多关系，提交时使用）
  packageId?: number // 产品包装编号
  packageCode?: string // 产品包装编码
  oemId?: number // 产品OEM编号
  oemCode?: string // 产品OEM编码
}

// ERP 产品 API
export const ProductApi = {
  // 查询产品分页
  getProductPage: async (params: any) => {
    return await request.get({ url: `/erp/product/page`, params })
  },

  // 查询产品精简列表
  getProductSimpleList: async () => {
    return await request.get({ url: `/erp/product/simple-list` })
  },

  // 查询产品详情
  getProduct: async (id: number) => {
    return await request.get({ url: `/erp/product/get?id=` + id })
  },

  // 新增产品
  createProduct: async (data: ProductVO) => {
    return await request.post({ url: `/erp/product/create`, data })
  },

  // 修改产品
  updateProduct: async (data: ProductVO) => {
    return await request.put({ url: `/erp/product/update`, data })
  },

  // 删除产品
  deleteProduct: async (id: number) => {
    return await request.delete({ url: `/erp/product/delete?id=` + id })
  },

  // 导出产品 Excel
  exportProduct: async (params) => {
    return await request.download({ url: `/erp/product/export-excel`, params })
  },

  // 下载产品导入模板
  importProductTemplate: () => {
    return request.download({ url: '/erp/product/get-import-template' })
  },

  // 导入产品
  importProduct: async (formData) => {
    return await request.upload({ url: `/erp/product/import`, data: formData })
  }
}
