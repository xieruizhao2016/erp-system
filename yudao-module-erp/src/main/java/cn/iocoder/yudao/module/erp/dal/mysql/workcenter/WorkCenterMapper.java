package cn.iocoder.yudao.module.erp.dal.mysql.workcenter;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.workcenter.WorkCenterDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo.*;

/**
 * ERP 工作中心 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface WorkCenterMapper extends BaseMapperX<WorkCenterDO> {

    default PageResult<WorkCenterDO> selectPage(WorkCenterPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkCenterDO>()
                .eqIfPresent(WorkCenterDO::getWorkCenterNo, reqVO.getWorkCenterNo())
                .likeIfPresent(WorkCenterDO::getWorkCenterName, reqVO.getWorkCenterName())
                .eqIfPresent(WorkCenterDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WorkCenterDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkCenterDO::getId));
    }

    default WorkCenterDO selectByWorkCenterNo(String workCenterNo) {
        return selectOne(WorkCenterDO::getWorkCenterNo, workCenterNo);
    }

}

