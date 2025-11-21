package cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 产品包装新增/修改 Request VO")
@Data
public class ProductPackageSaveReqVO {

    @Schema(description = "包装编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "包装编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "包装编码不能为空")
    private String packageCode;

    @Schema(description = "包装名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "标准纸箱")
    @NotEmpty(message = "包装名称不能为空")
    private String packageName;

    @Schema(description = "包装描述", example = "适用于标准产品")
    private String description;

    @Schema(description = "状态：0-禁用，1-启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "装箱数量（件/箱）", example = "10")
    private Integer quantityPerBox;

    @Schema(description = "单箱毛重（KG）", example = "5.5")
    private BigDecimal grossWeight;

    @Schema(description = "单箱净重（KG）", example = "5.0")
    private BigDecimal netWeight;

    @Schema(description = "内箱尺寸（CM）", example = "30x20x15")
    private String innerBoxSize;

    @Schema(description = "外箱尺寸（CM）", example = "32x22x17")
    private String outerBoxSize;

    @Schema(description = "外箱体积（立方米）", example = "0.0119")
    private BigDecimal boxVolume;

    @Schema(description = "托盘装箱数（箱/托盘）", example = "50")
    private Integer palletQuantity;

    @Schema(description = "包装材料", example = "瓦楞纸")
    private String material;

    @Schema(description = "包装类型", example = "纸箱")
    private String packageType;

    @Schema(description = "包装条码")
    private String barcode;

    @Schema(description = "包装图片URL", example = "https://www.example.com/image.jpg")
    private String imageUrl;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "常用包装")
    private String remark;

}

