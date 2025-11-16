package cn.iocoder.yudao.module.erp.controller.admin.equipmentstatus.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 设备状态记录新增/修改 Request VO")
@Data
public class EquipmentStatusSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5095")
    private Long id;

    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27342")
    @NotNull(message = "设备ID不能为空")
    private Long equipmentId;

    @Schema(description = "状态：1-运行，2-待机，3-故障，4-维修，5-停机", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态：1-运行，2-待机，3-故障，4-维修，5-停机不能为空")
    private Integer status;

    @Schema(description = "状态开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "状态开始时间不能为空")
    private LocalDateTime statusStartTime;

    @Schema(description = "状态结束时间")
    private LocalDateTime statusEndTime;

    @Schema(description = "持续时间（分钟）")
    private Integer duration;

    @Schema(description = "关联工单ID", example = "6790")
    private Long workOrderId;

    @Schema(description = "操作员ID", example = "21367")
    private Long operatorId;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}