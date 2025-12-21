package cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet;

import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import javax.annotation.Resource;
import java.time.LocalDate;
import org.springframework.validation.annotation.Validated;
import java.time.LocalDate;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.Operation;
import java.time.LocalDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import javax.validation.*;
import java.time.LocalDate;
import javax.servlet.http.*;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDate;
import java.io.IOException;
import java.time.LocalDate;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import java.time.LocalDate;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import java.time.LocalDate;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import java.time.LocalDate;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import java.time.LocalDate;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import java.time.LocalDate;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

import cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.balancesheet.ErpFinanceBalanceSheetDO;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.service.finance.balancesheet.ErpFinanceBalanceSheetService;
import java.time.LocalDate;

@Tag(name = "管理后台 - 资产负债表")
@RestController
@RequestMapping("/erp/finance-balance-sheet")
@Validated
@Slf4j
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

    @GetMapping("/statistics")
    @Operation(summary = "获取资产负债表统计数据")
    @PreAuthorize("@ss.hasPermission('erp:finance-balance-sheet:query')")
    public CommonResult<ErpFinanceBalanceSheetStatisticsRespVO> getBalanceSheetStatistics() {                                                                   
        try {
            log.info("开始获取资产负债表统计数据");
            ErpFinanceBalanceSheetStatisticsRespVO statistics = financeBalanceSheetService.getBalanceSheetStatistics();                                             
            log.info("成功获取资产负债表统计数据");
            return success(statistics);
        } catch (Exception e) {
            log.error("获取资产负债表统计数据失败", e);
            throw e;
        }
    }

}