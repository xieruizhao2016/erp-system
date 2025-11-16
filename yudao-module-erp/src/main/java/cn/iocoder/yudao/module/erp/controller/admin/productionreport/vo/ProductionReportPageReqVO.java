package cn.iocoder.yudao.module.erp.controller.admin.productionreport.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产报表分页 Request VO")
@Data
public class ProductionReportPageReqVO extends PageParam {

    @Schema(description = "报表编号")
    private String reportNo;

    @Schema(description = "报表名称", example = "李四")
    private String reportName;

    @Schema(description = "报表类型：1-日报，2-周报，3-月报，4-年报", example = "1")
    private Integer reportType;

    @Schema(description = "报表期间")
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

    @Schema(description = "报表日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate[] reportDate;

    @Schema(description = "生成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] generateTime;

    @Schema(description = "状态：1-草稿，2-已发布", example = "1")
    private Integer status;

    @Schema(description = "详细数据（JSON）")
    private String reportData;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}