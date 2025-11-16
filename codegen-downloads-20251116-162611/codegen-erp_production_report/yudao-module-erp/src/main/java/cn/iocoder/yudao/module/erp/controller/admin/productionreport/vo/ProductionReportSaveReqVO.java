package cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 生产报表新增/修改 Request VO")
@Data
public class ProductionReportSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16916")
    private Long id;

    @Schema(description = "报表编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "报表编号不能为空")
    private String reportNo;

    @Schema(description = "报表名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "报表名称不能为空")
    private String reportName;

    @Schema(description = "报表类型：1-日报，2-周报，3-月报，4-年报", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "报表类型：1-日报，2-周报，3-月报，4-年报不能为空")
    private Integer reportType;

    @Schema(description = "报表期间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "报表期间不能为空")
    private String reportPeriod;

    @Schema(description = "工作中心ID", example = "10821")
    private Long workCenterId;

    @Schema(description = "生产订单数")
    private Integer productionOrders;

    @Schema(description = "总计划数量")
    private BigDecimal totalPlanQuantity;

    @Schema(description = "总完成数量")
    private BigDecimal totalCompletedQuantity;

    @Schema(description = "总合格数量")
    private BigDecimal totalQualifiedQuantity;

    @Schema(description = "完成率")
    private BigDecimal completionRate;

    @Schema(description = "合格率")
    private BigDecimal qualityRate;

    @Schema(description = "总工时")
    private BigDecimal totalWorkHours;

    @Schema(description = "总机时")
    private BigDecimal totalEquipmentHours;

    @Schema(description = "OEE")
    private BigDecimal oee;

    @Schema(description = "准时交付率")
    private BigDecimal onTimeDeliveryRate;

    @Schema(description = "产值")
    private BigDecimal productionValue;

    @Schema(description = "平均成本")
    private BigDecimal averageCost;

    @Schema(description = "报表日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报表日期不能为空")
    private LocalDate reportDate;

    @Schema(description = "生成时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生成时间不能为空")
    private LocalDateTime generateTime;

    @Schema(description = "状态：1-草稿，2-已发布", example = "1")
    private Integer status;

    @Schema(description = "详细数据（JSON）")
    private String reportData;

}