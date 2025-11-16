package cn.iocoder.yudao.module.erp.controller.admin.mrpparams.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - ERP MRP参数新增/修改 Request VO")
@Data
public class MrpParamsSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "29606")
    private Long id;

    @Schema(description = "参数名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "参数名称不能为空")
    private String paramName;

    @Schema(description = "参数编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "参数编码不能为空")
    private String paramCode;

    @Schema(description = "参数值", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "参数值不能为空")
    private String paramValue;

    @Schema(description = "参数类型：1-字符串，2-数字，3-日期，4-布尔", example = "1")
    private Integer paramType;

    @Schema(description = "参数描述", example = "你说的对")
    private String description;

    @Schema(description = "是否系统参数")
    private Boolean isSystem;

}