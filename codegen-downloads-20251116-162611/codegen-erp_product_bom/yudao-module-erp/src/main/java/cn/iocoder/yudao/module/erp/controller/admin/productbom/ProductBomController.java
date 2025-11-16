package cn.iocoder.yudao.module.erp.controller.admin.productbom;

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

import cn.iocoder.yudao.module.erp.controller.admin.productbom.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productbom.ProductBomDO;
import cn.iocoder.yudao.module.erp.service.productbom.ProductBomService;

@Tag(name = "管理后台 - ERP BOM主")
@RestController
@RequestMapping("/erp/product-bom")
@Validated
public class ProductBomController {

    @Resource
    private ProductBomService productBomService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP BOM主")
    @PreAuthorize("@ss.hasPermission('erp:product-bom:create')")
    public CommonResult<Long> createProductBom(@Valid @RequestBody ProductBomSaveReqVO createReqVO) {
        return success(productBomService.createProductBom(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP BOM主")
    @PreAuthorize("@ss.hasPermission('erp:product-bom:update')")
    public CommonResult<Boolean> updateProductBom(@Valid @RequestBody ProductBomSaveReqVO updateReqVO) {
        productBomService.updateProductBom(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP BOM主")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-bom:delete')")
    public CommonResult<Boolean> deleteProductBom(@RequestParam("id") Long id) {
        productBomService.deleteProductBom(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP BOM主")
                @PreAuthorize("@ss.hasPermission('erp:product-bom:delete')")
    public CommonResult<Boolean> deleteProductBomList(@RequestParam("ids") List<Long> ids) {
        productBomService.deleteProductBomListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP BOM主")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-bom:query')")
    public CommonResult<ProductBomRespVO> getProductBom(@RequestParam("id") Long id) {
        ProductBomDO productBom = productBomService.getProductBom(id);
        return success(BeanUtils.toBean(productBom, ProductBomRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP BOM主分页")
    @PreAuthorize("@ss.hasPermission('erp:product-bom:query')")
    public CommonResult<PageResult<ProductBomRespVO>> getProductBomPage(@Valid ProductBomPageReqVO pageReqVO) {
        PageResult<ProductBomDO> pageResult = productBomService.getProductBomPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductBomRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP BOM主 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-bom:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductBomExcel(@Valid ProductBomPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductBomDO> list = productBomService.getProductBomPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP BOM主.xls", "数据", ProductBomRespVO.class,
                        BeanUtils.toBean(list, ProductBomRespVO.class));
    }

}