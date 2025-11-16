package cn.iocoder.yudao.module.erp.dal.dataobject.costvariance;

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
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 成本差异分析 DO
 *
 * @author 芋道源码
 */
@TableName("erp_cost_variance")
@KeySequence("erp_cost_variance_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostVarianceDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 实际成本ID
     */
    private Long costActualId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 生产数量
     */
    private BigDecimal productionQuantity;
    /**
     * 标准总成本
     */
    private BigDecimal standardTotalCost;
    /**
     * 实际总成本
     */
    private BigDecimal actualTotalCost;
    /**
     * 总差异
     */
    private BigDecimal totalVariance;
    /**
     * 总差异率
     */
    private BigDecimal totalVarianceRate;
    /**
     * 材料成本差异
     */
    private BigDecimal materialVariance;
    /**
     * 材料差异率
     */
    private BigDecimal materialVarianceRate;
    /**
     * 人工成本差异
     */
    private BigDecimal laborVariance;
    /**
     * 人工差异率
     */
    private BigDecimal laborVarianceRate;
    /**
     * 制造费用差异
     */
    private BigDecimal overheadVariance;
    /**
     * 制造费用差异率
     */
    private BigDecimal overheadVarianceRate;
    /**
     * 差异类型：1-有利，2-不利
     */
    private Integer varianceType;
    /**
     * 分析日期
     */
    private LocalDateTime analysisDate;
    /**
     * 差异原因
     */
    private String varianceReason;
    /**
     * 备注
     */
    private String remark;


}