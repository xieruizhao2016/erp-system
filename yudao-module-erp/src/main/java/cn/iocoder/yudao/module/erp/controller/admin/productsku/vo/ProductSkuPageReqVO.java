package cn.iocoder.yudao.module.erp.controller.admin.productsku.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 产品SKU分页 Request VO")
@Data
public class ProductSkuPageReqVO extends PageParam {

    @Schema(description = "SKU编码")
    private String skuCode;

    @Schema(description = "SKU名称", example = "赵六")
    private String skuName;

    @Schema(description = "SKU描述", example = "随便")
    private String description;

    @Schema(description = "状态：0-禁用，1-启用", example = "1")
    private Integer status;

    @Schema(description = "SKU条码")
    private String barCode;

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

    @Schema(description = "重量（kg）")
    private BigDecimal weight;

    @Schema(description = "体积（m³）")
    private BigDecimal volume;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}