import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 排程明细信息 */
export interface ProductionScheduleItem {
          id: number; // 编号
          scheduleId?: number; // 排程ID
          productionOrderId?: number; // 生产订单ID
          productId?: number; // 产品ID
          quantity?: number; // 排程数量
          plannedStartTime?: string | Dayjs; // 计划开始时间
          plannedEndTime?: string | Dayjs; // 计划结束时间
          workCenterId: number; // 工作中心ID
          equipmentId: number; // 设备ID
          processSequence: number; // 工序序号
          priority: number; // 优先级
          setupTime: number; // 准备时间
          runTime?: number; // 运行时间
          waitTime: number; // 等待时间
          status: number; // 状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟
          actualStartTime: string | Dayjs; // 实际开始时间
          actualEndTime: string | Dayjs; // 实际结束时间
          completionRate: number; // 完成率
          delayReason: string; // 延迟原因
  }

// ERP 排程明细 API
export const ProductionScheduleItemApi = {
  // 查询ERP 排程明细分页
  getProductionScheduleItemPage: async (params: any) => {
    return await request.get({ url: `/erp/production-schedule-item/page`, params })
  },

  // 查询ERP 排程明细详情
  getProductionScheduleItem: async (id: number) => {
    return await request.get({ url: `/erp/production-schedule-item/get?id=` + id })
  },

  // 新增ERP 排程明细
  createProductionScheduleItem: async (data: ProductionScheduleItem) => {
    return await request.post({ url: `/erp/production-schedule-item/create`, data })
  },

  // 修改ERP 排程明细
  updateProductionScheduleItem: async (data: ProductionScheduleItem) => {
    return await request.put({ url: `/erp/production-schedule-item/update`, data })
  },

  // 删除ERP 排程明细
  deleteProductionScheduleItem: async (id: number) => {
    return await request.delete({ url: `/erp/production-schedule-item/delete?id=` + id })
  },

  /** 批量删除ERP 排程明细 */
  deleteProductionScheduleItemList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-schedule-item/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 排程明细 Excel
  exportProductionScheduleItem: async (params) => {
    return await request.download({ url: `/erp/production-schedule-item/export-excel`, params })
  }
}