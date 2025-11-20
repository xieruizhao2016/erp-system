package cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MRP运算结果 VO
 *
 * @author 芋道源码
 */
@Schema(description = "管理后台 - MRP运算结果 VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MrpCalculationResultVO {

    @Schema(description = "运算批次号", example = "MRP-20250120-001")
    private String runNo;

    @Schema(description = "是否运算成功", example = "true")
    private Boolean success;

    @Schema(description = "运算耗时（毫秒）", example = "1234")
    private Long calculationTime;

    @Schema(description = "运算消息", example = "MRP运算成功完成")
    private String message;

    @Schema(description = "运算结果明细列表")
    private List<MrpResultItemVO> results;

    @Schema(description = "统计信息")
    private MrpStatisticsVO statistics;

    /**
     * MRP运算结果项 VO
     */
    @Schema(description = "MRP运算结果项")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MrpResultItemVO {

        @Schema(description = "产品ID", example = "1")
        private Long productId;

        @Schema(description = "产品名称", example = "智能手环")
        private String productName;

        @Schema(description = "仓库ID", example = "1")
        private Long warehouseId;

        @Schema(description = "仓库名称", example = "成品仓")
        private String warehouseName;

        @Schema(description = "毛需求", example = "100.00")
        private String grossRequirement;

        @Schema(description = "现有库存", example = "50.00")
        private String onHandInventory;

        @Schema(description = "计划接收量", example = "30.00")
        private String scheduledReceipts;

        @Schema(description = "净需求", example = "20.00")
        private String netRequirement;

        @Schema(description = "计划订单发放量", example = "20.00")
        private String plannedOrderReleases;

        @Schema(description = "订单类型：1-生产订单，2-采购订单", example = "1")
        private Integer orderType;

        @Schema(description = "建议操作", example = "创建生产订单")
        private String suggestion;

    }

    /**
     * MRP统计信息 VO
     */
    @Schema(description = "MRP统计信息")
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MrpStatisticsVO {

        @Schema(description = "总产品数", example = "10")
        private Integer totalProducts;

        @Schema(description = "需要生产的产品数", example = "5")
        private Integer needProductionCount;

        @Schema(description = "需要采购的产品数", example = "3")
        private Integer needPurchaseCount;

        @Schema(description = "库存充足的产品数", example = "2")
        private Integer sufficientStockCount;

    }

}

