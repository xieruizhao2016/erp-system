package cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 工艺路线明细 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessRouteItemRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "922")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工艺路线ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19016")
    @ExcelProperty("工艺路线ID")
    private Long routeId;

    @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3717")
    @ExcelProperty("工序ID")
    private Long processId;

    @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("序号")
    private Integer sequence;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("工序名称")
    private String operationName;

    @Schema(description = "标准工时（分钟）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("标准工时（分钟）")
    private Integer standardTime;

    @Schema(description = "准备时间（分钟）")
    @ExcelProperty("准备时间（分钟）")
    private Integer setupTime;

    @Schema(description = "人员数量", example = "16872")
    @ExcelProperty("人员数量")
    private Integer workerCount;

    @Schema(description = "设备ID", example = "990")
    @ExcelProperty("设备ID")
    private Long equipmentId;

    @Schema(description = "工作中心ID", example = "12614")
    @ExcelProperty("工作中心ID")
    private Long workCenterId;

    @Schema(description = "人工费率")
    @ExcelProperty("人工费率")
    private BigDecimal laborRate;

    @Schema(description = "制造费率")
    @ExcelProperty("制造费率")
    private BigDecimal overheadRate;

    @Schema(description = "是否瓶颈工序")
    @ExcelProperty("是否瓶颈工序")
    private Boolean isBottleneck;

    @Schema(description = "是否需要质检")
    @ExcelProperty("是否需要质检")
    private Boolean qualityCheckRequired;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}