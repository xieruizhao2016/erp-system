import request from '@/config/axios'
import type { Dayjs } from 'dayjs';

/** ERP 看板配置信息 */
export interface ProductionDashboardConfig {
          id: number; // 编号
          configName?: string; // 配置名称
          configType: number; // 配置类型：1-大屏，2-PC端，3-移动端
          screenResolution: string; // 屏幕分辨率
          layoutConfig?: string; // 布局配置（JSON）
          componentConfig?: string; // 组件配置（JSON）
          dataRefreshInterval: number; // 数据刷新间隔（秒）
          isDefault: boolean; // 是否默认配置
          isActive: boolean; // 是否启用
  }

// ERP 看板配置 API
export const ProductionDashboardConfigApi = {
  // 查询ERP 看板配置分页
  getProductionDashboardConfigPage: async (params: any) => {
    return await request.get({ url: `/erp/production-dashboard-config/page`, params })
  },

  // 查询ERP 看板配置详情
  getProductionDashboardConfig: async (id: number) => {
    return await request.get({ url: `/erp/production-dashboard-config/get?id=` + id })
  },

  // 新增ERP 看板配置
  createProductionDashboardConfig: async (data: ProductionDashboardConfig) => {
    return await request.post({ url: `/erp/production-dashboard-config/create`, data })
  },

  // 修改ERP 看板配置
  updateProductionDashboardConfig: async (data: ProductionDashboardConfig) => {
    return await request.put({ url: `/erp/production-dashboard-config/update`, data })
  },

  // 删除ERP 看板配置
  deleteProductionDashboardConfig: async (id: number) => {
    return await request.delete({ url: `/erp/production-dashboard-config/delete?id=` + id })
  },

  /** 批量删除ERP 看板配置 */
  deleteProductionDashboardConfigList: async (ids: number[]) => {
    return await request.delete({ url: `/erp/production-dashboard-config/delete-list?ids=${ids.join(',')}` })
  },

  // 导出ERP 看板配置 Excel
  exportProductionDashboardConfig: async (params) => {
    return await request.download({ url: `/erp/production-dashboard-config/export-excel`, params })
  }
}