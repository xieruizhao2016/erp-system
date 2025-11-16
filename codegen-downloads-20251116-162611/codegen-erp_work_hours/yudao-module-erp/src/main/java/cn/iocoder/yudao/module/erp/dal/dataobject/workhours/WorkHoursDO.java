package cn.iocoder.yudao.module.erp.dal.dataobject.workhours;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 工时统计 DO
 *
 * @author 芋道源码
 */
@TableName("erp_work_hours")
@KeySequence("erp_work_hours_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkHoursDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工单ID
     */
    private Long workOrderId;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 操作员ID
     */
    private Long operatorId;
    /**
     * 工作日期
     */
    private LocalDate workDate;
    /**
     * 班次ID
     */
    private Long shiftId;
    /**
     * 开始时间
     */
    private LocalTime startTime;
    /**
     * 结束时间
     */
    private LocalTime endTime;
    /**
     * 工作时长（分钟）
     */
    private Integer duration;
    /**
     * 标准工时（分钟）
     */
    private Integer standardDuration;
    /**
     * 加班时长（分钟）
     */
    private Integer overtimeDuration;
    /**
     * 机时
     */
    private BigDecimal machineHours;
    /**
     * 操作员工时费率
     */
    private BigDecimal operatorRate;
    /**
     * 设备时费率
     */
    private BigDecimal machineRate;
    /**
     * 人工成本
     */
    private BigDecimal laborCost;
    /**
     * 设备成本
     */
    private BigDecimal machineCost;
    /**
     * 状态：1-有效，2-无效
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}