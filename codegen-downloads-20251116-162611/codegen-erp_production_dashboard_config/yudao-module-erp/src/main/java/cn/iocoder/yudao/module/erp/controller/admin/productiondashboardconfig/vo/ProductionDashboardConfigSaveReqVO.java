package cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - ERP 看板配置新增/修改 Request VO")
@Data
public class ProductionDashboardConfigSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30451")
    private Long id;

    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "配置名称不能为空")
    private String configName;

    @Schema(description = "配置类型：1-大屏，2-PC端，3-移动端", example = "1")
    private Integer configType;

    @Schema(description = "屏幕分辨率")
    private String screenResolution;

    @Schema(description = "布局配置（JSON）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "布局配置（JSON）不能为空")
    private String layoutConfig;

    @Schema(description = "组件配置（JSON）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "组件配置（JSON）不能为空")
    private String componentConfig;

    @Schema(description = "数据刷新间隔（秒）")
    private Integer dataRefreshInterval;

    @Schema(description = "是否默认配置")
    private Boolean isDefault;

    @Schema(description = "是否启用")
    private Boolean isActive;

}