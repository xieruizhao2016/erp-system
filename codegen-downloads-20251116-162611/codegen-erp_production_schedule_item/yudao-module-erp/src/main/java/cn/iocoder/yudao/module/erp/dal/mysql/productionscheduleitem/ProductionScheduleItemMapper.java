package cn.iocoder.yudao.module.erp.dal.mysql.productionscheduleitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionscheduleitem.ProductionScheduleItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo.*;

/**
 * ERP 排程明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionScheduleItemMapper extends BaseMapperX<ProductionScheduleItemDO> {

    default PageResult<ProductionScheduleItemDO> selectPage(ProductionScheduleItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionScheduleItemDO>()
                .eqIfPresent(ProductionScheduleItemDO::getScheduleId, reqVO.getScheduleId())
                .eqIfPresent(ProductionScheduleItemDO::getProductionOrderId, reqVO.getProductionOrderId())
                .eqIfPresent(ProductionScheduleItemDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ProductionScheduleItemDO::getQuantity, reqVO.getQuantity())
                .betweenIfPresent(ProductionScheduleItemDO::getPlannedStartTime, reqVO.getPlannedStartTime())
                .betweenIfPresent(ProductionScheduleItemDO::getPlannedEndTime, reqVO.getPlannedEndTime())
                .eqIfPresent(ProductionScheduleItemDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(ProductionScheduleItemDO::getEquipmentId, reqVO.getEquipmentId())
                .eqIfPresent(ProductionScheduleItemDO::getProcessSequence, reqVO.getProcessSequence())
                .eqIfPresent(ProductionScheduleItemDO::getPriority, reqVO.getPriority())
                .betweenIfPresent(ProductionScheduleItemDO::getSetupTime, reqVO.getSetupTime())
                .betweenIfPresent(ProductionScheduleItemDO::getRunTime, reqVO.getRunTime())
                .betweenIfPresent(ProductionScheduleItemDO::getWaitTime, reqVO.getWaitTime())
                .eqIfPresent(ProductionScheduleItemDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductionScheduleItemDO::getActualStartTime, reqVO.getActualStartTime())
                .betweenIfPresent(ProductionScheduleItemDO::getActualEndTime, reqVO.getActualEndTime())
                .eqIfPresent(ProductionScheduleItemDO::getCompletionRate, reqVO.getCompletionRate())
                .eqIfPresent(ProductionScheduleItemDO::getDelayReason, reqVO.getDelayReason())
                .betweenIfPresent(ProductionScheduleItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductionScheduleItemDO::getId));
    }

}