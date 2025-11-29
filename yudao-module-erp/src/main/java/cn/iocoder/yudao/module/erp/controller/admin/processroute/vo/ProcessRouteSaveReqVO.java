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

    @Schema(description = "工艺路线明细列表")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "明细编号", example = "1")
        private Long id;

        @Schema(description = "工序ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "工序ID不能为空")
        private Long processId;

        @Schema(description = "序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
        @NotNull(message = "序号不能为空")
        private Integer sequence;

        @Schema(description = "工序名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "切割")
        @NotEmpty(message = "工序名称不能为空")
        private String operationName;

        @Schema(description = "标准工时（分钟）", requiredMode = Schema.RequiredMode.REQUIRED, example = "30")
        @NotNull(message = "标准工时不能为空")
        private Integer standardTime;

        @Schema(description = "准备时间（分钟）", example = "5")
        private Integer setupTime;

        @Schema(description = "人员数量", example = "2")
        private Integer workerCount;

        @Schema(description = "设备ID", example = "1")
        private Long equipmentId;

        @Schema(description = "工作中心ID", example = "1")
        private Long workCenterId;

        @Schema(description = "人工费率")
        private java.math.BigDecimal laborRate;

        @Schema(description = "制造费率")
        private java.math.BigDecimal overheadRate;

        @Schema(description = "是否瓶颈工序", example = "false")
        private Boolean isBottleneck;

        @Schema(description = "是否需要质检", example = "false")
        private Boolean qualityCheckRequired;

        @Schema(description = "备注")
        private String remark;

    }

}