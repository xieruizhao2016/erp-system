package cn.iocoder.yudao.module.erp.dal.dataobject.equipment;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 设备台账 DO
 *
 * @author 芋道源码
 */
@TableName("erp_equipment")
@KeySequence("erp_equipment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 设备编号
     */
    private String equipmentNo;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     * 设备类型
     */
    private String equipmentType;
    /**
     * 设备型号
     */
    private String model;
    /**
     * 制造商
     */
    private String manufacturer;
    /**
     * 序列号
     */
    private String serialNumber;
    /**
     * 购置日期
     */
    private LocalDate purchaseDate;
    /**
     * 购置价格
     */
    private BigDecimal purchasePrice;
    /**
     * 设计寿命（年）
     */
    private Integer serviceLife;
    /**
     * 工作中心ID
     */
    private Long workCenterId;
    /**
     * 设备位置
     */
    private String location;
    /**
     * 产能（小时/天）
     */
    private BigDecimal capacity;
    /**
     * 效率系数
     */
    private BigDecimal efficiencyRate;
    /**
     * 状态：1-正常，2-维修中，3-故障，4-报废
     */
    private Integer status;
    /**
     * 责任人
     */
    private String responsiblePerson;
    /**
     * 技术规格
     */
    private String specification;
    /**
     * 备注
     */
    private String remark;


}