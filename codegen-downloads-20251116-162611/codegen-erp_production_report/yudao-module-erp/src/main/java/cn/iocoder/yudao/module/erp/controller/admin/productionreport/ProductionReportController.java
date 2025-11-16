package cn.iocoder.yudao.module.erp.controller.admin.productionreport;

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

import cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionreport.ProductionReportDO;
import cn.iocoder.yudao.module.erp.service.productionreport.ProductionReportService;

@Tag(name = "管理后台 - ERP 生产报表")
@RestController
@RequestMapping("/erp/production-report")
@Validated
public class ProductionReportController {

    @Resource
    private ProductionReportService productionReportService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 生产报表")
    @PreAuthorize("@ss.hasPermission('erp:production-report:create')")
    public CommonResult<Long> createProductionReport(@Valid @RequestBody ProductionReportSaveReqVO createReqVO) {
        return success(productionReportService.createProductionReport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 生产报表")
    @PreAuthorize("@ss.hasPermission('erp:production-report:update')")
    public CommonResult<Boolean> updateProductionReport(@Valid @RequestBody ProductionReportSaveReqVO updateReqVO) {
        productionReportService.updateProductionReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 生产报表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-report:delete')")
    public CommonResult<Boolean> deleteProductionReport(@RequestParam("id") Long id) {
        productionReportService.deleteProductionReport(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 生产报表")
                @PreAuthorize("@ss.hasPermission('erp:production-report:delete')")
    public CommonResult<Boolean> deleteProductionReportList(@RequestParam("ids") List<Long> ids) {
        productionReportService.deleteProductionReportListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 生产报表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-report:query')")
    public CommonResult<ProductionReportRespVO> getProductionReport(@RequestParam("id") Long id) {
        ProductionReportDO productionReport = productionReportService.getProductionReport(id);
        return success(BeanUtils.toBean(productionReport, ProductionReportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 生产报表分页")
    @PreAuthorize("@ss.hasPermission('erp:production-report:query')")
    public CommonResult<PageResult<ProductionReportRespVO>> getProductionReportPage(@Valid ProductionReportPageReqVO pageReqVO) {
        PageResult<ProductionReportDO> pageResult = productionReportService.getProductionReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionReportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 生产报表 Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionReportExcel(@Valid ProductionReportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionReportDO> list = productionReportService.getProductionReportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 生产报表.xls", "数据", ProductionReportRespVO.class,
                        BeanUtils.toBean(list, ProductionReportRespVO.class));
    }

}