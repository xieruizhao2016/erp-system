package cn.iocoder.yudao.module.erp.dal.mysql.costvariance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.costvariance.CostVarianceDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo.*;

/**
 * ERP 成本差异分析 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CostVarianceMapper extends BaseMapperX<CostVarianceDO> {

    default PageResult<CostVarianceDO> selectPage(CostVariancePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CostVarianceDO>()
                .eqIfPresent(CostVarianceDO::getCostActualId, reqVO.getCostActualId())
                .eqIfPresent(CostVarianceDO::getProductId, reqVO.getProductId())
                .eqIfPresent(CostVarianceDO::getProductionQuantity, reqVO.getProductionQuantity())
                .eqIfPresent(CostVarianceDO::getStandardTotalCost, reqVO.getStandardTotalCost())
                .eqIfPresent(CostVarianceDO::getActualTotalCost, reqVO.getActualTotalCost())
                .eqIfPresent(CostVarianceDO::getTotalVariance, reqVO.getTotalVariance())
                .eqIfPresent(CostVarianceDO::getTotalVarianceRate, reqVO.getTotalVarianceRate())
                .eqIfPresent(CostVarianceDO::getMaterialVariance, reqVO.getMaterialVariance())
                .eqIfPresent(CostVarianceDO::getMaterialVarianceRate, reqVO.getMaterialVarianceRate())
                .eqIfPresent(CostVarianceDO::getLaborVariance, reqVO.getLaborVariance())
                .eqIfPresent(CostVarianceDO::getLaborVarianceRate, reqVO.getLaborVarianceRate())
                .eqIfPresent(CostVarianceDO::getOverheadVariance, reqVO.getOverheadVariance())
                .eqIfPresent(CostVarianceDO::getOverheadVarianceRate, reqVO.getOverheadVarianceRate())
                .eqIfPresent(CostVarianceDO::getVarianceType, reqVO.getVarianceType())
                .betweenIfPresent(CostVarianceDO::getAnalysisDate, reqVO.getAnalysisDate())
                .eqIfPresent(CostVarianceDO::getVarianceReason, reqVO.getVarianceReason())
                .eqIfPresent(CostVarianceDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(CostVarianceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CostVarianceDO::getId));
    }

}