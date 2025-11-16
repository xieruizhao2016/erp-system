package cn.iocoder.yudao.module.erp.service.workorder;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.workorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * ERP 工单主 Service 接口
 *
 * @author 芋道源码
 */
public interface WorkOrderService {

    /**
     * 创建ERP 工单主
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkOrder(@Valid WorkOrderSaveReqVO createReqVO);

    /**
     * 更新ERP 工单主
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkOrder(@Valid WorkOrderSaveReqVO updateReqVO);

    /**
     * 删除ERP 工单主
     *
     * @param id 编号
     */
    void deleteWorkOrder(Long id);

    /**
    * 批量删除ERP 工单主
    *
    * @param ids 编号
    */
    void deleteWorkOrderListByIds(List<Long> ids);

    /**
     * 获得ERP 工单主
     *
     * @param id 编号
     * @return ERP 工单主
     */
    WorkOrderDO getWorkOrder(Long id);

    /**
     * 获得ERP 工单主分页
     *
     * @param pageReqVO 分页查询
     * @return ERP 工单主分页
     */
    PageResult<WorkOrderDO> getWorkOrderPage(WorkOrderPageReqVO pageReqVO);

}