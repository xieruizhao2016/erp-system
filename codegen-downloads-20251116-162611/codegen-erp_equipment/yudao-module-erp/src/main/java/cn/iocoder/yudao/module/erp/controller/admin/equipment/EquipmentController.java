package cn.iocoder.yudao.module.erp.controller.admin.equipment;

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

import cn.iocoder.yudao.module.erp.controller.admin.equipment.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipment.EquipmentDO;
import cn.iocoder.yudao.module.erp.service.equipment.EquipmentService;

@Tag(name = "管理后台 - ERP 设备台账")
@RestController
@RequestMapping("/erp/equipment")
@Validated
public class EquipmentController {

    @Resource
    private EquipmentService equipmentService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 设备台账")
    @PreAuthorize("@ss.hasPermission('erp:equipment:create')")
    public CommonResult<Long> createEquipment(@Valid @RequestBody EquipmentSaveReqVO createReqVO) {
        return success(equipmentService.createEquipment(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 设备台账")
    @PreAuthorize("@ss.hasPermission('erp:equipment:update')")
    public CommonResult<Boolean> updateEquipment(@Valid @RequestBody EquipmentSaveReqVO updateReqVO) {
        equipmentService.updateEquipment(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 设备台账")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:equipment:delete')")
    public CommonResult<Boolean> deleteEquipment(@RequestParam("id") Long id) {
        equipmentService.deleteEquipment(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 设备台账")
                @PreAuthorize("@ss.hasPermission('erp:equipment:delete')")
    public CommonResult<Boolean> deleteEquipmentList(@RequestParam("ids") List<Long> ids) {
        equipmentService.deleteEquipmentListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 设备台账")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:equipment:query')")
    public CommonResult<EquipmentRespVO> getEquipment(@RequestParam("id") Long id) {
        EquipmentDO equipment = equipmentService.getEquipment(id);
        return success(BeanUtils.toBean(equipment, EquipmentRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 设备台账分页")
    @PreAuthorize("@ss.hasPermission('erp:equipment:query')")
    public CommonResult<PageResult<EquipmentRespVO>> getEquipmentPage(@Valid EquipmentPageReqVO pageReqVO) {
        PageResult<EquipmentDO> pageResult = equipmentService.getEquipmentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EquipmentRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 设备台账 Excel")
    @PreAuthorize("@ss.hasPermission('erp:equipment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEquipmentExcel(@Valid EquipmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EquipmentDO> list = equipmentService.getEquipmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 设备台账.xls", "数据", EquipmentRespVO.class,
                        BeanUtils.toBean(list, EquipmentRespVO.class));
    }

}