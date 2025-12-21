package cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - 资产负债表统计数据 Response VO")
@Data
public class ErpFinanceBalanceSheetStatisticsRespVO {

    @Schema(description = "资产总额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal assetTotal;

    @Schema(description = "负债总额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal liabilityTotal;

    @Schema(description = "所有者权益", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal equityTotal;

    @Schema(description = "资产构成数据")
    private List<AssetCompositionItem> assetComposition;

    @Schema(description = "负债构成数据")
    private List<LiabilityCompositionItem> liabilityComposition;

    @Schema(description = "月度趋势数据")
    private List<MonthlyTrendItem> monthlyTrend;

    @Data
    @Schema(description = "资产构成项")
    public static class AssetCompositionItem {
        @Schema(description = "名称")
        private String name;
        @Schema(description = "金额")
        private BigDecimal amount;
    }

    @Data
    @Schema(description = "负债构成项")
    public static class LiabilityCompositionItem {
        @Schema(description = "名称")
        private String name;
        @Schema(description = "金额")
        private BigDecimal amount;
    }

    @Data
    @Schema(description = "月度趋势项")
    public static class MonthlyTrendItem {
        @Schema(description = "月份（YYYY-MM）")
        private String month;
        @Schema(description = "资产总额")
        private BigDecimal assetTotal;
        @Schema(description = "负债总额")
        private BigDecimal liabilityTotal;
    }

}

