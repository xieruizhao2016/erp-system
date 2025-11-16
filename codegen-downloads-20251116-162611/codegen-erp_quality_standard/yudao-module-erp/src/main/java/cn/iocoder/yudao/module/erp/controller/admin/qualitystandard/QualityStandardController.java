package cn.iocoder.yudao.module.erp.controller.admin.qualitystandard;

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

import cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualitystandard.QualityStandardDO;
import cn.iocoder.yudao.module.erp.service.qualitystandard.QualityStandardService;

@Tag(name = "管理后台 - ERP 质检标准")
@RestController
@RequestMapping("/erp/quality-standard")
@Validated
public class QualityStandardController {

    @Resource
    private QualityStandardService qualityStandardService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 质检标准")
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:create')")
    public CommonResult<Long> createQualityStandard(@Valid @RequestBody QualityStandardSaveReqVO createReqVO) {
        return success(qualityStandardService.createQualityStandard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 质检标准")
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:update')")
    public CommonResult<Boolean> updateQualityStandard(@Valid @RequestBody QualityStandardSaveReqVO updateReqVO) {
        qualityStandardService.updateQualityStandard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 质检标准")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:delete')")
    public CommonResult<Boolean> deleteQualityStandard(@RequestParam("id") Long id) {
        qualityStandardService.deleteQualityStandard(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 质检标准")
                @PreAuthorize("@ss.hasPermission('erp:quality-standard:delete')")
    public CommonResult<Boolean> deleteQualityStandardList(@RequestParam("ids") List<Long> ids) {
        qualityStandardService.deleteQualityStandardListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 质检标准")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:query')")
    public CommonResult<QualityStandardRespVO> getQualityStandard(@RequestParam("id") Long id) {
        QualityStandardDO qualityStandard = qualityStandardService.getQualityStandard(id);
        return success(BeanUtils.toBean(qualityStandard, QualityStandardRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 质检标准分页")
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:query')")
    public CommonResult<PageResult<QualityStandardRespVO>> getQualityStandardPage(@Valid QualityStandardPageReqVO pageReqVO) {
        PageResult<QualityStandardDO> pageResult = qualityStandardService.getQualityStandardPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QualityStandardRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 质检标准 Excel")
    @PreAuthorize("@ss.hasPermission('erp:quality-standard:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportQualityStandardExcel(@Valid QualityStandardPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QualityStandardDO> list = qualityStandardService.getQualityStandardPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 质检标准.xls", "数据", QualityStandardRespVO.class,
                        BeanUtils.toBean(list, QualityStandardRespVO.class));
    }

}