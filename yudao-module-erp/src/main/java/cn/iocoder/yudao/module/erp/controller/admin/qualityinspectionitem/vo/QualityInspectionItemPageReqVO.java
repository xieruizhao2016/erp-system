package cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检明细分页 Request VO")
@Data
public class QualityInspectionItemPageReqVO extends PageParam {

    @Schema(description = "检验ID", example = "6240")
    private Long inspectionId;

    @Schema(description = "检验项目ID", example = "16093")
    private Long itemId;

    @Schema(description = "样本编号")
    private String sampleNo;

    @Schema(description = "测量值")
    private String measuredValue;

    @Schema(description = "实际数值")
    private BigDecimal actualValue;

    @Schema(description = "检验结果：1-合格，2-不合格，3-超差")
    private Integer testResult;

    @Schema(description = "缺陷类型", example = "1")
    private String defectType;

    @Schema(description = "缺陷描述", example = "你说的对")
    private String defectDescription;

    @Schema(description = "缺陷图片URLs")
    private String imageUrls;

    @Schema(description = "检验员ID", example = "32178")
    private Long inspectorId;

    @Schema(description = "检验时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] inspectionTime;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}