package cn.iocoder.yudao.module.erp.dal.mysql.finance.profitstatement;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement.ErpFinanceProfitStatementDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo.*;

/**
 * 利润表 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinanceProfitStatementMapper extends BaseMapperX<ErpFinanceProfitStatementDO> {

    default PageResult<ErpFinanceProfitStatementDO> selectPage(ErpFinanceProfitStatementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinanceProfitStatementDO>()
                .eqIfPresent(ErpFinanceProfitStatementDO::getPeriodDate, reqVO.getPeriodDate())
                .eqIfPresent(ErpFinanceProfitStatementDO::getRevenue, reqVO.getRevenue())
                .eqIfPresent(ErpFinanceProfitStatementDO::getCost, reqVO.getCost())
                .eqIfPresent(ErpFinanceProfitStatementDO::getGrossProfit, reqVO.getGrossProfit())
                .eqIfPresent(ErpFinanceProfitStatementDO::getOperatingExpense, reqVO.getOperatingExpense())
                .eqIfPresent(ErpFinanceProfitStatementDO::getNetProfit, reqVO.getNetProfit())
                .eqIfPresent(ErpFinanceProfitStatementDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinanceProfitStatementDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinanceProfitStatementDO::getId));
    }

}