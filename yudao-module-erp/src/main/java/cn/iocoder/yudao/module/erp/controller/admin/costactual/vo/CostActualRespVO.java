package cn.iocoder.yudao.module.erp.controller.admin.costactual.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 实际成本 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CostActualRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21625")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "成本单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("成本单号")
    private String costNo;

    @Schema(description = "工单ID", example = "4712")
    @ExcelProperty("工单ID")
    private Long workOrderId;

    @Schema(description = "生产订单ID", example = "24973")
    @ExcelProperty("生产订单ID")
    private Long productionOrderId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25054")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产数量")
    private BigDecimal productionQuantity;

    @Schema(description = "材料成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("材料成本")
    private BigDecimal materialCost;

    @Schema(description = "材料成本调整")
    @ExcelProperty("材料成本调整")
    private BigDecimal materialCostAdjust;

    @Schema(description = "人工成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("人工成本")
    private BigDecimal laborCost;

    @Schema(description = "人工成本调整")
    @ExcelProperty("人工成本调整")
    private BigDecimal laborCostAdjust;

    @Schema(description = "制造费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("制造费用")
    private BigDecimal overheadCost;

    @Schema(description = "制造费用调整")
    @ExcelProperty("制造费用调整")
    private BigDecimal overheadCostAdjust;

    @Schema(description = "总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成本")
    private BigDecimal totalCost;

    @Schema(description = "单位成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单位成本")
    private BigDecimal unitCost;

    @Schema(description = "成本币种")
    @ExcelProperty("成本币种")
    private String costCurrency;

    @Schema(description = "成本期间（YYYY-MM）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("成本期间（YYYY-MM）")
    private String costPeriod;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计算日期")
    private LocalDateTime calculationDate;

    @Schema(description = "最后调整日期")
    @ExcelProperty("最后调整日期")
    private LocalDateTime lastAdjustDate;

    @Schema(description = "状态：1-草稿，2-已计算，3-已确认", example = "1")
    @ExcelProperty("状态：1-草稿，2-已计算，3-已确认")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}