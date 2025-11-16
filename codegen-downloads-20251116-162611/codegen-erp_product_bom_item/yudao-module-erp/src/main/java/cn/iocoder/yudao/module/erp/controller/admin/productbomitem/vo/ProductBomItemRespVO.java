package cn.iocoder.yudao.module.erp.controller.admin.productbomitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP BOM明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductBomItemRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "BOM ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2698")
    @ExcelProperty("BOM ID")
    private Long bomId;

    @Schema(description = "父产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17549")
    @ExcelProperty("父产品ID")
    private Long parentProductId;

    @Schema(description = "子产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2300")
    @ExcelProperty("子产品ID")
    private Long childProductId;

    @Schema(description = "子产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("子产品名称")
    private String childProductName;

    @Schema(description = "子产品规格")
    @ExcelProperty("子产品规格")
    private String childProductSpec;

    @Schema(description = "单位ID", example = "31214")
    @ExcelProperty("单位ID")
    private Long unitId;

    @Schema(description = "用量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("用量")
    private BigDecimal quantity;

    @Schema(description = "损耗率")
    @ExcelProperty("损耗率")
    private BigDecimal lossRate;

    @Schema(description = "有效用量")
    @ExcelProperty("有效用量")
    private BigDecimal effectiveQuantity;

    @Schema(description = "是否关键物料")
    @ExcelProperty("是否关键物料")
    private Boolean isKeyMaterial;

    @Schema(description = "是否替代料")
    @ExcelProperty("是否替代料")
    private Boolean isAlternative;

    @Schema(description = "替代料组")
    @ExcelProperty("替代料组")
    private String alternativeGroup;

    @Schema(description = "位号")
    @ExcelProperty("位号")
    private Integer position;

    @Schema(description = "工序ID", example = "9522")
    @ExcelProperty("工序ID")
    private Long processId;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}