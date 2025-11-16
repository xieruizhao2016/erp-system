import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 工时统计信息 */
export interface WorkHours {
          id: number; // 编号
          workOrderId?: number; // 工单ID
          processId?: number; // 工序ID
          operatorId: number; // 操作员ID
          workDate?: string | Dayjs; // 工作日期
          shiftId: number; // 班次ID
          startTime: localtime; // 开始时间
          endTime: localtime; // 结束时间
          duration?: number; // 工作时长（分钟）
          standardDuration: number; // 标准工时（分钟）
          overtimeDuration: number; // 加班时长（分钟）
          machineHours: number; // 机时
          operatorRate: number; // 操作员工时费率
          machineRate: number; // 设备时费率
          laborCost: number; // 人工成本
          machineCost: number; // 设备成本
          status: number; // 状态：1-有效，2-无效
          remark: string; // 备注
  }

// ERP 工时统计 API
export const WorkHoursApi = {
  // 查询ERP 工时统计分页
  getWorkHoursPage: async (params: any) => {
    return await request.get({ url: `/erp/work-hours/page`, params })
  },

  // 查询ERP 工时统计详情
  getWorkHours: async (id: number) => {
    return await request.get({ url: `/erp/work-hours/get?id=` + id })
  },

  // 新增ERP 工时统计
  createWorkHours: async (data: WorkHours) => {
    return await request.post({ url: `/erp/work-hours/create`, data })
  },

  // 修改ERP 工时统计
  updateWorkHours: async (data: WorkHours) => {
    return await request.put({ url: `/erp/work-hours/update`, data })
  },

  // 删除ERP 工时统计
  deleteWorkHours: async (id: number) => {
    return await request.delete({ url: `/erp/work-hours/delete?id=` + id })
  },

  /** 批量删除ERP 工时统计 */
  deleteWorkHoursList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/work-hours/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工时统计 Excel
  exportWorkHours: async (params) => {
    return await request.download({ url: `/erp/work-hours/export-excel`, params })
  }
}