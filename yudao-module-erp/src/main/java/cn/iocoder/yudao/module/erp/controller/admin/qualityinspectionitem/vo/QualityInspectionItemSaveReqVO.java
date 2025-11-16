package cn.iocoder.yudao.module.erp.controller.admin.qualityinspectionitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Schema(description = "管理后台 - ERP 质检明细新增/修改 Request VO")
@Data
public class QualityInspectionItemSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "21059")
    private Long id;

    @Schema(description = "检验ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6240")
    @NotNull(message = "检验ID不能为空")
    private Long inspectionId;

    @Schema(description = "检验项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16093")
    @NotNull(message = "检验项目ID不能为空")
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

    @Schema(description = "检验时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "检验时间不能为空")
    private LocalDateTime inspectionTime;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}