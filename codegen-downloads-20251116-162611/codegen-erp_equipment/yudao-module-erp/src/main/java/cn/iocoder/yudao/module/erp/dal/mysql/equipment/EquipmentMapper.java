package cn.iocoder.yudao.module.erp.dal.mysql.equipment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.equipment.vo.*;

/**
 * ERP 设备台账 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface EquipmentMapper extends BaseMapperX<EquipmentDO> {

    default PageResult<EquipmentDO> selectPage(EquipmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EquipmentDO>()
                .eqIfPresent(EquipmentDO::getEquipmentNo, reqVO.getEquipmentNo())
                .likeIfPresent(EquipmentDO::getEquipmentName, reqVO.getEquipmentName())
                .eqIfPresent(EquipmentDO::getEquipmentType, reqVO.getEquipmentType())
                .eqIfPresent(EquipmentDO::getModel, reqVO.getModel())
                .eqIfPresent(EquipmentDO::getManufacturer, reqVO.getManufacturer())
                .eqIfPresent(EquipmentDO::getSerialNumber, reqVO.getSerialNumber())
                .betweenIfPresent(EquipmentDO::getPurchaseDate, reqVO.getPurchaseDate())
                .eqIfPresent(EquipmentDO::getPurchasePrice, reqVO.getPurchasePrice())
                .eqIfPresent(EquipmentDO::getServiceLife, reqVO.getServiceLife())
                .eqIfPresent(EquipmentDO::getWorkCenterId, reqVO.getWorkCenterId())
                .eqIfPresent(EquipmentDO::getLocation, reqVO.getLocation())
                .eqIfPresent(EquipmentDO::getCapacity, reqVO.getCapacity())
                .eqIfPresent(EquipmentDO::getEfficiencyRate, reqVO.getEfficiencyRate())
                .eqIfPresent(EquipmentDO::getStatus, reqVO.getStatus())
                .eqIfPresent(EquipmentDO::getResponsiblePerson, reqVO.getResponsiblePerson())
                .eqIfPresent(EquipmentDO::getSpecification, reqVO.getSpecification())
                .eqIfPresent(EquipmentDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(EquipmentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(EquipmentDO::getId));
    }

}