package cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus;

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

import cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.equipmentstatus.EquipmentStatusDO;
import cn.iocoder.yudao.module.erp.service.equipmentstatus.EquipmentStatusService;

@Tag(name = "管理后台 - ERP 设备状态记录")
@RestController
@RequestMapping("/erp/equipment-status")
@Validated
public class EquipmentStatusController {

    @Resource
    private EquipmentStatusService equipmentStatusService;

    @PostMapping("/create")
    @Operation(summary = "创建ERP 设备状态记录")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:create')")
    public CommonResult<Long> createEquipmentStatus(@Valid @RequestBody EquipmentStatusSaveReqVO createReqVO) {
        return success(equipmentStatusService.createEquipmentStatus(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新ERP 设备状态记录")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:update')")
    public CommonResult<Boolean> updateEquipmentStatus(@Valid @RequestBody EquipmentStatusSaveReqVO updateReqVO) {
        equipmentStatusService.updateEquipmentStatus(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除ERP 设备状态记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:delete')")
    public CommonResult<Boolean> deleteEquipmentStatus(@RequestParam("id") Long id) {
        equipmentStatusService.deleteEquipmentStatus(id);
        return success(true);
    }

    @DeleteMapping("/delete-list")
    @Parameter(name = "ids", description = "编号", required = true)
    @Operation(summary = "批量删除ERP 设备状态记录")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:delete')")
    public CommonResult<Boolean> deleteEquipmentStatusList(@RequestParam("ids") List<Long> ids) {
        equipmentStatusService.deleteEquipmentStatusListByIds(ids);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得ERP 设备状态记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:query')")
    public CommonResult<EquipmentStatusRespVO> getEquipmentStatus(@RequestParam("id") Long id) {
        EquipmentStatusDO equipmentStatus = equipmentStatusService.getEquipmentStatus(id);
        return success(BeanUtils.toBean(equipmentStatus, EquipmentStatusRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得ERP 设备状态记录分页")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:query')")
    public CommonResult<PageResult<EquipmentStatusRespVO>> getEquipmentStatusPage(@Valid EquipmentStatusPageReqVO pageReqVO) {
        PageResult<EquipmentStatusDO> pageResult = equipmentStatusService.getEquipmentStatusPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EquipmentStatusRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出ERP 设备状态记录 Excel")
    @PreAuthorize("@ss.hasPermission('erp:equipment-status:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEquipmentStatusExcel(@Valid EquipmentStatusPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EquipmentStatusDO> list = equipmentStatusService.getEquipmentStatusPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "ERP 设备状态记录.xls", "数据", EquipmentStatusRespVO.class,
                        BeanUtils.toBean(list, EquipmentStatusRespVO.class));
    }

}