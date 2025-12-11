package cn.iocoder.yudao.module.erp.dal.dataobject.finance.receivable;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 应收账款 DO
 *
 * @author 开发团队
 */
@TableName("erp_finance_receivable")
@KeySequence("erp_finance_receivable_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpFinanceReceivableDO extends BaseDO {

    /**
     * 单据号
     */
    private String no;
    /**
     * 客户编号（关联 erp_customer.id）
     */
    private Long customerId;
    /**
     * 销售订单编号（关联 erp_sale_order.id）
     */
    private Long orderId;
    /**
     * 应收金额，单位：元
     */
    private BigDecimal amount;
    /**
     * 已收金额，单位：元
     */
    private BigDecimal receivedAmount;
    /**
     * 余额，单位：元（balance = amount - received_amount）
     */
    private BigDecimal balance;
    /**
     * 到期日
     */
    private LocalDate dueDate;
    /**
     * 状态（10-未审核，20-已审核）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}