package cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP MRP运算结果 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MrpResultRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "26152")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "运算批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("运算批次号")
    private String runNo;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28551")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "仓库ID", example = "32074")
    @ExcelProperty("仓库ID")
    private Long warehouseId;

    @Schema(description = "周期开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期开始日期")
    private LocalDate periodStartDate;

    @Schema(description = "周期结束日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期结束日期")
    private LocalDate periodEndDate;

    @Schema(description = "毛需求")
    @ExcelProperty("毛需求")
    private BigDecimal grossRequirement;

    @Schema(description = "计划接收量")
    @ExcelProperty("计划接收量")
    private BigDecimal scheduledReceipts;

    @Schema(description = "现有库存")
    @ExcelProperty("现有库存")
    private BigDecimal onHandInventory;

    @Schema(description = "净需求")
    @ExcelProperty("净需求")
    private BigDecimal netRequirement;

    @Schema(description = "计划订单接收量")
    @ExcelProperty("计划订单接收量")
    private BigDecimal plannedOrderReceipts;

    @Schema(description = "计划订单发放量")
    @ExcelProperty("计划订单发放量")
    private BigDecimal plannedOrderReleases;

    @Schema(description = "订单类型：1-生产订单，2-采购订单", example = "1")
    @ExcelProperty("订单类型：1-生产订单，2-采购订单")
    private Integer orderType;

    @Schema(description = "批量规则：1-固定批量，2-按需，3-最小-最大")
    @ExcelProperty("批量规则：1-固定批量，2-按需，3-最小-最大")
    private Integer lotSizingRule;

    @Schema(description = "提前期（天）")
    @ExcelProperty("提前期（天）")
    private Integer leadTime;

    @Schema(description = "安全库存")
    @ExcelProperty("安全库存")
    private BigDecimal safetyStock;

    @Schema(description = "订单状态：1-建议，2-确认，3-下达", example = "1")
    @ExcelProperty("订单状态：1-建议，2-确认，3-下达")
    private Integer orderStatus;

    @Schema(description = "需求日期")
    @ExcelProperty("需求日期")
    private LocalDate dueDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}