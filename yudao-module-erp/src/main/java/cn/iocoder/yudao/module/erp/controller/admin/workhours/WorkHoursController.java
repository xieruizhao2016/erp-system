package cn.iocoder.yudao.module.erp.controller.admin.workhours;

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

import cn.iocoder.yudao.module.erp.controller.admin.workhours.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workhours.WorkHoursDO;
import cn.iocoder.yudao.module.erp.service.workhours.WorkHoursService;

@Tag(name = "管理后台 - ERP 工时统计")
@RestController
@RequestMapping("/erp/work-hours")
@Validated
public class WorkHoursController {

    @Resource
    private WorkHoursService workHoursService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工时统计")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:create')")
    public CommonResult<Long> createWorkHours(@Valid @RequestBody WorkHoursSaveReqVO createReqVO) {
        return success(workHoursService.createWorkHours(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工时统计")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:update')")
    public CommonResult<Boolean> updateWorkHours(@Valid @RequestBody WorkHoursSaveReqVO updateReqVO) {
        workHoursService.updateWorkHours(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工时统计")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:work-hours:delete')")
    public CommonResult<Boolean> deleteWorkHours(@RequestParam("id") Long id) {
        workHoursService.deleteWorkHours(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工时统计")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:delete')")
    public CommonResult<Boolean> deleteWorkHoursList(@RequestParam("ids") List<Long> ids) {
        workHoursService.deleteWorkHoursListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工时统计")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:query')")
    public CommonResult<WorkHoursRespVO> getWorkHours(@RequestParam("id") Long id) {
        WorkHoursDO workHours = workHoursService.getWorkHours(id);
        return success(BeanUtils.toBean(workHours, WorkHoursRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工时统计分页")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:query')")
    public CommonResult<PageResult<WorkHoursRespVO>> getWorkHoursPage(@Valid WorkHoursPageReqVO pageReqVO) {
        PageResult<WorkHoursDO> pageResult = workHoursService.getWorkHoursPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkHoursRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工时统计 Excel")
    @PreAuthorize("@ss.hasPermission('erp:work-hours:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWorkHoursExcel(@Valid WorkHoursPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WorkHoursDO> list = workHoursService.getWorkHoursPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工时统计.xls", "数据", WorkHoursRespVO.class,
                        BeanUtils.toBean(list, WorkHoursRespVO.class));
    }

}