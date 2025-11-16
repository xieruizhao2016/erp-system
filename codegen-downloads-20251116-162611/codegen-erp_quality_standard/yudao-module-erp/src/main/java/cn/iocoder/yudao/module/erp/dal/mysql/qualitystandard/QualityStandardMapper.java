package cn.iocoder.yudao.module.erp.dal.mysql.qualitystandard;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualitystandard.QualityStandardDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo.*;

/**
 * ERP 质检标准 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QualityStandardMapper extends BaseMapperX<QualityStandardDO> {

    default PageResult<QualityStandardDO> selectPage(QualityStandardPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QualityStandardDO>()
                .eqIfPresent(QualityStandardDO::getStandardNo, reqVO.getStandardNo())
                .likeIfPresent(QualityStandardDO::getStandardName, reqVO.getStandardName())
                .eqIfPresent(QualityStandardDO::getProductId, reqVO.getProductId())
                .eqIfPresent(QualityStandardDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(QualityStandardDO::getInspectionType, reqVO.getInspectionType())
                .eqIfPresent(QualityStandardDO::getStandardVersion, reqVO.getStandardVersion())
                .eqIfPresent(QualityStandardDO::getAqlLevel, reqVO.getAqlLevel())
                .eqIfPresent(QualityStandardDO::getSamplingMethod, reqVO.getSamplingMethod())
                .eqIfPresent(QualityStandardDO::getStatus, reqVO.getStatus())
                .eqIfPresent(QualityStandardDO::getDescription, reqVO.getDescription())
                .betweenIfPresent(QualityStandardDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QualityStandardDO::getId));
    }

}