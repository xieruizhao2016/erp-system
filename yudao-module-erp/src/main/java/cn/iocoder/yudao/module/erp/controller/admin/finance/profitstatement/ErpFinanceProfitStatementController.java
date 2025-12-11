package cn.iocoder.yudao.module.erp.controller.admin.finance-profit-statement;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance-profit-statement.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-profit-statement.ErpFinanceProfitStatementDO;
import cn.iocoder.yudao.module.erp.service.profitstatement.ErpFinanceProfitStatementService;

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
    public CommonResult<${primaryColumn.javaType}> createFinanceProfitStatement(@Valid @RequestBody ErpFinanceProfitStatementSaveReqVO createReqVO) {
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
    public CommonResult<Boolean> deleteFinanceProfitStatement(@RequestParam("id") ${primaryColumn.javaType} id) {
        financeProfitStatementService.deleteFinanceProfitStatement(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除利润表")
                @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:delete')")
    public CommonResult<Boolean> deleteFinanceProfitStatementList(@RequestParam("ids") List<${primaryColumn.javaType}> ids) {
        financeProfitStatementService.deleteFinanceProfitStatementListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得利润表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-profit-statement:query')")
    public CommonResult<ErpFinanceProfitStatementRespVO> getFinanceProfitStatement(@RequestParam("id") ${primaryColumn.javaType} id) {
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

}