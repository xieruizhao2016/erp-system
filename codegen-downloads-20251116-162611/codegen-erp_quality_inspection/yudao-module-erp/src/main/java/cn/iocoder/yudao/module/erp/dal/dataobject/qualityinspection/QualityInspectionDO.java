package cn.iocoder.yudao.module.erp.dal.dataobject.qualityinspection;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 质检记录 DO
 *
 * @author 芋道源码
 */
@TableName("erp_quality_inspection")
@KeySequence("erp_quality_inspection_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QualityInspectionDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 检验单号
     */
    private String inspectionNo;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 工序ID
     */
    private Long processId;
    /**
     * 工单ID
     */
    private Long workOrderId;
    /**
     * 检验类型：1-进料检验，2-过程检验，3-成品检验
     */
    private Integer inspectionType;
    /**
     * 检验级别：1-全检，2-抽检
     */
    private Integer inspectionLevel;
    /**
     * 批量大小
     */
    private Integer lotSize;
    /**
     * 样本数量
     */
    private Integer sampleSize;
    /**
     * 合格数量
     */
    private Integer qualifiedQuantity;
    /**
     * 不合格数量
     */
    private Integer rejectedQuantity;
    /**
     * 报废数量
     */
    private Integer scrapQuantity;
    /**
     * 检验结果：1-合格，2-不合格，3-待复检
     */
    private Integer inspectionResult;
    /**
     * 检验员ID
     */
    private Long inspectorId;
    /**
     * 检验时间
     */
    private LocalDateTime inspectionTime;
    /**
     * 检验环境
     */
    private String environment;
    /**
     * 检验设备
     */
    private String equipment;
    /**
     * 检验备注
     */
    private String remark;


}