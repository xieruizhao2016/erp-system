import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 质检明细信息 */
export interface QualityInspectionItem {
          id: number; // 编号
          inspectionId?: number; // 检验ID
          itemId?: number; // 检验项目ID
          sampleNo: string; // 样本编号
          measuredValue: string; // 测量值
          actualValue: number; // 实际数值
          testResult: number; // 检验结果：1-合格，2-不合格，3-超差
          defectType: string; // 缺陷类型
          defectDescription: string; // 缺陷描述
          imageUrls: string; // 缺陷图片URLs
          inspectorId: number; // 检验员ID
          inspectionTime?: string | Dayjs; // 检验时间
          remark: string; // 备注
  }

// ERP 质检明细 API
export const QualityInspectionItemApi = {
  // 查询ERP 质检明细分页
  getQualityInspectionItemPage: async (params: any) => {
    return await request.get({ url: `/erp/quality-inspection-item/page`, params })
  },

  // 查询ERP 质检明细详情
  getQualityInspectionItem: async (id: number) => {
    return await request.get({ url: `/erp/quality-inspection-item/get?id=` + id })
  },

  // 新增ERP 质检明细
  createQualityInspectionItem: async (data: QualityInspectionItem) => {
    return await request.post({ url: `/erp/quality-inspection-item/create`, data })
  },

  // 修改ERP 质检明细
  updateQualityInspectionItem: async (data: QualityInspectionItem) => {
    return await request.put({ url: `/erp/quality-inspection-item/update`, data })
  },

  // 删除ERP 质检明细
  deleteQualityInspectionItem: async (id: number) => {
    return await request.delete({ url: `/erp/quality-inspection-item/delete?id=` + id })
  },

  /** 批量删除ERP 质检明细 */
  deleteQualityInspectionItemList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/quality-inspection-item/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 质检明细 Excel
  exportQualityInspectionItem: async (params) => {
    return await request.download({ url: `/erp/quality-inspection-item/export-excel`, params })
  }
}