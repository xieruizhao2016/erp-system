package cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 生产KPI新增/修改 Request VO")
@Data
public class ProductionKpiSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4927")
    private Long id;

    @Schema(description = "KPI编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "KPI编号不能为空")
    private String kpiNo;

    @Schema(description = "KPI名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "KPI名称不能为空")
    private String kpiName;

    @Schema(description = "KPI类型：1-OEE，2-合格率，3-达成率，4-交期率", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "KPI类型：1-OEE，2-合格率，3-达成率，4-交期率不能为空")
    private Integer kpiType;

    @Schema(description = "分类：1-效率，2-质量，3-交付，4-成本")
    private Integer category;

    @Schema(description = "工作中心ID", example = "321")
    private Long workCenterId;

    @Schema(description = "产品ID", example = "31509")
    private Long productId;

    @Schema(description = "统计周期：1-小时，2-天，3-周，4-月", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "统计周期：1-小时，2-天，3-周，4-月不能为空")
    private Integer measurementPeriod;

    @Schema(description = "目标值")
    private BigDecimal targetValue;

    @Schema(description = "实际值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实际值不能为空")
    private BigDecimal actualValue;

    @Schema(description = "差异值")
    private BigDecimal variance;

    @Schema(description = "差异率")
    private BigDecimal varianceRate;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计算日期不能为空")
    private LocalDateTime calculationDate;

    @Schema(description = "周期开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期开始时间不能为空")
    private LocalDateTime periodStartTime;

    @Schema(description = "周期结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "周期结束时间不能为空")
    private LocalDateTime periodEndTime;

    @Schema(description = "数据来源")
    private String dataSource;

    @Schema(description = "备注")
    private String remarks;

}