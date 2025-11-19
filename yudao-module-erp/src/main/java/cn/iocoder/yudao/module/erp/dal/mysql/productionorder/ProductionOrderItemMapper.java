package cn.iocoder.yudao.module.erp.dal.mysql.productionorder;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * ERP 生产订单项 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductionOrderItemMapper extends BaseMapperX<ProductionOrderItemDO> {

    default List<ProductionOrderItemDO> selectListByOrderId(Long orderId) {
        return selectList(ProductionOrderItemDO::getProductionOrderId, orderId);
    }

    default List<ProductionOrderItemDO> selectListByOrderIds(Collection<Long> orderIds) {
        return selectList(ProductionOrderItemDO::getProductionOrderId, orderIds);
    }

    default int deleteByOrderId(Long orderId) {
        return delete(ProductionOrderItemDO::getProductionOrderId, orderId);
    }

}

