package cn.iocoder.yudao.module.erp.controller.admin.process;

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

import cn.iocoder.yudao.module.erp.controller.admin.process.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.process.ProcessDO;
import cn.iocoder.yudao.module.erp.service.process.ProcessService;

@Tag(name = "管理后台 - ERP 工序")
@RestController
@RequestMapping("/erp/process")
@Validated
public class ProcessController {

    @Resource
    private ProcessService processService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工序")
    @PreAuthorize("@ss.hasPermission('erp:process:create')")
    public CommonResult<Long> createProcess(@Valid @RequestBody ProcessSaveReqVO createReqVO) {
        return success(processService.createProcess(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工序")
    @PreAuthorize("@ss.hasPermission('erp:process:update')")
    public CommonResult<Boolean> updateProcess(@Valid @RequestBody ProcessSaveReqVO updateReqVO) {
        processService.updateProcess(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工序")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:process:delete')")
    public CommonResult<Boolean> deleteProcess(@RequestParam("id") Long id) {
        processService.deleteProcess(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工序")
    @PreAuthorize("@ss.hasPermission('erp:process:delete')")
    public CommonResult<Boolean> deleteProcessList(@RequestParam("ids") List<Long> ids) {
        processService.deleteProcessListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工序")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:process:query')")
    public CommonResult<ProcessRespVO> getProcess(@RequestParam("id") Long id) {
        ProcessDO process = processService.getProcess(id);
        return success(BeanUtils.toBean(process, ProcessRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工序分页")
    @PreAuthorize("@ss.hasPermission('erp:process:query')")
    public CommonResult<PageResult<ProcessRespVO>> getProcessPage(@Valid ProcessPageReqVO pageReqVO) {
        PageResult<ProcessDO> pageResult = processService.getProcessPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得ERP 工序列表（简单列表）")
    @PreAuthorize("@ss.hasPermission('erp:process:query')")
    public CommonResult<List<ProcessRespVO>> getProcessList() {
        List<ProcessDO> list = processService.getProcessList();
        return success(BeanUtils.toBean(list, ProcessRespVO.class));
    }

    @GetMapping("/generate-no")
    @Operation(summary = "生成默认工序编号")
    @PreAuthorize("@ss.hasPermission('erp:process:create')")
    public CommonResult<String> generateProcessNo() {
        return success(processService.generateProcessNo());
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工序 Excel")
    @PreAuthorize("@ss.hasPermission('erp:process:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProcessExcel(@Valid ProcessPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProcessDO> list = processService.getProcessPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工序.xls", "数据", ProcessRespVO.class,
                        BeanUtils.toBean(list, ProcessRespVO.class));
    }

}



