package cn.iocoder.yudao.module.erp.dal.mysql.finance.payable;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable.ErpFinancePayableDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo.*;

/**
 * 应付账款 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinancePayableMapper extends BaseMapperX<ErpFinancePayableDO> {

    default PageResult<ErpFinancePayableDO> selectPage(ErpFinancePayablePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinancePayableDO>()
                .eqIfPresent(ErpFinancePayableDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpFinancePayableDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(ErpFinancePayableDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ErpFinancePayableDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ErpFinancePayableDO::getPaidAmount, reqVO.getPaidAmount())
                .eqIfPresent(ErpFinancePayableDO::getBalance, reqVO.getBalance())
                .eqIfPresent(ErpFinancePayableDO::getDueDate, reqVO.getDueDate())
                .eqIfPresent(ErpFinancePayableDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinancePayableDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinancePayableDO::getId));
    }

    /**
     * 根据订单ID查询应付账款
     */
    default ErpFinancePayableDO selectByOrderId(Long orderId) {
        return selectOne(ErpFinancePayableDO::getOrderId, orderId);
    }

    /**
     * 根据供应商ID和余额查询未核销的应付账款列表（按到期日排序）
     */
    default List<ErpFinancePayableDO> selectListBySupplierIdAndBalance(Long supplierId, BigDecimal minBalance) {
        return selectList(new LambdaQueryWrapperX<ErpFinancePayableDO>()
                .eq(ErpFinancePayableDO::getSupplierId, supplierId)
                .gt(ErpFinancePayableDO::getBalance, minBalance)
                .orderByAsc(ErpFinancePayableDO::getDueDate));
    }

}