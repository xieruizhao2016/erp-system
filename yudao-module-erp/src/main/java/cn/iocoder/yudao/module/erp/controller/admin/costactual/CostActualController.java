package cn.iocoder.yudao.module.erp.controller.admin.costactual;

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

import cn.iocoder.yudao.module.erp.controller.admin.costactual.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.costactual.CostActualDO;
import cn.iocoder.yudao.module.erp.service.costactual.CostActualService;

@Tag(name = "管理后台 - ERP 实际成本")
@RestController
@RequestMapping("/erp/cost-actual")
@Validated
public class CostActualController {

    @Resource
    private CostActualService costActualService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 实际成本")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:create')")
    public CommonResult<Long> createCostActual(@Valid @RequestBody CostActualSaveReqVO createReqVO) {
        return success(costActualService.createCostActual(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 实际成本")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:update')")
    public CommonResult<Boolean> updateCostActual(@Valid @RequestBody CostActualSaveReqVO updateReqVO) {
        costActualService.updateCostActual(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 实际成本")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:delete')")
    public CommonResult<Boolean> deleteCostActual(@RequestParam("id") Long id) {
        costActualService.deleteCostActual(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 实际成本")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:delete')")
    public CommonResult<Boolean> deleteCostActualList(@RequestParam("ids") List<Long> ids) {
        costActualService.deleteCostActualListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 实际成本")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:query')")
    public CommonResult<CostActualRespVO> getCostActual(@RequestParam("id") Long id) {
        CostActualDO costActual = costActualService.getCostActual(id);
        return success(BeanUtils.toBean(costActual, CostActualRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 实际成本分页")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:query')")
    public CommonResult<PageResult<CostActualRespVO>> getCostActualPage(@Valid CostActualPageReqVO pageReqVO) {
        PageResult<CostActualDO> pageResult = costActualService.getCostActualPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CostActualRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 实际成本 Excel")
    @PreAuthorize("@ss.hasPermission('erp:cost-actual:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCostActualExcel(@Valid CostActualPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CostActualDO> list = costActualService.getCostActualPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 实际成本.xls", "数据", CostActualRespVO.class,
                        BeanUtils.toBean(list, CostActualRespVO.class));
    }

}