package cn.iocoder.yudao.module.erp.dal.dataobject.mrpparams;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP MRP参数 DO
 *
 * @author 芋道源码
 */
@TableName("erp_mrp_params")
@KeySequence("erp_mrp_params_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MrpParamsDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 参数名称
     */
    private String paramName;
    /**
     * 参数编码
     */
    private String paramCode;
    /**
     * 参数值
     */
    private String paramValue;
    /**
     * 参数类型：1-字符串，2-数字，3-日期，4-布尔
     */
    private Integer paramType;
    /**
     * 参数描述
     */
    private String description;
    /**
     * 是否系统参数
     */
    private Boolean isSystem;


}