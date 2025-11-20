package cn.iocoder.yudao.module.erp.service.mrpresult;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            
            // 1. 获取销售订单需求
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
            
            // 2. 计算MRP结果
            List<MrpResultDO> mrpResults = new ArrayList<>();
            List<MrpCalculationResultVO.MrpResultItemVO> resultItems = new ArrayList<>();
            int needProductionCount = 0;
            int needPurchaseCount = 0;
            int sufficientStockCount = 0;
            
            for (Map.Entry<Long, BigDecimal> entry : productRequirementMap.entrySet()) {
                Long productId = entry.getKey();
                BigDecimal grossRequirement = entry.getValue();
                
                // 获取产品信息
                ErpProductDO product = productService.getProduct(productId);
                if (product == null) {
                    log.warn("[MRP运算] 产品{}不存在，跳过", productId);
                    continue;
                }
                
                // 获取库存信息
                BigDecimal onHandInventory = stockService.getStockCount(productId);
                
                // 计划接收量（暂时为0，后续可以从采购订单和生产订单中获取）
                BigDecimal scheduledReceipts = BigDecimal.ZERO;
                
                // 安全库存（简化处理，使用0）
                BigDecimal safetyStock = BigDecimal.ZERO;
                
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
                
                // 确定订单类型（简化处理：假设都是生产订单，实际应根据产品属性判断）
                Integer orderType = 1; // 1-生产订单
                
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
                        .dueDate(calculationReqVO.getPlanStartDate().toLocalDate())
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
            // 计算未完成数量 = 订单数量 - 已出库数量
            BigDecimal unfinishedCount = item.getCount().subtract(item.getOutCount());
            
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

}