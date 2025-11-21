package cn.iocoder.yudao.module.erp.controller.admin.productpackage;

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

import cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productpackage.ProductPackageDO;
import cn.iocoder.yudao.module.erp.service.productpackage.ProductPackageService;

@Tag(name = "管理后台 - ERP 产品包装")
@RestController
@RequestMapping("/erp/product-package")
@Validated
public class ProductPackageController {

    @Resource
    private ProductPackageService productPackageService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 产品包装")
    @PreAuthorize("@ss.hasPermission('erp:product-package:create')")
    public CommonResult<Long> createProductPackage(@Valid @RequestBody ProductPackageSaveReqVO createReqVO) {
        return success(productPackageService.createProductPackage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 产品包装")
    @PreAuthorize("@ss.hasPermission('erp:product-package:update')")
    public CommonResult<Boolean> updateProductPackage(@Valid @RequestBody ProductPackageSaveReqVO updateReqVO) {
        productPackageService.updateProductPackage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 产品包装")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-package:delete')")
    public CommonResult<Boolean> deleteProductPackage(@RequestParam("id") Long id) {
        productPackageService.deleteProductPackage(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @Operation(summary = "批量删除ERP 产品包装")
    @PreAuthorize("@ss.hasPermission('erp:product-package:delete')")
    public CommonResult<Boolean> deleteProductPackageList(@RequestParam("ids") List<Long> ids) {
        productPackageService.deleteProductPackageListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 产品包装")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-package:query')")
    public CommonResult<ProductPackageRespVO> getProductPackage(@RequestParam("id") Long id) {
        ProductPackageDO productPackage = productPackageService.getProductPackage(id);
        return success(BeanUtils.toBean(productPackage, ProductPackageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 产品包装分页")
    @PreAuthorize("@ss.hasPermission('erp:product-package:query')")
    public CommonResult<PageResult<ProductPackageRespVO>> getProductPackagePage(@Valid ProductPackagePageReqVO pageReqVO) {
        PageResult<ProductPackageDO> pageResult = productPackageService.getProductPackagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductPackageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 产品包装 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-package:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductPackageExcel(@Valid ProductPackagePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductPackageDO> list = productPackageService.getProductPackagePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 产品包装.xls", "数据", ProductPackageRespVO.class,
                        BeanUtils.toBean(list, ProductPackageRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品包装精简列表")
    @PreAuthorize("@ss.hasPermission('erp:product-package:query')")
    public CommonResult<List<ProductPackageRespVO>> getProductPackageSimpleList() {
        List<ProductPackageDO> list = productPackageService.getProductPackageSimpleList();
        return success(BeanUtils.toBean(list, ProductPackageRespVO.class));
    }

}

