package cn.iocoder.yudao.module.erp.controller.admin.workorderprogress;

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

import cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress.WorkOrderProgressDO;
import cn.iocoder.yudao.module.erp.service.workorderprogress.WorkOrderProgressService;

@Tag(name = "管理后台 - ERP 工单进度")
@RestController
@RequestMapping("/erp/work-order-progress")
@Validated
public class WorkOrderProgressController {

    @Resource
    private WorkOrderProgressService workOrderProgressService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工单进度")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:create')")
    public CommonResult<Long> createWorkOrderProgress(@Valid @RequestBody WorkOrderProgressSaveReqVO createReqVO) {
        return success(workOrderProgressService.createWorkOrderProgress(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工单进度")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:update')")
    public CommonResult<Boolean> updateWorkOrderProgress(@Valid @RequestBody WorkOrderProgressSaveReqVO updateReqVO) {
        workOrderProgressService.updateWorkOrderProgress(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工单进度")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:delete')")
    public CommonResult<Boolean> deleteWorkOrderProgress(@RequestParam("id") Long id) {
        workOrderProgressService.deleteWorkOrderProgress(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工单进度")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:delete')")
    public CommonResult<Boolean> deleteWorkOrderProgressList(@RequestParam("ids") List<Long> ids) {
        workOrderProgressService.deleteWorkOrderProgressListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工单进度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:query')")
    public CommonResult<WorkOrderProgressRespVO> getWorkOrderProgress(@RequestParam("id") Long id) {
        WorkOrderProgressDO workOrderProgress = workOrderProgressService.getWorkOrderProgress(id);
        return success(BeanUtils.toBean(workOrderProgress, WorkOrderProgressRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工单进度分页")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:query')")
    public CommonResult<PageResult<WorkOrderProgressRespVO>> getWorkOrderProgressPage(@Valid WorkOrderProgressPageReqVO pageReqVO) {
        PageResult<WorkOrderProgressDO> pageResult = workOrderProgressService.getWorkOrderProgressPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkOrderProgressRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工单进度 Excel")
    @PreAuthorize("@ss.hasPermission('erp:work-order-progress:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWorkOrderProgressExcel(@Valid WorkOrderProgressPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WorkOrderProgressDO> list = workOrderProgressService.getWorkOrderProgressPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工单进度.xls", "数据", WorkOrderProgressRespVO.class,
                        BeanUtils.toBean(list, WorkOrderProgressRespVO.class));
    }

}