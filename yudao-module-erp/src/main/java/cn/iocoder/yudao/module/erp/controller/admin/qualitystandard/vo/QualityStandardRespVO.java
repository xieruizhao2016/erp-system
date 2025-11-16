package cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检标准 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QualityStandardRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7038")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "标准编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标准编号")
    private String standardNo;

    @Schema(description = "标准名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("标准名称")
    private String standardName;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29166")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "工序ID", example = "24223")
    @ExcelProperty("工序ID")
    private Long processId;

    @Schema(description = "检验类型：1-进料检验，2-过程检验，3-成品检验", example = "1")
    @ExcelProperty("检验类型：1-进料检验，2-过程检验，3-成品检验")
    private Integer inspectionType;

    @Schema(description = "标准版本")
    @ExcelProperty("标准版本")
    private String standardVersion;

    @Schema(description = "AQL水平")
    @ExcelProperty("AQL水平")
    private String aqlLevel;

    @Schema(description = "抽样方法")
    @ExcelProperty("抽样方法")
    private String samplingMethod;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    @ExcelProperty("状态：1-草稿，2-生效，3-失效")
    private Integer status;

    @Schema(description = "标准描述", example = "随便")
    @ExcelProperty("标准描述")
    private String description;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}