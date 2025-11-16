package cn.iocoder.yudao.module.erp.dal.mysql.workhours;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.workhours.WorkHoursDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.workhours.vo.*;

/**
 * ERP 工时统计 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WorkHoursMapper extends BaseMapperX<WorkHoursDO> {

    default PageResult<WorkHoursDO> selectPage(WorkHoursPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkHoursDO>()
                .eqIfPresent(WorkHoursDO::getWorkOrderId, reqVO.getWorkOrderId())
                .eqIfPresent(WorkHoursDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(WorkHoursDO::getOperatorId, reqVO.getOperatorId())
                .betweenIfPresent(WorkHoursDO::getWorkDate, reqVO.getWorkDate())
                .eqIfPresent(WorkHoursDO::getShiftId, reqVO.getShiftId())
                .betweenIfPresent(WorkHoursDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(WorkHoursDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(WorkHoursDO::getDuration, reqVO.getDuration())
                .eqIfPresent(WorkHoursDO::getStandardDuration, reqVO.getStandardDuration())
                .eqIfPresent(WorkHoursDO::getOvertimeDuration, reqVO.getOvertimeDuration())
                .eqIfPresent(WorkHoursDO::getMachineHours, reqVO.getMachineHours())
                .eqIfPresent(WorkHoursDO::getOperatorRate, reqVO.getOperatorRate())
                .eqIfPresent(WorkHoursDO::getMachineRate, reqVO.getMachineRate())
                .eqIfPresent(WorkHoursDO::getLaborCost, reqVO.getLaborCost())
                .eqIfPresent(WorkHoursDO::getMachineCost, reqVO.getMachineCost())
                .eqIfPresent(WorkHoursDO::getStatus, reqVO.getStatus())
                .eqIfPresent(WorkHoursDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(WorkHoursDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkHoursDO::getId));
    }

}