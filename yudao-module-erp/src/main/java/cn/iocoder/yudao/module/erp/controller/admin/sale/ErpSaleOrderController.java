package cn.iocoder.yudao.module.erp.controller.admin.sale;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import lombok.extern.slf4j.Slf4j;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderImportExcelVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderImportReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderImportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.order.ErpSaleOrderSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOrderItemDO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.productsku.ProductSkuService;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOrderService;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockService;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - ERP 销售订单")
@RestController
@RequestMapping("/erp/sale-order")
@Validated
@Slf4j
public class ErpSaleOrderController {

    @Resource
    private ErpSaleOrderService saleOrderService;
    @Resource
    private ErpStockService stockService;
    @Resource
    private ErpProductService productService;
    @Resource
    private ProductSkuService productSkuService;
    @Resource
    private ErpCustomerService customerService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建销售订单")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:create')")
    public CommonResult<Long> createSaleOrder(@Valid @RequestBody ErpSaleOrderSaveReqVO createReqVO) {
        return success(saleOrderService.createSaleOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售订单")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:update')")
    public CommonResult<Boolean> updateSaleOrder(@Valid @RequestBody ErpSaleOrderSaveReqVO updateReqVO) {
        saleOrderService.updateSaleOrder(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新销售订单的状态")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:update-status')")
    public CommonResult<Boolean> updateSaleOrderStatus(@RequestParam("id") Long id,
                                                      @RequestParam("status") Integer status) {
        saleOrderService.updateSaleOrderStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售订单")
    @Parameter(name = "ids", description = "编号数组", required = true)
    @PreAuthorize("@ss.hasPermission('erp:sale-out:delete')")
    public CommonResult<Boolean> deleteSaleOrder(@RequestParam("ids") List<Long> ids) {
        saleOrderService.deleteSaleOrder(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:query')")
    public CommonResult<ErpSaleOrderRespVO> getSaleOrder(@RequestParam("id") Long id) {
        try {
            ErpSaleOrderDO saleOrder = saleOrderService.getSaleOrder(id);
            if (saleOrder == null) {
                return success(null);
            }
            List<ErpSaleOrderItemDO> saleOrderItemList = saleOrderService.getSaleOrderItemListByOrderId(id);
            if (CollUtil.isEmpty(saleOrderItemList)) {
                return success(BeanUtils.toBean(saleOrder, ErpSaleOrderRespVO.class));
            }
            // 过滤掉productId为null的项
            List<ErpSaleOrderItemDO> validItemList = saleOrderItemList.stream()
                    .filter(item -> item != null && item.getProductId() != null)
                    .collect(Collectors.toList());
            
            // 如果没有有效的订单项，直接返回订单信息
            if (CollUtil.isEmpty(validItemList)) {
                ErpSaleOrderRespVO respVO = BeanUtils.toBean(saleOrder, ErpSaleOrderRespVO.class);
                respVO.setItems(Collections.emptyList());
                return success(respVO);
            }
            
            Map<Long, ErpProductRespVO> productMap = productService.getProductVOMap(
                    convertSet(validItemList, ErpSaleOrderItemDO::getProductId));
            return success(BeanUtils.toBean(saleOrder, ErpSaleOrderRespVO.class, saleOrderVO ->
                    saleOrderVO.setItems(BeanUtils.toBean(validItemList, ErpSaleOrderRespVO.Item.class, item -> {
                        if (item.getProductId() != null) {
                            try {
                                BigDecimal stockCount = stockService.getStockCount(item.getProductId());
                                item.setStockCount(stockCount != null ? stockCount : BigDecimal.ZERO);
                            } catch (Exception e) {
                                log.warn("获取产品库存失败，产品ID: {}", item.getProductId(), e);
                                item.setStockCount(BigDecimal.ZERO);
                            }
                            MapUtils.findAndThen(productMap, item.getProductId(), product -> {
                                if (product != null) {
                                    item.setProductName(product.getName());
                                    item.setProductBarCode(product.getBarCode());
                                    item.setProductUnitName(product.getUnitName());
                                }
                            });
                        }
                    }))));
        } catch (Exception e) {
            log.error("获取销售订单详情失败，订单ID: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售订单分页")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:query')")
    public CommonResult<PageResult<ErpSaleOrderRespVO>> getSaleOrderPage(@Valid ErpSaleOrderPageReqVO pageReqVO) {
        try {
            PageResult<ErpSaleOrderDO> pageResult = saleOrderService.getSaleOrderPage(pageReqVO);
            return success(buildSaleOrderVOPageResult(pageResult));
        } catch (Exception e) {
            log.error("获取销售订单分页失败，查询参数: {}", pageReqVO, e);
            // 重新抛出异常，让全局异常处理器处理
            throw e;
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售订单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSaleOrderExcel(@Valid ErpSaleOrderPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpSaleOrderRespVO> list = buildSaleOrderVOPageResult(saleOrderService.getSaleOrderPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售订单.xls", "数据", ErpSaleOrderRespVO.class, list);
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入销售订单模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<ErpSaleOrderImportExcelVO> list = Arrays.asList(
                ErpSaleOrderImportExcelVO.builder()
                        .orderNo("SO202501010001")
                        .customerName("示例客户")
                        .orderTime(LocalDateTime.now())
                        .saleUserName("")
                        .accountName("")
                        .discountPercent(BigDecimal.ZERO)
                        .depositPrice(BigDecimal.ZERO)
                        .purchaseOrderNo("")
                        .contractNo("")
                        .remark("示例备注")
                        .productName("示例产品1")
                        .skuCode("")
                        .productUnitName("个")
                        .count(BigDecimal.valueOf(10))
                        .productPrice(BigDecimal.valueOf(100.00))
                        .taxPercent(BigDecimal.valueOf(13))
                        .itemRemark("")
                        .build(),
                ErpSaleOrderImportExcelVO.builder()
                        .orderNo("")
                        .customerName("")
                        .orderTime(null)
                        .saleUserName("")
                        .accountName("")
                        .discountPercent(null)
                        .depositPrice(null)
                        .purchaseOrderNo("")
                        .contractNo("")
                        .remark("")
                        .productName("示例产品2")
                        .skuCode("SKU001")
                        .productUnitName("个")
                        .count(BigDecimal.valueOf(20))
                        .productPrice(BigDecimal.valueOf(200.00))
                        .taxPercent(BigDecimal.valueOf(13))
                        .itemRemark("")
                        .build()
        );
        // 输出
        ExcelUtils.write(response, "销售订单导入模板.xls", "销售订单列表", ErpSaleOrderImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入销售订单")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:import')")
    public CommonResult<ErpSaleOrderImportRespVO> importExcel(@Valid ErpSaleOrderImportReqVO importReqVO)
            throws Exception {
        List<ErpSaleOrderImportExcelVO> list = ExcelUtils.read(importReqVO.getFile(), ErpSaleOrderImportExcelVO.class);
        return success(saleOrderService.importSaleOrderList(list, importReqVO));
    }

    private PageResult<ErpSaleOrderRespVO> buildSaleOrderVOPageResult(PageResult<ErpSaleOrderDO> pageResult) {
        try {
            if (CollUtil.isEmpty(pageResult.getList())) {
                return PageResult.empty(pageResult.getTotal());
            }
        // 1.1 订单项
        Set<Long> orderIds = convertSet(pageResult.getList(), ErpSaleOrderDO::getId, order -> order.getId() != null);
        List<ErpSaleOrderItemDO> saleOrderItemList = CollUtil.isEmpty(orderIds) ? Collections.emptyList() :
                saleOrderService.getSaleOrderItemListByOrderIds(orderIds);
        if (saleOrderItemList == null) {
            saleOrderItemList = Collections.emptyList();
        }
        // 过滤掉 orderId 为 null 的项，避免 convertMultiMap 出现问题
        List<ErpSaleOrderItemDO> validItemList = saleOrderItemList.stream()
                .filter(item -> item != null && item.getOrderId() != null)
                .collect(Collectors.toList());
        Map<Long, List<ErpSaleOrderItemDO>> saleOrderItemMap = convertMultiMap(validItemList, ErpSaleOrderItemDO::getOrderId);
        // 1.2 产品信息
        Set<Long> productIds = convertSet(saleOrderItemList, ErpSaleOrderItemDO::getProductId, item -> item.getProductId() != null);
        Map<Long, ErpProductRespVO> productMap = CollUtil.isEmpty(productIds) ? Collections.emptyMap() :
                productService.getProductVOMap(productIds);
        // 1.3 SKU信息
        Set<Long> skuIds = convertSet(saleOrderItemList, ErpSaleOrderItemDO::getSkuId, item -> item.getSkuId() != null);
        Map<Long, ProductSkuDO> skuMap = CollUtil.isEmpty(skuIds) ? Collections.emptyMap() :
                convertMap(productSkuService.getProductSkuList(skuIds), ProductSkuDO::getId);
        // 1.4 客户信息
        Set<Long> customerIds = convertSet(pageResult.getList(), ErpSaleOrderDO::getCustomerId, saleOrder -> saleOrder.getCustomerId() != null);
        Map<Long, ErpCustomerDO> customerMap = CollUtil.isEmpty(customerIds) ? Collections.emptyMap() :
                customerService.getCustomerMap(customerIds);
        // 1.5 管理员信息
        Set<Long> creatorIds = convertSet(pageResult.getList(), saleOrder -> {
            String creator = saleOrder.getCreator();
            if (creator == null || creator.trim().isEmpty()) {
                return null;
            }
            try {
                return Long.parseLong(creator);
            } catch (NumberFormatException e) {
                return null;
            }
        }, creatorId -> creatorId != null);
        Map<Long, AdminUserRespDTO> userMap = CollUtil.isEmpty(creatorIds) ? Collections.emptyMap() :
                adminUserApi.getUserMap(creatorIds);
        // 2. 开始拼接
        return BeanUtils.toBean(pageResult, ErpSaleOrderRespVO.class, saleOrder -> {
            try {
                List<ErpSaleOrderItemDO> items = saleOrderItemMap.get(saleOrder.getId());
                if (items != null && !items.isEmpty()) {
                    saleOrder.setItems(BeanUtils.toBean(items, ErpSaleOrderRespVO.Item.class,
                            item -> {
                                if (item.getProductId() != null) {
                                    MapUtils.findAndThen(productMap, item.getProductId(), product -> {
                                        if (product != null) {
                                            item.setProductName(product.getName());
                                            item.setProductBarCode(product.getBarCode());
                                            item.setProductUnitName(product.getUnitName());
                                        }
                                    });
                                }
                            }));
                } else {
                    saleOrder.setItems(Collections.emptyList());
                }
                // 生成产品信息：产品名称+SKU编号（如果有SKU）
                if (saleOrder.getItems() != null && !saleOrder.getItems().isEmpty()) {
                    saleOrder.setProductNames(CollUtil.join(saleOrder.getItems(), "，", item -> {
                        try {
                            String productName = item.getProductName() != null ? item.getProductName() : "";
                            if (item.getSkuId() != null) {
                                ProductSkuDO sku = skuMap.get(item.getSkuId());
                                if (sku != null) {
                                    // 优先使用SKU编码，如果没有则使用SKU名称
                                    String skuInfo = sku.getSkuCode() != null ? sku.getSkuCode() : 
                                                    (sku.getSkuName() != null ? sku.getSkuName() : sku.getId().toString());
                                    return productName + "(" + skuInfo + ")";
                                }
                            }
                            return productName;
                        } catch (Exception e) {
                            // 忽略异常，返回产品名称
                            return item.getProductName() != null ? item.getProductName() : "";
                        }
                    }));
                } else {
                    saleOrder.setProductNames("");
                }
                if (saleOrder.getCustomerId() != null) {
                    MapUtils.findAndThen(customerMap, saleOrder.getCustomerId(), supplier -> {
                        if (supplier != null) {
                            saleOrder.setCustomerName(supplier.getName());
                        }
                    });
                }
                String creator = saleOrder.getCreator();
                if (creator != null && !creator.trim().isEmpty()) {
                    try {
                        Long creatorId = Long.parseLong(creator);
                        MapUtils.findAndThen(userMap, creatorId, user -> {
                            if (user != null) {
                                saleOrder.setCreatorName(user.getNickname());
                            }
                        });
                    } catch (NumberFormatException e) {
                        // 忽略格式错误，不设置创建者名称
                    }
                }
            } catch (Exception e) {
                // 记录异常但不抛出，确保至少返回基本数据
                log.error("处理销售订单数据时发生异常，订单ID: {}", saleOrder.getId(), e);
            }
        });
        } catch (Exception e) {
            log.error("构建销售订单VO分页结果失败", e);
            // 如果构建失败，返回空结果而不是抛出异常
            return PageResult.empty(pageResult != null ? pageResult.getTotal() : 0);
        }
    }

}