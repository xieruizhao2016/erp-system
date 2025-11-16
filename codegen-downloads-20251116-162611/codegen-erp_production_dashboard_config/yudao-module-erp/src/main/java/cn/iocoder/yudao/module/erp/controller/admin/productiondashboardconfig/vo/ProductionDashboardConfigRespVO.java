package cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 看板配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionDashboardConfigRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30451")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("配置名称")
    private String configName;

    @Schema(description = "配置类型：1-大屏，2-PC端，3-移动端", example = "1")
    @ExcelProperty("配置类型：1-大屏，2-PC端，3-移动端")
    private Integer configType;

    @Schema(description = "屏幕分辨率")
    @ExcelProperty("屏幕分辨率")
    private String screenResolution;

    @Schema(description = "布局配置（JSON）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("布局配置（JSON）")
    private String layoutConfig;

    @Schema(description = "组件配置（JSON）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件配置（JSON）")
    private String componentConfig;

    @Schema(description = "数据刷新间隔（秒）")
    @ExcelProperty("数据刷新间隔（秒）")
    private Integer dataRefreshInterval;

    @Schema(description = "是否默认配置")
    @ExcelProperty("是否默认配置")
    private Boolean isDefault;

    @Schema(description = "是否启用")
    @ExcelProperty("是否启用")
    private Boolean isActive;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}