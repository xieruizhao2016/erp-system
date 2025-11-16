package cn.iocoder.yudao.module.erp.dal.dataobject.equipmentstatus;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 设备状态记录 DO
 *
 * @author 芋道源码
 */
@TableName("erp_equipment_status")
@KeySequence("erp_equipment_status_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentStatusDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 设备ID
     */
    private Long equipmentId;
    /**
     * 状态：1-运行，2-待机，3-故障，4-维修，5-停机
     */
    private Integer status;
    /**
     * 状态开始时间
     */
    private LocalDateTime statusStartTime;
    /**
     * 状态结束时间
     */
    private LocalDateTime statusEndTime;
    /**
     * 持续时间（分钟）
     */
    private Integer duration;
    /**
     * 关联工单ID
     */
    private Long workOrderId;
    /**
     * 操作员ID
     */
    private Long operatorId;
    /**
     * 备注
     */
    private String remark;


}