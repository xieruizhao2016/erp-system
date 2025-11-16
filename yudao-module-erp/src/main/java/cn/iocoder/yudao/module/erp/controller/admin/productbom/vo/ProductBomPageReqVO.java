package cn.iocoder.yudao.module.erp.controller.admin.productbom.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP BOM主分页 Request VO")
@Data
public class ProductBomPageReqVO extends PageParam {

    @Schema(description = "产品ID", example = "17400")
    private Long productId;

    @Schema(description = "BOM编号")
    private String bomNo;

    @Schema(description = "BOM名称", example = "芋艿")
    private String bomName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "生效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] effectiveDate;

    @Schema(description = "失效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] expireDate;

    @Schema(description = "BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM", example = "2")
    private Integer bomType;

    @Schema(description = "标准成本")
    private BigDecimal standardCost;

    @Schema(description = "总材料成本")
    private BigDecimal totalMaterialCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}