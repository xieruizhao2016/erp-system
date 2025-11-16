package cn.iocoder.yudao.module.erp.service.productionscheduleitem;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionscheduleitem.ProductionScheduleItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 排程明细 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionScheduleItemService {

    /**
     * 创建ERP 排程明细
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionScheduleItem(@Valid ProductionScheduleItemSaveReqVO createReqVO);

    /**
     * 更新ERP 排程明细
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionScheduleItem(@Valid ProductionScheduleItemSaveReqVO updateReqVO);

    /**
     * 删除ERP 排程明细
     *
     * @param id 编号
     */
    void deleteProductionScheduleItem(Long id);

    /**
    * 批量删除ERP 排程明细
    *
    * @param ids 编号
    */
    void deleteProductionScheduleItemListByIds(List<Long> ids);

    /**
     * 获得ERP 排程明细
     *
     * @param id 编号
     * @return ERP 排程明细
     */
    ProductionScheduleItemDO getProductionScheduleItem(Long id);

    /**
     * 获得ERP 排程明细分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 排程明细分页
     */
    PageResult<ProductionScheduleItemDO> getProductionScheduleItemPage(ProductionScheduleItemPageReqVO pageReqVO);

}