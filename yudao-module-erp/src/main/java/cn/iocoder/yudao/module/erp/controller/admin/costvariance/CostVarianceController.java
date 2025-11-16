package cn.iocoder.yudao.module.erp.controller.admin.costvariance;

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

import cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costvariance.CostVarianceDO;
import cn.iocoder.yudao.module.erp.service.costvariance.CostVarianceService;

@Tag(name = "管理后台 - ERP 成本差异分析")
@RestController
@RequestMapping("/erp/cost-variance")
@Validated
public class CostVarianceController {

    @Resource
    private CostVarianceService costVarianceService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 成本差异分析")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:create')")
    public CommonResult<Long> createCostVariance(@Valid @RequestBody CostVarianceSaveReqVO createReqVO) {
        return success(costVarianceService.createCostVariance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 成本差异分析")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:update')")
    public CommonResult<Boolean> updateCostVariance(@Valid @RequestBody CostVarianceSaveReqVO updateReqVO) {
        costVarianceService.updateCostVariance(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 成本差异分析")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:delete')")
    public CommonResult<Boolean> deleteCostVariance(@RequestParam("id") Long id) {
        costVarianceService.deleteCostVariance(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 成本差异分析")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:delete')")
    public CommonResult<Boolean> deleteCostVarianceList(@RequestParam("ids") List<Long> ids) {
        costVarianceService.deleteCostVarianceListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 成本差异分析")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:query')")
    public CommonResult<CostVarianceRespVO> getCostVariance(@RequestParam("id") Long id) {
        CostVarianceDO costVariance = costVarianceService.getCostVariance(id);
        return success(BeanUtils.toBean(costVariance, CostVarianceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 成本差异分析分页")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:query')")
    public CommonResult<PageResult<CostVarianceRespVO>> getCostVariancePage(@Valid CostVariancePageReqVO pageReqVO) {
        PageResult<CostVarianceDO> pageResult = costVarianceService.getCostVariancePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CostVarianceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 成本差异分析 Excel")
    @PreAuthorize("@ss.hasPermission('erp:cost-variance:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCostVarianceExcel(@Valid CostVariancePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CostVarianceDO> list = costVarianceService.getCostVariancePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 成本差异分析.xls", "数据", CostVarianceRespVO.class,
                        BeanUtils.toBean(list, CostVarianceRespVO.class));
    }

}