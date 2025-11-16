package cn.iocoder.yudao.module.erp.controller.admin.productbom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP BOM主新增/修改 Request VO")
@Data
public class ProductBomSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30637")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17400")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "BOM编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "BOM编号不能为空")
    private String bomNo;

    @Schema(description = "BOM名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "BOM名称不能为空")
    private String bomName;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "生效日期")
    private LocalDateTime effectiveDate;

    @Schema(description = "失效日期")
    private LocalDateTime expireDate;

    @Schema(description = "BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM", example = "2")
    private Integer bomType;

    @Schema(description = "标准成本")
    private BigDecimal standardCost;

    @Schema(description = "总材料成本")
    private BigDecimal totalMaterialCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "1")
    private Integer status;

}