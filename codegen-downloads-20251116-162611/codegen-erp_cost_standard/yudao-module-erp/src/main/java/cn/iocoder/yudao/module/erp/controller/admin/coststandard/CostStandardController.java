package cn.iocoder.yudao.module.erp.controller.admin.coststandard;

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

import cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.coststandard.CostStandardDO;
import cn.iocoder.yudao.module.erp.service.coststandard.CostStandardService;

@Tag(name = "管理后台 - ERP 标准成本")
@RestController
@RequestMapping("/erp/cost-standard")
@Validated
public class CostStandardController {

    @Resource
    private CostStandardService costStandardService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 标准成本")
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:create')")
    public CommonResult<Long> createCostStandard(@Valid @RequestBody CostStandardSaveReqVO createReqVO) {
        return success(costStandardService.createCostStandard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 标准成本")
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:update')")
    public CommonResult<Boolean> updateCostStandard(@Valid @RequestBody CostStandardSaveReqVO updateReqVO) {
        costStandardService.updateCostStandard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 标准成本")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:delete')")
    public CommonResult<Boolean> deleteCostStandard(@RequestParam("id") Long id) {
        costStandardService.deleteCostStandard(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 标准成本")
                @PreAuthorize("@ss.hasPermission('erp:cost-standard:delete')")
    public CommonResult<Boolean> deleteCostStandardList(@RequestParam("ids") List<Long> ids) {
        costStandardService.deleteCostStandardListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 标准成本")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:query')")
    public CommonResult<CostStandardRespVO> getCostStandard(@RequestParam("id") Long id) {
        CostStandardDO costStandard = costStandardService.getCostStandard(id);
        return success(BeanUtils.toBean(costStandard, CostStandardRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 标准成本分页")
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:query')")
    public CommonResult<PageResult<CostStandardRespVO>> getCostStandardPage(@Valid CostStandardPageReqVO pageReqVO) {
        PageResult<CostStandardDO> pageResult = costStandardService.getCostStandardPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CostStandardRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 标准成本 Excel")
    @PreAuthorize("@ss.hasPermission('erp:cost-standard:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCostStandardExcel(@Valid CostStandardPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CostStandardDO> list = costStandardService.getCostStandardPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 标准成本.xls", "数据", CostStandardRespVO.class,
                        BeanUtils.toBean(list, CostStandardRespVO.class));
    }

}