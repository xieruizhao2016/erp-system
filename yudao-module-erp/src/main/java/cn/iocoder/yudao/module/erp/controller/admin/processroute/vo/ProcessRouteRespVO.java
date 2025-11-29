package cn.iocoder.yudao.module.erp.controller.admin.processroute.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import cn.idev.excel.annotation.*;
import java.time.LocalDate;

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

    @Schema(description = "工艺路线明细列表")
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "明细编号", example = "1")
        private Long id;

        @Schema(description = "工序ID", example = "1")
        private Long processId;

        @Schema(description = "序号", example = "1")
        private Integer sequence;

        @Schema(description = "工序名称", example = "切割")
        private String operationName;

        @Schema(description = "标准工时（分钟）", example = "30")
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
        private BigDecimal laborRate;

        @Schema(description = "制造费率")
        private BigDecimal overheadRate;

        @Schema(description = "是否瓶颈工序", example = "false")
        private Boolean isBottleneck;

        @Schema(description = "是否需要质检", example = "false")
        private Boolean qualityCheckRequired;

        @Schema(description = "备注")
        private String remark;

    }

}