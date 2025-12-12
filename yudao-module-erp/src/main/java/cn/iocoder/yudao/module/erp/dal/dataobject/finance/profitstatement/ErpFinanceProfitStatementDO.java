package cn.iocoder.yudao.module.erp.dal.dataobject.finance.profitstatement;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 利润表 DO
 *
 * @author 开发团队
 */
@TableName("erp_finance_profit_statement")
@KeySequence("erp_finance_profit_statement_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpFinanceProfitStatementDO extends BaseDO {

    /**
     * 期间日期（年月）
     */
    private LocalDate periodDate;
    /**
     * 营业收入，单位：元（从销售订单汇总）
     */
    private BigDecimal revenue;
    /**
     * 营业成本，单位：元（从采购订单/生产订单汇总）
     */
    private BigDecimal cost;
    /**
     * 毛利润，单位：元（gross_profit = revenue - cost）
     */
    private BigDecimal grossProfit;
    /**
     * 营业费用，单位：元
     */
    private BigDecimal operatingExpense;
    /**
     * 净利润，单位：元（net_profit = gross_profit - operating_expense）
     */
    private BigDecimal netProfit;
    /**
     * 状态（10-未审核，20-已审核）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}