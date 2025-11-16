package cn.iocoder.yudao.module.erp.controller.admin.processroute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - ERP 工艺路线主 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProcessRouteRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7310")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工艺路线编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工艺路线编号")
    private String routeNo;

    @Schema(description = "工艺路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("工艺路线名称")
    private String routeName;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28566")
    @ExcelProperty("产品ID")
    private Long productId;

    @Schema(description = "版本号")
    @ExcelProperty("版本号")
    private String version;

    @Schema(description = "标准周期时间（分钟）")
    @ExcelProperty("标准周期时间（分钟）")
    private Integer standardCycleTime;

    @Schema(description = "标准人工成本")
    @ExcelProperty("标准人工成本")
    private BigDecimal standardLaborCost;

    @Schema(description = "标准制造费用")
    @ExcelProperty("标准制造费用")
    private BigDecimal standardOverheadCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    @ExcelProperty("状态：1-草稿，2-生效，3-失效")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}