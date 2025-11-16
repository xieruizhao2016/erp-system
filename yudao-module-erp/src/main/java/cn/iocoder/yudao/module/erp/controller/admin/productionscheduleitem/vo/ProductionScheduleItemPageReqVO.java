package cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 排程明细分页 Request VO")
@Data
public class ProductionScheduleItemPageReqVO extends PageParam {

    @Schema(description = "排程ID", example = "10681")
    private Long scheduleId;

    @Schema(description = "生产订单ID", example = "29829")
    private Long productionOrderId;

    @Schema(description = "产品ID", example = "8391")
    private Long productId;

    @Schema(description = "排程数量")
    private BigDecimal quantity;

    @Schema(description = "计划开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] plannedStartTime;

    @Schema(description = "计划结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] plannedEndTime;

    @Schema(description = "工作中心ID", example = "12032")
    private Long workCenterId;

    @Schema(description = "设备ID", example = "28801")
    private Long equipmentId;

    @Schema(description = "工序序号")
    private Integer processSequence;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "准备时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] setupTime;

    @Schema(description = "运行时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] runTime;

    @Schema(description = "等待时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] waitTime;

    @Schema(description = "状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟", example = "2")
    private Integer status;

    @Schema(description = "实际开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] actualStartTime;

    @Schema(description = "实际结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] actualEndTime;

    @Schema(description = "完成率")
    private BigDecimal completionRate;

    @Schema(description = "延迟原因", example = "不香")
    private String delayReason;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}