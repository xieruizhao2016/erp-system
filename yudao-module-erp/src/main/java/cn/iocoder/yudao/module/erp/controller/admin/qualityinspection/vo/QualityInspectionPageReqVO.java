package cn.iocoder.yudao.module.erp.controller.admin.qualityinspection.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检记录分页 Request VO")
@Data
public class QualityInspectionPageReqVO extends PageParam {

    @Schema(description = "检验单号")
    private String inspectionNo;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "产品ID", example = "15434")
    private Long productId;

    @Schema(description = "工序ID", example = "13581")
    private Long processId;

    @Schema(description = "工单ID", example = "15322")
    private Long workOrderId;

    @Schema(description = "检验类型：1-进料检验，2-过程检验，3-成品检验", example = "2")
    private Integer inspectionType;

    @Schema(description = "检验级别：1-全检，2-抽检")
    private Integer inspectionLevel;

    @Schema(description = "批量大小")
    private Integer lotSize;

    @Schema(description = "样本数量")
    private Integer sampleSize;

    @Schema(description = "合格数量")
    private Integer qualifiedQuantity;

    @Schema(description = "不合格数量")
    private Integer rejectedQuantity;

    @Schema(description = "报废数量")
    private Integer scrapQuantity;

    @Schema(description = "检验结果：1-合格，2-不合格，3-待复检")
    private Integer inspectionResult;

    @Schema(description = "检验员ID", example = "14966")
    private Long inspectorId;

    @Schema(description = "检验时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inspectionTime;

    @Schema(description = "检验环境")
    private String environment;

    @Schema(description = "检验设备")
    private String equipment;

    @Schema(description = "检验备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}