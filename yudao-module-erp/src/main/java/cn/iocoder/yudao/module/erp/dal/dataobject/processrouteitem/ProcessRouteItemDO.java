package cn.iocoder.yudao.module.erp.dal.dataobject.processrouteitem;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 工艺路线明细 DO
 *
 * @author 芋道源码
 */
@TableName("erp_process_route_item")
@KeySequence("erp_process_route_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessRouteItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工艺路线ID
     */
    private Long routeId;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 序号
     */
    private Integer sequence;
    /**
     * 工序名称
     */
    private String operationName;
    /**
     * 标准工时（分钟）
     */
    private Integer standardTime;
    /**
     * 准备时间（分钟）
     */
    private Integer setupTime;
    /**
     * 人员数量
     */
    private Integer workerCount;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 人工费率
     */
    private BigDecimal laborRate;
    /**
     * 制造费率
     */
    private BigDecimal overheadRate;
    /**
     * 是否瓶颈工序
     */
    private Boolean isBottleneck;
    /**
     * 是否需要质检
     */
    private Boolean qualityCheckRequired;
    /**
     * 备注
     */
    private String remark;


}