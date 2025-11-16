package cn.iocoder.yudao.module.erp.service.productionschedule;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule.ProductionScheduleDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 生产排程主 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionScheduleService {

    /**
     * 创建ERP 生产排程主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionSchedule(@Valid ProductionScheduleSaveReqVO createReqVO);

    /**
     * 更新ERP 生产排程主
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionSchedule(@Valid ProductionScheduleSaveReqVO updateReqVO);

    /**
     * 删除ERP 生产排程主
     *
     * @param id 编号
     */
    void deleteProductionSchedule(Long id);

    /**
    * 批量删除ERP 生产排程主
    *
    * @param ids 编号
    */
    void deleteProductionScheduleListByIds(List<Long> ids);

    /**
     * 获得ERP 生产排程主
     *
     * @param id 编号
     * @return ERP 生产排程主
     */
    ProductionScheduleDO getProductionSchedule(Long id);

    /**
     * 获得ERP 生产排程主分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 生产排程主分页
     */
    PageResult<ProductionScheduleDO> getProductionSchedulePage(ProductionSchedulePageReqVO pageReqVO);

}