package cn.iocoder.yudao.module.erp.controller.admin.finance.receivable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 应收账款新增/修改 Request VO")
@Data
public class ErpFinanceReceivableSaveReqVO {

    @Schema(description = "编号")
    private Long id;

    @Schema(description = "单据号，不填则自动生成")
    private String no;

    @Schema(description = "客户编号（关联 erp_customer.id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "客户编号不能为空")
    private Long customerId;

    @Schema(description = "销售订单编号（关联 erp_sale_order.id），可选，用于手动创建初始应收或没有关联订单的借贷记录")
    private Long orderId;

    @Schema(description = "应收金额，单位：元", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "应收金额不能为空")
    @DecimalMin(value = "0.01", message = "应收金额必须大于0")
    private BigDecimal amount;

    @Schema(description = "已收金额，单位：元")
    private BigDecimal receivedAmount;

    @Schema(description = "余额，单位：元（balance = amount - received_amount）")
    private BigDecimal balance;

    @Schema(description = "到期日")
    private LocalDate dueDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}