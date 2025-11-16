package cn.iocoder.yudao.module.erp.dal.mysql.productbom;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.*;

/**
 * ERP BOM主 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductBomMapper extends BaseMapperX<ProductBomDO> {

    default PageResult<ProductBomDO> selectPage(ProductBomPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductBomDO>()
                .eqIfPresent(ProductBomDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ProductBomDO::getBomNo, reqVO.getBomNo())
                .likeIfPresent(ProductBomDO::getBomName, reqVO.getBomName())
                .eqIfPresent(ProductBomDO::getVersion, reqVO.getVersion())
                .betweenIfPresent(ProductBomDO::getEffectiveDate, reqVO.getEffectiveDate())
                .betweenIfPresent(ProductBomDO::getExpireDate, reqVO.getExpireDate())
                .eqIfPresent(ProductBomDO::getBomType, reqVO.getBomType())
                .eqIfPresent(ProductBomDO::getStandardCost, reqVO.getStandardCost())
                .eqIfPresent(ProductBomDO::getTotalMaterialCost, reqVO.getTotalMaterialCost())
                .eqIfPresent(ProductBomDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductBomDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductBomDO::getId));
    }

}