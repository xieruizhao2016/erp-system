package cn.iocoder.yudao.module.erp.dal.mysql.equipmentstatus;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipmentstatus.EquipmentStatusDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo.*;

/**
 * ERP 设备状态记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface EquipmentStatusMapper extends BaseMapperX<EquipmentStatusDO> {

    default PageResult<EquipmentStatusDO> selectPage(EquipmentStatusPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EquipmentStatusDO>()
                .eqIfPresent(EquipmentStatusDO::getEquipmentId, reqVO.getEquipmentId())
                .eqIfPresent(EquipmentStatusDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(EquipmentStatusDO::getStatusStartTime, reqVO.getStatusStartTime())
                .betweenIfPresent(EquipmentStatusDO::getStatusEndTime, reqVO.getStatusEndTime())
                .eqIfPresent(EquipmentStatusDO::getDuration, reqVO.getDuration())
                .eqIfPresent(EquipmentStatusDO::getWorkOrderId, reqVO.getWorkOrderId())
                .eqIfPresent(EquipmentStatusDO::getOperatorId, reqVO.getOperatorId())
                .eqIfPresent(EquipmentStatusDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(EquipmentStatusDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(EquipmentStatusDO::getId));
    }

}