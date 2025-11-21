package cn.iocoder.yudao.module.erp.dal.mysql.productoem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productoem.vo.*;

/**
 * ERP 产品OEM Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductOemMapper extends BaseMapperX<ProductOemDO> {

    default PageResult<ProductOemDO> selectPage(ProductOemPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductOemDO>()
                .likeIfPresent(ProductOemDO::getOemCode, reqVO.getOemCode())
                .likeIfPresent(ProductOemDO::getOemName, reqVO.getOemName())
                .likeIfPresent(ProductOemDO::getFactoryName, reqVO.getFactoryName())
                .likeIfPresent(ProductOemDO::getFactoryCode, reqVO.getFactoryCode())
                .eqIfPresent(ProductOemDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductOemDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductOemDO::getId));
    }

}

