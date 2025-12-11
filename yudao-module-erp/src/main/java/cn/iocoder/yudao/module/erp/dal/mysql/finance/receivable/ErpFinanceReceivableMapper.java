package cn.iocoder.yudao.module.erp.dal.mysql.finance.receivable;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.*;

/**
 * 应收账款 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinanceReceivableMapper extends BaseMapperX<ErpFinanceReceivableDO> {

    default PageResult<ErpFinanceReceivableDO> selectPage(ErpFinanceReceivablePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinanceReceivableDO>()
                .eqIfPresent(ErpFinanceReceivableDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpFinanceReceivableDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(ErpFinanceReceivableDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ErpFinanceReceivableDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ErpFinanceReceivableDO::getReceivedAmount, reqVO.getReceivedAmount())
                .eqIfPresent(ErpFinanceReceivableDO::getBalance, reqVO.getBalance())
                .eqIfPresent(ErpFinanceReceivableDO::getDueDate, reqVO.getDueDate())
                .eqIfPresent(ErpFinanceReceivableDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinanceReceivableDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinanceReceivableDO::getId));
    }

    /**
     * 根据订单ID查询应收账款
     */
    default ErpFinanceReceivableDO selectByOrderId(Long orderId) {
        return selectOne(ErpFinanceReceivableDO::getOrderId, orderId);
    }

    /**
     * 根据客户ID和余额查询未核销的应收账款列表（按到期日排序）
     */
    default List<ErpFinanceReceivableDO> selectListByCustomerIdAndBalance(Long customerId, BigDecimal minBalance) {
        return selectList(new LambdaQueryWrapperX<ErpFinanceReceivableDO>()
                .eq(ErpFinanceReceivableDO::getCustomerId, customerId)
                .gt(ErpFinanceReceivableDO::getBalance, minBalance)
                .orderByAsc(ErpFinanceReceivableDO::getDueDate));
    }

}