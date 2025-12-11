package cn.iocoder.yudao.module.erp.controller.admin.finance-receivable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 应收账款新增/修改 Request VO")
@Data
public class ErpFinanceReceivableSaveReqVO {

    @Schema(description = "单据号")
    private String no;

    @Schema(description = "客户编号（关联 erp_customer.id）")
    private Long customerId;

    @Schema(description = "销售订单编号（关联 erp_sale_order.id）")
    private Long orderId;

    @Schema(description = "应收金额，单位：元")
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