package cn.iocoder.yudao.module.erp.service.productionreport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionreport.ProductionReportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 生产报表 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionReportService {

    /**
     * 创建ERP 生产报表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionReport(@Valid ProductionReportSaveReqVO createReqVO);

    /**
     * 更新ERP 生产报表
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionReport(@Valid ProductionReportSaveReqVO updateReqVO);

    /**
     * 删除ERP 生产报表
     *
     * @param id 编号
     */
    void deleteProductionReport(Long id);

    /**
    * 批量删除ERP 生产报表
    *
    * @param ids 编号
    */
    void deleteProductionReportListByIds(List<Long> ids);

    /**
     * 获得ERP 生产报表
     *
     * @param id 编号
     * @return ERP 生产报表
     */
    ProductionReportDO getProductionReport(Long id);

    /**
     * 获得ERP 生产报表分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 生产报表分页
     */
    PageResult<ProductionReportDO> getProductionReportPage(ProductionReportPageReqVO pageReqVO);

}