package cn.iocoder.yudao.module.erp.controller.admin.processroute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 工艺路线主新增/修改 Request VO")
@Data
public class ProcessRouteSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "7310")
    private Long id;

    @Schema(description = "工艺路线编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "工艺路线编号不能为空")
    private String routeNo;

    @Schema(description = "工艺路线名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "工艺路线名称不能为空")
    private String routeName;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28566")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "版本号")
    private String version;

    @Schema(description = "标准周期时间（分钟）")
    private Integer standardCycleTime;

    @Schema(description = "标准人工成本")
    private BigDecimal standardLaborCost;

    @Schema(description = "标准制造费用")
    private BigDecimal standardOverheadCost;

    @Schema(description = "状态：1-草稿，2-生效，3-失效", example = "2")
    private Integer status;

}