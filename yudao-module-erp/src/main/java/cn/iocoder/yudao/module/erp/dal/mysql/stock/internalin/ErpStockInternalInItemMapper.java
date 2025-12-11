package cn.iocoder.yudao.module.erp.dal.mysql.stock.internalin;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * ERP 内部入库单项 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ErpStockInternalInItemMapper extends BaseMapperX<ErpStockInternalInItemDO> {

    default List<ErpStockInternalInItemDO> selectListByInId(Long inId) {
        return selectList(ErpStockInternalInItemDO::getInId, inId);
    }

    default List<ErpStockInternalInItemDO> selectListByInIds(Collection<Long> inIds) {
        return selectList(ErpStockInternalInItemDO::getInId, inIds);
    }

    default int deleteByInId(Long inId) {
        return delete(ErpStockInternalInItemDO::getInId, inId);
    }

}

