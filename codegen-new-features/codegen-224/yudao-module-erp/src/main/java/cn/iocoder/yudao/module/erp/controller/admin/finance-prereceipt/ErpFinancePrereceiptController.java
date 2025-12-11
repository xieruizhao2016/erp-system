package cn.iocoder.yudao.module.erp.controller.admin.finance-prereceipt;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance-prereceipt.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance-prereceipt.ErpFinancePrereceiptDO;
import cn.iocoder.yudao.module.erp.service.finance-prereceipt.ErpFinancePrereceiptService;

@Tag(name = "管理后台 - 预收款")
@RestController
@RequestMapping("/erp/finance-prereceipt")
@Validated
public class ErpFinancePrereceiptController {

    @Resource
    private ErpFinancePrereceiptService financePrereceiptService;

    @PostMapping("/create")
    @Operation(summary = "创建预收款")
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:create')")
    public CommonResult<${primaryColumn.javaType}> createFinancePrereceipt(@Valid @RequestBody ErpFinancePrereceiptSaveReqVO createReqVO) {
        return success(financePrereceiptService.createFinancePrereceipt(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预收款")
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:update')")
    public CommonResult<Boolean> updateFinancePrereceipt(@Valid @RequestBody ErpFinancePrereceiptSaveReqVO updateReqVO) {
        financePrereceiptService.updateFinancePrereceipt(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预收款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:delete')")
    public CommonResult<Boolean> deleteFinancePrereceipt(@RequestParam("id") ${primaryColumn.javaType} id) {
        financePrereceiptService.deleteFinancePrereceipt(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除预收款")
                @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:delete')")
    public CommonResult<Boolean> deleteFinancePrereceiptList(@RequestParam("ids") List<${primaryColumn.javaType}> ids) {
        financePrereceiptService.deleteFinancePrereceiptListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预收款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:query')")
    public CommonResult<ErpFinancePrereceiptRespVO> getFinancePrereceipt(@RequestParam("id") ${primaryColumn.javaType} id) {
        ErpFinancePrereceiptDO financePrereceipt = financePrereceiptService.getFinancePrereceipt(id);
        return success(BeanUtils.toBean(financePrereceipt, ErpFinancePrereceiptRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预收款分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:query')")
    public CommonResult<PageResult<ErpFinancePrereceiptRespVO>> getFinancePrereceiptPage(@Valid ErpFinancePrereceiptPageReqVO pageReqVO) {
        PageResult<ErpFinancePrereceiptDO> pageResult = financePrereceiptService.getFinancePrereceiptPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinancePrereceiptRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预收款 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-prereceipt:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePrereceiptExcel(@Valid ErpFinancePrereceiptPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePrereceiptDO> list = financePrereceiptService.getFinancePrereceiptPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "预收款.xls", "数据", ErpFinancePrereceiptRespVO.class,
                        BeanUtils.toBean(list, ErpFinancePrereceiptRespVO.class));
    }

}