package cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 设备状态记录分页 Request VO")
@Data
public class EquipmentStatusPageReqVO extends PageParam {

    @Schema(description = "设备ID", example = "27342")
    private Long equipmentId;

    @Schema(description = "状态：1-运行，2-待机，3-故障，4-维修，5-停机", example = "1")
    private Integer status;

    @Schema(description = "状态开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] statusStartTime;

    @Schema(description = "状态结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] statusEndTime;

    @Schema(description = "持续时间（分钟）")
    private Integer duration;

    @Schema(description = "关联工单ID", example = "6790")
    private Long workOrderId;

    @Schema(description = "操作员ID", example = "21367")
    private Long operatorId;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}