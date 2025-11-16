package cn.iocoder.yudao.module.erp.controller.admin.workorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工单主 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkOrderRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "24340")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工单号")
    private String workOrderNo;

    @Schema(description = "生产订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14797")
    @ExcelProperty("生产订单ID")
    private Long productionOrderId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7312")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("产品名称")
    private String productName;

    @Schema(description = "工单数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工单数量")
    private BigDecimal quantity;

    @Schema(description = "完成数量")
    @ExcelProperty("完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "合格数量")
    @ExcelProperty("合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "工作中心ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5867")
    @ExcelProperty("工作中心ID")
    private Long workCenterId;

    @Schema(description = "主管ID", example = "8062")
    @ExcelProperty("主管ID")
    private Long supervisorId;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划开始时间")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划结束时间")
    private LocalDateTime plannedEndTime;

    @Schema(description = "实际开始时间")
    @ExcelProperty("实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际结束时间")
    @ExcelProperty("实际结束时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消", example = "1")
    @ExcelProperty("状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消")
    private Integer status;

    @Schema(description = "优先级")
    @ExcelProperty("优先级")
    private Integer priority;

    @Schema(description = "作业指导书")
    @ExcelProperty("作业指导书")
    private String instruction;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}