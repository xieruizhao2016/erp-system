package cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 应付账款新增/修改 Request VO")
@Data
public class ErpFinancePayableSaveReqVO {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "单据号，不填则自动生成")
    private String no;

    @Schema(description = "供应商编号（关联 erp_supplier.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供应商编号不能为空")
    private Long supplierId;

    @Schema(description = "采购订单编号（关联 erp_purchase_order.id），可选，用于手动创建初始应付或没有关联订单的借贷记录")
    private Long orderId;

    @Schema(description = "应付金额，单位：元", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "应付金额不能为空")
    @DecimalMin(value = "0.01", message = "应付金额必须大于0")
    private BigDecimal amount;

    @Schema(description = "已付金额，单位：元")
    private BigDecimal paidAmount;

    @Schema(description = "余额，单位：元（balance = amount - paid_amount）")
    private BigDecimal balance;

    @Schema(description = "到期日")
    private LocalDate dueDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}