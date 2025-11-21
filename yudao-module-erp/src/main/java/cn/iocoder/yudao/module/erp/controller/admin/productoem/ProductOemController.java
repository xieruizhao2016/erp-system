package cn.iocoder.yudao.module.erp.controller.admin.productoem;

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

import cn.iocoder.yudao.module.erp.controller.admin.productoem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productoem.ProductOemDO;
import cn.iocoder.yudao.module.erp.service.productoem.ProductOemService;

@Tag(name = "管理后台 - ERP 产品OEM")
@RestController
@RequestMapping("/erp/product-oem")
@Validated
public class ProductOemController {

    @Resource
    private ProductOemService productOemService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 产品OEM")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:create')")
    public CommonResult<Long> createProductOem(@Valid @RequestBody ProductOemSaveReqVO createReqVO) {
        return success(productOemService.createProductOem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 产品OEM")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:update')")
    public CommonResult<Boolean> updateProductOem(@Valid @RequestBody ProductOemSaveReqVO updateReqVO) {
        productOemService.updateProductOem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 产品OEM")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-oem:delete')")
    public CommonResult<Boolean> deleteProductOem(@RequestParam("id") Long id) {
        productOemService.deleteProductOem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号列表", required = true)
    @Operation(summary = "批量删除ERP 产品OEM")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:delete')")
    public CommonResult<Boolean> deleteProductOemList(@RequestParam("ids") List<Long> ids) {
        productOemService.deleteProductOemListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 产品OEM")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:query')")
    public CommonResult<ProductOemRespVO> getProductOem(@RequestParam("id") Long id) {
        ProductOemDO productOem = productOemService.getProductOem(id);
        return success(BeanUtils.toBean(productOem, ProductOemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 产品OEM分页")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:query')")
    public CommonResult<PageResult<ProductOemRespVO>> getProductOemPage(@Valid ProductOemPageReqVO pageReqVO) {
        PageResult<ProductOemDO> pageResult = productOemService.getProductOemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductOemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 产品OEM Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductOemExcel(@Valid ProductOemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductOemDO> list = productOemService.getProductOemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 产品OEM.xls", "数据", ProductOemRespVO.class,
                        BeanUtils.toBean(list, ProductOemRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品OEM精简列表")
    @PreAuthorize("@ss.hasPermission('erp:product-oem:query')")
    public CommonResult<List<ProductOemRespVO>> getProductOemSimpleList() {
        List<ProductOemDO> list = productOemService.getProductOemSimpleList();
        return success(BeanUtils.toBean(list, ProductOemRespVO.class));
    }

}

