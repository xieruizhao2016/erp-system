package cn.iocoder.yudao.module.erp.controller.admin.productionorder;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionorder.ProductionOrderDO;
import cn.iocoder.yudao.module.erp.service.productionorder.ProductionOrderService;

@Tag(name = "管理后台 - ERP 生产订单 DO")
@RestController
@RequestMapping("/erp/production-order")
@Validated
public class ProductionOrderController {

    @Resource
    private ProductionOrderService productionOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 生产订单 DO")
    @PreAuthorize("@ss.hasPermission('erp:production-order:create')")
    public CommonResult<Long> createProductionOrder(@Valid @RequestBody ProductionOrderSaveReqVO createReqVO) {
        return success(productionOrderService.createProductionOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 生产订单 DO")
    @PreAuthorize("@ss.hasPermission('erp:production-order:update')")
    public CommonResult<Boolean> updateProductionOrder(@Valid @RequestBody ProductionOrderSaveReqVO updateReqVO) {
        productionOrderService.updateProductionOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 生产订单 DO")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-order:delete')")
    public CommonResult<Boolean> deleteProductionOrder(@RequestParam("id") Long id) {
        productionOrderService.deleteProductionOrder(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 生产订单 DO")
                @PreAuthorize("@ss.hasPermission('erp:production-order:delete')")
    public CommonResult<Boolean> deleteProductionOrderList(@RequestParam("ids") List<Long> ids) {
        productionOrderService.deleteProductionOrderListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 生产订单 DO")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-order:query')")
    public CommonResult<ProductionOrderRespVO> getProductionOrder(@RequestParam("id") Long id) {
        ProductionOrderDO productionOrder = productionOrderService.getProductionOrder(id);
        return success(BeanUtils.toBean(productionOrder, ProductionOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 生产订单 DO分页")
    @PreAuthorize("@ss.hasPermission('erp:production-order:query')")
    public CommonResult<PageResult<ProductionOrderRespVO>> getProductionOrderPage(@Valid ProductionOrderPageReqVO pageReqVO) {
        PageResult<ProductionOrderDO> pageResult = productionOrderService.getProductionOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 生产订单 DO Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionOrderExcel(@Valid ProductionOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionOrderDO> list = productionOrderService.getProductionOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 生产订单 DO.xls", "数据", ProductionOrderRespVO.class,
                        BeanUtils.toBean(list, ProductionOrderRespVO.class));
    }

}