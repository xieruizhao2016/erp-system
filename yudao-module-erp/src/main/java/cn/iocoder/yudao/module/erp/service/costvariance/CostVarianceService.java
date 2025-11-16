package cn.iocoder.yudao.module.erp.service.costvariance;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costvariance.CostVarianceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 成本差异分析 Service 接口
 *
 * @author 芋道源码
 */
public interface CostVarianceService {

    /**
     * 创建ERP 成本差异分析
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCostVariance(@Valid CostVarianceSaveReqVO createReqVO);

    /**
     * 更新ERP 成本差异分析
     *
     * @param updateReqVO 更新信息
     */
    void updateCostVariance(@Valid CostVarianceSaveReqVO updateReqVO);

    /**
     * 删除ERP 成本差异分析
     *
     * @param id 编号
     */
    void deleteCostVariance(Long id);

    /**
    * 批量删除ERP 成本差异分析
    *
    * @param ids 编号
    */
    void deleteCostVarianceListByIds(List<Long> ids);

    /**
     * 获得ERP 成本差异分析
     *
     * @param id 编号
     * @return ERP 成本差异分析
     */
    CostVarianceDO getCostVariance(Long id);

    /**
     * 获得ERP 成本差异分析分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 成本差异分析分页
     */
    PageResult<CostVarianceDO> getCostVariancePage(CostVariancePageReqVO pageReqVO);

}