package cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP MRP运算结果分页 Request VO")
@Data
public class MrpResultPageReqVO extends PageParam {

    @Schema(description = "运算批次号")
    private String runNo;

    @Schema(description = "产品ID", example = "28551")
    private Long productId;

    @Schema(description = "仓库ID", example = "32074")
    private Long warehouseId;

    @Schema(description = "周期开始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] periodStartDate;

    @Schema(description = "周期结束日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] periodEndDate;

    @Schema(description = "毛需求")
    private BigDecimal grossRequirement;

    @Schema(description = "计划接收量")
    private BigDecimal scheduledReceipts;

    @Schema(description = "现有库存")
    private BigDecimal onHandInventory;

    @Schema(description = "净需求")
    private BigDecimal netRequirement;

    @Schema(description = "计划订单接收量")
    private BigDecimal plannedOrderReceipts;

    @Schema(description = "计划订单发放量")
    private BigDecimal plannedOrderReleases;

    @Schema(description = "订单类型：1-生产订单，2-采购订单", example = "1")
    private Integer orderType;

    @Schema(description = "批量规则：1-固定批量，2-按需，3-最小-最大")
    private Integer lotSizingRule;

    @Schema(description = "提前期（天）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] leadTime;

    @Schema(description = "安全库存")
    private BigDecimal safetyStock;

    @Schema(description = "订单状态：1-建议，2-确认，3-下达", example = "1")
    private Integer orderStatus;

    @Schema(description = "需求日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] dueDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}