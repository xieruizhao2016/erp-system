package cn.iocoder.yudao.module.erp.controller.admin.productoem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 产品OEM新增/修改 Request VO")
@Data
public class ProductOemSaveReqVO {

    @Schema(description = "OEM编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "产品OEM编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品OEM编码不能为空")
    private String oemCode;

    @Schema(description = "OEM名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "富士康OEM")
    @NotEmpty(message = "OEM名称不能为空")
    private String oemName;

    @Schema(description = "产品工厂名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "富士康科技集团")
    @NotEmpty(message = "产品工厂名称不能为空")
    private String factoryName;

    @Schema(description = "产品工厂编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品工厂编码不能为空")
    private String factoryCode;

    @Schema(description = "工厂地址", example = "广东省深圳市")
    private String factoryAddress;

    @Schema(description = "工厂联系人", example = "张经理")
    private String factoryContact;

    @Schema(description = "工厂联系电话", example = "0755-88888888")
    private String factoryPhone;

    @Schema(description = "工厂邮箱", example = "contact@factory.com")
    private String factoryEmail;

    @Schema(description = "生产能力", example = "年产100万件")
    private String productionCapacity;

    @Schema(description = "认证资质", example = "ISO9001")
    private String certification;

    @Schema(description = "合作开始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime cooperationStartDate;

    @Schema(description = "合作结束日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime cooperationEndDate;

    @Schema(description = "状态：0-禁用，1-启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "质量等级", example = "A级")
    private String qualityLevel;

    @Schema(description = "付款条款", example = "30天付款")
    private String paymentTerms;

    @Schema(description = "交货条款", example = "FOB")
    private String deliveryTerms;

    @Schema(description = "OEM LOGO URL")
    private String logoUrl;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "主要供应商")
    private String remark;

}

