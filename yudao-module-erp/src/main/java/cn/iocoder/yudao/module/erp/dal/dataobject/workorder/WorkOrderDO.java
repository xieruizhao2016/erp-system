package cn.iocoder.yudao.module.erp.dal.dataobject.workorder;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 工单主 DO
 *
 * @author 芋道源码
 */
@TableName("erp_work_order")
@KeySequence("erp_work_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工单号
     */
    private String workOrderNo;
    /**
     * 生产订单ID
     */
    private Long productionOrderId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 工单数量
     */
    private BigDecimal quantity;
    /**
     * 完成数量
     */
    private BigDecimal completedQuantity;
    /**
     * 合格数量
     */
    private BigDecimal qualifiedQuantity;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 主管ID
     */
    private Long supervisorId;
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
     * 状态：1-已创建，2-已下达，3-进行中，4-已暂停，5-已完成，6-已取消
     */
    private Integer status;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 作业指导书
     */
    private String instruction;
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 总工时（分钟）- 计算字段，不映射到数据库
     * 通过聚合工单进度表的 work_time 字段计算得出
     */
    @TableField(exist = false)
    private Integer totalWorkTime;


}