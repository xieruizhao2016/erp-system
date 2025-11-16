package cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产排程主分页 Request VO")
@Data
public class ProductionSchedulePageReqVO extends PageParam {

    @Schema(description = "排程单号")
    private String scheduleNo;

    @Schema(description = "排程名称", example = "李四")
    private String scheduleName;

    @Schema(description = "排程类型：1-主排程，2-详细排程", example = "1")
    private Integer scheduleType;

    @Schema(description = "计划天数")
    private Integer planningHorizonDays;

    @Schema(description = "计划开始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] startDate;

    @Schema(description = "计划结束日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] endDate;

    @Schema(description = "状态：1-草稿，2-已发布，3-执行中，4-已完成", example = "1")
    private Integer status;

    @Schema(description = "总订单数")
    private Integer totalOrders;

    @Schema(description = "总数量")
    private BigDecimal totalQuantity;

    @Schema(description = "总工时")
    private Integer totalWorkHours;

    @Schema(description = "创建人")
    private String createdBy;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新人")
    private String updatedBy;

}