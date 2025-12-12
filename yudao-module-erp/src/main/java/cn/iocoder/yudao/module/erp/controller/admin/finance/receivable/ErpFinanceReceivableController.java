package cn.iocoder.yudao.module.erp.controller.admin.finance.receivable;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable.ErpFinanceReceivableDO;
import cn.iocoder.yudao.module.erp.service.receivable.ErpFinanceReceivableService;

@Tag(name = "管理后台 - 应收账款")
@RestController
@RequestMapping("/erp/finance-receivable")
@Validated
public class ErpFinanceReceivableController {

    @Resource
    private ErpFinanceReceivableService financeReceivableService;

    @PostMapping("/create")
    @Operation(summary = "创建应收账款")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:create')")
    public CommonResult<Long> createFinanceReceivable(@Valid @RequestBody ErpFinanceReceivableSaveReqVO createReqVO) {
        return success(financeReceivableService.createFinanceReceivable(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应收账款")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:update')")
    public CommonResult<Boolean> updateFinanceReceivable(@Valid @RequestBody ErpFinanceReceivableSaveReqVO updateReqVO) {
        financeReceivableService.updateFinanceReceivable(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应收账款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:delete')")
    public CommonResult<Boolean> deleteFinanceReceivable(@RequestParam("id") Long id) {
        financeReceivableService.deleteFinanceReceivable(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除应收账款")
                @PreAuthorize("@ss.hasPermission('erp:finance-receivable:delete')")
    public CommonResult<Boolean> deleteFinanceReceivableList(@RequestParam("ids") List<Long> ids) {
        financeReceivableService.deleteFinanceReceivableListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应收账款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:query')")
    public CommonResult<ErpFinanceReceivableRespVO> getFinanceReceivable(@RequestParam("id") Long id) {
        ErpFinanceReceivableDO financeReceivable = financeReceivableService.getFinanceReceivable(id);
        return success(BeanUtils.toBean(financeReceivable, ErpFinanceReceivableRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得应收账款分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:query')")
    public CommonResult<PageResult<ErpFinanceReceivableRespVO>> getFinanceReceivablePage(@Valid ErpFinanceReceivablePageReqVO pageReqVO) {
        PageResult<ErpFinanceReceivableDO> pageResult = financeReceivableService.getFinanceReceivablePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinanceReceivableRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出应收账款 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-receivable:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinanceReceivableExcel(@Valid ErpFinanceReceivablePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinanceReceivableDO> list = financeReceivableService.getFinanceReceivablePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "应收账款.xls", "数据", ErpFinanceReceivableRespVO.class,
                        BeanUtils.toBean(list, ErpFinanceReceivableRespVO.class));
    }

}