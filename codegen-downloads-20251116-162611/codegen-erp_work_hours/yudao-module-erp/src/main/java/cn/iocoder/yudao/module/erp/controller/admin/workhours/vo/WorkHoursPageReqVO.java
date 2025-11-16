package cn.iocoder.yudao.module.erp.controller.admin.workhours.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 工时统计分页 Request VO")
@Data
public class WorkHoursPageReqVO extends PageParam {

    @Schema(description = "工单ID", example = "26278")
    private Long workOrderId;

    @Schema(description = "工序ID", example = "7030")
    private Long processId;

    @Schema(description = "操作员ID", example = "27887")
    private Long operatorId;

    @Schema(description = "工作日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] workDate;

    @Schema(description = "班次ID", example = "16198")
    private Long shiftId;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalTime[] startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalTime[] endTime;

    @Schema(description = "工作时长（分钟）")
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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}