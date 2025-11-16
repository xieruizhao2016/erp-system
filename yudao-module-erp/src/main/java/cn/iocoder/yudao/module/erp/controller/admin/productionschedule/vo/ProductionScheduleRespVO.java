package cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产排程主 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionScheduleRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19487")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "排程单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排程单号")
    private String scheduleNo;

    @Schema(description = "排程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("排程名称")
    private String scheduleName;

    @Schema(description = "排程类型：1-主排程，2-详细排程", example = "1")
    @ExcelProperty("排程类型：1-主排程，2-详细排程")
    private Integer scheduleType;

    @Schema(description = "计划天数")
    @ExcelProperty("计划天数")
    private Integer planningHorizonDays;

    @Schema(description = "计划开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划开始日期")
    private LocalDate startDate;

    @Schema(description = "计划结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划结束日期")
    private LocalDate endDate;

    @Schema(description = "状态：1-草稿，2-已发布，3-执行中，4-已完成", example = "1")
    @ExcelProperty("状态：1-草稿，2-已发布，3-执行中，4-已完成")
    private Integer status;

    @Schema(description = "总订单数")
    @ExcelProperty("总订单数")
    private Integer totalOrders;

    @Schema(description = "总数量")
    @ExcelProperty("总数量")
    private BigDecimal totalQuantity;

    @Schema(description = "总工时")
    @ExcelProperty("总工时")
    private Integer totalWorkHours;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createdBy;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updatedBy;

}