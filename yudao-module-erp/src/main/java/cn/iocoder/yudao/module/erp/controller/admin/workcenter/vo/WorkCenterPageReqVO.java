package cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 工作中心分页 Request VO")
@Data
public class WorkCenterPageReqVO extends PageParam {

    @Schema(description = "工作中心编号")
    private String workCenterNo;

    @Schema(description = "工作中心名称", example = "加工中心")
    private String workCenterName;

    @Schema(description = "状态：1-启用，2-停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}

