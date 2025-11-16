import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 生产订单 DO
 *信息 */
export interface ProductionOrder {
          id: number; // 编号
          no?: string; // 生产订单号
          customerId: number; // 客户ID（关联销售订单）
          productId?: number; // 产品ID
          productName?: string; // 产品名称
          productSpec: string; // 产品规格
          unitId: number; // 单位ID
          quantity?: number; // 生产数量
          completedQuantity: number; // 已完成数量
          startTime: string | Dayjs; // 计划开始时间
          endTime: string | Dayjs; // 计划完成时间
          actualStartTime: string | Dayjs; // 实际开始时间
          actualEndTime: string | Dayjs; // 实际完成时间
          status?: number; // 状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消
          priority: number; // 优先级：1-紧急，2-高，3-中，4-低
          sourceType: number; // 来源类型：1-手动创建，2-销售订单，3-库存补充
          sourceId: number; // 来源单据ID
          description: string; // 生产说明
          remark: string; // 备注
  }

// ERP 生产订单 DO
 * API
export const ProductionOrderApi = {
  // 查询ERP 生产订单 DO
 *分页
  getProductionOrderPage: async (params: any) => {
    return await request.get({ url: `/erp/production-order/page`, params })
  },

  // 查询ERP 生产订单 DO
 *详情
  getProductionOrder: async (id: number) => {
    return await request.get({ url: `/erp/production-order/get?id=` + id })
  },

  // 新增ERP 生产订单 DO
 *
  createProductionOrder: async (data: ProductionOrder) => {
    return await request.post({ url: `/erp/production-order/create`, data })
  },

  // 修改ERP 生产订单 DO
 *
  updateProductionOrder: async (data: ProductionOrder) => {
    return await request.put({ url: `/erp/production-order/update`, data })
  },

  // 删除ERP 生产订单 DO
 *
  deleteProductionOrder: async (id: number) => {
    return await request.delete({ url: `/erp/production-order/delete?id=` + id })
  },

  /** 批量删除ERP 生产订单 DO
 * */
  deleteProductionOrderList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-order/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 生产订单 DO
 * Excel
  exportProductionOrder: async (params) => {
    return await request.download({ url: `/erp/production-order/export-excel`, params })
  }
}