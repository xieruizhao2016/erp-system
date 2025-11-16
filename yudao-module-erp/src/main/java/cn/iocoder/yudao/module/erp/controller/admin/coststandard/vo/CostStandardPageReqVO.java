package cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 标准成本分页 Request VO")
@Data
public class CostStandardPageReqVO extends PageParam {

    @Schema(description = "产品ID", example = "24762")
    private Long productId;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "生效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] effectiveDate;

    @Schema(description = "失效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expireDate;

    @Schema(description = "材料成本")
    private BigDecimal materialCost;

    @Schema(description = "人工成本")
    private BigDecimal laborCost;

    @Schema(description = "制造费用")
    private BigDecimal overheadCost;

    @Schema(description = "总成本")
    private BigDecimal totalCost;

    @Schema(description = "成本币种")
    private String costCurrency;

    @Schema(description = "计算日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] calculationDate;

    @Schema(description = "关联BOM版本")
    private String bomVersion;

    @Schema(description = "关联工艺版本")
    private String routeVersion;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}