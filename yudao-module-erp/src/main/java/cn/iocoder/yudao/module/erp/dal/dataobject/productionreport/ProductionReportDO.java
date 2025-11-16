package cn.iocoder.yudao.module.erp.dal.dataobject.productionreport;

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
 * ERP 生产报表 DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_report")
@KeySequence("erp_production_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionReportDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 报表编号
     */
    private String reportNo;
    /**
     * 报表名称
     */
    private String reportName;
    /**
     * 报表类型：1-日报，2-周报，3-月报，4-年报
     */
    private Integer reportType;
    /**
     * 报表期间
     */
    private String reportPeriod;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 生产订单数
     */
    private Integer productionOrders;
    /**
     * 总计划数量
     */
    private BigDecimal totalPlanQuantity;
    /**
     * 总完成数量
     */
    private BigDecimal totalCompletedQuantity;
    /**
     * 总合格数量
     */
    private BigDecimal totalQualifiedQuantity;
    /**
     * 完成率
     */
    private BigDecimal completionRate;
    /**
     * 合格率
     */
    private BigDecimal qualityRate;
    /**
     * 总工时
     */
    private BigDecimal totalWorkHours;
    /**
     * 总机时
     */
    private BigDecimal totalEquipmentHours;
    /**
     * OEE
     */
    private BigDecimal oee;
    /**
     * 准时交付率
     */
    private BigDecimal onTimeDeliveryRate;
    /**
     * 产值
     */
    private BigDecimal productionValue;
    /**
     * 平均成本
     */
    private BigDecimal averageCost;
    /**
     * 报表日期
     */
    private LocalDate reportDate;
    /**
     * 生成时间
     */
    private LocalDateTime generateTime;
    /**
     * 状态：1-草稿，2-已发布
     */
    private Integer status;
    /**
     * 详细数据（JSON）
     */
    private String reportData;


}