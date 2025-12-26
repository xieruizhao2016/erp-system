package cn.iocoder.yudao.module.erp.controller.admin.workcenter;

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

import cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.workcenter.WorkCenterDO;
import cn.iocoder.yudao.module.erp.service.workcenter.WorkCenterService;

@Tag(name = "管理后台 - ERP 工作中心")
@RestController
@RequestMapping("/erp/work-center")
@Validated
public class WorkCenterController {

    @Resource
    private WorkCenterService workCenterService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工作中心")
    @PreAuthorize("@ss.hasPermission('erp:work-center:create')")
    public CommonResult<Long> createWorkCenter(@Valid @RequestBody WorkCenterSaveReqVO createReqVO) {
        return success(workCenterService.createWorkCenter(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工作中心")
    @PreAuthorize("@ss.hasPermission('erp:work-center:update')")
    public CommonResult<Boolean> updateWorkCenter(@Valid @RequestBody WorkCenterSaveReqVO updateReqVO) {
        workCenterService.updateWorkCenter(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工作中心")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:work-center:delete')")
    public CommonResult<Boolean> deleteWorkCenter(@RequestParam("id") Long id) {
        workCenterService.deleteWorkCenter(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工作中心")
    @PreAuthorize("@ss.hasPermission('erp:work-center:delete')")
    public CommonResult<Boolean> deleteWorkCenterList(@RequestParam("ids") List<Long> ids) {
        workCenterService.deleteWorkCenterListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工作中心")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:work-center:query')")
    public CommonResult<WorkCenterRespVO> getWorkCenter(@RequestParam("id") Long id) {
        WorkCenterDO workCenter = workCenterService.getWorkCenter(id);
        return success(BeanUtils.toBean(workCenter, WorkCenterRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工作中心分页")
    @PreAuthorize("@ss.hasPermission('erp:work-center:query')")
    public CommonResult<PageResult<WorkCenterRespVO>> getWorkCenterPage(@Valid WorkCenterPageReqVO pageReqVO) {
        PageResult<WorkCenterDO> pageResult = workCenterService.getWorkCenterPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkCenterRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得ERP 工作中心列表（简单列表）")
    @PreAuthorize("@ss.hasPermission('erp:work-center:query')")
    public CommonResult<List<WorkCenterRespVO>> getWorkCenterList() {
        List<WorkCenterDO> list = workCenterService.getWorkCenterList();
        return success(BeanUtils.toBean(list, WorkCenterRespVO.class));
    }

    @GetMapping("/generate-no")
    @Operation(summary = "生成工作中心编号")
    @PreAuthorize("@ss.hasPermission('erp:work-center:create')")
    public CommonResult<String> generateWorkCenterNo() {
        String workCenterNo = workCenterService.generateWorkCenterNo();
        return success(workCenterNo);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工作中心 Excel")
    @PreAuthorize("@ss.hasPermission('erp:work-center:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWorkCenterExcel(@Valid WorkCenterPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WorkCenterDO> list = workCenterService.getWorkCenterPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工作中心.xls", "数据", WorkCenterRespVO.class,
                BeanUtils.toBean(list, WorkCenterRespVO.class));
    }

}

