package cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 设备状态记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class EquipmentStatusRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5095")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27342")
    @ExcelProperty("设备ID")
    private Long equipmentId;

    @Schema(description = "状态：1-运行，2-待机，3-故障，4-维修，5-停机", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态：1-运行，2-待机，3-故障，4-维修，5-停机")
    private Integer status;

    @Schema(description = "状态开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("状态开始时间")
    private LocalDateTime statusStartTime;

    @Schema(description = "状态结束时间")
    @ExcelProperty("状态结束时间")
    private LocalDateTime statusEndTime;

    @Schema(description = "持续时间（分钟）")
    @ExcelProperty("持续时间（分钟）")
    private Integer duration;

    @Schema(description = "关联工单ID", example = "6790")
    @ExcelProperty("关联工单ID")
    private Long workOrderId;

    @Schema(description = "操作员ID", example = "21367")
    @ExcelProperty("操作员ID")
    private Long operatorId;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}