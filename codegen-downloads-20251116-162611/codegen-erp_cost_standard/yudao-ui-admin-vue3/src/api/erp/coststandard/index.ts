import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 标准成本信息 */
export interface CostStandard {
          id: number; // 编号
          productId?: number; // 产品ID
          version: string; // 版本号
          effectiveDate?: string | Dayjs; // 生效日期
          expireDate: string | Dayjs; // 失效日期
          materialCost?: number; // 材料成本
          laborCost?: number; // 人工成本
          overheadCost?: number; // 制造费用
          totalCost?: number; // 总成本
          costCurrency: string; // 成本币种
          calculationDate?: string | Dayjs; // 计算日期
          bomVersion: string; // 关联BOM版本
          routeVersion: string; // 关联工艺版本
          status: number; // 状态：1-草稿，2-生效，3-失效
          remark: string; // 备注
  }

// ERP 标准成本 API
export const CostStandardApi = {
  // 查询ERP 标准成本分页
  getCostStandardPage: async (params: any) => {
    return await request.get({ url: `/erp/cost-standard/page`, params })
  },

  // 查询ERP 标准成本详情
  getCostStandard: async (id: number) => {
    return await request.get({ url: `/erp/cost-standard/get?id=` + id })
  },

  // 新增ERP 标准成本
  createCostStandard: async (data: CostStandard) => {
    return await request.post({ url: `/erp/cost-standard/create`, data })
  },

  // 修改ERP 标准成本
  updateCostStandard: async (data: CostStandard) => {
    return await request.put({ url: `/erp/cost-standard/update`, data })
  },

  // 删除ERP 标准成本
  deleteCostStandard: async (id: number) => {
    return await request.delete({ url: `/erp/cost-standard/delete?id=` + id })
  },

  /** 批量删除ERP 标准成本 */
  deleteCostStandardList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/cost-standard/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 标准成本 Excel
  exportCostStandard: async (params) => {
    return await request.download({ url: `/erp/cost-standard/export-excel`, params })
  }
}