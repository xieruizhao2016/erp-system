package cn.iocoder.yudao.module.erp.controller.admin.stock.internalout.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 内部出库单新增/修改 Request VO")
@Data
public class ErpStockInternalOutSaveReqVO {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "出库单号")
    private String no;

    @Schema(description = "员工编号（关联 system_users.id）")
    private Long employeeId;

    @Schema(description = "部门编号（关联 system_dept.id）")
    private Long deptId;

    @Schema(description = "内部类型（1-部门调拨，2-员工领料，3-生产产品，4-其他）")
    private Integer internalType;

    @Schema(description = "出库时间")
    private LocalDateTime outTime;

    @Schema(description = "合计数量")
    private BigDecimal totalCount;

    @Schema(description = "合计金额，单位：元")
    private BigDecimal totalPrice;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "附件 URL")
    private String fileUrl;

    @Schema(description = "出库项列表")
    @Valid
    private List<Item> items;

    @Data
    public static class Item {

        @Schema(description = "出库项编号", example = "11756")
        private Long id;

        @Schema(description = "仓库编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        @NotNull(message = "仓库编号不能为空")
        private Long warehouseId;

        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "3113")
        @NotNull(message = "产品编号不能为空")
        private Long productId;

        @Schema(description = "产品单价", example = "100.00")
        private BigDecimal productPrice;

        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "100.00")
        @NotNull(message = "产品数量不能为空")
        private BigDecimal count;

        @Schema(description = "备注", example = "随便")
        private String remark;

    }

}