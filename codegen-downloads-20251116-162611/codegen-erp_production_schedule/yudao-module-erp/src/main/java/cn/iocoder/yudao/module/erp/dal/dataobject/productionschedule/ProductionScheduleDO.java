package cn.iocoder.yudao.module.erp.dal.dataobject.productionschedule;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 生产排程主 DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_schedule")
@KeySequence("erp_production_schedule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionScheduleDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 排程单号
     */
    private String scheduleNo;
    /**
     * 排程名称
     */
    private String scheduleName;
    /**
     * 排程类型：1-主排程，2-详细排程
     */
    private Integer scheduleType;
    /**
     * 计划天数
     */
    private Integer planningHorizonDays;
    /**
     * 计划开始日期
     */
    private LocalDate startDate;
    /**
     * 计划结束日期
     */
    private LocalDate endDate;
    /**
     * 状态：1-草稿，2-已发布，3-执行中，4-已完成
     */
    private Integer status;
    /**
     * 总订单数
     */
    private Integer totalOrders;
    /**
     * 总数量
     */
    private BigDecimal totalQuantity;
    /**
     * 总工时
     */
    private Integer totalWorkHours;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 更新人
     */
    private String updatedBy;


}