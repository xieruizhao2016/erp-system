package cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspectionitem;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 质检明细 DO
 *
 * @author 芋道源码
 */
@TableName("erp_quality_inspection_item")
@KeySequence("erp_quality_inspection_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityInspectionItemDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 检验ID
     */
    private Long inspectionId;
    /**
     * 检验项目ID
     */
    private Long itemId;
    /**
     * 样本编号
     */
    private String sampleNo;
    /**
     * 测量值
     */
    private String measuredValue;
    /**
     * 实际数值
     */
    private BigDecimal actualValue;
    /**
     * 检验结果：1-合格，2-不合格，3-超差
     */
    private Integer testResult;
    /**
     * 缺陷类型
     */
    private String defectType;
    /**
     * 缺陷描述
     */
    private String defectDescription;
    /**
     * 缺陷图片URLs
     */
    private String imageUrls;
    /**
     * 检验员ID
     */
    private Long inspectorId;
    /**
     * 检验时间
     */
    private LocalDateTime inspectionTime;
    /**
     * 备注
     */
    private String remark;


}