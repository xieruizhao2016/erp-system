package cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP BOM明细新增/修改 Request VO")
@Data
public class ProductBomItemSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13")
    private Long id;

    @Schema(description = "BOM ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2698")
    @NotNull(message = "BOM ID不能为空")
    private Long bomId;

    @Schema(description = "父产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17549")
    @NotNull(message = "父产品ID不能为空")
    private Long parentProductId;

    @Schema(description = "子产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2300")
    @NotNull(message = "子产品ID不能为空")
    private Long childProductId;

    @Schema(description = "子产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "子产品名称不能为空")
    private String childProductName;

    @Schema(description = "子产品规格")
    private String childProductSpec;

    @Schema(description = "单位ID", example = "31214")
    private Long unitId;

    @Schema(description = "用量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "用量不能为空")
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

}