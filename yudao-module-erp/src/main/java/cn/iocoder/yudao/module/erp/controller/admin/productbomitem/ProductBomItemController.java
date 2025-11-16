package cn.iocoder.yudao.module.erp.controller.admin.productbomitem;

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

import cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbomitem.ProductBomItemDO;
import cn.iocoder.yudao.module.erp.service.productbomitem.ProductBomItemService;

@Tag(name = "管理后台 - ERP BOM明细")
@RestController
@RequestMapping("/erp/product-bom-item")
@Validated
public class ProductBomItemController {

    @Resource
    private ProductBomItemService productBomItemService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP BOM明细")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:create')")
    public CommonResult<Long> createProductBomItem(@Valid @RequestBody ProductBomItemSaveReqVO createReqVO) {
        return success(productBomItemService.createProductBomItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP BOM明细")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:update')")
    public CommonResult<Boolean> updateProductBomItem(@Valid @RequestBody ProductBomItemSaveReqVO updateReqVO) {
        productBomItemService.updateProductBomItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP BOM明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:delete')")
    public CommonResult<Boolean> deleteProductBomItem(@RequestParam("id") Long id) {
        productBomItemService.deleteProductBomItem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP BOM明细")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:delete')")
    public CommonResult<Boolean> deleteProductBomItemList(@RequestParam("ids") List<Long> ids) {
        productBomItemService.deleteProductBomItemListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP BOM明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:query')")
    public CommonResult<ProductBomItemRespVO> getProductBomItem(@RequestParam("id") Long id) {
        ProductBomItemDO productBomItem = productBomItemService.getProductBomItem(id);
        return success(BeanUtils.toBean(productBomItem, ProductBomItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP BOM明细分页")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:query')")
    public CommonResult<PageResult<ProductBomItemRespVO>> getProductBomItemPage(@Valid ProductBomItemPageReqVO pageReqVO) {
        PageResult<ProductBomItemDO> pageResult = productBomItemService.getProductBomItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductBomItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP BOM明细 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-bom-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductBomItemExcel(@Valid ProductBomItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductBomItemDO> list = productBomItemService.getProductBomItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP BOM明细.xls", "数据", ProductBomItemRespVO.class,
                        BeanUtils.toBean(list, ProductBomItemRespVO.class));
    }

}