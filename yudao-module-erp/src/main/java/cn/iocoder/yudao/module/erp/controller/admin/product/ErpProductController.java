package cn.iocoder.yudao.module.erp.controller.admin.product;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import lombok.extern.slf4j.Slf4j;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductSaveReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductImportReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductImportRespVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ErpProductImportExcelVO;
import cn.iocoder.yudao.module.erp.dal.dataobject.product.ErpProductDO;
import cn.iocoder.yudao.module.erp.service.product.ErpProductService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cn.hutool.core.collection.CollUtil;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

@Slf4j
@Tag(name = "管理后台 - ERP 产品")
@RestController
@RequestMapping("/erp/product")
@Validated
public class ErpProductController {

    @Resource
    private ErpProductService productService;
    @Resource
    private cn.iocoder.yudao.module.erp.service.productsku.ProductSkuService productSkuService;

    @PostMapping("/create")
    @Operation(summary = "创建产品")
    @PreAuthorize("@ss.hasPermission('erp:product:create')")
    public CommonResult<Long> createProduct(@Valid @RequestBody ProductSaveReqVO createReqVO) {
        log.info("创建产品: productName={}, skuIds={}, skuIdsSize={}", 
                createReqVO.getName(), 
                createReqVO.getSkuIds(), 
                createReqVO.getSkuIds() != null ? createReqVO.getSkuIds().size() : 0);
        return success(productService.createProduct(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新产品")
    @PreAuthorize("@ss.hasPermission('erp:product:update')")
    public CommonResult<Boolean> updateProduct(@Valid @RequestBody ProductSaveReqVO updateReqVO) {
        log.info("更新产品: productId={}, skuIds={}, skuIdsSize={}", 
                updateReqVO.getId(), 
                updateReqVO.getSkuIds(), 
                updateReqVO.getSkuIds() != null ? updateReqVO.getSkuIds().size() : 0);
        productService.updateProduct(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:product:delete')")
    public CommonResult<Boolean> deleteProduct(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<ErpProductRespVO> getProduct(@RequestParam("id") Long id) {
        ErpProductDO product = productService.getProduct(id);
        if (product == null) {
            return success(null);
        }
        // 使用Service的方法构建VO，确保所有字段都正确填充
        List<ErpProductRespVO> productVOList = productService.getProductVOList(Collections.singletonList(id));
        ErpProductRespVO productVO = CollUtil.isNotEmpty(productVOList) ? productVOList.get(0) : null;
        // 编辑产品时需要显示所有状态的SKU（包括禁用的），使用 getProductSkuListByProductIdAll
        if (productVO != null) {
            List<cn.iocoder.yudao.module.erp.dal.dataobject.productsku.ProductSkuDO> skuList = 
                    productSkuService.getProductSkuListByProductIdAll(id);
            if (skuList != null && !skuList.isEmpty()) {
                productVO.setSkuList(BeanUtils.toBean(skuList, 
                        cn.iocoder.yudao.module.erp.controller.admin.productsku.vo.ProductSkuRespVO.class));
            }
        }
        return success(productVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得产品分页")
    @PreAuthorize("@ss.hasPermission('erp:product:query')")
    public CommonResult<PageResult<ErpProductRespVO>> getProductPage(@Valid ErpProductPageReqVO pageReqVO) {
        return success(productService.getProductVOPage(pageReqVO));
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得产品精简列表", description = "只包含被开启的产品，主要用于前端的下拉选项")
    public CommonResult<List<ErpProductRespVO>> getProductSimpleList() {
        List<ErpProductRespVO> list = productService.getProductVOListByStatus(CommonStatusEnum.ENABLE.getStatus());
        return success(convertList(list, product -> new ErpProductRespVO().setId(product.getId())
                .setName(product.getName()).setBarCode(product.getBarCode())
                .setCategoryId(product.getCategoryId()).setCategoryName(product.getCategoryName())
                .setUnitId(product.getUnitId()).setUnitName(product.getUnitName())
                .setStandard(product.getStandard()) // 添加规格字段
                .setPackageId(product.getPackageId()).setPackageCode(product.getPackageCode())
                .setOemId(product.getOemId()).setOemCode(product.getOemCode())
                .setPurchasePrice(product.getPurchasePrice()).setSalePrice(product.getSalePrice()).setMinPrice(product.getMinPrice())));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出产品 Excel")
    @PreAuthorize("@ss.hasPermission('erp:product:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductExcel(@Valid ErpProductPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ErpProductRespVO> pageResult = productService.getProductVOPage(pageReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "产品.xls", "数据", ErpProductRespVO.class,
                pageResult.getList());
    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入产品模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<ErpProductImportExcelVO> list = Arrays.asList(
                ErpProductImportExcelVO.builder()
                        .name("示例产品1")
                        .barCode("P001")
                        .categoryName("电子产品")
                        .unitName("个")
                        .packageName("")
                        .oemName("")
                        .status(0)
                        .standard("标准规格")
                        .remark("示例备注")
                        .expiryDay(365)
                        .weight(java.math.BigDecimal.valueOf(1.5))
                        .purchasePrice(java.math.BigDecimal.valueOf(100.00))
                        .salePrice(java.math.BigDecimal.valueOf(150.00))
                        .minPrice(java.math.BigDecimal.valueOf(120.00))
                        .skuCodes("SKU001;SKU002")
                        .build(),
                ErpProductImportExcelVO.builder()
                        .name("示例产品2")
                        .barCode("P002")
                        .categoryName("电子产品")
                        .unitName("个")
                        .packageName("")
                        .oemName("")
                        .status(0)
                        .standard("标准规格")
                        .remark("示例备注")
                        .expiryDay(365)
                        .weight(java.math.BigDecimal.valueOf(2.0))
                        .purchasePrice(java.math.BigDecimal.valueOf(200.00))
                        .salePrice(java.math.BigDecimal.valueOf(300.00))
                        .minPrice(java.math.BigDecimal.valueOf(250.00))
                        .skuCodes("")
                        .build()
        );
        // 输出
        ExcelUtils.write(response, "产品导入模板.xls", "产品列表", ErpProductImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入产品")
    @PreAuthorize("@ss.hasPermission('erp:product:import')")
    public CommonResult<ErpProductImportRespVO> importExcel(@Valid ErpProductImportReqVO importReqVO)
            throws Exception {
        List<ErpProductImportExcelVO> list = ExcelUtils.read(importReqVO.getFile(), ErpProductImportExcelVO.class);
        return success(productService.importProductList(list, importReqVO));
    }

}