import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 实际成本信息 */
export interface CostActual {
          id: number; // 编号
          costNo?: string; // 成本单号
          workOrderId: number; // 工单ID
          productionOrderId: number; // 生产订单ID
          productId?: number; // 产品ID
          productionQuantity?: number; // 生产数量
          materialCost?: number; // 材料成本
          materialCostAdjust: number; // 材料成本调整
          laborCost?: number; // 人工成本
          laborCostAdjust: number; // 人工成本调整
          overheadCost?: number; // 制造费用
          overheadCostAdjust: number; // 制造费用调整
          totalCost?: number; // 总成本
          unitCost?: number; // 单位成本
          costCurrency: string; // 成本币种
          costPeriod?: string; // 成本期间（YYYY-MM）
          calculationDate?: string | Dayjs; // 计算日期
          lastAdjustDate: string | Dayjs; // 最后调整日期
          status: number; // 状态：1-草稿，2-已计算，3-已确认
          remark: string; // 备注
  }

// ERP 实际成本 API
export const CostActualApi = {
  // 查询ERP 实际成本分页
  getCostActualPage: async (params: any) => {
    return await request.get({ url: `/erp/cost-actual/page`, params })
  },

  // 查询ERP 实际成本详情
  getCostActual: async (id: number) => {
    return await request.get({ url: `/erp/cost-actual/get?id=` + id })
  },

  // 新增ERP 实际成本
  createCostActual: async (data: CostActual) => {
    return await request.post({ url: `/erp/cost-actual/create`, data })
  },

  // 修改ERP 实际成本
  updateCostActual: async (data: CostActual) => {
    return await request.put({ url: `/erp/cost-actual/update`, data })
  },

  // 删除ERP 实际成本
  deleteCostActual: async (id: number) => {
    return await request.delete({ url: `/erp/cost-actual/delete?id=` + id })
  },

  /** 批量删除ERP 实际成本 */
  deleteCostActualList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/cost-actual/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 实际成本 Excel
  exportCostActual: async (params) => {
    return await request.download({ url: `/erp/cost-actual/export-excel`, params })
  }
}