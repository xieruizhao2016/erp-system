package cn.iocoder.yudao.module.erp.dal.dataobject.process;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 工序 DO
 *
 * @author 芋道源码
 */
@TableName("erp_process")
@KeySequence("erp_process_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 工序编号
     */
    private String processNo;
    /**
     * 工序名称
     */
    private String processName;
    /**
     * 工序类型：1-加工，2-装配，3-检验，4-包装，5-其他
     */
    private Integer processType;
    /**
     * 工序分类
     */
    private String category;
    /**
     * 标准工时（分钟）
     */
    private Integer standardTime;
    /**
     * 准备时间（分钟）
     */
    private Integer setupTime;
    /**
     * 标准人员数量
     */
    private Integer workerCount;
    /**
     * 设备类型
     */
    private String equipmentType;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 人工费率（元/小时）
     */
    private BigDecimal laborRate;
    /**
     * 制造费率（元/小时）
     */
    private BigDecimal overheadRate;
    /**
     * 是否需要质检
     */
    private Boolean qualityCheckRequired;
    /**
     * 是否瓶颈工序
     */
    private Boolean isBottleneck;
    /**
     * 工序描述
     */
    private String description;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态：1-启用，2-停用
     */
    private Integer status;

}



