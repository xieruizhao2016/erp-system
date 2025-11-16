package cn.iocoder.yudao.module.erp.controller.admin.coststandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 标准成本 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CostStandardRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25877")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24762")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "版本号")
    @ExcelProperty("版本号")
    private String version;

    @Schema(description = "生效日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生效日期")
    private LocalDateTime effectiveDate;

    @Schema(description = "失效日期")
    @ExcelProperty("失效日期")
    private LocalDateTime expireDate;

    @Schema(description = "材料成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("材料成本")
    private BigDecimal materialCost;

    @Schema(description = "人工成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("人工成本")
    private BigDecimal laborCost;

    @Schema(description = "制造费用", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("制造费用")
    private BigDecimal overheadCost;

    @Schema(description = "总成本", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成本")
    private BigDecimal totalCost;

    @Schema(description = "成本币种")
    @ExcelProperty("成本币种")
    private String costCurrency;

    @Schema(description = "计算日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计算日期")
    private LocalDateTime calculationDate;

    @Schema(description = "关联BOM版本")
    @ExcelProperty("关联BOM版本")
    private String bomVersion;

    @Schema(description = "关联工艺版本")
    @ExcelProperty("关联工艺版本")
    private String routeVersion;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    @ExcelProperty("状态：1-草稿，2-生效，3-失效")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}