package cn.iocoder.yudao.module.erp.dal.dataobject.productbom;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.time.LocalDate;

/**
 * ERP BOM主 DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_bom")
@KeySequence("erp_product_bom_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBomDO extends BaseDO {

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
     * BOM编号
     */
    private String bomNo;
    /**
     * BOM名称
     */
    private String bomName;
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
     * BOM类型：1-生产BOM，2-设计BOM，3-工艺BOM
     */
    private Integer bomType;
    /**
     * 标准成本
     */
    private BigDecimal standardCost;
    /**
     * 总材料成本
     */
    private BigDecimal totalMaterialCost;
    /**
     * 状态：1-草稿，2-生效，3-失效
     */
    private Integer status;


}