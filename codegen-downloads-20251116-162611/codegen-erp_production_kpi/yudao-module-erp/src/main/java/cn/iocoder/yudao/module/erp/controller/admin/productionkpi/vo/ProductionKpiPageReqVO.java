package cn.iocoder.yudao.module.erp.controller.admin.productionkpi.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 生产KPI分页 Request VO")
@Data
public class ProductionKpiPageReqVO extends PageParam {

    @Schema(description = "KPI编号")
    private String kpiNo;

    @Schema(description = "KPI名称", example = "芋艿")
    private String kpiName;

    @Schema(description = "KPI类型：1-OEE，2-合格率，3-达成率，4-交期率", example = "2")
    private Integer kpiType;

    @Schema(description = "分类：1-效率，2-质量，3-交付，4-成本")
    private Integer category;

    @Schema(description = "工作中心ID", example = "321")
    private Long workCenterId;

    @Schema(description = "产品ID", example = "31509")
    private Long productId;

    @Schema(description = "统计周期：1-小时，2-天，3-周，4-月")
    private Integer measurementPeriod;

    @Schema(description = "目标值")
    private BigDecimal targetValue;

    @Schema(description = "实际值")
    private BigDecimal actualValue;

    @Schema(description = "差异值")
    private BigDecimal variance;

    @Schema(description = "差异率")
    private BigDecimal varianceRate;

    @Schema(description = "计算日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] calculationDate;

    @Schema(description = "周期开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodStartTime;

    @Schema(description = "周期结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] periodEndTime;

    @Schema(description = "数据来源")
    private String dataSource;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}