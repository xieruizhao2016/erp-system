package cn.iocoder.yudao.module.erp.controller.admin.costactual.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 实际成本新增/修改 Request VO")
@Data
public class CostActualSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21625")
    private Long id;

    @Schema(description = "成本单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "成本单号不能为空")
    private String costNo;

    @Schema(description = "工单ID", example = "4712")
    private Long workOrderId;

    @Schema(description = "生产订单ID", example = "24973")
    private Long productionOrderId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25054")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生产数量不能为空")
    private BigDecimal productionQuantity;

    @Schema(description = "材料成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "材料成本不能为空")
    private BigDecimal materialCost;

    @Schema(description = "材料成本调整")
    private BigDecimal materialCostAdjust;

    @Schema(description = "人工成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "人工成本不能为空")
    private BigDecimal laborCost;

    @Schema(description = "人工成本调整")
    private BigDecimal laborCostAdjust;

    @Schema(description = "制造费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "制造费用不能为空")
    private BigDecimal overheadCost;

    @Schema(description = "制造费用调整")
    private BigDecimal overheadCostAdjust;

    @Schema(description = "总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总成本不能为空")
    private BigDecimal totalCost;

    @Schema(description = "单位成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单位成本不能为空")
    private BigDecimal unitCost;

    @Schema(description = "成本币种")
    private String costCurrency;

    @Schema(description = "成本期间（YYYY-MM）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "成本期间（YYYY-MM）不能为空")
    private String costPeriod;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计算日期不能为空")
    private LocalDateTime calculationDate;

    @Schema(description = "最后调整日期")
    private LocalDateTime lastAdjustDate;

    @Schema(description = "状态：1-草稿，2-已计算，3-已确认", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}