import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 生产报表信息 */
export interface ProductionReport {
          id: number; // 编号
          reportNo?: string; // 报表编号
          reportName?: string; // 报表名称
          reportType?: number; // 报表类型：1-日报，2-周报，3-月报，4-年报
          reportPeriod?: string; // 报表期间
          workCenterId: number; // 工作中心ID
          productionOrders: number; // 生产订单数
          totalPlanQuantity: number; // 总计划数量
          totalCompletedQuantity: number; // 总完成数量
          totalQualifiedQuantity: number; // 总合格数量
          completionRate: number; // 完成率
          qualityRate: number; // 合格率
          totalWorkHours: number; // 总工时
          totalEquipmentHours: number; // 总机时
          oee: number; // OEE
          onTimeDeliveryRate: number; // 准时交付率
          productionValue: number; // 产值
          averageCost: number; // 平均成本
          reportDate?: string | Dayjs; // 报表日期
          generateTime?: string | Dayjs; // 生成时间
          status: number; // 状态：1-草稿，2-已发布
          reportData: string; // 详细数据（JSON）
  }

// ERP 生产报表 API
export const ProductionReportApi = {
  // 查询ERP 生产报表分页
  getProductionReportPage: async (params: any) => {
    return await request.get({ url: `/erp/production-report/page`, params })
  },

  // 查询ERP 生产报表详情
  getProductionReport: async (id: number) => {
    return await request.get({ url: `/erp/production-report/get?id=` + id })
  },

  // 新增ERP 生产报表
  createProductionReport: async (data: ProductionReport) => {
    return await request.post({ url: `/erp/production-report/create`, data })
  },

  // 修改ERP 生产报表
  updateProductionReport: async (data: ProductionReport) => {
    return await request.put({ url: `/erp/production-report/update`, data })
  },

  // 删除ERP 生产报表
  deleteProductionReport: async (id: number) => {
    return await request.delete({ url: `/erp/production-report/delete?id=` + id })
  },

  /** 批量删除ERP 生产报表 */
  deleteProductionReportList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-report/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 生产报表 Excel
  exportProductionReport: async (params) => {
    return await request.download({ url: `/erp/production-report/export-excel`, params })
  }
}