package cn.iocoder.yudao.module.erp.controller.admin.productoem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 产品OEM Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductOemRespVO {

    @Schema(description = "OEM编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("OEM编号")
    private Long id;

    @Schema(description = "产品OEM编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("OEM编码")
    private String oemCode;

    @Schema(description = "OEM名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "富士康OEM")
    @ExcelProperty("OEM名称")
    private String oemName;

    @Schema(description = "产品工厂名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "富士康科技集团")
    @ExcelProperty("工厂名称")
    private String factoryName;

    @Schema(description = "产品工厂编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工厂编码")
    private String factoryCode;

    @Schema(description = "工厂地址", example = "广东省深圳市")
    @ExcelProperty("工厂地址")
    private String factoryAddress;

    @Schema(description = "工厂联系人", example = "张经理")
    @ExcelProperty("联系人")
    private String factoryContact;

    @Schema(description = "工厂联系电话", example = "0755-88888888")
    @ExcelProperty("联系电话")
    private String factoryPhone;

    @Schema(description = "工厂邮箱", example = "contact@factory.com")
    @ExcelProperty("工厂邮箱")
    private String factoryEmail;

    @Schema(description = "生产能力", example = "年产100万件")
    @ExcelProperty("生产能力")
    private String productionCapacity;

    @Schema(description = "认证资质", example = "ISO9001")
    @ExcelProperty("认证资质")
    private String certification;

    @Schema(description = "合作开始日期")
    @ExcelProperty("合作开始日期")
    private LocalDateTime cooperationStartDate;

    @Schema(description = "合作结束日期")
    @ExcelProperty("合作结束日期")
    private LocalDateTime cooperationEndDate;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "质量等级", example = "A级")
    @ExcelProperty("质量等级")
    private String qualityLevel;

    @Schema(description = "付款条款", example = "30天付款")
    @ExcelProperty("付款条款")
    private String paymentTerms;

    @Schema(description = "交货条款", example = "FOB")
    @ExcelProperty("交货条款")
    private String deliveryTerms;

    @Schema(description = "OEM LOGO URL")
    private String logoUrl;

    @Schema(description = "排序", example = "1")
    @ExcelProperty("排序")
    private Integer sort;

    @Schema(description = "备注", example = "主要供应商")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

