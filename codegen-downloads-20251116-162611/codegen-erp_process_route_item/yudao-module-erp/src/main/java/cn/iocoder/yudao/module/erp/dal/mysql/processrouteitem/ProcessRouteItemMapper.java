package cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.*;

/**
 * ERP 工艺路线明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProcessRouteItemMapper extends BaseMapperX<ProcessRouteItemDO> {

    default PageResult<ProcessRouteItemDO> selectPage(ProcessRouteItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProcessRouteItemDO>()
                .eqIfPresent(ProcessRouteItemDO::getRouteId, reqVO.getRouteId())
                .eqIfPresent(ProcessRouteItemDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(ProcessRouteItemDO::getSequence, reqVO.getSequence())
                .likeIfPresent(ProcessRouteItemDO::getOperationName, reqVO.getOperationName())
                .betweenIfPresent(ProcessRouteItemDO::getStandardTime, reqVO.getStandardTime())
                .betweenIfPresent(ProcessRouteItemDO::getSetupTime, reqVO.getSetupTime())
                .eqIfPresent(ProcessRouteItemDO::getWorkerCount, reqVO.getWorkerCount())
                .eqIfPresent(ProcessRouteItemDO::getEquipmentId, reqVO.getEquipmentId())
                .eqIfPresent(ProcessRouteItemDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(ProcessRouteItemDO::getLaborRate, reqVO.getLaborRate())
                .eqIfPresent(ProcessRouteItemDO::getOverheadRate, reqVO.getOverheadRate())
                .eqIfPresent(ProcessRouteItemDO::getIsBottleneck, reqVO.getIsBottleneck())
                .eqIfPresent(ProcessRouteItemDO::getQualityCheckRequired, reqVO.getQualityCheckRequired())
                .eqIfPresent(ProcessRouteItemDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ProcessRouteItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProcessRouteItemDO::getId));
    }

}