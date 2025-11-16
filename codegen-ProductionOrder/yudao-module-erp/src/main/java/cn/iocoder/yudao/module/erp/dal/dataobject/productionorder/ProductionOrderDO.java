package cn.iocoder.yudao.module.erp.dal.dataobject.productionorder;

import lombok.*;
import java.util.*;
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

/**
 * ERP 生产订单 DO DO
 *
 * @author 芋道源码
 */
@TableName("erp_production_order")
@KeySequence("erp_production_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOrderDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 生产订单号
     */
    private String no;
    /**
     * 客户ID（关联销售订单）
     */
    private Long customerId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品规格
     */
    private String productSpec;
    /**
     * 单位ID
     */
    private Long unitId;
    /**
     * 生产数量
     */
    private BigDecimal quantity;
    /**
     * 已完成数量
     */
    private BigDecimal completedQuantity;
    /**
     * 计划开始时间
     */
    private LocalDateTime startTime;
    /**
     * 计划完成时间
     */
    private LocalDateTime endTime;
    /**
     * 实际开始时间
     */
    private LocalDateTime actualStartTime;
    /**
     * 实际完成时间
     */
    private LocalDateTime actualEndTime;
    /**
     * 状态：1-待开始，2-进行中，3-已暂停，4-已完成，5-已取消
     */
    private Integer status;
    /**
     * 优先级：1-紧急，2-高，3-中，4-低
     */
    private Integer priority;
    /**
     * 来源类型：1-手动创建，2-销售订单，3-库存补充
     */
    private Integer sourceType;
    /**
     * 来源单据ID
     */
    private Long sourceId;
    /**
     * 生产说明
     */
    private String description;
    /**
     * 备注
     */
    private String remark;


}