import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP BOM明细信息 */
export interface ProductBomItem {
          id?: number; // 编号
          bomId?: number; // BOM ID
          parentProductId?: number; // 父产品ID
          childProductId?: number; // 子产品ID
          childProductName?: string; // 子产品名称
          childProductSpec?: string; // 子产品规格
          unitId?: number; // 单位ID
          quantity?: number; // 用量
          lossRate?: number; // 损耗率
          effectiveQuantity?: number; // 有效用量
          isKeyMaterial?: boolean; // 是否关键物料
          isAlternative?: boolean; // 是否替代料
          alternativeGroup?: string; // 替代料组
          position?: number; // 位号
          processId?: number; // 工序ID
          remark?: string; // 备注
  }

// ERP BOM明细 API
export const ProductBomItemApi = {
  // 查询ERP BOM明细分页
  getProductBomItemPage: async (params: any) => {
    return await request.get({ url: `/erp/product-bom-item/page`, params })
  },

  // 查询ERP BOM明细详情
  getProductBomItem: async (id: number) => {
    return await request.get({ url: `/erp/product-bom-item/get?id=` + id })
  },

  // 新增ERP BOM明细
  createProductBomItem: async (data: ProductBomItem) => {
    return await request.post({ url: `/erp/product-bom-item/create`, data })
  },

  // 修改ERP BOM明细
  updateProductBomItem: async (data: ProductBomItem) => {
    return await request.put({ url: `/erp/product-bom-item/update`, data })
  },

  // 删除ERP BOM明细
  deleteProductBomItem: async (id: number) => {
    return await request.delete({ url: `/erp/product-bom-item/delete?id=` + id })
  },

  /** 批量删除ERP BOM明细 */
  deleteProductBomItemList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/product-bom-item/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP BOM明细 Excel
  exportProductBomItem: async (params) => {
    return await request.download({ url: `/erp/product-bom-item/export-excel`, params })
  }
}