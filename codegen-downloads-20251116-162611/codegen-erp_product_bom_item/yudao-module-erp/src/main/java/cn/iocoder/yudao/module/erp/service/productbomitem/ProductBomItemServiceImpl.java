package cn.iocoder.yudao.module.erp.service.productbomitem;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productbomitem.ProductBomItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP BOM明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductBomItemServiceImpl implements ProductBomItemService {

    @Resource
    private ProductBomItemMapper productBomItemMapper;

    @Override
    public Long createProductBomItem(ProductBomItemSaveReqVO createReqVO) {
        // 插入
        ProductBomItemDO productBomItem = BeanUtils.toBean(createReqVO, ProductBomItemDO.class);
        productBomItemMapper.insert(productBomItem);

        // 返回
        return productBomItem.getId();
    }

    @Override
    public void updateProductBomItem(ProductBomItemSaveReqVO updateReqVO) {
        // 校验存在
        validateProductBomItemExists(updateReqVO.getId());
        // 更新
        ProductBomItemDO updateObj = BeanUtils.toBean(updateReqVO, ProductBomItemDO.class);
        productBomItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductBomItem(Long id) {
        // 校验存在
        validateProductBomItemExists(id);
        // 删除
        productBomItemMapper.deleteById(id);
    }

    @Override
        public void deleteProductBomItemListByIds(List<Long> ids) {
        // 删除
        productBomItemMapper.deleteByIds(ids);
        }


    private void validateProductBomItemExists(Long id) {
        if (productBomItemMapper.selectById(id) == null) {
            throw exception(PRODUCT_BOM_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ProductBomItemDO getProductBomItem(Long id) {
        return productBomItemMapper.selectById(id);
    }

    @Override
    public PageResult<ProductBomItemDO> getProductBomItemPage(ProductBomItemPageReqVO pageReqVO) {
        return productBomItemMapper.selectPage(pageReqVO);
    }

}