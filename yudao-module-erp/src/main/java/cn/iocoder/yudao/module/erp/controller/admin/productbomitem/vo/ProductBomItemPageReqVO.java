package cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP BOM明细分页 Request VO")
@Data
public class ProductBomItemPageReqVO extends PageParam {

    @Schema(description = "BOM ID", example = "2698")
    private Long bomId;

    @Schema(description = "父产品ID", example = "17549")
    private Long parentProductId;

    @Schema(description = "子产品ID", example = "2300")
    private Long childProductId;

    @Schema(description = "子产品名称", example = "李四")
    private String childProductName;

    @Schema(description = "子产品规格")
    private String childProductSpec;

    @Schema(description = "单位ID", example = "31214")
    private Long unitId;

    @Schema(description = "用量")
    private BigDecimal quantity;

    @Schema(description = "损耗率")
    private BigDecimal lossRate;

    @Schema(description = "有效用量")
    private BigDecimal effectiveQuantity;

    @Schema(description = "是否关键物料")
    private Boolean isKeyMaterial;

    @Schema(description = "是否替代料")
    private Boolean isAlternative;

    @Schema(description = "替代料组")
    private String alternativeGroup;

    @Schema(description = "位号")
    private Integer position;

    @Schema(description = "工序ID", example = "9522")
    private Long processId;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}