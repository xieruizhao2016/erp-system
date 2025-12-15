package cn.iocoder.yudao.module.erp.service.sale;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.number.MoneyUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderItemDO;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleOrderItemMapper;
import cn.iocoder.yudao.module.erp.dal.mysql.sale.ErpSaleOrderMapper;
import cn.iocoder.yudao.module.erp.dal.redis.no.ErpNoRedisDAO;
import cn.iocoder.yudao.module.erp.enums.ErpAuditStatus;
import cn.iocoder.yudao.module.erp.service.finance.ErpAccountService;
import cn.iocoder.yudao.module.erp.service.finance.receivable.ErpFinanceReceivableService;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.productsku.ProductSkuService;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;

// TODO 芋艿：记录操作日志

/**
 * ERP 销售订单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ErpSaleOrderServiceImpl implements ErpSaleOrderService {

    @Resource
    private ErpSaleOrderMapper saleOrderMapper;
    @Resource
    private ErpSaleOrderItemMapper saleOrderItemMapper;

    @Resource
    private ErpNoRedisDAO noRedisDAO;

    @Resource
    private ErpProductService productService;
    @Resource
    private ErpCustomerService customerService;
    @Resource
    private ErpAccountService accountService;
    @Resource
    private ErpFinanceReceivableService financeReceivableService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productbom.ProductBomService productBomService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productbomitem.ProductBomItemService productBomItemService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.processroute.ProcessRouteService processRouteService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.processrouteitem.ProcessRouteItemService processRouteItemService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.purchase.ErpPurchaseOrderService purchaseOrderService;
    @Resource
    private cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderItemMapper purchaseOrderItemMapper;
    @Resource
    private cn.iocoder.yudao.module.erp.dal.mysql.purchase.ErpPurchaseOrderMapper purchaseOrderMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSaleOrder(ErpSaleOrderSaveReqVO createReqVO) {
        // 1.1 校验订单项的有效性
        List<ErpSaleOrderItemDO> saleOrderItems = validateSaleOrderItems(createReqVO.getItems());
        // 1.2 校验客户
        customerService.validateCustomer(createReqVO.getCustomerId());
        // 1.3 校验结算账户
        if (createReqVO.getAccountId() != null) {
            accountService.validateAccount(createReqVO.getAccountId());
        }
        // 1.4 校验销售人员
        if (createReqVO.getSaleUserId() != null) {
            adminUserApi.validateUser(createReqVO.getSaleUserId());
        }
        // 1.5 生成订单号，并校验唯一性
        String no = noRedisDAO.generate(ErpNoRedisDAO.SALE_ORDER_NO_PREFIX);
        if (saleOrderMapper.selectByNo(no) != null) {
            throw exception(SALE_ORDER_NO_EXISTS);
        }

        // 2.1 插入订单
        ErpSaleOrderDO saleOrder = BeanUtils.toBean(createReqVO, ErpSaleOrderDO.class, in -> in
                .setNo(no).setStatus(ErpAuditStatus.PROCESS.getStatus()));
        calculateTotalPrice(saleOrder, saleOrderItems);
        // 计算成本
        calculateCost(saleOrder, saleOrderItems);
        saleOrderMapper.insert(saleOrder);
        // 2.2 插入订单项
        saleOrderItems.forEach(o -> o.setOrderId(saleOrder.getId()));
        saleOrderItemMapper.insertBatch(saleOrderItems);
        return saleOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleOrder(ErpSaleOrderSaveReqVO updateReqVO) {
        // 1.1 校验存在
        ErpSaleOrderDO saleOrder = validateSaleOrderExists(updateReqVO.getId());
        if (ErpAuditStatus.APPROVE.getStatus().equals(saleOrder.getStatus())) {
            throw exception(SALE_ORDER_UPDATE_FAIL_APPROVE, saleOrder.getNo());
        }
        // 1.2 校验客户
        customerService.validateCustomer(updateReqVO.getCustomerId());
        // 1.3 校验结算账户
        if (updateReqVO.getAccountId() != null) {
            accountService.validateAccount(updateReqVO.getAccountId());
        }
        // 1.4 校验销售人员
        if (updateReqVO.getSaleUserId() != null) {
            adminUserApi.validateUser(updateReqVO.getSaleUserId());
        }
        // 1.5 校验订单项的有效性
        List<ErpSaleOrderItemDO> saleOrderItems = validateSaleOrderItems(updateReqVO.getItems());

        // 2.1 更新订单
        ErpSaleOrderDO updateObj = BeanUtils.toBean(updateReqVO, ErpSaleOrderDO.class);
        calculateTotalPrice(updateObj, saleOrderItems);
        // 计算成本
        calculateCost(updateObj, saleOrderItems);
        saleOrderMapper.updateById(updateObj);
        // 2.2 更新订单项
        updateSaleOrderItemList(updateReqVO.getId(), saleOrderItems);
    }

    private void calculateTotalPrice(ErpSaleOrderDO saleOrder, List<ErpSaleOrderItemDO> saleOrderItems) {
        saleOrder.setTotalCount(getSumValue(saleOrderItems, ErpSaleOrderItemDO::getCount, BigDecimal::add));
        saleOrder.setTotalProductPrice(getSumValue(saleOrderItems, ErpSaleOrderItemDO::getTotalPrice, BigDecimal::add, BigDecimal.ZERO));
        saleOrder.setTotalTaxPrice(getSumValue(saleOrderItems, ErpSaleOrderItemDO::getTaxPrice, BigDecimal::add, BigDecimal.ZERO));
        saleOrder.setTotalPrice(saleOrder.getTotalProductPrice().add(saleOrder.getTotalTaxPrice()));
        // 计算优惠价格
        if (saleOrder.getDiscountPercent() == null) {
            saleOrder.setDiscountPercent(BigDecimal.ZERO);
        }
        saleOrder.setDiscountPrice(MoneyUtils.priceMultiplyPercent(saleOrder.getTotalPrice(), saleOrder.getDiscountPercent()));
        saleOrder.setTotalPrice(saleOrder.getTotalPrice().subtract(saleOrder.getDiscountPrice()));
    }

    /**
     * 计算销售订单成本
     */
    private void calculateCost(ErpSaleOrderDO saleOrder, List<ErpSaleOrderItemDO> items) {
        BigDecimal totalMaterialCost = BigDecimal.ZERO;
        BigDecimal totalLaborCost = BigDecimal.ZERO;

        for (ErpSaleOrderItemDO item : items) {
            // 计算原材料成本（从BOM获取）
            BigDecimal materialCost = calculateMaterialCost(item.getProductId(), item.getCount());
            item.setMaterialCost(materialCost);

            // 计算员工成本（从工艺路线获取）
            BigDecimal laborCost = calculateLaborCost(item.getProductId(), item.getCount());
            item.setLaborCost(laborCost);

            // 计算行毛利率
            BigDecimal itemTotalCost = materialCost.add(laborCost);
            BigDecimal itemGrossProfitRate = calculateGrossProfitRate(
                item.getTotalPrice(), itemTotalCost);
            item.setGrossProfitRate(itemGrossProfitRate);

            totalMaterialCost = totalMaterialCost.add(materialCost);
            totalLaborCost = totalLaborCost.add(laborCost);
        }

        // 计算订单总成本
        saleOrder.setMaterialCost(totalMaterialCost);
        saleOrder.setLaborCost(totalLaborCost);
        saleOrder.setTotalCost(totalMaterialCost.add(totalLaborCost));

        // 计算订单毛利率
        saleOrder.setGrossProfitRate(calculateGrossProfitRate(
            saleOrder.getTotalPrice(), saleOrder.getTotalCost()));
    }

    /**
     * 计算原材料成本
     */
    private BigDecimal calculateMaterialCost(Long productId, BigDecimal quantity) {
        if (productId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        try {
            // 1. 获取产品BOM（通过productId查询，取第一个激活的BOM）
            cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.ProductBomPageReqVO pageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.ProductBomPageReqVO();
            pageReqVO.setProductId(productId);
            pageReqVO.setStatus(1); // 1表示激活状态
            pageReqVO.setPageSize(100); // 设置合理的页面大小
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO> bomPage = 
                productBomService.getProductBomPage(pageReqVO);
            
            if (bomPage == null || bomPage.getList().isEmpty()) {
                return BigDecimal.ZERO;
            }

            // 取第一个激活的BOM（按创建时间倒序，取最新的）
            cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO bom = bomPage.getList().stream()
                .filter(b -> b.getStatus() != null && b.getStatus() == 1) // 1为激活状态
                .findFirst()
                .orElse(null);

            if (bom == null) {
                return BigDecimal.ZERO;
            }

            // 2. 遍历BOM明细，计算原材料成本
            BigDecimal totalCost = BigDecimal.ZERO;
            cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.ProductBomItemPageReqVO itemPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.ProductBomItemPageReqVO();
            itemPageReqVO.setBomId(bom.getId());
            itemPageReqVO.setPageSize(1000); // BOM明细可能较多
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO> bomItemPage = 
                productBomItemService.getProductBomItemPage(itemPageReqVO);

            if (bomItemPage != null && !bomItemPage.getList().isEmpty()) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO bomItem : bomItemPage.getList()) {
                    if (bomItem.getChildProductId() == null || bomItem.getQuantity() == null) {
                        continue;
                    }

                    // 获取原材料的最新采购价格
                    BigDecimal purchasePrice = getLatestPurchasePrice(bomItem.getChildProductId());
                    if (purchasePrice == null || purchasePrice.compareTo(BigDecimal.ZERO) <= 0) {
                        // 如果获取不到价格，尝试使用产品的基础价格
                        ErpProductDO product = productService.getProduct(bomItem.getChildProductId());
                        if (product != null && product.getPurchasePrice() != null) {
                            purchasePrice = product.getPurchasePrice();
                        } else {
                            purchasePrice = BigDecimal.ZERO;
                        }
                    }

                    // 计算该原材料的成本 = 采购价格 * BOM数量 * 订单数量
                    BigDecimal itemCost = purchasePrice
                        .multiply(bomItem.getQuantity())
                        .multiply(quantity);
                    totalCost = totalCost.add(itemCost);
                }
            }

            return totalCost;
        } catch (Exception e) {
            // 如果计算失败，返回0，不影响主流程
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算员工成本
     */
    private BigDecimal calculateLaborCost(Long productId, BigDecimal quantity) {
        if (productId == null || quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        try {
            // 1. 获取产品的工艺路线（通过productId查询，取第一个激活的工艺路线）
            cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.ProcessRoutePageReqVO pageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.ProcessRoutePageReqVO();
            pageReqVO.setProductId(productId);
            pageReqVO.setStatus(1); // 1表示激活状态
            pageReqVO.setPageSize(100);
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO> routePage = 
                processRouteService.getProcessRoutePage(pageReqVO);

            if (routePage == null || routePage.getList().isEmpty()) {
                return BigDecimal.ZERO;
            }

            // 取第一个激活的工艺路线（按创建时间倒序，取最新的）
            cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO route = routePage.getList().stream()
                .filter(r -> r.getStatus() != null && r.getStatus() == 1) // 1为激活状态
                .findFirst()
                .orElse(null);

            if (route == null) {
                return BigDecimal.ZERO;
            }

            // 如果工艺路线有标准人工成本，直接使用
            if (route.getStandardLaborCost() != null && route.getStandardLaborCost().compareTo(BigDecimal.ZERO) > 0) {
                return route.getStandardLaborCost().multiply(quantity);
            }

            // 2. 遍历工艺路线明细，计算员工成本
            BigDecimal totalCost = BigDecimal.ZERO;
            cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.ProcessRouteItemPageReqVO itemPageReqVO = 
                new cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.ProcessRouteItemPageReqVO();
            itemPageReqVO.setRouteId(route.getId());
            itemPageReqVO.setPageSize(1000); // 工艺路线明细可能较多
            cn.iocoder.yudao.framework.common.pojo.PageResult<cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO> routeItemPage = 
                processRouteItemService.getProcessRouteItemPage(itemPageReqVO);

            if (routeItemPage != null && !routeItemPage.getList().isEmpty()) {
                for (cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO routeItem : routeItemPage.getList()) {
                    // 获取工序的标准工时
                    BigDecimal standardTime = routeItem.getStandardTime() != null ? new BigDecimal(routeItem.getStandardTime()) : BigDecimal.ZERO;
                    if (standardTime.compareTo(BigDecimal.ZERO) <= 0) {
                        continue;
                    }

                    // 获取员工时薪
                    BigDecimal hourlyRate = getEmployeeHourlyRate(routeItem.getProcessId());
                    if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) <= 0) {
                        // 如果获取不到时薪，使用工艺路线的标准人工成本或默认值
                        if (route.getStandardLaborCost() != null && route.getStandardLaborCost().compareTo(BigDecimal.ZERO) > 0) {
                            // 使用工艺路线的标准人工成本除以标准周期时间
                            BigDecimal standardCycleTime = (route.getStandardCycleTime() != null && route.getStandardCycleTime() > 0)
                                ? new BigDecimal(route.getStandardCycleTime()) : BigDecimal.ONE;
                            hourlyRate = route.getStandardLaborCost().divide(standardCycleTime, 2, java.math.RoundingMode.HALF_UP);
                        } else {
                            hourlyRate = new BigDecimal("50"); // 默认时薪50元/小时
                        }
                    }

                    // 计算该工序的成本 = 时薪 * 标准工时 * 订单数量
                    BigDecimal itemCost = hourlyRate
                        .multiply(standardTime)
                        .multiply(quantity);
                    totalCost = totalCost.add(itemCost);
                }
            }

            return totalCost;
        } catch (Exception e) {
            // 如果计算失败，返回0，不影响主流程
            return BigDecimal.ZERO;
        }
    }

    /**
     * 获取最新采购价格
     */
    private BigDecimal getLatestPurchasePrice(Long productId) {
        if (productId == null) {
            return null;
        }

        try {
            // 查询该产品的采购订单项
            List<cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO> items = 
                purchaseOrderItemMapper.selectList(
                    cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO::getProductId, 
                    productId);

            if (CollUtil.isEmpty(items)) {
                return null;
            }

            // 过滤出已审核的采购订单项，并按创建时间倒序，取最新的价格
            return items.stream()
                .filter(item -> {
                    cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderDO order = 
                        purchaseOrderMapper.selectById(item.getOrderId());
                    return order != null && 
                           ErpAuditStatus.APPROVE.getStatus().equals(order.getStatus()) &&
                           item.getProductPrice() != null &&
                           item.getProductPrice().compareTo(BigDecimal.ZERO) > 0;
                })
                .max((a, b) -> {
                    // 按创建时间倒序
                    if (a.getCreateTime() != null && b.getCreateTime() != null) {
                        return a.getCreateTime().compareTo(b.getCreateTime());
                    }
                    return 0;
                })
                .map(cn.iocoder.yudao.module.erp.dal.dataobject.purchase.ErpPurchaseOrderItemDO::getProductPrice)
                .orElse(null);
        } catch (Exception e) {
            // 如果查询失败，返回null
            return null;
        }
    }

    /**
     * 获取员工时薪
     */
    private BigDecimal getEmployeeHourlyRate(Long processId) {
        if (processId == null) {
            return null;
        }

        try {
            // TODO: 实现从系统配置或员工表获取时薪的逻辑
            // 方案1: 从系统配置表获取（如果有配置的话）
            // 方案2: 从工序表获取标准时薪（如果有字段的话）
            // 方案3: 从员工表获取时薪（如果有字段的话）
            
            // 这里先返回null，使用默认值或工艺路线的标准人工成本
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算毛利率
     */
    private BigDecimal calculateGrossProfitRate(BigDecimal salePrice, BigDecimal totalCost) {
        if (salePrice == null || salePrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }

        // 毛利率 = (销售价格 - 总成本) / 销售价格 × 100%
        BigDecimal profit = salePrice.subtract(totalCost);
        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }
        return profit.divide(salePrice, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSaleOrderStatus(Long id, Integer status) {
        boolean approve = ErpAuditStatus.APPROVE.getStatus().equals(status);
        // 1.1 校验存在
        ErpSaleOrderDO saleOrder = validateSaleOrderExists(id);
        // 1.2 校验状态
        if (saleOrder.getStatus().equals(status)) {
            throw exception(approve ? SALE_ORDER_APPROVE_FAIL : SALE_ORDER_PROCESS_FAIL);
        }
        // 1.3 存在销售出库单，无法反审核
        BigDecimal outCount = saleOrder.getOutCount() != null ? saleOrder.getOutCount() : BigDecimal.ZERO;
        if (!approve && outCount.compareTo(BigDecimal.ZERO) > 0) {
            throw exception(SALE_ORDER_PROCESS_FAIL_EXISTS_OUT);
        }
        // 1.4 存在销售退货单，无法反审核
        BigDecimal returnCount = saleOrder.getReturnCount() != null ? saleOrder.getReturnCount() : BigDecimal.ZERO;
        if (!approve && returnCount.compareTo(BigDecimal.ZERO) > 0) {
            throw exception(SALE_ORDER_PROCESS_FAIL_EXISTS_RETURN);
        }

        // 2. 更新状态
        int updateCount = saleOrderMapper.updateByIdAndStatus(id, saleOrder.getStatus(),
                new ErpSaleOrderDO().setStatus(status));
        if (updateCount == 0) {
            throw exception(approve ? SALE_ORDER_APPROVE_FAIL : SALE_ORDER_PROCESS_FAIL);
        }

        // 3. 审核通过时，自动创建应收账款
        if (approve) {
            financeReceivableService.createReceivableFromSaleOrder(saleOrder);
        } else {
            // 反审核时，删除应收账款（如果存在且未审核）
            financeReceivableService.deleteReceivableByOrderId(id);
        }
    }

    private List<ErpSaleOrderItemDO> validateSaleOrderItems(List<ErpSaleOrderSaveReqVO.Item> list) {
        // 1. 校验产品存在
        List<ErpProductDO> productList = productService.validProductList(
                convertSet(list, ErpSaleOrderSaveReqVO.Item::getProductId));
        Map<Long, ErpProductDO> productMap = convertMap(productList, ErpProductDO::getId);
        // 2. 校验SKU（如果产品有多个SKU，skuId必填）
        for (ErpSaleOrderSaveReqVO.Item item : list) {
            if (item.getProductId() != null) {
                List<ProductSkuDO> skuList = productSkuService.getProductSkuListByProductId(item.getProductId());
                if (skuList != null && skuList.size() > 1) {
                    // 如果产品有多个SKU，skuId必填
                    if (item.getSkuId() == null) {
                        throw exception(SALE_ORDER_ITEM_SKU_ID_REQUIRED, productMap.get(item.getProductId()).getName());
                    }
                    // 校验skuId是否属于该产品
                    boolean skuExists = skuList.stream().anyMatch(sku -> sku.getId().equals(item.getSkuId()));
                    if (!skuExists) {
                        throw exception(SALE_ORDER_ITEM_SKU_INVALID, item.getSkuId(), productMap.get(item.getProductId()).getName());
                    }
                }
            }
        }
        // 3. 转化为 ErpSaleOrderItemDO 列表
        return convertList(list, o -> BeanUtils.toBean(o, ErpSaleOrderItemDO.class, item -> {
            item.setProductUnitId(productMap.get(item.getProductId()).getUnitId());
            item.setTotalPrice(MoneyUtils.priceMultiply(item.getProductPrice(), item.getCount()));
            if (item.getTotalPrice() == null) {
                return;
            }
            if (item.getTaxPercent() != null) {
                item.setTaxPrice(MoneyUtils.priceMultiplyPercent(item.getTotalPrice(), item.getTaxPercent()));
            }
        }));
    }

    private void updateSaleOrderItemList(Long id, List<ErpSaleOrderItemDO> newList) {
        // 第一步，对比新老数据，获得添加、修改、删除的列表
        List<ErpSaleOrderItemDO> oldList = saleOrderItemMapper.selectListByOrderId(id);
        List<List<ErpSaleOrderItemDO>> diffList = diffList(oldList, newList, // id 不同，就认为是不同的记录
                (oldVal, newVal) -> oldVal.getId().equals(newVal.getId()));

        // 第二步，批量添加、修改、删除
        if (CollUtil.isNotEmpty(diffList.get(0))) {
            diffList.get(0).forEach(o -> o.setOrderId(id));
            saleOrderItemMapper.insertBatch(diffList.get(0));
        }
        if (CollUtil.isNotEmpty(diffList.get(1))) {
            saleOrderItemMapper.updateBatch(diffList.get(1));
        }
        if (CollUtil.isNotEmpty(diffList.get(2))) {
            saleOrderItemMapper.deleteByIds(convertList(diffList.get(2), ErpSaleOrderItemDO::getId));
        }
    }

    @Override
    public void updateSaleOrderOutCount(Long id, Map<Long, BigDecimal> outCountMap) {
        List<ErpSaleOrderItemDO> orderItems = saleOrderItemMapper.selectListByOrderId(id);
        // 1. 更新每个销售订单项
        orderItems.forEach(item -> {
            BigDecimal outCount = outCountMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            BigDecimal currentOutCount = item.getOutCount() != null ? item.getOutCount() : BigDecimal.ZERO;
            if (currentOutCount.equals(outCount)) {
                return;
            }
            if (outCount.compareTo(item.getCount()) > 0) {
                throw exception(SALE_ORDER_ITEM_OUT_FAIL_PRODUCT_EXCEED,
                        productService.getProduct(item.getProductId()).getName(), item.getCount());
            }
            saleOrderItemMapper.updateById(new ErpSaleOrderItemDO().setId(item.getId()).setOutCount(outCount));
        });
        // 2. 更新销售订单
        BigDecimal totalOutCount = getSumValue(outCountMap.values(), value -> value, BigDecimal::add, BigDecimal.ZERO);
        saleOrderMapper.updateById(new ErpSaleOrderDO().setId(id).setOutCount(totalOutCount));
    }

    @Override
    public void updateSaleOrderReturnCount(Long orderId, Map<Long, BigDecimal> returnCountMap) {
        List<ErpSaleOrderItemDO> orderItems = saleOrderItemMapper.selectListByOrderId(orderId);
        // 1. 更新每个销售订单项
        orderItems.forEach(item -> {
            BigDecimal returnCount = returnCountMap.getOrDefault(item.getId(), BigDecimal.ZERO);
            BigDecimal currentReturnCount = item.getReturnCount() != null ? item.getReturnCount() : BigDecimal.ZERO;
            if (currentReturnCount.equals(returnCount)) {
                return;
            }
            BigDecimal currentOutCount = item.getOutCount() != null ? item.getOutCount() : BigDecimal.ZERO;
            if (returnCount.compareTo(currentOutCount) > 0) {
                throw exception(SALE_ORDER_ITEM_RETURN_FAIL_OUT_EXCEED,
                        productService.getProduct(item.getProductId()).getName(), currentOutCount);
            }
            saleOrderItemMapper.updateById(new ErpSaleOrderItemDO().setId(item.getId()).setReturnCount(returnCount));
        });
        // 2. 更新销售订单
        BigDecimal totalReturnCount = getSumValue(returnCountMap.values(), value -> value, BigDecimal::add, BigDecimal.ZERO);
        saleOrderMapper.updateById(new ErpSaleOrderDO().setId(orderId).setReturnCount(totalReturnCount));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSaleOrder(List<Long> ids) {
        // 1. 校验不处于已审批
        List<ErpSaleOrderDO> saleOrders = saleOrderMapper.selectByIds(ids);
        if (CollUtil.isEmpty(saleOrders)) {
            return;
        }
        saleOrders.forEach(saleOrder -> {
            if (ErpAuditStatus.APPROVE.getStatus().equals(saleOrder.getStatus())) {
                throw exception(SALE_ORDER_DELETE_FAIL_APPROVE, saleOrder.getNo());
            }
        });

        // 2. 遍历删除，并记录操作日志
        saleOrders.forEach(saleOrder -> {
            // 2.1 删除订单
            saleOrderMapper.deleteById(saleOrder.getId());
            // 2.2 删除订单项
            saleOrderItemMapper.deleteByOrderId(saleOrder.getId());
        });
    }

    private ErpSaleOrderDO validateSaleOrderExists(Long id) {
        ErpSaleOrderDO saleOrder = saleOrderMapper.selectById(id);
        if (saleOrder == null) {
            throw exception(SALE_ORDER_NOT_EXISTS);
        }
        return saleOrder;
    }

    @Override
    public ErpSaleOrderDO getSaleOrder(Long id) {
        return saleOrderMapper.selectById(id);
    }

    @Override
    public ErpSaleOrderDO validateSaleOrder(Long id) {
        ErpSaleOrderDO saleOrder = validateSaleOrderExists(id);
        if (ObjectUtil.notEqual(saleOrder.getStatus(), ErpAuditStatus.APPROVE.getStatus())) {
            throw exception(SALE_ORDER_NOT_APPROVE);
        }
        return saleOrder;
    }

    @Override
    public PageResult<ErpSaleOrderDO> getSaleOrderPage(ErpSaleOrderPageReqVO pageReqVO) {
        return saleOrderMapper.selectPage(pageReqVO);
    }

    // ==================== 订单项 ====================

    @Override
    public List<ErpSaleOrderItemDO> getSaleOrderItemListByOrderId(Long orderId) {
        return saleOrderItemMapper.selectListByOrderId(orderId);
    }

    @Override
    public List<ErpSaleOrderItemDO> getSaleOrderItemListByOrderIds(Collection<Long> orderIds) {
        if (CollUtil.isEmpty(orderIds)) {
            return Collections.emptyList();
        }
        return saleOrderItemMapper.selectListByOrderIds(orderIds);
    }

}
