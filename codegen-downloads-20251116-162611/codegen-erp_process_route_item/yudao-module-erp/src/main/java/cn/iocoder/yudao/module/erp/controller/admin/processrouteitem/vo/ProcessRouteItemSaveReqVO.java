package cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 工艺路线明细新增/修改 Request VO")
@Data
public class ProcessRouteItemSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "922")
    private Long id;

    @Schema(description = "工艺路线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19016")
    @NotNull(message = "工艺路线ID不能为空")
    private Long routeId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3717")
    @NotNull(message = "工序ID不能为空")
    private Long processId;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "序号不能为空")
    private Integer sequence;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "工序名称不能为空")
    private String operationName;

    @Schema(description = "标准工时（分钟）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "标准工时（分钟）不能为空")
    private Integer standardTime;

    @Schema(description = "准备时间（分钟）")
    private Integer setupTime;

    @Schema(description = "人员数量", example = "16872")
    private Integer workerCount;

    @Schema(description = "设备ID", example = "990")
    private Long equipmentId;

    @Schema(description = "工作中心ID", example = "12614")
    private Long workCenterId;

    @Schema(description = "人工费率")
    private BigDecimal laborRate;

    @Schema(description = "制造费率")
    private BigDecimal overheadRate;

    @Schema(description = "是否瓶颈工序")
    private Boolean isBottleneck;

    @Schema(description = "是否需要质检")
    private Boolean qualityCheckRequired;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}