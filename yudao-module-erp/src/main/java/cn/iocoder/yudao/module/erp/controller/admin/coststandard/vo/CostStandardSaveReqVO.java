package cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 标准成本新增/修改 Request VO")
@Data
public class CostStandardSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25877")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24762")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "生效日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生效日期不能为空")
    private LocalDateTime effectiveDate;

    @Schema(description = "失效日期")
    private LocalDateTime expireDate;

    @Schema(description = "材料成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "材料成本不能为空")
    private BigDecimal materialCost;

    @Schema(description = "人工成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "人工成本不能为空")
    private BigDecimal laborCost;

    @Schema(description = "制造费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "制造费用不能为空")
    private BigDecimal overheadCost;

    @Schema(description = "总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总成本不能为空")
    private BigDecimal totalCost;

    @Schema(description = "成本币种")
    private String costCurrency;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计算日期不能为空")
    private LocalDateTime calculationDate;

    @Schema(description = "关联BOM版本")
    private String bomVersion;

    @Schema(description = "关联工艺版本")
    private String routeVersion;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}