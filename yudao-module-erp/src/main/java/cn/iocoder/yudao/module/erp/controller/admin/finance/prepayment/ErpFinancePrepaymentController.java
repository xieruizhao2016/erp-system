package cn.iocoder.yudao.module.erp.controller.admin.finance.prepayment;

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

import cn.iocoder.yudao.module.erp.controller.admin.finance.prepayment.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.finance.prepayment.ErpFinancePrepaymentDO;
import cn.iocoder.yudao.module.erp.service.prepayment.ErpFinancePrepaymentService;

@Tag(name = "管理后台 - 预付款")
@RestController
@RequestMapping("/erp/finance-prepayment")
@Validated
public class ErpFinancePrepaymentController {

    @Resource
    private ErpFinancePrepaymentService financePrepaymentService;

    @PostMapping("/create")
    @Operation(summary = "创建预付款")
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:create')")
    public CommonResult<Long> createFinancePrepayment(@Valid @RequestBody ErpFinancePrepaymentSaveReqVO createReqVO) {
        return success(financePrepaymentService.createFinancePrepayment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预付款")
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:update')")
    public CommonResult<Boolean> updateFinancePrepayment(@Valid @RequestBody ErpFinancePrepaymentSaveReqVO updateReqVO) {
        financePrepaymentService.updateFinancePrepayment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预付款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:delete')")
    public CommonResult<Boolean> deleteFinancePrepayment(@RequestParam("id") Long id) {
        financePrepaymentService.deleteFinancePrepayment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除预付款")
                @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:delete')")
    public CommonResult<Boolean> deleteFinancePrepaymentList(@RequestParam("ids") List<Long> ids) {
        financePrepaymentService.deleteFinancePrepaymentListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预付款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:query')")
    public CommonResult<ErpFinancePrepaymentRespVO> getFinancePrepayment(@RequestParam("id") Long id) {
        ErpFinancePrepaymentDO financePrepayment = financePrepaymentService.getFinancePrepayment(id);
        return success(BeanUtils.toBean(financePrepayment, ErpFinancePrepaymentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预付款分页")
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:query')")
    public CommonResult<PageResult<ErpFinancePrepaymentRespVO>> getFinancePrepaymentPage(@Valid ErpFinancePrepaymentPageReqVO pageReqVO) {
        PageResult<ErpFinancePrepaymentDO> pageResult = financePrepaymentService.getFinancePrepaymentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpFinancePrepaymentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预付款 Excel")
    @PreAuthorize("@ss.hasPermission('erp:finance-prepayment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFinancePrepaymentExcel(@Valid ErpFinancePrepaymentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpFinancePrepaymentDO> list = financePrepaymentService.getFinancePrepaymentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "预付款.xls", "数据", ErpFinancePrepaymentRespVO.class,
                        BeanUtils.toBean(list, ErpFinancePrepaymentRespVO.class));
    }

}