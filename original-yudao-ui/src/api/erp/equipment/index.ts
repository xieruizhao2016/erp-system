import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 设备台账信息 */
export interface Equipment {
          id: number; // 编号
          equipmentNo?: string; // 设备编号
          equipmentName?: string; // 设备名称
          equipmentType: string; // 设备类型
          model: string; // 设备型号
          manufacturer: string; // 制造商
          serialNumber: string; // 序列号
          purchaseDate: string | Dayjs; // 购置日期
          purchasePrice: number; // 购置价格
          serviceLife: number; // 设计寿命（年）
          workCenterId: number; // 工作中心ID
          location: string; // 设备位置
          capacity: number; // 产能（小时/天）
          efficiencyRate: number; // 效率系数
          status: number; // 状态：1-正常，2-维修中，3-故障，4-报废
          responsiblePerson: string; // 责任人
          specification: string; // 技术规格
          remark: string; // 备注
  }

// ERP 设备台账 API
export const EquipmentApi = {
  // 查询ERP 设备台账分页
  getEquipmentPage: async (params: any) => {
    return await request.get({ url: `/erp/equipment/page`, params })
  },

  // 查询ERP 设备台账详情
  getEquipment: async (id: number) => {
    return await request.get({ url: `/erp/equipment/get?id=` + id })
  },

  // 新增ERP 设备台账
  createEquipment: async (data: Equipment) => {
    return await request.post({ url: `/erp/equipment/create`, data })
  },

  // 修改ERP 设备台账
  updateEquipment: async (data: Equipment) => {
    return await request.put({ url: `/erp/equipment/update`, data })
  },

  // 删除ERP 设备台账
  deleteEquipment: async (id: number) => {
    return await request.delete({ url: `/erp/equipment/delete?id=` + id })
  },

  /** 批量删除ERP 设备台账 */
  deleteEquipmentList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/equipment/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 设备台账 Excel
  exportEquipment: async (params) => {
    return await request.download({ url: `/erp/equipment/export-excel`, params })
  }
}