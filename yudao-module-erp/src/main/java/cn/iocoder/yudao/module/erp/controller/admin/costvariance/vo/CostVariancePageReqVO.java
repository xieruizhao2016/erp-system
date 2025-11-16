package cn.iocoder.yudao.module.erp.controller.admin.costvariance.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 成本差异分析分页 Request VO")
@Data
public class CostVariancePageReqVO extends PageParam {

    @Schema(description = "实际成本ID", example = "15646")
    private Long costActualId;

    @Schema(description = "产品ID", example = "29471")
    private Long productId;

    @Schema(description = "生产数量")
    private BigDecimal productionQuantity;

    @Schema(description = "标准总成本")
    private BigDecimal standardTotalCost;

    @Schema(description = "实际总成本")
    private BigDecimal actualTotalCost;

    @Schema(description = "总差异")
    private BigDecimal totalVariance;

    @Schema(description = "总差异率")
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

    @Schema(description = "分析日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] analysisDate;

    @Schema(description = "差异原因", example = "不对")
    private String varianceReason;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}