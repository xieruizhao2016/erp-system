package cn.iocoder.yudao.module.erp.service.productsku;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuRelationDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productsku.ProductSkuMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.productsku.ProductSkuRelationMapper;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 产品SKU Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductSkuServiceImpl implements ProductSkuService {

    @Resource
    private ProductSkuMapper productSkuMapper;

    @Resource
    private ProductSkuRelationMapper productSkuRelationMapper;

    @Override
    public Long createProductSku(ProductSkuSaveReqVO createReqVO) {
        // 校验SKU编码唯一性
        validateSkuCodeUnique(createReqVO.getSkuCode(), null);
        // 插入
        ProductSkuDO productSku = BeanUtils.toBean(createReqVO, ProductSkuDO.class);
        productSkuMapper.insert(productSku);

        // 返回
        return productSku.getId();
    }

    @Override
    public void updateProductSku(ProductSkuSaveReqVO updateReqVO) {
        // 校验存在
        validateProductSkuExists(updateReqVO.getId());
        // 校验SKU编码唯一性（排除自己）
        validateSkuCodeUnique(updateReqVO.getSkuCode(), updateReqVO.getId());
        // 更新
        ProductSkuDO updateObj = BeanUtils.toBean(updateReqVO, ProductSkuDO.class);
        productSkuMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductSku(Long id) {
        // 校验存在
        validateProductSkuExists(id);
        // 删除
        productSkuMapper.deleteById(id);
    }

    @Override
        public void deleteProductSkuListByIds(List<Long> ids) {
        // 删除
        productSkuMapper.deleteByIds(ids);
        }


    private void validateProductSkuExists(Long id) {
        if (productSkuMapper.selectById(id) == null) {
            throw exception(PRODUCT_SKU_NOT_EXISTS);
        }
    }

    private void validateSkuCodeUnique(String skuCode, Long excludeId) {
        ProductSkuDO existing = productSkuMapper.selectBySkuCode(skuCode);
        if (existing != null && (excludeId == null || !existing.getId().equals(excludeId))) {
            throw exception(PRODUCT_SKU_CODE_DUPLICATE, skuCode);
        }
    }

    @Override
    public ProductSkuDO getProductSku(Long id) {
        return productSkuMapper.selectById(id);
    }

    @Override
    public PageResult<ProductSkuDO> getProductSkuPage(ProductSkuPageReqVO pageReqVO) {
        return productSkuMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductSkuDO> getProductSkuSimpleList() {
        return productSkuMapper.selectList();
    }

    @Override
    public List<ProductSkuDO> getProductSkuList(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return productSkuMapper.selectBatchIds(ids);
    }

    @Override
    public List<ProductSkuDO> getProductSkuListByProductId(Long productId) {
        if (productId == null) {
            return Collections.emptyList();
        }
        // 通过关联表查询SKU列表
        List<ProductSkuRelationDO> relations = productSkuRelationMapper.selectListByProductId(productId);
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyList();
        }
        Set<Long> skuIdsSet = convertSet(relations, ProductSkuRelationDO::getSkuId);
        List<Long> skuIds = new java.util.ArrayList<>(skuIdsSet);
        List<ProductSkuDO> skuList = productSkuMapper.selectBatchIds(skuIds);
        // 过滤启用的SKU并排序（CommonStatusEnum.ENABLE = 0）
        return skuList.stream()
                .filter(sku -> sku.getStatus() != null && sku.getStatus() == 0)
                .sorted(Comparator.comparing(ProductSkuDO::getSort, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(ProductSkuDO::getId, Comparator.reverseOrder()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<ProductSkuDO> getProductSkuListByProductIdAll(Long productId) {
        if (productId == null) {
            return Collections.emptyList();
        }
        // 通过关联表查询SKU列表（包含所有状态）
        List<ProductSkuRelationDO> relations = productSkuRelationMapper.selectListByProductId(productId);
        if (CollUtil.isEmpty(relations)) {
            return Collections.emptyList();
        }
        Set<Long> skuIdsSet = convertSet(relations, ProductSkuRelationDO::getSkuId);
        List<Long> skuIds = new java.util.ArrayList<>(skuIdsSet);
        List<ProductSkuDO> skuList = productSkuMapper.selectBatchIds(skuIds);
        // 排序（不过滤状态）
        return skuList.stream()
                .sorted(Comparator.comparing(ProductSkuDO::getSort, Comparator.nullsLast(Integer::compareTo))
                        .thenComparing(ProductSkuDO::getId, Comparator.reverseOrder()))
                .collect(java.util.stream.Collectors.toList());
    }

}