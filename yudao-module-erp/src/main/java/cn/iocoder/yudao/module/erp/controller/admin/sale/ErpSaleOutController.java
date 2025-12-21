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
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.out.ErpSaleOutPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.out.ErpSaleOutRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.sale.vo.out.ErpSaleOutSaveReqVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpCustomerDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOutDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.sale.ErpSaleOutItemDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.ErpStockDO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
import cn.iocoder.yudao.module.erp.service.sale.ErpCustomerService;
import cn.iocoder.yudao.module.erp.service.sale.ErpSaleOutService;
import cn.iocoder.yudao.module.erp.service.stock.ErpStockService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMultiMap;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - ERP 销售出库")
@RestController
@RequestMapping("/erp/sale-out")
@Validated
@Slf4j
public class ErpSaleOutController {

    @Resource
    private ErpSaleOutService saleOutService;
    @Resource
    private ErpStockService stockService;
    @Resource
    private ErpProductService productService;
    @Resource
    private ErpCustomerService customerService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建销售出库")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:create')")
    public CommonResult<Long> createSaleOut(@Valid @RequestBody ErpSaleOutSaveReqVO createReqVO) {
        return success(saleOutService.createSaleOut(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售出库")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:update')")
    public CommonResult<Boolean> updateSaleOut(@Valid @RequestBody ErpSaleOutSaveReqVO updateReqVO) {
        saleOutService.updateSaleOut(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新销售出库的状态")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:update-status')")
    public CommonResult<Boolean> updateSaleOutStatus(@RequestParam("id") Long id,
                                                      @RequestParam("status") Integer status) {
        saleOutService.updateSaleOutStatus(id, status);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售出库")
    @Parameter(name = "ids", description = "编号数组", required = true)
    @PreAuthorize("@ss.hasPermission('erp:sale-out:delete')")
    public CommonResult<Boolean> deleteSaleOut(@RequestParam("ids") List<Long> ids) {
        saleOutService.deleteSaleOut(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售出库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:query')")
    public CommonResult<ErpSaleOutRespVO> getSaleOut(@RequestParam("id") Long id) {
        ErpSaleOutDO saleOut = saleOutService.getSaleOut(id);
        if (saleOut == null) {
            return success(null);
        }
        List<ErpSaleOutItemDO> saleOutItemList = saleOutService.getSaleOutItemListByOutId(id);
        Map<Long, ErpProductRespVO> productMap = productService.getProductVOMap(
                convertSet(saleOutItemList, ErpSaleOutItemDO::getProductId));
        return success(BeanUtils.toBean(saleOut, ErpSaleOutRespVO.class, saleOutVO ->
                saleOutVO.setItems(BeanUtils.toBean(saleOutItemList, ErpSaleOutRespVO.Item.class, item -> {
                    ErpStockDO stock = stockService.getStock(item.getProductId(), item.getWarehouseId());
                    item.setStockCount(stock != null ? stock.getCount() : BigDecimal.ZERO);
                    MapUtils.findAndThen(productMap, item.getProductId(), product -> item.setProductName(product.getName())
                            .setProductBarCode(product.getBarCode()).setProductUnitName(product.getUnitName()));
                }))));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售出库分页")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:query')")
    public CommonResult<PageResult<ErpSaleOutRespVO>> getSaleOutPage(@Valid ErpSaleOutPageReqVO pageReqVO) {
        try {
            PageResult<ErpSaleOutDO> pageResult = saleOutService.getSaleOutPage(pageReqVO);
            return success(buildSaleOutVOPageResult(pageResult));
        } catch (Exception e) {
            log.error("获取销售出库分页失败，查询参数: {}", pageReqVO, e);
            throw e;
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售出库 Excel")
    @PreAuthorize("@ss.hasPermission('erp:sale-out:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSaleOutExcel(@Valid ErpSaleOutPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpSaleOutRespVO> list = buildSaleOutVOPageResult(saleOutService.getSaleOutPage(pageReqVO)).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售出库.xls", "数据", ErpSaleOutRespVO.class, list);
    }

    private PageResult<ErpSaleOutRespVO> buildSaleOutVOPageResult(PageResult<ErpSaleOutDO> pageResult) {
        try {
            if (CollUtil.isEmpty(pageResult.getList())) {
                return PageResult.empty(pageResult.getTotal());
            }
            // 1.1 出库项
            Set<Long> outIds = convertSet(pageResult.getList(), ErpSaleOutDO::getId, out -> out.getId() != null);
            List<ErpSaleOutItemDO> saleOutItemList = CollUtil.isEmpty(outIds) ? Collections.emptyList() :
                    saleOutService.getSaleOutItemListByOutIds(outIds);
            if (saleOutItemList == null) {
                saleOutItemList = Collections.emptyList();
            }
            // 过滤掉 outId 为 null 的项，避免 convertMultiMap 出现问题
            List<ErpSaleOutItemDO> validItemList = saleOutItemList.stream()
                    .filter(item -> item != null && item.getOutId() != null)
                    .collect(Collectors.toList());
            Map<Long, List<ErpSaleOutItemDO>> saleOutItemMap = convertMultiMap(validItemList, ErpSaleOutItemDO::getOutId);
            // 1.2 产品信息
            Set<Long> productIds = convertSet(saleOutItemList, ErpSaleOutItemDO::getProductId, item -> item.getProductId() != null);
            Map<Long, ErpProductRespVO> productMap = CollUtil.isEmpty(productIds) ? Collections.emptyMap() :
                    productService.getProductVOMap(productIds);
            // 1.3 客户信息
            Set<Long> customerIds = convertSet(pageResult.getList(), ErpSaleOutDO::getCustomerId, saleOut -> saleOut.getCustomerId() != null);
            Map<Long, ErpCustomerDO> customerMap = CollUtil.isEmpty(customerIds) ? Collections.emptyMap() :
                    customerService.getCustomerMap(customerIds);
            // 1.4 管理员信息
            Set<Long> creatorIds = convertSet(pageResult.getList(), saleOut -> {
                String creator = saleOut.getCreator();
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
            return BeanUtils.toBean(pageResult, ErpSaleOutRespVO.class, saleOut -> {
                try {
                    List<ErpSaleOutItemDO> items = saleOutItemMap.get(saleOut.getId());
                    if (items != null && !items.isEmpty()) {
                        saleOut.setItems(BeanUtils.toBean(items, ErpSaleOutRespVO.Item.class,
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
                        saleOut.setItems(Collections.emptyList());
                    }
                    // 生成产品信息
                    if (saleOut.getItems() != null && !saleOut.getItems().isEmpty()) {
                        saleOut.setProductNames(CollUtil.join(saleOut.getItems(), "，", item -> {
                            return item.getProductName() != null ? item.getProductName() : "";
                        }));
                    } else {
                        saleOut.setProductNames("");
                    }
                    if (saleOut.getCustomerId() != null) {
                        MapUtils.findAndThen(customerMap, saleOut.getCustomerId(), supplier -> {
                            if (supplier != null) {
                                saleOut.setCustomerName(supplier.getName());
                            }
                        });
                    }
                    String creator = saleOut.getCreator();
                    if (creator != null && !creator.trim().isEmpty()) {
                        try {
                            Long creatorId = Long.parseLong(creator);
                            MapUtils.findAndThen(userMap, creatorId, user -> {
                                if (user != null) {
                                    saleOut.setCreatorName(user.getNickname());
                                }
                            });
                        } catch (NumberFormatException e) {
                            // 忽略格式错误，不设置创建者名称
                        }
                    }
                } catch (Exception e) {
                    log.error("处理销售出库数据时发生异常，出库单ID: {}", saleOut.getId(), e);
                }
            });
        } catch (Exception e) {
            log.error("构建销售出库VO分页结果失败", e);
            // 如果构建失败，返回空结果而不是抛出异常
            return PageResult.empty(pageResult != null ? pageResult.getTotal() : 0);
        }
    }

}