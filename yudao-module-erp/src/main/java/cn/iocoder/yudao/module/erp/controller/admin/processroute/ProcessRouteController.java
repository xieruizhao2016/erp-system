package cn.iocoder.yudao.module.erp.controller.admin.processroute;

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

import cn.iocoder.yudao.module.erp.controller.admin.processroute.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.processroute.ProcessRouteDO;
import cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem.ProcessRouteItemDO;
import cn.iocoder.yudao.module.erp.service.processroute.ProcessRouteService;
import cn.iocoder.yudao.module.erp.dal.mysql.processrouteitem.ProcessRouteItemMapper;
import cn.hutool.core.collection.CollUtil;

@Tag(name = "管理后台 - ERP 工艺路线主")
@RestController
@RequestMapping("/erp/process-route")
@Validated
public class ProcessRouteController {

    @Resource
    private ProcessRouteService processRouteService;

    @Resource
    private ProcessRouteItemMapper processRouteItemMapper;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 工艺路线主")
    @PreAuthorize("@ss.hasPermission('erp:process-route:create')")
    public CommonResult<Long> createProcessRoute(@Valid @RequestBody ProcessRouteSaveReqVO createReqVO) {
        return success(processRouteService.createProcessRoute(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 工艺路线主")
    @PreAuthorize("@ss.hasPermission('erp:process-route:update')")
    public CommonResult<Boolean> updateProcessRoute(@Valid @RequestBody ProcessRouteSaveReqVO updateReqVO) {
        processRouteService.updateProcessRoute(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 工艺路线主")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:process-route:delete')")
    public CommonResult<Boolean> deleteProcessRoute(@RequestParam("id") Long id) {
        processRouteService.deleteProcessRoute(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 工艺路线主")
    @PreAuthorize("@ss.hasPermission('erp:process-route:delete')")
    public CommonResult<Boolean> deleteProcessRouteList(@RequestParam("ids") List<Long> ids) {
        processRouteService.deleteProcessRouteListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 工艺路线主")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:process-route:query')")
    public CommonResult<ProcessRouteRespVO> getProcessRoute(@RequestParam("id") Long id) {
        ProcessRouteDO processRoute = processRouteService.getProcessRoute(id);
        ProcessRouteRespVO respVO = BeanUtils.toBean(processRoute, ProcessRouteRespVO.class);
        // 查询明细列表
        if (respVO != null) {
            List<ProcessRouteItemDO> items = processRouteItemMapper.selectListByRouteId(id);
            if (CollUtil.isNotEmpty(items)) {
                respVO.setItems(BeanUtils.toBean(items, ProcessRouteRespVO.Item.class));
            }
        }
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 工艺路线主分页")
    @PreAuthorize("@ss.hasPermission('erp:process-route:query')")
    public CommonResult<PageResult<ProcessRouteRespVO>> getProcessRoutePage(@Valid ProcessRoutePageReqVO pageReqVO) {
        PageResult<ProcessRouteDO> pageResult = processRouteService.getProcessRoutePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProcessRouteRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 工艺路线主 Excel")
    @PreAuthorize("@ss.hasPermission('erp:process-route:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProcessRouteExcel(@Valid ProcessRoutePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProcessRouteDO> list = processRouteService.getProcessRoutePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 工艺路线主.xls", "数据", ProcessRouteRespVO.class,
                        BeanUtils.toBean(list, ProcessRouteRespVO.class));
    }

    @GetMapping("/generate-route-no")
    @Operation(summary = "生成默认工艺路线编号")
    @PreAuthorize("@ss.hasPermission('erp:process-route:create')")
    public CommonResult<String> generateRouteNo() {
        return success(processRouteService.generateRouteNo());
    }

}