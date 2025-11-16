package cn.iocoder.yudao.module.erp.service.costactual;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.costactual.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costactual.CostActualDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 实际成本 Service 接口
 *
 * @author 芋道源码
 */
public interface CostActualService {

    /**
     * 创建ERP 实际成本
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCostActual(@Valid CostActualSaveReqVO createReqVO);

    /**
     * 更新ERP 实际成本
     *
     * @param updateReqVO 更新信息
     */
    void updateCostActual(@Valid CostActualSaveReqVO updateReqVO);

    /**
     * 删除ERP 实际成本
     *
     * @param id 编号
     */
    void deleteCostActual(Long id);

    /**
    * 批量删除ERP 实际成本
    *
    * @param ids 编号
    */
    void deleteCostActualListByIds(List<Long> ids);

    /**
     * 获得ERP 实际成本
     *
     * @param id 编号
     * @return ERP 实际成本
     */
    CostActualDO getCostActual(Long id);

    /**
     * 获得ERP 实际成本分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 实际成本分页
     */
    PageResult<CostActualDO> getCostActualPage(CostActualPageReqVO pageReqVO);

}