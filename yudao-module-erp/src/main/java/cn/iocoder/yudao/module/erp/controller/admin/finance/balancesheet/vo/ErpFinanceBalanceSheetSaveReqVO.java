package cn.iocoder.yudao.module.erp.controller.admin.finance.balancesheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 资产负债表新增/修改 Request VO")
@Data
public class ErpFinanceBalanceSheetSaveReqVO {

    @Schema(description = "期间日期（年月）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "期间日期（年月）不能为空")
    private LocalDate periodDate;

    @Schema(description = "资产总计，单位：元")
    private BigDecimal assetTotal;

    @Schema(description = "负债总计，单位：元")
    private BigDecimal liabilityTotal;

    @Schema(description = "所有者权益总计，单位：元")
    private BigDecimal equityTotal;

    @Schema(description = "状态（10-未审核，20-已审核）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}