import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP MRP运算结果信息 */
export interface MrpResult {
          id: number; // 编号
          runNo?: string; // 运算批次号
          productId?: number; // 产品ID
          warehouseId: number; // 仓库ID
          periodStartDate?: string | Dayjs; // 周期开始日期
          periodEndDate?: string | Dayjs; // 周期结束日期
          grossRequirement: number; // 毛需求
          scheduledReceipts: number; // 计划接收量
          onHandInventory: number; // 现有库存
          netRequirement: number; // 净需求
          plannedOrderReceipts: number; // 计划订单接收量
          plannedOrderReleases: number; // 计划订单发放量
          orderType: number; // 订单类型：1-生产订单，2-采购订单
          lotSizingRule: number; // 批量规则：1-固定批量，2-按需，3-最小-最大
          leadTime: number; // 提前期（天）
          safetyStock: number; // 安全库存
          orderStatus: number; // 订单状态：1-建议，2-确认，3-下达
          dueDate: string | Dayjs; // 需求日期
  }

// ERP MRP运算结果 API
export const MrpResultApi = {
  // 查询ERP MRP运算结果分页
  getMrpResultPage: async (params: any) => {
    return await request.get({ url: `/erp/mrp-result/page`, params })
  },

  // 查询ERP MRP运算结果详情
  getMrpResult: async (id: number) => {
    return await request.get({ url: `/erp/mrp-result/get?id=` + id })
  },

  // 新增ERP MRP运算结果
  createMrpResult: async (data: MrpResult) => {
    return await request.post({ url: `/erp/mrp-result/create`, data })
  },

  // 修改ERP MRP运算结果
  updateMrpResult: async (data: MrpResult) => {
    return await request.put({ url: `/erp/mrp-result/update`, data })
  },

  // 删除ERP MRP运算结果
  deleteMrpResult: async (id: number) => {
    return await request.delete({ url: `/erp/mrp-result/delete?id=` + id })
  },

  /** 批量删除ERP MRP运算结果 */
  deleteMrpResultList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/mrp-result/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP MRP运算结果 Excel
  exportMrpResult: async (params) => {
    return await request.download({ url: `/erp/mrp-result/export-excel`, params })
  }
}