package cn.iocoder.yudao.module.erp.service.finance.receivable;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 应收账款 Service 接口
 *
 * @author 开发团队
 */
public interface ErpFinanceReceivableService {

    /**
     * 创建应收账款
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFinanceReceivable(@Valid ErpFinanceReceivableSaveReqVO createReqVO);

    /**
     * 更新应收账款
     *
     * @param updateReqVO 更新信息
     */
    void updateFinanceReceivable(@Valid ErpFinanceReceivableSaveReqVO updateReqVO);

    /**
     * 删除应收账款
     *
     * @param id 编号
     */
    void deleteFinanceReceivable(Long id);

    /**
    * 批量删除应收账款
    *
    * @param ids 编号
    */
    void deleteFinanceReceivableListByIds(List<Long> ids);

    /**
     * 获得应收账款
     *
     * @param id 编号
     * @return 应收账款
     */
    ErpFinanceReceivableDO getFinanceReceivable(Long id);

    /**
     * 获得应收账款分页
     *
     * @param pageReqVO 分页查询
     * @return 应收账款分页
     */
    PageResult<ErpFinanceReceivableDO> getFinanceReceivablePage(ErpFinanceReceivablePageReqVO pageReqVO);

    // ==================== 业务方法 ====================

    /**
     * 从销售订单创建应收账款
     *
     * @param saleOrder 销售订单
     */
    void createReceivableFromSaleOrder(ErpSaleOrderDO saleOrder);

    /**
     * 根据订单ID删除应收账款
     *
     * @param orderId 订单ID
     */
    void deleteReceivableByOrderId(Long orderId);

    /**
     * 核销应收账款
     *
     * @param customerId 客户ID
     * @param amount 核销金额
     */
    void writeOff(Long customerId, BigDecimal amount);

}