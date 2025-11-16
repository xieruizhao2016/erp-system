package cn.iocoder.yudao.module.erp.controller.admin.productionorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 生产订单 DO
 *新增/修改 Request VO")
@Data
public class ProductionOrderSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "26384")
    private Long id;

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "生产订单号不能为空")
    private String no;

    @Schema(description = "客户ID（关联销售订单）", example = "3753")
    private Long customerId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32079")
    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "产品名称不能为空")
    private String productName;

    @Schema(description = "产品规格")
    private String productSpec;

    @Schema(description = "单位ID", example = "16828")
    private Long unitId;

    @Schema(description = "生产数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生产数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "已完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "计划开始时间")
    private LocalDateTime startTime;

    @Schema(description = "计划完成时间")
    private LocalDateTime endTime;

    @Schema(description = "实际开始时间")
    private LocalDateTime actualStartTime;

    @Schema(description = "实际完成时间")
    private LocalDateTime actualEndTime;

    @Schema(description = "状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消不能为空")
    private Integer status;

    @Schema(description = "优先级：1-紧急，2-高，3-中，4-低")
    private Integer priority;

    @Schema(description = "来源类型：1-手动创建，2-销售订单，3-库存补充", example = "1")
    private Integer sourceType;

    @Schema(description = "来源单据ID", example = "10935")
    private Long sourceId;

    @Schema(description = "生产说明", example = "随便")
    private String description;

    @Schema(description = "备注", example = "随便")
    private String remark;

}