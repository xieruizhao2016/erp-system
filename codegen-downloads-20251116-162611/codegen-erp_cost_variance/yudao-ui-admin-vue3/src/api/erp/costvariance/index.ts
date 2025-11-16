import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 成本差异分析信息 */
export interface CostVariance {
          id: number; // 编号
          costActualId?: number; // 实际成本ID
          productId?: number; // 产品ID
          productionQuantity?: number; // 生产数量
          standardTotalCost?: number; // 标准总成本
          actualTotalCost?: number; // 实际总成本
          totalVariance?: number; // 总差异
          totalVarianceRate?: number; // 总差异率
          materialVariance: number; // 材料成本差异
          materialVarianceRate: number; // 材料差异率
          laborVariance: number; // 人工成本差异
          laborVarianceRate: number; // 人工差异率
          overheadVariance: number; // 制造费用差异
          overheadVarianceRate: number; // 制造费用差异率
          varianceType: number; // 差异类型：1-有利，2-不利
          analysisDate?: string | Dayjs; // 分析日期
          varianceReason: string; // 差异原因
          remark: string; // 备注
  }

// ERP 成本差异分析 API
export const CostVarianceApi = {
  // 查询ERP 成本差异分析分页
  getCostVariancePage: async (params: any) => {
    return await request.get({ url: `/erp/cost-variance/page`, params })
  },

  // 查询ERP 成本差异分析详情
  getCostVariance: async (id: number) => {
    return await request.get({ url: `/erp/cost-variance/get?id=` + id })
  },

  // 新增ERP 成本差异分析
  createCostVariance: async (data: CostVariance) => {
    return await request.post({ url: `/erp/cost-variance/create`, data })
  },

  // 修改ERP 成本差异分析
  updateCostVariance: async (data: CostVariance) => {
    return await request.put({ url: `/erp/cost-variance/update`, data })
  },

  // 删除ERP 成本差异分析
  deleteCostVariance: async (id: number) => {
    return await request.delete({ url: `/erp/cost-variance/delete?id=` + id })
  },

  /** 批量删除ERP 成本差异分析 */
  deleteCostVarianceList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/cost-variance/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 成本差异分析 Excel
  exportCostVariance: async (params) => {
    return await request.download({ url: `/erp/cost-variance/export-excel`, params })
  }
}