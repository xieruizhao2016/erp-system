package cn.iocoder.yudao.module.erp.controller.admin.stock.internalin;

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

import cn.iocoder.yudao.module.erp.controller.admin.stock.internalin.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.stock.internalin.ErpStockInternalInDO;
import cn.iocoder.yudao.module.erp.service.stock.internalin.ErpStockInternalInService;

@Tag(name = "管理后台 - 内部入库单")
@RestController
@RequestMapping("/erp/stock-internal-in")
@Validated
public class ErpStockInternalInController {

    @Resource
    private ErpStockInternalInService stockInternalInService;

    @PostMapping("/create")
    @Operation(summary = "创建内部入库单")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:create')")
    public CommonResult<Long> createStockInternalIn(@Valid @RequestBody ErpStockInternalInSaveReqVO createReqVO) {
        return success(stockInternalInService.createStockInternalIn(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新内部入库单")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:update')")
    public CommonResult<Boolean> updateStockInternalIn(@Valid @RequestBody ErpStockInternalInSaveReqVO updateReqVO) {
        stockInternalInService.updateStockInternalIn(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除内部入库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:delete')")
    public CommonResult<Boolean> deleteStockInternalIn(@RequestParam("id") Long id) {
        stockInternalInService.deleteStockInternalIn(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除内部入库单")
                @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:delete')")
    public CommonResult<Boolean> deleteStockInternalInList(@RequestParam("ids") List<Long> ids) {
        stockInternalInService.deleteStockInternalInListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得内部入库单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:query')")
    public CommonResult<ErpStockInternalInRespVO> getStockInternalIn(@RequestParam("id") Long id) {
        ErpStockInternalInDO stockInternalIn = stockInternalInService.getStockInternalIn(id);
        return success(BeanUtils.toBean(stockInternalIn, ErpStockInternalInRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得内部入库单分页")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:query')")
    public CommonResult<PageResult<ErpStockInternalInRespVO>> getStockInternalInPage(@Valid ErpStockInternalInPageReqVO pageReqVO) {
        PageResult<ErpStockInternalInDO> pageResult = stockInternalInService.getStockInternalInPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ErpStockInternalInRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出内部入库单 Excel")
    @PreAuthorize("@ss.hasPermission('erp:stock-internal-in:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportStockInternalInExcel(@Valid ErpStockInternalInPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ErpStockInternalInDO> list = stockInternalInService.getStockInternalInPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "内部入库单.xls", "数据", ErpStockInternalInRespVO.class,
                        BeanUtils.toBean(list, ErpStockInternalInRespVO.class));
    }

}