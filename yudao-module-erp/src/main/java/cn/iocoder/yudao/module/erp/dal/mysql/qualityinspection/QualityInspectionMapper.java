package cn.iocoder.yudao.module.erp.dal.mysql.qualityinspection;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspection.QualityInspectionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspection.vo.*;

/**
 * ERP 质检记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QualityInspectionMapper extends BaseMapperX<QualityInspectionDO> {

    default PageResult<QualityInspectionDO> selectPage(QualityInspectionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QualityInspectionDO>()
                .eqIfPresent(QualityInspectionDO::getInspectionNo, reqVO.getInspectionNo())
                .eqIfPresent(QualityInspectionDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(QualityInspectionDO::getProductId, reqVO.getProductId())
                .eqIfPresent(QualityInspectionDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(QualityInspectionDO::getWorkOrderId, reqVO.getWorkOrderId())
                .eqIfPresent(QualityInspectionDO::getInspectionType, reqVO.getInspectionType())
                .eqIfPresent(QualityInspectionDO::getInspectionLevel, reqVO.getInspectionLevel())
                .eqIfPresent(QualityInspectionDO::getLotSize, reqVO.getLotSize())
                .eqIfPresent(QualityInspectionDO::getSampleSize, reqVO.getSampleSize())
                .eqIfPresent(QualityInspectionDO::getQualifiedQuantity, reqVO.getQualifiedQuantity())
                .eqIfPresent(QualityInspectionDO::getRejectedQuantity, reqVO.getRejectedQuantity())
                .eqIfPresent(QualityInspectionDO::getScrapQuantity, reqVO.getScrapQuantity())
                .eqIfPresent(QualityInspectionDO::getInspectionResult, reqVO.getInspectionResult())
                .eqIfPresent(QualityInspectionDO::getInspectorId, reqVO.getInspectorId())
                .betweenIfPresent(QualityInspectionDO::getInspectionTime, reqVO.getInspectionTime())
                .eqIfPresent(QualityInspectionDO::getEnvironment, reqVO.getEnvironment())
                .eqIfPresent(QualityInspectionDO::getEquipment, reqVO.getEquipment())
                .eqIfPresent(QualityInspectionDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(QualityInspectionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QualityInspectionDO::getId));
    }

    default QualityInspectionDO selectByInspectionNo(String inspectionNo) {
        return selectOne(QualityInspectionDO::getInspectionNo, inspectionNo);
    }

}