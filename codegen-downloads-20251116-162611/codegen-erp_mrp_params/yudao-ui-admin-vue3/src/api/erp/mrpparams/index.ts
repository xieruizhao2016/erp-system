import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP MRP参数信息 */
export interface MrpParams {
          id: number; // 编号
          paramName?: string; // 参数名称
          paramCode?: string; // 参数编码
          paramValue?: string; // 参数值
          paramType: number; // 参数类型：1-字符串，2-数字，3-日期，4-布尔
          description: string; // 参数描述
          isSystem: boolean; // 是否系统参数
  }

// ERP MRP参数 API
export const MrpParamsApi = {
  // 查询ERP MRP参数分页
  getMrpParamsPage: async (params: any) => {
    return await request.get({ url: `/erp/mrp-params/page`, params })
  },

  // 查询ERP MRP参数详情
  getMrpParams: async (id: number) => {
    return await request.get({ url: `/erp/mrp-params/get?id=` + id })
  },

  // 新增ERP MRP参数
  createMrpParams: async (data: MrpParams) => {
    return await request.post({ url: `/erp/mrp-params/create`, data })
  },

  // 修改ERP MRP参数
  updateMrpParams: async (data: MrpParams) => {
    return await request.put({ url: `/erp/mrp-params/update`, data })
  },

  // 删除ERP MRP参数
  deleteMrpParams: async (id: number) => {
    return await request.delete({ url: `/erp/mrp-params/delete?id=` + id })
  },

  /** 批量删除ERP MRP参数 */
  deleteMrpParamsList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/mrp-params/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP MRP参数 Excel
  exportMrpParams: async (params) => {
    return await request.download({ url: `/erp/mrp-params/export-excel`, params })
  }
}