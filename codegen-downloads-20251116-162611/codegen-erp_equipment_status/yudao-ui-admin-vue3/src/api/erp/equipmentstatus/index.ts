import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 设备状态记录信息 */
export interface EquipmentStatus {
          id: number; // 编号
          equipmentId?: number; // 设备ID
          status?: number; // 状态：1-运行，2-待机，3-故障，4-维修，5-停机
          statusStartTime?: string | Dayjs; // 状态开始时间
          statusEndTime: string | Dayjs; // 状态结束时间
          duration: number; // 持续时间（分钟）
          workOrderId: number; // 关联工单ID
          operatorId: number; // 操作员ID
          remark: string; // 备注
  }

// ERP 设备状态记录 API
export const EquipmentStatusApi = {
  // 查询ERP 设备状态记录分页
  getEquipmentStatusPage: async (params: any) => {
    return await request.get({ url: `/erp/equipment-status/page`, params })
  },

  // 查询ERP 设备状态记录详情
  getEquipmentStatus: async (id: number) => {
    return await request.get({ url: `/erp/equipment-status/get?id=` + id })
  },

  // 新增ERP 设备状态记录
  createEquipmentStatus: async (data: EquipmentStatus) => {
    return await request.post({ url: `/erp/equipment-status/create`, data })
  },

  // 修改ERP 设备状态记录
  updateEquipmentStatus: async (data: EquipmentStatus) => {
    return await request.put({ url: `/erp/equipment-status/update`, data })
  },

  // 删除ERP 设备状态记录
  deleteEquipmentStatus: async (id: number) => {
    return await request.delete({ url: `/erp/equipment-status/delete?id=` + id })
  },

  /** 批量删除ERP 设备状态记录 */
  deleteEquipmentStatusList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/equipment-status/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 设备状态记录 Excel
  exportEquipmentStatus: async (params) => {
    return await request.download({ url: `/erp/equipment-status/export-excel`, params })
  }
}