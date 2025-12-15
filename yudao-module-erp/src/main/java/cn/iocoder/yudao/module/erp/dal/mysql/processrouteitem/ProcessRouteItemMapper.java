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
        LambdaQueryWrapperX<ProcessRouteItemDO> wrapper = new LambdaQueryWrapperX<>();
        if (reqVO.getRouteId() != null) wrapper.eq(ProcessRouteItemDO::getRouteId, reqVO.getRouteId());
        if (reqVO.getProcessId() != null) wrapper.eq(ProcessRouteItemDO::getProcessId, reqVO.getProcessId());
        if (reqVO.getSequence() != null) wrapper.eq(ProcessRouteItemDO::getSequence, reqVO.getSequence());
        if (reqVO.getOperationName() != null) wrapper.like(ProcessRouteItemDO::getOperationName, reqVO.getOperationName());
        if (reqVO.getStandardTime() != null && reqVO.getStandardTime().length == 2) {
            wrapper.between(ProcessRouteItemDO::getStandardTime, reqVO.getStandardTime()[0], reqVO.getStandardTime()[1]);
        }
        if (reqVO.getSetupTime() != null && reqVO.getSetupTime().length == 2) {
            wrapper.between(ProcessRouteItemDO::getSetupTime, reqVO.getSetupTime()[0], reqVO.getSetupTime()[1]);
        }
        if (reqVO.getWorkerCount() != null) wrapper.eq(ProcessRouteItemDO::getWorkerCount, reqVO.getWorkerCount());
        if (reqVO.getEquipmentId() != null) wrapper.eq(ProcessRouteItemDO::getEquipmentId, reqVO.getEquipmentId());
        if (reqVO.getWorkCenterId() != null) wrapper.eq(ProcessRouteItemDO::getWorkCenterId, reqVO.getWorkCenterId());
        if (reqVO.getLaborRate() != null) wrapper.eq(ProcessRouteItemDO::getLaborRate, reqVO.getLaborRate());
        if (reqVO.getOverheadRate() != null) wrapper.eq(ProcessRouteItemDO::getOverheadRate, reqVO.getOverheadRate());
        if (reqVO.getIsBottleneck() != null) wrapper.eq(ProcessRouteItemDO::getIsBottleneck, reqVO.getIsBottleneck());
        if (reqVO.getQualityCheckRequired() != null) wrapper.eq(ProcessRouteItemDO::getQualityCheckRequired, reqVO.getQualityCheckRequired());
        if (reqVO.getRemark() != null) wrapper.like(ProcessRouteItemDO::getRemark, reqVO.getRemark());
        if (reqVO.getCreateTime() != null && reqVO.getCreateTime().length == 2) {
            wrapper.between(ProcessRouteItemDO::getCreateTime, reqVO.getCreateTime()[0], reqVO.getCreateTime()[1]);
        }
        wrapper.orderByDesc(ProcessRouteItemDO::getId);
        return selectPage(reqVO, wrapper);
    }

    default List<ProcessRouteItemDO> selectListByRouteId(Long routeId) {
        return selectList(new LambdaQueryWrapperX<ProcessRouteItemDO>()
                .eq(ProcessRouteItemDO::getRouteId, routeId)
                .orderByAsc(ProcessRouteItemDO::getSequence));
    }

    default void deleteByRouteId(Long routeId) {
        delete(new LambdaQueryWrapperX<ProcessRouteItemDO>()
                .eq(ProcessRouteItemDO::getRouteId, routeId));
    }

}