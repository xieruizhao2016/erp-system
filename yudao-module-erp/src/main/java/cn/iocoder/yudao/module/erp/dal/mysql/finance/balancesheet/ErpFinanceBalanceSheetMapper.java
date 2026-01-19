package cn.iocoder.yudao.module.erp.dal.mysql.finance.balancesheet;

import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;

/**
 * 资产负债表 Mapper
 *
 * @author 开发团队
 */
@Mapper
public interface ErpFinanceBalanceSheetMapper extends BaseMapperX<ErpFinanceBalanceSheetDO> {

    default PageResult<ErpFinanceBalanceSheetDO> selectPage(ErpFinanceBalanceSheetPageReqVO reqVO) {
        LambdaQueryWrapperX<ErpFinanceBalanceSheetDO> wrapper = new LambdaQueryWrapperX<ErpFinanceBalanceSheetDO>()
                .eqIfPresent(ErpFinanceBalanceSheetDO::getAssetTotal, reqVO.getAssetTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getLiabilityTotal, reqVO.getLiabilityTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getEquityTotal, reqVO.getEquityTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getRemark, reqVO.getRemark());
        
        // 处理 periodDate 查询：如果是 LocalDate，使用日期范围查询
        if (reqVO.getPeriodDate() != null) {
            LocalDate periodDate = reqVO.getPeriodDate();
            LocalDateTime startOfDay = periodDate.atStartOfDay();
            LocalDateTime endOfDay = periodDate.plusDays(1).atStartOfDay();
            wrapper.between(ErpFinanceBalanceSheetDO::getPeriodDate, startOfDay, endOfDay);
        }
        
        return selectPage(reqVO, wrapper.orderByDesc(ErpFinanceBalanceSheetDO::getId));
    }

}