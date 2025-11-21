package cn.iocoder.yudao.module.erp.service.productoem;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productoem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.iocoder.yudao.module.erp.dal.mysql.productoem.ProductOemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 产品OEM Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductOemServiceImpl implements ProductOemService {

    @Resource
    private ProductOemMapper productOemMapper;

    @Override
    public Long createProductOem(ProductOemSaveReqVO createReqVO) {
        // 插入
        ProductOemDO productOem = BeanUtils.toBean(createReqVO, ProductOemDO.class);
        productOemMapper.insert(productOem);

        // 返回
        return productOem.getId();
    }

    @Override
    public void updateProductOem(ProductOemSaveReqVO updateReqVO) {
        // 校验存在
        validateProductOemExists(updateReqVO.getId());
        // 更新
        ProductOemDO updateObj = BeanUtils.toBean(updateReqVO, ProductOemDO.class);
        productOemMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductOem(Long id) {
        // 校验存在
        validateProductOemExists(id);
        // 删除
        productOemMapper.deleteById(id);
    }

    @Override
    public void deleteProductOemListByIds(List<Long> ids) {
        // 删除
        productOemMapper.deleteByIds(ids);
    }

    private void validateProductOemExists(Long id) {
        if (productOemMapper.selectById(id) == null) {
            throw exception(PRODUCT_OEM_NOT_EXISTS);
        }
    }

    @Override
    public ProductOemDO getProductOem(Long id) {
        return productOemMapper.selectById(id);
    }

    @Override
    public PageResult<ProductOemDO> getProductOemPage(ProductOemPageReqVO pageReqVO) {
        return productOemMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductOemDO> getProductOemSimpleList() {
        return productOemMapper.selectList(
            new LambdaQueryWrapperX<ProductOemDO>()
                .eq(ProductOemDO::getStatus, 1) // 只返回启用的OEM
                .orderByAsc(ProductOemDO::getSort)
                .orderByDesc(ProductOemDO::getId)
        );
    }

    @Override
    public List<ProductOemDO> getProductOemList(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return productOemMapper.selectBatchIds(ids);
    }

}

