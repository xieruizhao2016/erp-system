package cn.iocoder.yudao.module.erp.service.coststandard;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.coststandard.CostStandardDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 标准成本 Service 接口
 *
 * @author 芋道源码
 */
public interface CostStandardService {

    /**
     * 创建ERP 标准成本
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCostStandard(@Valid CostStandardSaveReqVO createReqVO);

    /**
     * 更新ERP 标准成本
     *
     * @param updateReqVO 更新信息
     */
    void updateCostStandard(@Valid CostStandardSaveReqVO updateReqVO);

    /**
     * 删除ERP 标准成本
     *
     * @param id 编号
     */
    void deleteCostStandard(Long id);

    /**
    * 批量删除ERP 标准成本
    *
    * @param ids 编号
    */
    void deleteCostStandardListByIds(List<Long> ids);

    /**
     * 获得ERP 标准成本
     *
     * @param id 编号
     * @return ERP 标准成本
     */
    CostStandardDO getCostStandard(Long id);

    /**
     * 获得ERP 标准成本分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 标准成本分页
     */
    PageResult<CostStandardDO> getCostStandardPage(CostStandardPageReqVO pageReqVO);

}