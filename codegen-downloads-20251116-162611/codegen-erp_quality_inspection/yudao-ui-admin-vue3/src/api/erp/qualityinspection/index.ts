import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 质检记录信息 */
export interface QualityInspection {
          id: number; // 编号
          inspectionNo?: string; // 检验单号
          batchNo: string; // 批次号
          productId?: number; // 产品ID
          processId: number; // 工序ID
          workOrderId: number; // 工单ID
          inspectionType: number; // 检验类型：1-进料检验，2-过程检验，3-成品检验
          inspectionLevel: number; // 检验级别：1-全检，2-抽检
          lotSize: number; // 批量大小
          sampleSize: number; // 样本数量
          qualifiedQuantity: number; // 合格数量
          rejectedQuantity: number; // 不合格数量
          scrapQuantity: number; // 报废数量
          inspectionResult: number; // 检验结果：1-合格，2-不合格，3-待复检
          inspectorId: number; // 检验员ID
          inspectionTime?: string | Dayjs; // 检验时间
          environment: string; // 检验环境
          equipment: string; // 检验设备
          remark: string; // 检验备注
  }

// ERP 质检记录 API
export const QualityInspectionApi = {
  // 查询ERP 质检记录分页
  getQualityInspectionPage: async (params: any) => {
    return await request.get({ url: `/erp/quality-inspection/page`, params })
  },

  // 查询ERP 质检记录详情
  getQualityInspection: async (id: number) => {
    return await request.get({ url: `/erp/quality-inspection/get?id=` + id })
  },

  // 新增ERP 质检记录
  createQualityInspection: async (data: QualityInspection) => {
    return await request.post({ url: `/erp/quality-inspection/create`, data })
  },

  // 修改ERP 质检记录
  updateQualityInspection: async (data: QualityInspection) => {
    return await request.put({ url: `/erp/quality-inspection/update`, data })
  },

  // 删除ERP 质检记录
  deleteQualityInspection: async (id: number) => {
    return await request.delete({ url: `/erp/quality-inspection/delete?id=` + id })
  },

  /** 批量删除ERP 质检记录 */
  deleteQualityInspectionList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/quality-inspection/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 质检记录 Excel
  exportQualityInspection: async (params) => {
    return await request.download({ url: `/erp/quality-inspection/export-excel`, params })
  }
}