import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 质检项目信息 */
export interface QualityItem {
          id: number; // 编号
          standardId?: number; // 标准ID
          itemNo?: string; // 项目编号
          itemName?: string; // 项目名称
          itemType: number; // 项目类型：1-定性，2-定量
          testMethod: string; // 检验方法
          standardValue: string; // 标准值
          tolerance: string; // 公差范围
          unit: string; // 单位
          isKeyItem: boolean; // 是否关键项
          sequence: number; // 检验序号
          remark: string; // 备注
  }

// ERP 质检项目 API
export const QualityItemApi = {
  // 查询ERP 质检项目分页
  getQualityItemPage: async (params: any) => {
    return await request.get({ url: `/erp/quality-item/page`, params })
  },

  // 查询ERP 质检项目详情
  getQualityItem: async (id: number) => {
    return await request.get({ url: `/erp/quality-item/get?id=` + id })
  },

  // 新增ERP 质检项目
  createQualityItem: async (data: QualityItem) => {
    return await request.post({ url: `/erp/quality-item/create`, data })
  },

  // 修改ERP 质检项目
  updateQualityItem: async (data: QualityItem) => {
    return await request.put({ url: `/erp/quality-item/update`, data })
  },

  // 删除ERP 质检项目
  deleteQualityItem: async (id: number) => {
    return await request.delete({ url: `/erp/quality-item/delete?id=` + id })
  },

  /** 批量删除ERP 质检项目 */
  deleteQualityItemList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/quality-item/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 质检项目 Excel
  exportQualityItem: async (params) => {
    return await request.download({ url: `/erp/quality-item/export-excel`, params })
  }
}