package cn.iocoder.yudao.module.erp.service.productionorder;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderItemDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderItemMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP 生产订单 DO
 * Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductionOrderServiceImpl implements ProductionOrderService {

    @Resource
    private ProductionOrderMapper productionOrderMapper;

    @Resource
    private ProductionOrderItemMapper productionOrderItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    private ErpProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createProductionOrder(ProductionOrderSaveReqVO createReqVO) {
        // 1.1 校验订单项的有效性
        List<ProductionOrderItemDO> orderItems = validateProductionOrderItems(createReqVO.getItems());
        // 1.2 生成生产订单号，并校验唯一性
        String no = noRedisDAO.generate(ErpNoRedisDAO.PRODUCTION_ORDER_NO_PREFIX);
        if (productionOrderMapper.selectByNo(no) != null) {
            throw exception(PRODUCTION_ORDER_NO_EXISTS);
        }

        // 2.1 插入订单（兼容旧数据：如果 items 为空，使用单个产品字段）
        ProductionOrderDO productionOrder = BeanUtils.toBean(createReqVO, ProductionOrderDO.class);
        productionOrder.setNo(no);
        // 如果 items 不为空，使用第一个产品作为主表字段（兼容旧数据）
        if (CollUtil.isNotEmpty(orderItems)) {
            ProductionOrderItemDO firstItem = orderItems.get(0);
            productionOrder.setProductId(firstItem.getProductId());
            productionOrder.setProductName(firstItem.getProductName());
            productionOrder.setProductSpec(firstItem.getProductSpec());
            productionOrder.setUnitId(firstItem.getUnitId());
            productionOrder.setQuantity(firstItem.getQuantity());
        }
        productionOrderMapper.insert(productionOrder);
        
        // 2.2 插入订单项
        if (CollUtil.isNotEmpty(orderItems)) {
            orderItems.forEach(item -> item.setProductionOrderId(productionOrder.getId()));
            productionOrderItemMapper.insertBatch(orderItems);
        }

        // 返回
        return productionOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductionOrder(ProductionOrderSaveReqVO updateReqVO) {
        // 1.1 校验存在，并获取现有订单（用于保持订单号不变）
        ProductionOrderDO existingOrder = productionOrderMapper.selectById(updateReqVO.getId());
        if (existingOrder == null) {
            throw exception(PRODUCTION_ORDER_NOT_EXISTS);
        }
        // 1.2 校验订单项的有效性
        List<ProductionOrderItemDO> orderItems = validateProductionOrderItems(updateReqVO.getItems());
        
        // 2.1 更新订单
        ProductionOrderDO updateObj = BeanUtils.toBean(updateReqVO, ProductionOrderDO.class);
        // 保持订单号不变（订单号创建后不允许修改）
        updateObj.setNo(existingOrder.getNo());
        // 如果 items 不为空，使用第一个产品作为主表字段（兼容旧数据）
        if (CollUtil.isNotEmpty(orderItems)) {
            ProductionOrderItemDO firstItem = orderItems.get(0);
            updateObj.setProductId(firstItem.getProductId());
            updateObj.setProductName(firstItem.getProductName());
            updateObj.setProductSpec(firstItem.getProductSpec());
            updateObj.setUnitId(firstItem.getUnitId());
            updateObj.setQuantity(firstItem.getQuantity());
        }
        productionOrderMapper.updateById(updateObj);
        
        // 2.2 更新订单项
        updateProductionOrderItems(updateReqVO.getId(), orderItems);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductionOrder(Long id) {
        // 校验存在
        validateProductionOrderExists(id);
        // 删除订单项
        productionOrderItemMapper.deleteByOrderId(id);
        // 删除订单
        productionOrderMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductionOrderListByIds(List<Long> ids) {
        // 删除订单项
        ids.forEach(productionOrderItemMapper::deleteByOrderId);
        // 删除订单
        productionOrderMapper.deleteByIds(ids);
    }


    private void validateProductionOrderExists(Long id) {
        if (productionOrderMapper.selectById(id) == null) {
            throw exception(PRODUCTION_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public ProductionOrderDO getProductionOrder(Long id) {
        return productionOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionOrderDO> getProductionOrderPage(ProductionOrderPageReqVO pageReqVO) {
        return productionOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateProductionOrderStatus(Long id, Integer status) {
        // 校验存在
        validateProductionOrderExists(id);
        // 更新状态
        ProductionOrderDO updateObj = new ProductionOrderDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        productionOrderMapper.updateById(updateObj);
    }

    @Override
    public List<ProductionOrderItemDO> getProductionOrderItemListByOrderId(Long orderId) {
        return productionOrderItemMapper.selectListByOrderId(orderId);
    }

    /**
     * 校验生产订单项的有效性
     *
     * @param items 订单项列表
     * @return 订单项 DO 列表
     */
    private List<ProductionOrderItemDO> validateProductionOrderItems(List<ProductionOrderSaveReqVO.Item> items) {
        if (CollUtil.isEmpty(items)) {
            return Collections.emptyList();
        }
        // 校验产品
        productService.validProductList(convertSet(items, ProductionOrderSaveReqVO.Item::getProductId));
        // 转换为 DO
        return BeanUtils.toBean(items, ProductionOrderItemDO.class);
    }

    /**
     * 更新生产订单项
     *
     * @param orderId 订单编号
     * @param newItems 新订单项列表
     */
    private void updateProductionOrderItems(Long orderId, List<ProductionOrderItemDO> newItems) {
        // 1. 查询已有订单项
        List<ProductionOrderItemDO> oldItems = productionOrderItemMapper.selectListByOrderId(orderId);
        // 2. 计算新增和修改的订单项
        List<ProductionOrderItemDO> insertItems = new ArrayList<>();
        List<ProductionOrderItemDO> updateItems = new ArrayList<>();
        if (CollUtil.isEmpty(newItems)) {
            // 如果新订单项为空，删除所有旧订单项
            productionOrderItemMapper.deleteByOrderId(orderId);
            return;
        }
        // 3. 计算需要新增、修改、删除的订单项
        Map<Long, ProductionOrderItemDO> oldItemMap = convertMap(oldItems, ProductionOrderItemDO::getId);
        for (ProductionOrderItemDO newItem : newItems) {
            newItem.setProductionOrderId(orderId);
            if (newItem.getId() != null && oldItemMap.containsKey(newItem.getId())) {
                // 更新
                updateItems.add(newItem);
            } else {
                // 新增
                newItem.setId(null);
                insertItems.add(newItem);
            }
        }
        // 4. 执行新增、修改、删除
        if (CollUtil.isNotEmpty(insertItems)) {
            productionOrderItemMapper.insertBatch(insertItems);
        }
        if (CollUtil.isNotEmpty(updateItems)) {
            updateItems.forEach(productionOrderItemMapper::updateById);
        }
        // 计算需要删除的订单项
        Set<Long> newItemIds = convertSet(newItems, item -> item.getId() != null ? item.getId() : 0L);
        List<Long> deleteItemIds = new ArrayList<>();
        for (ProductionOrderItemDO oldItem : oldItems) {
            if (!newItemIds.contains(oldItem.getId())) {
                deleteItemIds.add(oldItem.getId());
            }
        }
        if (CollUtil.isNotEmpty(deleteItemIds)) {
            productionOrderItemMapper.deleteByIds(deleteItemIds);
        }
    }

}