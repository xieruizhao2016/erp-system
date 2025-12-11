package cn.iocoder.yudao.module.erp.controller.admin.finance-prepayment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 预付款分页 Request VO")
@Data
public class ErpFinancePrepaymentPageReqVO extends PageParam {

    @Schema(description = "单据号")
    private String no;

    @Schema(description = "供应商编号（关联 erp_supplier.id）")
    private Long supplierId;

    @Schema(description = "采购订单编号（关联 erp_purchase_order.id，可选）")
    private Long orderId;

    @Schema(description = "预付款金额，单位：元")
    private BigDecimal amount;

    @Schema(description = "已使用金额，单位：元")
    private BigDecimal usedAmount;

    @Schema(description = "余额，单位：元（balance = amount - used_amount）")
    private BigDecimal balance;

    @Schema(description = "预付日期")
    private LocalDate prepayDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}