package cn.iocoder.yudao.module.erp.controller.admin.finance.payable.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 应付账款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpFinancePayableRespVO {

    @Schema(description = "单据号")
    @ExcelProperty("单据号")
    private String no;

    @Schema(description = "供应商编号（关联 erp_supplier.id）")
    @ExcelProperty("供应商编号（关联 erp_supplier.id）")
    private Long supplierId;

    @Schema(description = "采购订单编号（关联 erp_purchase_order.id）")
    @ExcelProperty("采购订单编号（关联 erp_purchase_order.id）")
    private Long orderId;

    @Schema(description = "应付金额，单位：元")
    @ExcelProperty("应付金额，单位：元")
    private BigDecimal amount;

    @Schema(description = "已付金额，单位：元")
    @ExcelProperty("已付金额，单位：元")
    private BigDecimal paidAmount;

    @Schema(description = "余额，单位：元（balance = amount - paid_amount）")
    @ExcelProperty("余额，单位：元（balance = amount - paid_amount）")
    private BigDecimal balance;

    @Schema(description = "到期日")
    @ExcelProperty("到期日")
    private LocalDate dueDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    @ExcelProperty("状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

}