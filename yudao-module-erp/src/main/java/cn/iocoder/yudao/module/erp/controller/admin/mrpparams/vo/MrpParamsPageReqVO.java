package cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP MRP参数分页 Request VO")
@Data
public class MrpParamsPageReqVO extends PageParam {

    @Schema(description = "参数名称", example = "芋艿")
    private String paramName;

    @Schema(description = "参数编码")
    private String paramCode;

    @Schema(description = "参数值")
    private String paramValue;

    @Schema(description = "参数类型：1-字符串，2-数字，3-日期，4-布尔", example = "1")
    private Integer paramType;

    @Schema(description = "参数描述", example = "你说的对")
    private String description;

    @Schema(description = "是否系统参数")
    private Boolean isSystem;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}