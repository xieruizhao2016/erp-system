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
import cn.iocoder.yudao.module.erp.service.workorder.WorkOrderService;
import cn.iocoder.yudao.module.erp.dal.mysql.workorder.WorkOrderMapper;
import cn.iocoder.yudao.module.erp.service.processroute.ProcessRouteService;
import cn.iocoder.yudao.module.erp.dal.mysql.processroute.ProcessRouteMapper;
import cn.iocoder.yudao.module.erp.service.processrouteitem.ProcessRouteItemService;
import cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem.ProcessRouteItemMapper;
import cn.iocoder.yudao.module.erp.service.workorderprogress.WorkOrderProgressService;
import cn.iocoder.yudao.module.erp.controller.admin.workorder.vo.WorkOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.WorkOrderProgressSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorder.WorkOrderDO;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
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

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private WorkOrderMapper workOrderMapper;

    @Resource
    private ProcessRouteService processRouteService;

    @Resource
    private ProcessRouteMapper processRouteMapper;

    @Resource
    private ProcessRouteItemMapper processRouteItemMapper;

    @Resource
    private WorkOrderProgressService workOrderProgressService;

    @Resource
    private cn.iocoder.yudao.module.erp.service.workcenter.WorkCenterService workCenterService;

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
        
        // 检查状态是否从非"进行中"变为"进行中"
        Integer oldStatus = existingOrder.getStatus();
        Integer newStatus = updateObj.getStatus();
        boolean statusChangedToProcessing = (oldStatus == null || oldStatus != 2) && (newStatus != null && newStatus == 2);
        
        productionOrderMapper.updateById(updateObj);
        
        // 2.2 更新订单项
        updateProductionOrderItems(updateReqVO.getId(), orderItems);
        
        // 2.3 如果状态变为"进行中"，自动创建工单
        if (statusChangedToProcessing) {
            try {
                // 重新查询更新后的订单信息
                ProductionOrderDO updatedOrder = productionOrderMapper.selectById(updateReqVO.getId());
                if (updatedOrder != null) {
                    WorkOrderDO existingWorkOrder = workOrderMapper.selectByProductionOrderId(updateReqVO.getId());
                    if (existingWorkOrder == null) {
                        createWorkOrderFromProductionOrder(updatedOrder);
                    }
                }
            } catch (Exception e) {
                log.error("【ERROR】在updateProductionOrder中自动创建工单时发生异常，生产订单ID: {}", updateReqVO.getId(), e);
            }
        }
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



    @Override
    public ProductionOrderDO getProductionOrder(Long id) {
        return productionOrderMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionOrderDO> getProductionOrderPage(ProductionOrderPageReqVO pageReqVO) {
        return productionOrderMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductionOrderStatus(Long id, Integer status) {
        // 校验存在
        ProductionOrderDO productionOrder = validateProductionOrderExists(id);
        // 更新状态
        ProductionOrderDO updateObj = new ProductionOrderDO();
        updateObj.setId(id);
        updateObj.setStatus(status);
        productionOrderMapper.updateById(updateObj);

        // 如果状态变为"进行中"（状态2），且还没有工单，则自动创建工单
        log.info("更新生产订单状态，订单ID: {}, 新状态: {}, 原状态: {}", id, status, productionOrder.getStatus());
        if (status != null && status == 2) { // 2-进行中
            log.info("生产订单状态变为进行中，订单ID: {}, 订单号: {}", id, productionOrder.getNo());
            try {
                WorkOrderDO existingWorkOrder = workOrderMapper.selectByProductionOrderId(id);
                log.info("查询已存在工单结果: {}", existingWorkOrder != null ? "存在，工单ID=" + existingWorkOrder.getId() : "不存在");
                if (existingWorkOrder == null) {
                    log.info("未找到已存在的工单，开始自动创建工单，生产订单ID: {}", id);
                    // 自动创建工单
                    createWorkOrderFromProductionOrder(productionOrder);
                } else {
                    log.info("已存在工单，工单ID: {}, 工单号: {}", existingWorkOrder.getId(), existingWorkOrder.getWorkOrderNo());
                }
            } catch (Exception e) {
                log.error("自动创建工单时发生异常，生产订单ID: {}", id, e);
            }
        } else {
            log.info("状态不是进行中，跳过自动创建工单，状态: {}", status);
        }
    }

    /**
     * 根据生产订单自动创建工单
     *
     * @param productionOrder 生产订单
     */
    private void createWorkOrderFromProductionOrder(ProductionOrderDO productionOrder) {
        log.info("开始根据生产订单自动创建工单，生产订单ID: {}, 产品ID: {}", productionOrder.getId(), productionOrder.getProductId());
        
        // 1. 校验必要信息
        if (productionOrder.getProductId() == null) {
            log.warn("生产订单没有产品ID，无法创建工单，生产订单ID: {}", productionOrder.getId());
            return; // 没有产品ID，无法创建工单
        }
        if (productionOrder.getQuantity() == null || productionOrder.getQuantity().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            log.warn("生产订单数量无效，无法创建工单，生产订单ID: {}, 数量: {}", productionOrder.getId(), productionOrder.getQuantity());
            return; // 数量无效，无法创建工单
        }

        // 2. 获取产品的工艺路线
        log.info("查询产品的生效工艺路线，产品ID: {}", productionOrder.getProductId());
        ProcessRouteDO processRoute = processRouteMapper.selectActiveByProductId(productionOrder.getProductId());
        if (processRoute == null) {
            log.warn("产品没有生效的工艺路线，无法创建工单，产品ID: {}", productionOrder.getProductId());
            return; // 没有生效的工艺路线，无法创建工单
        }
        log.info("找到工艺路线，工艺路线ID: {}, 工艺路线名称: {}", processRoute.getId(), processRoute.getRouteName());

        // 3. 获取工艺路线明细（工序）
        log.info("查询工艺路线明细，工艺路线ID: {}", processRoute.getId());
        List<ProcessRouteItemDO> routeItems = processRouteItemMapper.selectListByRouteId(processRoute.getId());
        if (CollUtil.isEmpty(routeItems)) {
            log.warn("工艺路线没有工序明细，无法创建工单，工艺路线ID: {}", processRoute.getId());
            return; // 没有工序，无法创建工单
        }
        log.info("找到 {} 个工序", routeItems.size());

        // 4. 计算计划时间（基于工艺路线的标准周期时间）
        LocalDateTime plannedStartTime = productionOrder.getStartTime() != null 
            ? productionOrder.getStartTime() 
            : LocalDateTime.now();
        LocalDateTime plannedEndTime = productionOrder.getEndTime() != null 
            ? productionOrder.getEndTime() 
            : plannedStartTime.plusMinutes(processRoute.getStandardCycleTime() != null ? processRoute.getStandardCycleTime() : 60);

        // 5. 获取工作中心（作为工单的工作中心）
        // 优先使用第一个工序的工作中心，如果第一个工序没有工作中心，则从其他工序中查找
        Long workCenterId = null;
        for (ProcessRouteItemDO routeItem : routeItems) {
            if (routeItem.getWorkCenterId() != null) {
                workCenterId = routeItem.getWorkCenterId();
                break;
            }
        }
        // 如果所有工序都没有工作中心，尝试获取第一个启用的工作中心作为默认值
        if (workCenterId == null) {
            log.warn("所有工序都没有工作中心，尝试获取默认工作中心");
            try {
                List<cn.iocoder.yudao.module.erp.dal.dataobject.workcenter.WorkCenterDO> workCenters = workCenterService.getWorkCenterList();
                if (CollUtil.isNotEmpty(workCenters)) {
                    workCenterId = workCenters.get(0).getId();
                    log.info("使用默认工作中心ID: {}, 名称: {}", workCenterId, workCenters.get(0).getWorkCenterName());
                } else {
                    log.error("系统中没有启用的工作中心，无法创建工单");
                    throw new RuntimeException("系统中没有启用的工作中心，请先配置工作中心后再创建工单");
                }
            } catch (Exception e) {
                log.error("获取默认工作中心失败", e);
                throw new RuntimeException("无法获取工作中心，请先配置工艺路线的工作中心或系统默认工作中心");
            }
        } else {
            log.info("使用工作中心ID: {}", workCenterId);
        }

        // 6. 创建工单
        WorkOrderSaveReqVO workOrderReqVO = new WorkOrderSaveReqVO();
        workOrderReqVO.setProductionOrderId(productionOrder.getId());
        workOrderReqVO.setProductId(productionOrder.getProductId());
        workOrderReqVO.setProductName(productionOrder.getProductName());
        workOrderReqVO.setQuantity(productionOrder.getQuantity());
        workOrderReqVO.setWorkCenterId(workCenterId);
        workOrderReqVO.setPlannedStartTime(plannedStartTime);
        workOrderReqVO.setPlannedEndTime(plannedEndTime);
        workOrderReqVO.setStatus(1); // 1-已创建
        workOrderReqVO.setPriority(productionOrder.getPriority());
        workOrderReqVO.setRemark("自动生成自生产订单：" + productionOrder.getNo());

        log.info("开始创建工单，生产订单ID: {}, 产品ID: {}, 数量: {}", productionOrder.getId(), productionOrder.getProductId(), productionOrder.getQuantity());
        Long workOrderId = workOrderService.createWorkOrder(workOrderReqVO);
        log.info("工单创建成功，工单ID: {}", workOrderId);

        // 7. 根据工艺路线明细创建工单进度记录
        log.info("开始创建工单进度记录，工单ID: {}, 工序数量: {}", workOrderId, routeItems.size());
        for (ProcessRouteItemDO routeItem : routeItems) {
            WorkOrderProgressSaveReqVO progressReqVO = new WorkOrderProgressSaveReqVO();
            progressReqVO.setWorkOrderId(workOrderId);
            progressReqVO.setProcessId(routeItem.getProcessId());
            progressReqVO.setProcessName(routeItem.getOperationName()); // 使用 operationName 作为 processName
            progressReqVO.setSequence(routeItem.getSequence());
            progressReqVO.setEquipmentId(routeItem.getEquipmentId());
            progressReqVO.setStatus(1); // 1-待开始
            progressReqVO.setRemark(routeItem.getRemark());

            workOrderProgressService.createWorkOrderProgress(progressReqVO);
        }
        log.info("工单进度记录创建完成，工单ID: {}, 共创建 {} 条记录", workOrderId, routeItems.size());
    }

    private ProductionOrderDO validateProductionOrderExists(Long id) {
        ProductionOrderDO productionOrder = productionOrderMapper.selectById(id);
        if (productionOrder == null) {
            throw exception(PRODUCTION_ORDER_NOT_EXISTS);
        }
        return productionOrder;
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