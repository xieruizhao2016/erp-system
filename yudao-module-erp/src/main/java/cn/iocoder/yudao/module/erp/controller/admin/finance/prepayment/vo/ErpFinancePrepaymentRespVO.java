package cn.iocoder.yudao.module.erp.controller.admin.finance-prepayment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 预付款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpFinancePrepaymentRespVO {

    @Schema(description = "单据号")
    @ExcelProperty("单据号")
    private String no;

    @Schema(description = "供应商编号（关联 erp_supplier.id）")
    @ExcelProperty("供应商编号（关联 erp_supplier.id）")
    private Long supplierId;

    @Schema(description = "采购订单编号（关联 erp_purchase_order.id，可选）")
    @ExcelProperty("采购订单编号（关联 erp_purchase_order.id，可选）")
    private Long orderId;

    @Schema(description = "预付款金额，单位：元")
    @ExcelProperty("预付款金额，单位：元")
    private BigDecimal amount;

    @Schema(description = "已使用金额，单位：元")
    @ExcelProperty("已使用金额，单位：元")
    private BigDecimal usedAmount;

    @Schema(description = "余额，单位：元（balance = amount - used_amount）")
    @ExcelProperty("余额，单位：元（balance = amount - used_amount）")
    private BigDecimal balance;

    @Schema(description = "预付日期")
    @ExcelProperty("预付日期")
    private LocalDate prepayDate;

    @Schema(description = "状态（10-未审核，20-已审核）")
    @ExcelProperty("状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

}