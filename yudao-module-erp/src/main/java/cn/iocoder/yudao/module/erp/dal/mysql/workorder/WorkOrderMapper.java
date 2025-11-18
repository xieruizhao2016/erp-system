package cn.iocoder.yudao.module.erp.dal.mysql.workorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.workorder.vo.*;

/**
 * ERP 工单主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WorkOrderMapper extends BaseMapperX<WorkOrderDO> {

    default PageResult<WorkOrderDO> selectPage(WorkOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkOrderDO>()
                .eqIfPresent(WorkOrderDO::getWorkOrderNo, reqVO.getWorkOrderNo())
                .eqIfPresent(WorkOrderDO::getProductionOrderId, reqVO.getProductionOrderId())
                .eqIfPresent(WorkOrderDO::getProductId, reqVO.getProductId())
                .likeIfPresent(WorkOrderDO::getProductName, reqVO.getProductName())
                .eqIfPresent(WorkOrderDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(WorkOrderDO::getCompletedQuantity, reqVO.getCompletedQuantity())
                .eqIfPresent(WorkOrderDO::getQualifiedQuantity, reqVO.getQualifiedQuantity())
                .eqIfPresent(WorkOrderDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(WorkOrderDO::getSupervisorId, reqVO.getSupervisorId())
                .betweenIfPresent(WorkOrderDO::getPlannedStartTime, reqVO.getPlannedStartTime())
                .betweenIfPresent(WorkOrderDO::getPlannedEndTime, reqVO.getPlannedEndTime())
                .betweenIfPresent(WorkOrderDO::getActualStartTime, reqVO.getActualStartTime())
                .betweenIfPresent(WorkOrderDO::getActualEndTime, reqVO.getActualEndTime())
                .eqIfPresent(WorkOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(WorkOrderDO::getPriority, reqVO.getPriority())
                .eqIfPresent(WorkOrderDO::getInstruction, reqVO.getInstruction())
                .eqIfPresent(WorkOrderDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(WorkOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkOrderDO::getId));
    }

    default WorkOrderDO selectByWorkOrderNo(String workOrderNo) {
        return selectOne(WorkOrderDO::getWorkOrderNo, workOrderNo);
    }

}