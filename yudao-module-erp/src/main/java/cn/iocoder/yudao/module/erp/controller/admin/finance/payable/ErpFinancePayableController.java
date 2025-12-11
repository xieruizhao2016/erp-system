package cn.iocoder.yudao.module.erp.controller.admin.finance-payable;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance-payable.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-payable.ErpFinancePayableDO;
import cn.iocoder.yudao.module.erp.service.payable.ErpFinancePayableService;

@Tag(name = "管理后台 - 应付账款")
@RestController
@RequestMapping("/erp/finance-payable")
@Validated
public class ErpFinancePayableController {

    @Resource
    private ErpFinancePayableService financePayableService;

    @PostMapping("/create")
    @Operation(summary = "创建应付账款")
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:create')")
    public CommonResult<${primaryColumn.javaType}> createFinancePayable(@Valid @RequestBody ErpFinancePayableSaveReqVO createReqVO) {
        return success(financePayableService.createFinancePayable(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新应付账款")
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:update')")
    public CommonResult<Boolean> updateFinancePayable(@Valid @RequestBody ErpFinancePayableSaveReqVO updateReqVO) {
        financePayableService.updateFinancePayable(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除应付账款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:delete')")
    public CommonResult<Boolean> deleteFinancePayable(@RequestParam("id") ${primaryColumn.javaType} id) {
        financePayableService.deleteFinancePayable(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除应付账款")
                @PreAuthorize("@ss.hasPermission('erp:finance-payable:delete')")
    public CommonResult<Boolean> deleteFinancePayableList(@RequestParam("ids") List<${primaryColumn.javaType}> ids) {
        financePayableService.deleteFinancePayableListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得应付账款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:query')")
    public CommonResult<ErpFinancePayableRespVO> getFinancePayable(@RequestParam("id") ${primaryColumn.javaType} id) {
        ErpFinancePayableDO financePayable = financePayableService.getFinancePayable(id);
        return success(BeanUtils.toBean(financePayable, ErpFinancePayableRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得应付账款分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:query')")
    public CommonResult<PageResult<ErpFinancePayableRespVO>> getFinancePayablePage(@Valid ErpFinancePayablePageReqVO pageReqVO) {
        PageResult<ErpFinancePayableDO> pageResult = financePayableService.getFinancePayablePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinancePayableRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出应付账款 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-payable:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePayableExcel(@Valid ErpFinancePayablePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePayableDO> list = financePayableService.getFinancePayablePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "应付账款.xls", "数据", ErpFinancePayableRespVO.class,
                        BeanUtils.toBean(list, ErpFinancePayableRespVO.class));
    }

}