package cn.iocoder.yudao.module.erp.dal.mysql.finance.balancesheet;

import java.util.*;

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
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpFinanceBalanceSheetDO>()
                .eqIfPresent(ErpFinanceBalanceSheetDO::getPeriodDate, reqVO.getPeriodDate())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getAssetTotal, reqVO.getAssetTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getLiabilityTotal, reqVO.getLiabilityTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getEquityTotal, reqVO.getEquityTotal())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ErpFinanceBalanceSheetDO::getRemark, reqVO.getRemark())
                .orderByDesc(ErpFinanceBalanceSheetDO::getId));
    }

}