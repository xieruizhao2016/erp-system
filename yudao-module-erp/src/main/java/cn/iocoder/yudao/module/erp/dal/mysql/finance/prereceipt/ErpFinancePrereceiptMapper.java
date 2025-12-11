package cn.iocoder.yudao.module.erp.dal.mysql.finance-prereceipt;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-prereceipt.ErpFinancePrereceiptDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance-prereceipt.vo.*;

/**
 * 预收款 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinancePrereceiptMapper extends BaseMapperX<ErpFinancePrereceiptDO> {

    default PageResult<ErpFinancePrereceiptDO> selectPage(ErpFinancePrereceiptPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinancePrereceiptDO>()
                .eqIfPresent(ErpFinancePrereceiptDO::getNo, reqVO.getNo())
                .eqIfPresent(ErpFinancePrereceiptDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(ErpFinancePrereceiptDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ErpFinancePrereceiptDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ErpFinancePrereceiptDO::getUsedAmount, reqVO.getUsedAmount())
                .eqIfPresent(ErpFinancePrereceiptDO::getBalance, reqVO.getBalance())
                .eqIfPresent(ErpFinancePrereceiptDO::getPrereceiptDate, reqVO.getPrereceiptDate())
                .eqIfPresent(ErpFinancePrereceiptDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinancePrereceiptDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinancePrereceiptDO::getId));
    }

}