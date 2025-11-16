package cn.iocoder.yudao.module.erp.dal.dataobject.productionscheduleitem;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 排程明细 DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_schedule_item")
@KeySequence("erp_production_schedule_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionScheduleItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 排程ID
     */
    private Long scheduleId;
    /**
     * 生产订单ID
     */
    private Long productionOrderId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 排程数量
     */
    private BigDecimal quantity;
    /**
     * 计划开始时间
     */
    private LocalDateTime plannedStartTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime plannedEndTime;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 工序序号
     */
    private Integer processSequence;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 准备时间
     */
    private Integer setupTime;
    /**
     * 运行时间
     */
    private Integer runTime;
    /**
     * 等待时间
     */
    private Integer waitTime;
    /**
     * 状态：1-已计划，2-已下达，3-进行中，4-已完成，5-已延迟
     */
    private Integer status;
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    /**
     * 实际结束时间
     */
    private LocalDateTime actualEndTime;
    /**
     * 完成率
     */
    private BigDecimal completionRate;
    /**
     * 延迟原因
     */
    private String delayReason;


}