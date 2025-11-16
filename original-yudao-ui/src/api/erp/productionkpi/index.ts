import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 生产KPI信息 */
export interface ProductionKpi {
          id: number; // 编号
          kpiNo?: string; // KPI编号
          kpiName?: string; // KPI名称
          kpiType?: number; // KPI类型：1-OEE，2-合格率，3-达成率，4-交期率
          category: number; // 分类：1-效率，2-质量，3-交付，4-成本
          workCenterId: number; // 工作中心ID
          productId: number; // 产品ID
          measurementPeriod?: number; // 统计周期：1-小时，2-天，3-周，4-月
          targetValue: number; // 目标值
          actualValue?: number; // 实际值
          variance: number; // 差异值
          varianceRate: number; // 差异率
          calculationDate?: string | Dayjs; // 计算日期
          periodStartTime?: string | Dayjs; // 周期开始时间
          periodEndTime?: string | Dayjs; // 周期结束时间
          dataSource: string; // 数据来源
          remarks: string; // 备注
  }

// ERP 生产KPI API
export const ProductionKpiApi = {
  // 查询ERP 生产KPI分页
  getProductionKpiPage: async (params: any) => {
    return await request.get({ url: `/erp/production-kpi/page`, params })
  },

  // 查询ERP 生产KPI详情
  getProductionKpi: async (id: number) => {
    return await request.get({ url: `/erp/production-kpi/get?id=` + id })
  },

  // 新增ERP 生产KPI
  createProductionKpi: async (data: ProductionKpi) => {
    return await request.post({ url: `/erp/production-kpi/create`, data })
  },

  // 修改ERP 生产KPI
  updateProductionKpi: async (data: ProductionKpi) => {
    return await request.put({ url: `/erp/production-kpi/update`, data })
  },

  // 删除ERP 生产KPI
  deleteProductionKpi: async (id: number) => {
    return await request.delete({ url: `/erp/production-kpi/delete?id=` + id })
  },

  /** 批量删除ERP 生产KPI */
  deleteProductionKpiList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-kpi/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 生产KPI Excel
  exportProductionKpi: async (params) => {
    return await request.download({ url: `/erp/production-kpi/export-excel`, params })
  }
}