import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 生产排程主信息 */
export interface ProductionSchedule {
          id: number; // 编号
          scheduleNo?: string; // 排程单号
          scheduleName?: string; // 排程名称
          scheduleType: number; // 排程类型：1-主排程，2-详细排程
          planningHorizonDays: number; // 计划天数
          startDate?: string | Dayjs; // 计划开始日期
          endDate?: string | Dayjs; // 计划结束日期
          status: number; // 状态：1-草稿，2-已发布，3-执行中，4-已完成
          totalOrders: number; // 总订单数
          totalQuantity: number; // 总数量
          totalWorkHours: number; // 总工时
          createdBy: string; // 创建人
          updatedBy: string; // 更新人
  }

// ERP 生产排程主 API
export const ProductionScheduleApi = {
  // 查询ERP 生产排程主分页
  getProductionSchedulePage: async (params: any) => {
    return await request.get({ url: `/erp/production-schedule/page`, params })
  },

  // 查询ERP 生产排程主详情
  getProductionSchedule: async (id: number) => {
    return await request.get({ url: `/erp/production-schedule/get?id=` + id })
  },

  // 新增ERP 生产排程主
  createProductionSchedule: async (data: ProductionSchedule) => {
    return await request.post({ url: `/erp/production-schedule/create`, data })
  },

  // 修改ERP 生产排程主
  updateProductionSchedule: async (data: ProductionSchedule) => {
    return await request.put({ url: `/erp/production-schedule/update`, data })
  },

  // 删除ERP 生产排程主
  deleteProductionSchedule: async (id: number) => {
    return await request.delete({ url: `/erp/production-schedule/delete?id=` + id })
  },

  /** 批量删除ERP 生产排程主 */
  deleteProductionScheduleList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-schedule/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 生产排程主 Excel
  exportProductionSchedule: async (params) => {
    return await request.download({ url: `/erp/production-schedule/export-excel`, params })
  }
}