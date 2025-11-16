package cn.iocoder.yudao.module.erp.controller.admin.processrouteitem;

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

import cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.module.erp.service.processrouteitem.ProcessRouteItemService;

@Tag(name = "管理后台 - ERP 工艺路线明细")
@RestController
@RequestMapping("/erp/process-route-item")
@Validated
public class ProcessRouteItemController {

    @Resource
    private ProcessRouteItemService processRouteItemService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工艺路线明细")
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:create')")
    public CommonResult<Long> createProcessRouteItem(@Valid @RequestBody ProcessRouteItemSaveReqVO createReqVO) {
        return success(processRouteItemService.createProcessRouteItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工艺路线明细")
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:update')")
    public CommonResult<Boolean> updateProcessRouteItem(@Valid @RequestBody ProcessRouteItemSaveReqVO updateReqVO) {
        processRouteItemService.updateProcessRouteItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工艺路线明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:delete')")
    public CommonResult<Boolean> deleteProcessRouteItem(@RequestParam("id") Long id) {
        processRouteItemService.deleteProcessRouteItem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工艺路线明细")
                @PreAuthorize("@ss.hasPermission('erp:process-route-item:delete')")
    public CommonResult<Boolean> deleteProcessRouteItemList(@RequestParam("ids") List<Long> ids) {
        processRouteItemService.deleteProcessRouteItemListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工艺路线明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:query')")
    public CommonResult<ProcessRouteItemRespVO> getProcessRouteItem(@RequestParam("id") Long id) {
        ProcessRouteItemDO processRouteItem = processRouteItemService.getProcessRouteItem(id);
        return success(BeanUtils.toBean(processRouteItem, ProcessRouteItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工艺路线明细分页")
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:query')")
    public CommonResult<PageResult<ProcessRouteItemRespVO>> getProcessRouteItemPage(@Valid ProcessRouteItemPageReqVO pageReqVO) {
        PageResult<ProcessRouteItemDO> pageResult = processRouteItemService.getProcessRouteItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessRouteItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工艺路线明细 Excel")
    @PreAuthorize("@ss.hasPermission('erp:process-route-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProcessRouteItemExcel(@Valid ProcessRouteItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProcessRouteItemDO> list = processRouteItemService.getProcessRouteItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工艺路线明细.xls", "数据", ProcessRouteItemRespVO.class,
                        BeanUtils.toBean(list, ProcessRouteItemRespVO.class));
    }

}