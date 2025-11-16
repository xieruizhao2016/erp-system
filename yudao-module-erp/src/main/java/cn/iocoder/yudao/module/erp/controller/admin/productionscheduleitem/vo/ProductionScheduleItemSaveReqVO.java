package cn.iocoder.yudao.module.erp.controller.admin.productionscheduleitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 排程明细新增/修改 Request VO")
@Data
public class ProductionScheduleItemSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "8898")
    private Long id;

    @Schema(description = "排程ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10681")
    @NotNull(message = "排程ID不能为空")
    private Long scheduleId;

    @Schema(description = "生产订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29829")
    @NotNull(message = "生产订单ID不能为空")
    private Long productionOrderId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8391")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "排程数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排程数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划结束时间不能为空")
    private LocalDateTime plannedEndTime;

    @Schema(description = "工作中心ID", example = "12032")
    private Long workCenterId;

    @Schema(description = "设备ID", example = "28801")
    private Long equipmentId;

    @Schema(description = "工序序号")
    private Integer processSequence;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "准备时间")
    private Integer setupTime;

    @Schema(description = "运行时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "运行时间不能为空")
    private Integer runTime;

    @Schema(description = "等待时间")
    private Integer waitTime;

    @Schema(description = "状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟", example = "2")
    private Integer status;

    @Schema(description = "实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际结束时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "完成率")
    private BigDecimal completionRate;

    @Schema(description = "延迟原因", example = "不香")
    private String delayReason;

}