package cn.iocoder.yudao.module.erp.service.productiondashboardconfig;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productiondashboardconfig.ProductionDashboardConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 看板配置 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionDashboardConfigService {

    /**
     * 创建ERP 看板配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionDashboardConfig(@Valid ProductionDashboardConfigSaveReqVO createReqVO);

    /**
     * 更新ERP 看板配置
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionDashboardConfig(@Valid ProductionDashboardConfigSaveReqVO updateReqVO);

    /**
     * 删除ERP 看板配置
     *
     * @param id 编号
     */
    void deleteProductionDashboardConfig(Long id);

    /**
    * 批量删除ERP 看板配置
    *
    * @param ids 编号
    */
    void deleteProductionDashboardConfigListByIds(List<Long> ids);

    /**
     * 获得ERP 看板配置
     *
     * @param id 编号
     * @return ERP 看板配置
     */
    ProductionDashboardConfigDO getProductionDashboardConfig(Long id);

    /**
     * 获得ERP 看板配置分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 看板配置分页
     */
    PageResult<ProductionDashboardConfigDO> getProductionDashboardConfigPage(ProductionDashboardConfigPageReqVO pageReqVO);

}