package cn.iocoder.yudao.module.erp.controller.admin.finance.profitstatement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import cn.idev.excel.annotation.*;

@Schema(description = "管理后台 - 利润表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpFinanceProfitStatementRespVO {

    @Schema(description = "期间日期（年月）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("期间日期（年月）")
    private LocalDate periodDate;

    @Schema(description = "营业收入，单位：元（从销售订单汇总）")
    @ExcelProperty("营业收入，单位：元（从销售订单汇总）")
    private BigDecimal revenue;

    @Schema(description = "营业成本，单位：元（从采购订单/生产订单汇总）")
    @ExcelProperty("营业成本，单位：元（从采购订单/生产订单汇总）")
    private BigDecimal cost;

    @Schema(description = "毛利润，单位：元（gross_profit = revenue - cost）")
    @ExcelProperty("毛利润，单位：元（gross_profit = revenue - cost）")
    private BigDecimal grossProfit;

    @Schema(description = "营业费用，单位：元")
    @ExcelProperty("营业费用，单位：元")
    private BigDecimal operatingExpense;

    @Schema(description = "净利润，单位：元（net_profit = gross_profit - operating_expense）")
    @ExcelProperty("净利润，单位：元（net_profit = gross_profit - operating_expense）")
    private BigDecimal netProfit;

    @Schema(description = "状态（10-未审核，20-已审核）")
    @ExcelProperty("状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

}