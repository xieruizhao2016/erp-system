package cn.iocoder.yudao.module.erp.dal.mysql.finance-payable;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-payable.ErpFinancePayableDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance-payable.vo.*;

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

}