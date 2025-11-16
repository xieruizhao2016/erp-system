package cn.iocoder.yudao.module.erp.controller.admin.processroute.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 工艺路线主分页 Request VO")
@Data
public class ProcessRoutePageReqVO extends PageParam {

    @Schema(description = "工艺路线编号")
    private String routeNo;

    @Schema(description = "工艺路线名称", example = "赵六")
    private String routeName;

    @Schema(description = "产品ID", example = "28566")
    private Long productId;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "标准周期时间（分钟）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Integer[] standardCycleTime;

    @Schema(description = "标准人工成本")
    private BigDecimal standardLaborCost;

    @Schema(description = "标准制造费用")
    private BigDecimal standardOverheadCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}