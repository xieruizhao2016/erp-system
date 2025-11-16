package cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem;

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

import cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspectionitem.QualityInspectionItemDO;
import cn.iocoder.yudao.module.erp.service.qualityinspectionitem.QualityInspectionItemService;

@Tag(name = "管理后台 - ERP 质检明细")
@RestController
@RequestMapping("/erp/quality-inspection-item")
@Validated
public class QualityInspectionItemController {

    @Resource
    private QualityInspectionItemService qualityInspectionItemService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 质检明细")
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:create')")
    public CommonResult<Long> createQualityInspectionItem(@Valid @RequestBody QualityInspectionItemSaveReqVO createReqVO) {
        return success(qualityInspectionItemService.createQualityInspectionItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 质检明细")
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:update')")
    public CommonResult<Boolean> updateQualityInspectionItem(@Valid @RequestBody QualityInspectionItemSaveReqVO updateReqVO) {
        qualityInspectionItemService.updateQualityInspectionItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 质检明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:delete')")
    public CommonResult<Boolean> deleteQualityInspectionItem(@RequestParam("id") Long id) {
        qualityInspectionItemService.deleteQualityInspectionItem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 质检明细")
                @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:delete')")
    public CommonResult<Boolean> deleteQualityInspectionItemList(@RequestParam("ids") List<Long> ids) {
        qualityInspectionItemService.deleteQualityInspectionItemListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 质检明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:query')")
    public CommonResult<QualityInspectionItemRespVO> getQualityInspectionItem(@RequestParam("id") Long id) {
        QualityInspectionItemDO qualityInspectionItem = qualityInspectionItemService.getQualityInspectionItem(id);
        return success(BeanUtils.toBean(qualityInspectionItem, QualityInspectionItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 质检明细分页")
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:query')")
    public CommonResult<PageResult<QualityInspectionItemRespVO>> getQualityInspectionItemPage(@Valid QualityInspectionItemPageReqVO pageReqVO) {
        PageResult<QualityInspectionItemDO> pageResult = qualityInspectionItemService.getQualityInspectionItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, QualityInspectionItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 质检明细 Excel")
    @PreAuthorize("@ss.hasPermission('erp:quality-inspection-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportQualityInspectionItemExcel(@Valid QualityInspectionItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<QualityInspectionItemDO> list = qualityInspectionItemService.getQualityInspectionItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 质检明细.xls", "数据", QualityInspectionItemRespVO.class,
                        BeanUtils.toBean(list, QualityInspectionItemRespVO.class));
    }

}