package cn.iocoder.yudao.module.erp.dal.mysql.costactual;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.costactual.CostActualDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.costactual.vo.*;

/**
 * ERP 实际成本 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CostActualMapper extends BaseMapperX<CostActualDO> {

    default PageResult<CostActualDO> selectPage(CostActualPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CostActualDO>()
                .eqIfPresent(CostActualDO::getCostNo, reqVO.getCostNo())
                .eqIfPresent(CostActualDO::getWorkOrderId, reqVO.getWorkOrderId())
                .eqIfPresent(CostActualDO::getProductionOrderId, reqVO.getProductionOrderId())
                .eqIfPresent(CostActualDO::getProductId, reqVO.getProductId())
                .eqIfPresent(CostActualDO::getProductionQuantity, reqVO.getProductionQuantity())
                .eqIfPresent(CostActualDO::getMaterialCost, reqVO.getMaterialCost())
                .eqIfPresent(CostActualDO::getMaterialCostAdjust, reqVO.getMaterialCostAdjust())
                .eqIfPresent(CostActualDO::getLaborCost, reqVO.getLaborCost())
                .eqIfPresent(CostActualDO::getLaborCostAdjust, reqVO.getLaborCostAdjust())
                .eqIfPresent(CostActualDO::getOverheadCost, reqVO.getOverheadCost())
                .eqIfPresent(CostActualDO::getOverheadCostAdjust, reqVO.getOverheadCostAdjust())
                .eqIfPresent(CostActualDO::getTotalCost, reqVO.getTotalCost())
                .eqIfPresent(CostActualDO::getUnitCost, reqVO.getUnitCost())
                .eqIfPresent(CostActualDO::getCostCurrency, reqVO.getCostCurrency())
                .eqIfPresent(CostActualDO::getCostPeriod, reqVO.getCostPeriod())
                .betweenIfPresent(CostActualDO::getCalculationDate, reqVO.getCalculationDate())
                .betweenIfPresent(CostActualDO::getLastAdjustDate, reqVO.getLastAdjustDate())
                .eqIfPresent(CostActualDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CostActualDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CostActualDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CostActualDO::getId));
    }

}