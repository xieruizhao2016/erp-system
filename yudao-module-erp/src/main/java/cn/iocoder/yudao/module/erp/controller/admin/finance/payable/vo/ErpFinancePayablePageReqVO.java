package cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - 应付账款分页 Request VO")
@Data
public class ErpFinancePayablePageReqVO extends PageParam {

    @Schema(description = "单据号")
    private String no;

    @Schema(description = "供应商编号（关联 erp_supplier.id）")
    private Long supplierId;

    @Schema(description = "采购订单编号（关联 erp_purchase_order.id）")
    private Long orderId;

    @Schema(description = "应付金额，单位：元")
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