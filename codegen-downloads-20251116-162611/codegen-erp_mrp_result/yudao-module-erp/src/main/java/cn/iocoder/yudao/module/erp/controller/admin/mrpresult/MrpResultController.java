package cn.iocoder.yudao.module.erp.controller.admin.mrpresult;

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

import cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpresult.MrpResultDO;
import cn.iocoder.yudao.module.erp.service.mrpresult.MrpResultService;

@Tag(name = "管理后台 - ERP MRP运算结果")
@RestController
@RequestMapping("/erp/mrp-result")
@Validated
public class MrpResultController {

    @Resource
    private MrpResultService mrpResultService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP MRP运算结果")
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:create')")
    public CommonResult<Long> createMrpResult(@Valid @RequestBody MrpResultSaveReqVO createReqVO) {
        return success(mrpResultService.createMrpResult(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP MRP运算结果")
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:update')")
    public CommonResult<Boolean> updateMrpResult(@Valid @RequestBody MrpResultSaveReqVO updateReqVO) {
        mrpResultService.updateMrpResult(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP MRP运算结果")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:delete')")
    public CommonResult<Boolean> deleteMrpResult(@RequestParam("id") Long id) {
        mrpResultService.deleteMrpResult(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP MRP运算结果")
                @PreAuthorize("@ss.hasPermission('erp:mrp-result:delete')")
    public CommonResult<Boolean> deleteMrpResultList(@RequestParam("ids") List<Long> ids) {
        mrpResultService.deleteMrpResultListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP MRP运算结果")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:query')")
    public CommonResult<MrpResultRespVO> getMrpResult(@RequestParam("id") Long id) {
        MrpResultDO mrpResult = mrpResultService.getMrpResult(id);
        return success(BeanUtils.toBean(mrpResult, MrpResultRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP MRP运算结果分页")
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:query')")
    public CommonResult<PageResult<MrpResultRespVO>> getMrpResultPage(@Valid MrpResultPageReqVO pageReqVO) {
        PageResult<MrpResultDO> pageResult = mrpResultService.getMrpResultPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MrpResultRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP MRP运算结果 Excel")
    @PreAuthorize("@ss.hasPermission('erp:mrp-result:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMrpResultExcel(@Valid MrpResultPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MrpResultDO> list = mrpResultService.getMrpResultPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP MRP运算结果.xls", "数据", MrpResultRespVO.class,
                        BeanUtils.toBean(list, MrpResultRespVO.class));
    }

}