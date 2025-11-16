package cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QualityInspectionItemRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21059")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "检验ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6240")
    @ExcelProperty("检验ID")
    private Long inspectionId;

    @Schema(description = "检验项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16093")
    @ExcelProperty("检验项目ID")
    private Long itemId;

    @Schema(description = "样本编号")
    @ExcelProperty("样本编号")
    private String sampleNo;

    @Schema(description = "测量值")
    @ExcelProperty("测量值")
    private String measuredValue;

    @Schema(description = "实际数值")
    @ExcelProperty("实际数值")
    private BigDecimal actualValue;

    @Schema(description = "检验结果：1-合格，2-不合格，3-超差")
    @ExcelProperty("检验结果：1-合格，2-不合格，3-超差")
    private Integer testResult;

    @Schema(description = "缺陷类型", example = "1")
    @ExcelProperty("缺陷类型")
    private String defectType;

    @Schema(description = "缺陷描述", example = "你说的对")
    @ExcelProperty("缺陷描述")
    private String defectDescription;

    @Schema(description = "缺陷图片URLs")
    @ExcelProperty("缺陷图片URLs")
    private String imageUrls;

    @Schema(description = "检验员ID", example = "32178")
    @ExcelProperty("检验员ID")
    private Long inspectorId;

    @Schema(description = "检验时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("检验时间")
    private LocalDateTime inspectionTime;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}