package cn.iocoder.yudao.module.erp.controller.admin.workhours.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Schema(description = "管理后台 - ERP 工时统计新增/修改 Request VO")
@Data
public class WorkHoursSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5606")
    private Long id;

    @Schema(description = "工单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26278")
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7030")
    @NotNull(message = "工序ID不能为空")
    private Long processId;

    @Schema(description = "操作员ID", example = "27887")
    private Long operatorId;

    @Schema(description = "工作日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工作日期不能为空")
    private LocalDate workDate;

    @Schema(description = "班次ID", example = "16198")
    private Long shiftId;

    @Schema(description = "开始时间")
    private LocalTime startTime;

    @Schema(description = "结束时间")
    private LocalTime endTime;

    @Schema(description = "工作时长（分钟）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工作时长（分钟）不能为空")
    private Integer duration;

    @Schema(description = "标准工时（分钟）")
    private Integer standardDuration;

    @Schema(description = "加班时长（分钟）")
    private Integer overtimeDuration;

    @Schema(description = "机时")
    private BigDecimal machineHours;

    @Schema(description = "操作员工时费率")
    private BigDecimal operatorRate;

    @Schema(description = "设备时费率")
    private BigDecimal machineRate;

    @Schema(description = "人工成本")
    private BigDecimal laborCost;

    @Schema(description = "设备成本")
    private BigDecimal machineCost;

    @Schema(description = "状态：1-有效，2-无效", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}