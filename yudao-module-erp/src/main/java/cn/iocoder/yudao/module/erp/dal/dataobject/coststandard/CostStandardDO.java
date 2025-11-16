package cn.iocoder.yudao.module.erp.dal.dataobject.coststandard;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP 标准成本 DO
 *
 * @author 芋道源码
 */
@TableName("erp_cost_standard")
@KeySequence("erp_cost_standard_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostStandardDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 版本号
     */
    private String version;
    /**
     * 生效日期
     */
    private LocalDateTime effectiveDate;
    /**
     * 失效日期
     */
    private LocalDateTime expireDate;
    /**
     * 材料成本
     */
    private BigDecimal materialCost;
    /**
     * 人工成本
     */
    private BigDecimal laborCost;
    /**
     * 制造费用
     */
    private BigDecimal overheadCost;
    /**
     * 总成本
     */
    private BigDecimal totalCost;
    /**
     * 成本币种
     */
    private String costCurrency;
    /**
     * 计算日期
     */
    private LocalDateTime calculationDate;
    /**
     * 关联BOM版本
     */
    private String bomVersion;
    /**
     * 关联工艺版本
     */
    private String routeVersion;
    /**
     * 状态：1-草稿，2-生效，3-失效
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;


}