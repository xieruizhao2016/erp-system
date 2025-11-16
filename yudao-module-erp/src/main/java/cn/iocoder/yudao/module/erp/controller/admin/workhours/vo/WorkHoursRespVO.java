package cn.iocoder.yudao.module.erp.controller.admin.workhours.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 工时统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkHoursRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5606")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26278")
    @ExcelProperty("工单ID")
    private Long workOrderId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7030")
    @ExcelProperty("工序ID")
    private Long processId;

    @Schema(description = "操作员ID", example = "27887")
    @ExcelProperty("操作员ID")
    private Long operatorId;

    @Schema(description = "工作日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工作日期")
    private LocalDate workDate;

    @Schema(description = "班次ID", example = "16198")
    @ExcelProperty("班次ID")
    private Long shiftId;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalTime endTime;

    @Schema(description = "工作时长（分钟）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工作时长（分钟）")
    private Integer duration;

    @Schema(description = "标准工时（分钟）")
    @ExcelProperty("标准工时（分钟）")
    private Integer standardDuration;

    @Schema(description = "加班时长（分钟）")
    @ExcelProperty("加班时长（分钟）")
    private Integer overtimeDuration;

    @Schema(description = "机时")
    @ExcelProperty("机时")
    private BigDecimal machineHours;

    @Schema(description = "操作员工时费率")
    @ExcelProperty("操作员工时费率")
    private BigDecimal operatorRate;

    @Schema(description = "设备时费率")
    @ExcelProperty("设备时费率")
    private BigDecimal machineRate;

    @Schema(description = "人工成本")
    @ExcelProperty("人工成本")
    private BigDecimal laborCost;

    @Schema(description = "设备成本")
    @ExcelProperty("设备成本")
    private BigDecimal machineCost;

    @Schema(description = "状态：1-有效，2-无效", example = "2")
    @ExcelProperty("状态：1-有效，2-无效")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}