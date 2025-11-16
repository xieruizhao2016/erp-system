package cn.iocoder.yudao.module.erp.service.productbom;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productbom.ProductBomMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP BOM主 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductBomServiceImpl implements ProductBomService {

    @Resource
    private ProductBomMapper productBomMapper;

    @Override
    public Long createProductBom(ProductBomSaveReqVO createReqVO) {
        // 插入
        ProductBomDO productBom = BeanUtils.toBean(createReqVO, ProductBomDO.class);
        productBomMapper.insert(productBom);

        // 返回
        return productBom.getId();
    }

    @Override
    public void updateProductBom(ProductBomSaveReqVO updateReqVO) {
        // 校验存在
        validateProductBomExists(updateReqVO.getId());
        // 更新
        ProductBomDO updateObj = BeanUtils.toBean(updateReqVO, ProductBomDO.class);
        productBomMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductBom(Long id) {
        // 校验存在
        validateProductBomExists(id);
        // 删除
        productBomMapper.deleteById(id);
    }

    @Override
        public void deleteProductBomListByIds(List<Long> ids) {
        // 删除
        productBomMapper.deleteByIds(ids);
        }


    private void validateProductBomExists(Long id) {
        if (productBomMapper.selectById(id) == null) {
            throw exception(PRODUCT_BOM_NOT_EXISTS);
        }
    }

    @Override
    public ProductBomDO getProductBom(Long id) {
        return productBomMapper.selectById(id);
    }

    @Override
    public PageResult<ProductBomDO> getProductBomPage(ProductBomPageReqVO pageReqVO) {
        return productBomMapper.selectPage(pageReqVO);
    }

}