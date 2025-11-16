package cn.iocoder.yudao.module.erp.dal.mysql.productionkpi;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionkpi.ProductionKpiDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo.*;

/**
 * ERP 生产KPI Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionKpiMapper extends BaseMapperX<ProductionKpiDO> {

    default PageResult<ProductionKpiDO> selectPage(ProductionKpiPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionKpiDO>()
                .eqIfPresent(ProductionKpiDO::getKpiNo, reqVO.getKpiNo())
                .likeIfPresent(ProductionKpiDO::getKpiName, reqVO.getKpiName())
                .eqIfPresent(ProductionKpiDO::getKpiType, reqVO.getKpiType())
                .eqIfPresent(ProductionKpiDO::getCategory, reqVO.getCategory())
                .eqIfPresent(ProductionKpiDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(ProductionKpiDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ProductionKpiDO::getMeasurementPeriod, reqVO.getMeasurementPeriod())
                .eqIfPresent(ProductionKpiDO::getTargetValue, reqVO.getTargetValue())
                .eqIfPresent(ProductionKpiDO::getActualValue, reqVO.getActualValue())
                .eqIfPresent(ProductionKpiDO::getVariance, reqVO.getVariance())
                .eqIfPresent(ProductionKpiDO::getVarianceRate, reqVO.getVarianceRate())
                .betweenIfPresent(ProductionKpiDO::getCalculationDate, reqVO.getCalculationDate())
                .betweenIfPresent(ProductionKpiDO::getPeriodStartTime, reqVO.getPeriodStartTime())
                .betweenIfPresent(ProductionKpiDO::getPeriodEndTime, reqVO.getPeriodEndTime())
                .eqIfPresent(ProductionKpiDO::getDataSource, reqVO.getDataSource())
                .eqIfPresent(ProductionKpiDO::getRemarks, reqVO.getRemarks())
                .betweenIfPresent(ProductionKpiDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductionKpiDO::getId));
    }

}