package cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP MRP参数 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MrpParamsRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "29606")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("参数名称")
    private String paramName;

    @Schema(description = "参数编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("参数编码")
    private String paramCode;

    @Schema(description = "参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("参数值")
    private String paramValue;

    @Schema(description = "参数类型：1-字符串，2-数字，3-日期，4-布尔", example = "1")
    @ExcelProperty("参数类型：1-字符串，2-数字，3-日期，4-布尔")
    private Integer paramType;

    @Schema(description = "参数描述", example = "你说的对")
    @ExcelProperty("参数描述")
    private String description;

    @Schema(description = "是否系统参数")
    @ExcelProperty("是否系统参数")
    private Boolean isSystem;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}