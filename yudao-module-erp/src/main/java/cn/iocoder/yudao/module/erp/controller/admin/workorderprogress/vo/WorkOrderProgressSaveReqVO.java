package cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工单进度新增/修改 Request VO")
@Data
public class WorkOrderProgressSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4404")
    private Long id;

    @Schema(description = "工单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25099")
    @NotNull(message = "工单ID不能为空")
    private Long workOrderId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9492")
    @NotNull(message = "工序ID不能为空")
    private Long processId;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "工序名称不能为空")
    private String processName;

    @Schema(description = "工序序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工序序号不能为空")
    private Integer sequence;

    @Schema(description = "计划开始时间")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间")
    private LocalDateTime plannedEndTime;

    @Schema(description = "实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际结束时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "计划数量")
    private BigDecimal plannedQuantity;

    @Schema(description = "完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "不合格数量")
    private BigDecimal rejectedQuantity;

    @Schema(description = "报废数量")
    private BigDecimal scrapQuantity;

    @Schema(description = "状态：1-待开始，2-进行中，3-已完成，4-异常", example = "2")
    private Integer status;

    @Schema(description = "操作员ID", example = "30012")
    private Long operatorId;

    @Schema(description = "设备ID", example = "27089")
    private Long equipmentId;

    @Schema(description = "实际工时（分钟）")
    private Integer workTime;

    @Schema(description = "停机时间（分钟）")
    private Integer downtime;

    @Schema(description = "质检状态：1-待检，2-合格，3-不合格", example = "1")
    private Integer qualityStatus;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}