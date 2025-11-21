package cn.iocoder.yudao.module.erp.service.productpackage;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;

import cn.iocoder.yudao.module.erp.dal.mysql.productpackage.ProductPackageMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 产品包装 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductPackageServiceImpl implements ProductPackageService {

    @Resource
    private ProductPackageMapper productPackageMapper;

    @Override
    public Long createProductPackage(ProductPackageSaveReqVO createReqVO) {
        // 插入
        ProductPackageDO productPackage = BeanUtils.toBean(createReqVO, ProductPackageDO.class);
        productPackageMapper.insert(productPackage);

        // 返回
        return productPackage.getId();
    }

    @Override
    public void updateProductPackage(ProductPackageSaveReqVO updateReqVO) {
        // 校验存在
        validateProductPackageExists(updateReqVO.getId());
        // 更新
        ProductPackageDO updateObj = BeanUtils.toBean(updateReqVO, ProductPackageDO.class);
        productPackageMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductPackage(Long id) {
        // 校验存在
        validateProductPackageExists(id);
        // 删除
        productPackageMapper.deleteById(id);
    }

    @Override
    public void deleteProductPackageListByIds(List<Long> ids) {
        // 删除
        productPackageMapper.deleteByIds(ids);
    }

    private void validateProductPackageExists(Long id) {
        if (productPackageMapper.selectById(id) == null) {
            throw exception(PRODUCT_PACKAGE_NOT_EXISTS);
        }
    }

    @Override
    public ProductPackageDO getProductPackage(Long id) {
        return productPackageMapper.selectById(id);
    }

    @Override
    public PageResult<ProductPackageDO> getProductPackagePage(ProductPackagePageReqVO pageReqVO) {
        return productPackageMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ProductPackageDO> getProductPackageSimpleList() {
        return productPackageMapper.selectList(
            new LambdaQueryWrapperX<ProductPackageDO>()
                .eq(ProductPackageDO::getStatus, 0) // 只返回开启的包装（0=开启，1=关闭）
                .orderByAsc(ProductPackageDO::getSort)
                .orderByDesc(ProductPackageDO::getId)
        );
    }

    @Override
    public List<ProductPackageDO> getProductPackageList(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return productPackageMapper.selectBatchIds(ids);
    }

}

