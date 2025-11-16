package cn.iocoder.yudao.module.erp.service.productionkpi;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionkpi.ProductionKpiDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 生产KPI Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionKpiService {

    /**
     * 创建ERP 生产KPI
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionKpi(@Valid ProductionKpiSaveReqVO createReqVO);

    /**
     * 更新ERP 生产KPI
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionKpi(@Valid ProductionKpiSaveReqVO updateReqVO);

    /**
     * 删除ERP 生产KPI
     *
     * @param id 编号
     */
    void deleteProductionKpi(Long id);

    /**
    * 批量删除ERP 生产KPI
    *
    * @param ids 编号
    */
    void deleteProductionKpiListByIds(List<Long> ids);

    /**
     * 获得ERP 生产KPI
     *
     * @param id 编号
     * @return ERP 生产KPI
     */
    ProductionKpiDO getProductionKpi(Long id);

    /**
     * 获得ERP 生产KPI分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 生产KPI分页
     */
    PageResult<ProductionKpiDO> getProductionKpiPage(ProductionKpiPageReqVO pageReqVO);

}