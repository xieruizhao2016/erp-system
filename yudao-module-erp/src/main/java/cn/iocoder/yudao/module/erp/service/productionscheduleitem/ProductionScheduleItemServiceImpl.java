package cn.iocoder.yudao.module.erp.service.productionscheduleitem;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionscheduleitem.ProductionScheduleItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionscheduleitem.ProductionScheduleItemMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 排程明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionScheduleItemServiceImpl implements ProductionScheduleItemService {

    @Resource
    private ProductionScheduleItemMapper productionScheduleItemMapper;

    @Override
    public Long createProductionScheduleItem(ProductionScheduleItemSaveReqVO createReqVO) {
        // 插入
        ProductionScheduleItemDO productionScheduleItem = BeanUtils.toBean(createReqVO, ProductionScheduleItemDO.class);
        productionScheduleItemMapper.insert(productionScheduleItem);

        // 返回
        return productionScheduleItem.getId();
    }

    @Override
    public void updateProductionScheduleItem(ProductionScheduleItemSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionScheduleItemExists(updateReqVO.getId());
        // 更新
        ProductionScheduleItemDO updateObj = BeanUtils.toBean(updateReqVO, ProductionScheduleItemDO.class);
        productionScheduleItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionScheduleItem(Long id) {
        // 校验存在
        validateProductionScheduleItemExists(id);
        // 删除
        productionScheduleItemMapper.deleteById(id);
    }

    @Override
        public void deleteProductionScheduleItemListByIds(List<Long> ids) {
        // 删除
        productionScheduleItemMapper.deleteByIds(ids);
        }


    private void validateProductionScheduleItemExists(Long id) {
        if (productionScheduleItemMapper.selectById(id) == null) {
            throw exception(PRODUCTION_SCHEDULE_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public ProductionScheduleItemDO getProductionScheduleItem(Long id) {
        return productionScheduleItemMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionScheduleItemDO> getProductionScheduleItemPage(ProductionScheduleItemPageReqVO pageReqVO) {
        return productionScheduleItemMapper.selectPage(pageReqVO);
    }

}