package cn.iocoder.yudao.module.erp.dal.dataobject.costactual;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 实际成本 DO
 *
 * @author 芋道源码
 */
@TableName("erp_cost_actual")
@KeySequence("erp_cost_actual_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostActualDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 成本单号
     */
    private String costNo;
    /**
     * 工单ID
     */
    private Long workOrderId;
    /**
     * 生产订单ID
     */
    private Long productionOrderId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 生产数量
     */
    private BigDecimal productionQuantity;
    /**
     * 材料成本
     */
    private BigDecimal materialCost;
    /**
     * 材料成本调整
     */
    private BigDecimal materialCostAdjust;
    /**
     * 人工成本
     */
    private BigDecimal laborCost;
    /**
     * 人工成本调整
     */
    private BigDecimal laborCostAdjust;
    /**
     * 制造费用
     */
    private BigDecimal overheadCost;
    /**
     * 制造费用调整
     */
    private BigDecimal overheadCostAdjust;
    /**
     * 总成本
     */
    private BigDecimal totalCost;
    /**
     * 单位成本
     */
    private BigDecimal unitCost;
    /**
     * 成本币种
     */
    private String costCurrency;
    /**
     * 成本期间（YYYY-MM）
     */
    private String costPeriod;
    /**
     * 计算日期
     */
    private LocalDateTime calculationDate;
    /**
     * 最后调整日期
     */
    private LocalDateTime lastAdjustDate;
    /**
     * 状态：1-草稿，2-已计算，3-已确认
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}