package cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 质检项目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QualityItemRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25866")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "标准ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19958")
    @ExcelProperty("标准ID")
    private Long standardId;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("项目编号")
    private String itemNo;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("项目名称")
    private String itemName;

    @Schema(description = "项目类型：1-定性，2-定量", example = "1")
    @ExcelProperty("项目类型：1-定性，2-定量")
    private Integer itemType;

    @Schema(description = "检验方法")
    @ExcelProperty("检验方法")
    private String testMethod;

    @Schema(description = "标准值")
    @ExcelProperty("标准值")
    private String standardValue;

    @Schema(description = "公差范围")
    @ExcelProperty("公差范围")
    private String tolerance;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "是否关键项")
    @ExcelProperty("是否关键项")
    private Boolean isKeyItem;

    @Schema(description = "检验序号")
    @ExcelProperty("检验序号")
    private Integer sequence;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}