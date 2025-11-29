package cn.iocoder.yudao.module.erp.controller.admin.process.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 工序 Response VO")
@Data
public class ProcessRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "工序编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "P001")
    private String processNo;

    @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "加工")
    private String processName;

    @Schema(description = "工序类型：1-加工，2-装配，3-检验，4-包装，5-其他", example = "1")
    private Integer processType;

    @Schema(description = "工序分类", example = "机加工")
    private String category;

    @Schema(description = "标准工时（分钟）", example = "60")
    private Integer standardTime;

    @Schema(description = "准备时间（分钟）", example = "10")
    private Integer setupTime;

    @Schema(description = "标准人员数量", example = "2")
    private Integer workerCount;

    @Schema(description = "设备类型", example = "CNC机床")
    private String equipmentType;

    @Schema(description = "工作中心ID", example = "1")
    private Long workCenterId;

    @Schema(description = "人工费率（元/小时）", example = "50.00")
    private BigDecimal laborRate;

    @Schema(description = "制造费率（元/小时）", example = "30.00")
    private BigDecimal overheadRate;

    @Schema(description = "是否需要质检", example = "true")
    private Boolean qualityCheckRequired;

    @Schema(description = "是否瓶颈工序", example = "false")
    private Boolean isBottleneck;

    @Schema(description = "工序描述", example = "零件加工工序")
    private String description;

    @Schema(description = "备注", example = "注意安全")
    private String remark;

    @Schema(description = "状态：1-启用，2-停用", example = "1")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}



