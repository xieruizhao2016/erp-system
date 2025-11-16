package cn.iocoder.yudao.module.erp.controller.admin.qualityitem.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - ERP 质检项目新增/修改 Request VO")
@Data
public class QualityItemSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25866")
    private Long id;

    @Schema(description = "标准ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19958")
    @NotNull(message = "标准ID不能为空")
    private Long standardId;

    @Schema(description = "项目编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "项目编号不能为空")
    private String itemNo;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "项目名称不能为空")
    private String itemName;

    @Schema(description = "项目类型：1-定性，2-定量", example = "1")
    private Integer itemType;

    @Schema(description = "检验方法")
    private String testMethod;

    @Schema(description = "标准值")
    private String standardValue;

    @Schema(description = "公差范围")
    private String tolerance;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "是否关键项")
    private Boolean isKeyItem;

    @Schema(description = "检验序号")
    private Integer sequence;

    @Schema(description = "备注", example = "随便")
    private String remark;

}