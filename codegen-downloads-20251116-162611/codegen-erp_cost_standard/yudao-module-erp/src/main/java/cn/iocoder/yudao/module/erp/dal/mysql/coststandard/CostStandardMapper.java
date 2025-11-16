package cn.iocoder.yudao.module.erp.dal.mysql.coststandard;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.coststandard.CostStandardDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo.*;

/**
 * ERP 标准成本 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CostStandardMapper extends BaseMapperX<CostStandardDO> {

    default PageResult<CostStandardDO> selectPage(CostStandardPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CostStandardDO>()
                .eqIfPresent(CostStandardDO::getProductId, reqVO.getProductId())
                .eqIfPresent(CostStandardDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(CostStandardDO::getEffectiveDate, reqVO.getEffectiveDate())
                .betweenIfPresent(CostStandardDO::getExpireDate, reqVO.getExpireDate())
                .eqIfPresent(CostStandardDO::getMaterialCost, reqVO.getMaterialCost())
                .eqIfPresent(CostStandardDO::getLaborCost, reqVO.getLaborCost())
                .eqIfPresent(CostStandardDO::getOverheadCost, reqVO.getOverheadCost())
                .eqIfPresent(CostStandardDO::getTotalCost, reqVO.getTotalCost())
                .eqIfPresent(CostStandardDO::getCostCurrency, reqVO.getCostCurrency())
                .betweenIfPresent(CostStandardDO::getCalculationDate, reqVO.getCalculationDate())
                .eqIfPresent(CostStandardDO::getBomVersion, reqVO.getBomVersion())
                .eqIfPresent(CostStandardDO::getRouteVersion, reqVO.getRouteVersion())
                .eqIfPresent(CostStandardDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CostStandardDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CostStandardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CostStandardDO::getId));
    }

}