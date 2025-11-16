package cn.iocoder.yudao.module.erp.dal.dataobject.productionkpi;

import lombok.*;
import java.util.*;
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

/**
 * ERP 生产KPI DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_kpi")
@KeySequence("erp_production_kpi_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionKpiDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * KPI编号
     */
    private String kpiNo;
    /**
     * KPI名称
     */
    private String kpiName;
    /**
     * KPI类型：1-OEE，2-合格率，3-达成率，4-交期率
     */
    private Integer kpiType;
    /**
     * 分类：1-效率，2-质量，3-交付，4-成本
     */
    private Integer category;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 统计周期：1-小时，2-天，3-周，4-月
     */
    private Integer measurementPeriod;
    /**
     * 目标值
     */
    private BigDecimal targetValue;
    /**
     * 实际值
     */
    private BigDecimal actualValue;
    /**
     * 差异值
     */
    private BigDecimal variance;
    /**
     * 差异率
     */
    private BigDecimal varianceRate;
    /**
     * 计算日期
     */
    private LocalDateTime calculationDate;
    /**
     * 周期开始时间
     */
    private LocalDateTime periodStartTime;
    /**
     * 周期结束时间
     */
    private LocalDateTime periodEndTime;
    /**
     * 数据来源
     */
    private String dataSource;
    /**
     * 备注
     */
    private String remarks;


}