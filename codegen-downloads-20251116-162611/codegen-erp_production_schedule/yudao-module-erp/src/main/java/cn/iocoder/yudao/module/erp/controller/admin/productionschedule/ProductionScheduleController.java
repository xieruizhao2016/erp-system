package cn.iocoder.yudao.module.erp.controller.admin.productionschedule;

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

import cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule.ProductionScheduleDO;
import cn.iocoder.yudao.module.erp.service.productionschedule.ProductionScheduleService;

@Tag(name = "管理后台 - ERP 生产排程主")
@RestController
@RequestMapping("/erp/production-schedule")
@Validated
public class ProductionScheduleController {

    @Resource
    private ProductionScheduleService productionScheduleService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 生产排程主")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:create')")
    public CommonResult<Long> createProductionSchedule(@Valid @RequestBody ProductionScheduleSaveReqVO createReqVO) {
        return success(productionScheduleService.createProductionSchedule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 生产排程主")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:update')")
    public CommonResult<Boolean> updateProductionSchedule(@Valid @RequestBody ProductionScheduleSaveReqVO updateReqVO) {
        productionScheduleService.updateProductionSchedule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 生产排程主")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:delete')")
    public CommonResult<Boolean> deleteProductionSchedule(@RequestParam("id") Long id) {
        productionScheduleService.deleteProductionSchedule(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 生产排程主")
                @PreAuthorize("@ss.hasPermission('erp:production-schedule:delete')")
    public CommonResult<Boolean> deleteProductionScheduleList(@RequestParam("ids") List<Long> ids) {
        productionScheduleService.deleteProductionScheduleListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 生产排程主")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:query')")
    public CommonResult<ProductionScheduleRespVO> getProductionSchedule(@RequestParam("id") Long id) {
        ProductionScheduleDO productionSchedule = productionScheduleService.getProductionSchedule(id);
        return success(BeanUtils.toBean(productionSchedule, ProductionScheduleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 生产排程主分页")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:query')")
    public CommonResult<PageResult<ProductionScheduleRespVO>> getProductionSchedulePage(@Valid ProductionSchedulePageReqVO pageReqVO) {
        PageResult<ProductionScheduleDO> pageResult = productionScheduleService.getProductionSchedulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionScheduleRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 生产排程主 Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionScheduleExcel(@Valid ProductionSchedulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionScheduleDO> list = productionScheduleService.getProductionSchedulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 生产排程主.xls", "数据", ProductionScheduleRespVO.class,
                        BeanUtils.toBean(list, ProductionScheduleRespVO.class));
    }

}