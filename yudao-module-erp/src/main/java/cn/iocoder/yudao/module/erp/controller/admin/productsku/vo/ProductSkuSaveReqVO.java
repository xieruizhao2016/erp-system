package cn.iocoder.yudao.module.erp.controller.admin.productsku.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 产品SKU新增/修改 Request VO")
@Data
public class ProductSkuSaveReqVO {

    @Schema(description = "SKU编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "23545")
    private Long id;

    @Schema(description = "产品编号（关联产品，可选，支持多对多关系）", example = "1")
    private Long productId;

    @Schema(description = "SKU编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "SKU编码不能为空")
    private String skuCode;

    @Schema(description = "SKU名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "SKU名称不能为空")
    private String skuName;

    @Schema(description = "SKU描述", example = "随便")
    private String description;

    @Schema(description = "状态：0-开启，1-关闭", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "SKU条码")
    private String barCode;

    @Schema(description = "SKU规格", example = "标准规格")
    private String standard;

    @Schema(description = "单位编号", example = "1")
    private Long unitId;

    @Schema(description = "SKU图片URL", example = "https://www.iocoder.cn")
    private String imageUrl;

    @Schema(description = "规格参数（JSON格式）")
    private String specification;

    @Schema(description = "属性信息（JSON格式，如：颜色、尺寸等）")
    private String attributes;

    @Schema(description = "采购价格，单位：元", example = "1207")
    private BigDecimal purchasePrice;

    @Schema(description = "销售价格，单位：元", example = "5214")
    private BigDecimal salePrice;

    @Schema(description = "最低价格，单位：元", example = "12251")
    private BigDecimal minPrice;

    @Schema(description = "成本价格，单位：元", example = "6859")
    private BigDecimal costPrice;

    @Schema(description = "颜色", example = "红色")
    private String color;

    @Schema(description = "尺寸", example = "L")
    private String size;

    @Schema(description = "材质", example = "棉质")
    private String material;

    @Schema(description = "重量（kg）")
    private BigDecimal weight;

    @Schema(description = "体积（m³）")
    private BigDecimal volume;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注", example = "随便")
    private String remark;

}