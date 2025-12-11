package cn.iocoder.yudao.module.erp.controller.admin.finance-profit-statement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 利润表新增/修改 Request VO")
@Data
public class ErpFinanceProfitStatementSaveReqVO {

    @Schema(description = "期间日期（年月）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "期间日期（年月）不能为空")
    private LocalDate periodDate;

    @Schema(description = "营业收入，单位：元（从销售订单汇总）")
    private BigDecimal revenue;

    @Schema(description = "营业成本，单位：元（从采购订单/生产订单汇总）")
    private BigDecimal cost;

    @Schema(description = "毛利润，单位：元（gross_profit = revenue - cost）")
    private BigDecimal grossProfit;

    @Schema(description = "营业费用，单位：元")
    private BigDecimal operatingExpense;

    @Schema(description = "净利润，单位：元（net_profit = gross_profit - operating_expense）")
    private BigDecimal netProfit;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}