package cn.iocoder.yudao.module.erp.dal.mysql.productsku;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.*;

/**
 * ERP 产品SKU Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductSkuMapper extends BaseMapperX<ProductSkuDO> {

    default PageResult<ProductSkuDO> selectPage(ProductSkuPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductSkuDO>()
                .eqIfPresent(ProductSkuDO::getSkuCode, reqVO.getSkuCode())
                .likeIfPresent(ProductSkuDO::getSkuName, reqVO.getSkuName())
                .eqIfPresent(ProductSkuDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ProductSkuDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ProductSkuDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(ProductSkuDO::getImageUrl, reqVO.getImageUrl())
                .eqIfPresent(ProductSkuDO::getSpecification, reqVO.getSpecification())
                .eqIfPresent(ProductSkuDO::getAttributes, reqVO.getAttributes())
                .eqIfPresent(ProductSkuDO::getPurchasePrice, reqVO.getPurchasePrice())
                .eqIfPresent(ProductSkuDO::getSalePrice, reqVO.getSalePrice())
                .eqIfPresent(ProductSkuDO::getMinPrice, reqVO.getMinPrice())
                .eqIfPresent(ProductSkuDO::getCostPrice, reqVO.getCostPrice())
                .eqIfPresent(ProductSkuDO::getWeight, reqVO.getWeight())
                .eqIfPresent(ProductSkuDO::getVolume, reqVO.getVolume())
                .eqIfPresent(ProductSkuDO::getSort, reqVO.getSort())
                .eqIfPresent(ProductSkuDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ProductSkuDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductSkuDO::getId));
    }

}