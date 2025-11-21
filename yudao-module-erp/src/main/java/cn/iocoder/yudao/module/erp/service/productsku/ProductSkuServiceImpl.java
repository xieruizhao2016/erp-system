package cn.iocoder.yudao.module.erp.service.productsku;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productsku.ProductSkuMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapperX;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
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

    @Override
    public Long createProductSku(ProductSkuSaveReqVO createReqVO) {
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
        return productSkuMapper.selectList(
            new LambdaQueryWrapperX<ProductSkuDO>()
                .eq(ProductSkuDO::getProductId, productId)
                .eq(ProductSkuDO::getStatus, 1) // 只返回启用的SKU
                .orderByAsc(ProductSkuDO::getSort)
                .orderByDesc(ProductSkuDO::getId)
        );
    }

}