package cn.iocoder.yudao.module.erp.dal.dataobject.qualitystandard;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 质检标准 DO
 *
 * @author 芋道源码
 */
@TableName("erp_quality_standard")
@KeySequence("erp_quality_standard_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityStandardDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 标准编号
     */
    private String standardNo;
    /**
     * 标准名称
     */
    private String standardName;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 检验类型：1-进料检验，2-过程检验，3-成品检验
     */
    private Integer inspectionType;
    /**
     * 标准版本
     */
    private String standardVersion;
    /**
     * AQL水平
     */
    private String aqlLevel;
    /**
     * 抽样方法
     */
    private String samplingMethod;
    /**
     * 状态：1-草稿，2-生效，3-失效
     */
    private Integer status;
    /**
     * 标准描述
     */
    private String description;


}