package cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 生产KPI Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionKpiRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4927")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "KPI编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("KPI编号")
    private String kpiNo;

    @Schema(description = "KPI名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("KPI名称")
    private String kpiName;

    @Schema(description = "KPI类型：1-OEE，2-合格率，3-达成率，4-交期率", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("KPI类型：1-OEE，2-合格率，3-达成率，4-交期率")
    private Integer kpiType;

    @Schema(description = "分类：1-效率，2-质量，3-交付，4-成本")
    @ExcelProperty("分类：1-效率，2-质量，3-交付，4-成本")
    private Integer category;

    @Schema(description = "工作中心ID", example = "321")
    @ExcelProperty("工作中心ID")
    private Long workCenterId;

    @Schema(description = "产品ID", example = "31509")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "统计周期：1-小时，2-天，3-周，4-月", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("统计周期：1-小时，2-天，3-周，4-月")
    private Integer measurementPeriod;

    @Schema(description = "目标值")
    @ExcelProperty("目标值")
    private BigDecimal targetValue;

    @Schema(description = "实际值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实际值")
    private BigDecimal actualValue;

    @Schema(description = "差异值")
    @ExcelProperty("差异值")
    private BigDecimal variance;

    @Schema(description = "差异率")
    @ExcelProperty("差异率")
    private BigDecimal varianceRate;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计算日期")
    private LocalDateTime calculationDate;

    @Schema(description = "周期开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期开始时间")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("周期结束时间")
    private LocalDateTime periodEndTime;

    @Schema(description = "数据来源")
    @ExcelProperty("数据来源")
    private String dataSource;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remarks;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}