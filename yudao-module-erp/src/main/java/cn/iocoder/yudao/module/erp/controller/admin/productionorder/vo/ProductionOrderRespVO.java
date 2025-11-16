package cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionOrderRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "26384")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产订单号")
    private String no;

    @Schema(description = "客户ID（关联销售订单）", example = "3753")
    @ExcelProperty("客户ID（关联销售订单）")
    private Long customerId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32079")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("产品名称")
    private String productName;

    @Schema(description = "产品规格")
    @ExcelProperty("产品规格")
    private String productSpec;

    @Schema(description = "单位ID", example = "16828")
    @ExcelProperty("单位ID")
    private Long unitId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产数量")
    private BigDecimal quantity;

    @Schema(description = "已完成数量")
    @ExcelProperty("已完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "计划开始时间")
    @ExcelProperty("计划开始时间")
    private LocalDateTime startTime;

    @Schema(description = "计划完成时间")
    @ExcelProperty("计划完成时间")
    private LocalDateTime endTime;

    @Schema(description = "实际开始时间")
    @ExcelProperty("实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际完成时间")
    @ExcelProperty("实际完成时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消")
    private Integer status;

    @Schema(description = "优先级：1-紧急，2-高，3-中，4-低")
    @ExcelProperty("优先级：1-紧急，2-高，3-中，4-低")
    private Integer priority;

    @Schema(description = "来源类型：1-手动创建，2-销售订单，3-库存补充", example = "1")
    @ExcelProperty("来源类型：1-手动创建，2-销售订单，3-库存补充")
    private Integer sourceType;

    @Schema(description = "来源单据ID", example = "10935")
    @ExcelProperty("来源单据ID")
    private Long sourceId;

    @Schema(description = "生产说明", example = "随便")
    @ExcelProperty("生产说明")
    private String description;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}