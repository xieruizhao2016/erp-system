package cn.iocoder.yudao.module.erp.dal.dataobject.finance-balance-sheet;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 资产负债表 DO
 *
 * @author 开发团队
 */
@TableName("erp_finance_balance_sheet")
@KeySequence("erp_finance_balance_sheet_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpFinanceBalanceSheetDO extends BaseDO {

    /**
     * 期间日期（年月）
     */
    private LocalDate periodDate;
    /**
     * 资产总计，单位：元
     */
    private BigDecimal assetTotal;
    /**
     * 负债总计，单位：元
     */
    private BigDecimal liabilityTotal;
    /**
     * 所有者权益总计，单位：元
     */
    private BigDecimal equityTotal;
    /**
     * 状态（10-未审核，20-已审核）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}