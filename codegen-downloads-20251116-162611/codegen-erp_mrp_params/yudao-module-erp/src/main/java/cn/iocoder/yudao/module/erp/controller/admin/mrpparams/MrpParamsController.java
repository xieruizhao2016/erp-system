package cn.iocoder.yudao.module.erp.controller.admin.mrpparams;

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

import cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams.MrpParamsDO;
import cn.iocoder.yudao.module.erp.service.mrpparams.MrpParamsService;

@Tag(name = "管理后台 - ERP MRP参数")
@RestController
@RequestMapping("/erp/mrp-params")
@Validated
public class MrpParamsController {

    @Resource
    private MrpParamsService mrpParamsService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP MRP参数")
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:create')")
    public CommonResult<Long> createMrpParams(@Valid @RequestBody MrpParamsSaveReqVO createReqVO) {
        return success(mrpParamsService.createMrpParams(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP MRP参数")
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:update')")
    public CommonResult<Boolean> updateMrpParams(@Valid @RequestBody MrpParamsSaveReqVO updateReqVO) {
        mrpParamsService.updateMrpParams(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP MRP参数")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:delete')")
    public CommonResult<Boolean> deleteMrpParams(@RequestParam("id") Long id) {
        mrpParamsService.deleteMrpParams(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP MRP参数")
                @PreAuthorize("@ss.hasPermission('erp:mrp-params:delete')")
    public CommonResult<Boolean> deleteMrpParamsList(@RequestParam("ids") List<Long> ids) {
        mrpParamsService.deleteMrpParamsListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP MRP参数")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:query')")
    public CommonResult<MrpParamsRespVO> getMrpParams(@RequestParam("id") Long id) {
        MrpParamsDO mrpParams = mrpParamsService.getMrpParams(id);
        return success(BeanUtils.toBean(mrpParams, MrpParamsRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP MRP参数分页")
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:query')")
    public CommonResult<PageResult<MrpParamsRespVO>> getMrpParamsPage(@Valid MrpParamsPageReqVO pageReqVO) {
        PageResult<MrpParamsDO> pageResult = mrpParamsService.getMrpParamsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MrpParamsRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP MRP参数 Excel")
    @PreAuthorize("@ss.hasPermission('erp:mrp-params:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMrpParamsExcel(@Valid MrpParamsPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MrpParamsDO> list = mrpParamsService.getMrpParamsPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP MRP参数.xls", "数据", MrpParamsRespVO.class,
                        BeanUtils.toBean(list, MrpParamsRespVO.class));
    }

}