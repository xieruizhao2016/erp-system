package cn.iocoder.yudao.module.erp.dal.mysql.workorderprogress;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.*;

/**
 * ERP 工单进度 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WorkOrderProgressMapper extends BaseMapperX<WorkOrderProgressDO> {

    default PageResult<WorkOrderProgressDO> selectPage(WorkOrderProgressPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkOrderProgressDO>()
                .eqIfPresent(WorkOrderProgressDO::getWorkOrderId, reqVO.getWorkOrderId())
                .eqIfPresent(WorkOrderProgressDO::getProcessId, reqVO.getProcessId())
                .likeIfPresent(WorkOrderProgressDO::getProcessName, reqVO.getProcessName())
                .eqIfPresent(WorkOrderProgressDO::getSequence, reqVO.getSequence())
                .betweenIfPresent(WorkOrderProgressDO::getPlannedStartTime, reqVO.getPlannedStartTime())
                .betweenIfPresent(WorkOrderProgressDO::getPlannedEndTime, reqVO.getPlannedEndTime())
                .betweenIfPresent(WorkOrderProgressDO::getActualStartTime, reqVO.getActualStartTime())
                .betweenIfPresent(WorkOrderProgressDO::getActualEndTime, reqVO.getActualEndTime())
                .eqIfPresent(WorkOrderProgressDO::getPlannedQuantity, reqVO.getPlannedQuantity())
                .eqIfPresent(WorkOrderProgressDO::getCompletedQuantity, reqVO.getCompletedQuantity())
                .eqIfPresent(WorkOrderProgressDO::getQualifiedQuantity, reqVO.getQualifiedQuantity())
                .eqIfPresent(WorkOrderProgressDO::getRejectedQuantity, reqVO.getRejectedQuantity())
                .eqIfPresent(WorkOrderProgressDO::getScrapQuantity, reqVO.getScrapQuantity())
                .eqIfPresent(WorkOrderProgressDO::getStatus, reqVO.getStatus())
                .eqIfPresent(WorkOrderProgressDO::getOperatorId, reqVO.getOperatorId())
                .eqIfPresent(WorkOrderProgressDO::getEquipmentId, reqVO.getEquipmentId())
                .betweenIfPresent(WorkOrderProgressDO::getWorkTime, reqVO.getWorkTime())
                .betweenIfPresent(WorkOrderProgressDO::getDowntime, reqVO.getDowntime())
                .eqIfPresent(WorkOrderProgressDO::getQualityStatus, reqVO.getQualityStatus())
                .eqIfPresent(WorkOrderProgressDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(WorkOrderProgressDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkOrderProgressDO::getId));
    }

}