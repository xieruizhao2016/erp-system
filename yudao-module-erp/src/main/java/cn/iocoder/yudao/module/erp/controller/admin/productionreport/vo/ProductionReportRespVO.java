package cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产报表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionReportRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16916")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "报表编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("报表编号")
    private String reportNo;

    @Schema(description = "报表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("报表名称")
    private String reportName;

    @Schema(description = "报表类型：1-日报，2-周报，3-月报，4-年报", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("报表类型：1-日报，2-周报，3-月报，4-年报")
    private Integer reportType;

    @Schema(description = "报表期间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("报表期间")
    private String reportPeriod;

    @Schema(description = "工作中心ID", example = "10821")
    @ExcelProperty("工作中心ID")
    private Long workCenterId;

    @Schema(description = "生产订单数")
    @ExcelProperty("生产订单数")
    private Integer productionOrders;

    @Schema(description = "总计划数量")
    @ExcelProperty("总计划数量")
    private BigDecimal totalPlanQuantity;

    @Schema(description = "总完成数量")
    @ExcelProperty("总完成数量")
    private BigDecimal totalCompletedQuantity;

    @Schema(description = "总合格数量")
    @ExcelProperty("总合格数量")
    private BigDecimal totalQualifiedQuantity;

    @Schema(description = "完成率")
    @ExcelProperty("完成率")
    private BigDecimal completionRate;

    @Schema(description = "合格率")
    @ExcelProperty("合格率")
    private BigDecimal qualityRate;

    @Schema(description = "总工时")
    @ExcelProperty("总工时")
    private BigDecimal totalWorkHours;

    @Schema(description = "总机时")
    @ExcelProperty("总机时")
    private BigDecimal totalEquipmentHours;

    @Schema(description = "OEE")
    @ExcelProperty("OEE")
    private BigDecimal oee;

    @Schema(description = "准时交付率")
    @ExcelProperty("准时交付率")
    private BigDecimal onTimeDeliveryRate;

    @Schema(description = "产值")
    @ExcelProperty("产值")
    private BigDecimal productionValue;

    @Schema(description = "平均成本")
    @ExcelProperty("平均成本")
    private BigDecimal averageCost;

    @Schema(description = "报表日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("报表日期")
    private LocalDate reportDate;

    @Schema(description = "生成时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "状态：1-草稿，2-已发布", example = "1")
    @ExcelProperty("状态：1-草稿，2-已发布")
    private Integer status;

    @Schema(description = "详细数据（JSON）")
    @ExcelProperty("详细数据（JSON）")
    private String reportData;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}