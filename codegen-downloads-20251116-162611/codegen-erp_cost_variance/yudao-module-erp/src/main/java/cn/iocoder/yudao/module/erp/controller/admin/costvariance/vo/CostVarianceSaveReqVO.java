package cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 成本差异分析新增/修改 Request VO")
@Data
public class CostVarianceSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11533")
    private Long id;

    @Schema(description = "实际成本ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15646")
    @NotNull(message = "实际成本ID不能为空")
    private Long costActualId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29471")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生产数量不能为空")
    private BigDecimal productionQuantity;

    @Schema(description = "标准总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "标准总成本不能为空")
    private BigDecimal standardTotalCost;

    @Schema(description = "实际总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际总成本不能为空")
    private BigDecimal actualTotalCost;

    @Schema(description = "总差异", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总差异不能为空")
    private BigDecimal totalVariance;

    @Schema(description = "总差异率", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总差异率不能为空")
    private BigDecimal totalVarianceRate;

    @Schema(description = "材料成本差异")
    private BigDecimal materialVariance;

    @Schema(description = "材料差异率")
    private BigDecimal materialVarianceRate;

    @Schema(description = "人工成本差异")
    private BigDecimal laborVariance;

    @Schema(description = "人工差异率")
    private BigDecimal laborVarianceRate;

    @Schema(description = "制造费用差异")
    private BigDecimal overheadVariance;

    @Schema(description = "制造费用差异率")
    private BigDecimal overheadVarianceRate;

    @Schema(description = "差异类型：1-有利，2-不利", example = "1")
    private Integer varianceType;

    @Schema(description = "分析日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "分析日期不能为空")
    private LocalDateTime analysisDate;

    @Schema(description = "差异原因", example = "不对")
    private String varianceReason;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}