package cn.iocoder.yudao.module.erp.service.finance.payable;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 应付账款 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinancePayableService {

    /**
     * 创建应付账款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinancePayable(@Valid ErpFinancePayableSaveReqVO createReqVO);

    /**
     * 更新应付账款
     *
     * @param updateReqVO 更新信息
     */
    void updateFinancePayable(@Valid ErpFinancePayableSaveReqVO updateReqVO);

    /**
     * 删除应付账款
     *
     * @param id 编号
     */
    void deleteFinancePayable(Long id);

    /**
    * 批量删除应付账款
    *
    * @param ids 编号
    */
    void deleteFinancePayableListByIds(List<Long> ids);

    /**
     * 获得应付账款
     *
     * @param id 编号
     * @return 应付账款
     */
    ErpFinancePayableDO getFinancePayable(Long id);

    /**
     * 获得应付账款分页
     *
     * @param pageReqVO 分页查询
     * @return 应付账款分页
     */
    PageResult<ErpFinancePayableDO> getFinancePayablePage(ErpFinancePayablePageReqVO pageReqVO);

    // ==================== 业务方法 ====================

    /**
     * 从采购订单创建应付账款
     *
     * @param purchaseOrder 采购订单
     */
    void createPayableFromPurchaseOrder(ErpPurchaseOrderDO purchaseOrder);

    /**
     * 根据订单ID删除应付账款
     *
     * @param orderId 订单ID
     */
    void deletePayableByOrderId(Long orderId);

    /**
     * 核销应付账款
     *
     * @param supplierId 供应商ID
     * @param amount 核销金额
     */
    void writeOff(Long supplierId, BigDecimal amount);

}