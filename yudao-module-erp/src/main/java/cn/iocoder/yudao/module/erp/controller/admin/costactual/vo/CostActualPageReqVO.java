package cn.iocoder.yudao.module.erp.controller.admin.costactual.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 实际成本分页 Request VO")
@Data
public class CostActualPageReqVO extends PageParam {

    @Schema(description = "成本单号")
    private String costNo;

    @Schema(description = "工单ID", example = "4712")
    private Long workOrderId;

    @Schema(description = "生产订单ID", example = "24973")
    private Long productionOrderId;

    @Schema(description = "产品ID", example = "25054")
    private Long productId;

    @Schema(description = "生产数量")
    private BigDecimal productionQuantity;

    @Schema(description = "材料成本")
    private BigDecimal materialCost;

    @Schema(description = "材料成本调整")
    private BigDecimal materialCostAdjust;

    @Schema(description = "人工成本")
    private BigDecimal laborCost;

    @Schema(description = "人工成本调整")
    private BigDecimal laborCostAdjust;

    @Schema(description = "制造费用")
    private BigDecimal overheadCost;

    @Schema(description = "制造费用调整")
    private BigDecimal overheadCostAdjust;

    @Schema(description = "总成本")
    private BigDecimal totalCost;

    @Schema(description = "单位成本")
    private BigDecimal unitCost;

    @Schema(description = "成本币种")
    private String costCurrency;

    @Schema(description = "成本期间（YYYY-MM）")
    private String costPeriod;

    @Schema(description = "计算日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] calculationDate;

    @Schema(description = "最后调整日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastAdjustDate;

    @Schema(description = "状态：1-草稿，2-已计算，3-已确认", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}