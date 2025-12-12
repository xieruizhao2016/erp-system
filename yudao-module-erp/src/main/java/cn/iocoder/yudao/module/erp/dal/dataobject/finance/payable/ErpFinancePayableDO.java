package cn.iocoder.yudao.module.erp.dal.dataobject.finance.payable;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 应付账款 DO
 *
 * @author 开发团队
 */
@TableName("erp_finance_payable")
@KeySequence("erp_finance_payable_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpFinancePayableDO extends BaseDO {

    /**
     * 单据号
     */
    private String no;
    /**
     * 供应商编号（关联 erp_supplier.id）
     */
    private Long supplierId;
    /**
     * 采购订单编号（关联 erp_purchase_order.id）
     */
    private Long orderId;
    /**
     * 应付金额，单位：元
     */
    private BigDecimal amount;
    /**
     * 已付金额，单位：元
     */
    private BigDecimal paidAmount;
    /**
     * 余额，单位：元（balance = amount - paid_amount）
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