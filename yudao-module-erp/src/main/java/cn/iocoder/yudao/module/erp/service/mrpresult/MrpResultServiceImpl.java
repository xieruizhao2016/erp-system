package cn.iocoder.yudao.module.erp.service.mrpresult;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult.MrpResultDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpWarehouseDO;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService;
import cn.iocoder.yudao.module.erp.service.productbom.ProductBomService;
import cn.iocoder.yudao.module.erp.service.productbomitem.ProductBomItemService;
import cn.iocoder.yudao.module.erp.dal.mysql.productbom.ProductBomMapper;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockService;
import cn.iocoder.yudao.module.erp.service.stock.ErpWarehouseService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.erp.dal.mysql.mrpresult.MrpResultMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleOrderMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.productbom.ProductBomMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.productbomitem.ProductBomItemMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderItemMapper;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO;
import cn.iocoder.yudao.module.erp.dal.mysql.productionorder.ProductionOrderMapper;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;

import lombok.extern.slf4j.Slf4j;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

/**
 * ERP MRP运算结果 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MrpResultServiceImpl implements MrpResultService {

    @Resource
    private MrpResultMapper mrpResultMapper;
    
    @Resource
    private ErpSaleOrderMapper saleOrderMapper;
    
    @Resource
    private ErpSaleOrderService saleOrderService;
    
    @Resource
    private ProductBomMapper productBomMapper;
    
    @Resource
    private ProductBomItemMapper productBomItemMapper;
    
    @Resource
    private ErpStockService stockService;
    
    @Resource
    private ErpWarehouseService warehouseService;
    
    @Resource
    private ErpProductService productService;
    
    @Resource
    private ErpPurchaseOrderMapper purchaseOrderMapper;
    
    @Resource
    private ErpPurchaseOrderItemMapper purchaseOrderItemMapper;
    
    @Resource
    private ProductionOrderMapper productionOrderMapper;

    @Override
    public Long createMrpResult(MrpResultSaveReqVO createReqVO) {
        // 插入
        MrpResultDO mrpResult = BeanUtils.toBean(createReqVO, MrpResultDO.class);
        mrpResultMapper.insert(mrpResult);

        // 返回
        return mrpResult.getId();
    }

    @Override
    public void updateMrpResult(MrpResultSaveReqVO updateReqVO) {
        // 校验存在
        validateMrpResultExists(updateReqVO.getId());
        // 更新
        MrpResultDO updateObj = BeanUtils.toBean(updateReqVO, MrpResultDO.class);
        mrpResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteMrpResult(Long id) {
        // 校验存在
        validateMrpResultExists(id);
        // 删除
        mrpResultMapper.deleteById(id);
    }

    @Override
        public void deleteMrpResultListByIds(List<Long> ids) {
        // 删除
        mrpResultMapper.deleteByIds(ids);
        }


    private void validateMrpResultExists(Long id) {
        if (mrpResultMapper.selectById(id) == null) {
            throw exception(MRP_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public MrpResultDO getMrpResult(Long id) {
        return mrpResultMapper.selectById(id);
    }

    @Override
    public PageResult<MrpResultDO> getMrpResultPage(MrpResultPageReqVO pageReqVO) {
        return mrpResultMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MrpCalculationResultVO executeMrpCalculation(MrpCalculationReqVO calculationReqVO) {
        long startTime = System.currentTimeMillis();
        String runNo = generateRunNo();
        
        try {
            log.info("[MRP运算] 开始执行MRP运算，批次号：{}", runNo);
            
            // 1. 获取销售订单需求（成品需求）
            Map<Long, BigDecimal> productRequirementMap = getProductRequirements(calculationReqVO);
            log.info("[MRP运算] 获取到{}个产品的需求", productRequirementMap.size());
            
            if (productRequirementMap.isEmpty()) {
                return MrpCalculationResultVO.builder()
                        .runNo(runNo)
                        .success(true)
                        .calculationTime(System.currentTimeMillis() - startTime)
                        .message("指定时间范围内没有销售订单需求")
                        .results(new ArrayList<>())
                        .statistics(MrpCalculationResultVO.MrpStatisticsVO.builder()
                                .totalProducts(0)
                                .needProductionCount(0)
                                .needPurchaseCount(0)
                                .sufficientStockCount(0)
                                .build())
                        .build();
            }
            
            // 2. BOM多层级展开：根据成品的BOM递归计算所有层级的原材料需求
            Map<Long, BigDecimal> expandedRequirementMap = expandBomRequirements(productRequirementMap, calculationReqVO);
            log.info("[MRP运算] BOM展开后，共{}个产品需要计算", expandedRequirementMap.size());
            
            // 3. 计算MRP结果
            List<MrpResultDO> mrpResults = new ArrayList<>();
            List<MrpCalculationResultVO.MrpResultItemVO> resultItems = new ArrayList<>();
            int needProductionCount = 0;
            int needPurchaseCount = 0;
            int sufficientStockCount = 0;
            
            for (Map.Entry<Long, BigDecimal> entry : expandedRequirementMap.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal grossRequirement = entry.getValue();
                
                // 获取产品信息
                ErpProductDO product = productService.getProduct(productId);
                if (product == null) {
                    log.warn("[MRP运算] 产品{}不存在，跳过", productId);
                    continue;
                }
                
                // 获取库存信息（可能为null，需要处理）
                BigDecimal onHandInventory = stockService.getStockCount(productId);
                if (onHandInventory == null) {
                    onHandInventory = BigDecimal.ZERO;
                }
                
                // 计划接收量：从采购订单和生产订单中获取未完成数量
                BigDecimal scheduledReceipts = getScheduledReceipts(productId, calculationReqVO);
                
                // 安全库存：从产品配置中获取（如果产品表有安全库存字段，否则为0）
                BigDecimal safetyStock = getSafetyStock(productId);
                
                // 计算净需求 = 毛需求 - 现有库存 - 计划接收量 + 安全库存
                BigDecimal netRequirement = grossRequirement
                        .subtract(onHandInventory)
                        .subtract(scheduledReceipts)
                        .add(safetyStock);
                
                // 如果净需求小于等于0，说明库存充足
                if (netRequirement.compareTo(BigDecimal.ZERO) <= 0) {
                    netRequirement = BigDecimal.ZERO;
                }
                
                // 计划订单发放量 = 净需求（简化处理，实际应考虑批量规则）
                BigDecimal plannedOrderReleases = netRequirement;
                
                // 确定订单类型：根据产品是否有BOM来判断
                // 如果有BOM，说明是成品，需要生产 → 订单类型 = 1（生产订单）
                // 如果没有BOM，说明是原材料，需要采购 → 订单类型 = 2（采购订单）
                Integer orderType;
                List<ProductBomDO> productBoms = productBomMapper.selectList(
                        new LambdaQueryWrapperX<ProductBomDO>()
                                .eq(ProductBomDO::getProductId, productId)
                                .eq(ProductBomDO::getStatus, 2) // 2-生效状态的BOM
                );
                if (CollUtil.isNotEmpty(productBoms)) {
                    // 有BOM，是成品，需要生产
                    orderType = 1; // 1-生产订单
                    log.debug("[MRP运算] 产品{}有BOM，订单类型：生产订单", productId);
                } else {
                    // 没有BOM，是原材料，需要采购
                    orderType = 2; // 2-采购订单
                    log.debug("[MRP运算] 产品{}无BOM，订单类型：采购订单", productId);
                }
                
                // 确定订单状态
                Integer orderStatus = netRequirement.compareTo(BigDecimal.ZERO) > 0 ? 1 : 3; // 1-建议，3-下达
                
                // 统计
                if (netRequirement.compareTo(BigDecimal.ZERO) > 0) {
                    if (orderType == 1) {
                        needProductionCount++;
                    } else {
                        needPurchaseCount++;
                    }
                } else {
                    sufficientStockCount++;
                }
                
                // 计算需求日期：使用计划结束日期作为需求日期
                // 需求日期表示客户需要产品的日期，应该是计划结束日期
                LocalDate dueDate = calculationReqVO.getPlanEndDate().toLocalDate();
                
                // 构建MRP结果DO并保存
                MrpResultDO mrpResult = MrpResultDO.builder()
                        .runNo(runNo)
                        .productId(productId)
                        .warehouseId(null) // 暂不指定仓库
                        .periodStartDate(calculationReqVO.getPlanStartDate().toLocalDate())
                        .periodEndDate(calculationReqVO.getPlanEndDate().toLocalDate())
                        .grossRequirement(grossRequirement)
                        .scheduledReceipts(scheduledReceipts)
                        .onHandInventory(onHandInventory)
                        .netRequirement(netRequirement)
                        .plannedOrderReceipts(plannedOrderReleases)
                        .plannedOrderReleases(plannedOrderReleases)
                        .orderType(orderType)
                        .lotSizingRule(2) // 2-按需
                        .leadTime(7) // 默认提前期7天
                        .safetyStock(safetyStock)
                        .orderStatus(orderStatus)
                        .dueDate(dueDate)
                        .build();
                
                mrpResultMapper.insert(mrpResult);
                mrpResults.add(mrpResult);
                
                // 构建返回结果项
                String suggestion = "";
                if (netRequirement.compareTo(BigDecimal.ZERO) > 0) {
                    suggestion = orderType == 1 
                            ? "建议创建生产订单，数量：" + plannedOrderReleases.stripTrailingZeros().toPlainString()
                            : "建议创建采购订单，数量：" + plannedOrderReleases.stripTrailingZeros().toPlainString();
                } else {
                    suggestion = "库存充足，无需处理";
                }
                
                resultItems.add(MrpCalculationResultVO.MrpResultItemVO.builder()
                        .productId(productId)
                        .productName(product.getName())
                        .warehouseId(null)
                        .warehouseName("-")
                        .grossRequirement(grossRequirement.stripTrailingZeros().toPlainString())
                        .onHandInventory(onHandInventory.stripTrailingZeros().toPlainString())
                        .scheduledReceipts(scheduledReceipts.stripTrailingZeros().toPlainString())
                        .netRequirement(netRequirement.stripTrailingZeros().toPlainString())
                        .plannedOrderReleases(plannedOrderReleases.stripTrailingZeros().toPlainString())
                        .orderType(orderType)
                        .suggestion(suggestion)
                        .build());
            }
            
            // 3. 构建统计信息
            MrpCalculationResultVO.MrpStatisticsVO statistics = MrpCalculationResultVO.MrpStatisticsVO.builder()
                    .totalProducts(productRequirementMap.size())
                    .needProductionCount(needProductionCount)
                    .needPurchaseCount(needPurchaseCount)
                    .sufficientStockCount(sufficientStockCount)
                    .build();
            
            long endTime = System.currentTimeMillis();
            log.info("[MRP运算] MRP运算完成，批次号：{}，耗时：{}ms，共处理{}个产品", 
                    runNo, endTime - startTime, productRequirementMap.size());
            
            // 4. 返回结果
            return MrpCalculationResultVO.builder()
                    .runNo(runNo)
                    .success(true)
                    .calculationTime(endTime - startTime)
                    .message("MRP运算成功完成")
                    .results(resultItems)
                    .statistics(statistics)
                    .build();
                    
        } catch (Exception e) {
            log.error("[MRP运算] MRP运算失败，批次号：{}", runNo, e);
            return MrpCalculationResultVO.builder()
                    .runNo(runNo)
                    .success(false)
                    .calculationTime(System.currentTimeMillis() - startTime)
                    .message("MRP运算失败：" + e.getMessage())
                    .results(new ArrayList<>())
                    .statistics(null)
                    .build();
        }
    }
    
    /**
     * 生成运算批次号
     */
    private String generateRunNo() {
        return "MRP-" + DateUtil.format(new Date(), "yyyyMMdd") + "-" 
                + String.format("%03d", (int) (Math.random() * 1000));
    }
    
    /**
     * 获取产品需求
     */
    private Map<Long, BigDecimal> getProductRequirements(MrpCalculationReqVO calculationReqVO) {
        Map<Long, BigDecimal> productRequirementMap = new HashMap<>();
        
        // 查询已审核的销售订单
        List<ErpSaleOrderDO> saleOrders = saleOrderMapper.selectList(
                "status", ErpAuditStatus.APPROVE.getStatus());
        
        if (CollUtil.isEmpty(saleOrders)) {
            return productRequirementMap;
        }
        
        // 获取所有订单的ID
        List<Long> orderIds = convertList(saleOrders, ErpSaleOrderDO::getId);
        
        // 获取所有订单项
        List<ErpSaleOrderItemDO> orderItems = saleOrderService.getSaleOrderItemListByOrderIds(orderIds);
        
        // 按产品汇总需求数量
        for (ErpSaleOrderItemDO item : orderItems) {
            // 跳过无效数据
            if (item.getCount() == null || item.getProductId() == null) {
                log.warn("[MRP运算] 订单项数据不完整，跳过：orderItemId={}, count={}, productId={}", 
                        item.getId(), item.getCount(), item.getProductId());
                continue;
            }
            
            // 计算未完成数量 = 订单数量 - 已出库数量（已出库数量可能为null，默认为0）
            BigDecimal outCount = item.getOutCount() != null ? item.getOutCount() : BigDecimal.ZERO;
            BigDecimal unfinishedCount = item.getCount().subtract(outCount);
            
            if (unfinishedCount.compareTo(BigDecimal.ZERO) > 0) {
                productRequirementMap.merge(item.getProductId(), unfinishedCount, BigDecimal::add);
            }
        }
        
        // 如果指定了产品ID列表，则只保留指定的产品
        if (CollUtil.isNotEmpty(calculationReqVO.getProductIds())) {
            productRequirementMap.keySet().retainAll(calculationReqVO.getProductIds());
        }
        
        return productRequirementMap;
    }
    
    /**
     * BOM多层级展开：根据成品的BOM递归计算所有层级的原材料需求
     */
    private Map<Long, BigDecimal> expandBomRequirements(Map<Long, BigDecimal> productRequirementMap, 
                                                       MrpCalculationReqVO calculationReqVO) {
        Map<Long, BigDecimal> expandedMap = new HashMap<>(productRequirementMap);
        Set<Long> processedProducts = new HashSet<>();
        
        // 递归展开BOM
        for (Map.Entry<Long, BigDecimal> entry : productRequirementMap.entrySet()) {
            expandBomRecursive(entry.getKey(), entry.getValue(), expandedMap, processedProducts, 0);
        }
        
        return expandedMap;
    }
    
    /**
     * 递归展开BOM
     * @param productId 产品ID
     * @param quantity 需要数量
     * @param expandedMap 展开后的需求映射
     * @param processedProducts 已处理的产品（防止循环引用）
     * @param level 当前层级（防止无限递归）
     */
    private void expandBomRecursive(Long productId, BigDecimal quantity, 
                                    Map<Long, BigDecimal> expandedMap,
                                    Set<Long> processedProducts, int level) {
        // 防止无限递归（最多展开10层）
        if (level > 10) {
            log.warn("[MRP运算] BOM展开层级过深，停止展开：productId={}, level={}", productId, level);
            return;
        }
        
        // 防止循环引用
        if (processedProducts.contains(productId)) {
            log.warn("[MRP运算] 检测到BOM循环引用，跳过：productId={}", productId);
            return;
        }
        
        // 查询产品是否有BOM
        List<ProductBomDO> productBoms = productBomMapper.selectList(
                new LambdaQueryWrapperX<ProductBomDO>()
                        .eq(ProductBomDO::getProductId, productId)
                        .eq(ProductBomDO::getStatus, 2) // 2-生效状态的BOM
        );
        
        if (CollUtil.isEmpty(productBoms)) {
            // 没有BOM，说明是原材料，不需要展开
            return;
        }
        
        // 使用第一个生效的BOM
        ProductBomDO bom = productBoms.get(0);
        
        // 查询BOM明细
        List<ProductBomItemDO> bomItems = productBomItemMapper.selectList(
                new LambdaQueryWrapperX<ProductBomItemDO>()
                        .eq(ProductBomItemDO::getBomId, bom.getId())
        );
        
        if (CollUtil.isEmpty(bomItems)) {
            return;
        }
        
        // 标记当前产品已处理
        processedProducts.add(productId);
        
        // 展开BOM明细
        for (ProductBomItemDO item : bomItems) {
            if (item.getChildProductId() == null || item.getQuantity() == null) {
                continue;
            }
            
            // 计算子产品需求数量 = 父产品数量 * BOM用量
            BigDecimal childQuantity = quantity.multiply(item.getQuantity());
            
            // 如果有损耗率，需要考虑损耗
            if (item.getLossRate() != null && item.getLossRate().compareTo(BigDecimal.ZERO) > 0) {
                // 有效用量 = 用量 * (1 + 损耗率)
                BigDecimal effectiveQuantity = item.getQuantity().multiply(
                        BigDecimal.ONE.add(item.getLossRate().divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP))
                );
                childQuantity = quantity.multiply(effectiveQuantity);
            }
            
            // 累加到展开后的需求映射中
            expandedMap.merge(item.getChildProductId(), childQuantity, BigDecimal::add);
            
            // 递归展开子产品的BOM
            expandBomRecursive(item.getChildProductId(), childQuantity, expandedMap, processedProducts, level + 1);
        }
        
        // 移除标记，允许在其他路径中再次处理
        processedProducts.remove(productId);
    }
    
    /**
     * 获取计划接收量：从采购订单和生产订单中获取未完成数量
     */
    private BigDecimal getScheduledReceipts(Long productId, MrpCalculationReqVO calculationReqVO) {
        BigDecimal scheduledReceipts = BigDecimal.ZERO;
        
        // 1. 从采购订单中获取未完成数量
        List<ErpPurchaseOrderDO> purchaseOrders = purchaseOrderMapper.selectList(
                new LambdaQueryWrapperX<ErpPurchaseOrderDO>()
                        .eq(ErpPurchaseOrderDO::getStatus, ErpAuditStatus.APPROVE.getStatus())
        );
        
        if (CollUtil.isNotEmpty(purchaseOrders)) {
            List<Long> purchaseOrderIds = convertList(purchaseOrders, ErpPurchaseOrderDO::getId);
            List<ErpPurchaseOrderItemDO> purchaseOrderItems = purchaseOrderItemMapper.selectList(
                    new LambdaQueryWrapperX<ErpPurchaseOrderItemDO>()
                            .in(ErpPurchaseOrderItemDO::getOrderId, purchaseOrderIds)
                            .eq(ErpPurchaseOrderItemDO::getProductId, productId)
            );
            
            for (ErpPurchaseOrderItemDO item : purchaseOrderItems) {
                if (item.getCount() == null) {
                    continue;
                }
                // 未完成数量 = 订单数量 - 已入库数量
                BigDecimal inCount = item.getInCount() != null ? item.getInCount() : BigDecimal.ZERO;
                BigDecimal unfinishedCount = item.getCount().subtract(inCount);
                if (unfinishedCount.compareTo(BigDecimal.ZERO) > 0) {
                    scheduledReceipts = scheduledReceipts.add(unfinishedCount);
                }
            }
        }
        
        // 2. 从生产订单中获取未完成数量
        List<ProductionOrderDO> productionOrders = productionOrderMapper.selectList(
                new LambdaQueryWrapperX<ProductionOrderDO>()
                        .eq(ProductionOrderDO::getProductId, productId)
                        .in(ProductionOrderDO::getStatus, Arrays.asList(1, 2)) // 1-待开始，2-进行中
        );
        
        for (ProductionOrderDO order : productionOrders) {
            if (order.getQuantity() == null) {
                continue;
            }
            // 未完成数量 = 生产数量 - 已完成数量
            BigDecimal completedQuantity = order.getCompletedQuantity() != null 
                    ? order.getCompletedQuantity() : BigDecimal.ZERO;
            BigDecimal unfinishedCount = order.getQuantity().subtract(completedQuantity);
            if (unfinishedCount.compareTo(BigDecimal.ZERO) > 0) {
                scheduledReceipts = scheduledReceipts.add(unfinishedCount);
            }
        }
        
        return scheduledReceipts;
    }
    
    /**
     * 获取安全库存
     * 注意：当前产品表没有安全库存字段，这里返回0
     * 后续可以在产品表中添加safetyStock字段，或者创建单独的安全库存配置表
     */
    private BigDecimal getSafetyStock(Long productId) {
        // TODO: 从产品表或安全库存配置表中获取安全库存
        // 当前实现：返回0
        return BigDecimal.ZERO;
    }

}