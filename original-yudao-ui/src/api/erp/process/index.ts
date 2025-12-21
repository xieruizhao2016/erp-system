import request from '@/config/axios'

/** ERP 工序信息 */
export interface Process {
  id: number // 编号
  processNo?: string // 工序编号
  processName?: string // 工序名称
  processType?: number // 工序类型：1-加工，2-装配，3-检验，4-包装，5-其他
  category?: string // 工序分类
  standardTime?: number // 标准工时（分钟）
  setupTime?: number // 准备时间（分钟）
  workerCount?: number // 标准人员数量
  equipmentType?: string // 设备类型
  equipmentId?: number // 设备ID
  workCenterId?: number // 工作中心ID
  laborRate?: number // 人工费率（元/小时）
  overheadRate?: number // 制造费率（元/小时）
  qualityCheckRequired?: boolean // 是否需要质检
  isBottleneck?: boolean // 是否瓶颈工序
  description?: string // 工序描述
  remark?: string // 备注
  status?: number // 状态：1-启用，2-停用
}

// ERP 工序 API
export const ProcessApi = {
  // 查询ERP 工序分页
  getProcessPage: async (params: any) => {
    return await request.get({ url: `/erp/process/page`, params })
  },

  // 查询ERP 工序详情
  getProcess: async (id: number) => {
    return await request.get({ url: `/erp/process/get?id=` + id })
  },

  // 查询ERP 工序列表（简单列表）
  getProcessList: async () => {
    return await request.get({ url: `/erp/process/list` })
  },

  // 生成默认工序编号
  generateProcessNo: async () => {
    return await request.get({ url: `/erp/process/generate-no` })
  },

  // 新增ERP 工序
  createProcess: async (data: Process) => {
    return await request.post({ url: `/erp/process/create`, data })
  },

  // 修改ERP 工序
  updateProcess: async (data: Process) => {
    return await request.put({ url: `/erp/process/update`, data })
  },

  // 删除ERP 工序
  deleteProcess: async (id: number) => {
    return await request.delete({ url: `/erp/process/delete?id=` + id })
  },

  /** 批量删除ERP 工序 */
  deleteProcessList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/process/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 工序 Excel
  exportProcess: async (params) => {
    return await request.download({ url: `/erp/process/export-excel`, params })
  }
}

