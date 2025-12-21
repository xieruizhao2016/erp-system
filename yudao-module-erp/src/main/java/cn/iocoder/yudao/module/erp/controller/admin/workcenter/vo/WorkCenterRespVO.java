package cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 工作中心 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkCenterRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工作中心编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工作中心编号")
    private String workCenterNo;

    @Schema(description = "工作中心名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "加工中心")
    @ExcelProperty("工作中心名称")
    private String workCenterName;

    @Schema(description = "描述", example = "主要负责机械加工")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "位置", example = "1号车间")
    @ExcelProperty("位置")
    private String location;

    @Schema(description = "负责人", example = "张三")
    @ExcelProperty("负责人")
    private String responsiblePerson;

    @Schema(description = "状态：1-启用，2-停用", example = "1")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "备注", example = "备注信息")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}

