package cn.iocoder.yudao.module.erp.dal.mysql.productionschedule;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule.ProductionScheduleDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo.*;

/**
 * ERP 生产排程主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionScheduleMapper extends BaseMapperX<ProductionScheduleDO> {

    default PageResult<ProductionScheduleDO> selectPage(ProductionSchedulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionScheduleDO>()
                .eqIfPresent(ProductionScheduleDO::getScheduleNo, reqVO.getScheduleNo())
                .likeIfPresent(ProductionScheduleDO::getScheduleName, reqVO.getScheduleName())
                .eqIfPresent(ProductionScheduleDO::getScheduleType, reqVO.getScheduleType())
                .eqIfPresent(ProductionScheduleDO::getPlanningHorizonDays, reqVO.getPlanningHorizonDays())
                .betweenIfPresent(ProductionScheduleDO::getStartDate, reqVO.getStartDate())
                .betweenIfPresent(ProductionScheduleDO::getEndDate, reqVO.getEndDate())
                .eqIfPresent(ProductionScheduleDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ProductionScheduleDO::getTotalOrders, reqVO.getTotalOrders())
                .eqIfPresent(ProductionScheduleDO::getTotalQuantity, reqVO.getTotalQuantity())
                .eqIfPresent(ProductionScheduleDO::getTotalWorkHours, reqVO.getTotalWorkHours())
                .eqIfPresent(ProductionScheduleDO::getCreatedBy, reqVO.getCreatedBy())
                .betweenIfPresent(ProductionScheduleDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ProductionScheduleDO::getUpdatedBy, reqVO.getUpdatedBy())
                .orderByDesc(ProductionScheduleDO::getId));
    }

}