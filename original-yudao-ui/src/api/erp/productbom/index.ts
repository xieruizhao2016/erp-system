import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP BOM主信息 */
export interface ProductBom {
          id: number; // 编号
          productId?: number; // 产品ID
          bomNo?: string; // BOM编号
          bomName?: string; // BOM名称
          version: string; // 版本号
          effectiveDate: string | Dayjs; // 生效日期
          expireDate: string | Dayjs; // 失效日期
          bomType: number; // BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM
          standardCost: number; // 标准成本
          totalMaterialCost: number; // 总材料成本
          status: number; // 状态：1-草稿，2-生效，3-失效
  }

// ERP BOM主 API
export const ProductBomApi = {
  // 查询ERP BOM主分页
  getProductBomPage: async (params: any) => {
    return await request.get({ url: `/erp/product-bom/page`, params })
  },

  // 查询ERP BOM主详情
  getProductBom: async (id: number) => {
    return await request.get({ url: `/erp/product-bom/get?id=` + id })
  },

  // 新增ERP BOM主
  createProductBom: async (data: ProductBom) => {
    return await request.post({ url: `/erp/product-bom/create`, data })
  },

  // 修改ERP BOM主
  updateProductBom: async (data: ProductBom) => {
    return await request.put({ url: `/erp/product-bom/update`, data })
  },

  // 删除ERP BOM主
  deleteProductBom: async (id: number) => {
    return await request.delete({ url: `/erp/product-bom/delete?id=` + id })
  },

  /** 批量删除ERP BOM主 */
  deleteProductBomList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/product-bom/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP BOM主 Excel
  exportProductBom: async (params) => {
    return await request.download({ url: `/erp/product-bom/export-excel`, params })
  }
}