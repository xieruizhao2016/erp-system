import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 工艺路线主信息 */
export interface ProcessRoute {
          id: number; // 编号
          routeNo?: string; // 工艺路线编号
          routeName?: string; // 工艺路线名称
          productId?: number; // 产品ID
          version: string; // 版本号
          standardCycleTime: number; // 标准周期时间（分钟）
          standardLaborCost: number; // 标准人工成本
          standardOverheadCost: number; // 标准制造费用
          status: number; // 状态：1-草稿，2-生效，3-失效
          items?: Item[]; // 工艺路线明细列表
  }

/** ERP 工艺路线明细信息 */
export interface Item {
          id?: number; // 明细编号
          processId?: number; // 工序ID
          sequence?: number; // 序号
          operationName?: string; // 工序名称
          standardTime?: number; // 标准工时（分钟）
          setupTime?: number; // 准备时间（分钟）
          workerCount?: number; // 人员数量
          equipmentId?: number; // 设备ID
          workCenterId?: number; // 工作中心ID
          laborRate?: number; // 人工费率
          overheadRate?: number; // 制造费率
          isBottleneck?: boolean; // 是否瓶颈工序
          qualityCheckRequired?: boolean; // 是否需要质检
          remark?: string; // 备注
  }

// ERP 工艺路线主 API
export const ProcessRouteApi = {
  // 查询ERP 工艺路线主分页
  getProcessRoutePage: async (params: any) => {
    return await request.get({ url: `/erp/process-route/page`, params })
  },

  // 查询ERP 工艺路线主详情
  getProcessRoute: async (id: number) => {
    return await request.get({ url: `/erp/process-route/get?id=` + id })
  },

  // 新增ERP 工艺路线主
  createProcessRoute: async (data: ProcessRoute) => {
    return await request.post({ url: `/erp/process-route/create`, data })
  },

  // 修改ERP 工艺路线主
  updateProcessRoute: async (data: ProcessRoute) => {
    return await request.put({ url: `/erp/process-route/update`, data })
  },

  // 删除ERP 工艺路线主
  deleteProcessRoute: async (id: number) => {
    return await request.delete({ url: `/erp/process-route/delete?id=` + id })
  },

  /** 批量删除ERP 工艺路线主 */
  deleteProcessRouteList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/process-route/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工艺路线主 Excel
  exportProcessRoute: async (params) => {
    return await request.download({ url: `/erp/process-route/export-excel`, params })
  },

  // 生成默认工艺路线编号
  generateRouteNo: async () => {
    return await request.get({ url: `/erp/process-route/generate-route-no` })
  }
}