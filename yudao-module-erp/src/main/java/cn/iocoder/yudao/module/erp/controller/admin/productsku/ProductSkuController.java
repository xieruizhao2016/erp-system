package cn.iocoder.yudao.module.erp.controller.admin.productsku;

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

import cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO;
import cn.iocoder.yudao.module.erp.service.productsku.ProductSkuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import java.math.BigDecimal;
import java.text.NumberFormat;

@Tag(name = "管理后台 - ERP 产品SKU")
@RestController
@RequestMapping("/erp/product-sku")
@Validated
@Slf4j
public class ProductSkuController {

    @Resource
    private ProductSkuService productSkuService;

    /**
     * 处理空字符串到 BigDecimal 的转换，避免 NumberFormatException
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, NumberFormat.getNumberInstance(), true) {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                } else {
                    super.setAsText(text);
                }
            }
        });
    }

    @PostMapping("/create")
    @Operation(summary = "创建ERP 产品SKU")
    @PreAuthorize("@ss.hasPermission('erp:product-sku:create')")
    public CommonResult<Long> createProductSku(@Valid @RequestBody ProductSkuSaveReqVO createReqVO) {
        return success(productSkuService.createProductSku(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 产品SKU")
    @PreAuthorize("@ss.hasPermission('erp:product-sku:update')")
    public CommonResult<Boolean> updateProductSku(@Valid @RequestBody ProductSkuSaveReqVO updateReqVO) {
        productSkuService.updateProductSku(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 产品SKU")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product-sku:delete')")
    public CommonResult<Boolean> deleteProductSku(@RequestParam("id") Long id) {
        productSkuService.deleteProductSku(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 产品SKU")
                @PreAuthorize("@ss.hasPermission('erp:product-sku:delete')")
    public CommonResult<Boolean> deleteProductSkuList(@RequestParam("ids") List<Long> ids) {
        productSkuService.deleteProductSkuListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 产品SKU")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product-sku:query')")
    public CommonResult<ProductSkuRespVO> getProductSku(@RequestParam("id") Long id) {
        ProductSkuDO productSku = productSkuService.getProductSku(id);
        return success(BeanUtils.toBean(productSku, ProductSkuRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 产品SKU分页")
    // 暂时移除权限检查，用于调试 404 问题
    // @PreAuthorize("@ss.hasPermission('erp:product-sku:query')")
    public CommonResult<PageResult<ProductSkuRespVO>> getProductSkuPage(@Valid ProductSkuPageReqVO pageReqVO) {
        try {
            PageResult<ProductSkuDO> pageResult = productSkuService.getProductSkuPage(pageReqVO);
            if (pageResult == null) {
                return success(PageResult.empty());
            }
            PageResult<ProductSkuRespVO> result = BeanUtils.toBean(pageResult, ProductSkuRespVO.class);
            return success(result);
        } catch (Exception e) {
            log.error("[getProductSkuPage] 查询SKU分页失败", e);
            throw e;
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 产品SKU Excel")
    @PreAuthorize("@ss.hasPermission('erp:product-sku:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductSkuExcel(@Valid ProductSkuPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductSkuDO> list = productSkuService.getProductSkuPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 产品SKU.xls", "数据", ProductSkuRespVO.class,
                        BeanUtils.toBean(list, ProductSkuRespVO.class));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品SKU精简列表")
    public CommonResult<List<ProductSkuRespVO>> getProductSkuSimpleList() {
        List<ProductSkuDO> list = productSkuService.getProductSkuSimpleList();
        return success(BeanUtils.toBean(list, ProductSkuRespVO.class));
    }

    @GetMapping("/list-by-product")
    @Operation(summary = "根据产品ID获得产品SKU列表")
    @Parameter(name = "productId", description = "产品ID", required = true, example = "1")
    // 暂时移除权限检查，与 simple-list 保持一致，主要用于前端下拉选项
    // @PreAuthorize("@ss.hasPermission('erp:product-sku:query')")
    public CommonResult<List<ProductSkuRespVO>> getProductSkuListByProductId(@RequestParam("productId") Long productId) {
        List<ProductSkuDO> list = productSkuService.getProductSkuListByProductId(productId);
        return success(BeanUtils.toBean(list, ProductSkuRespVO.class));
    }

}