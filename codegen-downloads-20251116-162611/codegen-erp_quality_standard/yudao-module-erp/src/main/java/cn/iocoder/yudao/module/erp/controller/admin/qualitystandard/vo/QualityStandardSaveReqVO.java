package cn.iocoder.yudao.module.erp.controller.admin.qualitystandard.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - ERP 质检标准新增/修改 Request VO")
@Data
public class QualityStandardSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7038")
    private Long id;

    @Schema(description = "标准编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "标准编号不能为空")
    private String standardNo;

    @Schema(description = "标准名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "标准名称不能为空")
    private String standardName;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29166")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "工序ID", example = "24223")
    private Long processId;

    @Schema(description = "检验类型：1-进料检验，2-过程检验，3-成品检验", example = "1")
    private Integer inspectionType;

    @Schema(description = "标准版本")
    private String standardVersion;

    @Schema(description = "AQL水平")
    private String aqlLevel;

    @Schema(description = "抽样方法")
    private String samplingMethod;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

    @Schema(description = "标准描述", example = "随便")
    private String description;

}