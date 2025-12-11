package cn.iocoder.yudao.module.erp.controller.admin.stock-internal-out;

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

import cn.iocoder.yudao.module.erp.controller.admin.stock-internal-out.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock-internal-out.ErpStockInternalOutDO;
import cn.iocoder.yudao.module.erp.service.stock-internal-out.ErpStockInternalOutService;

@Tag(name = "管理后台 - 内部出库单")
@RestController
@RequestMapping("/erp/stock-internal-out")
@Validated
public class ErpStockInternalOutController {

    @Resource
    private ErpStockInternalOutService stockInternalOutService;

    @PostMapping("/create")
    @Operation(summary = "创建内部出库单")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:create')")
    public CommonResult<${primaryColumn.javaType}> createStockInternalOut(@Valid @RequestBody ErpStockInternalOutSaveReqVO createReqVO) {
        return success(stockInternalOutService.createStockInternalOut(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新内部出库单")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:update')")
    public CommonResult<Boolean> updateStockInternalOut(@Valid @RequestBody ErpStockInternalOutSaveReqVO updateReqVO) {
        stockInternalOutService.updateStockInternalOut(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除内部出库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:delete')")
    public CommonResult<Boolean> deleteStockInternalOut(@RequestParam("id") ${primaryColumn.javaType} id) {
        stockInternalOutService.deleteStockInternalOut(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除内部出库单")
                @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:delete')")
    public CommonResult<Boolean> deleteStockInternalOutList(@RequestParam("ids") List<${primaryColumn.javaType}> ids) {
        stockInternalOutService.deleteStockInternalOutListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得内部出库单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:query')")
    public CommonResult<ErpStockInternalOutRespVO> getStockInternalOut(@RequestParam("id") ${primaryColumn.javaType} id) {
        ErpStockInternalOutDO stockInternalOut = stockInternalOutService.getStockInternalOut(id);
        return success(BeanUtils.toBean(stockInternalOut, ErpStockInternalOutRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得内部出库单分页")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:query')")
    public CommonResult<PageResult<ErpStockInternalOutRespVO>> getStockInternalOutPage(@Valid ErpStockInternalOutPageReqVO pageReqVO) {
        PageResult<ErpStockInternalOutDO> pageResult = stockInternalOutService.getStockInternalOutPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpStockInternalOutRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出内部出库单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-out:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportStockInternalOutExcel(@Valid ErpStockInternalOutPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpStockInternalOutDO> list = stockInternalOutService.getStockInternalOutPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "内部出库单.xls", "数据", ErpStockInternalOutRespVO.class,
                        BeanUtils.toBean(list, ErpStockInternalOutRespVO.class));
    }

}