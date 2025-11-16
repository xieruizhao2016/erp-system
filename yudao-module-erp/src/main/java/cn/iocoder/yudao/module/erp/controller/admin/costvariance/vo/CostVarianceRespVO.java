package cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 成本差异分析 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CostVarianceRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11533")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "实际成本ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15646")
    @ExcelProperty("实际成本ID")
    private Long costActualId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29471")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产数量")
    private BigDecimal productionQuantity;

    @Schema(description = "标准总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标准总成本")
    private BigDecimal standardTotalCost;

    @Schema(description = "实际总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实际总成本")
    private BigDecimal actualTotalCost;

    @Schema(description = "总差异", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总差异")
    private BigDecimal totalVariance;

    @Schema(description = "总差异率", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总差异率")
    private BigDecimal totalVarianceRate;

    @Schema(description = "材料成本差异")
    @ExcelProperty("材料成本差异")
    private BigDecimal materialVariance;

    @Schema(description = "材料差异率")
    @ExcelProperty("材料差异率")
    private BigDecimal materialVarianceRate;

    @Schema(description = "人工成本差异")
    @ExcelProperty("人工成本差异")
    private BigDecimal laborVariance;

    @Schema(description = "人工差异率")
    @ExcelProperty("人工差异率")
    private BigDecimal laborVarianceRate;

    @Schema(description = "制造费用差异")
    @ExcelProperty("制造费用差异")
    private BigDecimal overheadVariance;

    @Schema(description = "制造费用差异率")
    @ExcelProperty("制造费用差异率")
    private BigDecimal overheadVarianceRate;

    @Schema(description = "差异类型：1-有利，2-不利", example = "1")
    @ExcelProperty("差异类型：1-有利，2-不利")
    private Integer varianceType;

    @Schema(description = "分析日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("分析日期")
    private LocalDateTime analysisDate;

    @Schema(description = "差异原因", example = "不对")
    @ExcelProperty("差异原因")
    private String varianceReason;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}