import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 工单主信息 */
export interface WorkOrder {
          id: number; // 编号
          workOrderNo?: string; // 工单号
          productionOrderId?: number; // 生产订单ID
          productId?: number; // 产品ID
          productName?: string; // 产品名称
          quantity?: number; // 工单数量
          completedQuantity: number; // 完成数量
          qualifiedQuantity: number; // 合格数量
          workCenterId?: number; // 工作中心ID
          supervisorId: number; // 主管ID
          plannedStartTime?: string | Dayjs; // 计划开始时间
          plannedEndTime?: string | Dayjs; // 计划结束时间
          actualStartTime: string | Dayjs; // 实际开始时间
          actualEndTime: string | Dayjs; // 实际结束时间
          status: number; // 状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消
          priority: number; // 优先级
          instruction: string; // 作业指导书
          remark: string; // 备注
          totalWorkTime?: number; // 总工时（分钟）- 计算字段，所有工序的工时之和
  }

// ERP 工单主 API
export const WorkOrderApi = {
  // 查询ERP 工单主分页
  getWorkOrderPage: async (params: any) => {
    return await request.get({ url: `/erp/work-order/page`, params })
  },

  // 查询ERP 工单主详情
  getWorkOrder: async (id: number) => {
    return await request.get({ url: `/erp/work-order/get?id=` + id })
  },

  // 新增ERP 工单主
  createWorkOrder: async (data: WorkOrder) => {
    return await request.post({ url: `/erp/work-order/create`, data })
  },

  // 修改ERP 工单主
  updateWorkOrder: async (data: WorkOrder) => {
    return await request.put({ url: `/erp/work-order/update`, data })
  },

  // 删除ERP 工单主
  deleteWorkOrder: async (id: number) => {
    return await request.delete({ url: `/erp/work-order/delete?id=` + id })
  },

  /** 批量删除ERP 工单主 */
  deleteWorkOrderList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/work-order/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工单主 Excel
  exportWorkOrder: async (params) => {
    return await request.download({ url: `/erp/work-order/export-excel`, params })
  }
}