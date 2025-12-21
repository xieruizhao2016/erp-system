import request from '@/config/axios'

/** ERP 工作中心信息 */
export interface WorkCenter {
  id?: number // 编号
  workCenterNo?: string // 工作中心编号
  workCenterName?: string // 工作中心名称
  description?: string // 描述
  location?: string // 位置
  responsiblePerson?: string // 负责人
  status?: number // 状态：1-启用，2-停用
  remark?: string // 备注
}

// ERP 工作中心 API
export const WorkCenterApi = {
  // 查询ERP 工作中心分页
  getWorkCenterPage: async (params: any) => {
    return await request.get({ url: `/erp/work-center/page`, params })
  },

  // 查询ERP 工作中心详情
  getWorkCenter: async (id: number) => {
    return await request.get({ url: `/erp/work-center/get?id=` + id })
  },

  // 查询ERP 工作中心列表（简单列表）
  getWorkCenterList: async () => {
    return await request.get({ url: `/erp/work-center/list` })
  },

  // 新增ERP 工作中心
  createWorkCenter: async (data: WorkCenter) => {
    return await request.post({ url: `/erp/work-center/create`, data })
  },

  // 修改ERP 工作中心
  updateWorkCenter: async (data: WorkCenter) => {
    return await request.put({ url: `/erp/work-center/update`, data })
  },

  // 删除ERP 工作中心
  deleteWorkCenter: async (id: number) => {
    return await request.delete({ url: `/erp/work-center/delete?id=` + id })
  },

  /** 批量删除ERP 工作中心 */
  deleteWorkCenterList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/work-center/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工作中心 Excel
  exportWorkCenter: async (params) => {
    return await request.download({ url: `/erp/work-center/export-excel`, params })
  }
}

