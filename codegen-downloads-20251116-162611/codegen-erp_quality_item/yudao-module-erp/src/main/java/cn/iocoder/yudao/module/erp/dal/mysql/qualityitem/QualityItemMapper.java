package cn.iocoder.yudao.module.erp.dal.mysql.qualityitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityitem.QualityItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo.*;

/**
 * ERP 质检项目 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface QualityItemMapper extends BaseMapperX<QualityItemDO> {

    default PageResult<QualityItemDO> selectPage(QualityItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<QualityItemDO>()
                .eqIfPresent(QualityItemDO::getStandardId, reqVO.getStandardId())
                .eqIfPresent(QualityItemDO::getItemNo, reqVO.getItemNo())
                .likeIfPresent(QualityItemDO::getItemName, reqVO.getItemName())
                .eqIfPresent(QualityItemDO::getItemType, reqVO.getItemType())
                .eqIfPresent(QualityItemDO::getTestMethod, reqVO.getTestMethod())
                .eqIfPresent(QualityItemDO::getStandardValue, reqVO.getStandardValue())
                .eqIfPresent(QualityItemDO::getTolerance, reqVO.getTolerance())
                .eqIfPresent(QualityItemDO::getUnit, reqVO.getUnit())
                .eqIfPresent(QualityItemDO::getIsKeyItem, reqVO.getIsKeyItem())
                .eqIfPresent(QualityItemDO::getSequence, reqVO.getSequence())
                .eqIfPresent(QualityItemDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(QualityItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(QualityItemDO::getId));
    }

}