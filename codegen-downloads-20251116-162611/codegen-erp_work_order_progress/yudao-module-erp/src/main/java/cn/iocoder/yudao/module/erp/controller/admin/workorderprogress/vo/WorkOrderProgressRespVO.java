package cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 工单进度 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkOrderProgressRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4404")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25099")
    @ExcelProperty("工单ID")
    private Long workOrderId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9492")
    @ExcelProperty("工序ID")
    private Long processId;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("工序名称")
    private String processName;

    @Schema(description = "工序序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工序序号")
    private Integer sequence;

    @Schema(description = "计划开始时间")
    @ExcelProperty("计划开始时间")
    private LocalDateTime plannedStartTime;

    @Schema(description = "计划结束时间")
    @ExcelProperty("计划结束时间")
    private LocalDateTime plannedEndTime;

    @Schema(description = "实际开始时间")
    @ExcelProperty("实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际结束时间")
    @ExcelProperty("实际结束时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "计划数量")
    @ExcelProperty("计划数量")
    private BigDecimal plannedQuantity;

    @Schema(description = "完成数量")
    @ExcelProperty("完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "合格数量")
    @ExcelProperty("合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "不合格数量")
    @ExcelProperty("不合格数量")
    private BigDecimal rejectedQuantity;

    @Schema(description = "报废数量")
    @ExcelProperty("报废数量")
    private BigDecimal scrapQuantity;

    @Schema(description = "状态：1-待开始，2-进行中，3-已完成，4-异常", example = "2")
    @ExcelProperty("状态：1-待开始，2-进行中，3-已完成，4-异常")
    private Integer status;

    @Schema(description = "操作员ID", example = "30012")
    @ExcelProperty("操作员ID")
    private Long operatorId;

    @Schema(description = "设备ID", example = "27089")
    @ExcelProperty("设备ID")
    private Long equipmentId;

    @Schema(description = "实际工时（分钟）")
    @ExcelProperty("实际工时（分钟）")
    private Integer workTime;

    @Schema(description = "停机时间（分钟）")
    @ExcelProperty("停机时间（分钟）")
    private Integer downtime;

    @Schema(description = "质检状态：1-待检，2-合格，3-不合格", example = "1")
    @ExcelProperty("质检状态：1-待检，2-合格，3-不合格")
    private Integer qualityStatus;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}