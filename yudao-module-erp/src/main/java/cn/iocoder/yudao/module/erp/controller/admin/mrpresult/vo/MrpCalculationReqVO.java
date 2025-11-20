package cn.iocoder.yudao.module.erp.controller.admin.mrpresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * MRP运算请求 VO
 *
 * @author 芋道源码
 */
@Schema(description = "管理后台 - MRP运算请求 VO")
@Data
public class MrpCalculationReqVO {

    @Schema(description = "计划开始日期", example = "2025-01-01 00:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @NotNull(message = "计划开始日期不能为空")
    private LocalDateTime planStartDate;

    @Schema(description = "计划结束日期", example = "2025-03-31 23:59:59")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @NotNull(message = "计划结束日期不能为空")
    private LocalDateTime planEndDate;

    @Schema(description = "包含的产品ID列表（为空则计算所有产品）", example = "[1, 2, 3]")
    private List<Long> productIds;

    @Schema(description = "包含的仓库ID列表（为空则计算所有仓库）", example = "[1, 2]")
    private List<Long> warehouseIds;

    @Schema(description = "是否包含预测需求", example = "false")
    private Boolean includeForecast = false;

    @Schema(description = "是否考虑安全库存", example = "true")
    private Boolean considerSafetyStock = true;

    @Schema(description = "是否考虑在途数量", example = "true")
    private Boolean considerInTransit = true;

}

