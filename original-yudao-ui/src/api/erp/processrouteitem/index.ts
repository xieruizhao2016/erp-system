import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 工艺路线明细信息 */
export interface ProcessRouteItem {
          id: number; // 编号
          routeId?: number; // 工艺路线ID
          processId?: number; // 工序ID
          sequence?: number; // 序号
          operationName?: string; // 工序名称
          standardTime?: number; // 标准工时（分钟）
          setupTime: number; // 准备时间（分钟）
          workerCount: number; // 人员数量
          equipmentId: number; // 设备ID
          workCenterId: number; // 工作中心ID
          laborRate: number; // 人工费率
          overheadRate: number; // 制造费率
          isBottleneck: boolean; // 是否瓶颈工序
          qualityCheckRequired: boolean; // 是否需要质检
          remark: string; // 备注
  }

// ERP 工艺路线明细 API
export const ProcessRouteItemApi = {
  // 查询ERP 工艺路线明细分页
  getProcessRouteItemPage: async (params: any) => {
    return await request.get({ url: `/erp/process-route-item/page`, params })
  },

  // 查询ERP 工艺路线明细详情
  getProcessRouteItem: async (id: number) => {
    return await request.get({ url: `/erp/process-route-item/get?id=` + id })
  },

  // 新增ERP 工艺路线明细
  createProcessRouteItem: async (data: ProcessRouteItem) => {
    return await request.post({ url: `/erp/process-route-item/create`, data })
  },

  // 修改ERP 工艺路线明细
  updateProcessRouteItem: async (data: ProcessRouteItem) => {
    return await request.put({ url: `/erp/process-route-item/update`, data })
  },

  // 删除ERP 工艺路线明细
  deleteProcessRouteItem: async (id: number) => {
    return await request.delete({ url: `/erp/process-route-item/delete?id=` + id })
  },

  /** 批量删除ERP 工艺路线明细 */
  deleteProcessRouteItemList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/process-route-item/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工艺路线明细 Excel
  exportProcessRouteItem: async (params) => {
    return await request.download({ url: `/erp/process-route-item/export-excel`, params })
  }
}