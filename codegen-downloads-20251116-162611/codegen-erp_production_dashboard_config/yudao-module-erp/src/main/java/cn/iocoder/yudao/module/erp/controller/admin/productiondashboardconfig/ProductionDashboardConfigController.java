package cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig;

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

import cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productiondashboardconfig.ProductionDashboardConfigDO;
import cn.iocoder.yudao.module.erp.service.productiondashboardconfig.ProductionDashboardConfigService;

@Tag(name = "管理后台 - ERP 看板配置")
@RestController
@RequestMapping("/erp/production-dashboard-config")
@Validated
public class ProductionDashboardConfigController {

    @Resource
    private ProductionDashboardConfigService productionDashboardConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 看板配置")
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:create')")
    public CommonResult<Long> createProductionDashboardConfig(@Valid @RequestBody ProductionDashboardConfigSaveReqVO createReqVO) {
        return success(productionDashboardConfigService.createProductionDashboardConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 看板配置")
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:update')")
    public CommonResult<Boolean> updateProductionDashboardConfig(@Valid @RequestBody ProductionDashboardConfigSaveReqVO updateReqVO) {
        productionDashboardConfigService.updateProductionDashboardConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 看板配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:delete')")
    public CommonResult<Boolean> deleteProductionDashboardConfig(@RequestParam("id") Long id) {
        productionDashboardConfigService.deleteProductionDashboardConfig(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 看板配置")
                @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:delete')")
    public CommonResult<Boolean> deleteProductionDashboardConfigList(@RequestParam("ids") List<Long> ids) {
        productionDashboardConfigService.deleteProductionDashboardConfigListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 看板配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:query')")
    public CommonResult<ProductionDashboardConfigRespVO> getProductionDashboardConfig(@RequestParam("id") Long id) {
        ProductionDashboardConfigDO productionDashboardConfig = productionDashboardConfigService.getProductionDashboardConfig(id);
        return success(BeanUtils.toBean(productionDashboardConfig, ProductionDashboardConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 看板配置分页")
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:query')")
    public CommonResult<PageResult<ProductionDashboardConfigRespVO>> getProductionDashboardConfigPage(@Valid ProductionDashboardConfigPageReqVO pageReqVO) {
        PageResult<ProductionDashboardConfigDO> pageResult = productionDashboardConfigService.getProductionDashboardConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionDashboardConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 看板配置 Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-dashboard-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionDashboardConfigExcel(@Valid ProductionDashboardConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionDashboardConfigDO> list = productionDashboardConfigService.getProductionDashboardConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 看板配置.xls", "数据", ProductionDashboardConfigRespVO.class,
                        BeanUtils.toBean(list, ProductionDashboardConfigRespVO.class));
    }

}