package cn.iocoder.yudao.module.erp.dal.mysql.finance-prepayment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-prepayment.ErpFinancePrepaymentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance-prepayment.vo.*;

/**
 * 预付款 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinancePrepaymentMapper extends BaseMapperX<ErpFinancePrepaymentDO> {

    default PageResult<ErpFinancePrepaymentDO> selectPage(ErpFinancePrepaymentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinancePrepaymentDO>()
                .eqIfPresent(ErpFinancePrepaymentDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpFinancePrepaymentDO::getSupplierId, reqVO.getSupplierId())
                .eqIfPresent(ErpFinancePrepaymentDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ErpFinancePrepaymentDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ErpFinancePrepaymentDO::getUsedAmount, reqVO.getUsedAmount())
                .eqIfPresent(ErpFinancePrepaymentDO::getBalance, reqVO.getBalance())
                .eqIfPresent(ErpFinancePrepaymentDO::getPrepayDate, reqVO.getPrepayDate())
                .eqIfPresent(ErpFinancePrepaymentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinancePrepaymentDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinancePrepaymentDO::getId));
    }

}