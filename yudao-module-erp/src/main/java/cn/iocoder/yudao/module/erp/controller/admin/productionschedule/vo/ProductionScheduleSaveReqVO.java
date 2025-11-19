package cn.iocoder.yudao.module.erp.controller.admin.productionschedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产排程主新增/修改 Request VO")
@Data
public class ProductionScheduleSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19487")
    private Long id;

    @Schema(description = "排程单号（新增时由系统自动生成，修改时必填）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String scheduleNo;

    @Schema(description = "排程名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "排程名称不能为空")
    private String scheduleName;

    @Schema(description = "排程类型：1-主排程，2-详细排程", example = "1")
    private Integer scheduleType;

    @Schema(description = "计划天数")
    private Integer planningHorizonDays;

    @Schema(description = "计划开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划开始日期不能为空")
    private LocalDate startDate;

    @Schema(description = "计划结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划结束日期不能为空")
    private LocalDate endDate;

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

    @Schema(description = "更新人")
    private String updatedBy;

}