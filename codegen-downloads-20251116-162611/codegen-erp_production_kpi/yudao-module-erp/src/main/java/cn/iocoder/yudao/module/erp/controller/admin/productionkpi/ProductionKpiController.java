package cn.iocoder.yudao.module.erp.controller.admin.productionkpi;

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

import cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionkpi.ProductionKpiDO;
import cn.iocoder.yudao.module.erp.service.productionkpi.ProductionKpiService;

@Tag(name = "管理后台 - ERP 生产KPI")
@RestController
@RequestMapping("/erp/production-kpi")
@Validated
public class ProductionKpiController {

    @Resource
    private ProductionKpiService productionKpiService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 生产KPI")
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:create')")
    public CommonResult<Long> createProductionKpi(@Valid @RequestBody ProductionKpiSaveReqVO createReqVO) {
        return success(productionKpiService.createProductionKpi(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 生产KPI")
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:update')")
    public CommonResult<Boolean> updateProductionKpi(@Valid @RequestBody ProductionKpiSaveReqVO updateReqVO) {
        productionKpiService.updateProductionKpi(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 生产KPI")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:delete')")
    public CommonResult<Boolean> deleteProductionKpi(@RequestParam("id") Long id) {
        productionKpiService.deleteProductionKpi(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 生产KPI")
                @PreAuthorize("@ss.hasPermission('erp:production-kpi:delete')")
    public CommonResult<Boolean> deleteProductionKpiList(@RequestParam("ids") List<Long> ids) {
        productionKpiService.deleteProductionKpiListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 生产KPI")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:query')")
    public CommonResult<ProductionKpiRespVO> getProductionKpi(@RequestParam("id") Long id) {
        ProductionKpiDO productionKpi = productionKpiService.getProductionKpi(id);
        return success(BeanUtils.toBean(productionKpi, ProductionKpiRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 生产KPI分页")
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:query')")
    public CommonResult<PageResult<ProductionKpiRespVO>> getProductionKpiPage(@Valid ProductionKpiPageReqVO pageReqVO) {
        PageResult<ProductionKpiDO> pageResult = productionKpiService.getProductionKpiPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionKpiRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 生产KPI Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-kpi:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionKpiExcel(@Valid ProductionKpiPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionKpiDO> list = productionKpiService.getProductionKpiPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 生产KPI.xls", "数据", ProductionKpiRespVO.class,
                        BeanUtils.toBean(list, ProductionKpiRespVO.class));
    }

}