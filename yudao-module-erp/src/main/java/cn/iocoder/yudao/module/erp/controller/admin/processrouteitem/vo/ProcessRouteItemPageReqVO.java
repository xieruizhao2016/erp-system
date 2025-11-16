package cn.iocoder.yudao.module.erp.controller.admin.processrouteitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 工艺路线明细分页 Request VO")
@Data
public class ProcessRouteItemPageReqVO extends PageParam {

    @Schema(description = "工艺路线ID", example = "19016")
    private Long routeId;

    @Schema(description = "工序ID", example = "3717")
    private Long processId;

    @Schema(description = "序号")
    private Integer sequence;

    @Schema(description = "工序名称", example = "张三")
    private String operationName;

    @Schema(description = "标准工时（分钟）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] standardTime;

    @Schema(description = "准备时间（分钟）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] setupTime;

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}