package cn.iocoder.yudao.module.erp.controller.admin.workorder.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工单主分页 Request VO")
@Data
public class WorkOrderPageReqVO extends PageParam {

    @Schema(description = "工单号")
    private String workOrderNo;

    @Schema(description = "生产订单ID", example = "14797")
    private Long productionOrderId;

    @Schema(description = "产品ID", example = "7312")
    private Long productId;

    @Schema(description = "产品名称", example = "赵六")
    private String productName;

    @Schema(description = "工单数量")
    private BigDecimal quantity;

    @Schema(description = "完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "工作中心ID", example = "5867")
    private Long workCenterId;

    @Schema(description = "主管ID", example = "8062")
    private Long supervisorId;

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

    @Schema(description = "状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消", example = "1")
    private Integer status;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "作业指导书")
    private String instruction;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}