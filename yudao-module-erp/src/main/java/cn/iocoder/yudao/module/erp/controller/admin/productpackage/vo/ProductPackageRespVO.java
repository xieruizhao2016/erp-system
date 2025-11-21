package cn.iocoder.yudao.module.erp.controller.admin.productpackage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 产品包装 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductPackageRespVO {

    @Schema(description = "包装编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("包装编号")
    private Long id;

    @Schema(description = "包装编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("包装编码")
    private String packageCode;

    @Schema(description = "包装名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "标准纸箱")
    @ExcelProperty("包装名称")
    private String packageName;

    @Schema(description = "包装描述", example = "适用于标准产品")
    @ExcelProperty("包装描述")
    private String description;

    @Schema(description = "状态：0-禁用，1-启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "装箱数量（件/箱）", example = "10")
    @ExcelProperty("装箱数量")
    private Integer quantityPerBox;

    @Schema(description = "单箱毛重（KG）", example = "5.5")
    @ExcelProperty("单箱毛重（KG）")
    private BigDecimal grossWeight;

    @Schema(description = "单箱净重（KG）", example = "5.0")
    @ExcelProperty("单箱净重（KG）")
    private BigDecimal netWeight;

    @Schema(description = "内箱尺寸（CM）", example = "30x20x15")
    @ExcelProperty("内箱尺寸（CM）")
    private String innerBoxSize;

    @Schema(description = "外箱尺寸（CM）", example = "32x22x17")
    @ExcelProperty("外箱尺寸（CM）")
    private String outerBoxSize;

    @Schema(description = "外箱体积（立方米）", example = "0.0119")
    @ExcelProperty("外箱体积（m³）")
    private BigDecimal boxVolume;

    @Schema(description = "托盘装箱数（箱/托盘）", example = "50")
    @ExcelProperty("托盘装箱数")
    private Integer palletQuantity;

    @Schema(description = "包装材料", example = "瓦楞纸")
    @ExcelProperty("包装材料")
    private String material;

    @Schema(description = "包装类型", example = "纸箱")
    @ExcelProperty("包装类型")
    private String packageType;

    @Schema(description = "包装条码")
    @ExcelProperty("包装条码")
    private String barcode;

    @Schema(description = "包装图片URL", example = "https://www.example.com/image.jpg")
    @ExcelProperty("包装图片URL")
    private String imageUrl;

    @Schema(description = "排序", example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "常用包装")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

