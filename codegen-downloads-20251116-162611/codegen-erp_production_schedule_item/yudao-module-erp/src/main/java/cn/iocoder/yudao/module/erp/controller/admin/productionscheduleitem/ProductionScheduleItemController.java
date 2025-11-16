package cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem;

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

import cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.productionscheduleitem.ProductionScheduleItemDO;
import cn.iocoder.yudao.module.erp.service.productionscheduleitem.ProductionScheduleItemService;

@Tag(name = "管理后台 - ERP 排程明细")
@RestController
@RequestMapping("/erp/production-schedule-item")
@Validated
public class ProductionScheduleItemController {

    @Resource
    private ProductionScheduleItemService productionScheduleItemService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 排程明细")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:create')")
    public CommonResult<Long> createProductionScheduleItem(@Valid @RequestBody ProductionScheduleItemSaveReqVO createReqVO) {
        return success(productionScheduleItemService.createProductionScheduleItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 排程明细")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:update')")
    public CommonResult<Boolean> updateProductionScheduleItem(@Valid @RequestBody ProductionScheduleItemSaveReqVO updateReqVO) {
        productionScheduleItemService.updateProductionScheduleItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 排程明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:delete')")
    public CommonResult<Boolean> deleteProductionScheduleItem(@RequestParam("id") Long id) {
        productionScheduleItemService.deleteProductionScheduleItem(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 排程明细")
                @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:delete')")
    public CommonResult<Boolean> deleteProductionScheduleItemList(@RequestParam("ids") List<Long> ids) {
        productionScheduleItemService.deleteProductionScheduleItemListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 排程明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:query')")
    public CommonResult<ProductionScheduleItemRespVO> getProductionScheduleItem(@RequestParam("id") Long id) {
        ProductionScheduleItemDO productionScheduleItem = productionScheduleItemService.getProductionScheduleItem(id);
        return success(BeanUtils.toBean(productionScheduleItem, ProductionScheduleItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 排程明细分页")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:query')")
    public CommonResult<PageResult<ProductionScheduleItemRespVO>> getProductionScheduleItemPage(@Valid ProductionScheduleItemPageReqVO pageReqVO) {
        PageResult<ProductionScheduleItemDO> pageResult = productionScheduleItemService.getProductionScheduleItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionScheduleItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 排程明细 Excel")
    @PreAuthorize("@ss.hasPermission('erp:production-schedule-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionScheduleItemExcel(@Valid ProductionScheduleItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionScheduleItemDO> list = productionScheduleItemService.getProductionScheduleItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 排程明细.xls", "数据", ProductionScheduleItemRespVO.class,
                        BeanUtils.toBean(list, ProductionScheduleItemRespVO.class));
    }

}