package cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import cn.iocoder.yudao.module.erp.service.finance.balancesheet.ErpFinanceBalanceSheetService;

@Tag(name = "管理后台 - 资产负债表")
@RestController
@RequestMapping("/erp/finance-balance-sheet")
@Validated
public class ErpFinanceBalanceSheetController {

    @Resource
    private ErpFinanceBalanceSheetService financeBalanceSheetService;

    @PostMapping("/create")
    @Operation(summary = "创建资产负债表")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:create')")
    public CommonResult<Long> createFinanceBalanceSheet(@Valid @RequestBody ErpFinanceBalanceSheetSaveReqVO createReqVO) {
        return success(financeBalanceSheetService.createFinanceBalanceSheet(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新资产负债表")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:update')")
    public CommonResult<Boolean> updateFinanceBalanceSheet(@Valid @RequestBody ErpFinanceBalanceSheetSaveReqVO updateReqVO) {
        financeBalanceSheetService.updateFinanceBalanceSheet(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除资产负债表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:delete')")
    public CommonResult<Boolean> deleteFinanceBalanceSheet(@RequestParam("id") Long id) {
        financeBalanceSheetService.deleteFinanceBalanceSheet(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除资产负债表")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:delete')")
    public CommonResult<Boolean> deleteFinanceBalanceSheetList(@RequestParam("ids") List<Long> ids) {
        financeBalanceSheetService.deleteFinanceBalanceSheetListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得资产负债表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:query')")
    public CommonResult<ErpFinanceBalanceSheetRespVO> getFinanceBalanceSheet(@RequestParam("id") Long id) {
        ErpFinanceBalanceSheetDO financeBalanceSheet = financeBalanceSheetService.getFinanceBalanceSheet(id);
        return success(BeanUtils.toBean(financeBalanceSheet, ErpFinanceBalanceSheetRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得资产负债表分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:query')")
    public CommonResult<PageResult<ErpFinanceBalanceSheetRespVO>> getFinanceBalanceSheetPage(@Valid ErpFinanceBalanceSheetPageReqVO pageReqVO) {
        PageResult<ErpFinanceBalanceSheetDO> pageResult = financeBalanceSheetService.getFinanceBalanceSheetPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinanceBalanceSheetRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出资产负债表 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceBalanceSheetExcel(@Valid ErpFinanceBalanceSheetPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceBalanceSheetDO> list = financeBalanceSheetService.getFinanceBalanceSheetPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "资产负债表.xls", "数据", ErpFinanceBalanceSheetRespVO.class,
                        BeanUtils.toBean(list, ErpFinanceBalanceSheetRespVO.class));
    }

    @PostMapping("/calculate")
    @Operation(summary = "计算资产负债表")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> calculateBalanceSheet(@RequestParam("periodDate") java.time.LocalDate periodDate) {
        financeBalanceSheetService.calculateBalanceSheet(periodDate);
        return success(true);
    }

}