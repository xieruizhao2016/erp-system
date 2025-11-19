package cn.iocoder.yudao.module.erp.controller.admin.workorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工单主新增/修改 Request VO")
@Data
public class WorkOrderSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24340")
    private Long id;

    @Schema(description = "工单号（新增时由系统自动生成，修改时必填）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String workOrderNo;

    @Schema(description = "生产订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14797")
    @NotNull(message = "生产订单ID不能为空")
    private Long productionOrderId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7312")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "工单数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工单数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "工作中心ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5867")
    @NotNull(message = "工作中心ID不能为空")
    private Long workCenterId;

    @Schema(description = "主管ID", example = "8062")
    private Long supervisorId;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划开始时间不能为空")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划结束时间不能为空")
    private LocalDateTime plannedEndTime;

    @Schema(description = "实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际结束时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消", example = "1")
    private Integer status;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "作业指导书")
    private String instruction;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}