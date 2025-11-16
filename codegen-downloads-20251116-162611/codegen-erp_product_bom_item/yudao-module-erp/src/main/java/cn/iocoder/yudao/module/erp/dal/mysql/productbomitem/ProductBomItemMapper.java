package cn.iocoder.yudao.module.erp.dal.mysql.productbomitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.*;

/**
 * ERP BOM明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductBomItemMapper extends BaseMapperX<ProductBomItemDO> {

    default PageResult<ProductBomItemDO> selectPage(ProductBomItemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductBomItemDO>()
                .eqIfPresent(ProductBomItemDO::getBomId, reqVO.getBomId())
                .eqIfPresent(ProductBomItemDO::getParentProductId, reqVO.getParentProductId())
                .eqIfPresent(ProductBomItemDO::getChildProductId, reqVO.getChildProductId())
                .likeIfPresent(ProductBomItemDO::getChildProductName, reqVO.getChildProductName())
                .eqIfPresent(ProductBomItemDO::getChildProductSpec, reqVO.getChildProductSpec())
                .eqIfPresent(ProductBomItemDO::getUnitId, reqVO.getUnitId())
                .eqIfPresent(ProductBomItemDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(ProductBomItemDO::getLossRate, reqVO.getLossRate())
                .eqIfPresent(ProductBomItemDO::getEffectiveQuantity, reqVO.getEffectiveQuantity())
                .eqIfPresent(ProductBomItemDO::getIsKeyMaterial, reqVO.getIsKeyMaterial())
                .eqIfPresent(ProductBomItemDO::getIsAlternative, reqVO.getIsAlternative())
                .eqIfPresent(ProductBomItemDO::getAlternativeGroup, reqVO.getAlternativeGroup())
                .eqIfPresent(ProductBomItemDO::getPosition, reqVO.getPosition())
                .eqIfPresent(ProductBomItemDO::getProcessId, reqVO.getProcessId())
                .eqIfPresent(ProductBomItemDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ProductBomItemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductBomItemDO::getId));
    }

}