package cn.iocoder.yudao.module.erp.dal.mysql.productsku;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuRelationDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * ERP 产品和SKU关联 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductSkuRelationMapper extends BaseMapperX<ProductSkuRelationDO> {

    /**
     * 根据产品ID查询关联的SKU列表
     *
     * @param productId 产品ID
     * @return SKU关联列表
     */
    default List<ProductSkuRelationDO> selectListByProductId(Long productId) {
        return selectList(ProductSkuRelationDO::getProductId, productId);
    }

    /**
     * 根据产品ID列表查询关联的SKU列表
     *
     * @param productIds 产品ID列表
     * @return SKU关联列表
     */
    default List<ProductSkuRelationDO> selectListByProductIds(Collection<Long> productIds) {
        return selectList(ProductSkuRelationDO::getProductId, productIds);
    }

    /**
     * 根据SKU ID查询关联的产品列表
     *
     * @param skuId SKU ID
     * @return 产品关联列表
     */
    default List<ProductSkuRelationDO> selectListBySkuId(Long skuId) {
        return selectList(ProductSkuRelationDO::getSkuId, skuId);
    }

    /**
     * 根据SKU ID列表查询关联的产品列表
     *
     * @param skuIds SKU ID列表
     * @return 产品关联列表
     */
    default List<ProductSkuRelationDO> selectListBySkuIds(Collection<Long> skuIds) {
        return selectList(ProductSkuRelationDO::getSkuId, skuIds);
    }

    /**
     * 根据产品ID删除关联关系
     *
     * @param productId 产品ID
     * @return 删除数量
     */
    default int deleteByProductId(Long productId) {
        return delete(ProductSkuRelationDO::getProductId, productId);
    }

    /**
     * 根据SKU ID删除关联关系
     *
     * @param skuId SKU ID
     * @return 删除数量
     */
    default int deleteBySkuId(Long skuId) {
        return delete(ProductSkuRelationDO::getSkuId, skuId);
    }

    /**
     * 删除产品和SKU的关联关系
     *
     * @param productId 产品ID
     * @param skuId SKU ID
     * @return 删除数量
     */
    default int deleteByProductIdAndSkuId(Long productId, Long skuId) {
        return delete(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<ProductSkuRelationDO>()
                .eq(ProductSkuRelationDO::getProductId, productId)
                .eq(ProductSkuRelationDO::getSkuId, skuId));
    }

}

