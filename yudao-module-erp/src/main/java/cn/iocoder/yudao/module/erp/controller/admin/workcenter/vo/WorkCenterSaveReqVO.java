package cn.iocoder.yudao.module.erp.controller.admin.workcenter.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - ERP 工作中心新增/修改 Request VO")
@Data
public class WorkCenterSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "工作中心编号（不填则自动生成）")
    private String workCenterNo;

    @Schema(description = "工作中心名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "加工中心")
    @NotEmpty(message = "工作中心名称不能为空")
    private String workCenterName;

    @Schema(description = "描述", example = "主要负责机械加工")
    private String description;

    @Schema(description = "位置", example = "1号车间")
    private String location;

    @Schema(description = "负责人", example = "张三")
    private String responsiblePerson;

    @Schema(description = "状态：1-启用，2-停用", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

}

