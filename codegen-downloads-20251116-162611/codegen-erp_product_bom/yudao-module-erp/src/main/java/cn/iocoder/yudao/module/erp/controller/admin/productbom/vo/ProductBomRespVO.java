package cn.iocoder.yudao.module.erp.controller.admin.productbom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP BOM主 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductBomRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30637")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17400")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "BOM编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("BOM编号")
    private String bomNo;

    @Schema(description = "BOM名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("BOM名称")
    private String bomName;

    @Schema(description = "版本号")
    @ExcelProperty("版本号")
    private String version;

    @Schema(description = "生效日期")
    @ExcelProperty("生效日期")
    private LocalDateTime effectiveDate;

    @Schema(description = "失效日期")
    @ExcelProperty("失效日期")
    private LocalDateTime expireDate;

    @Schema(description = "BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM", example = "2")
    @ExcelProperty("BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM")
    private Integer bomType;

    @Schema(description = "标准成本")
    @ExcelProperty("标准成本")
    private BigDecimal standardCost;

    @Schema(description = "总材料成本")
    @ExcelProperty("总材料成本")
    private BigDecimal totalMaterialCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "1")
    @ExcelProperty("状态：1-草稿，2-生效，3-失效")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}