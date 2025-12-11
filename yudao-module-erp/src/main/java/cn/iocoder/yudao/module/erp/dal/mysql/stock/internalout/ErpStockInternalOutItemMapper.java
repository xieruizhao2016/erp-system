package cn.iocoder.yudao.module.erp.dal.mysql.stock.internalout;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalout.ErpStockInternalOutItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * ERP 内部出库单项 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpStockInternalOutItemMapper extends BaseMapperX<ErpStockInternalOutItemDO> {

    default List<ErpStockInternalOutItemDO> selectListByOutId(Long outId) {
        return selectList(ErpStockInternalOutItemDO::getOutId, outId);
    }

    default List<ErpStockInternalOutItemDO> selectListByOutIds(Collection<Long> outIds) {
        return selectList(ErpStockInternalOutItemDO::getOutId, outIds);
    }

    default int deleteByOutId(Long outId) {
        return delete(ErpStockInternalOutItemDO::getOutId, outId);
    }

}

