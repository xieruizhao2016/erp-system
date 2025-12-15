package cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo.*;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement.ErpFinanceProfitStatementDO;
import java.time.LocalDate;
import cn.iocoder.yudao.module.erp.service.finance.profitstatement.ErpFinanceProfitStatementService;
import java.time.LocalDate;

@Tag(name = "管理后台 - 利润表")
@RestController
@RequestMapping("/erp/finance-profit-statement")
@Validated
public class ErpFinanceProfitStatementController {

    @Resource
    private ErpFinanceProfitStatementService financeProfitStatementService;

    @PostMapping("/create")
    @Operation(summary = "创建利润表")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:create')")
    public CommonResult<Long> createFinanceProfitStatement(@Valid @RequestBody ErpFinanceProfitStatementSaveReqVO createReqVO) {
        return success(financeProfitStatementService.createFinanceProfitStatement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新利润表")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:update')")
    public CommonResult<Boolean> updateFinanceProfitStatement(@Valid @RequestBody ErpFinanceProfitStatementSaveReqVO updateReqVO) {
        financeProfitStatementService.updateFinanceProfitStatement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除利润表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:delete')")
    public CommonResult<Boolean> deleteFinanceProfitStatement(@RequestParam("id") Long id) {
        financeProfitStatementService.deleteFinanceProfitStatement(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除利润表")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:delete')")
    public CommonResult<Boolean> deleteFinanceProfitStatementList(@RequestParam("ids") List<Long> ids) {
        financeProfitStatementService.deleteFinanceProfitStatementListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得利润表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:query')")
    public CommonResult<ErpFinanceProfitStatementRespVO> getFinanceProfitStatement(@RequestParam("id") Long id) {
        ErpFinanceProfitStatementDO financeProfitStatement = financeProfitStatementService.getFinanceProfitStatement(id);
        return success(BeanUtils.toBean(financeProfitStatement, ErpFinanceProfitStatementRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得利润表分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:query')")
    public CommonResult<PageResult<ErpFinanceProfitStatementRespVO>> getFinanceProfitStatementPage(@Valid ErpFinanceProfitStatementPageReqVO pageReqVO) {
        PageResult<ErpFinanceProfitStatementDO> pageResult = financeProfitStatementService.getFinanceProfitStatementPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinanceProfitStatementRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出利润表 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceProfitStatementExcel(@Valid ErpFinanceProfitStatementPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceProfitStatementDO> list = financeProfitStatementService.getFinanceProfitStatementPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "利润表.xls", "数据", ErpFinanceProfitStatementRespVO.class,
                        BeanUtils.toBean(list, ErpFinanceProfitStatementRespVO.class));
    }

    @PostMapping("/calculate")
    @Operation(summary = "计算利润表")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:update')")
    @ApiAccessLog(operateType = UPDATE)
    public CommonResult<Boolean> calculateProfitStatement(@RequestParam("periodDate") java.time.LocalDate periodDate) {
        financeProfitStatementService.calculateProfitStatement(periodDate);
        return success(true);
    }

}