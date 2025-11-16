import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 工单进度信息 */
export interface WorkOrderProgress {
          id: number; // 编号
          workOrderId?: number; // 工单ID
          processId?: number; // 工序ID
          processName?: string; // 工序名称
          sequence?: number; // 工序序号
          plannedStartTime: string | Dayjs; // 计划开始时间
          plannedEndTime: string | Dayjs; // 计划结束时间
          actualStartTime: string | Dayjs; // 实际开始时间
          actualEndTime: string | Dayjs; // 实际结束时间
          plannedQuantity: number; // 计划数量
          completedQuantity: number; // 完成数量
          qualifiedQuantity: number; // 合格数量
          rejectedQuantity: number; // 不合格数量
          scrapQuantity: number; // 报废数量
          status: number; // 状态：1-待开始，2-进行中，3-已完成，4-异常
          operatorId: number; // 操作员ID
          equipmentId: number; // 设备ID
          workTime: number; // 实际工时（分钟）
          downtime: number; // 停机时间（分钟）
          qualityStatus: number; // 质检状态：1-待检，2-合格，3-不合格
          remark: string; // 备注
  }

// ERP 工单进度 API
export const WorkOrderProgressApi = {
  // 查询ERP 工单进度分页
  getWorkOrderProgressPage: async (params: any) => {
    return await request.get({ url: `/erp/work-order-progress/page`, params })
  },

  // 查询ERP 工单进度详情
  getWorkOrderProgress: async (id: number) => {
    return await request.get({ url: `/erp/work-order-progress/get?id=` + id })
  },

  // 新增ERP 工单进度
  createWorkOrderProgress: async (data: WorkOrderProgress) => {
    return await request.post({ url: `/erp/work-order-progress/create`, data })
  },

  // 修改ERP 工单进度
  updateWorkOrderProgress: async (data: WorkOrderProgress) => {
    return await request.put({ url: `/erp/work-order-progress/update`, data })
  },

  // 删除ERP 工单进度
  deleteWorkOrderProgress: async (id: number) => {
    return await request.delete({ url: `/erp/work-order-progress/delete?id=` + id })
  },

  /** 批量删除ERP 工单进度 */
  deleteWorkOrderProgressList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/work-order-progress/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工单进度 Excel
  exportWorkOrderProgress: async (params) => {
    return await request.download({ url: `/erp/work-order-progress/export-excel`, params })
  }
}