package cn.iocoder.yudao.module.erp.dal.mysql.productionreport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionreport.ProductionReportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo.*;

/**
 * ERP 生产报表 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionReportMapper extends BaseMapperX<ProductionReportDO> {

    default PageResult<ProductionReportDO> selectPage(ProductionReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionReportDO>()
                .eqIfPresent(ProductionReportDO::getReportNo, reqVO.getReportNo())
                .likeIfPresent(ProductionReportDO::getReportName, reqVO.getReportName())
                .eqIfPresent(ProductionReportDO::getReportType, reqVO.getReportType())
                .eqIfPresent(ProductionReportDO::getReportPeriod, reqVO.getReportPeriod())
                .eqIfPresent(ProductionReportDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(ProductionReportDO::getProductionOrders, reqVO.getProductionOrders())
                .eqIfPresent(ProductionReportDO::getTotalPlanQuantity, reqVO.getTotalPlanQuantity())
                .eqIfPresent(ProductionReportDO::getTotalCompletedQuantity, reqVO.getTotalCompletedQuantity())
                .eqIfPresent(ProductionReportDO::getTotalQualifiedQuantity, reqVO.getTotalQualifiedQuantity())
                .eqIfPresent(ProductionReportDO::getCompletionRate, reqVO.getCompletionRate())
                .eqIfPresent(ProductionReportDO::getQualityRate, reqVO.getQualityRate())
                .eqIfPresent(ProductionReportDO::getTotalWorkHours, reqVO.getTotalWorkHours())
                .eqIfPresent(ProductionReportDO::getTotalEquipmentHours, reqVO.getTotalEquipmentHours())
                .eqIfPresent(ProductionReportDO::getOee, reqVO.getOee())
                .eqIfPresent(ProductionReportDO::getOnTimeDeliveryRate, reqVO.getOnTimeDeliveryRate())
                .eqIfPresent(ProductionReportDO::getProductionValue, reqVO.getProductionValue())
                .eqIfPresent(ProductionReportDO::getAverageCost, reqVO.getAverageCost())
                .betweenIfPresent(ProductionReportDO::getReportDate, reqVO.getReportDate())
                .betweenIfPresent(ProductionReportDO::getGenerateTime, reqVO.getGenerateTime())
                .eqIfPresent(ProductionReportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ProductionReportDO::getReportData, reqVO.getReportData())
                .betweenIfPresent(ProductionReportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductionReportDO::getId));
    }

}