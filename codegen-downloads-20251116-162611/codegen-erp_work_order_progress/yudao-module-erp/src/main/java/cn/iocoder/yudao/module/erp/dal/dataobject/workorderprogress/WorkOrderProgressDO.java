package cn.iocoder.yudao.module.erp.dal.dataobject.workorderprogress;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
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
 * ERP 工单进度 DO
 *
 * @author 芋道源码
 */
@TableName("erp_work_order_progress")
@KeySequence("erp_work_order_progress_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderProgressDO extends BaseDO {

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
     * 工序名称
     */
    private String processName;
    /**
     * 工序序号
     */
    private Integer sequence;
    /**
     * 计划开始时间
     */
    private LocalDateTime plannedStartTime;
    /**
     * 计划结束时间
     */
    private LocalDateTime plannedEndTime;
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    /**
     * 实际结束时间
     */
    private LocalDateTime actualEndTime;
    /**
     * 计划数量
     */
    private BigDecimal plannedQuantity;
    /**
     * 完成数量
     */
    private BigDecimal completedQuantity;
    /**
     * 合格数量
     */
    private BigDecimal qualifiedQuantity;
    /**
     * 不合格数量
     */
    private BigDecimal rejectedQuantity;
    /**
     * 报废数量
     */
    private BigDecimal scrapQuantity;
    /**
     * 状态：1-待开始，2-进行中，3-已完成，4-异常
     */
    private Integer status;
    /**
     * 操作员ID
     */
    private Long operatorId;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 实际工时（分钟）
     */
    private Integer workTime;
    /**
     * 停机时间（分钟）
     */
    private Integer downtime;
    /**
     * 质检状态：1-待检，2-合格，3-不合格
     */
    private Integer qualityStatus;
    /**
     * 备注
     */
    private String remark;


}