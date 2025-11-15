package cn.iocoder.yudao.module.erp.dal.mysql.productionorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo.*;

/**
 * ERP 生产订单 DO Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionOrderMapper extends BaseMapperX<ProductionOrderDO> {

    default PageResult<ProductionOrderDO> selectPage(ProductionOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionOrderDO>()
                .eqIfPresent(ProductionOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(ProductionOrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(ProductionOrderDO::getProductId, reqVO.getProductId())
                .likeIfPresent(ProductionOrderDO::getProductName, reqVO.getProductName())
                .eqIfPresent(ProductionOrderDO::getProductSpec, reqVO.getProductSpec())
                .eqIfPresent(ProductionOrderDO::getUnitId, reqVO.getUnitId())
                .eqIfPresent(ProductionOrderDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(ProductionOrderDO::getCompletedQuantity, reqVO.getCompletedQuantity())
                .betweenIfPresent(ProductionOrderDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(ProductionOrderDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(ProductionOrderDO::getActualStartTime, reqVO.getActualStartTime())
                .betweenIfPresent(ProductionOrderDO::getActualEndTime, reqVO.getActualEndTime())
                .eqIfPresent(ProductionOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ProductionOrderDO::getPriority, reqVO.getPriority())
                .eqIfPresent(ProductionOrderDO::getSourceType, reqVO.getSourceType())
                .eqIfPresent(ProductionOrderDO::getSourceId, reqVO.getSourceId())
                .eqIfPresent(ProductionOrderDO::getDescription, reqVO.getDescription())
                .eqIfPresent(ProductionOrderDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ProductionOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductionOrderDO::getId));
    }

    default ProductionOrderDO selectByNo(String no) {
        return selectOne(ProductionOrderDO::getNo, no);
    }

}