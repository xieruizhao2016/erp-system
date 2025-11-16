import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 质检标准信息 */
export interface QualityStandard {
          id: number; // 编号
          standardNo?: string; // 标准编号
          standardName?: string; // 标准名称
          productId?: number; // 产品ID
          processId: number; // 工序ID
          inspectionType: number; // 检验类型：1-进料检验，2-过程检验，3-成品检验
          standardVersion: string; // 标准版本
          aqlLevel: string; // AQL水平
          samplingMethod: string; // 抽样方法
          status: number; // 状态：1-草稿，2-生效，3-失效
          description: string; // 标准描述
  }

// ERP 质检标准 API
export const QualityStandardApi = {
  // 查询ERP 质检标准分页
  getQualityStandardPage: async (params: any) => {
    return await request.get({ url: `/erp/quality-standard/page`, params })
  },

  // 查询ERP 质检标准详情
  getQualityStandard: async (id: number) => {
    return await request.get({ url: `/erp/quality-standard/get?id=` + id })
  },

  // 新增ERP 质检标准
  createQualityStandard: async (data: QualityStandard) => {
    return await request.post({ url: `/erp/quality-standard/create`, data })
  },

  // 修改ERP 质检标准
  updateQualityStandard: async (data: QualityStandard) => {
    return await request.put({ url: `/erp/quality-standard/update`, data })
  },

  // 删除ERP 质检标准
  deleteQualityStandard: async (id: number) => {
    return await request.delete({ url: `/erp/quality-standard/delete?id=` + id })
  },

  /** 批量删除ERP 质检标准 */
  deleteQualityStandardList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/quality-standard/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 质检标准 Excel
  exportQualityStandard: async (params) => {
    return await request.download({ url: `/erp/quality-standard/export-excel`, params })
  }
}