package cn.iocoder.yudao.module.erp.dal.dataobject.qualityitem;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 质检项目 DO
 *
 * @author 芋道源码
 */
@TableName("erp_quality_item")
@KeySequence("erp_quality_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 标准ID
     */
    private Long standardId;
    /**
     * 项目编号
     */
    private String itemNo;
    /**
     * 项目名称
     */
    private String itemName;
    /**
     * 项目类型：1-定性，2-定量
     */
    private Integer itemType;
    /**
     * 检验方法
     */
    private String testMethod;
    /**
     * 标准值
     */
    private String standardValue;
    /**
     * 公差范围
     */
    private String tolerance;
    /**
     * 单位
     */
    private String unit;
    /**
     * 是否关键项
     */
    private Boolean isKeyItem;
    /**
     * 检验序号
     */
    private Integer sequence;
    /**
     * 备注
     */
    private String remark;


}