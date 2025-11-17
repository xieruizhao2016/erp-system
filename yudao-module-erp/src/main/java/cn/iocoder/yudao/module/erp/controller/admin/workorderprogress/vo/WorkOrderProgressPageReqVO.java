package cn.iocoder.yudao.module.erp.controller.admin.workorderprogress.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工单进度分页 Request VO")
@Data
public class WorkOrderProgressPageReqVO extends PageParam {

    @Schema(description = "工单ID", example = "25099")
    private Long workOrderId;

    @Schema(description = "工序ID", example = "9492")
    private Long processId;

    @Schema(description = "工序名称", example = "赵六")
    private String processName;

    @Schema(description = "工序序号")
    private Integer sequence;

    @Schema(description = "计划开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] plannedStartTime;

    @Schema(description = "计划结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] plannedEndTime;

    @Schema(description = "实际开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] actualStartTime;

    @Schema(description = "实际结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] actualEndTime;

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
    private Integer[] workTime;

    @Schema(description = "停机时间（分钟）")
    private Integer[] downtime;

    @Schema(description = "质检状态：1-待检，2-合格，3-不合格", example = "1")
    private Integer qualityStatus;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}