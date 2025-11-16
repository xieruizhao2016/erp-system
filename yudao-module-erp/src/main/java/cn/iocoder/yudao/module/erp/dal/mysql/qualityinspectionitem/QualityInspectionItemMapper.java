package cn.iocoder.yudao.module.erp.dal.mysql.qualityinspectionitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspectionitem.QualityInspectionItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo.*;

/**
 * ERP 质检明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QualityInspectionItemMapper extends BaseMapperX<QualityInspectionItemDO> {

    default PageResult<QualityInspectionItemDO> selectPage(QualityInspectionItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QualityInspectionItemDO>()
                .eqIfPresent(QualityInspectionItemDO::getInspectionId, reqVO.getInspectionId())
                .eqIfPresent(QualityInspectionItemDO::getItemId, reqVO.getItemId())
                .eqIfPresent(QualityInspectionItemDO::getSampleNo, reqVO.getSampleNo())
                .eqIfPresent(QualityInspectionItemDO::getMeasuredValue, reqVO.getMeasuredValue())
                .eqIfPresent(QualityInspectionItemDO::getActualValue, reqVO.getActualValue())
                .eqIfPresent(QualityInspectionItemDO::getTestResult, reqVO.getTestResult())
                .eqIfPresent(QualityInspectionItemDO::getDefectType, reqVO.getDefectType())
                .eqIfPresent(QualityInspectionItemDO::getDefectDescription, reqVO.getDefectDescription())
                .eqIfPresent(QualityInspectionItemDO::getImageUrls, reqVO.getImageUrls())
                .eqIfPresent(QualityInspectionItemDO::getInspectorId, reqVO.getInspectorId())
                .betweenIfPresent(QualityInspectionItemDO::getInspectionTime, reqVO.getInspectionTime())
                .eqIfPresent(QualityInspectionItemDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(QualityInspectionItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QualityInspectionItemDO::getId));
    }

}