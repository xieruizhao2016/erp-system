package cn.iocoder.yudao.module.erp.service.productionorder;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 生产订单 DO
 * Service 接口
 *
 * @author 芋道源码
 */
public interface ProductionOrderService {

    /**
     * 创建ERP 生产订单 DO
 *
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductionOrder(@Valid ProductionOrderSaveReqVO createReqVO);

    /**
     * 更新ERP 生产订单 DO
 *
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionOrder(@Valid ProductionOrderSaveReqVO updateReqVO);

    /**
     * 删除ERP 生产订单 DO
 *
     *
     * @param id 编号
     */
    void deleteProductionOrder(Long id);

    /**
    * 批量删除ERP 生产订单 DO
 *
    *
    * @param ids 编号
    */
    void deleteProductionOrderListByIds(List<Long> ids);

    /**
     * 获得ERP 生产订单 DO
 *
     *
     * @param id 编号
     * @return ERP 生产订单 DO
 *
     */
    ProductionOrderDO getProductionOrder(Long id);

    /**
     * 获得ERP 生产订单 DO
 *分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 生产订单 DO
 *分页
     */
    PageResult<ProductionOrderDO> getProductionOrderPage(ProductionOrderPageReqVO pageReqVO);

    /**
     * 更新生产订单状态
     *
     * @param id 编号
     * @param status 状态
     */
    void updateProductionOrderStatus(Long id, Integer status);

}