package cn.iocoder.yudao.module.erp.controller.admin.finance.prereceipt.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 预收款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpFinancePrereceiptRespVO {

    @Schema(description = "单据号")
    @ExcelProperty("单据号")
    private String no;

    @Schema(description = "客户编号（关联 erp_customer.id）")
    @ExcelProperty("客户编号（关联 erp_customer.id）")
    private Long customerId;

    @Schema(description = "销售订单编号（关联 erp_sale_order.id，可选）")
    @ExcelProperty("销售订单编号（关联 erp_sale_order.id，可选）")
    private Long orderId;

    @Schema(description = "预收款金额，单位：元")
    @ExcelProperty("预收款金额，单位：元")
    private BigDecimal amount;

    @Schema(description = "已使用金额，单位：元")
    @ExcelProperty("已使用金额，单位：元")
    private BigDecimal usedAmount;

    @Schema(description = "余额，单位：元（balance = amount - used_amount）")
    @ExcelProperty("余额，单位：元（balance = amount - used_amount）")
    private BigDecimal balance;

    @Schema(description = "预收日期")
    @ExcelProperty("预收日期")
    private LocalDate prereceiptDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    @ExcelProperty("状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

}