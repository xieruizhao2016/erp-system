package cn.iocoder.yudao.module.erp.controller.admin.productiondashboardconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 看板配置分页 Request VO")
@Data
public class ProductionDashboardConfigPageReqVO extends PageParam {

    @Schema(description = "配置名称", example = "赵六")
    private String configName;

    @Schema(description = "配置类型：1-大屏，2-PC端，3-移动端", example = "1")
    private Integer configType;

    @Schema(description = "屏幕分辨率")
    private String screenResolution;

    @Schema(description = "布局配置（JSON）")
    private String layoutConfig;

    @Schema(description = "组件配置（JSON）")
    private String componentConfig;

    @Schema(description = "数据刷新间隔（秒）")
    private Integer dataRefreshInterval;

    @Schema(description = "是否默认配置")
    private Boolean isDefault;

    @Schema(description = "是否启用")
    private Boolean isActive;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}