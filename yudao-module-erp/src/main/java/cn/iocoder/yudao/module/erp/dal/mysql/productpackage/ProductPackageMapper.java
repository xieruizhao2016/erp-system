package cn.iocoder.yudao.module.erp.dal.mysql.productpackage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo.*;

/**
 * ERP 产品包装 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductPackageMapper extends BaseMapperX<ProductPackageDO> {

    default PageResult<ProductPackageDO> selectPage(ProductPackagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductPackageDO>()
                .likeIfPresent(ProductPackageDO::getPackageCode, reqVO.getPackageCode())
                .likeIfPresent(ProductPackageDO::getPackageName, reqVO.getPackageName())
                .eqIfPresent(ProductPackageDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductPackageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductPackageDO::getId));
    }

}

